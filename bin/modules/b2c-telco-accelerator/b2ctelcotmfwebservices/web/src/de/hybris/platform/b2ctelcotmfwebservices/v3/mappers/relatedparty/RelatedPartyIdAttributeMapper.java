/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.relatedparty;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link TmaSubscriptionAccessData} and
 * {@link RelatedPartyRef}
 *
 * @since 2102
 */
public class RelatedPartyIdAttributeMapper extends TmaAttributeMapper<TmaSubscriptionAccessData, RelatedPartyRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionAccessData source, final RelatedPartyRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPrincipalUid()))
		{
			target.setId(source.getPrincipalUid());
		}
	}
}
