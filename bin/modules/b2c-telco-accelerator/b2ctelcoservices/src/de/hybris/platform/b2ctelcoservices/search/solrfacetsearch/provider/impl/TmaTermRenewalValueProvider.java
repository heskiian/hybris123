/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Field Value Provider for indexing the Subscription Term Limit of the displayed Product Offering Price.
 *
 * @since 6.7
 */
public class TmaTermRenewalValueProvider extends TmaSubscriptionTermDependentValueProvider
{
	private TypeService typeService;

	protected Object getFieldValueForSubscriptionTerm(final SubscriptionTermModel subscriptionTerm)
	{
		final TermOfServiceRenewal termOfServiceRenewal = subscriptionTerm.getTermOfServiceRenewal();
		return termOfServiceRenewal != null ? getTypeService().getEnumerationValue(termOfServiceRenewal).getName() : null;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
