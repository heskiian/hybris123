/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentAlterationDiscountComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaSolrDocumentAlterationDiscountComponentPop} with specific attributes having a {@link TmaProdOfferPriceAlterationModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentDiscountPriceAlterationPopulator extends
		TmaSolrDocumentComponentPopPopulator<TmaProdOfferPriceAlterationModel, TmaSolrDocumentAlterationDiscountComponentPop>
{
	public TmaSolrDocumentDiscountPriceAlterationPopulator(final Converter<TmaProductSpecCharacteristicValueModel,
			TmaSolrDocumentPscvUse> pscvUseConverter,
			final Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter)
	{
		super(pscvUseConverter, pricingLogicAlgorithmConverter);
	}

	@Override
	public void populate(final TmaProdOfferPriceAlterationModel source, final TmaSolrDocumentAlterationDiscountComponentPop target)
	{
		super.populate(source, target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
		target.setIsPercentage(source.getIsPercentage());
	}
}
