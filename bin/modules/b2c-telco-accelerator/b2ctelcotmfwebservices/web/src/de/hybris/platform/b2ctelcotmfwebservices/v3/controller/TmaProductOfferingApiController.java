/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcofacades.pagination.TmaFacetSearchPaginationFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductSearchFacade;
import de.hybris.platform.b2ctelcofacades.product.TmaProductSortFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedUser;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v3.api.ProductOfferingApi;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.IndexedProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * Default implementation of {@link ProductOfferingApi}
 *
 * @since 2007
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "productOffering", description = "Catalog Management API", tags = { "Product Catalog Management" })
public class TmaProductOfferingApiController extends TmaBaseController implements ProductOfferingApi
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaProductOfferingApiController.class);

	private static final Collection<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.DESCRIPTION,
			ProductOption.SOLD_INDIVIDUALLY, ProductOption.PRODUCT_SPECIFICATION, ProductOption.CATEGORIES,
			ProductOption.PRODUCT_MEDIA, ProductOption.PRODUCT_BPO_CHILDREN, ProductOption.PRODUCT_OFFERING_PRICES,
			ProductOption.PARENT_BPOS, ProductOption.PRODUCT_OFFERING_GROUPS, ProductOption.PRODUCT_SPEC_CHAR_VALUE_USE,
			ProductOption.CLASSIFICATION, ProductOption.VARIANT_MATRIX, ProductOption.VARIANT_MATRIX_ALL_OPTIONS,
			ProductOption.MEDIA_ATTACHMENT);

	private static final String APPLY_ELIGIBILITY_FLAG = "applyEligibility";

	private final HttpServletRequest request;

	@Resource(name = "tmaProductOfferFacade")
	private TmaProductOfferFacade tmaProductOfferingFacade;

	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	@Resource(name = "productSearchFacade")
	private TmaProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "tmaFacetSearchPaginationFacade")
	private TmaFacetSearchPaginationFacade tmaFacetSearchPaginationFacade;

	@Resource(name = "tmaProductSortFacade")
	private TmaProductSortFacade productSortFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Autowired
	public TmaProductOfferingApiController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@ApiOperation(value = "Retrieves a 'ProductOffering' by Id", nickname = "retrieveProductOffering", notes = "This operation retrieves a product offering entity using its unique ID", response = ProductOffering.class, responseContainer = "List", tags =
			{ "Product Catalog Management", })
	@ApiResponses(value =
			{ @ApiResponse(code = 200, message = "Ok", response = ProductOffering.class, responseContainer = "List"),
					@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
					@ApiResponse(code = 404, message = "Not Found", response = Error.class),
					@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/productOffering/{id}", produces =
			{ "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsAuthorizedUser
	@SuppressWarnings("unchecked")
	public ResponseEntity<List<ProductOffering>> retrieveProductOffering(
			@ApiParam(value = "Identifier of the Product Offering", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId,
			@ApiParam(value = "For filtering by the process type of the product offering price.") @Valid @RequestParam(value = "priceContext.processType.id", required = false) final List<String> priceProcessTypeId,
			@ApiParam(value = "Identifier of the place for which product offering has prices available") @Valid @RequestParam(value = "priceContext.place.id", required = false) final String priceContextPeriodplacePeriodId,
			@ApiParam(value = "For filtering: Identifier of the related party for which the price applies.") @Valid @RequestParam(value = "priceContext.relatedParty.id", required = false) final String relatedPartyId)

	{
		try
		{
			final String currentUserId = tmaCustomerFacade.getCurrentCustomer().getUid();
			final TmaPriceContextData priceContextData = new TmaPriceContextData();
			final Boolean eligibilityFlag = sessionService.getAttribute(APPLY_ELIGIBILITY_FLAG);

			if (StringUtils.isNotBlank(currentUserId) && Boolean.TRUE.equals(eligibilityFlag))
			{
				priceContextData.setUserId(currentUserId);
			}

			if (StringUtils.isNotEmpty(priceContextPeriodplacePeriodId))
			{
				final Set<String> regionIsoCodes = new HashSet<>();
				regionIsoCodes.add(priceContextPeriodplacePeriodId);
				priceContextData.setRegionIsoCodes(regionIsoCodes);
			}

			if (CollectionUtils.isNotEmpty(priceProcessTypeId))
			{
				priceContextData.setProcessTypeCodes(new HashSet<>(priceProcessTypeId));
			}

			priceContextData.setProductCode(id);

			final ProductData productOfferingData = tmaProductOfferingFacade.getPoForCode(id, PRODUCT_OPTIONS, priceContextData);

			final ProductOffering productOffering = getDataMapper().map(productOfferingData, ProductOffering.class, fields);

			final List<ProductOffering> productOfferings = new ArrayList<>();
			productOfferings.add(productOffering);
			return new ResponseEntity(getObjectMapper().writeValueAsString(productOfferings), HttpStatus.OK);

		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
		catch (final JsonProcessingException e)
		{
			LOG.error("There has been an issue in processing the response", e);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves a list of 'ProductOffering'", nickname = "listProductOffering", notes = "This operation retrieves a list of product offering entities", response =
			de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering.class, responseContainer = "List", tags =
			{ "Product Catalog Management", })
	@ApiResponses(value =
			{ @ApiResponse(code = 200, message = "Ok", response =
					de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering.class, responseContainer = "List"),
					@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
					@ApiResponse(code = 404, message = "Not Found", response = Error.class),
					@ApiResponse(code = 405, message = "Method Not Allowed", response = Error.class),
					@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/productOffering", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@IsAuthorizedUser
	@Override
	@SuppressWarnings(
			{ "unchecked", "Duplicates" })
	public ResponseEntity<List<de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering>> listProductOffering(
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "For filtering: Name of the productOffering") @Valid @RequestParam(value = "name", required = false) final String name,
			@ApiParam(value = "For filtering: Id of the productOfferingGroup") @Valid @RequestParam(value = "offeringGroup.id", required = false) final String offeringGroupId,
			@ApiParam(value = "For filtering: isBundle determines whether a productOffering represents a single productOffering (false), or a bundle of productOfferings (true).") @Valid @RequestParam(value = "isBundle", required = false) final Boolean isBundle,
			@ApiParam(value = "For filtering: Id of the parentBundledProductOffering") @Valid @RequestParam(value = "parentBundledProductOffering.id", required = false) final String parentBundledProductOfferingId,
			@ApiParam(value = "For filtering: Date and time of the last update") @Valid @RequestParam(value = "lastUpdate", required = false) final Date lastUpdate,
			@ApiParam(value = "For filtering: Used to indicate the current lifecycle status") @Valid @RequestParam(value = "lifecycleStatus", required = false) final String lifecycleStatus,
			@ApiParam(value = "For filtering: An instant of time, starting at the TimePeriod") @Valid @RequestParam(value = "validFor.startDateTime", required = false) final Date validForPeriodstartDateTime,
			@ApiParam(value = "For filtering: An instant of time, ending at the TimePeriod.") @Valid @RequestParam(value = "validFor.endDateTime", required = false) final Date validForPeriodendDateTime,
			@ApiParam(value = "For filtering: ProductOffering version") @Valid @RequestParam(value = "version", required = false) final String version,
			@ApiParam(value = "For filtering: Class type of the product offering") @Valid @RequestParam(value = "@type", required = false) final String attype,
			@ApiParam(value = "For filtering: Immediate base (class) type of the product offering") @Valid @RequestParam(value = "@baseType", required = false) final String atbaseType,
			@ApiParam(value = "For filtering: A link to the schema describing this product offering") @Valid @RequestParam(value = "@schemaLocation", required = false) final String atschemaLocation,
			@ApiParam(value = "For filtering: A user-friendly name for the place, such as \"Paris Store\", \"London Store\", \"Main Home\"") @Valid @RequestParam(value = "place.name", required = false) final String placePeriodname,
			@ApiParam(value = "For filtering: A string characterizing an address (for instance a formatted address or an identifier taken from an address database or an address API).") @Valid @RequestParam(value = "place.address", required = false) final String placePeriodaddress,
			@ApiParam(value = "For filtering: class type of the referred Place object") @Valid @RequestParam(value = "place.@referredType", required = false) final String placePeriodAtreferredType,
			@ApiParam(value = "For filtering: Role of the place (for instance: 'home delivery', 'shop retrieval')") @Valid @RequestParam(value = "place.role", required = false) final String placePeriodrole,
			@ApiParam(value = "For filtering: Name of the service level agreement") @Valid @RequestParam(value = "serviceLevelAgreement.name", required = false) final String serviceLevelAgreementPeriodname,
			@ApiParam(value = "For filtering: class type of referred Service Level Agreement") @Valid @RequestParam(value = "serviceLevelAgreement.@referredType", required = false) final String serviceLevelAgreementPeriodAtreferredType,
			@ApiParam(value = "For filtering: Version of the product specification") @Valid @RequestParam(value = "productSpecification.version", required = false) final String productSpecificationPeriodversion,
			@ApiParam(value = "For filtering: Name of the product specification") @Valid @RequestParam(value = "productSpecification.name", required = false) final String productSpecificationPeriodname,
			@ApiParam(value = "For filtering: class type of referred Product Specification") @Valid @RequestParam(value = "productSpecification.@referredType", required = false) final String productSpecificationPeriodAtreferredType,
			@ApiParam(value = "For filtering: Name of the channel") @Valid @RequestParam(value = "channel.name", required = false) final String channelPeriodname,
			@ApiParam(value = "For filtering: (Class) type of the referred channel like DistributionChannel, SalesChannel and so on") @Valid @RequestParam(value = "channel.@referredType", required = false) final String channelPeriodAtreferredType,
			@ApiParam(value = "For filtering: Version of the service candidate") @Valid @RequestParam(value = "serviceCandidate.version", required = false) final String serviceCandidatePeriodversion,
			@ApiParam(value = "For filtering: Name of the service candidate") @Valid @RequestParam(value = "serviceCandidate.name", required = false) final String serviceCandidatePeriodname,
			@ApiParam(value = "For filtering: The Class type of  referred Service Candidate") @Valid @RequestParam(value = "serviceCandidate.@referredType", required = false) final String serviceCandidatePeriodAtreferredType,
			@ApiParam(value = "For filtering: Attachment type such as video, picture") @Valid @RequestParam(value = "attachment.type", required = false) final String attachmentPeriodtype,
			@ApiParam(value = "For filtering: Uniform Resource Locator, is a web page address (a subset of URI)") @Valid @RequestParam(value = "attachment.url", required = false) final String attachmentPeriodurl,
			@ApiParam(value = "For filtering: Attachment mime type such as extension file for video, picture and document") @Valid @RequestParam(value = "attachment.mimeType", required = false) final String attachmentPeriodmimeType,
			@ApiParam(value = "For filtering: the class type of the Attachment") @Valid @RequestParam(value = "attachment.@type", required = false) final String attachmentPeriodAttype,
			@ApiParam(value = "For filtering: The immediate base class type of the attachment") @Valid @RequestParam(value = "attachment.@baseType", required = false) final String attachmentPeriodAtbaseType,
			@ApiParam(value = "For filtering: A link to the schema describing this attachment entity") @Valid @RequestParam(value = "attachment.@schemaLocation", required = false) final String attachmentPeriodAtschemaLocation,
			@ApiParam(value = "For filtering: Category version") @Valid @RequestParam(value = "category.version", required = false) final String categoryPeriodversion,
			@ApiParam(value = "For filtering: Name of the category") @Valid @RequestParam(value = "category.name", required = false) final String categoryPeriodname,
			@ApiParam(value = "For filtering: the class type of the referred Category") @Valid @RequestParam(value = "category.@referredType", required = false) final String categoryPeriodAtreferredType,
			@ApiParam(value = "For filtering: Version of the resource candidate") @Valid @RequestParam(value = "resourceCandidate.version", required = false) final String resourceCandidatePeriodversion,
			@ApiParam(value = "For filtering: Name of the resource candidate") @Valid @RequestParam(value = "resourceCandidate.name", required = false) final String resourceCandidatePeriodname,
			@ApiParam(value = "For filtering: The Class type of referred Resource Candidate") @Valid @RequestParam(value = "resourceCandidate.@referredType", required = false) final String resourceCandidatePeriodAtreferredType,
			@ApiParam(value = "For filtering: Name of the productOfferingTerm") @Valid @RequestParam(value = "productOfferingTerm.name", required = false) final String productOfferingTermPeriodname,
			@ApiParam(value = "For filtering: The class type of ProductOfferingTerm") @Valid @RequestParam(value = "productOfferingTerm.@type", required = false) final String productOfferingTermPeriodAttype,
			@ApiParam(value = "For filtering: A link to the schema describing this product offering term") @Valid @RequestParam(value = "productOfferingTerm.@schemaLocation", required = false) final String productOfferingTermPeriodAtschemaLocation,
			@ApiParam(value = "For filtering: Name of the market segment") @Valid @RequestParam(value = "marketSegment.name", required = false) final String marketSegmentPeriodname,
			@ApiParam(value = "For filtering: (Class) type of the referred market segment") @Valid @RequestParam(value = "marketSegment.@referredType", required = false) final String marketSegmentPeriodAtreferredType,
			@ApiParam(value = "For filtering: Name of the productOfferingPrice") @Valid @RequestParam(value = "productOfferingPrice.name", required = false) final String productOfferingPricePeriodname,
			@ApiParam(value = "For filtering: Indicates the price type: recurring, one time, usage") @Valid @RequestParam(value = "productOfferingPrice.priceType", required = false) final String productOfferingPricePeriodpriceType,
			@ApiParam(value = "For filtering: Could be minutes, GB...") @Valid @RequestParam(value = "productOfferingPrice.unitOfMeasure", required = false) final String productOfferingPricePeriodunitOfMeasure,
			@ApiParam(value = "For filtering: Could be month, week...") @Valid @RequestParam(value = "productOfferingPrice.recurringChargePeriod", required = false) final String productOfferingPricePeriodrecurringChargePeriod,
			@ApiParam(value = "For filtering: ProductOffering version") @Valid @RequestParam(value = "productOfferingPrice.version", required = false) final String productOfferingPricePeriodversion,
			@ApiParam(value = "For filtering: The class type of the product offering price") @Valid @RequestParam(value = "productOfferingPrice.@type", required = false) final String productOfferingPricePeriodAttype,
			@ApiParam(value = "For filtering: the immediate base class of product offering price") @Valid @RequestParam(value = "productOfferingPrice.@baseType", required = false) final String productOfferingPricePeriodAtbaseType,
			@ApiParam(value = "For filtering: hyperlink reference to the product offering price schema") @Valid @RequestParam(value = "productOfferingPrice.@schemaLocation", required = false) final String productOfferingPricePeriodAtschemaLocation,
			@ApiParam(value = "For filtering: a flag indicating if this product offering price is bundle (composite) or not") @Valid @RequestParam(value = "productOfferingPrice.isBundle", required = false) final Boolean productOfferingPricePeriodisBundle,
			@ApiParam(value = "For filtering: Name of the agreement") @Valid @RequestParam(value = "agreement.name", required = false) final String agreementPeriodname,
			@ApiParam(value = "For filtering: class type of the referred Agreement") @Valid @RequestParam(value = "agreement.@referredType", required = false) final String agreementPeriodAtreferredType,
			@ApiParam(value = "For filtering: Used to indicate the current lifecycle status") @Valid @RequestParam(value = "bundledProductOffering.lifecycleStatus", required = false) final String bundledProductOfferingPeriodlifecycleStatus,
			@ApiParam(value = "For filtering: Name of the BundledProductOffering") @Valid @RequestParam(value = "bundledProductOffering.name", required = false) final String bundledProductOfferingPeriodname,
			@ApiParam(value = "For filtering: Name of the associated productSpecCharacteristic") @Valid @RequestParam(value = "prodSpecCharValueUse.name", required = false) final String prodSpecCharValueUsePeriodname,
			@ApiParam(value = "For filtering: A kind of value that the characteristic can take on, such as numeric, text and so forth") @Valid @RequestParam(value = "prodSpecCharValueUse.valueType", required = false) final String prodSpecCharValueUsePeriodvalueType,
			@ApiParam(value = "For filtering: The minimum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where zero is the value for the minCardinality.") @Valid @RequestParam(value = "prodSpecCharValueUse.minCardinality", required = false) final Integer prodSpecCharValueUsePeriodminCardinality,
			@ApiParam(value = "For filtering: The maximum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where five is the value for the maxCardinality.") @Valid @RequestParam(value = "prodSpecCharValueUse.maxCardinality", required = false) final Integer prodSpecCharValueUsePeriodmaxCardinality,
			@ApiParam(value = "For filtering: Identifier of the place for which product offering has prices available") @Valid @RequestParam(value = "productOfferingPrice.place.id", required = false) final String productOfferingPricePeriodplacePeriodId,
			@ApiParam(value = "For filtering by facet options. The identifier of the facet search option.") @Valid @RequestParam(value = "facetSearchOption.id", required = false) final List<String> facetSearchOptionId,
			@ApiParam(value = "For sorting: The options the product offerings can be sorted by.") @Valid @RequestParam(value = "sort", required = false) final List<String> sort,
			@ApiParam(value = "For filtering: Identifier of the related party for which the price applies.") @Valid @RequestParam(value = "productOfferingPrice.relatedParty.id", required = false) final String relatedPartyId,
			@ApiParam(value = "Requested index for start of resources to be provided in response requested by client.") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response requested by client.") @Valid @RequestParam(value = "limit", required = false) Integer limit)
	{
		try
		{
			final SolrSearchQueryData searchQuery = new SolrSearchQueryData();
			final List<SolrSearchQueryTermData> filters = new ArrayList<>();
			final List<SolrSearchQueryTermData> solrSearchQueryTermDataList = productSearchFacade
					.decodeFacetSearchQuery(facetSearchOptionId);

			addAllToFilter(filters, solrSearchQueryTermDataList);
			addToFilter(filters, "name", name);
			addToFilter(filters, "categoryName", categoryPeriodname);
			addToFilter(filters, "channel", channelPeriodname);
			addToFilter(filters, "productOfferingGroups", offeringGroupId);
			addToFilter(filters, "isBundled", isBundle);
			addToFilter(filters, "parentBundledPo", parentBundledProductOfferingId);
			addToFilter(filters, "approvalStatus", lifecycleStatus);
			addToFilter(filters, "region", productOfferingPricePeriodplacePeriodId);

			searchQuery.setFilterTerms(filters);


			if (CollectionUtils.isNotEmpty(sort))
			{
				searchQuery.setSort(productSortFacade.decodeSortOption(sort));
			}

			offset = tmaFacetSearchPaginationFacade.checkOffset(offset);
			limit = tmaFacetSearchPaginationFacade.checkLimit(limit);

			final List<FacetSearchPageData<SearchStateData, ProductData>> sourceResults = productSearchFacade
					.getSolrSearchResults(searchQuery, offset, limit);

			if (sourceResults.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.OK);
			}

			final List<ProductData> productDataList = tmaFacetSearchPaginationFacade
					.getItemsFromPagesWithOffsetAndLimit(sourceResults, offset, limit);

			if (productDataList == null)
			{
				return new ResponseEntity<>(HttpStatus.OK);
			}

			final List<ProductOffering> productOfferingList = new ArrayList<>();
			for (final ProductData productData : productDataList)
			{
				final ProductOffering productOffering = getDataMapper()
						.map(productData, IndexedProductOffering.class, fields);
				solrSearchQueryTermDataList.forEach((final SolrSearchQueryTermData solrSearchQueryTermData) -> getDataMapper()
						.map(solrSearchQueryTermData, productOffering, fields));
				productOfferingList.add(productOffering);
			}
			final Long totalCount = sourceResults.get(0).getPagination().getTotalNumberOfResults();
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			tmaFacetSearchPaginationFacade.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
					queryStringWithoutParams, totalCount, limit, offset);
			if (limit < totalCount || offset != 0)
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(productOfferingList), header,
						HttpStatus.PARTIAL_CONTENT);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(productOfferingList), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException | IllegalArgumentException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected SolrSearchQueryTermData addQueryData(final String queryString, final String queryValue)
	{
		final SolrSearchQueryTermData searchQueryTermData = new SolrSearchQueryTermData();
		searchQueryTermData.setKey(queryString);
		searchQueryTermData.setValue(queryValue);
		return searchQueryTermData;
	}

	private void addAllToFilter(final List<SolrSearchQueryTermData> filters,
			final List<SolrSearchQueryTermData> solrSearchQueryTermDataList)
	{
		solrSearchQueryTermDataList.forEach(solrSearchQueryTermData -> filters
				.add(addQueryData(solrSearchQueryTermData.getKey(), solrSearchQueryTermData.getValue())));
	}

	private void addToFilter(final List<SolrSearchQueryTermData> filters, final String attribute, final String value)
	{
		if (StringUtils.isNotEmpty(value))
		{
			filters.add(addQueryData(attribute, value));
		}
	}

	private void addToFilter(final List<SolrSearchQueryTermData> filters, final String attribute, final Boolean value)
	{
		if (value != null)
		{
			filters.add(addQueryData(attribute, Boolean.toString(value)));
		}
	}
}
