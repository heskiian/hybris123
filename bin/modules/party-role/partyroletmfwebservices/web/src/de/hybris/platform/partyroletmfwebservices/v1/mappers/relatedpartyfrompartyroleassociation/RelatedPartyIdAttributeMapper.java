/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.relatedpartyfrompartyroleassociation;

import de.hybris.platform.partyroleservices.model.PrPartyRoleAssociationModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link PrPartyRoleAssociationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyIdAttributeMapper extends PrAttributeMapper<PrPartyRoleAssociationModel, RelatedParty>
{
	public RelatedPartyIdAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleAssociationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getPartyRoleInvolvedWith() == null && source.getPartyRoleInvolves() == null)
		{
			return;
		}

		if (source.getPartyRoleInvolvedWith() != null && source.getPartyRoleInvolvedWith().getParty() != null)
		{
			target.setId(source.getPartyRoleInvolvedWith().getParty().getId());
			return;
		}

		target.setId(source.getPartyRoleInvolves().getParty().getId());
	}
}
