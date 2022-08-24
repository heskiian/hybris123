/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationparentrelationship;

import de.hybris.platform.partyservices.model.PmOrganizationRelationshipModel;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationParentRelationship;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationRef;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for organization attribute between {@link PmOrganizationRelationshipModel} and {@link OrganizationParentRelationship}
 *
 * @since 2108
 */
public class OrganizationParentRelationshipOrganizationAttributeMapper
		extends PmAttributeMapper<PmOrganizationRelationshipModel, OrganizationParentRelationship>
{
	private MapperFacade mapperFacade;

	public OrganizationParentRelationshipOrganizationAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName, final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationRelationshipModel source,
			final OrganizationParentRelationship target, final MappingContext context)
	{
		if (source.getParentOrganization() == null)
		{
			return;
		}

		target.setOrganization(getMapperFacade().map(source.getParentOrganization(), OrganizationRef.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
