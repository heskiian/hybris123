/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundledPop attribute between {@link TmaCompositeProdOfferPriceModel} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopBundledPopAttributeMapper extends TmaAttributeMapper<TmaCompositeProdOfferPriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	public PopBundledPopAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaCompositeProdOfferPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getChildren()))
		{
			return;
		}
		final List<ProductOfferingPrice> children = new ArrayList<>();
		source.getChildren().forEach(child ->
				children.add(getMapperFacade().map(child, ProductOfferingPrice.class)));

		target.setBundledPop(children);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
