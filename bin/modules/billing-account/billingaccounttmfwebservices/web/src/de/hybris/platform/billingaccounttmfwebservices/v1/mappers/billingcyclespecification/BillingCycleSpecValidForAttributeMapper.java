/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingcyclespecification;

import de.hybris.platform.billingaccountservices.model.BaBillingCycleSpecificationModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingCycleSpecificationRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link BaBillingCycleSpecificationModel} and {@link BillingCycleSpecificationRefOrValue}
 *
 * @since 2105
 */
public class BillingCycleSpecValidForAttributeMapper
		extends BaAttributeMapper<BaBillingCycleSpecificationModel, BillingCycleSpecificationRefOrValue>
{
	private MapperFacade mapperFacade;

	public BillingCycleSpecValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingCycleSpecificationModel source,
			final BillingCycleSpecificationRefOrValue target, final MappingContext context)
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
	public void populateSourceAttributeFromTarget(final BillingCycleSpecificationRefOrValue target,
			final BaBillingCycleSpecificationModel source, final MappingContext context)
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
