/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.mediumcharacteristic;

import de.hybris.platform.partyroleservices.model.PrMediumCharacteristicModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.MediumCharacteristic;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PrMediumCharacteristicModel} and {@link MediumCharacteristic}
 *
 * @since 2108
 */
public class MediumCharacteristicAtSchemaLocationAttributeMapper
		extends PrAttributeMapper<PrMediumCharacteristicModel, MediumCharacteristic>
{
	public MediumCharacteristicAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrMediumCharacteristicModel source, final MediumCharacteristic target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(PartyroletmfwebservicesConstants.PARTY_ROLE_API_SCHEMA);
		}
	}
}
