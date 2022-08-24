/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for po price attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoPriceAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;

	public PoPriceAttributeMapper(final MapperFacade mapperFacade)
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
		final Set<TmaProductOfferingPriceData> inputPrices = new HashSet<>();
		final Set<String> inputPricesIds = new HashSet<>();

		source.getPrices().forEach(priceData ->
		{
			if (priceData.getProductOfferingPrice() != null && inputPricesIds.add(priceData.getProductOfferingPrice().getId()))
			{
				inputPrices.add(priceData.getProductOfferingPrice());
			}
		});

		target.setProductOfferingPrice(getMapperFacade().mapAsList(inputPrices, ProductOfferingPrice.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
