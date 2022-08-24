/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.characteristic;

import de.hybris.platform.subscribedproductservices.model.SpiCharacteristicModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link SpiCharacteristicModel} and {@link Characteristic}
 *
 * @since 2105
 */
public class CharacteristicTypeAttributeMapper extends SpiAttributeMapper<SpiCharacteristicModel, Characteristic>
{
	public CharacteristicTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiCharacteristicModel source, final Characteristic target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(SubscribedproducttmfwebservicesConstants.SPI_CHARACTERISTIC_TYPE);
		}
	}
}
