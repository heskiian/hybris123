/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPopBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the one time charge component
 *
 * @since 2007
 */
public class TmaAbstractOrderOneTimeChargeComponentPriceBuilder
		extends DefaultTmaAbstractOrderEntryPopBuilder<TmaOneTimeProdOfferPriceChargeModel>
{
	public TmaAbstractOrderOneTimeChargeComponentPriceBuilder(ModelService modelService,
			TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaOneTimeProdOfferPriceChargeModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePrice = getModelService()
				.create(TmaAbstractOrderOneTimeChargePriceModel.class);
		oneTimeChargePrice.setBillingTime(productOfferingPrice.getPriceEvent());
		oneTimeChargePrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		populatePriceValues(productOfferingPrice.getValue(), entry, taxes,
				oneTimeChargePrice);
		populateCatalogCode(productOfferingPrice, oneTimeChargePrice);
		populateName(productOfferingPrice, oneTimeChargePrice);
		getModelService().save(oneTimeChargePrice);
		return oneTimeChargePrice;
	}

}
