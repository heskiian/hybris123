/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.qualifiedprocesstype;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for ProcessTypes attribute between {@link TmaProcessType} and
 * {@link QualifiedProcessType}
 *
 * @since 1907
 */
public class QualifiedProcessTypeProcessTypeAttributeMapper extends TmaAttributeMapper<TmaProcessType, QualifiedProcessType>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaProcessType source, QualifiedProcessType target, MappingContext context)
	{
		if (source != null)
		{
			ProcessType processType = getMapperFacade().map(source, ProcessType.class);
			target.setProcessType(processType);
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
