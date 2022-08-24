/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for po price attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoPopAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	private MapperFacade mapperFacade;

	public PoPopAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPrices()))
		{
			return;
		}

		final List<ProductOfferingPrice> priceList = new ArrayList<>();
		source.getPrices().forEach(priceData ->
		{
			final ProductOfferingPrice price = getMapperFacade().map(priceData, ProductOfferingPrice.class, context);
			price.setLifecycleStatus(source.getApprovalStatus());
			priceList.add(price);
		});

		target.setProductOfferingPrice(priceList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
