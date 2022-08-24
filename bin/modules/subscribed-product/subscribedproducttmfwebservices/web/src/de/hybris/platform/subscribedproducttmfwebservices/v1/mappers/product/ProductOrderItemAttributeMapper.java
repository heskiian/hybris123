/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiRelatedProductOrderItemModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedProductOrderItem;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOrderItem attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductOrderItemAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private static final String ORDER_ITEM_ID = "orderItemId";

	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductOrderItemAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getRelatedProductOrderItems()))
		{
			return;
		}

		final List<RelatedProductOrderItem> relatedProductOrderItems = new ArrayList<>();
		source.getRelatedProductOrderItems().forEach(relatedProductOrderItemModel -> {
			final RelatedProductOrderItem relatedProductOrderItem = getMapperFacade().map(relatedProductOrderItemModel,
					RelatedProductOrderItem.class, context);
			relatedProductOrderItems.add(relatedProductOrderItem);
		});

		target.setProductOrderItem(relatedProductOrderItems);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductOrderItem()))
		{
			return;
		}

		source.setRelatedProductOrderItems(getAllProductOrderItems(target.getProductOrderItem(), context));
	}

	private Set<SpiRelatedProductOrderItemModel> getAllProductOrderItems(
			final List<RelatedProductOrderItem> relatedProductOrderItems, final MappingContext context)
	{
		final Set<SpiRelatedProductOrderItemModel> result = new HashSet<>();
		relatedProductOrderItems.forEach(relatedProductOrderItem -> {
			final Map<String, String> keys = new HashMap<>();
			keys.put(ORDER_ITEM_ID, relatedProductOrderItem.getOrderItemId());
			SpiRelatedProductOrderItemModel relatedProductOrderItemModel = (SpiRelatedProductOrderItemModel) getSpiGenericService()
					.getItem(SpiRelatedProductOrderItemModel._TYPECODE, keys);
			if (relatedProductOrderItemModel == null)
			{
				relatedProductOrderItemModel = (SpiRelatedProductOrderItemModel) getSpiGenericService()
						.createItem(SpiRelatedProductOrderItemModel.class);
			}
			getMapperFacade().map(relatedProductOrderItem, relatedProductOrderItemModel, context);
			getSpiGenericService().saveItem(relatedProductOrderItemModel);
			result.add(relatedProductOrderItemModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
