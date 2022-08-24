/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.skill;


import de.hybris.platform.partyservices.model.PmSkillModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Skill;
import de.hybris.platform.partytmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link PmSkillModel} and {@link Skill}
 *
 * @since 2108
 */
public class SkillValidForAttributeMapper extends PmAttributeMapper<PmSkillModel, Skill>
{
	public SkillValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmSkillModel source, final Skill target, final MappingContext context)
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
