/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Money;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for taxExcludedAmount attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillTaxExcludedAmountAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	public CustomerBillTaxExcludedAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getAmount() != null && !CollectionUtils.isEmpty(source.getTaxItems()))
		{
			final Money money = new Money();
			source.getTaxItems().forEach(bmAppliedPartyBillingTaxRateModel -> {
				if (bmAppliedPartyBillingTaxRateModel.getQuantity() != null && money.getValue() != null)
				{
					money.setValue((money.getValue() + bmAppliedPartyBillingTaxRateModel.getQuantity()));
				}
				else if (bmAppliedPartyBillingTaxRateModel.getQuantity() != null)
				{
					money.setValue((bmAppliedPartyBillingTaxRateModel.getQuantity()));
				}
			});
			money.setValue(source.getAmount() - money.getValue());
			money.setUnit(source.getCurrency().getIsocode());
			target.setTaxExcludedAmount(money);
		}
	}
}
