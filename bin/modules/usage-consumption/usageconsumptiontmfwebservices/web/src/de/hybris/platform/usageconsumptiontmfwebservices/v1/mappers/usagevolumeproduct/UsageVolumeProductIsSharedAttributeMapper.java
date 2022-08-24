/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.usagevolumeproduct;

import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeProduct;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isShared attribute between {@link UcUsageVolumeProductModel} and
 * {@link UsageVolumeProduct}
 *
 * @since 2108
 */
public class UsageVolumeProductIsSharedAttributeMapper extends UcAttributeMapper<UcUsageVolumeProductModel, UsageVolumeProduct>
{
	public UsageVolumeProductIsSharedAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcUsageVolumeProductModel source, final UsageVolumeProduct target,
			final MappingContext context)
	{
		if (source.getUsageVolumeProductSpec() != null)
		{
			target.setIsShared(source.getUsageVolumeProductSpec().getIsShared());
		}
	}
}
