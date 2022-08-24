/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import org.apache.commons.collections.CollectionUtils;


/**
 * Field value provider for the allowed principals
 *
 * @since 2105
 */
public class TmaProductOfferingAllowedPrincipalsFieldValueProvider extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private static final String ALL_GROUPS = "";
	private final TmaSpoSource spoSource;

	public TmaProductOfferingAllowedPrincipalsFieldValueProvider(final TmaSpoSource spoSource)
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

	protected void addFieldValues(final InputDocument document, final IndexedProperty indexedProperty, final String uid)
			throws FieldValueProviderException
	{
		final String fieldName = String.format("%s_%s_%s", indexedProperty.getName(), indexedProperty.getType(), "mv");
		document.addField(fieldName, uid);
	}

	private void addFieldValues(final InputDocument document, final IndexedProperty indexedProperty,
			final TmaProductOfferingModel productOffering) throws FieldValueProviderException
	{
		if (CollectionUtils.isEmpty(productOffering.getAllowedPrincipals()))
		{
			addFieldValues(document, indexedProperty, ALL_GROUPS);
		}

		for (final PrincipalModel allowedPrincipal : productOffering.getAllowedPrincipals())
		{
			addFieldValues(document, indexedProperty, allowedPrincipal.getUid());
		}
	}

	protected TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

}
