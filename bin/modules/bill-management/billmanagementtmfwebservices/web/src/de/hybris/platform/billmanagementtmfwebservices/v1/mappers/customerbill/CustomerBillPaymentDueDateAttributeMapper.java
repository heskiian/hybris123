/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmOnCyclePartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for paymentDueDate attribute between {@link BmOnCyclePartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillPaymentDueDateAttributeMapper extends BmAttributeMapper<BmOnCyclePartyBillModel, CustomerBill>
{

	public CustomerBillPaymentDueDateAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmOnCyclePartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getBillCycle() != null && source.getBillCycle().getPaymentDueDate() != null)
		{
			target.setPaymentDueDate(source.getBillCycle().getPaymentDueDate());
		}
	}
}
