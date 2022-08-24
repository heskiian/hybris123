/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.productofferingprice.TmaProductOfferingPriceFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v3.api.ProductOfferingPriceApi;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;
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
 * Default implementation of {@link ProductOfferingPriceApi}
 *
 * @since 2102
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "productOfferingPrice", tags = { "Product Catalog Management" })
public class TmaProductOfferingPriceApiController extends TmaBaseController implements ProductOfferingPriceApi
{
	@Resource(name = "tmaProductOfferingPriceFacade")
	private TmaProductOfferingPriceFacade tmaProductOfferingPriceFacade;

	@ApiOperation(value = "Retrieves a 'ProductOfferingPrice' by Id", nickname = "retrieveProductOfferingPrice", notes = "This "
			+ "operation retrieves a product offering price entity using its unique ID", response = ProductOfferingPrice.class,
			responseContainer = "List", tags = { "Product Catalog Management", })
	@ApiResponses(value =
			{ @ApiResponse(code = 200, message = "Ok", response = ProductOfferingPrice.class),
					@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
					@ApiResponse(code = 404, message = "Not Found", response = Error.class),
					@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/productOfferingPrice/{id}", produces =
			{ "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOfferingPrice> retrieveProductOfferingPrice(
			@ApiParam(value = "Identifier of the Product Offering Price", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			final TmaProductOfferingPriceData productOfferingPriceData = getTmaProductOfferingPriceFacade().getPop(id);

			final ProductOfferingPrice productOfferingPrice = getDataMapper().map(productOfferingPriceData,
					ProductOfferingPrice.class, fields);

			return new ResponseEntity(getObjectMapper().writeValueAsString(productOfferingPrice), HttpStatus.OK);

		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	protected TmaProductOfferingPriceFacade getTmaProductOfferingPriceFacade()
	{
		return tmaProductOfferingPriceFacade;
	}
}
