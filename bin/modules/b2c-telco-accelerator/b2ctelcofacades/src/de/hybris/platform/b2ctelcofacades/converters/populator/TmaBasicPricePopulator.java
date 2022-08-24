/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.UnitData;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.b2ctelcofacades.price.TmaPriceValueFormatter;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link PriceData} based on {@link PriceRowModel}.
 *
 * @since 6.7
 */
public class TmaBasicPricePopulator<SOURCE extends PriceRowModel, TARGET extends PriceData> implements Populator<SOURCE, TARGET>
{
	private TmaPriceDataFactory tmaPriceDataFactory;
	private Converter<UnitModel, UnitData> tmaUnitConverter;
	private TmaPriceValueFormatter priceValueFormatter;

	public TmaBasicPricePopulator(final Converter<UnitModel, UnitData> tmaUnitConverter,
			final TmaPriceValueFormatter priceValueFormatter)
	{
		this.tmaUnitConverter = tmaUnitConverter;
		this.priceValueFormatter = priceValueFormatter;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		final BigDecimal priceValue = BigDecimal.valueOf(source.getPrice());
		target.setValue(priceValue);
		target.setCode(source.getCode());
		target.setPriority(source.getPriority());
		target.setCurrencyIso(source.getCurrency().getIsocode());
		target.setFormattedValue(getPriceValueFormatter().formatPriceValue(priceValue, source.getCurrency()));
		target.setModifiedtime(source.getModifiedtime());
		target.setUnit(getTmaUnitConverter().convert(source.getUnit()));
		target.setUnitFactor(source.getUnitFactor());
	}

	protected TmaPriceDataFactory getTmaPriceDataFactory()
	{
		return tmaPriceDataFactory;
	}

	@Required
	public void setTmaPriceDataFactory(final TmaPriceDataFactory tmaPriceDataFactory)
	{
		this.tmaPriceDataFactory = tmaPriceDataFactory;
	}

	protected Converter<UnitModel, UnitData> getTmaUnitConverter()
	{
		return tmaUnitConverter;
	}

	protected TmaPriceValueFormatter getPriceValueFormatter()
	{
		return priceValueFormatter;
	}
}
