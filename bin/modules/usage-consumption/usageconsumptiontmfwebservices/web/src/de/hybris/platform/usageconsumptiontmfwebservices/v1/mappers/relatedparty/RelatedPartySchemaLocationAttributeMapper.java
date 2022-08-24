/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.usageconsumptionservices.model.UcPartyRoleModel;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link UcPartyRoleModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartySchemaLocationAttributeMapper extends UcAttributeMapper<UcPartyRoleModel, RelatedParty>
{
	public RelatedPartySchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcPartyRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(UsageconsumptiontmfwebservicesConstants.UC_API_SCHEMA);
		}
	}
}
