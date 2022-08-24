/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.characteristic;

import de.hybris.platform.partyservices.model.PmCharacteristicModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmCharacteristicModel} and {@link Characteristic}
 *
 * @since 2108
 */
public class CharacteristicAtTypeAttributeMapper extends PmAttributeMapper<PmCharacteristicModel, Characteristic>
{
	public CharacteristicAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmCharacteristicModel source, final Characteristic target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
