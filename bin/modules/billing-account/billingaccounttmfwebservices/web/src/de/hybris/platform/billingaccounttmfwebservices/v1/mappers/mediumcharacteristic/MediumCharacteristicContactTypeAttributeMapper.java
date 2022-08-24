/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.mediumcharacteristic;

import de.hybris.platform.billingaccountservices.enums.BaMediumCharacContactType;
import de.hybris.platform.billingaccountservices.model.BaMediumCharacteristicModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.MediumCharacteristic;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for contactType attribute between {@link BaMediumCharacteristicModel} and {@link MediumCharacteristic}
 *
 * @since 2111
 */
public class MediumCharacteristicContactTypeAttributeMapper
		extends BaAttributeMapper<BaMediumCharacteristicModel, MediumCharacteristic>
{
	public MediumCharacteristicContactTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaMediumCharacteristicModel source, final MediumCharacteristic target,
			final MappingContext context)
	{
		// no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final MediumCharacteristic target, final BaMediumCharacteristicModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getContactType()))
		{
			source.setContactType(BaMediumCharacContactType.valueOf(target.getContactType().toUpperCase()));
		}
	}

}
