/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.address.TmaAddressFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaUserFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.GeographicAddressApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ErrorRepresentation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfwebservices.validators.TmaGeoraphicAddressValidator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link GeographicAddressApi}
 *
 * @since 1907
 */
@SuppressWarnings("unused")
@Controller
@Api(value = "geographicAddress", description = "Geographic address management API", tags =
{ "Geographic Address Management" })
public class TmaGeographicAddressApiController extends TmaBaseController implements GeographicAddressApi
{
	@Resource(name = "userFacade")
	private TmaUserFacade userFacade;

	@Resource(name = "tmaGeoraphicAddressValidator")
	private TmaGeoraphicAddressValidator tmaGeoraphicAddressValidator;

	@Resource
	private TmaAddressFacade tmaAddressFacade;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	private static final int MISSING_PARAMETER_ERROR_CODE = 62;
	private static final int UNPROCESSABLE_ENTITY_ERROR_CODE = 27;
	private static final int RESOURCE_NOT_FOUND_ERROR_CODE = 60;
	private static final int BAD_REQUEST_ERROR_CODE = 22;
	private static final int NOT_FOUND_ERROR_CODE = 404;
	private static final String NOT_FOUND_MESSAGE = "Not Found";
	private static final String UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR = "Unprocessable entity  Functional error";

	@ApiOperation(value = "Retrieves a list of 'GeographicAddress'", nickname = "listGeographicAddresses", notes = "This operation retrieves a list of geographic address entities for the related party.", response = GeographicAddress.class, responseContainer = "List", tags =
	{ "Geographic Address Management", })
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Success", response = GeographicAddress.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = ErrorRepresentation.class),
			@ApiResponse(code = 405, message = "Method Not Allowed  List of supported error codes: - 61: Method not allowed", response = ErrorRepresentation.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/geographicAddress", produces =
	{ "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsAuthorizedResourceOwnerOrAdmin
	@Override
	public ResponseEntity<List<GeographicAddress>> geographicAddressFind(
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body")
			@Valid
			@RequestParam(value = "fields", required = false)
			final String fields,
			@ApiParam(value = "Identifier of the BaseSite")
			@Valid
			@RequestParam(value = "baseSiteId", required = false)
			final String baseSiteId,
			@ApiParam(value = "Id of a specific party role for which the addresses should be provided")
			@Valid
			@RequestParam(value = "relatedParty.id", required = false)
			final String relatedPartyId,
			@ApiParam(value = "Requested index for start of resources to be provided in response requested by client")
			@Valid
			@RequestParam(value = "offset", required = false)
			final String offset,
			@ApiParam(value = "Requested number of resources to be provided in response requested by client")
			@Valid
			@RequestParam(value = "limit", required = false)
			final String limit)
	{
		try
		{
			if (StringUtils.isEmpty(relatedPartyId))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, null, UNPROCESSABLE_ENTITY_ERROR_CODE,
						UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, HttpStatus.BAD_REQUEST);
			}

			final List<AddressData> addressDataList = userFacade.getAddressesForUser(relatedPartyId, true);
			if (CollectionUtils.isEmpty(addressDataList))
			{
				return new ResponseEntity<>(HttpStatus.OK);
			}

			final List<GeographicAddress> geographicAddressList = new ArrayList<>();
			for (final AddressData addressData : addressDataList)
			{
				final GeographicAddress geographicAddress = getDataMapper().map(addressData, GeographicAddress.class, fields);
				geographicAddressList.add(geographicAddress);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(geographicAddressList), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException | JsonProcessingException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation("Missing related party", e, MISSING_PARAMETER_ERROR_CODE, "Missing query-string parameter",
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@ApiOperation(value = "Creates a 'Geographic Address'", nickname = "createGeographicAddress", notes = "This operation creates a new geographic address entity for the related party.", response = GeographicAddress.class, tags =
	{
			"Geographic Address Management", })
	@ApiResponses(value =
	{
			@ApiResponse(code = 201, message = "Created", response = GeographicAddress.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/geographicAddress", produces =
	{ "application/json;charset=utf-8" }, consumes =
	{ "application/json;charset=utf-8" }, method = RequestMethod.POST)
	@IsAuthorizedResourceOwnerOrAdmin
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<GeographicAddress> createGeographicAddress(
			@ApiParam(value = "The 'Geographic Address' Cart to be created", required = true) @Valid @RequestBody GeographicAddress geographicAddress,
			@NotNull
			@ApiParam(value = "The id of the related party.", required = true)
			@Valid
			@RequestParam(value = "relatedParty.id")
			final String relatedPartyId)

	{
		if (geographicAddress == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try
		{
			final String errorMessage = validate(geographicAddress, "geographicAddress", tmaGeoraphicAddressValidator);

			if (StringUtils.isNotEmpty(errorMessage))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 22, errorMessage, HttpStatus.BAD_REQUEST);
			}
			final AddressData addressData = getDataMapper().map(geographicAddress, AddressData.class);
			addressData.setVisibleInAddressBook(true);

			final AddressData address = userFacade.createAddressForUser(addressData, relatedPartyId);

			if (address == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			geographicAddress = getDataMapper().map(address, GeographicAddress.class);

			return new ResponseEntity(getObjectMapper().writeValueAsString(geographicAddress), HttpStatus.CREATED);
		}
		catch (final UnknownIdentifierException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse("Error occurred due to bad request", e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Retrieves a 'GeographicAddress' By ID", nickname = "finsGeographicAddressById", notes = "This operation retrieves a geographic address entity using its unique ID.", response = GeographicAddress.class, responseContainer = "List", tags =
	{ "Geographic Address Management", })
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Success", response = GeographicAddress.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = ErrorRepresentation.class),
			@ApiResponse(code = 405, message = "Method Not Allowed  List of supported error codes: - 61: Method not allowed", response = ErrorRepresentation.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/geographicAddress/{id}", produces =
	{ "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsAuthorizedResourceOwnerOrAdmin
	@Override
	public ResponseEntity<GeographicAddress> getGeographicAddressById(
			@ApiParam(value = "Identifier of the Geographic Address", required = true)
			@PathVariable("id")
			final String id,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body")
			@Valid
			@RequestParam(value = "fields", required = false)
			final String fields,
			@ApiParam(value = "Id of a specific party role for which the addresses should be provided")
			@Valid
			@RequestParam(value = "relatedParty.id", required = false)
			final String relatedPartyId)

	{
		try
		{
			if (StringUtils.isEmpty(id))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, null, UNPROCESSABLE_ENTITY_ERROR_CODE,
						UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, HttpStatus.BAD_REQUEST);
			}
			final AddressData addressData = tmaAddressFacade.getAddress(id, relatedPartyId);
			if (addressData == null)
			{
				return getUnsuccessfulResponseWithErrorRepresentation(NOT_FOUND_MESSAGE, null, RESOURCE_NOT_FOUND_ERROR_CODE , "Resource not found",
						HttpStatus.NOT_FOUND);
			}
			final GeographicAddress geographicAddress = getDataMapper().map(addressData, GeographicAddress.class, fields);
			return new ResponseEntity(getObjectMapper().writeValueAsString(geographicAddress), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException | JsonProcessingException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation("Missing related party", e, MISSING_PARAMETER_ERROR_CODE , "Missing query-string parameter",
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@ApiOperation(value = "Deletes a GeographicAddress", nickname = "deleteGeographicAddress", notes = "This operation deletes a GeographicAddress entity.", tags =
	{ "Geographic Address Management", })
	@ApiResponses(value =
	{ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/geographicAddress/{id}", produces =
	{ "application/json;charset=utf-8" }, method = RequestMethod.DELETE)
	@IsAuthorizedResourceOwnerOrAdmin
	@Override
	public ResponseEntity<Void> deleteGeographicAddress(
			@ApiParam(value = "Unique identifier of the geographic address", required = true)
			@PathVariable("id")
			final String addressId, @NotNull
			@ApiParam(value = "Id of a specific party role for which the addresses should be provided", required = true)
			@Valid
			@RequestParam(value = "relatedParty.id", required = true)
			final String relatedPartyId)
	{
		if (StringUtils.isEmpty(relatedPartyId) || addressId == null)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, null, UNPROCESSABLE_ENTITY_ERROR_CODE ,
					UNPROCESSABLE_ENTITY_FUNCTIONAL_ERROR, HttpStatus.BAD_REQUEST);
		}

		if (tmaAddressFacade.removeAddress(relatedPartyId, addressId))
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return getUnsuccessfulResponseWithErrorRepresentation(NOT_FOUND_MESSAGE, null, NOT_FOUND_ERROR_CODE,
				"addessId doesn't exist or belongs to diffrent user",
				HttpStatus.NOT_FOUND);

	}

	@ApiOperation(value = "Updates partially a 'GeographicAddress' by Id", nickname = "updateGeographicAddressById", notes = "This operation updates an existing geographic address entity for the related party using its unique ID.", response = GeographicAddress.class, tags =
	{ "Geographic Address Management", })
	@ApiResponses(value =
	{
			@ApiResponse(code = 200, message = "Updated", response = GeographicAddress.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/geographicAddress/{id}", produces =
	{ "application/json;charset=utf-8" }, consumes =
	{ "application/json;charset=utf-8" }, method = RequestMethod.PATCH)
	@IsAuthorizedResourceOwnerOrAdmin
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<GeographicAddress> updateGeographicAddressById(
			@ApiParam(value = "Identifier of the GeographicAddress", required = true)
			@PathVariable("id")
			final String id,
			@ApiParam(value = "The Geographic Address to be updated", required = true) @Valid @RequestBody GeographicAddress geographicAddress,
			@NotNull
			@ApiParam(value = "The id of the related party.", required = true)
			@Valid
			@RequestParam(value = "relatedParty.id", required = true)
			final String relatedPartyId)
	{
		if (geographicAddress == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try
		{
			final AddressData addressData = tmaAddressFacade.getAddress(id, relatedPartyId);
			if (addressData == null)
			{
				return getUnsuccessfulResponseWithErrorRepresentation(NOT_FOUND_MESSAGE, null, RESOURCE_NOT_FOUND_ERROR_CODE, "Resource not found",
						HttpStatus.NOT_FOUND);
			}
			final AddressData payloadData = mergeExistingAddressData(geographicAddress, addressData);

			geographicAddress = getDataMapper().map(payloadData, GeographicAddress.class);
			final String errorMessage = validate(geographicAddress, "geographicAddress", tmaGeoraphicAddressValidator);

			if (StringUtils.isNotEmpty(errorMessage))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, BAD_REQUEST_ERROR_CODE, errorMessage, HttpStatus.BAD_REQUEST);
			}
			payloadData.setVisibleInAddressBook(true);
			final AddressData address = tmaAddressFacade.updateAddress(payloadData, id, relatedPartyId);

			if (address == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			geographicAddress = getDataMapper().map(address, GeographicAddress.class);
			return new ResponseEntity(getObjectMapper().writeValueAsString(geographicAddress), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException | IOException e)
		{
			return getUnsuccessfulResponse("Error occurred due to bad request", e, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to merge existing address with updated payload address.
	 *
	 * @param geographicAddressFromRequest
	 *           the {@link GeographicAddress}
	 * @param addressData
	 *           the address data
	 * @throws IOException
	 *            the IO Exception
	 * @throws JsonProcessingException
	 *            the JsonProcessing Exception
	 * @return AddressData
	 *         the Address Data
	 */
	private AddressData mergeExistingAddressData(GeographicAddress geographicAddressFromRequest, final AddressData addressData)
			throws IOException
	{
		final GeographicAddress geographicAddressFromDb = getDataMapper().map(addressData, GeographicAddress.class);
		final ObjectReader readerForUpdating = getObjectMapper().readerForUpdating(geographicAddressFromDb);
		geographicAddressFromRequest = readerForUpdating.readValue(getObjectMapper().writeValueAsString(geographicAddressFromRequest));
		final AddressData payloadData = getDataMapper().map(geographicAddressFromRequest, AddressData.class);
		return payloadData;
	}

	private boolean isRequestCorrect(final GeographicAddress geographicAddress)
	{
		return StringUtils.isNotEmpty(geographicAddress.getCountry()) && StringUtils.isNotEmpty(geographicAddress.getPostcode());
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

}
