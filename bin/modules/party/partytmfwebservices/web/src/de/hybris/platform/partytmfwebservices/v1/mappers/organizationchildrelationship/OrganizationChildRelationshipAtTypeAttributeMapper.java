/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationchildrelationship;

import de.hybris.platform.partyservices.model.PmOrganizationRelationshipModel;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationChildRelationship;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmOrganizationRelationshipModel} and {@link OrganizationChildRelationship}
 *
 * @since 2108
 */
public class OrganizationChildRelationshipAtTypeAttributeMapper
		extends PmAttributeMapper<PmOrganizationRelationshipModel, OrganizationChildRelationship>
{
	public OrganizationChildRelationshipAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationRelationshipModel source,
			final OrganizationChildRelationship target, final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
