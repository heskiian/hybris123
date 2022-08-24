/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.controllers;

import de.hybris.platform.billingaccountservices.enums.BaAccountStatus;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.services.BaBillingAccountService;
import de.hybris.platform.billingaccounttmfwebservices.v1.api.ListenerApi;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccountAttributeValueChangeEvent;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccountStateChangeEvent;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Error;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.EventSubscription;
import de.hybris.platform.billingaccounttmfwebservices.v1.validators.BaStateChangeEventValidator;
import de.hybris.platform.billingaccounttmfwebservices.v1.validators.BaUpdateEventValidator;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ListenerApi}.
 *
 * @since 2111
 */
@Controller
@Secured("ROLE_TRUSTED_CLIENT")
@Api(tags = "Billing Account Notification Listeners")
public class BaListenerController extends BaBaseController implements ListenerApi
{
	@Resource(name = "baBillingAccountService")
	private BaBillingAccountService baBillingAccountService;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Resource(name = "baUpdateEventValidator")
	private BaUpdateEventValidator baUpdateEventValidator;

	@Resource(name = "baStateChangeEventValidator")
	private BaStateChangeEventValidator baStateChangeEventValidator;

	@Override
	@ApiOperation(value = "Client listener for entity BillingAccountAttributeValueChangeEvent", nickname = "listenToBillingAccountAttributeValueChangeEvent", notes = "Example of a client listener for receiving the notification BillingAccountAttributeValueChangeEvent", response = EventSubscription.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listener/billingAccountAttributeValueChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity listenToBillingAccountAttributeValueChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody BillingAccountAttributeValueChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getBaUpdateEventValidator());

		if (StringUtils.isNotEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final BillingAccount billingAccount = data.getPayload().getBillingAccount();
		final BaAccountModel baBillingAccountModel = getBaBillingAccountService()
				.getBillingAccount(data.getPayload().getBillingAccount().getId());

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(billingAccount, baBillingAccountModel, false);
				getBaBillingAccountService().saveBillingAccount(baBillingAccountModel);
			}
		});


		return new ResponseEntity(HttpStatus.CREATED);
	}


	@Override
	@ApiOperation(value = "Client listener for entity BillingAccountStateChangeEvent", nickname = "listenToBillingAccountStateChangeEvent", notes = "Example of a client listener for receiving the notification BillingAccountStateChangeEvent", response = EventSubscription.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listener/billingAccountStateChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity listenToBillingAccountStateChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody BillingAccountStateChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getBaStateChangeEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final BillingAccount billingAccount = data.getPayload().getBillingAccount();
		final BaAccountModel baBillingAccountModel = getBaBillingAccountService()
				.getBillingAccount(data.getPayload().getBillingAccount().getId());
		baBillingAccountModel.setState(BaAccountStatus.valueOf(billingAccount.getState().toUpperCase()));
		getBaBillingAccountService().saveBillingAccount(baBillingAccountModel);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	protected BaBillingAccountService getBaBillingAccountService()
	{
		return baBillingAccountService;
	}

	protected BaUpdateEventValidator getBaUpdateEventValidator()
	{
		return baUpdateEventValidator;
	}

	protected BaStateChangeEventValidator getBaStateChangeEventValidator()
	{
		return baStateChangeEventValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
