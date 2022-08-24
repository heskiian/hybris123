/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderAllowanceAlterationPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAllowanceProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPopBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the allowance price alteration component
 *
 * @since 2011
 */
public class TmaAbstractOrderAllowanceAlterationPriceBuilder
		extends DefaultTmaAbstractOrderEntryPopBuilder<TmaAllowanceProdOfferPriceAlterationModel>
{
	public TmaAbstractOrderAllowanceAlterationPriceBuilder(final ModelService modelService,
			final TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final TmaAllowanceProdOfferPriceAlterationModel productOfferingPrice,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderAllowanceAlterationPriceModel allowancePriceModel = getModelService()
				.create(TmaAbstractOrderAllowanceAlterationPriceModel.class);

		allowancePriceModel.setBillingTime(productOfferingPrice.getPriceEvent());
		allowancePriceModel.setPriceType(TmaAbstractOrderPriceType.CREDIT_ALLOWANCE);
		if (Boolean.TRUE.equals(productOfferingPrice.getIsPercentage()))
		{
			allowancePriceModel.setPercentage(productOfferingPrice.getValue());
		}
		else
		{
			populatePriceValues(productOfferingPrice.getValue(), entry, taxes, allowancePriceModel);
		}
		allowancePriceModel.setCycleStart(productOfferingPrice.getCycleStart());
		allowancePriceModel.setCycleEnd(productOfferingPrice.getCycleEnd());
		populateCatalogCode(productOfferingPrice, allowancePriceModel);
		populateName(productOfferingPrice, allowancePriceModel);
		getModelService().save(allowancePriceModel);

		return allowancePriceModel;
	}
}
