/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroletmfwebservices.v1.mappers.relatedpartyfrombusinessinteractionrole;

import de.hybris.platform.partyroleservices.model.PrBusinessInteractionRoleModel;
import de.hybris.platform.partyroleservices.model.PrPartyModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PrBusinessInteractionRoleModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyAtReferredTypeAttributeMapper extends PrAttributeMapper<PrBusinessInteractionRoleModel, RelatedParty>
{
	private final MapperFacade mapperFacade;

	public RelatedPartyAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrBusinessInteractionRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final PrPartyModel party = getParty(source);

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
	 * @return The {@link PrPartyModel}
	 */
	protected PrPartyModel getParty(final PrBusinessInteractionRoleModel businessInteractionRole)
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
