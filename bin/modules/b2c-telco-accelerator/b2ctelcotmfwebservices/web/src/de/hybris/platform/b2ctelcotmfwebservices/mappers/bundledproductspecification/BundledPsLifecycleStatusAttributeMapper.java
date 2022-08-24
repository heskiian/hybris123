/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.bundledproductspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BundledProductSpecification;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for lifecycleStatus attribute between {@link TmaProductSpecificationData} and
 * {@link BundledProductSpecification}
 *
 * @since 2102
 */
public class BundledPsLifecycleStatusAttributeMapper extends TmaAttributeMapper<TmaProductSpecificationData,
		BundledProductSpecification>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source,
			final BundledProductSpecification target, final MappingContext context)
	{
		if (source.getLifecycleStatus() != null)
		{
			target.setLifecycleStatus(source.getLifecycleStatus().getCode());
		}
	}
}
