/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfrombusinessinteractionrole;

import de.hybris.platform.partyservices.model.PmBusinessInteractionRoleModel;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link PmBusinessInteractionRoleModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyIdAttributeMapper extends PmAttributeMapper<PmBusinessInteractionRoleModel, RelatedParty>
{
	public RelatedPartyIdAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmBusinessInteractionRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getPartyRole() == null || source.getPartyRole().getParty() == null)
		{
			return;
		}

		target.setId(source.getPartyRole().getParty().getId());
	}
}
