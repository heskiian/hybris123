/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderAlterationPriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderAlterationPriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData} as target type.
 *
 * @since 2007
 */
public class TmaAbstractOrderAlterationPriceDataPopulator<S extends TmaAbstractOrderAlterationPriceModel, T extends TmaAbstractOrderAlterationPriceData>
		extends TmaAbstractOrderComponentPriceDataPopulator<S, T>
{
	public TmaAbstractOrderAlterationPriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		super(billingTimeConverter, priceDataFactory);
	}

	@Override
	public void populate(final S source, final T target)
	{
		super.populate(source, target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
		target.setPercentage(source.getPercentage());
	}
}
