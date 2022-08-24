/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.product;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaRelatedPartyRole;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedParty;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps relatedParty attribute between {@link OrderEntryData} and {@link Product}
 *
 * @since 2003
 */
public class ProductRelatedPartyAttributeMapper extends TmaAttributeMapper<OrderEntryData, Product>
{

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final Product target,
			final MappingContext context)
	{

		if (ObjectUtils.isEmpty(source.getServiceProvider()))
		{
			return;
		}

		final RelatedParty serviceProvider = new RelatedParty();
		serviceProvider.setId(source.getServiceProvider());
		serviceProvider.setRole(TmaRelatedPartyRole.SERVICE_PROVIDER.name());

		if (ObjectUtils.isEmpty(target.getRelatedParty()))
		{
			final List<RelatedParty> serviceProviders = new ArrayList<>();
			serviceProviders.add(serviceProvider);
			target.setRelatedParty(serviceProviders);
		}
		else
		{
			target.getRelatedParty().add(serviceProvider);
		}
	}

}
