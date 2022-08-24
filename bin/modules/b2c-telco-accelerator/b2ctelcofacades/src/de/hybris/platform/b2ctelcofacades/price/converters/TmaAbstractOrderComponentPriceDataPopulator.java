/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderComponentPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderComponentPriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.math.BigDecimal;


/**
 * Populator implementation for {@link TmaAbstractOrderComponentPriceModel} as source and {@link TmaAbstractOrderComponentPriceData} as target type.
 *
 * @since 2007
 */
public class TmaAbstractOrderComponentPriceDataPopulator<S extends TmaAbstractOrderComponentPriceModel,
		T extends TmaAbstractOrderComponentPriceData> extends TmaAbstractOrderPriceDataPopulator<S, T>
{
	private PriceDataFactory priceDataFactory;
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;

	public TmaAbstractOrderComponentPriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		this.billingTimeConverter = billingTimeConverter;
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	public void populate(final S source, final T target)
	{
		super.populate(source, target);

		target.setTypeOfPrice(source.getPriceType());
		
		if (source.getBillingTime() != null)
		{
			target.setBillingTime(getBillingTimeConverter().convert(source.getBillingTime()));
		}

		if (source.getDutyFreeAmount() != null)
		{
			target.setDutyFreeAmount(createPriceDataForValue(source.getDutyFreeAmount(), source.getCurrency()));
		}

		if (source.getTaxIncludedAmount() != null)
		{
			target.setTaxIncludedAmount(createPriceDataForValue(source.getTaxIncludedAmount(), source.getCurrency()));
		}

		if (source.getTaxRate() != null)
		{
			target.setTaxRate(createPriceDataForValue(source.getTaxRate(), source.getCurrency()));
		}
	}

	protected PriceData createPriceDataForValue(final Double value, CurrencyModel currency)
	{
		return getPriceDataFactory()
				.create(PriceDataType.BUY, BigDecimal.valueOf(value), currency);
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	protected Converter<BillingTimeModel, BillingTimeData> getBillingTimeConverter()
	{
		return billingTimeConverter;
	}
}
