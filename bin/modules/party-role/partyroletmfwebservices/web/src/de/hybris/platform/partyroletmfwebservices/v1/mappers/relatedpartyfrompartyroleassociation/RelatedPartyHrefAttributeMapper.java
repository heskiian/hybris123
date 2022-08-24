/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.relatedpartyfrompartyroleassociation;

import de.hybris.platform.partyroleservices.model.PrPartyModel;
import de.hybris.platform.partyroleservices.model.PrPartyRoleAssociationModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link PrPartyRoleAssociationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyHrefAttributeMapper extends PrAttributeMapper<PrPartyRoleAssociationModel, RelatedParty>
{
	private MapperFacade mapperFacade;

	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleAssociationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final PrPartyModel party = getParty(source);

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
	 * @return The {@link PrPartyModel}
	 */
	protected PrPartyModel getParty(final PrPartyRoleAssociationModel partyRoleAssociation)
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
