/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * The TmaPopPscvAttributeMapper class maps data for ProductSpecCharacteristicValue attribute between
 * {@link TmaProductOfferingPriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 2011
 */
public class TmaPopPscvAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPopPscvAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source,
			final ProductOfferingPriceWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (CollectionUtils.isEmpty(source.getProductSpecCharacteristicValueUses()))
		{
			return;
		}
		final Set<ProductSpecCharacteristicValueWsDTO> productSpecCharacteristicValueWsDTOs = new HashSet<>();
		source.getProductSpecCharacteristicValueUses().stream()
				.filter(productSpecCharValueUseData -> CollectionUtils
						.isNotEmpty(productSpecCharValueUseData.getProductSpecCharacteristicValues()))
				.forEach(productSpecCharValueUseData -> {
					final ProductSpecCharacteristicValueWsDTO productSpecCharValueWsDTO = getMapperFacade()
							.map(productSpecCharValueUseData.getProductSpecCharacteristicValues().get(0),
									ProductSpecCharacteristicValueWsDTO.class);
					productSpecCharacteristicValueWsDTOs.add(productSpecCharValueWsDTO);
				});

		target.setProductSpecCharValues(productSpecCharacteristicValueWsDTOs);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}