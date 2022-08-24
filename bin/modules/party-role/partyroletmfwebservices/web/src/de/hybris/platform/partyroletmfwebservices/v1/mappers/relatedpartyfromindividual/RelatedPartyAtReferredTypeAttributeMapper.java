/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroletmfwebservices.v1.mappers.relatedpartyfromindividual;

import de.hybris.platform.partyroleservices.model.PrIndividualModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PrIndividualModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyAtReferredTypeAttributeMapper extends PrAttributeMapper<PrIndividualModel, RelatedParty>
{
	public RelatedPartyAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrIndividualModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtreferredType(PartyroletmfwebservicesConstants.INDIVIDUAL_DEFAULT_REFERRED_TYPE);
	}
}
