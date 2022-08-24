/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;


/**
 * Populator for setting the children of a Bundled Product Offerings as {@link ProductData} on the item indexed by solr.
 *
 * @since 2105
 */
public class TmaSearchResultBpoChildrenPopulator extends TmaSearchResultPopulator
{
	private static final Logger LOG = Logger.getLogger(TmaSearchResultBpoChildrenPopulator.class);
	private static final String CHILDREN_INDEXED_ATTRIBUTE = "children";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		final List<String> childrenProductOfferings = getValue(source, CHILDREN_INDEXED_ATTRIBUTE);
		if (CollectionUtils.isEmpty(childrenProductOfferings))
		{
			return;
		}

		final List<ProductData> childrenProducts = new ArrayList<>();
		final ObjectMapper mapper = getObjectMapper();

		childrenProductOfferings.forEach(
				child -> {
					try
					{
						childrenProducts.add(mapper.readValue(child, ProductData.class));
					}
					catch (JsonProcessingException ex)
					{
						LOG.warn("Populating Children of BPO failed: Could not transform information from SOLR "
								+ "into Data Object for Product Offering with code " + target.getCode(), ex);
					}
				}
		);
		target.setChildren(childrenProducts);
	}

	protected ObjectMapper getObjectMapper()
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
}
