/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ProcessTypeManagementApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
 * Default implementation of {@link ProcessTypeManagementApi}
 *
 * @since 1907
 */
@Controller
@Api(value = "processTypeManagement", description = "ProcessType Management API", tags = { "Process Type Management" })
@SuppressWarnings("unused")
public class TmaProcessTypeManagementApiController extends TmaBaseController implements ProcessTypeManagementApi
{
	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	@ApiOperation(value = "Returns the list of available process types", nickname = "listProcessType", tags = { "Process Type "
			+ "Management" }, response = ProcessType.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ProcessType.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "processTypeManagement/processType",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	public ResponseEntity<List<ProcessType>> listProcessType(
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			final Set<ProcessType> processTypesList = new HashSet<>();
			final Set<TmaProcessType> tmaProcessTypes;

			tmaProcessTypes = tmaCustomerFacade.getAllProcessTypes();

			for (TmaProcessType tmaProcessType : tmaProcessTypes)
			{
				ProcessType processType = getDataMapper().map(tmaProcessType, ProcessType.class, fields);
				processTypesList.add(processType);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(processTypesList), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Returns the list of available process types for a given customer", nickname =
			"listQualifiedProcessType", tags = { "Process Type Management" }, response = QualifiedProcessType.class,
			responseContainer = "List"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = QualifiedProcessType.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "processTypeManagement/qualifiedProcessType",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<QualifiedProcessType>> listQualifiedProcessType(
			@NotNull @ApiParam(value = "The id of the customer for which we request the qualifiedProcessType", required = true) @Valid @RequestParam(value = "relatedParty.Id") String relatedPartyId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			final List<QualifiedProcessType> qualifiedProcessTypeList = new ArrayList<>();
			final Set<TmaProcessType> tmaProcessTypes = tmaCustomerFacade.retrieveEligibleProcessTypesByCustomerId(relatedPartyId);
			final PrincipalData customer = tmaCustomerFacade.getUserForUID(relatedPartyId);

			for(TmaProcessType tmaProcessType : tmaProcessTypes)
			{
				QualifiedProcessType qualifiedProcessType = getDataMapper().map(tmaProcessType, QualifiedProcessType.class, fields);
				getDataMapper().map(customer,qualifiedProcessType,fields);
				qualifiedProcessTypeList.add(qualifiedProcessType);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(qualifiedProcessTypeList), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}
}
