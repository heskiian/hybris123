/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.disability;

import de.hybris.platform.partyservices.model.PmDisabilityModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Disability;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 *
 * This attribute Mapper class maps data for type attribute between {@link PmDisabilityModel} and {@link Disability}
 *
 * @since 2108
 */
public class DisabilityAtTypeAttributeMapper extends PmAttributeMapper<PmDisabilityModel, Disability>
{
	public DisabilityAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmDisabilityModel source, final Disability target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
