/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.daos.SpiProductDao;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link SpiProductService}.
 *
 * @since 2105
 */
public class DefaultSpiProductService implements SpiProductService
{
	private SpiProductDao spiProductDao;
	private ModelService modelService;

	public DefaultSpiProductService(final SpiProductDao spiProductDao, final ModelService modelService)
	{
		this.spiProductDao = spiProductDao;
		this.modelService = modelService;
	}

	@Override
	public SpiProductModel getProduct(final String id)
	{
		validateParameterNotNull(id, "Parameter id can not be null");
		final Map parameters = new HashMap();
		parameters.put(SpiProductModel.ID, id);
		return getSpiProductDao().findUnique(parameters);
	}

	@Override
	public List<SpiProductModel> getProducts(final SpiProductContext spiProductContext, final Integer offset, final Integer limit)
	{
		return getSpiProductDao().getProducts(spiProductContext, offset, limit);
	}

	@Override
	public Integer getNumberOfProductsFor(final SpiProductContext spiProductContext)
	{
		return getSpiProductDao().getNumberOfProductsFor(spiProductContext);
	}

	@Override
	public void saveProduct(final SpiProductModel product)
	{
		getModelService().save(product);
		getModelService().refresh(product);
	}

	@Override
	public SpiProductModel createProduct(final Class productClass)
	{
		return getModelService().create(productClass);
	}

	@Override
	public void removeProduct(final SpiProductModel product)
	{
		getModelService().remove(product);
	}

	protected SpiProductDao getSpiProductDao()
	{
		return spiProductDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
