/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountrelationship;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccountservices.model.BaAccountRelationshipModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRelationship;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link BaAccountRelationshipModel} and {@link AccountRelationship}
 *
 * @since 2111
 */
public class AccRelationshipValidForAttributeMapper extends BaAttributeMapper<BaAccountRelationshipModel, AccountRelationship>
{
	public AccRelationshipValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountRelationshipModel source, final AccountRelationship target,
			final MappingContext context)
	{
		if (source.getStartDateTime() == null || source.getEndDateTime() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDateTime());
		timePeriod.setEndDateTime(source.getEndDateTime());
		target.setValidFor(timePeriod);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AccountRelationship target, final BaAccountRelationshipModel source,
			final MappingContext context)
	{
		source.setStartDateTime(target.getValidFor().getStartDateTime());
		source.setEndDateTime(target.getValidFor().getEndDateTime());
	}
}
