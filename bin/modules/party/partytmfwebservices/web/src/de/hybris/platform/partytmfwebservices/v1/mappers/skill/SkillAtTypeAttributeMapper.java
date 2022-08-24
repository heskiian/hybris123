/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.skill;

import de.hybris.platform.partyservices.model.PmSkillModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Skill;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmSkillModel} and {@link Skill}
 *
 * @since 2108
 */
public class SkillAtTypeAttributeMapper extends PmAttributeMapper<PmSkillModel, Skill>
{
	public SkillAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmSkillModel source, final Skill target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
