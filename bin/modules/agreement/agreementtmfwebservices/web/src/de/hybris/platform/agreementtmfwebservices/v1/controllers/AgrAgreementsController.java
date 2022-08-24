/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.controllers;

import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrAgreementsService;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementservices.services.AgrPaginationService;
import de.hybris.platform.agreementtmfwebservices.v1.api.AgreementApi;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Error;
import de.hybris.platform.agreementtmfwebservices.v1.security.AgrAuthorizedAgreementOwnerOrAdmin;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementCreateValidator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
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


@Controller
@Api(tags = "Agreements")
public class AgrAgreementsController extends AgrBaseController implements AgreementApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private HttpServletRequest request;

	@Resource(name = "agrAgreementsService")
	private AgrAgreementsService agreementService;

	@Resource(name = "agrPaginationService")
	private AgrPaginationService agrPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "agrGenericService")
	private AgrGenericService agreementGenericService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "agrAgreementCreateValidator")
	private AgrAgreementCreateValidator agrAgreementCreateValidator;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Autowired
	public AgrAgreementsController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "Retrieves a Agreement by ID", nickname = "retrieveAgreement", notes = "This operation retrieves a Agreement entity. Attribute selection is enabled for all first level attributes.", response = Agreement.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Agreement.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreement/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@AgrAuthorizedAgreementOwnerOrAdmin
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<Agreement> retrieveAgreement(
			@ApiParam(value = "Identifier of the Agreement", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		final AgrAgreementModel agreement = agreementService.getAgreement(id);

		final Agreement agreementDto = getDataMapper().map(agreement, Agreement.class, fields);

		return new ResponseEntity(agreementDto, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Creates a Agreement", nickname = "createAgreement", notes = "This operation creates a Agreement entity.", response = Agreement.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Agreement.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreement",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<Agreement> createAgreement(
			@ApiParam(value = "The Agreement to be created", required = true) @Valid @RequestBody Agreement agreement)
	{
		if (agreement == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		final String errorMessage = validate(agreement, "[agreement]", getAgrAgreementCreateValidator());
		if (StringUtils.isNotEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgrAgreementModel agreementModel = (AgrAgreementModel) getAgreementGenericService()
				.createItem(AgrAgreementModel.class);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreement, agreementModel);
				getAgreementGenericService().saveItem(agreementModel);
			}
		});

		final AgrAgreementModel agrAgreementCreated = getAgreementService().getAgreement(agreement.getId());
		final Agreement agreementCreatedDto = getDataMapper().map(agrAgreementCreated, Agreement.class);

		return new ResponseEntity(agreementCreatedDto, HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Deletes a Agreement", nickname = "deleteAgreement", notes = "This operation deletes a Agreement entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreement/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.DELETE)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<Void> deleteAgreement(
			@ApiParam(value = "Identifier of the Agreement", required = true) @PathVariable("id") String id)
	{
		if (StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final AgrAgreementModel agreementModel = getAgreementService().getAgreement(id);
		getAgreementService().removeAgreement(agreementModel);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@ApiOperation(value = "List or find Agreement objects", nickname = "listAgreement", notes = "This operation list or find Agreement entities", response = Agreement.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Agreement.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreement",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<List<Agreement>> listAgreement(@ApiParam(value = "Comma-separated properties to be provided in "
			+ "response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Status value") @Valid @RequestParam(value = "status", required = false) String status)
	{
		final AgrAgreementContext agrAgreementContext = new AgrAgreementContext();
		agrAgreementContext.setStatus(status);

		if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
		{
			final UserModel user =
					userService.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			agrAgreementContext.setRelatedPartyId(user.getAgrParty().getId());
		}

		offset = agrPaginationService.checkOffset(offset);
		limit = agrPaginationService.checkLimit(limit);

		final List<AgrAgreementModel> agreementSpecificationModels = agreementService
				.getAgreements(agrAgreementContext, offset, limit);
		final Integer totalCount = agreementService.getNumberOfAgreementsFor(agrAgreementContext);
		final List<Agreement> agreementDtos = new ArrayList<>();
		agreementSpecificationModels.forEach(agrAgreementSpecificationModel -> agreementDtos
				.add(getDataMapper().map(agrAgreementSpecificationModel, Agreement.class, fields)));

		if (limit < totalCount || offset != 0)
		{
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			agrPaginationService.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);

			return new ResponseEntity(agreementDtos, header, HttpStatus.PARTIAL_CONTENT);
		}
		return new ResponseEntity(agreementDtos, HttpStatus.OK);
	}

	@Override
	public @ApiOperation(value = "Updates partially a Agreement", nickname = "patchAgreement", notes = "This operation updates partially a Agreement entity.", response = Agreement.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = Agreement.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreement/{id}",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.PATCH)
	@Secured("ROLE_TRUSTED_CLIENT")
	ResponseEntity<Agreement> patchAgreement(
			@ApiParam(value = "Identifier of the Agreement", required = true) @PathVariable("id") String id,
			@ApiParam(value = "The Agreement to be updated", required = true) @Valid @RequestBody Agreement agreement)
	{
		if (agreement == null || StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final AgrAgreementModel agrAgreementModel = getAgreementService().getAgreement(id);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreement, agrAgreementModel, false);
				getAgreementService().saveAgreement(agrAgreementModel);
			}
		});

		final AgrAgreementModel updatedAgreement = getAgreementService().getAgreement(id);
		final Agreement updatedAgreementDto = getDataMapper().map(updatedAgreement, Agreement.class);
		return new ResponseEntity(updatedAgreementDto, HttpStatus.OK);
	}

	protected AgrAgreementsService getAgreementService()
	{
		return agreementService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected AgrGenericService getAgreementGenericService()
	{
		return agreementGenericService;
	}

	protected AgrAgreementCreateValidator getAgrAgreementCreateValidator()
	{
		return agrAgreementCreateValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
