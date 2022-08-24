/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.StateValue;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import java.util.Map;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for state attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillStateAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	private Map<String, String> customerBillStateDtoMap;

	public CustomerBillStateAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final Map<String, String> customerBillStateDtoMap)
	{
		super(sourceAttributeName, targetAttributeName);
		this.customerBillStateDtoMap = customerBillStateDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getStatus() != null)
		{
			target.setState(StateValue.valueOf(getCustomerBillStateDtoMap().get(source.getStatus().getCode())));
		}
	}

	protected Map<String, String> getCustomerBillStateDtoMap()
	{
		return customerBillStateDtoMap;
	}
}
