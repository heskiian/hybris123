/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resolves Solr Property for the region(s) given an {@link PriceRowModel}. When there is no region defined (meaning
 * the price is applicable for all regions) a string(EMPTY_STRING) will be added to the Solr doc.
 *
 * @since 1907
 */
public class TmaPriceRegionResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{

	private static final String ALL_REGION_PRICES = " ";

	@Override
	protected void addFieldValues(final InputDocument doc, final IndexerBatchContext batchCtx, final IndexedProperty prop,
			final PriceRowModel priceRow, final ValueResolverContext<Object, Object> resolverCtx) throws FieldValueProviderException
	{
		final Set<RegionModel> regions = priceRow.getRegions();
		if (CollectionUtils.isEmpty(regions))
		{
			addFieldValues(doc, prop, ALL_REGION_PRICES);
		}
		else
		{
			for (final RegionModel region : regions)
			{
				addFieldValues(doc, prop, region.getIsocode());
			}
		}
	}

	protected void addFieldValues(final InputDocument doc, final IndexedProperty prop, final String isocode)
			throws FieldValueProviderException
	{
		final String fieldName = String.format("%s_%s_%s", prop.getName(), prop.getType(), "mv");
		doc.addField(fieldName, isocode);

	}
}
