/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.agreementservices.model.AgrPartyModel;
import de.hybris.platform.agreementtmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link AgrPartyModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyReferredTypeAttributeMapper extends AgrAttributeMapper<AgrPartyModel, RelatedParty>
{
	public RelatedPartyReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrPartyModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()) && source.getType() != null)
		{
			target.setAtreferredType(source.getType().getCode());
		}
	}
}
