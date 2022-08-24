/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productprice;

import de.hybris.platform.subscribedproductservices.model.SpiProductOfferingPriceModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductOfferingPriceRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOfferingPrice attribute between {@link SpiProductPriceModel} and {@link ProductPrice}
 *
 * @since 2105
 */
public class ProductPriceProductOfferingPriceAttributeMapper extends SpiAttributeMapper<SpiProductPriceModel, ProductPrice>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductPriceProductOfferingPriceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductPriceModel source, final ProductPrice target,
			final MappingContext context)
	{
		if (source.getProductOfferingPrice() == null)
		{
			return;
		}

		target.setProductOfferingPrice(
				getMapperFacade().map(source.getProductOfferingPrice(), ProductOfferingPriceRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductPrice target, final SpiProductPriceModel source,
			final MappingContext context)
	{
		if (target.getProductOfferingPrice() == null)
		{
			return;
		}

		SpiProductOfferingPriceModel productOfferingPriceModel = (SpiProductOfferingPriceModel) getSpiGenericService()
				.getItem(SpiProductOfferingPriceModel._TYPECODE, target.getProductOfferingPrice().getId());
		if (productOfferingPriceModel == null)
		{
			productOfferingPriceModel = (SpiProductOfferingPriceModel) getSpiGenericService()
					.createItem(SpiProductOfferingPriceModel.class);
		}

		getMapperFacade().map(target.getProductOfferingPrice(), productOfferingPriceModel, context);
		getSpiGenericService().saveItem(productOfferingPriceModel);
		source.setProductOfferingPrice(productOfferingPriceModel);
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
