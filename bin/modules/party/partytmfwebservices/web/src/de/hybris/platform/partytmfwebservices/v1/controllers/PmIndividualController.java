/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.controllers;

import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partyservices.services.PmIndividualService;
import de.hybris.platform.partytmfwebservices.v1.api.IndividualApi;
import de.hybris.platform.partytmfwebservices.v1.dto.Error;
import de.hybris.platform.partytmfwebservices.v1.dto.Individual;
import de.hybris.platform.partytmfwebservices.v1.security.PmAuthorizedIndividualOwnerOrAdmin;

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
 * Default implementation of controller for {@link IndividualApi}.
 *
 * @since 2108
 */
@Controller
@Api(tags = "Party Management")
public class PmIndividualController extends PmBaseController implements IndividualApi
{
	@Resource(name = "pmIndividualService")
	private PmIndividualService pmIndividualService;

	@ApiOperation(value = "Retrieves a Individual by ID", nickname = "retrieveIndividual", notes = "This operation retrieves a Individual entity. Attribute selection is enabled for all first level attributes.", response = Individual.class)
	@ApiResponses(value =
	{ @ApiResponse(code = 200, message = "Success", response = Individual.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/individual/{id}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@PmAuthorizedIndividualOwnerOrAdmin
	@Override
	public ResponseEntity<Individual> retrieveIndividual(
			@ApiParam(value = "Identifier of the Individual", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) final String fields)
	{
		final PmIndividualModel individualParty = pmIndividualService.findIndividualParty(id);
		final Individual individualPartyDto = getDataMapper().map(individualParty, Individual.class, fields);
		try
		{
			return new ResponseEntity(getObjectMapper().writeValueAsString(individualPartyDto), HttpStatus.OK);
		}
		catch (final JsonProcessingException ex)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, ex, HttpStatus.BAD_REQUEST);
		}
	}
}