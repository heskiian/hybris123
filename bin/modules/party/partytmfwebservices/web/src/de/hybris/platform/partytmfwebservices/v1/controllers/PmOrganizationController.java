/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.controllers;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partyservices.services.PmOrganizationService;
import de.hybris.platform.partytmfwebservices.v1.api.OrganizationApi;
import de.hybris.platform.partytmfwebservices.v1.dto.Error;
import de.hybris.platform.partytmfwebservices.v1.dto.Organization;
import de.hybris.platform.partytmfwebservices.v1.security.PmAuthorizedOrganizationOwnerOrAdmin;

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
 * Controller handling {@link Organization} related actions.
 *
 * @since 2108
 */
@Controller
@Api(tags = "Party Management")
public class PmOrganizationController extends PmBaseController implements OrganizationApi
{
	@Resource(name = "pmOrganizationService")
	private PmOrganizationService organizationService;

	@ApiOperation(value = "Retrieves a Organization by ID", nickname = "retrieveOrganization", notes = "This operation retrieves a Organization entity. Attribute selection is enabled for all first level attributes.", response = Organization.class, tags = {
			"Party Management", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Organization.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/organization/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@PmAuthorizedOrganizationOwnerOrAdmin
	@SuppressWarnings({ "unchecked", "Duplicates" })
	@Override
	public ResponseEntity<Organization> retrieveOrganization(
			@ApiParam(value = "Identifier of the Organization", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		final PmOrganizationModel organization = organizationService.getOrganization(id);

		final Organization organizationDto = getDataMapper().map(organization, Organization.class, fields);

		try
		{
			return new ResponseEntity(getObjectMapper().writeValueAsString(organizationDto), HttpStatus.OK);
		}
		catch (final JsonProcessingException ex)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, ex, HttpStatus.BAD_REQUEST);
		}
	}
}
