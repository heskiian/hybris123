/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PromotionRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Status;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for promotion attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1911
 */
public class ShoppingCartPromotionAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public ShoppingCartPromotionAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		final List<PromotionRef> promotionRefList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(source.getAppliedOrderPromotions()))
		{
			source.getAppliedOrderPromotions().forEach((PromotionResultData promotionResult) -> {
				final PromotionRef promotionRef = getMapperFacade().map(promotionResult, PromotionRef.class, context);
				promotionRef.setStatus(Status.APPLIED);
				promotionRefList.add(promotionRef);
			});
		}

		if (CollectionUtils.isNotEmpty(source.getPotentialOrderPromotions()))
		{
			source.getPotentialOrderPromotions().forEach((PromotionResultData promotionResult) -> {
				final PromotionRef promotionRef = getMapperFacade().map(promotionResult, PromotionRef.class, context);
				promotionRef.setStatus(Status.POTENTIAL);
				promotionRefList.add(promotionRef);
			});
		}

		if (CollectionUtils.isNotEmpty(promotionRefList))
		{
			target.setPromotion(promotionRefList);
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
