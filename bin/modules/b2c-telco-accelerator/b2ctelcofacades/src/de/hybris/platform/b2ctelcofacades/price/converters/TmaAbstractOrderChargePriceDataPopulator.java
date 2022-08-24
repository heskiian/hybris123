/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderChargePriceDataPopulator<S extends TmaAbstractOrderChargePriceModel,
		T extends TmaAbstractOrderChargePriceData> extends TmaAbstractOrderComponentPriceDataPopulator<S, T>
{
	public TmaAbstractOrderChargePriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		super(billingTimeConverter, priceDataFactory);
	}

	@Override
	public void populate(final S source, final T target)
	{
		super.populate(source, target);
	}
}
