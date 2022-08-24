/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl.pricing;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Field Value Provider for indexing pscvu's. The indexed pscvu's will include the entire structure,
 * persisted as json.
 *
 * @since 2105
 */
public class TmaProdSpecCharValueUseResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private static final Logger LOG = Logger.getLogger(TmaProdSpecCharValueUseResolver.class);

	private static final String CONVERSION_EXCEPTION_MESSAGE =
			"Failed to convert POP Object to JSON: Could not index Product Specification Characterstics Value Uses for Price Row with code %s ";

	private TmaSpoSource spoSource;
	private Converter<TmaProductSpecCharValueUseModel, TmaSolrDocumentPscvUse> tmaSolrDocumentPscvuConverter;
	
	public TmaProdSpecCharValueUseResolver(
			final TmaSpoSource spoSource,
			final Converter<TmaProductSpecCharValueUseModel, TmaSolrDocumentPscvUse> tmaSolrDocumentPscvuConverter)
	{
		this.spoSource = spoSource;
		this.tmaSolrDocumentPscvuConverter = tmaSolrDocumentPscvuConverter;
	}

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final PriceRowModel priceRowModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (priceRowModel == null || getSpoSource().getProduct(priceRowModel) == null)
		{
			return;
		}

		final TmaProductOfferingModel product = (TmaProductOfferingModel) getSpoSource().getProduct(priceRowModel);
		final Set<TmaSolrDocumentPscvUse> pscvUses = new HashSet<>();
		product.getProductSpecCharValueUses().forEach(sourcePscvu -> pscvUses.add(getTmaSolrDocumentPscvUseConverter().convert(sourcePscvu)));
		addProductOfferingPriceToInputDocument(inputDocument, indexedProperty, pscvUses,
				priceRowModel.getCode());
	}

	/**
	 * Transform the {@link TmaSolrDocumentPscvUse} into json format and appends it to the solr document received
	 * as {@param inputDocument}.
	 *
	 * @param inputDocument
	 * 		solr input document to be populated with the Product Offering Price
	 * @param indexedProperty
	 * 		the indexed property for which the input document will be constructed
	 * @param pscvUses
	 * 		product offering price to be populated on the document
	 * @param priceRowCode
	 * 		code of the price row for which the pop is to be indexed
	 * @throws FieldValueProviderException
	 * 		if the converted product offering price cannot be added to the document
	 */
	protected void addProductOfferingPriceToInputDocument(final InputDocument inputDocument, final IndexedProperty indexedProperty,
			final Set<TmaSolrDocumentPscvUse> pscvUses, final String priceRowCode)
			throws FieldValueProviderException
	{
		final ObjectMapper mapper = new ObjectMapper();
		final String pscvUsesAsString;
		try
		{
			pscvUsesAsString = mapper.writeValueAsString(pscvUses);
		}
		catch (final JsonProcessingException exception)
		{
			LOG.error(String.format(CONVERSION_EXCEPTION_MESSAGE, priceRowCode), exception);
			return;
		}
		final String fieldName = String.format("%s_%s", indexedProperty.getName(), indexedProperty.getType());
		inputDocument.addField(fieldName, pscvUsesAsString);
	}

	protected TmaSpoSource getSpoSource()
	{
		return spoSource;
	}
	
	protected Converter<TmaProductSpecCharValueUseModel, TmaSolrDocumentPscvUse> getTmaSolrDocumentPscvUseConverter()
	{
		return tmaSolrDocumentPscvuConverter;
	}
}
