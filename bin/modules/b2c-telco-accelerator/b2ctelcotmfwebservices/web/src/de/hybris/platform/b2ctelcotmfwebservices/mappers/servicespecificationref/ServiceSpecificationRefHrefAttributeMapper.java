/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.servicespecificationref;

import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ServiceSpecificationRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaServiceSpecificationData} and
 * {@link ServiceSpecificationRef}
 *
 * @since 2102
 */
public class ServiceSpecificationRefHrefAttributeMapper extends TmaAttributeMapper<TmaServiceSpecificationData,
		ServiceSpecificationRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaServiceSpecificationData source, final ServiceSpecificationRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.SERVICE_SPECIFICATION_API_URL + source.getId());
		}
	}
}
