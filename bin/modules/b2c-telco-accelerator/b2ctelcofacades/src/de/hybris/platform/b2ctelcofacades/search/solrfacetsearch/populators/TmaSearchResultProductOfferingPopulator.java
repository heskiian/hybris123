/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Populator for setting product specific values from the solr Search Result to target {@link ProductData}.
 *
 * @since 2007
 */
public class TmaSearchResultProductOfferingPopulator extends SearchResultProductPopulator
{
	private static final String APPROVAL_STATUS_INDEXED_ATTRIBUTE = "approvalStatus";
	private static final String ITEM_TYPE = "itemtype";

	private Converter<SearchResultValueData, PriceData> priceConverter;

	private Converter<SearchResultValueData, List<TmaProductSpecCharacteristicValueUseData>> pscvuConverter;
	
	public TmaSearchResultProductOfferingPopulator(final Converter<SearchResultValueData, PriceData> priceConverter,
			final Converter<SearchResultValueData, List<TmaProductSpecCharacteristicValueUseData>> pscvuConverter)
	{
		this.pscvuConverter = pscvuConverter;
		this.priceConverter = priceConverter;
	}

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		populatePoSpecCharValueUses(source,target);
		target.setItemType(getItemType(source));
		target.setApprovalStatus(getApprovalStatus(source));
	}

	@Override
	protected void populatePrices(final SearchResultValueData source, final ProductData target)
	{
		PriceData solrPrice = getPriceConverter().convert(source);
		target.setPrice(solrPrice);
		target.setPrices(Collections.singletonList(getPriceConverter().convert(source)));
	}

	private String getApprovalStatus(final SearchResultValueData source)
	{
		final String approvalStatus = getValue(source, APPROVAL_STATUS_INDEXED_ATTRIBUTE);
		return StringUtils.isEmpty(approvalStatus) ? StringUtils.EMPTY : approvalStatus;
	}

	private void populatePoSpecCharValueUses(final SearchResultValueData source, final ProductData target)
	{
		target.setProductSpecCharValueUses(getPscvuConverter().convert(source));
	}
	
	private String getItemType(final SearchResultValueData source)
	{
		return getValue(source, ITEM_TYPE);
	}

	protected Converter<SearchResultValueData, PriceData> getPriceConverter()
	{
		return priceConverter;
	}
	
	protected Converter<SearchResultValueData, List<TmaProductSpecCharacteristicValueUseData>> getPscvuConverter()
	{
		return pscvuConverter;
	}
}
