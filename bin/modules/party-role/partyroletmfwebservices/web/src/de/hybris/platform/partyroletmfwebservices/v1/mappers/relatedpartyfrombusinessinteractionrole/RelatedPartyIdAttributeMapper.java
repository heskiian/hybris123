/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.relatedpartyfrombusinessinteractionrole;

import de.hybris.platform.partyroleservices.model.PrBusinessInteractionRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link PrBusinessInteractionRoleModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyIdAttributeMapper extends PrAttributeMapper<PrBusinessInteractionRoleModel, RelatedParty>
{
	public RelatedPartyIdAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrBusinessInteractionRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getPartyRole() == null || source.getPartyRole().getParty() == null)
		{
			return;
		}

		target.setId(source.getPartyRole().getParty().getId());
	}
}
