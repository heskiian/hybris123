/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link CartData} and
 * {@link ShoppingCart}
 *
 * @since 1907
 */
public class ShoppingCartRelatedPartyAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		if (source.getUser() == null)
		{
			return;
		}
		final List<RelatedPartyRef> relatedPartyRefs = new ArrayList<>();
		final PrincipalData user = source.getUser();
		final RelatedPartyRef cartRelatedParty = getMapperFacade().map(user, RelatedPartyRef.class, context);
		relatedPartyRefs.add(cartRelatedParty);
		target.setRelatedParty(relatedPartyRefs);
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
}
