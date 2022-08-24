/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderTierUsageCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaTierUsageChargeCompositePopModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;
import java.util.Map;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the tier usage composite product offering price
 *
 * @since 2007
 */
public class TmaAbstractOrderTierUsageChargeCompositePriceBuilder
		extends TmaAbstractOrderCompositePriceBuilder<TmaTierUsageChargeCompositePopModel>
{
	public TmaAbstractOrderTierUsageChargeCompositePriceBuilder(ModelService modelService,
			TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy,
			Map<String, TmaAbstractOrderEntryPopPriceBuilder<TmaProductOfferingPriceModel>> productOfferingPriceBuilderMap)
	{
		super(modelService, tmaRetrieveTaxRateStrategy, productOfferingPriceBuilderMap);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaTierUsageChargeCompositePopModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderTierUsageCompositePriceModel price = getModelService()
				.create(TmaAbstractOrderTierUsageCompositePriceModel.class);
		price.setUsageChargeType(productOfferingPrice.getUsageChargeType());
		buildChildPrices(productOfferingPrice, entry, taxes, price);
		getModelService().save(price);

		return price;
	}
}
