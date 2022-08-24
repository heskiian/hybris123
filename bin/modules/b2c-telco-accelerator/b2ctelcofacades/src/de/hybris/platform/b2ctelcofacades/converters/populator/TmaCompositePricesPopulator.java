/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.ObjectUtils;


/**
 * Populates {@link ProductData} from a {@link ProductModel} entity.It is used for populating the composite prices on the product.
 *
 * @since 2007
 */
public class TmaCompositePricesPopulator extends AbstractProductPopulator<ProductModel, ProductData>
{
	private Converter<PriceRowModel, PriceData> tmaPriceDataConverter;

	public TmaCompositePricesPopulator(Converter<PriceRowModel, PriceData> tmaPriceDataConverter)
	{
		super();
		this.tmaPriceDataConverter = tmaPriceDataConverter;
	}

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
			target.setPrices(getPrices(source.getEurope1Prices()));
		}
	}

	private List<PriceData> getPrices(final Collection<PriceRowModel> priceRowModels)
	{
		final List<PriceData> priceDatas = new ArrayList<>();
		priceRowModels.forEach(
				priceRowModel -> priceDatas.add(getTmaPriceDataConverter().convert(priceRowModel)));
		return priceDatas;
	}


	protected Converter<PriceRowModel, PriceData> getTmaPriceDataConverter()
	{
		return tmaPriceDataConverter;
	}
}

