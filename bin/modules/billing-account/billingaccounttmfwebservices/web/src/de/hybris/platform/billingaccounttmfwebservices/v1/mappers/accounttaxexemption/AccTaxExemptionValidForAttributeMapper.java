/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accounttaxexemption;

import de.hybris.platform.billingaccountservices.model.BaAccountTaxExemptionModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountTaxExemption;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link BaAccountTaxExemptionModel} and {@link AccountTaxExemption}
 *
 * @since 2105
 */
public class AccTaxExemptionValidForAttributeMapper extends BaAttributeMapper<BaAccountTaxExemptionModel, AccountTaxExemption>
{
	public AccTaxExemptionValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountTaxExemptionModel source, final AccountTaxExemption target,
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
	public void populateSourceAttributeFromTarget(final AccountTaxExemption target, final BaAccountTaxExemptionModel source,
			final MappingContext context)
	{
		source.setStartDateTime(target.getValidFor().getStartDateTime());
		source.setEndDateTime(target.getValidFor().getEndDateTime());
	}
}
