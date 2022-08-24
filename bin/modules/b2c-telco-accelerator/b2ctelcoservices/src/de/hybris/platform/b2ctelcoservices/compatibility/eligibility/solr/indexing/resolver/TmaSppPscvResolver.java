/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @since 1810
 */
public class TmaSppPscvResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TmaSppPscvResolver.class);
	private CommonI18NService commonI18NService;
	private TmaSpoSource spoSource;

	@Override
	protected void addFieldValues(InputDocument doc, IndexerBatchContext batchCtx, IndexedProperty prop, ItemModel itemModel,
			ValueResolverContext<Object, Object> resolverCtx) throws FieldValueProviderException
	{
		LOGGER.debug("resolving pscvs (" + prop.getName() + ") for: " + itemModel + " " + itemModel.getClass());
		ProductModel prod = spoSource.getProduct((PriceRowModel) itemModel);
		if (!(prod instanceof TmaProductOfferingModel))
		{
			return;
		}

		if (prop.isLocalized())
		{
			for (LanguageModel languageModel : batchCtx.getFacetSearchConfig().getIndexConfig().getLanguages())
			{
				Locale locale = getCommonI18NService().getLocaleForLanguage(languageModel);
				addFieldValues(doc, prop, (TmaProductOfferingModel) prod, locale);
			}

		}
	}

	protected void addFieldValues(InputDocument doc, IndexedProperty prop, TmaProductOfferingModel prod, Locale locale)
			throws FieldValueProviderException
	{
		String pscvId = prop.getValueProviderParameter();
		Set<TmaProductSpecCharacteristicValueModel> pscvs = prod
				.getProductSpecCharValueUses()
				.stream()
				.filter(pscvu -> pscvu.getMinCardinality().equals(pscvu.getMaxCardinality())
						&& pscvu.getMinCardinality().equals(pscvu.getProductSpecCharacteristicValues().size()))
				.flatMap(pscvu -> pscvu.getProductSpecCharacteristicValues().stream())
				.collect(Collectors.toSet());

		for (TmaProductSpecCharacteristicValueModel pscv : pscvs)
		{
			if (pscv.getProductSpecCharacteristic().getId().equals(pscvId))
			{
				String fieldName = String.format("%s_%s_%s_%s", prop.getName(), locale.getLanguage(), prop.getType(), "mv");
				doc.addField(fieldName, pscv.getValue() + " " + pscv.getUnitOfMeasure().getName(locale));
			}
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

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
