/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;


import de.hybris.platform.b2ctelcofacades.data.CreateTmaBillingAccountRequest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaBillingAccountFacade;
import de.hybris.platform.b2ctelcowebservices.dto.TmaBillingAccountRequestWsDto;
import de.hybris.platform.b2ctelcowebservices.dto.TmaBillingAccountWsDto;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.validators.CompositeValidator;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller exposing operations related to billing accounts.
 *
 * @since 6.6
 */
@Controller
@RequestMapping("/billingAccounts")
@Secured({ "ROLE_TRUSTED_CLIENT" })
@Api(tags = "Billing Accounts")
public class TmaBillingAccountController extends BaseController
{
	private TmaBillingAccountFacade tmaBillingAccountFacade;

	private CompositeValidator billingAccountValidator;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	/**
	 * Creates a new billing account with the details received in the request body.
	 *
	 * @param billingAccountRequestWsDto
	 *           the {@link TmaBillingAccountRequestWsDto} object with details based on which the billing
	 *           account will be created.
	 * @param fields
	 *           predefined values to determine what fields to be returned in the response
	 * @return {@link TmaBillingAccountWsDto} the newly created billing account
	 * @throws WebserviceValidationException
	 *            exception thrown in case the request body fields has failed validation
	 * @throws UnknownIdentifierException
	 *            exception thrown in case the request body fields required as pre-defined in hybris,
	 *            cannot be found
	 */
	@RequestMapping(method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = " Creates a billing account", notes = "Creates a billing account based on the request body details provided.")
	public TmaBillingAccountWsDto createBillingAccount(
			@ApiParam(value = "The billing account request details based on which the billing account is created", required = true) @RequestBody final TmaBillingAccountRequestWsDto billingAccountRequestWsDto,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		validate(billingAccountRequestWsDto, "billingAccountRequestWsDto", getBillingAccountValidator());
		final CreateTmaBillingAccountRequest billingAccountRequest = getDataMapper()
				.map(billingAccountRequestWsDto, CreateTmaBillingAccountRequest.class);

		final TmaBillingAccountData billingAccountData = getTmaBillingAccountFacade().createBillingAccount(billingAccountRequest);
		return getDataMapper().map(billingAccountData, TmaBillingAccountWsDto.class, fields);
	}

	/**
	 * Deletes the Billing Account associated to a given billing account id and a billing system id.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 */
	@RequestMapping(value = "/{billingSystemId}/{billingAccountId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deletes a billing account", notes = "This method deletes a billing account from the resource found at"
			+ " the specified path.")
	public void deleteBillingAccount(
			@ApiParam(value = "Unique identifier of the billing system under which the billing account needs to be deleted", required = true) @PathVariable final String billingSystemId,
			@ApiParam(value = "Unique identifier of the billing account from the specified billing system", required = true) @PathVariable final String billingAccountId)
	{
		getTmaBillingAccountFacade().deleteBillingAccount(billingSystemId, billingAccountId);
	}

	protected TmaBillingAccountFacade getTmaBillingAccountFacade()
	{
		return tmaBillingAccountFacade;
	}

	@Resource(name = "tmaBillingAccountFacade")
	public void setTmaBillingAccountFacade(final TmaBillingAccountFacade tmaBillingAccountFacade)
	{
		this.tmaBillingAccountFacade = tmaBillingAccountFacade;
	}

	protected CompositeValidator getBillingAccountValidator()
	{
		return billingAccountValidator;
	}

	@Resource(name = "tmaBillingAccountWsDtoValidator")
	public void setBillingAccountValidator(final CompositeValidator billingAccountValidator)
	{
		this.billingAccountValidator = billingAccountValidator;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
