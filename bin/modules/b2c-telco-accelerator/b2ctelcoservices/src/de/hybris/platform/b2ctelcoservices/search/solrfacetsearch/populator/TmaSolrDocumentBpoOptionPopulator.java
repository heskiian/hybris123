/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentBundledProdOfferOption;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * Default populator for populating a {@link TmaSolrDocumentBundledProdOfferOption} to be persisted by the solr server having a
 * {@link TmaBundledProdOfferOptionModel} as source.
 *
 * @since 2105
 */
public class TmaSolrDocumentBpoOptionPopulator
		implements Populator<TmaBundledProdOfferOptionModel, TmaSolrDocumentBundledProdOfferOption>
{
	@Override
	public void populate(final TmaBundledProdOfferOptionModel source, final TmaSolrDocumentBundledProdOfferOption target)
	{
		ServicesUtil.validateParameterNotNull(source, "Parameter source can not be null");
		ServicesUtil.validateParameterNotNull(target, "Parameter target can not be null");

		target.setLowerLimit(source.getLowerLimit());
		target.setUpperLimit(source.getUpperLimit());
		target.setDefaultValue(source.getDefault());
	}
}
