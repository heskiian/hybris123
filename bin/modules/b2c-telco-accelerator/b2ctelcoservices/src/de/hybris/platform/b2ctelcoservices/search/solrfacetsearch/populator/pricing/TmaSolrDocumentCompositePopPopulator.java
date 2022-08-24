/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentCompositePop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default populator for populating a {@link TmaSolrDocumentProductOfferingPrice} of type
 * {@link TmaSolrDocumentCompositePop} with specific attributes having a {@link TmaCompositeProdOfferPriceModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2007
 */
public class TmaSolrDocumentCompositePopPopulator<SOURCE extends TmaCompositeProdOfferPriceModel, TARGET extends TmaSolrDocumentCompositePop>
		extends TmaSolrDocumentProductOfferingPricePopulator<SOURCE, TARGET>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TmaSolrDocumentCompositePopPopulator.class);

	private static final String MISSING_ITEM_TYPE_ERROR_MESSAGE = "There is no Solr Document -  Product Offering Price Converter"
			+ " for key %s; no prices of this type will be indexed. "
			+ "If you want to index this types of prices, include the converter in the converter map.";

	private Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> productOfferingPriceConvertersMap;

	public TmaSolrDocumentCompositePopPopulator(final Map<String, Converter<TmaProductOfferingPriceModel,
			TmaSolrDocumentProductOfferingPrice>> productOfferingPriceConvertersMap,
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvUseConverter)
	{
		super(pscvUseConverter);
		this.productOfferingPriceConvertersMap = productOfferingPriceConvertersMap;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);

		final Set<TmaProductOfferingPriceModel> childrenPopModels = source.getChildren();
		if (CollectionUtils.isEmpty(childrenPopModels))
		{
			return;
		}

		final List<TmaSolrDocumentProductOfferingPrice> solrDocumentChildrenPops = new ArrayList<>(childrenPopModels.size());
		childrenPopModels.forEach(childPop -> {
			final Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice> popConverter =
					getProductOfferingPriceConvertersMap()
							.get(childPop.getItemtype());
			if (popConverter == null)
			{
				LOGGER.warn(String.format(MISSING_ITEM_TYPE_ERROR_MESSAGE, childPop.getItemtype()));
				return;
			}
			solrDocumentChildrenPops.add(popConverter.convert(childPop));
		});

		target.setChildren(solrDocumentChildrenPops);
	}

	private Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> getProductOfferingPriceConvertersMap()
	{
		return productOfferingPriceConvertersMap;
	}
}
