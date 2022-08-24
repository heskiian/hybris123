/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscv;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * Default populator for populating a {@link TmaSolrDocumentPscvUse} to be persisted by the solr server having a
 * {@link TmaProductSpecCharValueUseModel} as source.
 *
 * @since 2105
 */
public class TmaSolrDocumentPscvuPopulator implements Populator<TmaProductSpecCharValueUseModel, TmaSolrDocumentPscvUse>
{
	private final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscv> pscvConverter;

	public TmaSolrDocumentPscvuPopulator(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscv> pscvConverter)
	{
		this.pscvConverter = pscvConverter;
	}

	@Override
	public void populate(final TmaProductSpecCharValueUseModel source, final TmaSolrDocumentPscvUse target)
	{
		ServicesUtil.validateParameterNotNull(source, "Parameter source can not be null");
		ServicesUtil.validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setValueType(source.getValueType() != null ? source.getValueType().getCode() : null);
		target.setMaxCardinality(source.getMaxCardinality());
		target.setMinCardinality(source.getMinCardinality());
		final List<TmaSolrDocumentPscv> pscvs = new ArrayList<>();
		pscvs.addAll(getPscvConverter().convertAll(source.getProductSpecCharacteristicValues()));
		target.setProductSpecCharacteristicValues(pscvs);
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscv> getPscvConverter()
	{
		return pscvConverter;
	}
}
