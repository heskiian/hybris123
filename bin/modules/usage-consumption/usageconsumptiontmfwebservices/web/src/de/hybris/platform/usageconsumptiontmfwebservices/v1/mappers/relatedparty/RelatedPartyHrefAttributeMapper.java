/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.usageconsumptionservices.enums.UcPartyType;
import de.hybris.platform.usageconsumptionservices.model.UcPartyRoleModel;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for href attribute between {@link UcPartyRoleModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartyHrefAttributeMapper extends UcAttributeMapper<UcPartyRoleModel, RelatedParty>
{
	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcPartyRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getParty().getType() != null && StringUtils.equals(source.getParty().getType().getCode(), UcPartyType.INDIVIDUAL.getCode()))
		{
			target.setHref(UsageconsumptiontmfwebservicesConstants.INDIVIDUAL_RELATED_PARTY_API_URL + source.getId());
		}
		else if (source.getParty().getType() != null && StringUtils.equals(source.getParty().getType().getCode(), UcPartyType.ORGANIZATION.getCode()))
		{
			target.setHref(UsageconsumptiontmfwebservicesConstants.ORGANIZATION_RELATED_PARTY_API_URL + source.getId());
		}
	}
}
