/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productspeccharacteristicvalueuse;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ProductSpecCharacteristicValues attribute between {@link TmaProductSpecCharacteristicValueUseData} and
 * {@link ProdSpecCharValueUseWsDTO}
 *
 * @since 2105
 */
public class TmaPscvuPscvAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicValueUseData, ProdSpecCharValueUseWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPscvuPscvAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicValueUseData source,
			final ProdSpecCharValueUseWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isNotEmpty(source.getProductSpecCharacteristicValues()))
		{
			target.setProductSpecCharacteristicValue(getMapperFacade()
					.mapAsList(source.getProductSpecCharacteristicValues(), ProductSpecCharacteristicValueWsDTO.class,context));
		}
	}
	
	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
