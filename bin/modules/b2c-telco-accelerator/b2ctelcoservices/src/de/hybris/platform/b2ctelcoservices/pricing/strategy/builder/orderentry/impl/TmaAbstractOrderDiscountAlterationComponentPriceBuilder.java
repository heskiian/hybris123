/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderDiscountAlterationPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaDiscountProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPopBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the discount price alteration component
 *
 * @since 2007
 */
public class TmaAbstractOrderDiscountAlterationComponentPriceBuilder
		extends DefaultTmaAbstractOrderEntryPopBuilder<TmaDiscountProdOfferPriceAlterationModel>
{
	public TmaAbstractOrderDiscountAlterationComponentPriceBuilder(final ModelService modelService,
			final TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaDiscountProdOfferPriceAlterationModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderDiscountAlterationPriceModel alterationPriceModel = getModelService()
				.create(TmaAbstractOrderDiscountAlterationPriceModel.class);

		alterationPriceModel.setBillingTime(productOfferingPrice.getPriceEvent());
		alterationPriceModel.setPriceType(TmaAbstractOrderPriceType.DISCOUNT_PRICE_ALTERATION);
		if (productOfferingPrice.getIsPercentage())
		{
			alterationPriceModel.setPercentage(productOfferingPrice.getValue());
		}
		else
		{
			populatePriceValues(productOfferingPrice.getValue(), entry, taxes, alterationPriceModel);
		}
		alterationPriceModel.setCycleStart(productOfferingPrice.getCycleStart());
		alterationPriceModel.setCycleEnd(productOfferingPrice.getCycleEnd());
		populateCatalogCode(productOfferingPrice, alterationPriceModel);
		populateName(productOfferingPrice, alterationPriceModel);
		getModelService().save(alterationPriceModel);

		return alterationPriceModel;
	}
}
