/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for product specification characateristic value use attribute between {@link ProductData}
 * and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoPscvuAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductSpecCharValueUses()))
		{
			return;
		}

		final List<ProdSpecCharValueUse> prodSpecCharValueUseList = new ArrayList<>();
		source.getProductSpecCharValueUses().forEach(productSpecCharValueUseData ->
		{
			final ProdSpecCharValueUse prodSpecCharValueUseItem = getMapperFacade()
					.map(productSpecCharValueUseData, ProdSpecCharValueUse.class, context);
			prodSpecCharValueUseList.add(prodSpecCharValueUseItem);
		});

		target.setProdSpecCharValueUse(prodSpecCharValueUseList);
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
