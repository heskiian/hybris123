/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcowebservices.dto.TmaSubscriptionBaseWsDto;
import de.hybris.platform.webservicescommons.validators.CompositeValidator;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller exposing operations related to the  {@link TmaSubscriptionBaseData}.
 *
 * @since 6.6
 */
@Controller
@Api(tags = "Subscription Base")
@Secured({ "ROLE_TRUSTED_CLIENT" })
@RequestMapping(value = "/subscriptionBases")
public class TmaSubscriptionBaseController extends BaseController
{
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;
	private CompositeValidator tmaSubscriptionBaseWsDtoValidator;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	/**
	 * Creates a new subscription base with the details received in the request.
	 *
	 * @param subscriptionBaseWsDto
	 * 		the {@link TmaSubscriptionBaseWsDto} object with the details based on which the subscription base will be created.
	 * @param fields
	 * 		response configuration (list of fields, to be returned in response)
	 * @return {@link TmaSubscriptionBaseWsDto} the newly created Subscription Base
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(value = "Creates a subscription base",
			notes = "Creates a subscription base based on the request body details provided.")
	public TmaSubscriptionBaseWsDto createSubscriptionBase(
			@ApiParam(value = "The request details based on which the new SubscriptionBase is created", required = true)
			@RequestBody final TmaSubscriptionBaseWsDto subscriptionBaseWsDto,
			@ApiParam(value = "list of fields to be populated") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(subscriptionBaseWsDto, "subscriptionBaseWsDto", getTmaSubscriptionBaseWsDtoValidator());
		final TmaSubscriptionBaseData subscriptionBaseData = getTmaSubscriptionBaseFacade().createSubscriptionBase
				(subscriptionBaseWsDto.getSubscriberIdentity(), subscriptionBaseWsDto.getBillingSystemId(), subscriptionBaseWsDto
						.getBillingAccountId());
		return getDataMapper().map(subscriptionBaseData, TmaSubscriptionBaseWsDto.class, fields);
	}

	/**
	 * Deletes the subscription base found with the given subscriber identity.
	 *
	 * @param subscriberIdentity
	 * 		unique identifier of the subscription base
	 */
	@RequestMapping(value = "/{billingSystemId}/{subscriberIdentity}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete the subscription base for the given identity",
			notes = "Please provide the following parameter: subscriberIdentity")
	public void deleteSubscriptionBase(
			@ApiParam(value = "Billing system identifier", required = true) @PathVariable(value = "billingSystemId") final String
					billingSystemId,
			@ApiParam(value = "The subscriber identifier", required = true) @PathVariable(value = "subscriberIdentity") final String
					subscriberIdentity)
	{
		getTmaSubscriptionBaseFacade().deleteSubscriptionBase(subscriberIdentity, billingSystemId);
	}

	protected TmaSubscriptionBaseFacade getTmaSubscriptionBaseFacade()
	{
		return tmaSubscriptionBaseFacade;
	}

	@Resource(name = "tmaSubscriptionBaseFacade")
	public void setTmaSubscriptionBaseFacade(TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade)
	{
		this.tmaSubscriptionBaseFacade = tmaSubscriptionBaseFacade;
	}

	protected CompositeValidator getTmaSubscriptionBaseWsDtoValidator()
	{
		return tmaSubscriptionBaseWsDtoValidator;
	}

	@Resource(name = "tmaSubscriptionBaseWsDtoValidator")
	public void setTmaSubscriptionBaseWsDtoValidator(CompositeValidator tmaSubscriptionBaseWsDtoValidator)
	{
		this.tmaSubscriptionBaseWsDtoValidator = tmaSubscriptionBaseWsDtoValidator;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
