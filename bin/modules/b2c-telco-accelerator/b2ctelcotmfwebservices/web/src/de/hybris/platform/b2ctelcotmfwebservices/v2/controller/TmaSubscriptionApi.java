/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseWsDto;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Interface {@link TmaSubscriptionApi} exposing operations related to Subscriptions.
 *
 * @since 1810.
 */
@Api(tags = "Subscription", description = "the Subscription API")
public interface TmaSubscriptionApi
{
	default Optional<HttpServletRequest> getRequest()
	{
		return Optional.empty();
	}

	default Optional<String> getAcceptHeader()
	{
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	@ApiOperation(value = "get Subscriptionbase for given Id", nickname = "findSubscriptionbaseForId", notes = "This operation get Subscriptionbase entity. Attribute selection is enabled for all first level attributes. Filtering may be available depending on the compliance level supported by an implementation.  Specific business errors for current operation will be encapsulated in  HTTP Response 422 Unprocessable entity ", response = TmaSubscriptionBaseWsDto.class, responseContainer = "array", tags =
	{ "Subscription", })
	@ApiResponses(value =
	{ @ApiResponse(code = 200, message = "Success", response = TmaSubscriptionBaseWsDto.class, responseContainer = "array"),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 405, message = "Method Not Allowed  List of supported error codes: - 61: Method not allowed", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 409, message = "Conflict  The request could not be completed due to a conflict with the current state of the target resource.", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 500, message = "Internal Server Error  List of supported error codes: - 1: Internal error", response = TmaErrorRepresentationWsDto.class) })
	@RequestMapping(value = "/subscriptionbase/{subscriptionBaseId}", produces =
	{ "application/json", "application/xml" }, method = RequestMethod.GET)
	ResponseEntity<Object> findSubscriptionbaseForId(
			@ApiParam(value = "Id for subscriptionBase selection", required = true) @PathVariable("subscriptionBaseId") final String subscriptionBaseId,
			@ApiParam(value = "Attributes selection") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId);

}
