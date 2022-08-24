/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.controller;

import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementservices.services.BmPaginationService;
import de.hybris.platform.billmanagementservices.services.BmPartyBillService;
import de.hybris.platform.billmanagementtmfwebservices.v1.api.CustomerBillApi;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Error;
import de.hybris.platform.billmanagementtmfwebservices.v1.security.BmAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
 * Default implementation of controller for {@link CustomerBillApi}.
 *
 * @since 2108
 */
@Controller
@Api(tags = "Customer Bill Management")
public class BmCustomerBillController extends BmBaseController implements CustomerBillApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private final HttpServletRequest request;

	@Resource(name = "bmPartyBillService")
	private BmPartyBillService bmPartyBillService;

	@Resource(name = "bmPaginationService")
	private BmPaginationService bmPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	public BmCustomerBillController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "List or find CustomerBill objects", nickname = "listCustomerBill", notes = "This operation list or find CustomerBill entities", response = CustomerBill.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = CustomerBill.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/customerBill", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<List<CustomerBill>> listCustomerBill(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Related party id value") @Valid @RequestParam(value = "relatedParty.id", required = false) String relatedPartyId)
	{
		final BmPartyBillContext bmPartyBillContext = new BmPartyBillContext();
		bmPartyBillContext.setRelatedPartyId(relatedPartyId);

		if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
		{
			final UserModel user = getUserService()
					.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			bmPartyBillContext.setRelatedPartyId(user.getBmParty().getId());
		}

		offset = getBmPaginationService().checkOffset(offset);
		limit = getBmPaginationService().checkLimit(limit);

		final List<BmPartyBillModel> partyBillModels = getBmPartyBillService().getPartyBills(bmPartyBillContext, offset, limit);
		final Integer totalCount = getBmPartyBillService().getNumberOfPartyBillsFor(bmPartyBillContext);
		final List<CustomerBill> customerBillsDtos = new ArrayList<>();
		partyBillModels
				.forEach(partyBillModel -> customerBillsDtos.add(getDataMapper().map(partyBillModel, CustomerBill.class, fields)));

		try
		{
			if (limit < totalCount || offset != 0)
			{
				final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
				final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
				getBmPaginationService().addEntryWithPaginationDetails(header, filter(request.getRequestURL().toString()),
						queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);

				return new ResponseEntity(getObjectMapper().writeValueAsString(customerBillsDtos), header,
						HttpStatus.PARTIAL_CONTENT);
			}
			return new ResponseEntity(getObjectMapper().writeValueAsString(customerBillsDtos), HttpStatus.OK);
		}
		catch (JsonProcessingException ex)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, ex, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@ApiOperation(value = "Retrieves a CustomerBill by ID", nickname = "retrieveCustomerBill", notes = "This operation retrieves a CustomerBill entity. Attribute selection is enabled for all first level attributes.", response = CustomerBill.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = CustomerBill.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@BmAuthorizedResourceOwnerOrAdmin
	@RequestMapping(value = "/customerBill/{id:.+}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<CustomerBill> retrieveCustomerBill(
			@ApiParam(value = "Identifier of the CustomerBill", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Comma-separated properties to provide in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			final BmPartyBillModel partyBillModel = getBmPartyBillService().getPartyBill(id);

			final CustomerBill customerBillDto = getDataMapper().map(partyBillModel, CustomerBill.class, fields);

			return new ResponseEntity(getObjectMapper().writeValueAsString(customerBillDto), HttpStatus.OK);
		}
		catch (JsonProcessingException ex)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, ex, HttpStatus.BAD_REQUEST);
		}
		catch (ModelNotFoundException ex)
		{
			return getUnsuccessfulResponse(NOT_FOUND, ex, HttpStatus.NOT_FOUND);
		}
	}

	public BmPartyBillService getBmPartyBillService()
	{
		return bmPartyBillService;
	}

	public BmPaginationService getBmPaginationService()
	{
		return bmPaginationService;
	}

	protected UserService getUserService()
	{
		return userService;
	}
}
