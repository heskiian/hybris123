/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.BundledProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundledPo attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoBundledPoAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;
	private TmaProductOfferFacade productOfferFacade;


	public PoBundledPoAttributeMapper(final MapperFacade mapperFacade, final TmaProductOfferFacade productOfferFacade)
	{
		this.mapperFacade = mapperFacade;
		this.productOfferFacade = productOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (!getProductOfferFacade().isBpo(source) || source.getChildren() == null)
		{
			return;
		}

		final List<BundledProductOffering> bundledProductOfferings = new ArrayList<>();
		source.getChildren().forEach(productData ->
		{
			final BundledProductOffering bundledProductOfferingWsDto = getMapperFacade()
					.map(productData, BundledProductOffering.class, context);
			bundledProductOfferings.add(bundledProductOfferingWsDto);

		});

		target.setBundledProductOffering(bundledProductOfferings);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected TmaProductOfferFacade getProductOfferFacade()
	{
		return productOfferFacade;
	}
}
