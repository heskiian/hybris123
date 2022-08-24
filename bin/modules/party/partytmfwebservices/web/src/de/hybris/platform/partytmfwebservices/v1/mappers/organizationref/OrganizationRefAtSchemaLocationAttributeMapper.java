/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationref;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationRef;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PmOrganizationModel} and {@link OrganizationRef}
 *
 * @since 2108
 */
public class OrganizationRefAtSchemaLocationAttributeMapper extends PmAttributeMapper<PmOrganizationModel, OrganizationRef>
{
	public OrganizationRefAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationModel source, final OrganizationRef target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtschemaLocation(PartytmfwebservicesConstants.PARTY_API_SCHEMA);
	}
}
