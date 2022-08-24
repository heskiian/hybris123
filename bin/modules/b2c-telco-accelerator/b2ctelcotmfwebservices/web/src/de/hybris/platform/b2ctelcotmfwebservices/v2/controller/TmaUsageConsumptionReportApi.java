/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaUsageConsumptionReportWsDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Interface {@link TmaUsageConsumptionReportApi} exposing operations related to UsageReport.
 *
 * @since 1810.
 */

@Api(tags = "UsageConsumptionReport", description = "the UsageConsumptionReport API")
public interface TmaUsageConsumptionReportApi
{
	
	public static final Logger LOG = LoggerFactory.getLogger(TmaUsageConsumptionReportApi.class);//NOSONAR

	default Optional<HttpServletRequest> getRequest()
	{
		return Optional.empty();
	}

	default Optional<String> getAcceptHeader()
	{
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	@ApiOperation(value = "Query the calculation of an usage consumption report in synchronous mode", nickname = "usageConsumptionReportFind", notes = "This operation is used to request the calculation of a new usage consumption report for a specific product identified by a msisdn number for example.  Attribute selection is enabled for all first level attributes. Filtering may be available depending on the compliance level supported by an implementation.  Specific business errors for current operation will be encapsulated in  HTTP Response 422 Unprocessable entity ", response = TmaUsageConsumptionReportWsDto.class, responseContainer = "array", tags =
	{ "UsageConsumptionReport", })
	@ApiResponses(value =
	{ @ApiResponse(code = 200, message = "Success", response = TmaUsageConsumptionReportWsDto.class, responseContainer = "array"),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 405, message = "Method Not Allowed  List of supported error codes: - 61: Method not allowed", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 500, message = "Internal Server Error  List of supported error codes: - 1: Internal error", response = TmaErrorRepresentationWsDto.class) })
	@RequestMapping(value = "/usageConsumptionReport", method = RequestMethod.GET, produces =
	{ "application/xml", "application/json" })
	ResponseEntity<Object> usageConsumptionReportFind(
			@ApiParam(value = "SubscriberIdentity contains Id of subcriptionbase", required = true) @Valid @RequestParam(value = "subscriptionBase.id", required = true) final String subscriptionBaseId,
			@ApiParam(value = "Attribute selection") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Susbcribed Product ID") @Valid @RequestParam(value = "product.id", required = false) final String productId,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId);
}
