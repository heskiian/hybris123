/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.consumptionsummary;

import de.hybris.platform.usageconsumptionservices.model.UcConsumptionSummaryModel;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between
 * {@link de.hybris.platform.usageconsumptionservices.model.UcConsumptionSummaryModel} and
 * {@link de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary}
 *
 * @since 2108
 */
public class ConsumptionSummarySchemaLocationAttributeMapper
		extends UcAttributeMapper<UcConsumptionSummaryModel, ConsumptionSummary>
{
	public ConsumptionSummarySchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcConsumptionSummaryModel source, final ConsumptionSummary target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(UsageconsumptiontmfwebservicesConstants.UC_API_SCHEMA);
		}
	}
}
