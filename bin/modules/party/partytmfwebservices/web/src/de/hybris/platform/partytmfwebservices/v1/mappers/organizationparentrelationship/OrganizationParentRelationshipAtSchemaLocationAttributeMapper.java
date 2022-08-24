/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationparentrelationship;

import de.hybris.platform.partyservices.model.PmOrganizationRelationshipModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationParentRelationship;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PmOrganizationRelationshipModel} and {@link OrganizationParentRelationship}
 *
 * @since 2108
 */
public class OrganizationParentRelationshipAtSchemaLocationAttributeMapper
		extends PmAttributeMapper<PmOrganizationRelationshipModel, OrganizationParentRelationship>
{
	public OrganizationParentRelationshipAtSchemaLocationAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationRelationshipModel source,
			final OrganizationParentRelationship target, final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtschemaLocation(PartytmfwebservicesConstants.PARTY_API_SCHEMA);
	}
}
