/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.languageability;

import de.hybris.platform.partyservices.model.PmLanguageAbilityModel;
import de.hybris.platform.partytmfwebservices.v1.dto.LanguageAbility;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmLanguageAbilityModel} and {@link LanguageAbility}
 *
 * @since 2108
 */
public class LanguageAbilityAtTypeAttributeMapper extends PmAttributeMapper<PmLanguageAbilityModel, LanguageAbility>
{

	public LanguageAbilityAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmLanguageAbilityModel source, final LanguageAbility target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
