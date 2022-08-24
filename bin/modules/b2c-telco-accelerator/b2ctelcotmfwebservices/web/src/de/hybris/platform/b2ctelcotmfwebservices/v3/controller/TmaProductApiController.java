/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedSubscriptionUserOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v3.api.ProductApi;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

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
 * Default implementation of {@link ProductApi}
 *
 * @since 2102
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "product", tags =
{ "Product Inventory Management" })
public class TmaProductApiController extends TmaBaseController implements ProductApi
{
	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;

	@Resource(name = "tmaSubscriptionBaseFacade")
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;

	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	@Resource(name = "tmaSubscriptionAccessFacade")
	private TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;

	@ApiOperation(value = "Retrieves a Product by Id", nickname = "retrieveProduct", notes = "This operation retrieves a subscribed product using its unique ID", response = Product.class, tags =
	{ "Product Inventory Management", })
	@ApiResponses(value =
	{ @ApiResponse(code = 200, message = "Ok", response = Product.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/product/{id}", produces =
	{ "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsAuthorizedSubscriptionUserOrAdmin
	@SuppressWarnings("unchecked")
	public ResponseEntity<Product> retrieveProduct(@ApiParam(value = "Identifier of the Product Offering", required = true)
	@PathVariable("id")
	final String id,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body")
			@Valid
			@RequestParam(value = "fields", required = false)
			final String fields, @ApiParam(value = "Identifier of the BaseSite")
			@Valid
			@RequestParam(value = "baseSiteId", required = false)
			final String baseSiteId)
	{
		try
		{
			if (tmaSubscriptionBaseFacade.doesSubscriptionBaseExist(id))
			{
				final TmaSubscriptionBaseData tmaSubscriptionBaseData = tmaSubscriptionBaseFacade
						.getSubscriptionBaseBySubscriberIdentity(id);
				final Product subscriptionBase = getDataMapper().map(tmaSubscriptionBaseData, Product.class, fields);
				return new ResponseEntity(getObjectMapper().writeValueAsString(subscriptionBase), HttpStatus.OK);
			}
			else
			{
				final TmaSubscribedProductData tmaSubscribedProductData = tmaSubscribedProductFacade.getSubscriptionsById(id);
				final Product subscribedproduct = getDataMapper().map(tmaSubscribedProductData, Product.class, fields);
				return new ResponseEntity(getObjectMapper().writeValueAsString(subscribedproduct), HttpStatus.OK);
			}

		}

		catch (final UnknownIdentifierException | ModelNotFoundException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}
}
