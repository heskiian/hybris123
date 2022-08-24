/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;


/**
 * Populator for setting the price values from the solr Search Result to target {@link ProductData}.
 *
 * @since 2007
 */
public class TmaSearchResultPricesPopulator implements Populator<SearchResultValueData, PriceData>
{
	private static final Logger LOG = Logger.getLogger(TmaSearchResultPricesPopulator.class);

	private static final String DEFAULT_SEPARATOR = "_";
	private static final String PRODUCT_OFFERING_PRICE_INDEXED_ATTRIBUTE = "productOfferingPrice" + DEFAULT_SEPARATOR + "string";
	private static final String TYPE = "type";
	private static final String POP_CHILDREN_ATTRIBUTE = "children";

	private static final String REGIONS_INDEXED_ATTRIBUTE = "region";
	private static final String CURRENCY_INDEXED_ATTRIBUTE = "currency";
	private static final String CHANNELS_INDEXED_ATTRIBUTE = "channel";
	private static final String SUBSCRIPTION_TERMS_INDEXED_ATTRIBUTE = "terms";
	private static final String PROCESS_TYPES_INDEXED_ATTRIBUTE = "process";
	private static final String PRIORITY_INDEXED_ATTRIBUTE = "pricePriority";
	private static final String PRICING_LOGIC_ALGORITHM = "pla";

	private Map<String, Class<TmaProductOfferingPriceData>> priceDataTypeMap;
	private Map<String, Class<TmaPricingLogicAlgorithmData>> pricingLogicAlgorithmDataTypeMap;

	public TmaSearchResultPricesPopulator(final Map<String, Class<TmaProductOfferingPriceData>> priceDataTypeMap,
			final Map<String, Class<TmaPricingLogicAlgorithmData>> pricingLogicAlgorithmDataTypeMap)
	{
		this.priceDataTypeMap = priceDataTypeMap;
		this.pricingLogicAlgorithmDataTypeMap = pricingLogicAlgorithmDataTypeMap;
	}

	@Override
	public void populate(final SearchResultValueData source, final PriceData target)
	{
		try
		{
			target.setPriceType(PriceDataType.BUY);
			target.setValue(BigDecimal.valueOf(0));
			target.setCurrencyIso(getCurrency(source));
			target.setPriority(getValue(source, PRIORITY_INDEXED_ATTRIBUTE));
			setPriceContext(source, target);
			setProductOfferingPrice(source, target);
		}
		catch (final IOException exception)
		{
			LOG.warn("Populating Product Offering Prices failed: Could not transform information from SOLR "
					+ "into Data Object for Product Offering with code " + target.getCode(), exception);
		}

	}

	protected void setProductOfferingPrice(final SearchResultValueData source, final PriceData priceData) throws IOException
	{
		final TmaProductOfferingPriceData productOfferingPriceData = getProductOfferingPrice(source);
		if (productOfferingPriceData == null)
		{
			return;
		}

		priceData.setProductOfferingPrice(productOfferingPriceData);
	}

	protected void setPriceContext(final SearchResultValueData source, final PriceData priceData)
	{
		priceData.setRegions(getRegions(source));
		priceData.setDistributionChannels(getDistributionChannels(source));
		priceData.setSubscriptionTerms(getSubscriptionTerms(source));
		priceData.setProcessTypes(getProcessTypes(source));
	}

	private Set<TmaProcessType> getProcessTypes(final SearchResultValueData source)
	{
		final List<String> processTypesCodes = getSearchResultCodes(source, PROCESS_TYPES_INDEXED_ATTRIBUTE);
		final Set<TmaProcessType> processTypes = new HashSet<>(processTypesCodes.size());
		processTypesCodes.forEach(processTypesCode -> processTypes.add(TmaProcessType.valueOf(processTypesCode)));

		return processTypes;
	}

	private List<SubscriptionTermData> getSubscriptionTerms(final SearchResultValueData source)
	{
		final List<String> subscriptionTermCodes = getSearchResultCodes(source, SUBSCRIPTION_TERMS_INDEXED_ATTRIBUTE);

		final List<SubscriptionTermData> subscriptionTermList = new ArrayList<>(subscriptionTermCodes.size());
		subscriptionTermCodes.forEach(subscriptionTermId -> {
			final SubscriptionTermData subscriptionTermData = new SubscriptionTermData();
			subscriptionTermData.setId(subscriptionTermId);
			subscriptionTermList.add(subscriptionTermData);
		});

		return subscriptionTermList;
	}

	private List<String> getSearchResultCodes(final SearchResultValueData source, final String indexedPropertyName)
	{
		final List<String> searchResultCodes = getValue(source, indexedPropertyName);
		if (CollectionUtils.isEmpty(searchResultCodes))
		{
			return new ArrayList<>();
		}
		return searchResultCodes;
	}

	private Set<PriceRowChannel> getDistributionChannels(final SearchResultValueData source)
	{
		final List<String> channelCodes = getSearchResultCodes(source, CHANNELS_INDEXED_ATTRIBUTE);
		final Set<PriceRowChannel> channelList = new HashSet<>(channelCodes.size());
		channelCodes.forEach(channelCode -> channelList.add(PriceRowChannel.valueOf(channelCode)));

		return channelList;
	}

	private List<RegionData> getRegions(final SearchResultValueData source)
	{
		final List<String> regions = getSearchResultCodes(source, REGIONS_INDEXED_ATTRIBUTE);
		final List<RegionData> regionDataList = new ArrayList<>(regions.size());

		regions.forEach(region -> {
			final RegionData regionData = new RegionData();
			regionData.setIsocode(region);
			regionDataList.add(regionData);
		});

		return regionDataList;
	}

	private String getCurrency(final SearchResultValueData source)
	{
		return getValue(source, CURRENCY_INDEXED_ATTRIBUTE);
	}

	/**
	 * Retrieves the Product Offering Price information as stored in solr, in json format, and creates Data Objects for it.
	 *
	 * @param source
	 * 		search result as available in solr, to retrieve the information from
	 * @return Product Offering Price Data object created from the input source
	 * @throws IOException
	 * 		if the json received from solr cannot be transformed into a {@link TmaProductOfferingPriceData}
	 */
	private TmaProductOfferingPriceData getProductOfferingPrice(final SearchResultValueData source) throws IOException
	{
		final ObjectMapper mapper = getObjectMapper();

		final String indexedPop = getValue(source, PRODUCT_OFFERING_PRICE_INDEXED_ATTRIBUTE);
		if (StringUtils.isEmpty(indexedPop))
		{
			return null;
		}

		final JsonNode jsonPop = mapper.readTree(indexedPop);
		final TmaProductOfferingPriceData rootPopPriceData = mapper.readValue(indexedPop, getPopDataClassFromJson(jsonPop));

		if (hasPricingLogicAlgorithm(jsonPop))
		{
			populatePricingLogicAlgorithm(jsonPop, (TmaComponentProdOfferPriceData) rootPopPriceData, mapper);
		}

		if (hasChildrenPops(jsonPop))
		{
			populateChildrenPopsOnCompositePopData((TmaCompositeProdOfferPriceData) rootPopPriceData, jsonPop, mapper);
		}

		return rootPopPriceData;
	}

	private void populateChildrenPopsOnCompositePopData(final TmaCompositeProdOfferPriceData rootPopPriceData,
			final JsonNode jsonPop, final ObjectMapper mapper) throws IOException
	{
		final JsonNode childrenPopsJson = jsonPop.get(POP_CHILDREN_ATTRIBUTE);
		final Iterator<JsonNode> childrenPopIterator = childrenPopsJson.iterator();
		final Set<TmaProductOfferingPriceData> childrenPops = new HashSet<>();

		while (childrenPopIterator.hasNext())
		{
			final JsonNode childPopJson = childrenPopIterator.next();

			final Class<? extends TmaProductOfferingPriceData> popDataClass = getPopDataClassFromJson(childPopJson);
			final TmaProductOfferingPriceData childPopPriceData = mapper.readValue(childPopJson.toString(), popDataClass);

			if (hasPricingLogicAlgorithm(childPopJson))
			{
				populatePricingLogicAlgorithm(childPopJson, (TmaComponentProdOfferPriceData) childPopPriceData, mapper);
			}

			if (hasChildrenPops(childPopJson))
			{
				populateChildrenPopsOnCompositePopData((TmaCompositeProdOfferPriceData) childPopPriceData, childPopJson, mapper);
			}
			childrenPops.add(childPopPriceData);
		}

		rootPopPriceData.setChildren(childrenPops);
	}

	private boolean hasChildrenPops(final JsonNode jsonPop)
	{
		return jsonPop.get(POP_CHILDREN_ATTRIBUTE) != null;
	}

	private boolean hasPricingLogicAlgorithm(final JsonNode jsonPop)
	{
		return jsonPop.get(PRICING_LOGIC_ALGORITHM) != null && !jsonPop.get(PRICING_LOGIC_ALGORITHM).isNull();
	}

	private Class<? extends TmaProductOfferingPriceData> getPopDataClassFromJson(final JsonNode jsonPop)
	{
		final String popType = jsonPop.get(TYPE).asText();
		return getPriceDataTypeMap().get(popType);
	}

	private void populatePricingLogicAlgorithm(final JsonNode jsonPop, final TmaComponentProdOfferPriceData childPopPriceData,
			final ObjectMapper mapper) throws JsonProcessingException
	{
		final JsonNode plaJsonNode = jsonPop.get(PRICING_LOGIC_ALGORITHM);
		final Class<? extends TmaPricingLogicAlgorithmData> plaDataClass = getPlaDataClassFromJson(plaJsonNode);
		final TmaPricingLogicAlgorithmData pricingLogicAlgorithmData = mapper.readValue(plaJsonNode.toString(), plaDataClass);
		childPopPriceData.setPla(pricingLogicAlgorithmData);
	}

	private Class<? extends TmaPricingLogicAlgorithmData> getPlaDataClassFromJson(final JsonNode jsonPop)
	{
		final String plaType = jsonPop.get(TYPE).asText();
		return getPricingLogicAlgorithmDataTypeMap().get(plaType);
	}

	protected ObjectMapper getObjectMapper()
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		return source.getValues() == null ? null : (T) source.getValues().get(propertyName);
	}

	private Map<String, Class<TmaProductOfferingPriceData>> getPriceDataTypeMap()
	{
		return priceDataTypeMap;
	}

	private Map<String, Class<TmaPricingLogicAlgorithmData>> getPricingLogicAlgorithmDataTypeMap()
	{
		return pricingLogicAlgorithmDataTypeMap;
	}
}
