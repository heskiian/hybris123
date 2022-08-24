/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.characteristic;

import de.hybris.platform.partyroleservices.model.PrCharacteristicModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PrCharacteristicModel} and {@link Characteristic}
 *
 * @since 2108
 */
public class CharacteristicAtSchemaLocationAttributeMapper extends PrAttributeMapper<PrCharacteristicModel, Characteristic>
{
	public CharacteristicAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrCharacteristicModel source, final Characteristic target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(PartyroletmfwebservicesConstants.PARTY_ROLE_API_SCHEMA);
		}
	}
}
