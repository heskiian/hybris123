/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiServiceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ServiceRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for realizingService attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductRealizingServiceAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductRealizingServiceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getServices()))
		{
			return;
		}

		final List<ServiceRef> realizingServices = new ArrayList<>();
		source.getServices().forEach(serviceModel -> {
			final ServiceRef serviceRef = getMapperFacade().map(serviceModel, ServiceRef.class, context);
			realizingServices.add(serviceRef);
		});

		target.setRealizingService(realizingServices);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getRealizingService()))
		{
			return;
		}

		source.setServices(getAllServices(target.getRealizingService(), context));
	}

	private Set<SpiServiceModel> getAllServices(final List<ServiceRef> services, final MappingContext context)
	{
		final Set<SpiServiceModel> result = new HashSet<>();
		services.forEach(serviceRef -> {
			SpiServiceModel spiServiceModel = (SpiServiceModel) getSpiGenericService()
					.getItem(SpiServiceModel._TYPECODE, serviceRef.getId());
			if (spiServiceModel == null)
			{
				spiServiceModel = (SpiServiceModel) getSpiGenericService().createItem(SpiServiceModel.class);
			}
			getMapperFacade().map(serviceRef, spiServiceModel, context);
			getSpiGenericService().saveItem(spiServiceModel);
			result.add(spiServiceModel);
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
