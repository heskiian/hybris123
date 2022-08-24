/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.individual;

import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Disability;
import de.hybris.platform.partytmfwebservices.v1.dto.Individual;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for disability attribute between {@link PmIndividualModel} and {@link Individual}
 *
 * @since 2108
 */
public class IndividualDisabilityAttributeMapper extends PmAttributeMapper<PmIndividualModel, Individual>
{
	private final MapperFacade mapperFacade;

	public IndividualDisabilityAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmIndividualModel source, final Individual target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getDisabilities()))
		{
			return;
		}

		target.setDisability(getMapperFacade().mapAsList(source.getDisabilities(), Disability.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
