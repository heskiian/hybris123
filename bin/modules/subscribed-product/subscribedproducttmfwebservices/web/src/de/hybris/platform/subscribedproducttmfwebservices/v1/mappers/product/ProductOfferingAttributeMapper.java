/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductOfferingModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductOfferingRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOffering attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductOfferingAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductOfferingAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (source.getProductOffering() == null)
		{
			return;
		}

		target.setProductOffering(getMapperFacade().map(source.getProductOffering(), ProductOfferingRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (target.getProductOffering() == null)
		{
			return;
		}
		SpiProductOfferingModel spiProductOfferingModel = (SpiProductOfferingModel) getSpiGenericService()
				.getItem(SpiProductOfferingModel._TYPECODE, target.getProductOffering().getId());
		if (spiProductOfferingModel == null)
		{
			spiProductOfferingModel = (SpiProductOfferingModel) getSpiGenericService()
					.createItem(SpiProductOfferingModel.class);
		}
		getMapperFacade().map(target.getProductOffering(), spiProductOfferingModel, context);
		getSpiGenericService().saveItem(spiProductOfferingModel);
		source.setProductOffering(spiProductOfferingModel);
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
