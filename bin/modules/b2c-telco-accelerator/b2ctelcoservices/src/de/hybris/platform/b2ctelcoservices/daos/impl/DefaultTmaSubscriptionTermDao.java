/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaGenericSearchProcessor;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionTermDao;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaSubscriptionTermDao} which supports query builders for filtering entities.
 *
 * @since 1810
 */
public class DefaultTmaSubscriptionTermDao extends DefaultGenericDao<SubscriptionTermModel> implements TmaSubscriptionTermDao
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTmaSubscriptionTermDao.class);

	private TmaGenericSearchProcessor subscriptionTermProcessor;
	private GenericSearchService genericSearchService;

	public DefaultTmaSubscriptionTermDao()
	{
		super(SubscriptionTermModel._TYPECODE);
	}

	@Override
	public List<SubscriptionTermModel> findAllSubscriptionTerms()
	{
		final GenericQuery query = new GenericQuery(SubscriptionTermModel._TYPECODE);
		try
		{
			getSubscriptionTermProcessor().enhanceQuery(query);
		}
		catch (TmaSearchQueryException exception)
		{
			LOG.debug("The search query cannot be enhanced... returning empty results!", exception);
			return Collections.emptyList();
		}
		return getGenericSearchService().<SubscriptionTermModel>search(query).getResult();
	}

	protected TmaGenericSearchProcessor getSubscriptionTermProcessor()
	{
		return subscriptionTermProcessor;
	}

	@Required
	public void setSubscriptionTermProcessor(TmaGenericSearchProcessor subscriptionTermProcessor)
	{
		this.subscriptionTermProcessor = subscriptionTermProcessor;
	}

	protected GenericSearchService getGenericSearchService()
	{
		return genericSearchService;
	}

	@Required
	public void setGenericSearchService(GenericSearchService genericSearchService)
	{
		this.genericSearchService = genericSearchService;
	}
}
