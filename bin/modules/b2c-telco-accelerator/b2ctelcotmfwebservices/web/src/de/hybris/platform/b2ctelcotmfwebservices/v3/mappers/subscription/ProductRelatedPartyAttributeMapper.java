/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParty attribute for Subscription between
 * {@link TmaSubscriptionBaseData} and {@link Product}
 *
 * @since 2102
 */
public class ProductRelatedPartyAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductRelatedPartyAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionBaseData source, final Product target,
			final MappingContext context)
	{

		if (CollectionUtils.isEmpty(source.getSubscriptionAccesses()))
		{
			return;
		}

		final List<RelatedPartyRef> relatedParties = new ArrayList<>();
		source.getSubscriptionAccesses().forEach(access -> {
			final RelatedPartyRef party = getMapperFacade().map(access, RelatedPartyRef.class, context);
			relatedParties.add(party);

		});

		target.setRelatedParty(relatedParties);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
