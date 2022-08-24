/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Populator for setting the pscvu values from the solr Search Result to target {@link ProductData}.
 *
 * @since 2105
 */
public class TmaSearchResultPscvuPopulator
		implements Populator<SearchResultValueData, List<TmaProductSpecCharacteristicValueUseData>>
{
	private static final Logger LOG = Logger.getLogger(TmaSearchResultPscvuPopulator.class);

	private static final String DEFAULT_SEPARATOR = "_";
	private static final String PSCVU_INDEXED_ATTRIBUTE = "pscvu" + DEFAULT_SEPARATOR + "string";
	private static final String PRODUCT_SPEC_INDEXED_ATTRIBUTE = "productSpecification";

	@Override
	public void populate(final SearchResultValueData source, final List<TmaProductSpecCharacteristicValueUseData> target)
	{
		try
		{
			target.addAll(getProductSpecCharacteristicValueUses(source));
		}
		catch (final IOException exception)
		{
			LOG.warn("Populating Product Specification Characteristics Value Uses failed: Could not transform information from SOLR "
					+ "into Data Object for Product Specification Characteristics Value Uses with code " + target, exception);
		}

	}

	/**
	 * Retrieves the Product Specification Characteristics Value Uses information as stored in solr, in json format, and
	 * creates Data Objects for it.
	 *
	 * @param source
	 *           search result as available in solr, to retrieve the information from
	 * @return Product Specification Characteristics Value Uses Data object created from the input source
	 * @throws IOException
	 *            if the json received from solr cannot be transformed into a
	 *            List{@link TmaProductSpecCharacteristicValueUseData}
	 */
	private List<TmaProductSpecCharacteristicValueUseData> getProductSpecCharacteristicValueUses(
			final SearchResultValueData source) throws IOException
	{
		final ObjectMapper mapper = getObjectMapper();

		final String indexedPscvuList = getValue(source, PSCVU_INDEXED_ATTRIBUTE);
		if (StringUtils.isEmpty(indexedPscvuList))
		{
			return Collections.emptyList();
		}
		final List<TmaProductSpecCharacteristicValueUseData> pscvuList = mapper.readValue(indexedPscvuList,
				new TypeReference<List<TmaProductSpecCharacteristicValueUseData>>()
				{
				});
		final String productSpecificationCode = getValue(source, PRODUCT_SPEC_INDEXED_ATTRIBUTE);
		if (StringUtils.isEmpty(productSpecificationCode))
		{
			return pscvuList;
		}

		final TmaProductSpecificationData productSpecification = new TmaProductSpecificationData();
		productSpecification.setId(productSpecificationCode);
		for (final TmaProductSpecCharacteristicValueUseData pscvu : pscvuList)
		{
			pscvu.setProductSpecification(productSpecification);
		}
		return pscvuList;
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

}
