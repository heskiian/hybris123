/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription.base;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseDetailRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.mappers.TmaTmfAbstractRelatedPartyDataMapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscriptionAccessData} and
 * {@link TmaSubscriptionBaseDetailRefWsDto}
 *
 * @since 1810
 */
public class TmaSubscriptionBaseDetailRefWsDtoFromAccessDataMapper
		extends TmaTmfAbstractRelatedPartyDataMapper<TmaSubscriptionAccessData, TmaSubscriptionBaseDetailRefWsDto>
{
	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade subscribedProductFacade;

	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";

	@Override
	public void mapAtoB(final TmaSubscriptionAccessData a, final TmaSubscriptionBaseDetailRefWsDto b, final MappingContext context)
	{
		mapFieldsAtoB(a, b, context);
		mapProductRefAtoB(a, b, context);
		mapRelatedPartyRefAtoB(a, b, context);
	}

	protected void mapFieldsAtoB(final TmaSubscriptionAccessData a, final TmaSubscriptionBaseDetailRefWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(SUBSCRIBER_IDENTITY, getAType(), a, "id", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriberIdentity()))
			{
				b.setId(a.getSubscriberIdentity());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapProductRefAtoB(final TmaSubscriptionAccessData a, final TmaSubscriptionBaseDetailRefWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(SUBSCRIBER_IDENTITY, getAType(), a, "product", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriberIdentity()))
			{
				final List<TmaSubscribedProductData> subscribedProducts = getSubscribedProductFacade().getSubscribedProducts(
						a.getSubscriberIdentity(), a.getBillingSystemId());
				if (CollectionUtils.isNotEmpty(subscribedProducts))
				{
					final List<TmaProductRefWsDto> productRefWsDtos = new ArrayList<>();
					subscribedProducts.forEach(subscribedProduct ->
					{
						subscribedProduct.setCustomerId(a.getPrincipalUid());
						final TmaProductRefWsDto productRefWsDto = mapperFacade.map(subscribedProduct, TmaProductRefWsDto.class,
								context);
						productRefWsDtos.add(productRefWsDto);
					});
					b.setProduct(productRefWsDtos);
				}
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapRelatedPartyRefAtoB(final TmaSubscriptionAccessData a, final TmaSubscriptionBaseDetailRefWsDto b,
			final MappingContext context)
	{
		context.beginMappingField(SUBSCRIBER_IDENTITY, getAType(), a, "relatedPartyRef", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriberIdentity()))
			{
				final List<TmaRelatedPartyWsDto> relatedPartyWsDtos = getRelatedPartyRefList(a.getSubscriberIdentity(),
						a.getBillingSystemId(), context);
				b.setRelatedPartyRef(relatedPartyWsDtos);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaSubscriptionBaseDetailRefWsDto b, final TmaSubscriptionAccessData a, final MappingContext context)
	{
		context.beginMappingField("id", getBType(), b, SUBSCRIBER_IDENTITY, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getId()))
			{
				a.setSubscriberIdentity(b.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected TmaSubscribedProductFacade getSubscribedProductFacade()
	{
		return subscribedProductFacade;
	}

	public void setSubscribedProductFacade(final TmaSubscribedProductFacade subscribedProductFacade)
	{
		this.subscribedProductFacade = subscribedProductFacade;
	}
}
