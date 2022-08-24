/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ProductSpecCharacteristicValueUse between {@link TmaProductOfferingPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2011
 */
public class PopPscvuAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	public PopPscvuAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductSpecCharacteristicValueUses()))
		{
			return;
		}

		final List<ProdSpecCharValueUse> prodSpecCharValueUseList = new ArrayList<>();
		source.getProductSpecCharacteristicValueUses().forEach(productSpecCharValueUseData ->
		{
			final ProdSpecCharValueUse prodSpecCharValueUseItem = getMapperFacade()
					.map(productSpecCharValueUseData, ProdSpecCharValueUse.class, context);
			prodSpecCharValueUseList.add(prodSpecCharValueUseItem);
		});

		target.setProdSpecCharValueUse(prodSpecCharValueUseList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
