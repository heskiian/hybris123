/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiCharacteristicModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productCharacteristic attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductCharacteristicAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductCharacteristicAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getCharacteristics()))
		{
			return;
		}

		final List<Characteristic> productCharacteristics = new ArrayList<>();
		source.getCharacteristics().forEach(productCharacteristic -> {
			final Characteristic characteristic = getMapperFacade().map(productCharacteristic, Characteristic.class, context);
			productCharacteristics.add(characteristic);
		});

		target.setProductCharacteristic(productCharacteristics);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductCharacteristic()))
		{
			return;
		}

		source.setCharacteristics(getAllCharacteristics(target.getProductCharacteristic(), context));
	}

	private Set<SpiCharacteristicModel> getAllCharacteristics(final List<Characteristic> characteristics,
			final MappingContext context)
	{
		final Set<SpiCharacteristicModel> result = new HashSet<>();
		characteristics.forEach(characteristic -> {
			final SpiCharacteristicModel spiCharacteristicModel = (SpiCharacteristicModel) getSpiGenericService()
					.createItem(SpiCharacteristicModel.class);
			getMapperFacade().map(characteristic, spiCharacteristicModel, context);
			result.add(spiCharacteristicModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
