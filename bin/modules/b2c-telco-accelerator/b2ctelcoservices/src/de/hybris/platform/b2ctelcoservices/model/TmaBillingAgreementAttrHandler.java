/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.model;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;


/**
 * Attribute handler for dynamic attribute billAgreement from {@link TmaSubscriptionBaseModel}.
 *
 * @since 1810
 */
public class TmaBillingAgreementAttrHandler extends AbstractDynamicAttributeHandler<TmaBillingAgreementModel,
		TmaSubscriptionBaseModel>
{
	@Override
	public TmaBillingAgreementModel get(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		return subscriptionBaseModel.getSubscribedProducts().stream().filter(
				subscribedProductModel -> subscribedProductModel.getAgreementItem() != null
						&& subscribedProductModel.getAgreementItem().getAgreement() != null).findFirst()
				.map(subscribedProductModel -> subscribedProductModel.getAgreementItem().getAgreement()).orElse(null);
	}
}
