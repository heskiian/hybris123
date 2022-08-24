/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.pagination.TmaPaginationFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductFacade;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdminOrAnonymous;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ReviewApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ErrorRepresentation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Review;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
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
 * Default implementation of {@link ReviewApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "review", description = "Review API", tags = { "Review" })
public class TmaReviewApiController extends TmaBaseController implements ReviewApi
{
	private final HttpServletRequest request;

	/*
	 *	Pagination facade
	 */
	@Resource(name = "tmaPaginationFacade")
	private TmaPaginationFacade tmaPaginationFacade;

	/**
	 * Product facade
	 */
	@Resource(name = "productFacade")
	private TmaProductFacade tmaProductFacade;

	/**
	 * Review validator
	 */
	@Resource(name = "tmaReviewValidator")
	private Validator tmaReviewValidator;
	
	private final String[] DISALLOWED_FIELDS = new String[] {};

	@Autowired
	public TmaReviewApiController(HttpServletRequest request)
	{
		this.request = request;
	}

	@ApiOperation(value = "Creates a 'Review'", nickname = "createReview", notes = "", response = Review.class, tags = {
			"Review", })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Review.class),
			@ApiResponse(code = 400, message = "Bad Request", response = ErrorRepresentation.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = ErrorRepresentation.class),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found", response = ErrorRepresentation.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = ErrorRepresentation.class),
			@ApiResponse(code = 422, message = "Unprocessable entity  Functional error", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/review",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@IsAuthorizedResourceOwnerOrAdminOrAnonymous
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Review> createReview(
			@ApiParam(value = "The 'Review' resource to be created", required = true) @Valid @RequestBody Review review,
			@ApiParam(value = "Identifier of the Product Offering", required = true) @Valid @RequestParam(value =
					"productOffering.id", required = true) String productOfferingId,
			@ApiParam(value = "The id of the related party.", required = true) @Valid @RequestParam(value = "relatedParty"
					+ ".id", required = true) String relatedPartyId)
	{
		if (review == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(review, "review", tmaReviewValidator);

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, null, 22, errorMessage,
					HttpStatus.BAD_REQUEST);
		}

		try
		{
			ReviewData reviewData = getDataMapper().map(review, ReviewData.class);

			ReviewData reviewCreated = tmaProductFacade.createReview(productOfferingId, relatedPartyId, reviewData);

			if (reviewCreated == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			Review reviewDto = getDataMapper().map(reviewCreated, Review.class);

			return new ResponseEntity(getObjectMapper().writeValueAsString(reviewDto), HttpStatus.CREATED);
		}
		catch (UnknownIdentifierException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, NOT_FOUND,
					HttpStatus.NOT_FOUND);
		}
		catch (JsonProcessingException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, BAD_REQUEST,
					HttpStatus.BAD_REQUEST);
		}
		catch (final InternalServerErrorException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "List reviews", nickname = "listReview", response = Review.class, responseContainer = "List", tags = {
			"Review", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = Review.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = ErrorRepresentation.class),
			@ApiResponse(code = 404, message = "Not Found", response = ErrorRepresentation.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorRepresentation.class) })
	@RequestMapping(value = "/review",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	public ResponseEntity<List<Review>> listReview(
			@NotNull @ApiParam(value = "Identifier of the Product Offering", required = true) @Valid @RequestParam(value =
					"productOffering.id") String productOfferingId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response requested by client.") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response requested by client.") @Valid @RequestParam(value = "limit", required = false) Integer limit
	)
	{
		try
		{
			offset = tmaPaginationFacade.checkOffset(offset);
			limit = tmaPaginationFacade.checkLimit(limit);

			final List<ReviewData> reviews = tmaProductFacade.getReviewsByLimitAndOffset(productOfferingId, offset, limit);

			if (CollectionUtils.isEmpty(reviews))
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			final List<Review> reviewList = new ArrayList<>();
			for (ReviewData reviewData : reviews)
			{
				final Review review = getDataMapper().map(reviewData, Review.class, fields);
				reviewList.add(review);
			}

			final Long totalCount = Long.valueOf(tmaProductFacade.getNumberOfReviewsByLanguage(productOfferingId));
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			tmaPaginationFacade.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, totalCount, limit, offset);

			if (limit < totalCount || offset > totalCount || offset != 0)
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(reviewList),
						header, HttpStatus.PARTIAL_CONTENT);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(reviewList), HttpStatus.OK);
		}
		catch (final ConversionException | JsonProcessingException | NumberFormatException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, BAD_REQUEST,
					HttpStatus.BAD_REQUEST);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, NOT_FOUND,
					HttpStatus.NOT_FOUND);
		}
		catch (final InternalServerErrorException e)
		{
			return getUnsuccessfulResponseWithErrorRepresentation(null, e, TmaApiResponseMessage.ERROR, INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
