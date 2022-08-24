/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * {@link TmaTmfAbstractRelatedPartyDataMapper} class containing common functionalities to be used by other mappers.
 *
 * @since 1810
 */
@WsDTOMapping
public abstract class TmaTmfAbstractRelatedPartyDataMapper<A, B> extends AbstractCustomMapper<A, B>
{
	@Resource
	private CustomerFacade customerFacade;
	@Resource
	private TmaSubscriptionAccessFacade subscriptionAccessFacade;

	/**
	 * Get Related Party Dto for given customer Id.
	 *
	 * @param custId
	 *           customer id of given customer.
	 * @param context
	 *           {@link MappingContext} to map fields between source and destination.
	 * @return {@link TmaRelatedPartyWsDto} for given customer id and mapping context.
	 */
	public TmaRelatedPartyWsDto getRelatedPartyRefDetailsForCustomerId(final String custId, final MappingContext context)
	{
		final CustomerData customerData = getCustomerFacade().getUserForUID(custId);
		return mapperFacade.map(customerData, TmaRelatedPartyWsDto.class, context);
	}

	/**
	 * Retrieve list of all {@link TmaRelatedPartyWsDto}s for given subscriberIdentity and billingSystemId.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of SubscriptionAccess.
	 * @param billingSystemId
	 *           unique identifier of SubscriptionAccess.
	 * @param context
	 *           {@link MappingContext} to map fields between source and destination.
	 * @return {@link List} of all {@link TmaRelatedPartyWsDto}s belonging to given subscriberIdentity and
	 *         billingSystemId.
	 */
	public List<TmaRelatedPartyWsDto> getRelatedPartyRefList(final String subscriberIdentity, final String billingSystemId,
			final MappingContext context)
	{
		final List<TmaSubscriptionAccessData> subscriptionAccesses = getSubscriptionAccessFacade()
				.getSubscriptionAccessesBySubscriberIdentity(subscriberIdentity, billingSystemId);
		return getTmaRelatedPartyWsDtoList(subscriptionAccesses, context);
	}

	/**
	 * Get {@link List} of {@link TmaRelatedPartyWsDto}
	 *
	 * @param subscriptionAccesses
	 *           list of {@link TmaSubscriptionAccessData}
	 * @param context
	 *           {@link MappingContext} to map fields between source and destination.
	 * @return {@link List} of {@link TmaRelatedPartyWsDto} for all subscription Accesses.
	 */
	public List<TmaRelatedPartyWsDto> getTmaRelatedPartyWsDtoList(
			final List<TmaSubscriptionAccessData> subscriptionAccesses, final MappingContext context)
	{
		final List<TmaRelatedPartyWsDto> relatedPartyWsDtos = new ArrayList<>();
		subscriptionAccesses.forEach(subscriptionAccess ->
		{
			if (StringUtils.isNotEmpty(subscriptionAccess.getPrincipalUid()))
			{
				final TmaRelatedPartyWsDto relatedPartyWsDto = getRelatedPartyDetails(subscriptionAccess, context);
				relatedPartyWsDtos.add(relatedPartyWsDto);
			}
		});
		return relatedPartyWsDtos;
	}

	/**
	 * Get RelatedPartyDetails for given subscriptionAccess
	 *
	 * @param subscriptionAccess
	 *           {@link TmaSubscriptionAccessData}
	 * @param context
	 *           {@link MappingContext} to map fields between source and destination.
	 * @return {@link TmaRelatedPartyWsDto} containing details of Related party.
	 */
	public TmaRelatedPartyWsDto getRelatedPartyDetails(
			final TmaSubscriptionAccessData subscriptionAccess, final MappingContext context)
	{
		final TmaRelatedPartyWsDto relatedPartyWsDto = getRelatedPartyRefDetailsForCustomerId(
				subscriptionAccess.getPrincipalUid(), context);
		relatedPartyWsDto.setRole(subscriptionAccess.getAccessType().name());
		return relatedPartyWsDto;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	protected TmaSubscriptionAccessFacade getSubscriptionAccessFacade()
	{
		return subscriptionAccessFacade;
	}

	public void setSubscriptionAccessFacade(final TmaSubscriptionAccessFacade subscriptionAccessFacade)
	{
		this.subscriptionAccessFacade = subscriptionAccessFacade;
	}
}