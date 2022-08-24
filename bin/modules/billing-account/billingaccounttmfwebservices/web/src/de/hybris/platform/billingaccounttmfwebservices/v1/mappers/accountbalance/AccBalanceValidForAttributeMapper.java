/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountbalance;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link BaAccountBalanceModel} and {@link AccountBalance}
 *
 * @since 2105
 */
public class AccBalanceValidForAttributeMapper
		extends BaAttributeMapper<BaAccountBalanceModel, AccountBalance>
{
	public AccBalanceValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountBalanceModel source, final AccountBalance target,
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
	public void populateSourceAttributeFromTarget(final AccountBalance target, final BaAccountBalanceModel source,
			final MappingContext context)
	{
		source.setStartDateTime(target.getValidFor().getStartDateTime());
		source.setEndDateTime(target.getValidFor().getEndDateTime());
	}
}
