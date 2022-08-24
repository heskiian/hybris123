/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product offering attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemPoAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;
	private TmaProductOfferFacade tmaProductOfferFacade;

	public OrderItemPoAttributeMapper(final TmaProductOfferFacade tmaProductOfferFacade)
	{
		this.tmaProductOfferFacade = tmaProductOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderItem target,
			final MappingContext context)
	{
		final TmaSubscribedProductAction action =
				source.getSubscriptionInfo() != null ? source.getSubscriptionInfo().getSubscribedProductAction() : null;

		if (source.getProduct() == null || getTmaProductOfferFacade().isServiceRequestAction(action))
		{
			return;
		}

		final ProductOfferingRef productOfferingRef = getMapperFacade().map(source.getProduct(), ProductOfferingRef.class, context);

		target.setProductOffering(productOfferingRef);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	protected TmaProductOfferFacade getTmaProductOfferFacade()
	{
		return tmaProductOfferFacade;
	}
}
