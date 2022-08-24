/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.controllers;

import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaBillingAccountService;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccountservices.services.BaPaginationService;
import de.hybris.platform.billingaccounttmfwebservices.v1.api.BillingAccountApi;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Error;
import de.hybris.platform.billingaccounttmfwebservices.v1.security.BaAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.billingaccounttmfwebservices.v1.validators.BaCreateBillingAccountValidator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
 * Default implementation of controller for {@link BillingAccountApi}.
 *
 * @since 2105
 */
@Controller
@Api(tags = "Billing Account")
public class BaBillingAccountController extends BaBaseController implements BillingAccountApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private final HttpServletRequest request;

	@Resource(name = "baBillingAccountService")
	private BaBillingAccountService baBillingAccountService;

	@Resource(name = "baPaginationService")
	private BaPaginationService baPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "baGenericService")
	private BaGenericService genericService;

	@Resource(name = "baCreateBillingAccountValidator")
	private BaCreateBillingAccountValidator baCreateBillingAccountValidator;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Autowired
	public BaBillingAccountController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "Creates a BillingAccount", nickname = "createBillingAccount", notes = "This operation creates a BillingAccount entity.", response = BillingAccount.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BillingAccount.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/billingAccount",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<BillingAccount> createBillingAccount(
			@ApiParam(value = "The BillingAccount to be created", required = true) @Valid @RequestBody BillingAccount billingAccount)
	{

		if (billingAccount == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(billingAccount, "[billingAccount]", getBaCreateBillingAccountValidator());
		if (StringUtils.isNotEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final BaAccountModel billingAccountModel = getBaBillingAccountService()
						.createBillingAccount(BaBillingAccountModel.class);
				getDataMapper().map(billingAccount, billingAccountModel);
				getBaBillingAccountService().saveBillingAccount(billingAccountModel);
			}
		});

		final BaAccountModel billingAccountCreated = getBaBillingAccountService().getBillingAccount(billingAccount.getId());
		final BillingAccount billingAccountCreatedDto = getDataMapper().map(billingAccountCreated, BillingAccount.class);
		return new ResponseEntity(billingAccountCreatedDto, HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Deletes a BillingAccount", nickname = "deleteBillingAccount", notes = "This operation deletes a BillingAccount entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/billingAccount/{id:.+}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.DELETE)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<Void> deleteBillingAccount(
			@ApiParam(value = "Identifier of the BillingAccount", required = true) @PathVariable("id") String id)
	{
		if (StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final BaAccountModel accountModel = getBaBillingAccountService().getBillingAccount(id);
		getBaBillingAccountService().removeBillingAccount(accountModel);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@ApiOperation(value = "List or find BillingAccount objects", nickname = "listBillingAccount", notes = "This operation list or find BillingAccount entities", response = BillingAccount.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = BillingAccount.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/billingAccount",
			produces = { "application/json" },
			method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<List<BillingAccount>> listBillingAccount(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Payment Status value") @Valid @RequestParam(value = "paymentStatus", required = false) String paymentStatus)
	{
		final BaBillingAccountContext billingAccountContext = new BaBillingAccountContext();
		billingAccountContext.setPaymentStatus(paymentStatus);

		if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
		{
			final UserModel user = getUserService()
					.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			billingAccountContext.setRelatedPartyId(user.getBaParty().getId());
		}

		offset = getBaPaginationService().checkOffset(offset);
		limit = getBaPaginationService().checkLimit(limit);

		final List<BaAccountModel> accountModels = getBaBillingAccountService()
				.getBillingAccounts(billingAccountContext, offset, limit);
		final Integer totalCount = getBaBillingAccountService().getNumberOfBillingAccountsFor(billingAccountContext);
		final List<BillingAccount> accountDtos = new ArrayList<>();
		accountModels.forEach(accountModel -> accountDtos.add(getDataMapper().map(accountModel, BillingAccount.class, fields)));

		if (limit < totalCount || offset != 0)
		{
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			getBaPaginationService().addEntryWithPaginationDetails(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);
			return new ResponseEntity(accountDtos, header, HttpStatus.PARTIAL_CONTENT);
		}

		return new ResponseEntity(accountDtos, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Updates partially a BillingAccount", nickname = "patchBillingAccount", notes = "This operation updates partially a BillingAccount entity.", response = BillingAccount.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = BillingAccount.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/billingAccount/{id:.+}",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.PATCH)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<BillingAccount> patchBillingAccount(
			@ApiParam(value = "Identifier of the BillingAccount", required = true) @PathVariable("id") String id,
			@ApiParam(value = "The BillingAccount to be updated", required = true) @Valid @RequestBody BillingAccount billingAccount)
	{
		if (billingAccount == null || StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final BaAccountModel baAccountModel = getBaBillingAccountService().getBillingAccount(id);
		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(billingAccount, baAccountModel, false);
				getBaBillingAccountService().saveBillingAccount(baAccountModel);
			}
		});

		final BaAccountModel accountCreated = getBaBillingAccountService().getBillingAccount(billingAccount.getId());
		final BillingAccount accountCreatedDto = getDataMapper().map(accountCreated, BillingAccount.class);
		return new ResponseEntity(accountCreatedDto, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Retrieves a BillingAccount by ID", nickname = "retrieveBillingAccount", notes = "This operation retrieves a BillingAccount entity. Attribute selection is enabled for all first level attributes.", response = BillingAccount.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = BillingAccount.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@BaAuthorizedResourceOwnerOrAdmin
	@RequestMapping(value = "/billingAccount/{id:.+}",
			produces = { "application/json" },
			method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<BillingAccount> retrieveBillingAccount(
			@ApiParam(value = "Identifier of the BillingAccount", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		final BaAccountModel accountModel = getBaBillingAccountService().getBillingAccount(id);
		final BillingAccount billingAccount = getDataMapper().map(accountModel, BillingAccount.class, fields);
		return new ResponseEntity(billingAccount, HttpStatus.OK);
	}

	protected BaBillingAccountService getBaBillingAccountService()
	{
		return baBillingAccountService;
	}

	protected BaPaginationService getBaPaginationService()
	{
		return baPaginationService;
	}

	protected BaGenericService getGenericService()
	{
		return genericService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected BaCreateBillingAccountValidator getBaCreateBillingAccountValidator()
	{
		return baCreateBillingAccountValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
