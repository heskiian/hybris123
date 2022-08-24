/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.processtype;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaProcessType} and
 * {@link ProcessType}
 *
 * @since 1907
 */
public class ProcessTypeAtTypeAttributeMapper extends TmaAttributeMapper<TmaProcessType, ProcessType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaProcessType source, ProcessType target,
			MappingContext context)
	{
		target.setAttype(target.getClass().getSimpleName());
	}
}
