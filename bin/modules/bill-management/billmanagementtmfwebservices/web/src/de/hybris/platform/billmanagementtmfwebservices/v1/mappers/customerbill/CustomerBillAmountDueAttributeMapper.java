/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Money;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for amountDue attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillAmountDueAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	public CustomerBillAmountDueAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getBillingAccount() == null || source.getCurrency() == null)
		{
			return;
		}
		final Money money = new Money();
		source.getBillingAccount().getAccountBalances().forEach(bmAccountBalanceModel -> {
			if (money.getValue() != null && bmAccountBalanceModel.getRemainingAmount() != null)
			{
				money.setValue(money.getValue() + bmAccountBalanceModel.getRemainingAmount());
			}
			else if (bmAccountBalanceModel.getRemainingAmount() != null)
			{
				money.setValue(bmAccountBalanceModel.getRemainingAmount());
			}
		});
		money.setUnit(source.getCurrency().getIsocode());
		target.setAmountDue(money);
	}
}
