/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link TmaSubscribedProductModel} having as source a {@link TmaSubscribedProductData}.
 *
 * @since 6.6
 */
public class TmaSubscribedProductReversePopulator implements Populator<TmaSubscribedProductData, TmaSubscribedProductModel>
{
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;

	@Override
	public void populate(final TmaSubscribedProductData source, final TmaSubscribedProductModel target)
	{
		target.setName(source.getName());
		target.setProductCode(source.getProductCode());
		target.setServiceType(TmaServiceType.valueOf(source.getServiceType().name()));
		setSubscriptionBase(source, target);
		target.setBillingsystemId(source.getBillingsystemId());
		target.setBillingSubscriptionId(source.getBillingSubscriptionId());
		target.setSubscriptionStatus(source.getSubscriptionStatus());
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setCancelledDate(source.getCancelledDate());
		target.setRenewalType(source.getRenewalType());
		target.setCancellable(source.getCancellable());
		target.setBillingFrequency(source.getBillingFrequency());
		target.setContractDuration(source.getContractDuration());
		target.setContractFrequency(source.getContractFrequency());
		target.setOrderEntryNumber(source.getOrderEntryNumber());
		target.setOrderNumber(source.getOrderNumber());
		target.setCustomerId(source.getCustomerId());
		target.setPlacedOn(source.getPlacedOn());
		target.setBundledProductCode(source.getParentPOCode());
		target.setPaymentMethodId(source.getPaymentMethodId());
	}

	private void setSubscriptionBase(final TmaSubscribedProductData source, final TmaSubscribedProductModel target)
	{
		final TmaSubscriptionBaseModel subscriptionBase = getTmaSubscriptionBaseService().getSubscriptionBase(
				source.getSubscriptionBaseId(), source.getBillingsystemId());
		if (subscriptionBase == null)
		{
			throw new ModelNotFoundException("Could not find " + TmaSubscriptionBaseModel._TYPECODE + " for subscriber identity "
					+ source.getSubscriptionBaseId());
		}
		target.setSubscriptionBase(subscriptionBase);
	}

	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
	}

	@Required
	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

}
