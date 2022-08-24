/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import java.util.Collection;
import java.util.List;


/**
 * @since 1810
 */
public class TmaChargeResolver implements ValueResolver<PriceRowModel>
{
	private TmaChargeValueSource chargeSource;
	private RangeNameProvider rangeNameProvider;
	private FieldNameProvider fieldNameProvider;

	@Override
	public void resolve(
			InputDocument doc,
			IndexerBatchContext batchCtx,
			Collection<IndexedProperty> props,
			PriceRowModel priceRow)
			throws FieldValueProviderException
	{
		CurrencyModel currency = priceRow.getCurrency();
		for (IndexedProperty prop : props)
		{
			Double price = chargeSource.getPrice(priceRow, prop.getValueProviderParameter());
			if (price == null)
			{
				continue;
			}

			List<String> rangeNameList = rangeNameProvider.getRangeNameList(prop, price, currency.getIsocode());
			if (rangeNameList.isEmpty())
			{
				addField(doc, currency, prop, price);
			}
			else
			{
				for (String range : rangeNameList)
				{
					addField(doc, currency, prop, range);
				}
			}
		}
	}

	private void addField(InputDocument doc, CurrencyModel currency, IndexedProperty prop, Object price)
			throws FieldValueProviderException
	{
		Collection<String> fieldNames = fieldNameProvider.getFieldNames(prop, currency.getIsocode().toLowerCase());
		for (String fieldName : fieldNames)
		{
			doc.addField(fieldName, price);
		}
	}

	public TmaChargeValueSource getChargeSource()
	{
		return chargeSource;
	}

	public void setChargeSource(TmaChargeValueSource chargeSource)
	{
		this.chargeSource = chargeSource;
	}

	public RangeNameProvider getRangeNameProvider()
	{
		return rangeNameProvider;
	}

	public void setRangeNameProvider(RangeNameProvider rangeNameProvider)
	{
		this.rangeNameProvider = rangeNameProvider;
	}

	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
