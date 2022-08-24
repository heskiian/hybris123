/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderDiscountAlterationPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderDiscountValuePriceBuilder;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionActionService;
import de.hybris.platform.util.DiscountValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import static java.util.stream.Stream.concat;


/**
 * Custom implementation for {@link DefaultPromotionActionService} to clean up and create cart prices for promotions
 *
 * @since 2007
 */
public class DefaultTmaPromotionActionService extends DefaultPromotionActionService
{
	private TmaAbstractOrderDiscountValuePriceBuilder orderDiscountValuePriceBuilder;

	public DefaultTmaPromotionActionService(final TmaAbstractOrderDiscountValuePriceBuilder orderDiscountValuePriceBuilder)
	{
		this.orderDiscountValuePriceBuilder = orderDiscountValuePriceBuilder;
	}

	@Override
	public void recalculateTotals(AbstractOrderModel order)
	{
		super.recalculateTotals(order);
		order.getEntries().stream().forEach(orderEntry -> createOrderEntryPromotionPrices(orderEntry));
		createOrderPromotionPrices(order);
	}

	@Override
	public List<ItemModel> removeDiscountValue(final String code, final AbstractOrderModel order)
	{
		final List<ItemModel> modifiedItems = super.removeDiscountValue(code, order);
		removeOrderLevelPromotionPrices(order);
		removeOrderEntryLevelPromotionPrices(order);
		return modifiedItems;
	}

	/**
	 * Remove cart prices for the given promotion from the  order
	 *
	 * @param order
	 * 		the order from which the prices will be removed
	 */
	private void removeOrderLevelPromotionPrices(final AbstractOrderModel order)
	{
		final TmaAbstractOrderCompositePriceModel orderPrice = (TmaAbstractOrderCompositePriceModel) order.getPrice();
		if (orderPrice != null)
		{
			removePromotionPrices(orderPrice);
			getModelService().save(order);
		}
	}

	/**
	 * Remove cart prices for the given promotion from the order entries
	 *
	 * @param order
	 * 		the order from which the prices will be removed
	 */
	private void removeOrderEntryLevelPromotionPrices(final AbstractOrderModel order)
	{
		order.getEntries().forEach(entry -> {
			if (entry.getPrice() != null)
			{
				removePromotionPrices((TmaAbstractOrderCompositePriceModel) entry.getPrice());
				getModelService().save(entry);
			}
		});
	}

	/**
	 * Creates prices for the promotions applied at order level
	 *
	 * @param order
	 * 		the order for which prices will be created
	 */
	private void createOrderPromotionPrices(final AbstractOrderModel order)
	{
		createPromotionPrices(order, order.getGlobalDiscountValues(), (TmaAbstractOrderCompositePriceModel) order.getPrice());
	}

	/**
	 * Creates prices for the promotions applied at order entry level
	 *
	 * @param entry
	 * 		the entry for which prices will be created
	 */
	private void createOrderEntryPromotionPrices(final AbstractOrderEntryModel entry)
	{
		createPromotionPrices(entry.getOrder(), entry.getDiscountValues(), (TmaAbstractOrderCompositePriceModel) entry.getPrice());
	}

	private void createPromotionPrices(final AbstractOrderModel order, final List<DiscountValue> discounts,
			TmaAbstractOrderCompositePriceModel price)
	{
		if (CollectionUtils.isEmpty(discounts))
		{
			return;
		}

		if (price == null)
		{
			price = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		}

		final Set<TmaAbstractOrderPriceModel> orderEntryChildPrices = new HashSet<>();
		discounts.forEach(discountValue -> orderEntryChildPrices
				.add(getOrderDiscountValuePriceBuilder().buildPrice(discountValue, order, null)));

		if (CollectionUtils.isNotEmpty(price.getChildPrices()))
		{
			final Set<TmaAbstractOrderPriceModel> childPrices = concat(price.getChildPrices().stream(),
					orderEntryChildPrices.stream()).collect(Collectors.toSet());
			price.setChildPrices(childPrices);
		}
		getModelService().save(price);
	}

	private void removePromotionPrices(final TmaAbstractOrderCompositePriceModel price)
	{
		final Set<TmaAbstractOrderPriceModel> currentChildPrices = price.getChildPrices();
		final Set<TmaAbstractOrderPriceModel> promotionChildPrices = new HashSet<>();

		if (CollectionUtils.isNotEmpty(currentChildPrices))
		{
			for (final TmaAbstractOrderPriceModel childPrice : currentChildPrices)
			{
				if (childPrice instanceof TmaAbstractOrderCompositePriceModel)
				{
					removePromotionPrices((TmaAbstractOrderCompositePriceModel) childPrice);
				}

				if (childPrice instanceof TmaAbstractOrderDiscountAlterationPriceModel
						&&
						TmaAbstractOrderPriceType.DISCOUNT
								.equals(((TmaAbstractOrderDiscountAlterationPriceModel) childPrice).getPriceType()))
				{
					promotionChildPrices.add(childPrice);
				}
			}
		}
		getModelService().removeAll(promotionChildPrices);
		getModelService().save(price);
	}

	protected TmaAbstractOrderDiscountValuePriceBuilder getOrderDiscountValuePriceBuilder()
	{
		return orderDiscountValuePriceBuilder;
	}
}
