/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscribedProductDao;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Validator for {@link TmaSubscribedProductModel}.
 * It ensures that a single instance can be saved for a billingSystemId-billingSubscriptionId pair.
 *
 * @since 6.6
 */
public class UniqueSubscribedProductItemInterceptor implements ValidateInterceptor<TmaSubscribedProductModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(UniqueSubscribedProductItemInterceptor.class);

	private TmaSubscribedProductDao tmaSubscribedProductDao;

	@Override
	public void onValidate(final TmaSubscribedProductModel subscribedProductModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		try
		{
			final TmaSubscribedProductModel existing = getTmaSubscribedProductDao()
					.findSubscribedProduct(subscribedProductModel.getBillingsystemId(),
							subscribedProductModel.getBillingSubscriptionId());
			if (!isSameServiceModelInstance(subscribedProductModel, existing))
			{
				throw new InterceptorException(
						"Duplicated Subscribed Product found for billingSystemId=" + subscribedProductModel.getBillingsystemId()
								+ " and billingSubscriptionId=" + subscribedProductModel.getBillingSubscriptionId() + ".");
			}
		}
		catch (final ModelNotFoundException exception)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(
						MessageFormat.format(
								"No Subscribed Product found for billing system id {0} and subscription id {1}, creating new one",
								subscribedProductModel.getBillingsystemId(), subscribedProductModel.getBillingSubscriptionId()));
			}
		}
	}

	private boolean isSameServiceModelInstance(final TmaSubscribedProductModel subscribedProductModel,
			final TmaSubscribedProductModel existing)
	{
		return subscribedProductModel.getId().equals(existing.getId());
	}

	@Required
	public void setTmaSubscribedProductDao(final TmaSubscribedProductDao tmaSubscribedProductDao)
	{
		this.tmaSubscribedProductDao = tmaSubscribedProductDao;
	}

	protected TmaSubscribedProductDao getTmaSubscribedProductDao()
	{
		return tmaSubscribedProductDao;
	}
}
