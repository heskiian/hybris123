/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.agreementservices.enums.AgrPartyType;
import de.hybris.platform.agreementservices.model.AgrPartyModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link AgrPartyModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyHrefAttributeMapper extends AgrAttributeMapper<AgrPartyModel, RelatedParty>
{
	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrPartyModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getType() != null && StringUtils.equals(source.getType().getCode(), AgrPartyType.INDIVIDUAL.getCode()))
		{
			target.setHref(AgreementtmfwebservicesConstants.RELATED_PARTY_API_URL_INDIVIDUAL + source.getId());
		}
		else if (source.getType() != null && StringUtils.equals(source.getType().getCode(), AgrPartyType.ORGANIZATION.getCode()))
		{
			target.setHref(AgreementtmfwebservicesConstants.RELATED_PARTY_API_URL_ORGANIZATION + source.getId());
		}
	}
}
