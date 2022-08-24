/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderRecurringChargePriceDataPopulator<SOURCE extends TmaAbstractOrderRecurringChargePriceModel,
		TARGET extends TmaAbstractOrderRecurringChargePriceData> extends
		TmaAbstractOrderChargePriceDataPopulator<SOURCE, TARGET>
{
	public TmaAbstractOrderRecurringChargePriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		super(billingTimeConverter, priceDataFactory);
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
	}
}
