/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.appliedpayment;

import de.hybris.platform.billmanagementservices.model.BmPartyPaymentItemModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AppliedPayment;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Money;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for appliedAmount attribute between {@link BmPartyPaymentItemModel} and {@link AppliedPayment}
 *
 * @since 2108
 */
public class AppliedPaymentAppliedAmountAttributeMapper extends BmAttributeMapper<BmPartyPaymentItemModel, AppliedPayment>
{
	public AppliedPaymentAppliedAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyPaymentItemModel source, final AppliedPayment target,
			final MappingContext context)
	{
		if (source.getAppliedAmount() != null && source.getCurrency() != null)
		{
			final Money money = new Money();
			money.setValue(source.getAppliedAmount());
			money.setUnit(source.getCurrency().getIsocode());
			target.setAppliedAmount(money);
		}
	}
}
