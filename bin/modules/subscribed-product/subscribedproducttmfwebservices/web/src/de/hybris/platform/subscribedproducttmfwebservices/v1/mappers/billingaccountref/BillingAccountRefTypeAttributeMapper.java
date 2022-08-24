/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.billingaccountref;

import de.hybris.platform.subscribedproductservices.model.SpiBillingAccountModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.BillingAccountRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link SpiBillingAccountModel} and
 * {@link BillingAccountRef}
 *
 * @since 2105
 */
public class BillingAccountRefTypeAttributeMapper extends SpiAttributeMapper<SpiBillingAccountModel, BillingAccountRef>
{
	public BillingAccountRefTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiBillingAccountModel source, final BillingAccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(SubscribedproducttmfwebservicesConstants.SPI_BILLING_ACCOUNT_REFERRED_TYPE);
		}
	}
}
