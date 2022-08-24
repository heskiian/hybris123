/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.bundledproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.BundledProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.BundledProductOfferingOption;
import de.hybris.platform.commercefacades.product.data.ProductData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundled product offering option attribute between {@link ProductData} and {@link BundledProductOffering}
 *
 * @since 2011
 */
public class BpoOptionAttributeMapper extends TmaAttributeMapper<ProductData, BundledProductOffering>
{
	private MapperFacade mapperFacade;

	public BpoOptionAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final BundledProductOffering target,
			final MappingContext context)
	{
		if (source.getBundledProdOfferOption() != null)
		{
			target.setBundledProductOfferingOption(getMapperFacade().map(source.getBundledProdOfferOption(),
					BundledProductOfferingOption.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
