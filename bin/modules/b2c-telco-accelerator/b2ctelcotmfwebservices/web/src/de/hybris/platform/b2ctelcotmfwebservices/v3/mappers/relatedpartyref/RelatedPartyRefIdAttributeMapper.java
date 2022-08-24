/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.relatedpartyref;

import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps id for related party attribute between {@link TmaUserData} and {@link RelatedPartyRef}
 *
 * @since 2007
 */
public class RelatedPartyRefIdAttributeMapper extends TmaAttributeMapper<TmaUserData, RelatedPartyRef>
{
	@Override public void populateTargetAttributeFromSource(final TmaUserData source,final RelatedPartyRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getUid()))
		{
			target.setId(source.getUid());
		}
	}
}
