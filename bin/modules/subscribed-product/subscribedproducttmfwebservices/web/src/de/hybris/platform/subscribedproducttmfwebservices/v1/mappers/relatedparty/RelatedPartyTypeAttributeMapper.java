/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.subscribedproductservices.enums.SpiPartyType;
import de.hybris.platform.subscribedproductservices.model.SpiPartyModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link SpiPartyModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartyTypeAttributeMapper extends SpiAttributeMapper<SpiPartyModel, RelatedParty>
{
	public RelatedPartyTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiPartyModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()) && source.getType() != null)
		{
			target.setAtreferredType(source.getType().getCode());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final RelatedParty relatedParty, final SpiPartyModel spiPartyModel,
			MappingContext context)
	{
		if (relatedParty.getAtreferredType() != null)
		{
			spiPartyModel.setType(SpiPartyType.valueOf(relatedParty.getAtreferredType()));
		}
	}
}
