/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaTierUsageChargeCompositePopModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaTierUsageCompositeProdOfferPriceData} from {@link TmaTierUsageChargeCompositePopModel}.
 *
 * @since 2007
 */
public class TmaTierUsageCompositeProdOfferPopulator<SOURCE extends TmaTierUsageChargeCompositePopModel,
		TARGET extends TmaTierUsageCompositeProdOfferPriceData> implements Populator<SOURCE, TARGET>
{
	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	public TmaTierUsageCompositeProdOfferPopulator(Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter)
	{
		this.usageChargeTypeConverter = usageChargeTypeConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (source.getUsageChargeType() != null)
		{
			target.setUsageChargeType(getUsageChargeTypeConverter().convert(source.getUsageChargeType()));
		}
	}

	protected Converter<UsageChargeType, UsageChargeTypeData> getUsageChargeTypeConverter()
	{
		return usageChargeTypeConverter;
	}
}
