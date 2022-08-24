/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaUsageProdOfferPriceChargeData} from {@link TmaUsageProdOfferPriceChargeModel}.
 *
 * @since 2007
 */
public class TmaUsageProdOfferPricePopulator<SOURCE extends TmaUsageProdOfferPriceChargeModel,
		TARGET extends TmaUsageProdOfferPriceChargeData> implements Populator<SOURCE, TARGET>
{
	private Converter<UsageUnitModel, UsageUnitData> usageUnitConverter;
	private Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> productUsageSpecificationConverter;

	public TmaUsageProdOfferPricePopulator(final Converter<UsageUnitModel, UsageUnitData> usageUnitConverter,
			final Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> productUsageSpecificationConverter)
	{
		this.usageUnitConverter = usageUnitConverter;
		this.productUsageSpecificationConverter = productUsageSpecificationConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setTierStart(source.getTierStart());
		target.setTierEnd(source.getTierEnd());
		target.setUsageUnit(getUsageUnitConverter().convert(source.getUsageUnit()));
		if (source.getProductUsageSpec() != null)
		{
			target.setProductUsageSpecification(getProductUsageSpecificationConverter().convert(source.getProductUsageSpec()));
		}
	}

	protected Converter<UsageUnitModel, UsageUnitData> getUsageUnitConverter()
	{
		return usageUnitConverter;
	}

	protected Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> getProductUsageSpecificationConverter()
	{
		return productUsageSpecificationConverter;
	}
}
