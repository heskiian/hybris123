/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.SubscriptionBaseApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBase;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
 * Default implementation of {@link SubscriptionBaseApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "subscriptionBase", description = "SubscriptionBase management API", tags = { "Customer Product Inventory" })
public class TmaSubscriptionBaseApiController extends TmaBaseController implements SubscriptionBaseApi
{
	@Resource(name = "tmaSubscriptionBaseFacade")
	private TmaSubscriptionBaseFacade subscriptionBaseFacade;

	@ApiOperation(value = "List or find 'SubscriptionBase' objects", nickname = "listSubscriptionBase", response = SubscriptionBase.class,
			responseContainer = "List", tags = { "Customer Product Inventory", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = SubscriptionBase.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/subscriptionBase",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<SubscriptionBase>> listSubscriptionBase(
			@NotNull @ApiParam(value = "Unique identifier of the Related Party", required = true) @Valid @RequestParam(value = "subscriptionAccess.relatedParty.id") String relatedPartyId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			List<TmaDetailedSubscriptionBaseData> detailedSubscriptionBaseDataList = subscriptionBaseFacade
					.findSubscriptionBasesByPrincipal(relatedPartyId);

			if (CollectionUtils.isEmpty(detailedSubscriptionBaseDataList))
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<SubscriptionBase> subscriptionBaseList = new ArrayList<>();
			for (TmaDetailedSubscriptionBaseData detailedSubscriptionBaseData : detailedSubscriptionBaseDataList)
			{
				subscriptionBaseList.add(getDataMapper().map(detailedSubscriptionBaseData, SubscriptionBase.class, fields));
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(subscriptionBaseList), HttpStatus.OK);
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
}
