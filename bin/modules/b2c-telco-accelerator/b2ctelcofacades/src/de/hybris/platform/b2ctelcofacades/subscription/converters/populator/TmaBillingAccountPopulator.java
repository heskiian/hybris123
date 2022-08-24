/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;


import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.converters.Populator;


/**
 * Populator for populating {@link TmaBillingAccountData} with details from {@link TmaBillingAccountModel}.
 *
 * @since 6.6
 */
public class TmaBillingAccountPopulator implements Populator<TmaBillingAccountModel, TmaBillingAccountData>
{
	@Override
	public void populate(final TmaBillingAccountModel source, final TmaBillingAccountData target)
	{
		target.setBillingAccountId(source.getBillingAccountId());
		target.setBillingSystemId(source.getBillingSystemId());
	}
}
