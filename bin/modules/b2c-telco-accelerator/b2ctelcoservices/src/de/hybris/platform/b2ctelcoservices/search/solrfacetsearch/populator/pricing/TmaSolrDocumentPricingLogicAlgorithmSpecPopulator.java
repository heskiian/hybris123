/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithmSpec;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default populator used for populating pricing logic algorithm spec attributes in {@link TmaSolrDocumentPricingLogicAlgorithmSpec}
 * with data from {@link TmaPricingLogicAlgorithmSpecModel}.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2102
 */
public class TmaSolrDocumentPricingLogicAlgorithmSpecPopulator
		implements Populator<TmaPricingLogicAlgorithmSpecModel, TmaSolrDocumentPricingLogicAlgorithmSpec>
{
	private Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter;

	public TmaSolrDocumentPricingLogicAlgorithmSpecPopulator(
			final Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter)
	{
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaPricingLogicAlgorithmSpecModel source, final TmaSolrDocumentPricingLogicAlgorithmSpec target)
	{
		validateParameterNotNull(source, "Parameter source can not be null");
		validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getId());

		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIds(getExternalIdentifierConverter().convertAll(source.getExternalIds()));
		}

		target.setName(source.getName());
		target.setDescription(source.getDescription());

		if (source.getApprovalStatus() != null)
		{
			target.setApprovalStatus(source.getApprovalStatus().getCode());
		}

		target.setOnlineDate(source.getOnlineDate());
		target.setOfflineDate(source.getOfflineDate());
	}

	protected Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> getExternalIdentifierConverter()
	{
		return externalIdentifierConverter;
	}
}
