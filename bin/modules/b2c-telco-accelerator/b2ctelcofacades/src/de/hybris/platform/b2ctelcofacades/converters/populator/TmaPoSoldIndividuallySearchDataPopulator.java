/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Value provider for {@link de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel#SOLDINDIVIDUALLY}
 *
 * @since 1903
 */
public class TmaPoSoldIndividuallySearchDataPopulator implements Populator<SearchResultValueData, ProductData>
{
	private static final String SOLD_INDIVIDUALLY = "soldIndividually";

	@Override
	public void populate(final SearchResultValueData searchResultValueData, final ProductData productData)
			throws ConversionException
	{
		final Boolean isSoldIndividually = (Boolean) searchResultValueData.getValues().get(SOLD_INDIVIDUALLY);
		productData.setSoldIndividually(isSoldIndividually);
	}
}
