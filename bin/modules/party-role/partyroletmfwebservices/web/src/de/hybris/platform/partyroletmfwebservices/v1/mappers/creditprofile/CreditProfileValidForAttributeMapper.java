/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.creditprofile;

import de.hybris.platform.partyroleservices.model.PrCreditProfileModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.CreditProfile;
import de.hybris.platform.partyroletmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link PrCreditProfileModel} and {@link CreditProfile}
 *
 * @since 2108
 */
public class CreditProfileValidForAttributeMapper extends PrAttributeMapper<PrCreditProfileModel, CreditProfile>
{
	public CreditProfileValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrCreditProfileModel source, final CreditProfile target,
			final MappingContext context)
	{
		if (source.getStartDate() == null && source.getEndDate() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDate());
		timePeriod.setEndDateTime(source.getEndDate());

		target.setValidFor(timePeriod);
	}
}
