/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.partyrole;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.partyroletmfwebservices.v1.dto.CreditProfile;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for credit profile attribute between {@link PrPartyRoleModel} and {@link PartyRole}
 *
 * @since 2108
 */
public class PartyRoleCreditProfileAttributeMapper extends PrAttributeMapper<PrPartyRoleModel, PartyRole>
{
	private MapperFacade mapperFacade;

	public PartyRoleCreditProfileAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleModel source, final PartyRole target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getCreditProfiles()))
		{
			return;
		}

		target.setCreditProfile(getMapperFacade().mapAsList(source.getCreditProfiles(), CreditProfile.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
