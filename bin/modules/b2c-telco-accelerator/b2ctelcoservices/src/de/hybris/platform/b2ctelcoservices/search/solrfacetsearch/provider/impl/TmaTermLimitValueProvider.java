/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Field Value Provider for indexing the Subscription Term Limit of the displayed Product Offering Price.
 *
 * @since 6.7
 */
public class TmaTermLimitValueProvider extends TmaSubscriptionTermDependentValueProvider
{
	private EnumerationService enumerationService;

	@Override
	protected Object getFieldValueForSubscriptionTerm(final SubscriptionTermModel subscriptionTerm)
	{
		final TermOfServiceFrequency termOfServiceFrequency = subscriptionTerm.getTermOfServiceFrequency();
		final Integer termOfServiceNumber = subscriptionTerm.getTermOfServiceNumber();
		return termOfServiceFrequency == null ? null : getTermOfServiceFrequencyName(termOfServiceNumber, termOfServiceFrequency);
	}

	private Object getTermOfServiceFrequencyName(final Integer termOfServiceNumber,
			final TermOfServiceFrequency termOfServiceFrequency)
	{
		final String termOfServiceFrequencyName = ("/") + getEnumerationService().getEnumerationName(termOfServiceFrequency);
		return termOfServiceNumber == null ? (("") + termOfServiceFrequencyName)
				: termOfServiceNumber.toString() + termOfServiceFrequencyName;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

}
