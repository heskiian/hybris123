/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;

import ma.glasnost.orika.MappingContext;

import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoIsBundleAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private TmaProductOfferFacade productOfferFacade;

	public PoIsBundleAttributeMapper(final TmaProductOfferFacade productOfferFacade)
	{
		this.productOfferFacade = productOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setIsBundle(getProductOfferFacade().isBpo(source));
		}
	}

	protected TmaProductOfferFacade getProductOfferFacade()
	{
		return productOfferFacade;
	}
}
