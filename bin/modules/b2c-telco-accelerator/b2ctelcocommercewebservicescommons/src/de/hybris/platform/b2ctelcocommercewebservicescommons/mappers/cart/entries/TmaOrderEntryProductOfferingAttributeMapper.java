/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps product attribute between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 2003
 */
public class TmaOrderEntryProductOfferingAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderEntryWsDTO>
{

	private MapperFacade mapperFacade;
	private TmaProductOfferFacade tmaProductOfferFacade;

	public TmaOrderEntryProductOfferingAttributeMapper(final MapperFacade mapperFacade,
			final TmaProductOfferFacade tmaProductOfferFacade)
	{
		this.mapperFacade = mapperFacade;
		this.tmaProductOfferFacade = tmaProductOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderEntryWsDTO target,
			final MappingContext context)
	{
		final TmaSubscribedProductAction action =
				source.getSubscriptionInfo() != null ? source.getSubscriptionInfo().getSubscribedProductAction() : null;

		if (!getTmaProductOfferFacade().isServiceRequestAction(action))
		{
			final ProductWsDTO product = getMapperFacade().map(source.getProduct(), ProductWsDTO.class, context);
			target.setProduct(product);
			return;
		}
		target.setProduct(null);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected TmaProductOfferFacade getTmaProductOfferFacade()
	{
		return tmaProductOfferFacade;
	}
}
