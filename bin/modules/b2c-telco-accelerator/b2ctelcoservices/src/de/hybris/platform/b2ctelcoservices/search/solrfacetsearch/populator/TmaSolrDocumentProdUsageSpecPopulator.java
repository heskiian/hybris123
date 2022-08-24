/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProdUsageSpecification;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.commons.collections.CollectionUtils;




/**
 * Default populator for populating a {@link TmaSolrDocumentProdUsageSpecification} to be persisted by the solr server having a
 * {@link TmaProductUsageSpecificationModel} as source.
 *
 * @since 2102
 */
public class TmaSolrDocumentProdUsageSpecPopulator
		implements Populator<TmaProductUsageSpecificationModel, TmaSolrDocumentProdUsageSpecification>
{
	private Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter;

	public TmaSolrDocumentProdUsageSpecPopulator(
			final Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter)
	{
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaProductUsageSpecificationModel source, final TmaSolrDocumentProdUsageSpecification target)
	{
		ServicesUtil.validateParameterNotNull(source, "Parameter source can not be null");
		ServicesUtil.validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getId());
		target.setName(source.getName());
		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIds(getExternalIdentifierConverter().convertAll(source.getExternalIds()));
		}

	}

	protected Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> getExternalIdentifierConverter()
	{
		return externalIdentifierConverter;
	}
}
