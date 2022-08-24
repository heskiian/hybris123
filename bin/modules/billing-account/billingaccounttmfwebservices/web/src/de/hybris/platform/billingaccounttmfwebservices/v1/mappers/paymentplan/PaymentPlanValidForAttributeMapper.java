/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanValidForAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	private MapperFacade mapperFacade;

	public PaymentPlanValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		if (source.getStartDateTime() == null && source.getEndDateTime() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDateTime());
		timePeriod.setEndDateTime(source.getEndDateTime());
		target.setValidFor(timePeriod);
	}

	@Override
	public void populateSourceAttributeFromTarget(final PaymentPlan target, final BaPaymentPlanModel source,
			final MappingContext context)
	{
		if (target.getValidFor() == null)
		{
			return;
		}

		source.setStartDateTime(target.getValidFor().getStartDateTime());
		source.setEndDateTime(target.getValidFor().getEndDateTime());
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
