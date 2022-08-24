/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfrompartyroleassociation;

import de.hybris.platform.partyservices.model.PmPartyRoleAssociationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link PmPartyRoleAssociationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyIdAttributeMapper extends PmAttributeMapper<PmPartyRoleAssociationModel, RelatedParty>
{
	public RelatedPartyIdAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyRoleAssociationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getPartyRoleInvolvedWith() == null && source.getPartyRoleInvolves() == null)
		{
			return;
		}

		if (source.getPartyRoleInvolvedWith() != null && source.getPartyRoleInvolvedWith().getParty() != null) {
			target.setId(source.getPartyRoleInvolvedWith().getParty().getId());
			return;
		}

		target.setId(source.getPartyRoleInvolves().getParty().getId());
	}
}
