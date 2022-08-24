/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfromindividual;

import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for href attribute between {@link PmIndividualModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyHrefAttributeMapper extends PmAttributeMapper<PmIndividualModel, RelatedParty>
{
	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmIndividualModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setHref(PartytmfwebservicesConstants.INDIVIDUAL_API_URL + source.getId());
	}
}
