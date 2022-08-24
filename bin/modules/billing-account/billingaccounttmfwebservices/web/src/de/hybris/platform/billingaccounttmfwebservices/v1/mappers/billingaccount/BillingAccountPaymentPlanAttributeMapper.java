/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for payment plan attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountPaymentPlanAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountPaymentPlanAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getPaymentPlans()))
		{
			return;
		}

		final List<PaymentPlan> paymentPlanList = source.getPaymentPlans().stream()
				.map(paymentPlan -> getMapperFacade().map(paymentPlan, PaymentPlan.class, context))
				.collect(Collectors.toList());

		target.setPaymentPlan(paymentPlanList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getPaymentPlan()))
		{
			return;
		}

		source.setPaymentPlans(getAllPaymentPlans(target.getPaymentPlan(), context));
	}

	private Set<BaPaymentPlanModel> getAllPaymentPlans(final List<PaymentPlan> paymentPlans, final MappingContext context)
	{
		final Set<BaPaymentPlanModel> result = new HashSet<>();
		paymentPlans.forEach(paymentPlan -> {
			final BaPaymentPlanModel baPaymentPlanModel = (BaPaymentPlanModel) getBaGenericService()
					.createItem(BaPaymentPlanModel.class);
			getMapperFacade().map(paymentPlan, baPaymentPlanModel, context);
			result.add(baPaymentPlanModel);
		});
		return result;
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
