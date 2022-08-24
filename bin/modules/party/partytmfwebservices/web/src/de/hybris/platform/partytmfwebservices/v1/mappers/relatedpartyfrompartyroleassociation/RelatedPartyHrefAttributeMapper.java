/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfrompartyroleassociation;

import de.hybris.platform.partyservices.model.PmPartyModel;
import de.hybris.platform.partyservices.model.PmPartyRoleAssociationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link PmPartyRoleAssociationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyHrefAttributeMapper extends PmAttributeMapper<PmPartyRoleAssociationModel, RelatedParty>
{
	private MapperFacade mapperFacade;

	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyRoleAssociationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final PmPartyModel party = getParty(source);

		if (party != null)
		{
			target.setHref(getMapperFacade().map(party, RelatedParty.class).getHref());
		}
	}

	/**
	 * Returns the party from the party role association.
	 *
	 * @param partyRoleAssociation
	 * 		The party role association
	 * @return The {@link PmPartyModel}
	 */
	protected PmPartyModel getParty(final PmPartyRoleAssociationModel partyRoleAssociation)
	{
		if (partyRoleAssociation.getPartyRoleInvolvedWith() != null)
		{
			return partyRoleAssociation.getPartyRoleInvolvedWith().getParty();
		}

		return partyRoleAssociation.getPartyRoleInvolves().getParty();
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
