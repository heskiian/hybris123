/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This class populates value of productSpecCharValueUses attribute from {@link ProductData} to {@link ProductWsDTO}
 *
 * @since 2007
 */
public class TmaProductSpecCharValuesAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{

	private MapperFacade mapperFacade;

	public TmaProductSpecCharValuesAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (CollectionUtils.isEmpty(source.getProductSpecCharValueUses()))
		{
			return;
		}
		final List<ProductSpecCharacteristicValueWsDTO> productSpecCharacteristicValueWsDTOList = new ArrayList<>();
		source.getProductSpecCharValueUses().stream()
				.filter(productSpecCharValueUseData -> CollectionUtils
						.isNotEmpty(productSpecCharValueUseData.getProductSpecCharacteristicValues()))
				.forEach(productSpecCharValueUseData -> productSpecCharValueUseData.getProductSpecCharacteristicValues()
						.forEach(productSpecCharValueData -> {
							final boolean alreadyAddedPscv = productSpecCharacteristicValueWsDTOList.stream()
									.map(ProductSpecCharacteristicValueWsDTO::getId)
									.anyMatch(id -> productSpecCharValueData.getId().equals(id));
							if (!alreadyAddedPscv)
							{
								final ProductSpecCharacteristicValueWsDTO productSpecCharValueWsDTO = getMapperFacade()
										.map(productSpecCharValueData, ProductSpecCharacteristicValueWsDTO.class);
								productSpecCharacteristicValueWsDTOList.add(productSpecCharValueWsDTO);

							}
						}));

		target.setProductSpecCharValues(productSpecCharacteristicValueWsDTOList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}