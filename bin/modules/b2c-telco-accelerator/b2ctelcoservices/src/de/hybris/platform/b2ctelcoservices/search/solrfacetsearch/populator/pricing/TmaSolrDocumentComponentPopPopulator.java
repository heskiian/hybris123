/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaComponentProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentCurrency;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaComponentProdOfferPriceModel} with specific attributes having a {@link TmaComponentProdOfferPriceModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentComponentPopPopulator<SOURCE extends TmaComponentProdOfferPriceModel, TARGET extends TmaSolrDocumentComponentPop>
		extends TmaSolrDocumentProductOfferingPricePopulator<SOURCE, TARGET>
{
	private Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter;

	public TmaSolrDocumentComponentPopPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter,
			final Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter)
	{
		super(pscvUseConverter);
		this.pricingLogicAlgorithmConverter = pricingLogicAlgorithmConverter;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);

		target.setValue(source.getValue());
		target.setCurrency(getCurrencyFromSource(source.getCurrency()));

		if (source.getPricingLogicAlgorithm() != null)
		{
			target.setPla(getPricingLogicAlgorithmConverter().convert(source.getPricingLogicAlgorithm()));
		}
	}

	protected TmaSolrDocumentCurrency getCurrencyFromSource(final CurrencyModel sourceCurrency)
	{
		final TmaSolrDocumentCurrency currency = new TmaSolrDocumentCurrency();

		currency.setName(sourceCurrency.getName());
		currency.setActive(sourceCurrency.getActive());
		currency.setIsocode(sourceCurrency.getIsocode());
		currency.setSymbol(sourceCurrency.getSymbol());
		return currency;
	}

	protected Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> getPricingLogicAlgorithmConverter()
	{
		return pricingLogicAlgorithmConverter;
	}
}
