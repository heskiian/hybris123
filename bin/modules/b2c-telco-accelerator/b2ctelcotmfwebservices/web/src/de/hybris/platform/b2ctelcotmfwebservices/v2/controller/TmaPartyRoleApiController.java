/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.PartyRoleApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PartyRole;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


@Controller
@Api(value = "partyRole", description = "PartyRole Management API", tags = { "Party Role Management" })
public class TmaPartyRoleApiController extends TmaBaseController implements PartyRoleApi
{
	private static final Log LOG = LogFactory.getLog(TmaPartyRoleApiController.class);
	@Resource(name = "tmaSubscriptionAccessFacade")
	private TmaSubscriptionAccessFacade subscriptionAccessFacade;
	@Resource(name = "customerFacade")
	private TmaCustomerFacade customerFacade;

	@Override
	@ApiOperation(value = "Retrieves a 'PartyRole' by Id", nickname = "retrievePartyRole", notes = "", response = PartyRole.class, responseContainer = "List", tags = {
			"Party Role Management" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = PartyRole.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/partyRole/{id:.+}",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<PartyRole>> retrievePartyRole(
			@ApiParam(value = "Identifier of the Party Role", required = true) @PathVariable("id") String relatedPartyId,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{

		try
		{
			CustomerData principal = getCustomerFacade().getUserForUID(relatedPartyId);
			final PartyRole partyRole = getDataMapper().map(principal, PartyRole.class, fields);

			List<TmaSubscriptionAccessData> subscriptionAccessData = getSubscriptionAccessFacade()
					.findSubscriptionAccessesByPrincipal(principal.getUid());

			subscriptionAccessData.forEach((TmaSubscriptionAccessData subscriptionAccess) ->
					getDataMapper().map(subscriptionAccess, partyRole, fields));

			return new ResponseEntity(getObjectMapper().writeValueAsString(Collections.singletonList(partyRole)), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	protected TmaSubscriptionAccessFacade getSubscriptionAccessFacade()
	{
		return subscriptionAccessFacade;
	}

	protected TmaCustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}
}
