/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.usageconsumptionservices.model.UcPartyModel;
import de.hybris.platform.usageconsumptionservices.model.UcPartyRoleModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link UcPartyModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyReferredTypeAttributeMapper extends UcAttributeMapper<UcPartyRoleModel, RelatedParty>
{
	public RelatedPartyReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcPartyRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getParty() == null || StringUtils.isEmpty(source.getParty().getId()) || source.getParty().getType() == null)
		{
			return;
		}
		target.setAtreferredType(source.getParty().getType().getCode());
	}
}