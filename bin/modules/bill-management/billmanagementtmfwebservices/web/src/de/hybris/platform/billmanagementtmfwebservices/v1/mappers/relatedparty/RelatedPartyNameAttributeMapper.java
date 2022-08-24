/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.billmanagementservices.model.BmPartyRoleModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.RelatedPartyRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link BmPartyRoleModel} and {@link RelatedPartyRef}
 *
 * @since 2108
 */
public class RelatedPartyNameAttributeMapper extends BmAttributeMapper<BmPartyRoleModel, RelatedPartyRef>
{
	public RelatedPartyNameAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyRoleModel source, final RelatedPartyRef target,
			final MappingContext context)
	{
		if (source.getParty() != null && StringUtils.isNotEmpty(source.getParty().getName()))
		{
			target.setName(source.getParty().getName());
		}
	}
}
