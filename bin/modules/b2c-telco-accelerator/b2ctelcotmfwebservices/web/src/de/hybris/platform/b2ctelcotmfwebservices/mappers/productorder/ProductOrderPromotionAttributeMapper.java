/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;



import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PromotionRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Status;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for promotion attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 2003
 */
public class ProductOrderPromotionAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public ProductOrderPromotionAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target, final MappingContext context)
	{
		final List<PromotionRef> promotionRefList = new ArrayList<>();

		if (CollectionUtils.isEmpty(source.getAppliedOrderPromotions()))
		{
			return;
		}

		source.getAppliedOrderPromotions().forEach((PromotionResultData promotionResult) -> {
			final PromotionRef promotionRef = getMapperFacade().map(promotionResult, PromotionRef.class, context);
			promotionRef.setStatus(Status.APPLIED);
			promotionRefList.add(promotionRef);
		});

		target.setPromotion(promotionRefList);

	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
