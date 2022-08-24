/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.List;


/**
 * Value provider for {@link TmaProductSpecCharacteristicValueModel#getDescription()}
 *
 * @since 6.7
 */
public class TmaProductSpecValuesSearchDataPopulator implements Populator<SearchResultValueData, ProductData>
{

	@Override
	public void populate(final SearchResultValueData source, final ProductData productData)
	{
		productData.setProductSpecDescription(this.<List<String>> getValue(source, "pscvDescription"));
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		return source.getValues() == null ? null : (T) source.getValues().get(propertyName);
	}
}
