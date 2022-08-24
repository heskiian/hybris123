/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;

import java.math.BigDecimal;

import org.springframework.util.ObjectUtils;


/**
 * Populates {@link UsageChargeEntryModel} to {@link I_UtilsProdChgUsageType}
 *
 * @since 1911
 */

public class UpilUsageChargeEntryPopulator implements Populator<UsageChargeEntryModel, I_UtilsProdChgUsageType>
{
	public static final String UTILS_PRICE_TIER_TYPE_SCALE = "2";
	public static final String UTILS_PRICE_TIER_TYPE_BLOCK = "1";
	public static final String UTILS_PRICE_TIER_TYPE_STANDARD = "0";

	@Override
	public void populate(final UsageChargeEntryModel source, final I_UtilsProdChgUsageType target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		final UsageChargeModel usageCharge = source.getUsageCharge();
		target.setUtilitiesProduct(usageCharge.getSubscriptionPricePlanUsage().getCode());
		target.setUtilsPriceID(source.getId());
		target.setUtilsPriceName(source.getId());
		if (!ObjectUtils.isEmpty(source.getUsageCharge()) && (!ObjectUtils.isEmpty(source.getUsageCharge().getUsageUnit())))
		{
			target.setUtilsMeasurementUnit((source.getUsageCharge().getUsageUnit().getId()));
		}
		if (source instanceof TierUsageChargeEntryModel)
		{
			target.setUtilsPriceFromBlock(((TierUsageChargeEntryModel) source).getTierStart().toString());
			target.setUtilsPriceToBlock(((TierUsageChargeEntryModel) source).getTierEnd().toString());
		}
		if (source instanceof OverageUsageChargeEntryModel)
		{
			getOverageUsageChargeEntryData((OverageUsageChargeEntryModel) source, target);
		}
		target.setUtilsPriceAmount(BigDecimal.valueOf(source.getPrice()));
		if (!ObjectUtils.isEmpty(source.getCurrency()))
		{
			target.setUtilsPriceCurrency(source.getCurrency().getIsocode());
		}

		if (!ObjectUtils.isEmpty(source.getSemantics()))
		{
			target.setUtilsSemanticsName1(source.getSemantics().getSemanticsName1());
			target.setUtilsSemanticsName2(source.getSemantics().getSemanticsName2());
		}
		getUtilsPriceTierType(usageCharge, target);
	}

	private void getUtilsPriceTierType(final UsageChargeModel usageCharge,
			final I_UtilsProdChgUsageType target)
	{
		if (usageCharge instanceof PerUnitUsageChargeModel)
		{
			final PerUnitUsageChargeModel perUsageChargeModel = (PerUnitUsageChargeModel) usageCharge;
			if (!ObjectUtils.isEmpty(perUsageChargeModel.getUsageChargeType()))
			{
				if (UsageChargeType.HIGHEST_APPLICABLE_TIER.getCode()
						.equalsIgnoreCase(perUsageChargeModel.getUsageChargeType().getCode()))
				{
					target.setUtilsPriceTierType(UTILS_PRICE_TIER_TYPE_SCALE);
				}
				else
				{
					target.setUtilsPriceTierType(UTILS_PRICE_TIER_TYPE_BLOCK);
				}
			}
		}
		else if (usageCharge instanceof VolumeUsageChargeModel)
		{
			target.setUtilsPriceTierType(UTILS_PRICE_TIER_TYPE_STANDARD);
		}

	}

	private void getOverageUsageChargeEntryData(final OverageUsageChargeEntryModel overageUsageChargeEntry,
			final I_UtilsProdChgUsageType target)
	{
		if (!ObjectUtils.isEmpty(overageUsageChargeEntry.getTierStart()))
		{
			target.setUtilsPriceFromBlock(overageUsageChargeEntry.getTierStart().toString());
		}
		if (!ObjectUtils.isEmpty(overageUsageChargeEntry.getTierEnd()))
		{
			target.setUtilsPriceToBlock(overageUsageChargeEntry.getTierEnd().toString());
		}
	}
}
