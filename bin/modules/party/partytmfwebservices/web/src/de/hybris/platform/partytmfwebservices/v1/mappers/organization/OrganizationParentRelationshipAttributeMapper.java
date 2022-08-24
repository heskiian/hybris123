/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.organization;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Organization;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationParentRelationship;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for organization parent relationship attribute between {@link PmOrganizationModel} and {@link Organization}
 *
 * @since 2108
 */
public class OrganizationParentRelationshipAttributeMapper extends PmAttributeMapper<PmOrganizationModel, Organization>
{
	private MapperFacade mapperFacade;

	public OrganizationParentRelationshipAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationModel source, final Organization target,
			final MappingContext context)
	{
		if (source.getOrganizationParent() == null)
		{
			return;
		}

		target.setOrganizationParentRelationship(
				getMapperFacade().map(source.getOrganizationParent(), OrganizationParentRelationship.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
