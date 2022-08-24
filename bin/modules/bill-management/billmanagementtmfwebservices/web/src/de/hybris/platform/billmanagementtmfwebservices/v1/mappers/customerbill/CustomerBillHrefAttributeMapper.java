/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillHrefAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	public CustomerBillHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getBillNo()))
		{
			target.setHref(BillmanagementtmfwebservicesConstants.CUSTOMER_BILL_API_URL + source.getBillNo());
		}
	}
}
