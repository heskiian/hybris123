/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProdUsageSpecification;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentUsageChargeComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentUsageUnit;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaSolrDocumentUsageChargeComponentPop} with specific attributes having a {@link TmaUsageProdOfferPriceChargeModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentUsageChargeComponentPopulator
		extends TmaSolrDocumentComponentPopPopulator<TmaUsageProdOfferPriceChargeModel, TmaSolrDocumentUsageChargeComponentPop>
{
	private Converter<TmaProductUsageSpecificationModel, TmaSolrDocumentProdUsageSpecification> prodUsageSpecificationConverter;

	public TmaSolrDocumentUsageChargeComponentPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter,
			final Converter<TmaProductUsageSpecificationModel, TmaSolrDocumentProdUsageSpecification> prodUsageSpecificationConverter,
			final Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter)
	{
		super(pscvUseConverter, pricingLogicAlgorithmConverter);
		this.prodUsageSpecificationConverter = prodUsageSpecificationConverter;
	}

	@Override
	public void populate(final TmaUsageProdOfferPriceChargeModel source, final TmaSolrDocumentUsageChargeComponentPop target)
	{
		super.populate(source, target);

		target.setTierStart(source.getTierStart());
		target.setTierEnd(source.getTierEnd());
		target.setUsageUnit(getUsageUnitFromSource(source.getUsageUnit()));
		if (source.getProductUsageSpec() != null)
		{
			target.setProductUsageSpecification(getProdUsageSpecificationConverter().convert(source.getProductUsageSpec()));
		}
	}

	protected TmaSolrDocumentUsageUnit getUsageUnitFromSource(final UsageUnitModel sourceUsageUnit)
	{
		final TmaSolrDocumentUsageUnit usageUnit = new TmaSolrDocumentUsageUnit();
		if (sourceUsageUnit == null)
		{
			return usageUnit;
		}
		usageUnit.setId(sourceUsageUnit.getId());
		usageUnit.setName(sourceUsageUnit.getName());
		usageUnit.setNamePlural(sourceUsageUnit.getNamePlural());
		usageUnit.setAccumulative(sourceUsageUnit.getAccumulative());
		return usageUnit;
	}

	protected Converter<TmaProductUsageSpecificationModel, TmaSolrDocumentProdUsageSpecification> getProdUsageSpecificationConverter()
	{
		return prodUsageSpecificationConverter;
	}
}
