/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default populator used for populating external identifier attributes in {@link TmaSolrDocumentExternalIdentifier}
 * with data from {@link TmaExternalIdentifierModel}.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2102
 */
public class TmaSolrDocumentExternalIdentifierPopulator
		implements Populator<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier>
{
	@Override
	public void populate(final TmaExternalIdentifierModel source, final TmaSolrDocumentExternalIdentifier target)
	{
		validateParameterNotNull(source, "Parameter source can not be null");
		validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getBillingSystemExtId());
		target.setOwner(source.getBillingSystemId());
		target.setResourceType(source.getResourceType());
	}
}