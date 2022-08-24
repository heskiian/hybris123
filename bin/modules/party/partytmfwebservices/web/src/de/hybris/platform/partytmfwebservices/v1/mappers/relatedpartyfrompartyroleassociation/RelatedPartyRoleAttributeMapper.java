/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfrompartyroleassociation;

import de.hybris.platform.partyservices.model.PmPartyRoleAssociationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for role attribute between {@link PmPartyRoleAssociationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyRoleAttributeMapper extends PmAttributeMapper<PmPartyRoleAssociationModel, RelatedParty>
{
	public RelatedPartyRoleAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyRoleAssociationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getAssociationType()))
		{
			return;
		}

		target.setRole(source.getAssociationType());
	}
}
