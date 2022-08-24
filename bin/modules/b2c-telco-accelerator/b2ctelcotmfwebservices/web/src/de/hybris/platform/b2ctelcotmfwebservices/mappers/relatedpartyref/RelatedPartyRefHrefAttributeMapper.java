/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.relatedpartyref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps href for related party attribute between {@link PrincipalData} and {@link RelatedPartyRef}
 *
 * @since 1907
 */
public class RelatedPartyRefHrefAttributeMapper extends TmaAttributeMapper<PrincipalData, RelatedPartyRef>
{
	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, RelatedPartyRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getUid()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.RELATEDPARTYREF_API_URL + source.getUid());
		}
	}
}
