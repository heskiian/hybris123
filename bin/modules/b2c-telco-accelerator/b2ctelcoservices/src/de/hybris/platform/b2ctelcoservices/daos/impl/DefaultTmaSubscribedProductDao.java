/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscribedProductDao;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link TmaSubscribedProductDao}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscribedProductDao extends DefaultGenericSearchDao<TmaSubscribedProductModel> implements
		TmaSubscribedProductDao
{
	public DefaultTmaSubscribedProductDao()
	{
		super(TmaSubscribedProductModel._TYPECODE);
	}

	@Override
	public TmaSubscribedProductModel findSubscribedProduct(final String billingSystemId, final String billingSubscriptionId)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(SubscriptionModel.BILLINGSYSTEMID, billingSystemId);
		params.put(TmaSubscribedProductModel.BILLINGSUBSCRIPTIONID, billingSubscriptionId);
		return findUnique(params);
	}

	@Override
	public TmaSubscribedProductModel findSubscribedProductById(final String subscribedProductId)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(TmaSubscribedProductModel.ID, subscribedProductId);
		return findUnique(params);
	}

	@Override
	public List<TmaSubscribedProductModel> findSubscriptionsForPaymentMethod(final String paymentMethodId)
	{
		validateParameterNotNull(paymentMethodId, "paymentMethodId must not be null!");
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(TmaSubscribedProductModel.PAYMENTMETHODID, paymentMethodId);
		return find(parameters);

	}
}
