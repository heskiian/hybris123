/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaAllowanceProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentAlterationAllowanceComponentPop;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default populator for populating a {@link TmaSolrDocumentAlterationAllowanceComponentPop} with attributes from
 * {@link TmaAllowanceProdOfferPriceAlterationModel} as source.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2011
 */
public class TmaSolrDocumentAllowancePriceAlterationPopulator<SOURCE extends TmaAllowanceProdOfferPriceAlterationModel,
		TARGET extends TmaSolrDocumentAlterationAllowanceComponentPop> implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setCycleStart(source.getCycleStart());
		target.setCycleEnd(source.getCycleEnd());
		target.setIsPercentage(source.getIsPercentage());
		target.setAllowanceType(source.getType().getCode());
	}
}
