/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
import de.hybris.platform.util.Config;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscribedProductData} and (@link TmaProductRefWsDto}
 *
 * @since 1810
 */
public class TmaProductRefWsDtoMapper extends TmaTmfAbstractRelatedPartyDataMapper<TmaSubscribedProductData, TmaProductRefWsDto>
{
	private static final String SUBSCRIPTION_BASE_ID = "subscriptionBaseId";
	private static final String CUSTOMER_TYPE_OWNER = "OWNER";
	private static final String RELATED_PARTY = "user";
	private static final String PORDUCT_WS_DTO_HREF = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_HREF, StringUtils.EMPTY);

	@Override
	public void mapAtoB(final TmaSubscribedProductData a, final TmaProductRefWsDto b, final MappingContext context)
	{
		mapDefaultAtoB(a, b, context);
		mapFieldsAtoB(a, b, context);
		mapRelatedPartyAtoB(a, b, context);
	}

	protected void mapRelatedPartyAtoB(final TmaSubscribedProductData a, final TmaProductRefWsDto b, final MappingContext context)
	{
		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, RELATED_PARTY, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriptionBaseId())
					&& StringUtils.isNotEmpty(a.getBillingsystemId()))
			{
				final List<TmaSubscriptionAccessData> subscriptionAccesses = getSubscriptionAccessFacade()
						.getSubscriptionAccessesBySubscriberIdentity(a.getSubscriptionBaseId(), a.getBillingsystemId());
				getRelatedPartyUser(subscriptionAccesses, a, b, context);
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void getRelatedPartyUser(final List<TmaSubscriptionAccessData> subscriptionAccesses,
			final TmaSubscribedProductData a, final TmaProductRefWsDto b, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(subscriptionAccesses))
		{
			return;
		}
		for (final TmaSubscriptionAccessData subscriptionAccess : subscriptionAccesses)
		{
			if (isValidAccessDataOrCustomerIdAvailable(a.getCustomerId(), subscriptionAccess))
			{
				b.setUser(getValidTmaRelatedPartyData(subscriptionAccess, context));
				break;
			}
			b.setUser(getValidTmaRelatedPartyData(subscriptionAccesses.get(0), context));
		}
	}

	protected TmaRelatedPartyWsDto getValidTmaRelatedPartyData(final TmaSubscriptionAccessData subscriptionAccess,
			final MappingContext context)
	{
		final TmaRelatedPartyWsDto relatedPartyWsDto = getRelatedPartyRefDetailsForCustomerId(subscriptionAccess.getPrincipalUid(),
				context);
		relatedPartyWsDto.setRole(subscriptionAccess.getAccessType().name());
		return relatedPartyWsDto;
	}

	/**
	 * Returns true if customerId matches the customerId present in accessData OR if access type of Customer is OWNER
	 *
	 * @param customerID
	 *           identifier of subscribedProductData
	 * @param accessData
	 *           identifier of a SubscriptionAccess
	 * @return {@link boolean} true if customerId matches the customerId of accessData OR if access type of Customer is
	 *         OWNER else false
	 */
	protected Boolean isValidAccessDataOrCustomerIdAvailable(final String customerID, final TmaSubscriptionAccessData accessData)
	{
		if (StringUtils.isNotEmpty(customerID))
		{
			return (accessData.getPrincipalUid().equalsIgnoreCase(customerID));
		}
		return (CUSTOMER_TYPE_OWNER.equalsIgnoreCase(accessData.getAccessType().name()));
	}

	protected void mapFieldsAtoB(final TmaSubscribedProductData a, final TmaProductRefWsDto b, final MappingContext context)
	{
		context.beginMappingField("productCode", getAType(), a, "name", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getProductCode()))
			{
				b.setName(a.getName());
			}
		}
		finally
		{
			context.endMappingField();
		}


		context.beginMappingField(SUBSCRIPTION_BASE_ID, getAType(), a, "publicIdentifier", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getSubscriptionBaseId()))
			{
				b.setPublicIdentifier(a.getSubscriptionBaseId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapDefaultAtoB(final TmaSubscribedProductData a, final TmaProductRefWsDto b, final MappingContext context)
	{
		context.beginMappingField("id", getAType(), a, "href", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getId()))
			{
				b.setHref(PORDUCT_WS_DTO_HREF + a.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaProductRefWsDto b, final TmaSubscribedProductData a, final MappingContext context)
	{
		context.beginMappingField("publicIdentifier", getBType(), b, SUBSCRIPTION_BASE_ID, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getPublicIdentifier()))
			{
				a.setSubscriptionBaseId(b.getPublicIdentifier());
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField("productCode", getAType(), a, "name", getBType(), b);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getName()))
			{
				a.setProductCode(b.getName());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
