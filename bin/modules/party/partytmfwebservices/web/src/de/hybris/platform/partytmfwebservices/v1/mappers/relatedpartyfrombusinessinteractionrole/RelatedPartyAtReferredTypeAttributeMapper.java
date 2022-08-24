/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfrombusinessinteractionrole;

import de.hybris.platform.partyservices.model.PmBusinessInteractionRoleModel;
import de.hybris.platform.partyservices.model.PmPartyModel;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PmBusinessInteractionRoleModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyAtReferredTypeAttributeMapper extends PmAttributeMapper<PmBusinessInteractionRoleModel, RelatedParty>
{
	private MapperFacade mapperFacade;

	public RelatedPartyAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmBusinessInteractionRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final PmPartyModel party = getParty(source);

		if (party != null)
		{
			target.setAtreferredType(getMapperFacade().map(party, RelatedParty.class).getAtreferredType());
		}
	}

	/**
	 * Returns the party from the business interaction role.
	 *
	 * @param businessInteractionRole
	 * 		The business interaction role
	 * @return The {@link PmPartyModel}
	 */
	protected PmPartyModel getParty(final PmBusinessInteractionRoleModel businessInteractionRole)
	{
		if (businessInteractionRole.getPartyRole() == null)
		{
			return null;
		}

		return businessInteractionRole.getPartyRole().getParty();
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
