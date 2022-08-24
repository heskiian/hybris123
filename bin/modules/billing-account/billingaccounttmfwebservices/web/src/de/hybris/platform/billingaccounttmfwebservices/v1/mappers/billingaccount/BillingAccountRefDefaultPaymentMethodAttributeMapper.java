/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaPaymentInfoModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referredDefaultPaymentMethod attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2111
 */
public class BillingAccountRefDefaultPaymentMethodAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountRefDefaultPaymentMethodAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (source.getReferredDefaultPaymentMethod() != null)
		{
			target.setDefaultPaymentMethod(
					getMapperFacade().map(source.getReferredDefaultPaymentMethod(), PaymentMethodRef.class, context));
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (target.getDefaultPaymentMethod() == null)
		{
			return;
		}
		BaPaymentInfoModel baPaymentInfoModel = (BaPaymentInfoModel) getBaGenericService()
				.getItem(BaPaymentInfoModel._TYPECODE, target.getDefaultPaymentMethod().getId());
		if (baPaymentInfoModel == null)
		{
			baPaymentInfoModel = (BaPaymentInfoModel) getBaGenericService().createItem(BaPaymentInfoModel.class);
			baPaymentInfoModel.setId(target.getDefaultPaymentMethod().getId());
		}
		getBaGenericService().saveItem(baPaymentInfoModel);
		getMapperFacade().map(target.getDefaultPaymentMethod(), baPaymentInfoModel, context);
		source.setReferredDefaultPaymentMethod(baPaymentInfoModel);
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
