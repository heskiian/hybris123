/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcoservices.model.TmaComponentProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaComponentProdOfferPriceData} from {@link TmaComponentProdOfferPriceModel}.
 *
 * @since 2007
 */
public class TmaComponentProdOfferPricePopulator<SOURCE extends TmaComponentProdOfferPriceModel,
		TARGET extends TmaComponentProdOfferPriceData> implements Populator<SOURCE, TARGET>
{
	private Converter<CurrencyModel, CurrencyData> currencyConverter;
	private Map<String, Converter<TmaPricingLogicAlgorithmModel, TmaPricingLogicAlgorithmData>> pricingLogicAlgorithmConverterMap;

	public TmaComponentProdOfferPricePopulator(final Converter<CurrencyModel, CurrencyData> currencyConverter,
			final Map<String, Converter<TmaPricingLogicAlgorithmModel, TmaPricingLogicAlgorithmData>> pricingLogicAlgorithmConverterMap)
	{
		this.currencyConverter = currencyConverter;
		this.pricingLogicAlgorithmConverterMap = pricingLogicAlgorithmConverterMap;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setValue(source.getValue());
		target.setCurrency(getCurrencyConverter().convert(source.getCurrency()));

		if (source.getPricingLogicAlgorithm() != null)
		{
			final Converter<TmaPricingLogicAlgorithmModel, TmaPricingLogicAlgorithmData> pricingLogicAlgorithmConverter =
					getPricingLogicAlgorithmConverterMap().get(source.getPricingLogicAlgorithm().getItemtype());
			target.setPla(pricingLogicAlgorithmConverter.convert(source.getPricingLogicAlgorithm()));
		}
	}

	protected Converter<CurrencyModel, CurrencyData> getCurrencyConverter()
	{
		return currencyConverter;
	}

	protected Map<String, Converter<TmaPricingLogicAlgorithmModel, TmaPricingLogicAlgorithmData>> getPricingLogicAlgorithmConverterMap()
	{
		return pricingLogicAlgorithmConverterMap;
	}
}
