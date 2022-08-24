/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderDiscountAlterationPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.DefaultTmaAbstractOrderCartPriceBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder for creating instances of {@link TmaAbstractOrderDiscountAlterationPriceModel} to be set on the order and order entry.
 *
 * @since 2007
 */
public class DefaultTmaAbstractOrderDiscountPriceBuilder extends DefaultTmaAbstractOrderCartPriceBuilder
		implements TmaAbstractOrderPriceBuilder
{
	private ModelService modelService;

	public DefaultTmaAbstractOrderDiscountPriceBuilder(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final Double price, final AbstractOrderModel order,
			final List<TaxValue> taxes, final TmaAbstractOrderPriceType priceType)
	{
		final TmaAbstractOrderDiscountAlterationPriceModel orderPrice =
				getModelService().create(TmaAbstractOrderDiscountAlterationPriceModel.class);

		setPriceValues(price, order, orderPrice, retrieveTaxRate(price, order, taxes));
		orderPrice.setBillingTime(getBillingTimeService().getDefaultBillingTime());
		orderPrice.setPriceType(priceType);
		orderPrice.setCurrency(order.getCurrency());
		getModelService().save(orderPrice);
		return orderPrice;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
