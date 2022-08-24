/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * TmaPoIsBundleAttributeMapper populates value of isBundle attribute from {@link ProductData} to
 * {@link ProductWsDTO}
 *
 * @since 2011
 */
public class TmaPoIsBundleAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{
	private TmaProductOfferFacade productOfferFacade;

	public TmaPoIsBundleAttributeMapper(final TmaProductOfferFacade productOfferFacade)
	{
		this.productOfferFacade = productOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
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