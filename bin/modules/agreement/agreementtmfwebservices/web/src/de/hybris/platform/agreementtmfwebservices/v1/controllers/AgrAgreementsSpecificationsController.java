/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.controllers;

import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrAgreementSpecificationService;
import de.hybris.platform.agreementservices.services.AgrPaginationService;
import de.hybris.platform.agreementtmfwebservices.v1.api.AgreementSpecificationApi;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Error;
import de.hybris.platform.agreementtmfwebservices.v1.security.AgrAuthorizedAgreementSpecOwnerOrAdmin;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementSpecificationCreateValidator;
import de.hybris.platform.core.model.user.UserModel;
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
@Api(tags = "Agreement Specifications")
public class AgrAgreementsSpecificationsController extends AgrBaseController implements AgreementSpecificationApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private HttpServletRequest request;

	@Resource(name = "agrAgreementSpecificationService")
	private AgrAgreementSpecificationService agreementSpecificationService;

	@Resource(name = "agrPaginationService")
	private AgrPaginationService agrPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Resource(name = "agrAgreementSpecificationCreateValidator")
	private AgrAgreementSpecificationCreateValidator agrAgreementSpecificationCreateValidator;

	@Autowired
	public AgrAgreementsSpecificationsController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "Retrieves a AgreementSpecification by ID", nickname = "retrieveAgreementSpecification", notes = "This operation retrieves a AgreementSpecification entity. Attribute selection is enabled for all first level attributes.", response = AgreementSpecification.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = AgreementSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreementSpecification/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@AgrAuthorizedAgreementSpecOwnerOrAdmin
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<AgreementSpecification> retrieveAgreementSpecification(
			@ApiParam(value = "Identifier of the AgreementSpecification", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		final AgrAgreementSpecificationModel agreementSpecification = agreementSpecificationService.getAgreementSpecification(id);

		final AgreementSpecification agreementSpecificationDto = getDataMapper()
				.map(agreementSpecification, AgreementSpecification.class, fields);

		return new ResponseEntity(agreementSpecificationDto, HttpStatus.OK);
	}


	@ApiOperation(value = "Creates a AgreementSpecification", nickname = "createAgreementSpecification", notes = "This operation creates a AgreementSpecification entity.", response = AgreementSpecification.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = AgreementSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreementSpecification",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<AgreementSpecification> createAgreementSpecification(@ApiParam(value = "The AgreementSpecification to "
			+ "be created", required = true) @Valid @RequestBody AgreementSpecification agreementSpecification)
	{
		if (agreementSpecification == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(agreementSpecification, "[agreementSpecification]",
				getAgrAgreementSpecificationCreateValidator());

		if (StringUtils.isNotEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgrAgreementSpecificationModel agreementSpecificationModel =
				getAgreementSpecificationService().createAgreementSpecification();

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreementSpecification, agreementSpecificationModel);
				getAgreementSpecificationService().saveAgreementSpecification(agreementSpecificationModel);
			}
		});

		final AgrAgreementSpecificationModel agrAgreementSpecificationCreated =
				getAgreementSpecificationService().getAgreementSpecification(agreementSpecification.getId());
		final AgreementSpecification agreementSpecificationCreatedDto = getDataMapper().map(agrAgreementSpecificationCreated,
				AgreementSpecification.class);

		return new ResponseEntity<>(agreementSpecificationCreatedDto, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Deletes a AgreementSpecification", nickname = "deleteAgreementSpecification", notes = "This operation deletes a AgreementSpecification entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreementSpecification/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.DELETE)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<Void> deleteAgreementSpecification(
			@ApiParam(value = "Identifier of the AgreementSpecification", required =
					true) @PathVariable("id") String id)
	{
		if (StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		final AgrAgreementSpecificationModel agreementSpecificationModel =
				getAgreementSpecificationService().getAgreementSpecification(id);
		getAgreementSpecificationService().removeAgreementSpecification(agreementSpecificationModel);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@ApiOperation(value = "List or find AgreementSpecification objects", nickname = "listAgreementSpecification", notes = "This operation list or find AgreementSpecification entities", response = AgreementSpecification.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = AgreementSpecification.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreementSpecification",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<List<AgreementSpecification>> listAgreementSpecification(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Lifecycle status value") @Valid @RequestParam(value = "lifecycleStatus", required = false) String lifecycleStatus)
	{
		final AgrAgreementContext agrAgreementContext = new AgrAgreementContext();
		agrAgreementContext.setLifecycleStatus(lifecycleStatus);

		if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
		{
			final UserModel user =
					userService.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			agrAgreementContext.setRelatedPartyId(user.getAgrParty().getId());
		}

		offset = agrPaginationService.checkOffset(offset);
		limit = agrPaginationService.checkLimit(limit);

		final List<AgrAgreementSpecificationModel> agreementSpecificationModels = agreementSpecificationService
				.getAgreementSpecifications(agrAgreementContext, offset, limit);
		final Integer totalCount = agreementSpecificationService.getNumberOfAgreementSpecificationsFor(agrAgreementContext);
		final List<AgreementSpecification> agreementSpecificationDtos = new ArrayList<>();
		agreementSpecificationModels.forEach(agrAgreementSpecificationModel -> agreementSpecificationDtos
				.add(getDataMapper().map(agrAgreementSpecificationModel, AgreementSpecification.class, fields)));

		if (limit < totalCount || offset != 0)
		{
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			agrPaginationService.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);

			return new ResponseEntity(agreementSpecificationDtos, header,
					HttpStatus.PARTIAL_CONTENT);
		}
		return new ResponseEntity(agreementSpecificationDtos, HttpStatus.OK);
	}

	@ApiOperation(value = "Updates partially a AgreementSpecification", nickname = "patchAgreementSpecification", notes = "This operation updates partially a AgreementSpecification entity.", response = AgreementSpecification.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = AgreementSpecification.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/agreementSpecification/{id}",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.PATCH)
	@Secured("ROLE_TRUSTED_CLIENT")
	public ResponseEntity<AgreementSpecification> patchAgreementSpecification(@ApiParam(value = "Identifier of the "
			+ "AgreementSpecification", required = true) @PathVariable("id") String id,
			@ApiParam(value = "The AgreementSpecification to be updated", required = true) @Valid @RequestBody AgreementSpecification agreementSpecification)
	{
		if (agreementSpecification == null || StringUtils.isEmpty(id))
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final AgrAgreementSpecificationModel agreementSpecificationModel =
				getAgreementSpecificationService().getAgreementSpecification(id);

		agreementSpecification.setId(id);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreementSpecification, agreementSpecificationModel, false);
				getAgreementSpecificationService().saveAgreementSpecification(agreementSpecificationModel);
			}
		});

		final AgrAgreementSpecificationModel agreementSpecificationUpdated =
				getAgreementSpecificationService().getAgreementSpecification(id);
		final AgreementSpecification agreementSpecificationUpdatedDto =
				getDataMapper().map(agreementSpecificationUpdated, AgreementSpecification.class);

		return new ResponseEntity<>(agreementSpecificationUpdatedDto, HttpStatus.OK);
	}

	protected AgrAgreementSpecificationService getAgreementSpecificationService()
	{
		return agreementSpecificationService;
	}

	protected AgrAgreementSpecificationCreateValidator getAgrAgreementSpecificationCreateValidator()
	{
		return agrAgreementSpecificationCreateValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
