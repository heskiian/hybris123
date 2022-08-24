/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.consumptionsummary;

import de.hybris.platform.usageconsumptionservices.model.UcConsumptionSummaryModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for user attribute between {@link UcConsumptionSummaryModel} and
 * {@link ConsumptionSummary}
 *
 * @since 2108
 */
public class ConsumptionSummaryUserAttributeMapper extends UcAttributeMapper<UcConsumptionSummaryModel, ConsumptionSummary>
{
	private MapperFacade mapperFacade;

	public ConsumptionSummaryUserAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final UcConsumptionSummaryModel source, final ConsumptionSummary target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPartyRoles()))
		{
			return;
	}

		final RelatedParty relatedParty = getMapperFacade().map(source.getPartyRoles().iterator().next().getParty(),
				RelatedParty.class, context);
		relatedParty.setRole(source.getPartyRoles().iterator().next().getRole());
		target.setUser(relatedParty);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
