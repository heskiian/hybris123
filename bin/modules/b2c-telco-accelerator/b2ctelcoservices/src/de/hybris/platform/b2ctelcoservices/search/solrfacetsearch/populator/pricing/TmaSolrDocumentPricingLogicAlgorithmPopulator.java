/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithmSpec;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default populator used for populating pricing logic algorithm attributes in {@link TmaSolrDocumentPricingLogicAlgorithm} with
 * data from {@link TmaPricingLogicAlgorithmModel}.
 * The populated target will then be persisted in the indexed type by the solr server.
 *
 * @since 2102
 */
public class TmaSolrDocumentPricingLogicAlgorithmPopulator
		implements Populator<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm>
{
	private Converter<TmaPricingLogicAlgorithmSpecModel, TmaSolrDocumentPricingLogicAlgorithmSpec> pricingLogicAlgorithmSpecConverter;
	private Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter;

	public TmaSolrDocumentPricingLogicAlgorithmPopulator(
			final Converter<TmaPricingLogicAlgorithmSpecModel, TmaSolrDocumentPricingLogicAlgorithmSpec> pricingLogicAlgorithmSpecConverter,
			final Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter)
	{
		this.pricingLogicAlgorithmSpecConverter = pricingLogicAlgorithmSpecConverter;
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaPricingLogicAlgorithmModel source, final TmaSolrDocumentPricingLogicAlgorithm target)
	{
		validateParameterNotNull(source, "Parameter source can not be null");
		validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setOnlineDate(source.getOnlineDate());
		target.setOfflineDate(source.getOfflineDate());
		target.setType(source.getItemtype());

		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIds(getExternalIdentifierConverter().convertAll(source.getExternalIds()));
		}

		if (source.getPricingLogicAlgorithmSpec() != null)
		{
			target.setPricingLogicAlgorithmSpec(
					getPricingLogicAlgorithmSpecConverter().convert(source.getPricingLogicAlgorithmSpec()));
		}
	}

	protected Converter<TmaPricingLogicAlgorithmSpecModel, TmaSolrDocumentPricingLogicAlgorithmSpec> getPricingLogicAlgorithmSpecConverter()
	{
		return pricingLogicAlgorithmSpecConverter;
	}

	protected Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> getExternalIdentifierConverter()
	{
		return externalIdentifierConverter;
	}
}
