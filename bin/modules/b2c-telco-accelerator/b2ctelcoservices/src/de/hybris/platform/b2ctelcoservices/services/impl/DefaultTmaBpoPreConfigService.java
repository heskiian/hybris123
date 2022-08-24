/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.daos.TmaBpoPreConfigDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBpoPreConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaBpoPreConfigService}
 *
 * @since 6.7
 *
 */
public class DefaultTmaBpoPreConfigService implements TmaBpoPreConfigService
{
	private TmaBpoPreConfigDao tmaBpoPreConfigDao;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Override
	public TmaBpoPreConfigModel getBpoPreConfigForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");

		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		CatalogVersionModel catalogVersion = null;
		if (baseSite instanceof CMSSiteModel)
		{
			final CMSSiteModel cmsSite = (CMSSiteModel) getBaseSiteService().getCurrentBaseSite();
			catalogVersion = cmsSite.getDefaultCatalog().getActiveCatalogVersion();
		}
		return getTmaBpoPreConfigDao().findBpoPreconfigByCode(code, catalogVersion);
	}

	public TmaBpoPreConfigDao getTmaBpoPreConfigDao()
	{
		return tmaBpoPreConfigDao;
	}

	@Required
	public void setTmaBpoPreConfigDao(final TmaBpoPreConfigDao tmaBpoPreConfigDao)
	{
		this.tmaBpoPreConfigDao = tmaBpoPreConfigDao;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
