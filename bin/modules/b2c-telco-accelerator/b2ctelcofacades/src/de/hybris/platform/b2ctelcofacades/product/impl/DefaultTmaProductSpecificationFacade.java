/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductSpecificationFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductSpecificationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;


/**
 * Default implementation of {@link TmaProductSpecificationFacade}.
 *
 * @since 2102
 */
public class DefaultTmaProductSpecificationFacade implements TmaProductSpecificationFacade
{
	private TmaProductSpecificationService productSpecificationService;
	private Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> productSpecificationConverterMap;

	public DefaultTmaProductSpecificationFacade(final TmaProductSpecificationService productSpecificationService,
			final Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> productSpecificationConverterMap)
	{
		this.productSpecificationService = productSpecificationService;
		this.productSpecificationConverterMap = productSpecificationConverterMap;
	}

	@Override
	public TmaProductSpecificationData getProductSpecification(final String id)
	{
		final TmaProductSpecificationModel productSpecificationModel = getProductSpecificationService().getProductSpecification(id);
		return getProductSpecificationConverterMap().get(productSpecificationModel.getItemtype())
				.convert(productSpecificationModel);
	}

	protected TmaProductSpecificationService getProductSpecificationService()
	{
		return productSpecificationService;
	}

	protected Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> getProductSpecificationConverterMap()
	{
		return productSpecificationConverterMap;
	}
}
