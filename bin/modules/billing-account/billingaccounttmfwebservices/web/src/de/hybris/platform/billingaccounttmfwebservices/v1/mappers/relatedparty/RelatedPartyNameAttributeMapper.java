/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link BaPartyRoleModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartyNameAttributeMapper extends BaAttributeMapper<BaPartyRoleModel, RelatedParty>
{
	public RelatedPartyNameAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPartyRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getParty() != null && StringUtils.isNotEmpty(source.getParty().getName()))
		{
			target.setName(source.getParty().getName());
		}
	}
}
