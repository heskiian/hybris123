/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import org.apache.commons.lang.StringUtils;


/**
 * Populator for setting the Product Specification on the item indexed by solr.
 *
 * @since 2007
 */
public class TmaSearchResultProductSpecificationPopulator extends TmaSearchResultPopulator
{
	private static final String PRODUCT_SPECIFICATION_INDEXED_PROPERTY = "productSpecification";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		final String productSpecificationCode = getValue(source, PRODUCT_SPECIFICATION_INDEXED_PROPERTY);
		if (StringUtils.isEmpty(productSpecificationCode))
		{
			return;
		}

		final TmaProductSpecificationData productSpecification = new TmaProductSpecificationData();
		productSpecification.setId(productSpecificationCode);
		target.setProductSpecification(productSpecification);
	}

}
