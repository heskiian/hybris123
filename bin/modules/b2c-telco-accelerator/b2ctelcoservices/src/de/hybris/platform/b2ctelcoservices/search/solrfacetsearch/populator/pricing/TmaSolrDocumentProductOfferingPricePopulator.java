/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentBillingTime;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} to be persisted in the indexed type by the
 * solr server having a {@link TmaProductOfferingPriceModel} as source.
 *
 * @since 2007
 */
public abstract class TmaSolrDocumentProductOfferingPricePopulator<SOURCE extends TmaProductOfferingPriceModel,
		TARGET extends TmaSolrDocumentProductOfferingPrice> implements Populator<SOURCE, TARGET>
{
	private Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter;

	public TmaSolrDocumentProductOfferingPricePopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter)
	{
		this.pscvUseConverter = pscvUseConverter;
	}

	@Override
	public void populate(final TmaProductOfferingPriceModel source, final TmaSolrDocumentProductOfferingPrice target)
	{
		if (source == null)
		{
			return;
		}

		target.setType(source.getItemtype());
		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setPriceEvent(getPriceEventFromSource(source.getPriceEvent()));

		if (CollectionUtils.isNotEmpty(source.getProductSpecCharacteristicValues()))
		{
			final Set<TmaSolrDocumentPscvUse> pscvs = new HashSet<>();
			source.getProductSpecCharacteristicValues().forEach(sourcePscv -> pscvs.add(getPscvUseConverter().convert(sourcePscv)));
			target.setProductSpecCharacteristicValueUses(pscvs);
		}
	}

	protected TmaSolrDocumentBillingTime getPriceEventFromSource(final BillingTimeModel sourcePriceEvent)
	{
		final TmaSolrDocumentBillingTime priceEvent = new TmaSolrDocumentBillingTime();
		if (sourcePriceEvent == null)
		{
			return priceEvent;
		}

		priceEvent.setCode(sourcePriceEvent.getCode());
		priceEvent.setName(sourcePriceEvent.getNameInCart());
		priceEvent.setDescription(sourcePriceEvent.getDescription());
		return priceEvent;
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> getPscvUseConverter()
	{
		return pscvUseConverter;
	}
}
