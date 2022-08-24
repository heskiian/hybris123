/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductSpecificationFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ProductSpecificationApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
 * Default implementation of {@link ProductSpecificationApi}
 *
 * @since 2102
 */
@SuppressWarnings("unused")
@Controller
@Api(value = "productSpecification", tags = { "Product Catalog Management" })
public class TmaProductSpecificationApiController extends TmaBaseController implements ProductSpecificationApi
{
	@Resource(name = "tmaProductSpecificationFacade")
	private TmaProductSpecificationFacade tmaProductSpecificationFacade;

	@ApiOperation(value = "Retrieves a ProductSpecification by ID", nickname = "retrieveProductSpecification", notes = "This "
			+ "operation retrieves a ProductSpecification entity. Attribute selection is enabled for all first level attributes.",
			response = ProductSpecification.class, tags = { "Product Catalog Management", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ProductSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/productSpecification/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	public ResponseEntity<ProductSpecification> retrieveProductSpecification(
			@ApiParam(value = "Identifier of the ProductSpecification", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields",
					required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			final TmaProductSpecificationData productSpecificationData = getTmaProductSpecificationFacade()
					.getProductSpecification(id);

			final ProductSpecification productSpecification = getDataMapper()
					.map(productSpecificationData, ProductSpecification.class, fields);

			return new ResponseEntity(getObjectMapper().writeValueAsString(productSpecification), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	protected TmaProductSpecificationFacade getTmaProductSpecificationFacade()
	{
		return tmaProductSpecificationFacade;
	}
}
