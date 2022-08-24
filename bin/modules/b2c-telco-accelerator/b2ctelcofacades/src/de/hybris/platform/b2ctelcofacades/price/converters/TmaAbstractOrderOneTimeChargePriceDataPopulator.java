/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderOneTimeChargePriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderOneTimeChargePriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderOneTimeChargePriceDataPopulator<SOURCE extends TmaAbstractOrderOneTimeChargePriceModel,
		TARGET extends TmaAbstractOrderOneTimeChargePriceData> extends
		TmaAbstractOrderChargePriceDataPopulator<SOURCE, TARGET>
{
	public TmaAbstractOrderOneTimeChargePriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		super(billingTimeConverter, priceDataFactory);
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);
	}
}
