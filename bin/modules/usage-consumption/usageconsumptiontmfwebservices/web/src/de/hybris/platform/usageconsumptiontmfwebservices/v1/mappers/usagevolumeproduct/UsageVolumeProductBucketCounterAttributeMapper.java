/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.usagevolumeproduct;

import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeProduct;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bucketCounter attribute between {@link UcUsageVolumeProductModel} and
 * {@link UsageVolumeProduct}
 *
 * @since 2108
 */
public class UsageVolumeProductBucketCounterAttributeMapper
		extends UcAttributeMapper<UcUsageVolumeProductModel, UsageVolumeProduct>
{
	private MapperFacade mapperFacade;

	public UsageVolumeProductBucketCounterAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final UcUsageVolumeProductModel source, final UsageVolumeProduct target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getConsumptionSummaries()))
		{
			return;
		}

		final List<ConsumptionSummary> consumptionSummaries = new ArrayList<>();
		source.getConsumptionSummaries().forEach(ucConsumptionSummaryModel -> {
			final ConsumptionSummary consumptionSummary = getMapperFacade()
					.map(ucConsumptionSummaryModel, ConsumptionSummary.class, context);
			consumptionSummaries.add(consumptionSummary);
		});

		target.setBucketCounter(consumptionSummaries);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
