/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentInfoModel;
import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referredPaymentMethod attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanRefPaymentMethodAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public PaymentPlanRefPaymentMethodAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getReferredPaymentMethod()))
		{
			target.setPaymentMethod(getMapperFacade().map(source.getReferredPaymentMethod(), PaymentMethodRef.class, context));
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final PaymentPlan target, final BaPaymentPlanModel source,
			final MappingContext context)
	{
		if (target.getPaymentMethod() == null)
		{
			return;
		}

		BaPaymentInfoModel baPaymentInfoModel = (BaPaymentInfoModel) getBaGenericService()
				.getItem(BaPaymentInfoModel._TYPECODE, target.getPaymentMethod().getId());
		if (baPaymentInfoModel == null)
		{
			baPaymentInfoModel = (BaPaymentInfoModel) getBaGenericService()
					.createItem(BaPaymentInfoModel.class);
			baPaymentInfoModel.setId(target.getPaymentMethod().getId());
		}
		source.setReferredPaymentMethod(baPaymentInfoModel);
		source.setPaymentMethod(null);
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
