/*
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecificationWsDTO;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * TmaPsAttributeMapper populates value of productSpecification attribute from
 * {@link TmaProductSpecCharacteristicValueUseData} to {@link ProdSpecCharValueUseWsDTO}
 *
 * @since 1907
 */
public class TmaPsAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicValueUseData, ProdSpecCharValueUseWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicValueUseData source,
			final ProdSpecCharValueUseWsDTO target, final MappingContext context)

	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getProductSpecification() == null)
		{
			return;
		}

		final ProductSpecificationWsDTO productSpecificationRef = getMapperFacade().map(source.getProductSpecification(),
				ProductSpecificationWsDTO.class, context);
		target.setProductSpecification(productSpecificationRef);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
