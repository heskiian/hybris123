/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristic;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productSpecCharacteristic attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationPscAttributeMapper extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	private MapperFacade mapperFacade;

	public ProductSpecificationPscAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source, final ProductSpecification target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductSpecCharacteristic()))
		{
			return;
		}

		final List<ProductSpecCharacteristic> productSpecCharacteristics = new ArrayList<>();
		source.getProductSpecCharacteristic().forEach(productSpecCharacteristicData ->
		{
			final ProductSpecCharacteristic productSpecCharacteristic = getMapperFacade().map(productSpecCharacteristicData,
					ProductSpecCharacteristic.class, context);
			productSpecCharacteristics.add(productSpecCharacteristic);
		});

		target.setProductSpecCharacteristic(productSpecCharacteristics);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
