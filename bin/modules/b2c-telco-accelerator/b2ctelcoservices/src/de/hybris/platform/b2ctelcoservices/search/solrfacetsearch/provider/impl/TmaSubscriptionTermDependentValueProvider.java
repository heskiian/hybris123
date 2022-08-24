/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Base class of Field Value Providers which are dependent on the information provided on {@link SubscriptionTermModel}.
 *
 * @since 6.7
 */
public abstract class TmaSubscriptionTermDependentValueProvider extends TmaAbstractLocalizedPropertyFieldValueProvider implements
		FieldValueProvider
{
	private TmaCommercePriceService commercePriceService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		validateParameterNotNullStandardMessage("model", model);

		final List<FieldValue> fieldValues = new ArrayList<>();
		if (!(model instanceof TmaSimpleProductOfferingModel)
				|| CollectionUtils.isEmpty(indexConfig.getCurrencies())
				|| !indexedProperty.isLocalized())
		{
			return fieldValues;
		}

		addFieldValuesForCurrencies(indexConfig, indexedProperty, fieldValues, (TmaSimpleProductOfferingModel) model);
		return fieldValues;
	}

	protected void addFieldValuesForCurrencies(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final List<FieldValue> fieldValues, final TmaSimpleProductOfferingModel product) throws FieldValueProviderException
	{
		for (final CurrencyModel currency : indexConfig.getCurrencies())
		{
			fieldValues.addAll(createFieldValues(product, indexedProperty, currency));
		}
	}

	private Collection<? extends FieldValue> createFieldValues(final TmaSimpleProductOfferingModel productOffering,
			final IndexedProperty indexedProperty, final CurrencyModel currency) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<>();
		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(productOffering)
				.withProcessTypes(new HashSet<>(Arrays.asList(DEFAULT_CATEGORY_PAGE_PROCESS_TYPE)))
				.withCurrency(currency)
				.build();
		final PriceRowModel minimumPriceForContext = getCommercePriceService().getMinimumPrice(priceContext);
		if (minimumPriceForContext == null)
		{
			throw new FieldValueProviderException("Got no applicable price for context and product with code [" +
					productOffering.getCode() + "]");
		}

		if (!(minimumPriceForContext instanceof SubscriptionPricePlanModel))
		{
			return Collections.emptyList();
		}

		final Set<SubscriptionTermModel> subscriptionTerms = ((SubscriptionPricePlanModel) minimumPriceForContext)
				.getSubscriptionTerms();
		if (CollectionUtils.isEmpty(subscriptionTerms))
		{
			return Collections.emptyList();
		}

		subscriptionTerms.forEach(subscriptionTerm ->
		{
			addFieldValuesForCurrency(fieldValues, indexedProperty, currency, getFieldValueForSubscriptionTerm(subscriptionTerm));
			addFieldValueForLanguages(fieldValues, indexedProperty, subscriptionTerm);
		});

		return fieldValues;
	}

	/**
	 * Retrieves the Field Value to be indexed, retrieved from the Subscription Term provided.
	 *
	 * @param subscriptionTerm
	 *           to retrieve the data from
	 * @return Field Value to be indexed
	 */
	protected abstract Object getFieldValueForSubscriptionTerm(final SubscriptionTermModel subscriptionTerm);

	@Override
	protected String getLocalizedString(final Object model, final Locale locale)
	{
		if (!(model instanceof SubscriptionTermModel))
		{
			return null;
		}

		return (String) getFieldValueForSubscriptionTerm((SubscriptionTermModel) model);
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
