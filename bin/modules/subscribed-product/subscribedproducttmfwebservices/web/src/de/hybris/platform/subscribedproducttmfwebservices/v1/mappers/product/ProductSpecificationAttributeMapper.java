/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductSpecificationModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductSpecificationRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productSpecification attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductSpecificationAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductSpecificationAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (source.getProductSpecification() == null)
		{
			return;
		}

		target.setProductSpecification(
				getMapperFacade().map(source.getProductSpecification(), ProductSpecificationRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (target.getProductSpecification() == null)
		{
			return;
		}

		SpiProductSpecificationModel spiProductSpecificationModel = (SpiProductSpecificationModel) getSpiGenericService()
				.getItem(SpiProductSpecificationModel._TYPECODE, target.getProductSpecification().getId());

		if (spiProductSpecificationModel == null)
		{
			spiProductSpecificationModel = (SpiProductSpecificationModel) getSpiGenericService()
					.createItem(SpiProductSpecificationModel.class);
		}

		getMapperFacade().map(target.getProductSpecification(), spiProductSpecificationModel, context);
		getSpiGenericService().saveItem(spiProductSpecificationModel);
		source.setProductSpecification(spiProductSpecificationModel);
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
