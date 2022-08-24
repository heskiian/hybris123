/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscv;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * Default populator for populating a {@link TmaSolrDocumentPscv} to be persisted by the solr server having a
 * {@link TmaProductSpecCharacteristicValueModel} as source.
 *
 * @since 2011
 */
public class TmaSolrDocumentPscvPopulator implements Populator<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscv>
{
	@Override
	public void populate(final TmaProductSpecCharacteristicValueModel source, final TmaSolrDocumentPscv target)
	{
		ServicesUtil.validateParameterNotNull(source, "Parameter source can not be null");
		ServicesUtil.validateParameterNotNull(target, "Parameter target can not be null");

		target.setId(source.getId());
		target.setDescription(source.getDescription());
		target.setValue(source.getValue());
		target.setUnitOfMeasurment(source.getUnitOfMeasure() != null ? source.getUnitOfMeasure().getId() : null);
		target.setValueType(source.getValueType() != null ? source.getValueType().getCode() : null);
	}
}