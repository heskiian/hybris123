/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.othernameindividual;

import de.hybris.platform.partyservices.model.PmOtherNameModel;
import de.hybris.platform.partytmfwebservices.v1.dto.OtherNameIndividual;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmOtherNameModel} and
 * {@link OtherNameIndividual}
 *
 * @since 2108
 */
public class OtherNameIndividualAtTypeAttributeMapper extends PmAttributeMapper<PmOtherNameModel, OtherNameIndividual>
{
	public OtherNameIndividualAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOtherNameModel source, final OtherNameIndividual target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
