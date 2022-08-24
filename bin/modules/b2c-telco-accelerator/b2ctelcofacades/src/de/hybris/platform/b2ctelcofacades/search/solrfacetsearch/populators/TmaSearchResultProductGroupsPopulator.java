/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;


/**
 * Populator for setting the Product Groups as id on the {@link TmaOfferingGroupData#getId()} on the item indexed by solr
 *
 * @since 2007
 */
public class TmaSearchResultProductGroupsPopulator extends TmaSearchResultPopulator
{
	private static final String PRODUCT_OFFERING_GROUP_INDEXED_PROPERTY = "productOfferingGroups";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		final List<TmaOfferingGroupData> productOfferingGroups = new ArrayList<>();
		final List<String> sourcePoGroupCodes = getValue(source, PRODUCT_OFFERING_GROUP_INDEXED_PROPERTY);
		if (CollectionUtils.isEmpty(sourcePoGroupCodes))
		{
			return;
		}
		for (final String poGroupCode : sourcePoGroupCodes)
		{
			final TmaOfferingGroupData poGroupData = new TmaOfferingGroupData();
			poGroupData.setId(poGroupCode);
			productOfferingGroups.add(poGroupData);
		}
		target.setOfferingGroups(productOfferingGroups);
	}

}
