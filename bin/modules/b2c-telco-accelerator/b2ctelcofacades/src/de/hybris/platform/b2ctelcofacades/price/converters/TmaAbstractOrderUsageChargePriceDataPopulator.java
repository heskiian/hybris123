/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderUsageChargePriceDataPopulator<SOURCE extends TmaAbstractOrderUsageChargePriceModel,
		TARGET extends TmaAbstractOrderUsageChargePriceData> extends
		TmaAbstractOrderChargePriceDataPopulator<SOURCE, TARGET>
{
	private Converter<UsageUnitModel, UsageUnitData> usageUnitConverter;
	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	public TmaAbstractOrderUsageChargePriceDataPopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final PriceDataFactory priceDataFactory)
	{
		super(billingTimeConverter, priceDataFactory);
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);

		target.setTierStart(source.getTierStart());
		target.setTierEnd(source.getTierEnd());
		target.setUsageUnit(getUsageUnitConverter().convert(source.getUsageUnit()));
		if (source.getUsageChargeType() != null)
		{
			target.setUsageChargeType(getUsageChargeTypeConverter().convert(source.getUsageChargeType()));
		}
	}

	protected Converter<UsageChargeType, UsageChargeTypeData> getUsageChargeTypeConverter()
	{
		return usageChargeTypeConverter;
	}

	@Required
	public void setUsageChargeTypeConverter(
			Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter)
	{
		this.usageChargeTypeConverter = usageChargeTypeConverter;
	}

	protected Converter<UsageUnitModel, UsageUnitData> getUsageUnitConverter()
	{
		return usageUnitConverter;
	}

	@Required
	public void setUsageUnitConverter(final Converter<UsageUnitModel, UsageUnitData> usageUnitConverter)
	{
		this.usageUnitConverter = usageUnitConverter;
	}
}
