/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.individual;


import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Individual;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for title attribute between {@link PmIndividualModel} and {@link Individual}
 *
 * @since 2108
 */
public class IndividualTitleAttributeMapper extends PmAttributeMapper<PmIndividualModel, Individual>
{
	public IndividualTitleAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);

	}

	@Override
	public void populateTargetAttributeFromSource(final PmIndividualModel source, final Individual target,
			final MappingContext context)
	{
		if (source.getTitle() != null)
		{
			target.setTitle(source.getTitle().getCode());
		}

	}
}
