/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.partycreditprofile;

import de.hybris.platform.partyservices.model.PmPartyCreditProfileModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.PartyCreditProfile;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PmPartyCreditProfileModel} and {@link PartyCreditProfile}
 *
 * @since 2108
 */
public class PartyCreditProfileAtSchemaLocationAttributeMapper
		extends PmAttributeMapper<PmPartyCreditProfileModel, PartyCreditProfile>
{
	public PartyCreditProfileAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyCreditProfileModel source, final PartyCreditProfile target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtschemaLocation(PartytmfwebservicesConstants.PARTY_API_SCHEMA);
	}
}
