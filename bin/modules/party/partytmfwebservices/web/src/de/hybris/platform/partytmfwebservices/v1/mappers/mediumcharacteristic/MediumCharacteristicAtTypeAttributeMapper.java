/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.mediumcharacteristic;

import de.hybris.platform.partyservices.model.PmMediumCharacteristicModel;
import de.hybris.platform.partytmfwebservices.v1.dto.MediumCharacteristic;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmMediumCharacteristicModel} and {@link MediumCharacteristic}
 *
 * @since 2108
 */
public class MediumCharacteristicAtTypeAttributeMapper
		extends PmAttributeMapper<PmMediumCharacteristicModel, MediumCharacteristic>
{
	public MediumCharacteristicAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmMediumCharacteristicModel source, final MediumCharacteristic target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
