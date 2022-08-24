/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcowebservices.dto.TmaSubscribedProductWsDto;
import de.hybris.platform.b2ctelcowebservices.dto.UpdatableTmaSubscribedProductWsDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Controller exposing operations related to the subscribed product.
 *
 * @since 6.6
 */
@Controller
@Secured({ "ROLE_TRUSTED_CLIENT" })
@Api(tags = "Subscribed Product")
@RequestMapping(value = "/subscribedProducts")
public class TmaSubscribedProductController extends BaseController
{
	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;

	@Resource(name = "tmaSubscribedProductDtoValidator")
	private Validator tmaSubscribedProductDtoValidator;

	@Resource(name = "updatableTmaSubscribedProductDtoValidator")
	private Validator updatableTmaSubscribedProductDtoValidator;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	/**
	 * Creates a subscribed product.
	 *
	 * @param subscribedProductDto
	 * 			subscribed product which needs to be created
	 * @param fields
	 * 			response configuration (list of fields, to be returned in response)
	 * @return newly created {@link TmaSubscribedProductWsDto} populated with the fields given as input
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a subscribed product",
			notes = "Creates a subscribed product based on the request body details provided.")
	public TmaSubscribedProductWsDto createSubscribedProduct(
			@ApiParam(value = "Subscribed Product", required = true) @RequestBody final TmaSubscribedProductWsDto subscribedProductDto,
			@ApiParam(value = "Fields to be populated in the response") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(subscribedProductDto, "subscribedProduct", tmaSubscribedProductDtoValidator);
		final TmaSubscribedProductData subscribedProductDataSource = getDataMapper().map(subscribedProductDto,
				TmaSubscribedProductData.class);
		final TmaSubscribedProductData createdSubscribedProduct = tmaSubscribedProductFacade
				.createSubscribedProduct(subscribedProductDataSource);
		return getDataMapper().map(createdSubscribedProduct, TmaSubscribedProductWsDto.class, fields);
	}

	/**
	 * Updates the subscribed product identified by given billingSystemId and billingSubscriptionId
	 *
	 * @param updatableSubscribedProductWsDto
	 * 			data used to update the subscribed product, new values of the subscribed product
	 * @param fields
	 * 			response configuration (list of fields, to be returned in response)
	 * @return updated subscribed product
	 */
	@RequestMapping(value = "{billingSystemId}/{billingSubscriptionId}", method = RequestMethod.PUT, consumes =
			{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Updates a subscribed product", notes = "Updates a subscribed product based on the request body details provided.")
	public TmaSubscribedProductWsDto updateSubscribedProduct(
			@ApiParam(value = "BillingSystemId", required = true) @PathVariable final String billingSystemId,
			@ApiParam(value = "BillingSubscriptionId", required = true) @PathVariable final String billingSubscriptionId,
			@ApiParam(value = "Subscribed Product", required = true) @RequestBody final UpdatableTmaSubscribedProductWsDto updatableSubscribedProductWsDto,
			@ApiParam(value = "Fields to be populated in the response") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(updatableSubscribedProductWsDto, "subscribedProduct", updatableTmaSubscribedProductDtoValidator);

		final TmaSubscribedProductData subscribedProductData = getDataMapper().map(updatableSubscribedProductWsDto,
				TmaSubscribedProductData.class);
		final TmaSubscribedProductData updatedSubscribedProductData = tmaSubscribedProductFacade
				.updateSubscribedProduct(billingSystemId, billingSubscriptionId, subscribedProductData);
		return getDataMapper().map(updatedSubscribedProductData, TmaSubscribedProductWsDto.class, fields);
	}

	/**
	 * Deletes the subscribed product identified by given billingSystemId and billingSubscriptionId.
	 *
	 * @param billingSystemId
	 * 			the id of the billing system
	 * @param billingSubscriptionId
	 * 			the id of the billing subscription
	 */
	@RequestMapping(value = "/{billingSystemId}/{billingSubscriptionId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deletes a subscribed product", notes = "Please provide the following parameters: billingSystemId, "
			+ "billingSubscriptionId")
	public void deleteService(
			@ApiParam(value = "Billing system identifier", required = true) @PathVariable final String billingSystemId,
			@ApiParam(value = "Billing subscription identifier", required = true) @PathVariable final String billingSubscriptionId)
	{
		tmaSubscribedProductFacade.deleteSubscribedProduct(billingSystemId, billingSubscriptionId);
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
