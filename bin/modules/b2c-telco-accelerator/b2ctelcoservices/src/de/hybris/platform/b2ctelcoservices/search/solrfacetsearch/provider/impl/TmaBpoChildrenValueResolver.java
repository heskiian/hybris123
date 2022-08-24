/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.data.TmaBpoChildContext;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentBundledProductOffering;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Resolver for the bundledProductOffering property on {@link TmaBundledProductOfferingModel}.
 *
 * @since 2105
 */
public class TmaBpoChildrenValueResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private static final Logger LOG = Logger.getLogger(TmaBpoChildrenValueResolver.class);
	private static final String CONVERSION_EXCEPTION_MESSAGE =
			"Failed to convert BPO children Object to JSON: Could not index children for Price Row with code %s ";

	private Converter<TmaBpoChildContext, TmaSolrDocumentBundledProductOffering> bpoSolrConverter;

	public TmaBpoChildrenValueResolver(final Converter<TmaBpoChildContext, TmaSolrDocumentBundledProductOffering> bpoSolrConverter)
	{
		this.bpoSolrConverter = bpoSolrConverter;
	}

	@Override
	protected void addFieldValues
			(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
					final IndexedProperty indexedProperty, final PriceRowModel priceRow,
					final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (!(priceRow.getProduct() instanceof TmaBundledProductOfferingModel))
		{
			return;
		}
		final TmaBundledProductOfferingModel bpo = (TmaBundledProductOfferingModel) priceRow.getProduct();
		if (CollectionUtils.isEmpty(bpo.getChildren()))
		{
			return;
		}
		final Set<TmaSolrDocumentBundledProductOffering> bundledProductOfferings = new HashSet<>();

		bpo.getChildren().forEach(child -> {
					final TmaBpoChildContext bpoChildContext = new TmaBpoChildContext();
					bpoChildContext.setParent(bpo);
					bpoChildContext.setChild(child);
					final TmaSolrDocumentBundledProductOffering bundledProdOffer = getBpoSolrConverter().convert(bpoChildContext);

					bundledProductOfferings.add(bundledProdOffer);
				}
		);
		addBundledProductOfferingsToInputDocument(inputDocument, indexedProperty, bundledProductOfferings, priceRow.getCode());
	}

	protected void addBundledProductOfferingsToInputDocument(final InputDocument inputDocument,
			final IndexedProperty indexedProperty,
			final Set<TmaSolrDocumentBundledProductOffering> bundledProductOfferings, final String priceRowCode)
			throws FieldValueProviderException
	{
		final ObjectMapper mapper = new ObjectMapper();
		final List<String> bundledPoAsString = new ArrayList<>();

		bundledProductOfferings.forEach(bundledProdOffer -> {
			try
			{
				bundledPoAsString.add(mapper.writeValueAsString(bundledProdOffer));
			}
			catch (JsonProcessingException exception)
			{
				LOG.error(String.format(CONVERSION_EXCEPTION_MESSAGE, priceRowCode), exception);
			}
		});
		inputDocument.addField(indexedProperty, bundledPoAsString);
	}

	protected Converter<TmaBpoChildContext, TmaSolrDocumentBundledProductOffering> getBpoSolrConverter()
	{
		return bpoSolrConverter;
	}
}
