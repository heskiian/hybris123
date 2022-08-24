/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderTierUsageCompositePriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderTierUsageCompositePriceModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import org.springframework.util.ObjectUtils;


/**
 * Populates the usageChargeType field from {@link TmaAbstractOrderTierUsageCompositePriceModel} as source on
 * {@link TmaAbstractOrderTierUsageCompositePriceData} as target.
 *
 * @since 2007
 */
public class TmaAbstractOrderTierUsageChargeTypePopulator<SOURCE extends TmaAbstractOrderTierUsageCompositePriceModel,
		TARGET extends TmaAbstractOrderTierUsageCompositePriceData> implements Populator<SOURCE, TARGET>
{
	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	public TmaAbstractOrderTierUsageChargeTypePopulator(
			Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter)
	{
		this.usageChargeTypeConverter = usageChargeTypeConverter;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		if (ObjectUtils.isEmpty(source.getUsageChargeType()))
		{
			return;
		}
		target.setUsageChargeType(getUsageChargeTypeConverter().convert(source.getUsageChargeType()));
	}

	protected Converter<UsageChargeType, UsageChargeTypeData> getUsageChargeTypeConverter()
	{
		return usageChargeTypeConverter;
	}
}
