/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link ProductData} from a {@link ProductModel} entity.It is use for populating list of product offering
 * price to ProductData
 *
 * @since 1810
 */
public class TmaProductOfferingPricesPopulator extends AbstractProductPopulator<ProductModel, ProductData>
{
	private Converter<PriceRowModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter;

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		if (!(source instanceof TmaProductOfferingModel))
		{
			return;
		}
		final TmaProductOfferingModel productOfferingModel = (TmaProductOfferingModel) source;

		if (!ObjectUtils.isEmpty(productOfferingModel.getEurope1Prices()))
		{
			target.setProductOfferingPrices(getProductOfferingPrices(source.getEurope1Prices()));
		}
	}

	protected List<SubscriptionPricePlanData> getProductOfferingPrices(final Collection<PriceRowModel> priceRowModels)
	{
		final List<SubscriptionPricePlanData> subscriptionPricePlanDataList = new ArrayList<>();
		priceRowModels.forEach(
				priceRowModel -> subscriptionPricePlanDataList.add(getTmaSubscriptionPricePlanConverter().convert(priceRowModel)));
		return subscriptionPricePlanDataList;
	}

	protected Converter<PriceRowModel, SubscriptionPricePlanData> getTmaSubscriptionPricePlanConverter()
	{
		return tmaSubscriptionPricePlanConverter;
	}

	@Required
	public void setTmaSubscriptionPricePlanConverter(
			final Converter<PriceRowModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter)
	{
		this.tmaSubscriptionPricePlanConverter = tmaSubscriptionPricePlanConverter;
	}

}
