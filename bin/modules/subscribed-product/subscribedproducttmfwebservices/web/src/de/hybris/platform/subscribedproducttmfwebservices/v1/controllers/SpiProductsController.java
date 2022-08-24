/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.controllers;


import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.model.SpiProductBundleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiPaginationService;
import de.hybris.platform.subscribedproductservices.services.SpiProductService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.api.ProductApi;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Error;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.security.SpiAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.subscribedproducttmfwebservices.v1.validators.SpiCreateProductValidator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ProductApi}.
 *
 * @since 2111
 */
@Controller
@Api(tags = "Subscribed Product Inventory")
public class SpiProductsController extends SpiBaseController implements ProductApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private HttpServletRequest request;

	@Resource(name = "spiProductService")
	private SpiProductService spiProductService;

	@Resource(name = "spiPaginationService")
	private SpiPaginationService spiPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "spiCreateProductValidator")
	private SpiCreateProductValidator spiCreateProductValidator;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Autowired
	public SpiProductsController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "Retrieves a Product by ID", nickname = "retrieveProduct", notes = "This operation retrieves a Product entity. Attribute selection is enabled for all first level attributes.", response = Product.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Product.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SpiAuthorizedResourceOwnerOrAdmin
	@RequestMapping(value = "/product/{id}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<Product> retrieveProduct(@ApiParam(value = "Identifier of the Product", required = true) @PathVariable(
			"id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		final SpiProductModel product = spiProductService.getProduct(id);

		final Product productDto = getDataMapper().map(product, Product.class, fields);
		return new ResponseEntity(productDto, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Creates a Product", nickname = "createProduct", notes = "This operation creates a Product entity.", response = Product.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Product.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/product", produces = { "application/json;charset=utf-8" }, consumes = {
			"application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@Secured("ROLE_TRUSTED_CLIENT")
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<Product> createProduct(
			@ApiParam(value = "The Product to be created", required = true) @Valid @RequestBody Product product)
	{
		if (product == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		final String errorMessage = validate(product, "[product]", getSpiCreateProductValidator());
		if (StringUtils.isNotEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		setProductDefaultValues(product);

		final SpiProductModel productModel = getSpiProductService()
				.createProduct(Boolean.TRUE.equals(product.isIsBundle()) ? SpiProductBundleModel.class :
						SpiProductComponentModel.class);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(product, productModel);
				getSpiProductService().saveProduct(productModel);
			}
		});

		final SpiProductModel productCreated = getSpiProductService().getProduct(product.getId());
		final Product productCreatedDto = getDataMapper().map(productCreated, Product.class);
		return new ResponseEntity(productCreatedDto, HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Deletes a Product", nickname = "deleteProduct", notes = "This operation deletes a Product entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/product/{id}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.DELETE)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<Void> deleteProduct(
			@ApiParam(value = "Identifier of the Product", required = true) @PathVariable("id") String id)
	{
		if (StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		final SpiProductModel productModel = getSpiProductService().getProduct(id);
		getSpiProductService().removeProduct(productModel);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@ApiOperation(value = "List or find Product objects", nickname = "listProduct", notes = "This operation list or find Product entities", response = Product.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Product.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/product", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<List<Product>> listProduct(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Identifier of the billing account") @Valid @RequestParam(value = "billingAccount.id", required = false) String billingAccountPeriodid,
			@ApiParam(value = "Product Status value") @Valid @RequestParam(value = "status", required = false) String status)
	{
		final SpiProductContext spiProductContext = new SpiProductContext();
		spiProductContext.setBillingAccountId(billingAccountPeriodid);
		spiProductContext.setStatus(status);

		if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
		{
			final UserModel user =
					userService.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			spiProductContext.setRelatedPartyId(user.getSpiParty().getId());
		}

		offset = spiPaginationService.checkOffset(offset);
		limit = spiPaginationService.checkLimit(limit);

		final List<SpiProductModel> productModels = spiProductService.getProducts(spiProductContext, offset, limit);
		final Integer totalCount = spiProductService.getNumberOfProductsFor(spiProductContext);
		final List<Product> productDtos = new ArrayList<>();
		productModels.forEach(productModel -> productDtos.add(getDataMapper().map(productModel, Product.class, fields)));

		if (limit < totalCount || offset != 0)
		{
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			spiPaginationService.addEntryWithPaginationDetails(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);

			return new ResponseEntity(productDtos, header, HttpStatus.PARTIAL_CONTENT);
		}
		return new ResponseEntity(productDtos, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Partially updates a Product", nickname = "patchProduct", notes = "This operation updates partially a Product entity.", response = Product.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = Product.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/product/{id}",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.PATCH)
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<Product> patchProduct(
			@ApiParam(value = "Identifier of the Product", required = true) @PathVariable("id") String id,
			@ApiParam(value = "The Product to be updated", required = true) @Valid @RequestBody Product product)
	{
		if (product == null || StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final SpiProductModel productModel = getSpiProductService().getProduct(id);
		product.setId(id);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(product, productModel, false);
				getSpiProductService().saveProduct(productModel);
			}
		});

		final SpiProductModel updatedProduct = getSpiProductService().getProduct(id);
		final Product updatedProductDto = getDataMapper().map(updatedProduct, Product.class);
		return new ResponseEntity(updatedProductDto, HttpStatus.OK);
	}

	private void setProductDefaultValues(final Product product)
	{
		if (product.isIsBundle() == null)
		{
			product.setIsBundle(false);
		}
		if (product.isIsCustomerVisible() == null)
		{
			product.setIsCustomerVisible(true);
		}
		if (product.getStartDate() == null)
		{
			product.setStartDate(new Date());
		}
	}

	protected SpiProductService getSpiProductService()
	{
		return spiProductService;
	}

	protected SpiCreateProductValidator getSpiCreateProductValidator()
	{
		return spiCreateProductValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}

}
