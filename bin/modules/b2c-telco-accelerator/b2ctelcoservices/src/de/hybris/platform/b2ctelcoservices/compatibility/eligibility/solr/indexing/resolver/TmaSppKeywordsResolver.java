/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductKeywordsValueResolver.*;
import static de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils.*;


/**
 * @since 1810
 */
public class TmaSppKeywordsResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private TmaSpoSource spoSource;

	@Override
	protected void addFieldValues(
			InputDocument document,
			IndexerBatchContext batchCtx,
			IndexedProperty indexedProp,
			PriceRowModel price,
			ValueResolverContext<Object, Object> context) throws FieldValueProviderException
	{
		ProductModel product = spoSource.getProduct(price);
		addFieldValues(document, indexedProp, product, context);
	}

	protected void addFieldValues(
			InputDocument document,
			IndexedProperty prop,
			ProductModel product,
			ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		final Set<String> keywords = new HashSet<>();

		collectKeywords(keywords, product);
		if (keywords.isEmpty() && !getBoolean(prop, OPTIONAL_PARAM, OPTIONAL_PARAM_DEFAULT_VALUE))
		{
			throw new FieldValueProviderException("No value resolved for indexed property " + prop.getName());
		}

		boolean split = getBoolean(prop, SPLIT_PARAM, SPLIT_PARAM_DEFAULT_VALUE);
		if (split)
		{
			for (String keyword : keywords)
			{
				document.addField(prop, keyword, resolverContext.getFieldQualifier());
			}
		}
		else
		{
			String value = String.join(getString(prop, SEPARATOR_PARAM, SEPARATOR_PARAM_DEFAULT_VALUE), keywords);
			document.addField(prop, value, resolverContext.getFieldQualifier());
		}
	}

	protected void collectKeywords(final Set<String> keywords, final ProductModel product)
	{
		if (product == null)
		{
			return;
		}
		List<KeywordModel> productKeys = product.getKeywords();
		productKeys.stream().map(KeywordModel::getKeyword).filter(keyword -> !StringUtils.isBlank(keyword)).forEach(keywords::add);
		if (product instanceof VariantProductModel)
		{
			collectKeywords(keywords, ((VariantProductModel) product).getBaseProduct());
		}
	}

	public TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

	public void setSpoSource(TmaSpoSource spoSource)
	{
		this.spoSource = spoSource;
	}
}
