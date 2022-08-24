/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaFinancialAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.FinancialAccountRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for financial account attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2111
 */
public class BillingAccountFinancialAccountAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountFinancialAccountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
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
		if (target.getFinancialAccount() == null)
		{
			return;
		}

		source.setFinancialAccount(getFinancialAccount(target.getFinancialAccount(), context));
	}

	private BaFinancialAccountModel getFinancialAccount(final FinancialAccountRef financialAccountRef,
			final MappingContext context)
	{
		BaFinancialAccountModel financialAccountModel = (BaFinancialAccountModel) getBaGenericService()
				.getItem(BaFinancialAccountModel._TYPECODE, financialAccountRef.getId());
		if (financialAccountModel == null)
		{
			financialAccountModel = (BaFinancialAccountModel) getBaGenericService().createItem(BaFinancialAccountModel.class);
		}
		getMapperFacade().map(financialAccountRef, financialAccountModel, context);
		getBaGenericService().saveItem(financialAccountModel);
		return financialAccountModel;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
