/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(tags = "Product", description = "the Product API")
public interface TmaProductApi
{

	Logger log = LoggerFactory.getLogger(TmaProductApi.class);

	public static final String DEFAULT_RESPONSE = "{  \"isBundle\" : true,  \"productSpecification\" : {    \"@referredType\" : \"@referredType\",    \"describing\" : {      \"@type\" : \"@type\",      \"@schemaLocation\" : \"@schemaLocation\"    },    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\",    \"version\" : \"version\"  },  \"@type\" : \"@type\",  \"description\" : \"description\",  \"productOrder\" : [ {    \"@referredType\" : \"@referredType\",    \"orderItemId\" : \"orderItemId\",    \"id\" : \"id\",    \"href\" : \"href\",    \"orderItemAction\" : \"orderItemAction\"  }, {    \"@referredType\" : \"@referredType\",    \"orderItemId\" : \"orderItemId\",    \"id\" : \"id\",    \"href\" : \"href\",    \"orderItemAction\" : \"orderItemAction\"  } ],  \"billingAccount\" : [ {    \"@referredType\" : \"@referredType\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  }, {    \"@referredType\" : \"@referredType\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  } ],  \"characteristic\" : [ {    \"@type\" : \"@type\",    \"name\" : \"name\",    \"@schemaLocation\" : \"@schemaLocation\",    \"value\" : \"value\"  }, {    \"@type\" : \"@type\",    \"name\" : \"name\",    \"@schemaLocation\" : \"@schemaLocation\",    \"value\" : \"value\"  } ],  \"realizingService\" : [ {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  }, {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  } ],  \"terminationDate\" : \"2000-01-23\",  \"@baseType\" : \"@baseType\",  \"realizingResource\" : [ {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  }, {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  } ],  \"id\" : \"id\",  \"href\" : \"href\",  \"place\" : [ {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\",    \"@schemaLocation\" : \"@schemaLocation\"  }, {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\",    \"@schemaLocation\" : \"@schemaLocation\"  } ],  \"@schemaLocation\" : \"@schemaLocation\",  \"productOffering\" : {    \"@referredType\" : \"@referredType\",    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  },  \"relatedParty\" : [ {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"validFor\" : {      \"startDateTime\" : \"2001-01-23\",      \"endDateTime\" : \"2002-01-23\"    },    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  }, {    \"@referredType\" : \"@referredType\",    \"role\" : \"role\",    \"validFor\" : {      \"startDateTime\" : \"2003-01-23\",      \"endDateTime\" : \"2004-01-23\"    },    \"name\" : \"name\",    \"id\" : \"id\",    \"href\" : \"href\"  } ],  \"productTerm\" : [ {    \"duration\" : {      \"amount\" : 5.962134,      \"units\" : \"units\"    },    \"validFor\" : {      \"startDateTime\" : \"2005-01-23\",      \"endDateTime\" : \"2006-01-23\"    },    \"@type\" : \"@type\",    \"name\" : \"name\",    \"description\" : \"description\"  }, {    \"duration\" : {      \"amount\" : 5.962134,      \"units\" : \"units\"    },    \"validFor\" : {      \"startDateTime\" : \"2007-01-23\",      \"endDateTime\" : \"2008-01-23\"    },    \"@type\" : \"@type\",    \"name\" : \"name\",    \"description\" : \"description\"  } ],  \"productSerialNumber\" : \"productSerialNumber\",  \"name\" : \"name\",  \"productRelationship\" : [ {    \"product\" : {      \"id\" : \"id\",      \"href\" : \"href\"    },    \"type\" : \"type\"  }, {    \"product\" : {      \"id\" : \"id\",      \"href\" : \"href\"    },    \"type\" : \"type\"  } ],  \"isCustomerVisible\" : true,  \"startDate\" : \"2009-01-23\",  \"productPrice\" : [ {    \"unitOfMeasure\" : \"unitOfMeasure\",    \"@type\" : \"@type\",    \"price\" : {      \"taxRate\" : 6.0274563,      \"@type\" : \"@type\",      \"percentage\" : 0.8008282,      \"@schemaLocation\" : \"@schemaLocation\",      \"taxIncludedAmount\" : {        \"unit\" : \"unit\",        \"value\" : \"value\"      },      \"dutyFreeAmount\" : {        \"unit\" : \"unit\",        \"value\" : \"value\"      }    },    \"name\" : \"name\",    \"priceType\" : \"priceType\",    \"description\" : \"description\",    \"prodPriceAlteration\" : {      \"unitOfMeasure\" : \"unitOfMeasure\",      \"validFor\" : {        \"startDateTime\" : \"2010-01-23\",        \"endDateTime\" : \"2011-01-23\"      },      \"@type\" : \"@type\",      \"price\" : {        \"taxRate\" : 6.0274563,        \"@type\" : \"@type\",        \"percentage\" : 0.8008282,        \"@schemaLocation\" : \"@schemaLocation\",        \"taxIncludedAmount\" : {          \"unit\" : \"unit\",          \"value\" : \"value\"        },        \"dutyFreeAmount\" : {          \"unit\" : \"unit\",          \"value\" : \"value\"        }      },      \"name\" : \"name\",      \"priceType\" : \"priceType\",      \"description\" : \"description\",      \"id\" : \"id\",      \"recurringChargePeriod\" : \"recurringChargePeriod\",      \"priority\" : 1,      \"@schemaLocation\" : \"@schemaLocation\"    },    \"id\" : \"id\",    \"recurringChargePeriod\" : \"recurringChargePeriod\",    \"@schemaLocation\" : \"@schemaLocation\",    \"billingAccount\" : {      \"@referredType\" : \"@referredType\",      \"name\" : \"name\",      \"id\" : \"id\",      \"href\" : \"href\"    }  }, {    \"unitOfMeasure\" : \"unitOfMeasure\",    \"@type\" : \"@type\",    \"price\" : {      \"taxRate\" : 6.0274563,      \"@type\" : \"@type\",      \"percentage\" : 0.8008282,      \"@schemaLocation\" : \"@schemaLocation\",      \"taxIncludedAmount\" : {        \"unit\" : \"unit\",        \"value\" : \"value\"      },      \"dutyFreeAmount\" : {        \"unit\" : \"unit\",        \"value\" : \"value\"      }    },    \"name\" : \"name\",    \"priceType\" : \"priceType\",    \"description\" : \"description\",    \"prodPriceAlteration\" : {      \"unitOfMeasure\" : \"unitOfMeasure\",      \"validFor\" : {        \"startDateTime\" : \"2012-01-23\",        \"endDateTime\" : \"2013-01-23\"      },      \"@type\" : \"@type\",      \"price\" : {        \"taxRate\" : 6.0274563,        \"@type\" : \"@type\",        \"percentage\" : 0.8008282,        \"@schemaLocation\" : \"@schemaLocation\",        \"taxIncludedAmount\" : {          \"unit\" : \"unit\",          \"value\" : \"value\"        },        \"dutyFreeAmount\" : {          \"unit\" : \"unit\",          \"value\" : \"value\"        }      },      \"name\" : \"name\",      \"priceType\" : \"priceType\",      \"description\" : \"description\",      \"id\" : \"id\",      \"recurringChargePeriod\" : \"recurringChargePeriod\",      \"priority\" : 1,      \"@schemaLocation\" : \"@schemaLocation\"    },    \"id\" : \"id\",    \"recurringChargePeriod\" : \"recurringChargePeriod\",    \"@schemaLocation\" : \"@schemaLocation\",    \"billingAccount\" : {      \"@referredType\" : \"@referredType\",      \"name\" : \"name\",      \"id\" : \"id\",      \"href\" : \"href\"    }  } ],  \"status\" : { }}";


	default Optional<HttpServletRequest> getRequest()
	{
		return Optional.empty();
	}

	default Optional<String> getAcceptHeader()
	{
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	@ApiOperation(value = "Retrieve product", nickname = "productGet", notes = "This operation retrieves a product entity. Attribute selection is enabled for all first level attributes. Filtering on sub-resources may be available depending on the compliance level supported by an implementation.  Specific business errors for current operation will be encapsulated in  HTTP Response 422 Unprocessable entity ", response = TmaProductWsDto.class, tags =
	{ "Product", })
	@ApiResponses(value =
	{ @ApiResponse(code = 200, message = "Success", response = TmaProductWsDto.class),
			@ApiResponse(code = 400, message = "Bad Request  List of supported error codes: - 20: Invalid URL parameter value - 21: Missing body - 22: Invalid body - 23: Missing body field - 24: Invalid body field - 25: Missing header - 26: Invalid header value - 27: Missing query-string parameter - 28: Invalid query-string parameter value", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 404, message = "Not Found  List of supported error codes: - 60: Resource not found", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 405, message = "Method Not Allowed  List of supported error codes: - 61: Method not allowed", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 409, message = "Conflict  The request could not be completed due to a conflict with the current state of the target resource.", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = TmaErrorRepresentationWsDto.class),
			@ApiResponse(code = 500, message = "Internal Server Error  List of supported error codes: - 1: Internal error", response = TmaErrorRepresentationWsDto.class) })
	@RequestMapping(value = "/product/{productId}", produces =
	{ "application/json", "application/xml" }, method = RequestMethod.GET)

	ResponseEntity<Object> productGet(
			@ApiParam(value = "", required = true) @PathVariable("Identifier of Product ") final String productId,
			@ApiParam(value = "Attributes selection") @Valid @RequestParam(value = "Response configuration. This is the list of fields that should be returned in the response body", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId);


}
