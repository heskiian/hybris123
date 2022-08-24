/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.VolumeUsageChargeData;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link SubscriptionPricePlanData#usageCharges} based on {@link SubscriptionPricePlanModel =#usageCharges}}
 *
 * @since 2007
 */
public class TmaSppUsageChargePopulator<SOURCE extends PriceRowModel, TARGET extends SubscriptionPricePlanData>
		implements Populator<SOURCE, TARGET>
{
	private Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> perUnitUsageChargeConverter;
	private Converter<VolumeUsageChargeModel, VolumeUsageChargeData> volumeUsageChargeConverter;

	public TmaSppUsageChargePopulator(
			Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> perUnitUsageChargeConverter,
			Converter<VolumeUsageChargeModel, VolumeUsageChargeData> volumeUsageChargeConverter)
	{
		this.perUnitUsageChargeConverter = perUnitUsageChargeConverter;
		this.volumeUsageChargeConverter = volumeUsageChargeConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (!(source instanceof SubscriptionPricePlanModel))
		{
			return;
		}

		final SubscriptionPricePlanModel spp = (SubscriptionPricePlanModel) source;

		if (CollectionUtils.isEmpty(spp.getUsageCharges()))
		{
			target.setUsageCharges(Collections.emptyList());
			return;
		}

		final List<UsageChargeData> usageCharges = new ArrayList<>();

		spp.getUsageCharges().forEach(usageCharge -> {
			if (usageCharge instanceof PerUnitUsageChargeModel)
			{
				usageCharges.add(getPerUnitUsageChargeConverter().convert((PerUnitUsageChargeModel) usageCharge));
			}
			else
			{
				usageCharges.add(getVolumeUsageChargeConverter().convert((VolumeUsageChargeModel) usageCharge));
			}
		});

		target.setUsageCharges(usageCharges);

	}

	protected Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> getPerUnitUsageChargeConverter()
	{
		return perUnitUsageChargeConverter;
	}

	protected Converter<VolumeUsageChargeModel, VolumeUsageChargeData> getVolumeUsageChargeConverter()
	{
		return volumeUsageChargeConverter;
	}
}
