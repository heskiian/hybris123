/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.subscribedproduct;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.SubscribedProductWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RelatedPartyWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaRelatedPartyRole;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps relatedParty attribute between {@link OrderEntryData} and
 * {@link SubscribedProductWsDTO}
 *
 * @since 2003
 */
public class TmaSubscribedProductRelatedPartyAttributeMapper extends TmaAttributeMapper<OrderEntryData, SubscribedProductWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final SubscribedProductWsDTO target,
			final MappingContext context)
	{

		if (ObjectUtils.isEmpty(source.getServiceProvider()))
		{
			return;
		}

		final RelatedPartyWsDTO serviceProvider = new RelatedPartyWsDTO();
		serviceProvider.setId(source.getServiceProvider());
		serviceProvider.setRole(TmaRelatedPartyRole.SERVICE_PROVIDER.name());

		if (ObjectUtils.isEmpty(target.getRelatedParty()))
		{
			final List<RelatedPartyWsDTO> serviceProviders = new ArrayList<>();
			serviceProviders.add(serviceProvider);
			target.setRelatedParty(serviceProviders);
		}
		else
		{
			target.getRelatedParty().add(serviceProvider);
		}
	}

}
