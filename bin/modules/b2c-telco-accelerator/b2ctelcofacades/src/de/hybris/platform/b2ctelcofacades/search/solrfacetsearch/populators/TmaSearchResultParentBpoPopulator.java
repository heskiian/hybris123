/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;


/**
 * Populator for setting the parent Bundled Product Offerings as {@link ProductData#getCode()} on the item indexed by solr.
 *
 * @since 2007
 */
public class TmaSearchResultParentBpoPopulator extends TmaSearchResultPopulator
{
	private static final String PARENT_BUNDLED_PRODUCT_OFFERING_INDEXED_PROPERTY = "parentBundledPo";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		final List<String> parentBpoCodes = getValue(source, PARENT_BUNDLED_PRODUCT_OFFERING_INDEXED_PROPERTY);
		if (CollectionUtils.isEmpty(parentBpoCodes))
		{
			return;
		}

		final List<ProductData> parentBpos = new ArrayList<>();
		for (final String parentBpoCode : parentBpoCodes)
		{
			final ProductData productData = new ProductData();
			productData.setCode(parentBpoCode);
			parentBpos.add(productData);
		}
		target.setParents(parentBpos);
	}

}
