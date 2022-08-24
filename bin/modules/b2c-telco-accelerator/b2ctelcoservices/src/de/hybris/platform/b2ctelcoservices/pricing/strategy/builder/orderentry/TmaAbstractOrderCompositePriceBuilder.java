/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Builder that creates an {@link TmaAbstractOrderPriceModel} from the composite product offering price
 *
 * @since 2007
 */
public class TmaAbstractOrderCompositePriceBuilder<T extends TmaCompositeProdOfferPriceModel>
		extends DefaultTmaAbstractOrderEntryPopBuilder<T>
{
	private Map<String, TmaAbstractOrderEntryPopPriceBuilder<TmaProductOfferingPriceModel>> productOfferingPriceBuilderMap;

	public TmaAbstractOrderCompositePriceBuilder(ModelService modelService, TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy,
			Map<String, TmaAbstractOrderEntryPopPriceBuilder<TmaProductOfferingPriceModel>> productOfferingPriceBuilderMap)
	{
		super(modelService, tmaRetrieveTaxRateStrategy);
		this.productOfferingPriceBuilderMap = productOfferingPriceBuilderMap;
	}

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final T productOfferingPrice, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		final TmaAbstractOrderCompositePriceModel price = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		buildChildPrices(productOfferingPrice, entry, taxes, price);
		populateCatalogCode(productOfferingPrice, price);
		getModelService().save(price);
		return price;
	}

	protected void buildChildPrices(TmaCompositeProdOfferPriceModel productOfferingPrice, AbstractOrderEntryModel entry,
			List<TaxValue> taxes, TmaAbstractOrderCompositePriceModel price)
	{
		final Set<TmaAbstractOrderPriceModel> childPrices = new HashSet<>();
		productOfferingPrice.getChildren().forEach(child ->
		{
			final String childItemType = child.getItemtype();
			if (getProductOfferingPriceBuilderMap().containsKey(childItemType))
			{
				final TmaAbstractOrderPriceModel tmaAbstractOrderPriceModel = getProductOfferingPriceBuilderMap()
						.get(childItemType).buildPrice(child, entry, taxes);
				childPrices.add(tmaAbstractOrderPriceModel);
			}
		});
		price.setChildPrices(childPrices);
	}

	protected Map<String, TmaAbstractOrderEntryPopPriceBuilder<TmaProductOfferingPriceModel>> getProductOfferingPriceBuilderMap()
	{
		return productOfferingPriceBuilderMap;
	}
}
