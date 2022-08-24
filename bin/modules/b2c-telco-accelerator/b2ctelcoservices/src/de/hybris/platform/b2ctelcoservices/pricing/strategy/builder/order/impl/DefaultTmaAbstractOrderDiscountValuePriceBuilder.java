/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderDiscountAlterationPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderDiscountValuePriceBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Default implementation for {@link TmaAbstractOrderDiscountValuePriceBuilder}
 *
 * @since 2007
 */
public class DefaultTmaAbstractOrderDiscountValuePriceBuilder implements TmaAbstractOrderDiscountValuePriceBuilder
{
	private TmaAbstractOrderPriceBuilder orderPriceBuilder;
	private ModelService modelService;

	public DefaultTmaAbstractOrderDiscountValuePriceBuilder(final TmaAbstractOrderPriceBuilder orderPriceBuilder,
			final ModelService modelService)
	{
		this.orderPriceBuilder = orderPriceBuilder;
		this.modelService = modelService;
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final DiscountValue discountValue, final AbstractOrderModel order,
			final List<TaxValue> taxes)
	{
		final TmaAbstractOrderDiscountAlterationPriceModel promotionPrice = (TmaAbstractOrderDiscountAlterationPriceModel) getOrderPriceBuilder()
				.buildPrice(discountValue.getAppliedValue(), order, null, TmaAbstractOrderPriceType.DISCOUNT);
		promotionPrice.setCatalogCode(discountValue.getCode());
		getModelService().save(promotionPrice);

		return promotionPrice;
	}

	protected TmaAbstractOrderPriceBuilder getOrderPriceBuilder()
	{
		return orderPriceBuilder;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
