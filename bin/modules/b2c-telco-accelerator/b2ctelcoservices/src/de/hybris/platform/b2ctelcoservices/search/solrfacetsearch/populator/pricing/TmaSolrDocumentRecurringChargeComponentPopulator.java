/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentRecurringChargeComponentPop;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaSolrDocumentRecurringChargeComponentPop} with specific attributes having a {@link TmaRecurringProdOfferPriceChargeModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentRecurringChargeComponentPopulator extends
		TmaSolrDocumentComponentPopPopulator<TmaRecurringProdOfferPriceChargeModel, TmaSolrDocumentRecurringChargeComponentPop>
{
	public TmaSolrDocumentRecurringChargeComponentPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter,
			final Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter)
	{
		super(pscvUseConverter, pricingLogicAlgorithmConverter);
	}

	@Override
	public void populate(final TmaRecurringProdOfferPriceChargeModel source,
			final TmaSolrDocumentRecurringChargeComponentPop target)
	{
		super.populate(source, target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
	}
}
