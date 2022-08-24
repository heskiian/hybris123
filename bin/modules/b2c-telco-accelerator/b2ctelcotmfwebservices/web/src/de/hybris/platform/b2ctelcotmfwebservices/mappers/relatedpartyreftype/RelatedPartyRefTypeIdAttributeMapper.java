/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.relatedpartyreftype;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRefType;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link PrincipalData} and {@link RelatedPartyRefType}
 *
 * @since 1907
 */
public class RelatedPartyRefTypeIdAttributeMapper extends TmaAttributeMapper<PrincipalData, RelatedPartyRefType>
{
	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, RelatedPartyRefType target, MappingContext context)
	{
		if (source.getUid() != null)
		{
			target.setId(source.getUid());
		}
	}
}
