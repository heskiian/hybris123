/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.qualifiedprocesstype;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link PrincipalData} and
 * {@link QualifiedProcessType}
 *
 * @since 1907
 */
public class QualifiedProcessRelatedPartyRefAttributeMapper extends TmaAttributeMapper<PrincipalData, QualifiedProcessType>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, QualifiedProcessType target, MappingContext context)
	{
		if (source != null)
		{
			RelatedPartyRef relatedPartyRef = getMapperFacade().map(source, RelatedPartyRef.class);
			target.setRelatedParty(relatedPartyRef);
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
