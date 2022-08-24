/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * TmaProdOfferPriceAttributeMapper populates value of ProductOfferingPrice attribute from {@link ProductData} to
 * {@link ProductWsDTO}
 *
 * @since 1907
 */
public class TmaProdOfferPriceAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getProductOfferingPrices()))
		{
			return;
		}

		final List<ProductOfferingPriceWsDTO> priceList = new ArrayList<>();
		source.getProductOfferingPrices().forEach(priceData ->
		{
			final ProductOfferingPriceWsDTO price = getMapperFacade().map(priceData, ProductOfferingPriceWsDTO.class, context);
			price.setLifecycleStatus(source.getApprovalStatus());
			priceList.add(price);
		});

		target.setProductOfferingPrice(priceList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
