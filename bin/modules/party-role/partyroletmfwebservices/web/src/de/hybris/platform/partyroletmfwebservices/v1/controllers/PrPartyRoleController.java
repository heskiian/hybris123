/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.controllers;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroleservices.service.PrPartyRoleService;
import de.hybris.platform.partyroletmfwebservices.v1.api.PartyRoleApi;
import de.hybris.platform.partyroletmfwebservices.v1.dto.Error;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.security.PrAuthorizedPartyRoleOwnerOrAdmin;

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
 * Default implementation of controller for {@link PartyRoleApi}.
 *
 * @since 2108
 */
@Controller
@Api(tags = "Party Role")
public class PrPartyRoleController extends PrBaseController implements PartyRoleApi
{

	@Resource(name = "prPartyRoleService")
	private PrPartyRoleService prPartyRoleService;

	@Override
	@ApiOperation(value = "Retrieves a PartyRole by ID", nickname = "retrievePartyRole", notes = "This operation retrieves a PartyRole entity. Attribute selection is enabled for all first level attributes.", response = PartyRole.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = PartyRole.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/partyRole/{id}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@PrAuthorizedPartyRoleOwnerOrAdmin
	@SuppressWarnings({ "unchecked", "Duplicates" })
	public ResponseEntity<PartyRole> retrievePartyRole(
			@ApiParam(value = "Identifier of the PartyRole", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			final PrPartyRoleModel partyRoleModel = getPrPartyRoleService().getPartyRole(id);

			final PartyRole partyRole = getDataMapper().map(partyRoleModel, PartyRole.class, fields);

			return new ResponseEntity(getObjectMapper().writeValueAsString(partyRole), HttpStatus.OK);
		}
		catch (JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	protected PrPartyRoleService getPrPartyRoleService()
	{
		return prPartyRoleService;
	}
}
