/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.v1.mappers.organization;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Organization;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationChildRelationship;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

;


/**
 * This attribute Mapper class maps data for organization child relationship attribute between {@link PmOrganizationModel} and {@link Organization}
 *
 * @since 2108
 */
public class OrganizationChildRelationshipAttributeMapper extends PmAttributeMapper<PmOrganizationModel, Organization>
{
	private MapperFacade mapperFacade;

	public OrganizationChildRelationshipAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationModel source, final Organization target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getChildOrganizations()))
		{
			return;
		}

		target.setOrganizationChildRelationship(
				getMapperFacade().mapAsList(source.getChildOrganizations(), OrganizationChildRelationship.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
