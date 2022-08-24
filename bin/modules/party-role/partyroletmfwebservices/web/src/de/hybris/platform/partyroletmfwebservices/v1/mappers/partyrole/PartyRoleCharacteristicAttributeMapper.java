/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.partyrole;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.AccountRef;
import de.hybris.platform.partyroletmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for characteristic attribute between {@link PrPartyRoleModel} and {@link PartyRole}
 *
 * @since 2108
 */
public class PartyRoleCharacteristicAttributeMapper extends PrAttributeMapper<PrPartyRoleModel, PartyRole>
{
	private MapperFacade mapperFacade;

	public PartyRoleCharacteristicAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleModel source, final PartyRole target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getCharacteristics()))
		{
			return;
		}

		target.setCharacteristic(getMapperFacade().mapAsList(source.getCharacteristics(), Characteristic.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
