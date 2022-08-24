/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.othernameindividual;

import de.hybris.platform.partyservices.model.PmOtherNameModel;
import de.hybris.platform.partytmfwebservices.v1.dto.OtherNameIndividual;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for title attribute between {@link PmOtherNameModel} and
 * {@link OtherNameIndividual}
 *
 * @since 2108
 */
public class OtherNameIndividualTitleAttributeMapper extends PmAttributeMapper<PmOtherNameModel, OtherNameIndividual>
{
	public OtherNameIndividualTitleAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOtherNameModel source, final OtherNameIndividual target,
			final MappingContext context)
	{
		if (source.getTitle() != null)
		{
			target.setTitle(source.getTitle().getCode());
		}

	}
}
