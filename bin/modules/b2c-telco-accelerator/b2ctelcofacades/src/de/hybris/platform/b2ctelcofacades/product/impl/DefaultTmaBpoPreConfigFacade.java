/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaBpoPreConfigData;
import de.hybris.platform.b2ctelcofacades.product.TmaBpoPreConfigFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBpoPreConfigService;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Default implementation for {@link TmaBpoPreConfigFacade}}
 *
 * @since 6.7
 */
public class DefaultTmaBpoPreConfigFacade implements TmaBpoPreConfigFacade
{

	private TmaBpoPreConfigService tmaBpoPreConfigService;
	private Converter<TmaBpoPreConfigModel, TmaBpoPreConfigData> bpoPreConfigDataConverter;

	@Override
	public TmaBpoPreConfigData getPreConfigBpo(final String preConfigCode)
	{
		return this.bpoPreConfigDataConverter.convert(getTmaBpoPreConfigService().getBpoPreConfigForCode(preConfigCode));
	}

	public TmaBpoPreConfigService getTmaBpoPreConfigService()
	{
		return tmaBpoPreConfigService;
	}

	public void setTmaBpoPreConfigService(final TmaBpoPreConfigService tmaBpoPreConfigService)
	{
		this.tmaBpoPreConfigService = tmaBpoPreConfigService;
	}

	public Converter<TmaBpoPreConfigModel, TmaBpoPreConfigData> getBpoPreConfigDataConverter()
	{
		return bpoPreConfigDataConverter;
	}

	public void setBpoPreConfigDataConverter(final Converter<TmaBpoPreConfigModel, TmaBpoPreConfigData> bpoPreConfigDataConverter)
	{
		this.bpoPreConfigDataConverter = bpoPreConfigDataConverter;
	}

}
