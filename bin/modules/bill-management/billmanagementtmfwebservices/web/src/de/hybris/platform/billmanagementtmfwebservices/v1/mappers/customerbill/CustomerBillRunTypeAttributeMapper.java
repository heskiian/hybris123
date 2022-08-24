/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmOnCyclePartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for runType attribute between {@link BmOnCyclePartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillRunTypeAttributeMapper extends BmAttributeMapper<BmOnCyclePartyBillModel, CustomerBill>
{
	private static final String RUN_TYPE = "onCycle";

	public CustomerBillRunTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmOnCyclePartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getBillNo()))
		{
			target.setRunType(RUN_TYPE);
		}
	}
}
