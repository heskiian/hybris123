/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.bundle.TmaCheckoutFacade;
import de.hybris.platform.b2ctelcofacades.order.TmaOrderFacade;
import de.hybris.platform.b2ctelcofacades.pagination.TmaPaginationFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaUserFacade;
import de.hybris.platform.b2ctelcoservices.order.exception.OrderProcessingException;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedOrderCreatorOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedProductOrderUpdate;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsUserAbleToAccessOrders;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ProductOrderingApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ErrorRepresentation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ProductOrderingApi}
 *
 * @since 1907
 */
@SuppressWarnings("unused")
@Controller
@Api(value = "productOrdering", description = "Product Ordering API", tags = { "Product Ordering" })
public class TmaProductOrderApiController extends TmaBaseController implements ProductOrderingApi
{
	private static final int BAD_REQUEST_ERROR_CODE = 22;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	@Resource(name = "userFacade")
	private TmaUserFacade userFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private TmaCheckoutFacade checkoutFacade;

	@Resource(name = "orderFacade")
	private TmaOrderFacade tmaOrderFacade;

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	@Resource(name = "tmaPaginationFacade")
	private TmaPaginationFacade tmaPaginationFacade;

	@Resource(name = "tmaProductOrderUpdateValidator")
	private Validator tmaProductOrderUpdateValidator;

	private final HttpServletRequest request;

	@Autowired
	public TmaProductOrderApiController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@ApiOperation(value = "List product orders", nickname = "listProductOrders", notes = "This operation list product order entities. Attribute  selection  is enabled for all first level attributes. Filtering may  be available  depending  on the compliance  level supported  by an implementation.", response = ProductOrder.class, responseContainer = "List", tags = {
			"Product Ordering", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ProductOrder.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = ErrorRepresentation.class),
			@ApiResponse(code = 401, message = "Unauthorized  List of supported error codes: - 40: Missing credentials - 41: Invalid credentials - 42: Expired credentials", response = ErrorRepresentation.class),
			@ApiResponse(code = 403, message = "Forbidden  List of supported error codes: - 50: Access denied - 51: Forbidden requester - 52: Forbidden user - 53: Too many requests", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = ErrorRepresentation.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error  List of supported error codes: - 1: Internal error", response = ErrorRepresentation.class),
			@ApiResponse(code = 503, message = "Service Unavailable  List of supported error codes: - 5: The service is temporarily unavailable - 6: API is over capacity, retry later !", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/productOrdering/productOrder", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsUserAbleToAccessOrders
	@Override
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<List<ProductOrder>> listProductOrders(
			@NotNull @ApiParam(value = "Identifier of the PartyRole", required = true) @Valid @RequestParam(value = "relatedParty.id") String relatedPartyId,
			@ApiParam(value = "Attributes selection") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) String baseSiteId)
	{
		try
		{
			final List<OrderData> orders = getTmaOrderFacade().getOrders(relatedPartyId);

			List<ProductOrder> productOrders = getDataMapper().mapAsList(orders, ProductOrder.class, fields);

			final long totalCount = productOrders.size();
			final int checkedOffset = tmaPaginationFacade.checkOffset(offset);
			final int checkedLimit = tmaPaginationFacade.checkLimit(limit);

			final MultiValueMap<String, String> headersMap = getResponseHeadersMap(offset, limit, totalCount);

			productOrders = tmaPaginationFacade.filterListByOffsetAndLimit(checkedOffset, checkedLimit, productOrders);

			if (checkedLimit < totalCount || checkedOffset > totalCount || checkedOffset != 0)
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(productOrders),
						headersMap, HttpStatus.PARTIAL_CONTENT);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(productOrders), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * An order resource is created and persisted. Creation of an order resource is performed in 2 ways: <br/> - creation of the
	 * order taking data from an existing shopping cart (provided in the request as a reference to the shopping cart resource)
	 * <br/> - creation of the order with full data provided in the request <br/>
	 *
	 * @param productOrder
	 * 		input storing data (reference to a cart resource or full order data) for creating the order resource
	 * @return resource order created in the format of a {@link ProductOrder}
	 */
	@ApiOperation(value = "Creates a 'ProductOrder' resource", nickname = "productOrderCreate", notes = "This operation creates a product order entity and triggers the place order process", response = ProductOrder.class, tags = {
			"Product Ordering", })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Success", response = ProductOrder.class),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = ErrorRepresentation.class),
			@ApiResponse(code = 401, message = "Unauthorized  List of supported error codes: - 40: Missing credentials - 41: Invalid credentials - 42: Expired credentials", response = ErrorRepresentation.class),
			@ApiResponse(code = 403, message = "Forbidden  List of supported error codes: - 50: Access denied - 51: Forbidden requester - 52: Forbidden user - 53: Too many requests", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = ErrorRepresentation.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error  List of supported error codes: - 1: Internal error", response = ErrorRepresentation.class),
			@ApiResponse(code = 503, message = "Service Unavailable  List of supported error codes: - 5: The service is temporarily unavailable", response = ErrorRepresentation.class) })
	@RequestMapping(value = "productOrdering/productOrder",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@IsAuthorizedOrderCreatorOrAdmin
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrder> productOrderCreate(
			@ApiParam(required = true) @Valid @RequestBody ProductOrder productOrder, @Valid String baseSiteId)

	{
		if (productOrder == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String userId = productOrder.getRelatedParty().get(0).getId();
		final UserModel user = userService.getUserForUID(userId);
		userService.setCurrentUser(user);

		cartLoaderStrategy.loadCart(productOrder.getShoppingCart().getId());

		OrderData createdOrderData;
		try
		{
			if (productOrder.getShoppingCart() != null)
			{
				createdOrderData = checkoutFacade.placeOrderFromCart(productOrder.getShoppingCart().getId(), userId, true);
				CartData cartData = new CartData();
				cartData.setCode(productOrder.getShoppingCart().getId());
				createdOrderData.setCart(cartData);
			}
			else
			{
				OrderData orderDataToBeCreated = getDataMapper().map(productOrder, OrderData.class);
				createdOrderData = checkoutFacade.placeOrderFromDto(orderDataToBeCreated, userId);
			}

			if (createdOrderData == null)
			{
				return getUnsuccessfulResponseWithErrorRepresentation(null, null, 1, "Order cannot be created",
						HttpStatus.UNPROCESSABLE_ENTITY);
			}

			final ProductOrder createdProductOrderWsDto = getDataMapper().map(createdOrderData, ProductOrder.class);

			return new ResponseEntity(getObjectMapper().writeValueAsString(createdProductOrderWsDto), HttpStatus.CREATED);
		}
		catch (final OrderProcessingException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation("Order cannot be created", e,
					e.getOrderProcessingErrorCode().getErrorCode(), e.getBusinessReason(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		catch (final IllegalArgumentException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, e, 22, e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		catch (final JsonProcessingException e)
		{
			return getUnsuccessfulResponse("Error occurred while preparing the response", e, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@ApiOperation(value = "Retrieves a ProductOrder by ID", nickname = "retrieveProductOrder", notes = "This operation retrieves a ProductOrder entity. Attribute selection is enabled for all first level attributes.", response = ProductOrder.class, tags = {
			"Product Ordering", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ProductOrder.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/productOrdering/productOrder/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<ProductOrder> retrieveProductOrder(
			@ApiParam(value = "Identifier of the ProductOrder", required = true) @PathVariable("id") final String id,
			@NotNull @ApiParam(value = "Identifier of the Customer", required = true) @Valid @RequestParam(value = "relatedParty.id") final String relatedPartyId,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			final OrderData orderData = getTmaOrderFacade().getOrderDetails(id, relatedPartyId);

			final ProductOrder productOrder = getDataMapper().map(orderData, ProductOrder.class, fields);

			return new ResponseEntity(getObjectMapper().writeValueAsString(productOrder), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Updates partially a ProductOrder", nickname = "patchProductOrder", notes = "This operation updates partially a ProductOrder entity.", response = ProductOrder.class, tags = {
			"productOrder", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = ProductOrder.class),
			@ApiResponse(code = 400, message = "Bad Request", response = ErrorRepresentation.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = ErrorRepresentation.class),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found", response = ErrorRepresentation.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = ErrorRepresentation.class),
			@ApiResponse(code = 409, message = "Conflict", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/productOrdering/productOrder/{id}",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.PATCH)
	@IsAuthorizedProductOrderUpdate
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrder> patchProductOrder(
			@ApiParam(value = "Identifier of the ProductOrder", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "The ProductOrder to be updated", required = true) @Valid @RequestBody final ProductOrder productOrder,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			final String errorMessage = validate(productOrder, "productOrder", tmaProductOrderUpdateValidator);
			if (StringUtils.isNotEmpty(errorMessage))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, BAD_REQUEST_ERROR_CODE, errorMessage,
						HttpStatus.BAD_REQUEST);
			}

			final String relatedPartyId = productOrder.getRelatedParty().iterator().next().getId();

			getTmaOrderFacade().updateStatus(id, relatedPartyId, productOrder.getState().toString());

			final OrderData orderData = getTmaOrderFacade().getOrderDetails(id, relatedPartyId);


			final ProductOrder order = getDataMapper().map(orderData, ProductOrder.class, (String) null);

			return new ResponseEntity(getObjectMapper().writeValueAsString(order), HttpStatus.OK);

		}
		catch (final IllegalArgumentException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
		catch (final ModelNotFoundException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(e.getMessage(), null, BAD_REQUEST_ERROR_CODE,
					String.format("User '%s' does not have order with id '%s'.",
							productOrder.getRelatedParty().iterator().next().getId(), id), HttpStatus.BAD_REQUEST);
		}
		catch (final IllegalAccessException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(e.getMessage(), null, BAD_REQUEST_ERROR_CODE, e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
	}

	private MultiValueMap<String, String> getResponseHeadersMap(Integer offset, Integer limit, long totalCount)
	{
		final MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
		final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);

		tmaPaginationFacade
				.addPaginationHeadersToResponse(headersMap, filter(request.getRequestURL().toString()), queryStringWithoutParams,
						totalCount, limit, offset);
		return headersMap;
	}

	protected TmaOrderFacade getTmaOrderFacade()
	{
		return tmaOrderFacade;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
