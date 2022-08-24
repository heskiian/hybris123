/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps productOffering attributes between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1907
 */
public class CartItemProductOfferingAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;
	private TmaProductOfferFacade tmaProductOfferFacade;

	public CartItemProductOfferingAttributeMapper(final TmaProductOfferFacade tmaProductOfferFacade)
	{
		this.tmaProductOfferFacade=tmaProductOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target,
			MappingContext context)
	{
		final ProductData productData = source.getProduct();
		final TmaSubscribedProductAction action =
				source.getSubscriptionInfo() != null ? source.getSubscriptionInfo().getSubscribedProductAction() : null;

		if (!getTmaProductOfferFacade().isServiceRequestAction(action))
		{
			final ProductOfferingRef productOfferingRef = getMapperFacade().map(productData, ProductOfferingRef.class, context);
			target.setProductOffering(productOfferingRef);
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	protected TmaProductOfferFacade getTmaProductOfferFacade()
	{
		return tmaProductOfferFacade;
	}
}
