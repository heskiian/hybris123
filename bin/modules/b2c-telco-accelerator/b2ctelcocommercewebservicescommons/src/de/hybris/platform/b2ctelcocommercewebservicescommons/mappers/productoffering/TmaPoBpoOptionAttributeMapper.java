/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.BundledProductOfferingOptionWsDto;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundled product offering option attribute between {@link ProductData} and {@link ProductWsDTO}
 *
 * @since 2011
 */
public class TmaPoBpoOptionAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{
	private final MapperFacade mapperFacade;

	public TmaPoBpoOptionAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		if (source.getBundledProdOfferOption() != null)
		{
			target.setBundledProductOfferingOption(getMapperFacade().map(source.getBundledProdOfferOption(),
					BundledProductOfferingOptionWsDto.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
