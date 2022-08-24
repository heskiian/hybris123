/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link ProductData} with hasParentBpos property . True means that target product is contained in other BPOs
 *
 * @since 6.7
 */
public class TmaProductOfferingParentBposFlagPopulator implements Populator<SearchResultValueData, ProductData>
{
	@Override
	public void populate(final SearchResultValueData source, final ProductData productData)
	{
		final Boolean poHasParentsBpos = Boolean.valueOf((String) source.getValues().get("hasParentBpos"));
		productData.setHasParentBpos(poHasParentsBpos);
	}
}
