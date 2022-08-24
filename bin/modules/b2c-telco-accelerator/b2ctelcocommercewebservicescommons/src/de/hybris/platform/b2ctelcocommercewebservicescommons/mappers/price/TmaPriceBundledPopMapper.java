/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute mapper maps data for bundled POP price attribute between {@link PriceData} and
 * {@link ProductOfferingPriceWsDTO}. This mapper exposes the composite prices configured as
 * {@link PriceRowModel#getProductOfferingPrice()}
 *
 * @since 2007
 */
public class TmaPriceBundledPopMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;
	private Map<String, Class<ProductOfferingPriceWsDTO>> priceTypeDtoMap;

	public TmaPriceBundledPopMapper(final MapperFacade mapperFacade,
			final Map<String, Class<ProductOfferingPriceWsDTO>> priceTypeDtoMap)
	{
		this.mapperFacade = mapperFacade;
		this.priceTypeDtoMap = priceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (ObjectUtils.isEmpty(source.getProductOfferingPrice()))
		{
			populatePriceValue(source, target);
			target.setIsBundle(false);
			return;
		}

		final ProductOfferingPriceWsDTO productOfferingPrice = getMapperFacade()
				.map(source.getProductOfferingPrice(), getPriceTypeDtoMap()
						.get(source.getProductOfferingPrice().getClass().getSimpleName()), context);

		target.setBundledPop(Collections.singletonList(productOfferingPrice));
		target.setIsBundle(true);
	}

	protected void populatePriceValue(final PriceData source, final ProductOfferingPriceWsDTO target)
	{
		if (StringUtils.isEmpty(source.getCurrencyIso()) && source.getValue() == null)
		{
			return;
		}

		final MoneyWsDTO money = new MoneyWsDTO();
		money.setCurrencyIso(source.getCurrencyIso());
		money.setValue(source.getValue().toString());

		target.setPrice(money);
	}

	protected Map<String, Class<ProductOfferingPriceWsDTO>> getPriceTypeDtoMap()
	{
		return priceTypeDtoMap;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
