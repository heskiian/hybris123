/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.partyrole;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link PrPartyRoleModel} and {@link PartyRole}
 *
 * @since 2108
 */
public class PartyRoleValidForAttributeMapper extends PrAttributeMapper<PrPartyRoleModel, PartyRole>
{
	public PartyRoleValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleModel source, final PartyRole target,
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
