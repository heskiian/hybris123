/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicValueModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resolver for indexing service specification characteristics values in solr.
 *
 * @since 2102
 */
public class TmaServiceSpecCharacteristicValueResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private final CommonI18NService commonI18NService;
	private final TmaSpoSource spoSource;

	public TmaServiceSpecCharacteristicValueResolver(final CommonI18NService commonI18NService, final TmaSpoSource spoSource)
	{
		this.commonI18NService = commonI18NService;
		this.spoSource = spoSource;

	}

	@Override
	protected void addFieldValues(final InputDocument doc, final IndexerBatchContext batchCtx, final IndexedProperty prop,
			final PriceRowModel price, final ValueResolverContext<Object, Object> resolverCtx) throws FieldValueProviderException
	{
		final ProductModel productModel = getSpoSource().getProduct(price);
		if (!(productModel instanceof TmaProductOfferingModel))
		{
			return;
		}

		if (prop.isLocalized())
		{
			for (final LanguageModel languageModel : batchCtx.getFacetSearchConfig().getIndexConfig().getLanguages())
			{
				final Locale locale = getCommonI18NService().getLocaleForLanguage(languageModel);
				addFieldValues(doc, prop, (TmaProductOfferingModel) productModel, locale);
			}
		}
	}

	private void addFieldValues(final InputDocument doc, final IndexedProperty prop, final TmaProductOfferingModel productOffering,
			final Locale locale) throws FieldValueProviderException
	{
		final String pscvId = prop.getValueProviderParameter();
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = productOffering.getProductSpecCharValueUses().stream()
				.flatMap(pscvu -> pscvu.getProductSpecCharacteristicValues().stream()).collect(Collectors.toSet());
		if (pscvs.isEmpty())
		{
			return;
		}
		for (final TmaProductSpecCharacteristicValueModel pscv : pscvs)
		{
			final Set<TmaServiceSpecCharacteristicValueModel> sscvs = pscv.getServiceSpecCharacteristicValues();
			if (CollectionUtils.isNotEmpty(sscvs))
			{
				addSscvFieldValues(sscvs, pscvId, locale, prop, doc);
			}
		}
	}

	private void addSscvFieldValues(final Set<TmaServiceSpecCharacteristicValueModel> sscvs, final String pscvId,
			final Locale locale, final IndexedProperty prop, final InputDocument doc) throws FieldValueProviderException
	{
		for (final TmaServiceSpecCharacteristicValueModel sscv : sscvs)
		{
			if (sscv.getServiceSpecCharacteristic().getId().equals(pscvId))
			{
				final String fieldName = String.format("%s_%s_%s_%s", prop.getName(), locale.getLanguage(), prop.getType(), "mv");
				doc.addField(fieldName, sscv.getValue(locale) + " " + sscv.getUnitOfMeasure().getName(locale));
			}
		}
	}

	protected TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

}
