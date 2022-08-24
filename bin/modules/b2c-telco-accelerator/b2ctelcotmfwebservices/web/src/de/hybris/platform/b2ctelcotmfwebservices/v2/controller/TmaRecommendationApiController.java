/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferContextData;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.pagination.TmaPaginationFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.RecommendationApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessTypeRecommendation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
 * Default implementation of {@link RecommendationApi}.
 *
 * @since 1907
 */

@Controller
@SuppressWarnings("unused")
@Api(value = "recommendation", description = "the recommendation API", tags = { "Recommendation" })
public class TmaRecommendationApiController extends TmaBaseController implements RecommendationApi
{
	/**
	 * Product offering facade
	 */
	@Resource(name = "tmaProductOfferFacade")
	private TmaProductOfferFacade tmaProductOfferFacade;

	/**
	 * Subscription facade
	 */
	@Resource(name = "tmaSubscriptionBaseFacade")
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;

	/**
	 * Customer facade
	 */
	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	/**
	 * Pagination facade
	 */
	@Resource(name = "tmaPaginationFacade")
	private TmaPaginationFacade tmaPaginationFacade;

	/**
	 * User facade
	 */
	@Resource(name = "userFacade")
	private UserFacade userFacade;

	/**
	 * Http request
	 */
	private final HttpServletRequest request;

	/**
	 * Undercore serarator
	 */
	private static final String SEPARATOR = "_";

	/**
	 * Regexp used for checking if subscription base id is correct.
	 */
	private static final String SUBSCRIPTION_BASE_ID_REGEXP = "^\\w+__[^_]+$";

	@Autowired
	public TmaRecommendationApiController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@ApiOperation(value = "List or find 'Recommendation' objects", nickname = "listRecommendation", response = Recommendation.class, responseContainer = "List", tags = {
			"Recommendation", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = Recommendation.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/recommendation",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<Recommendation>> listRecommendation(
			@ApiParam(value = "The identifier of the related party") @Valid @RequestParam(value = "relatedParty.id", required = false) String relatedPartyId,
			@ApiParam(value = "The identifier of the subscription base") @Valid @RequestParam(value = "subscriptionBase.id", required = false) String subscriptionBaseId,
			@ApiParam(value = "The identifier of the process type") @Valid @RequestParam(value = "processType.id", required = false) String processTypeId,
			@ApiParam(value = "The identifier of the product offering term") @Valid @RequestParam(value = "productOfferingTerm.id", required = false) String productOfferingTermId,
			@ApiParam(value = "The identifier of the related product offering") @Valid @RequestParam(value = "relatedProductOffering.id", required = false) String relatedProductOfferingId,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit)
	{
		try
		{
			if (StringUtils.isEmpty(relatedPartyId) || StringUtils.isEmpty(processTypeId) || StringUtils
					.isEmpty(relatedProductOfferingId) || StringUtils.isEmpty(subscriptionBaseId))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 22, "Missing required fields.",
						HttpStatus.BAD_REQUEST);
			}

			if (!userFacade.isUserExisting(relatedPartyId))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 22,
						"Invalid related party id '" + relatedPartyId + "'.", HttpStatus.BAD_REQUEST);
			}

			final TmaSubscriptionBaseData subscriptionBaseData = getSubscriptionBase(subscriptionBaseId);

			if (subscriptionBaseData == null)
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 22,
						"Invalid subscription base '" + subscriptionBaseId + "'.", HttpStatus.BAD_REQUEST);
			}

			if (!isSubscriptionEligible(subscriptionBaseData, processTypeId, relatedPartyId))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 22,
						"Related party '" + relatedPartyId + "' is not eligible for '" + processTypeId + "' with subscription base '"
								+ subscriptionBaseId + "'.", HttpStatus.BAD_REQUEST);
			}

			final Set<String> requiredProductCodes = tmaCustomerFacade
					.getMainTariffProductCodesForSubscriptionBases(Arrays.asList(subscriptionBaseData));

			if (requiredProductCodes.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			String bpoCode = "";

			if (subscriptionBaseData.getTmaSubscribedProductData() != null)
			{
				bpoCode = subscriptionBaseData.getTmaSubscribedProductData().iterator().next().getParentPOCode();
			}

			bpoCode = bpoCode != null ? bpoCode : "";

			final TmaOfferContextData offerContextData = new TmaOfferContextData();
			offerContextData.setAffectedProductCode(relatedProductOfferingId);
			offerContextData.setBpoCode(bpoCode);
			offerContextData.setProcessType(TmaProcessType.valueOf(processTypeId));
			offerContextData.setRequiredProductCodes(requiredProductCodes);
			offerContextData.setSubscriptionTermId(productOfferingTermId);
			offerContextData.setUser(relatedPartyId);

			List<TmaOfferData> recommendationProductOfferingDataList = tmaProductOfferFacade.getOffers(offerContextData);

			if (recommendationProductOfferingDataList.isEmpty())
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(Collections.emptyList()), HttpStatus.OK);
			}

			offset = tmaPaginationFacade.checkOffset(offset);
			limit = tmaPaginationFacade.checkLimit(limit);

			final long totalCount = recommendationProductOfferingDataList.size();

			recommendationProductOfferingDataList =
					tmaPaginationFacade.filterListByOffsetAndLimit(offset, limit, recommendationProductOfferingDataList);

			final List<Recommendation> recommendationList = new ArrayList<>();

			for (TmaOfferData offerData : recommendationProductOfferingDataList)
			{
				final PrincipalData principalData = new PrincipalData();
				principalData.setUid(relatedPartyId);
				offerData.setPrincipalData(principalData);
				offerData.setSubscriptionBase(subscriptionBaseData);
				offerData.setCode(createCode(offerData));
				final Recommendation recommendation = getDataMapper().map(offerData, ProcessTypeRecommendation.class,
						fields);
				recommendationList.add(recommendation);
			}

			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			tmaPaginationFacade.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
					getQueryStringWithoutOffsetAndLimit(request), totalCount, limit, offset);

			if (limit < totalCount || offset != 0)
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(recommendationList),
						header, HttpStatus.PARTIAL_CONTENT);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(recommendationList), HttpStatus.OK);

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

	/**
	 * Creates the unique id for the offer from the principalData.id, subscriptionBase.subscriberIdentity, subscriptionBase
	 * .billingSystemId, processType.id, relatedProduct.id and channel.id separated with underscore separator.
	 *
	 * @param offerData
	 * 		The offerData which contains the information to create the id.
	 * @return The identifier of the offerData.
	 */
	private String createCode(TmaOfferData offerData)
	{
		final StringBuilder id = new StringBuilder();

		if (offerData.getPrincipalData() != null && StringUtils.isNotEmpty(offerData.getPrincipalData().getUid()))
		{
			id.append(offerData.getPrincipalData().getUid());
		}

		if (offerData.getSubscriptionBase() != null && StringUtils
				.isNotEmpty(offerData.getSubscriptionBase().getCode()))
		{
			id.append(SEPARATOR).append(offerData.getSubscriptionBase().getCode());
		}

		if (offerData.getProcessType() != null)
		{
			id.append(SEPARATOR).append(offerData.getProcessType().getCode());
		}

		if (offerData.getProduct() != null && StringUtils.isNotEmpty(offerData.getProduct().getCode()))
		{
			id.append(SEPARATOR).append(offerData.getProduct().getCode());
		}

		return id.toString();
	}

	private boolean isSubscriptionEligible(final TmaSubscriptionBaseData subscriptionBaseData, final String processType,
			final String relatedPartyId)
	{
		tmaCustomerFacade.updateEligibilityContextsByCustomer(relatedPartyId);
		final Set<TmaSubscriptionBaseData> eligibleSubscriptions = tmaCustomerFacade
				.retrieveEligibleSubscriptions(TmaProcessType.valueOf(processType), relatedPartyId);

		Optional<TmaSubscriptionBaseData> subscription =
				eligibleSubscriptions.stream().filter((TmaSubscriptionBaseData eligibleSubscription) ->
						eligibleSubscription.getSubscriberIdentity().equals(subscriptionBaseData.getSubscriberIdentity())
								&& eligibleSubscription.getBillingSystemId().equals(subscriptionBaseData.getBillingSystemId()))
						.findFirst();
		return subscription.isPresent();
	}

	private TmaSubscriptionBaseData getSubscriptionBase(final String subscriptionBaseId)
	{
		if (!subscriptionBaseId.matches(SUBSCRIPTION_BASE_ID_REGEXP))
		{
			return null;
		}

		final String[] splitSubscriptionBaseId = subscriptionBaseId.split("__");
		final String subscriberIdentity = splitSubscriptionBaseId[0];
		final String billingSystemId = splitSubscriptionBaseId[1];
		return tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity(subscriberIdentity, billingSystemId);
	}
}
