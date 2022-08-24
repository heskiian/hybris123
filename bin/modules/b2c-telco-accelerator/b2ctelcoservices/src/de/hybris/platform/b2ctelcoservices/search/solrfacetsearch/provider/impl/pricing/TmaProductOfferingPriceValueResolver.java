/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Field Value Provider for indexing Product Offering Prices. The indexed prices will include the entire Composite Structure,
 * persisted as json.
 *
 * @since 2007
 */
public class TmaProductOfferingPriceValueResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private static final Logger LOG = Logger.getLogger(TmaProductOfferingPriceValueResolver.class);

	private static final String CONVERSION_EXCEPTION_MESSAGE =
			"Failed to convert POP Object to JSON: Could not index Product Offering Price for Price Row with code %s ";

	private Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> productOfferingPriceConvertersMap;

	public TmaProductOfferingPriceValueResolver(
			final Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> popConvertersMap)
	{
		this.productOfferingPriceConvertersMap = popConvertersMap;
	}

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final PriceRowModel priceRowModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (priceRowModel == null || priceRowModel.getProductOfferingPrice() == null)
		{
			return;
		}

		final TmaProductOfferingPriceModel productOfferingPrice = priceRowModel.getProductOfferingPrice();
		final TmaSolrDocumentProductOfferingPrice solrDocumentProductOfferingPrice =
				getProductOfferingPriceConvertersMap().get(productOfferingPrice.getItemtype()).convert(productOfferingPrice);
		addProductOfferingPriceToInputDocument(inputDocument, indexedProperty, solrDocumentProductOfferingPrice,
				priceRowModel.getCode());
	}

	/**
	 * Transform the {@link TmaSolrDocumentProductOfferingPrice} into json format and appends it to the solr document received
	 * as {@param inputDocument}.
	 *
	 * @param inputDocument
	 * 		solr input document to be populated with the Product Offering Price
	 * @param indexedProperty
	 * 		the indexed property for which the input document will be constructed
	 * @param solrDocumentProductOfferingPrice
	 * 		product offering price to be populated on the document
	 * @param priceRowCode
	 * 		code of the price row for which the pop is to be indexed
	 * @throws FieldValueProviderException
	 * 		if the converted product offering price cannot be added to the document
	 */
	protected void addProductOfferingPriceToInputDocument(final InputDocument inputDocument, final IndexedProperty indexedProperty,
			final TmaSolrDocumentProductOfferingPrice solrDocumentProductOfferingPrice, final String priceRowCode)
			throws FieldValueProviderException
	{
		final ObjectMapper mapper = new ObjectMapper();
		final String productOfferingPriceAsString;
		try
		{
			productOfferingPriceAsString = mapper.writeValueAsString(solrDocumentProductOfferingPrice);
		}
		catch (final JsonProcessingException exception)
		{
			LOG.error(String.format(CONVERSION_EXCEPTION_MESSAGE, priceRowCode), exception);
			return;
		}
		final String fieldName = String.format("%s_%s", indexedProperty.getName(), indexedProperty.getType());
		inputDocument.addField(fieldName, productOfferingPriceAsString);
	}

	protected Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> getProductOfferingPriceConvertersMap()
	{
		return productOfferingPriceConvertersMap;
	}
}
