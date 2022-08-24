/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Field Value Provider for indexing Product Offering Prices. The indexed prices will be the minimum ones available for
 * the {@link TmaProcessType#ACQUISITION} flow.
 *
 * @since 6.7
 */
public class TmaPoPriceValueProvider extends TmaAbstractPropertyFieldValueProvider
{
	private TmaCommercePriceService commercePriceService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		validateParameterNotNull(model, TmaProductOfferingModel._TYPECODE + " must not be null.");

		final List<FieldValue> fieldValues = new ArrayList<>();
		if (hasNoApplicablePrice(indexConfig, model))
		{
			return fieldValues;
		}

		final TmaProductOfferingModel product = (TmaProductOfferingModel) model;
		for (final CurrencyModel currency : indexConfig.getCurrencies())
		{
			fieldValues.addAll(createFieldValue(product, indexedProperty, currency));
		}
		return fieldValues;
	}

	protected boolean hasNoApplicablePrice(final IndexConfig indexConfig, final Object model)
	{
		return !(model instanceof TmaProductOfferingModel) || (model instanceof TmaBundledProductOfferingModel) || CollectionUtils
				.isEmpty(indexConfig.getCurrencies());
	}

	protected List<FieldValue> createFieldValue(final TmaProductOfferingModel productOfferingModel,
			final IndexedProperty indexedProperty,
			final CurrencyModel currency) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<>();
		final Double basePrice = computeBasePriceValueForProductOffering(productOfferingModel, currency);
		addFieldValuesForCurrency(fieldValues, indexedProperty, currency, basePrice);
		return fieldValues;
	}

	/**
	 * Computes the base price value for the given {@param productOffering} and {@param currency} by retrieving it for
	 * the
	 * appropriate price context for which the price will be applicable.
	 *
	 * @param productOffering
	 *           product offering for which to retrieve the price
	 * @param currency
	 *           currency for which the price is applicable
	 * @return computed price value
	 * @throws FieldValueProviderException
	 *            if the given {@param price} has neither recurring charges, a value or one time charges,
	 *            thus no value can be found
	 */
	protected Double computeBasePriceValueForProductOffering(final TmaProductOfferingModel productOffering,
			final CurrencyModel currency) throws FieldValueProviderException
	{
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(productOffering)
				.withProcessTypes(new HashSet<>(Arrays.asList(DEFAULT_CATEGORY_PAGE_PROCESS_TYPE)))
				.withCurrency(currency)
				.build();
		final Double minimumPriceValue = getCommercePriceService().getMinimumPriceValue(priceContext);
		if (minimumPriceValue == null)
		{
			throw new FieldValueProviderException("Got no applicable price for context and product with code [" +
					productOffering.getCode() + "]");
		}
		return minimumPriceValue;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(final TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}
}
