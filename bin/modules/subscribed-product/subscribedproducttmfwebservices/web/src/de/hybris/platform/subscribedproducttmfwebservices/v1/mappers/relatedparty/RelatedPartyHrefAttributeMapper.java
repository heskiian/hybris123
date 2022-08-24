/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.subscribedproductservices.enums.SpiPartyType;
import de.hybris.platform.subscribedproductservices.model.SpiPartyModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link SpiPartyModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartyHrefAttributeMapper extends SpiAttributeMapper<SpiPartyModel, RelatedParty>
{
	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiPartyModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getType() != null && StringUtils.equals(source.getType().getCode(), SpiPartyType.INDIVIDUAL.getCode()))
		{
			target.setHref(SubscribedproducttmfwebservicesConstants.RELATED_PARTY_API_URL_INDIVIDUAL + source.getId());
		}
		else if (source.getType() != null && StringUtils.equals(source.getType().getCode(), SpiPartyType.ORGANIZATION.getCode()))
		{
			target.setHref(SubscribedproducttmfwebservicesConstants.RELATED_PARTY_API_URL_ORGANIZATION + source.getId());
		}
	}
}
