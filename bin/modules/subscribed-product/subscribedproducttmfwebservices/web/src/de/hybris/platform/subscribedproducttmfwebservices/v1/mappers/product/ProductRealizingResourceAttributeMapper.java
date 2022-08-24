/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiResourceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ResourceRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for realizingResource attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductRealizingResourceAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductRealizingResourceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getResources()))
		{
			return;
		}

		final List<ResourceRef> realizingResources = new ArrayList<>();
		source.getResources().forEach(resourceModel -> {
			final ResourceRef resourceRef = getMapperFacade().map(resourceModel, ResourceRef.class, context);
			realizingResources.add(resourceRef);
		});

		target.setRealizingResource(realizingResources);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getRealizingResource()))
		{
			return;
		}

		source.setResources(getAllResources(target.getRealizingResource(), context));
	}

	private Set<SpiResourceModel> getAllResources(final List<ResourceRef> resourceRefs, final MappingContext context)
	{
		final Set<SpiResourceModel> result = new HashSet<>();
		resourceRefs.forEach(resourceRef -> {
			SpiResourceModel resourceModel = (SpiResourceModel) getSpiGenericService()
					.getItem(SpiResourceModel._TYPECODE, resourceRef.getId());
			if (resourceModel == null)
			{
				resourceModel = (SpiResourceModel) getSpiGenericService().createItem(SpiResourceModel.class);
			}
			getMapperFacade().map(resourceRef, resourceModel, context);
			getSpiGenericService().saveItem(resourceModel);
			result.add(resourceModel);
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
