/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.networkproduct;

import de.hybris.platform.usageconsumptionservices.model.UcConsumptionSummaryModel;
import de.hybris.platform.usageconsumptionservices.model.UcNetworkProductModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.NetworkProduct;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for user attribute between {@link UcConsumptionSummaryModel} and
 * {@link ConsumptionSummary}
 *
 * @since 2108
 */
public class NetworkProductUserAttributeMapper extends UcAttributeMapper<UcNetworkProductModel, NetworkProduct>
{
	private MapperFacade mapperFacade;

	public NetworkProductUserAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final UcNetworkProductModel source,
			final NetworkProduct target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getUsedBy()))
		{
			return;
		}

		final List<RelatedParty> relatedParties = new ArrayList<>();
		source.getUsedBy().forEach(ucPartyRoleModel -> {
			final RelatedParty relatedParty = getMapperFacade().map(ucPartyRoleModel, RelatedParty.class, context);
			relatedParties.add(relatedParty);
		});

		target.setUser(relatedParties);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
