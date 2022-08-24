/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPriceBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.TmaAbstractOrderEntryPopPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.util.TaxValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Builder that creates a list of {@link TmaAbstractOrderPriceModel} from the Product Offering Price set on the price
 *
 * @since 2007
 */
public class TmaAbstractOrderProductOfferingPriceBuilder extends DefaultTmaAbstractOrderEntryPriceBuilder
{
	private Map<String, TmaAbstractOrderEntryPopPriceBuilder> productOfferingPriceBuilderMap;

	public TmaAbstractOrderProductOfferingPriceBuilder(
			Map<String, TmaAbstractOrderEntryPopPriceBuilder> productOfferingPriceBuilderMap)
	{
		this.productOfferingPriceBuilderMap = productOfferingPriceBuilderMap;
	}

	@Override
	protected boolean shouldCreatePrices(final PriceRowModel price)
	{
		return price.getProductOfferingPrice() != null;
	}

	@Override
	protected List<TmaAbstractOrderPriceModel> createPrices(final PriceRowModel price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		final TmaProductOfferingPriceModel productOfferingPrice = price.getProductOfferingPrice();
		final String popItemType = productOfferingPrice.getItemtype();
		if (getProductOfferingPriceBuilderMap().containsKey(popItemType))
		{
			final TmaAbstractOrderPriceModel tmaAbstractOrderPriceModel = getProductOfferingPriceBuilderMap()
					.get(popItemType).buildPrice(productOfferingPrice, entry, taxes);
			return Collections.singletonList(tmaAbstractOrderPriceModel);
		}
		return Collections.emptyList();
	}

	protected Map<String, TmaAbstractOrderEntryPopPriceBuilder> getProductOfferingPriceBuilderMap()
	{
		return productOfferingPriceBuilderMap;
	}
}
