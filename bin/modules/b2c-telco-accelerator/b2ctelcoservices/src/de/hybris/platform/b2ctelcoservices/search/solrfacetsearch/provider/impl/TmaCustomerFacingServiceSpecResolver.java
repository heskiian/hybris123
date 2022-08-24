/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaCustomerFacingServiceSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resolver for indexing the Customer Facing Service Specifications {@link TmaCustomerFacingServiceSpecModel} present in
 * {@link TmaProductSpecificationModel}
 *
 * @since 2102
 */
public class TmaCustomerFacingServiceSpecResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private final TmaSpoSource spoSource;

	public TmaCustomerFacingServiceSpecResolver(final TmaSpoSource spoSource)
	{
		this.spoSource = spoSource;
	}

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchCtx,
			final IndexedProperty indexedProperty, final PriceRowModel price,
			final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		final ProductModel productModel = getSpoSource().getProduct(price);

		if (!(productModel instanceof TmaProductOfferingModel))
		{
			return;
		}
		addFieldValues(document, indexedProperty, (TmaProductOfferingModel) productModel);
	}

	private void addFieldValues(final InputDocument document, final IndexedProperty indexedProperty,
			final TmaProductOfferingModel productOffering) throws FieldValueProviderException
	{
		if (productOffering.getProductSpecification() == null
				|| CollectionUtils.isEmpty(productOffering.getProductSpecification().getCfsSpecs()))
		{
			return;
		}
		for (final TmaCustomerFacingServiceSpecModel cfsSpec : productOffering.getProductSpecification().getCfsSpecs())
		{
			final String fieldName = String.format("%s_%s_%s", indexedProperty.getName(), indexedProperty.getType(), "mv");
			document.addField(fieldName, cfsSpec.getId());
		}
	}

	protected TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

}
