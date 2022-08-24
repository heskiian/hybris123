/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;

import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the productOfferingPrice on {@link PriceData} from {@link PriceRowModel}
 *
 * @since 2007
 */
public class TmaPriceDataProductOfferingPricePopulator<SOURCE extends PriceRowModel, TARGET extends PriceData>
		implements Populator<SOURCE, TARGET>
{
	private Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap;

	public TmaPriceDataProductOfferingPricePopulator(
			Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap)
	{
		this.productOfferingPriceConverterMap = productOfferingPriceConverterMap;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		final TmaProductOfferingPriceModel productOfferingPrice = source.getProductOfferingPrice();

		if (ObjectUtils.isEmpty(productOfferingPrice))
		{
			return;
		}

		final Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData> productOfferingPriceConverter =
				getProductOfferingPriceConverterMap().get(productOfferingPrice.getItemtype());

		if (ObjectUtils.isEmpty(productOfferingPriceConverter))
		{
			return;
		}

		target.setProductOfferingPrice(productOfferingPriceConverter.convert(productOfferingPrice));
	}

	protected Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> getProductOfferingPriceConverterMap()
	{
		return productOfferingPriceConverterMap;
	}
}
