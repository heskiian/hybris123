/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the recurring charge component
 *
 * @since 2007
 */
public class TmaAbstractOrderRecurringChargeComponentPriceBuilder
		extends DefaultTmaAbstractOrderEntryPopBuilder<TmaRecurringProdOfferPriceChargeModel>
{
	public TmaAbstractOrderRecurringChargeComponentPriceBuilder(ModelService modelService,
			TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaRecurringProdOfferPriceChargeModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePrice = getModelService()
				.create(TmaAbstractOrderRecurringChargePriceModel.class);
		recurringChargePrice.setBillingTime(productOfferingPrice.getPriceEvent());
		recurringChargePrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		populatePriceValues(productOfferingPrice.getValue(), entry, taxes, recurringChargePrice);
		recurringChargePrice.setCycleStart(productOfferingPrice.getCycleStart());
		recurringChargePrice.setCycleEnd(productOfferingPrice.getCycleEnd());
		getModelService().save(recurringChargePrice);
		return recurringChargePrice;
	}
}
