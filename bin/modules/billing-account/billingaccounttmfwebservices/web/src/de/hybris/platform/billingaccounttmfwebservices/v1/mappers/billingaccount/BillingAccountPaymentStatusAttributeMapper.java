/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.enums.BaPaymentStatus;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for paymentStatus attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2111
 */
public class BillingAccountPaymentStatusAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	public BillingAccountPaymentStatusAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingAccountModel source, final BillingAccount target,
			final MappingContext context)
	{
		//no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getPaymentStatus()))
		{
			source.setPaymentStatus(BaPaymentStatus.valueOf(target.getPaymentStatus()));
		}
	}
}
