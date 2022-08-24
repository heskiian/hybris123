/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.productofferingprice.TmaProductOfferingPriceFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;


/**
 * Default implementation of {@link TmaProductOfferingPriceFacade}.
 *
 * @since 2102
 */
public class DefaultTmaProductOfferingPriceFacade implements TmaProductOfferingPriceFacade
{
	private TmaPopService tmaPopService;
	private Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap;

	public DefaultTmaProductOfferingPriceFacade(final TmaPopService tmaPopService,
			final Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap)
	{
		this.tmaPopService = tmaPopService;
		this.productOfferingPriceConverterMap = productOfferingPriceConverterMap;
	}

	@Override
	public TmaProductOfferingPriceData getPop(final String code)
	{
		final TmaProductOfferingPriceModel pop = getTmaPopService().getPop(code);
		return getProductOfferingPriceConverterMap().get(pop.getItemtype()).convert(pop);
	}

	protected TmaPopService getTmaPopService()
	{
		return tmaPopService;
	}

	protected Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> getProductOfferingPriceConverterMap()
	{
		return productOfferingPriceConverterMap;
	}
}
