/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.qualifiedprocesstype;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for id attribute between {@link TmaProcessType} and
 * {@link QualifiedProcessType}
 *
 * @since 1907
 */
public class QualifiedProcessTypeIdAttributeMapper  extends TmaAttributeMapper<TmaProcessType, QualifiedProcessType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaProcessType source, QualifiedProcessType target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}
