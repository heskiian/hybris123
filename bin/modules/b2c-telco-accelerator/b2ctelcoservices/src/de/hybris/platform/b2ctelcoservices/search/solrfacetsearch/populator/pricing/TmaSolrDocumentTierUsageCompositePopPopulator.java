/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaTierUsageChargeCompositePopModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentCompositePop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentTierUsageCompositePop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentUsageChargeType;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import java.util.Map;

import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaSolrDocumentCompositePop} with specific attributes having a {@link TmaTierUsageChargeCompositePopModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentTierUsageCompositePopPopulator
		extends TmaSolrDocumentCompositePopPopulator<TmaTierUsageChargeCompositePopModel, TmaSolrDocumentTierUsageCompositePop>
{
	public TmaSolrDocumentTierUsageCompositePopPopulator(
			final Map<String, Converter<TmaProductOfferingPriceModel,
					TmaSolrDocumentProductOfferingPrice>> productOfferingPriceConvertersMap,
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter)
	{
		super(productOfferingPriceConvertersMap, pscvUseConverter);
	}

	@Override
	public void populate(final TmaTierUsageChargeCompositePopModel source, final TmaSolrDocumentTierUsageCompositePop target)
	{
		super.populate(source, target);
		target.setUsageChargeType(getUsageTypeFromSource(source.getUsageChargeType()));
	}

	protected TmaSolrDocumentUsageChargeType getUsageTypeFromSource(final UsageChargeType sourceUsageChargeType)
	{
		final TmaSolrDocumentUsageChargeType usageChargeType = new TmaSolrDocumentUsageChargeType();
		if (sourceUsageChargeType == null)
		{
			return usageChargeType;
		}

		usageChargeType.setCode(sourceUsageChargeType.getCode());
		return usageChargeType;
	}

}
