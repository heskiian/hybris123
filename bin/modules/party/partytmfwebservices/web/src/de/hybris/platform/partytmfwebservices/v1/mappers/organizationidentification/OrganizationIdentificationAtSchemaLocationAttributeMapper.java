/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationidentification;

import de.hybris.platform.partyservices.model.PmIdentificationModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationIdentification;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PmIdentificationModel} and {@link OrganizationIdentification}
 *
 * @since 2108
 */
public class OrganizationIdentificationAtSchemaLocationAttributeMapper
		extends PmAttributeMapper<PmIdentificationModel, OrganizationIdentification>
{
	public OrganizationIdentificationAtSchemaLocationAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmIdentificationModel source, final OrganizationIdentification target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getIdentificationId()))
		{
			return;
		}

		target.setAtschemaLocation(PartytmfwebservicesConstants.PARTY_API_SCHEMA);
	}
}
