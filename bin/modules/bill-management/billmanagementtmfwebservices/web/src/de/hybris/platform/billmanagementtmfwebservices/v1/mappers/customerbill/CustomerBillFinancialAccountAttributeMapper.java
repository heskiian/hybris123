/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.FinancialAccountRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for financialAccount attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillFinancialAccountAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	private MapperFacade mapperFacade;

	public CustomerBillFinancialAccountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (source.getBillingAccount() != null && source.getBillingAccount().getFinancialAccount()!=null)
		{
			target.setFinancialAccount(
					getMapperFacade().map(source.getBillingAccount().getFinancialAccount(), FinancialAccountRef.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
