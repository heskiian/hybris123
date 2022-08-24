/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the usage charge component
 *
 * @since 2007
 */
public class TmaAbstractOrderUsageChargeComponentPriceBuilder
		extends DefaultTmaAbstractOrderEntryPopBuilder<TmaUsageProdOfferPriceChargeModel>
{
	public TmaAbstractOrderUsageChargeComponentPriceBuilder(ModelService modelService,
			TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaUsageProdOfferPriceChargeModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderUsageChargePriceModel usageChargeEntryPrice = getModelService()
				.create(TmaAbstractOrderUsageChargePriceModel.class);
		usageChargeEntryPrice.setBillingTime(productOfferingPrice.getPriceEvent());
		usageChargeEntryPrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		populatePriceValues(productOfferingPrice.getValue(), entry, taxes, usageChargeEntryPrice);
		usageChargeEntryPrice.setUsageUnit(productOfferingPrice.getUsageUnit());
		usageChargeEntryPrice.setTierStart(productOfferingPrice.getTierStart());
		usageChargeEntryPrice.setTierEnd(productOfferingPrice.getTierEnd());
		return usageChargeEntryPrice;
	}
}
