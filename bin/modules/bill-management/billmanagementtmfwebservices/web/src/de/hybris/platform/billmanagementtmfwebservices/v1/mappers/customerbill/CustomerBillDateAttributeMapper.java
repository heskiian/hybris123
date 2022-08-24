/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmOnCyclePartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps billDate for billDate attribute between {@link BmOnCyclePartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillDateAttributeMapper extends BmAttributeMapper<BmOnCyclePartyBillModel, CustomerBill>
{

	public CustomerBillDateAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmOnCyclePartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getBillCycle() != null && source.getBillCycle().getBillingDate() != null)
		{
			target.setBillDate((source.getBillCycle().getBillingDate()));
		}
	}
}
