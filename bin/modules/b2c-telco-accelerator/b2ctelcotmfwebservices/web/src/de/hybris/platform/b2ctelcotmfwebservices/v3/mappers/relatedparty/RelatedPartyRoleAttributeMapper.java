/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.relatedparty;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps role attribute between {@link TmaSubscriptionAccessData} and {@link RelatedPartyRef}
 *
 * @since 2102
 */
public class RelatedPartyRoleAttributeMapper extends TmaAttributeMapper<TmaSubscriptionAccessData, RelatedPartyRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionAccessData source, final RelatedPartyRef target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getAccessType()))
		{
			target.setRole(source.getAccessType().toString());
		}
	}
}
