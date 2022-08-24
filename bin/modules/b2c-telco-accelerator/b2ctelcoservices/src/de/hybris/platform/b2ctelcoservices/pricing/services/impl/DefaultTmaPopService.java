/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.GenericSearchDao;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultGenericSearchDao;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopQualifier;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopService;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link TmaPopService}.
 *
 * @since 2011
 */
public class DefaultTmaPopService implements TmaPopService
{
	private List<TmaPopQualifier> popQualifiers;
	private GenericSearchDao tmaPopSearchDao;

	public DefaultTmaPopService(final List<TmaPopQualifier> popQualifiers,
			final DefaultGenericSearchDao tmaPopSearchDao)
	{
		this.popQualifiers = popQualifiers;
		this.tmaPopSearchDao = tmaPopSearchDao;
	}

	@Override
	public boolean popQualifiesFor(final TmaProductOfferingPriceModel price,
			final TmaProductOfferingModel productOffering)
	{
		validateParameterNotNull(price, "price should not be null");
		validateParameterNotNull(productOffering, "product offering should not be null");

		for (TmaPopQualifier poPriceCondition : popQualifiers)
		{
			if (!poPriceCondition.qualifies(price, productOffering))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<PriceRowModel> getPriceRowsFor(final TmaProductOfferingPriceModel price)
	{
		final Set<PriceRowModel> priceRows = new HashSet<>();
		findPriceRowsFor(price, priceRows);
		return priceRows;
	}

	public TmaProductOfferingPriceModel getPop(final String code)
	{
		validateParameterNotNull(code, "Parameter code can not be null");
		final Map parameters = new HashMap();
		parameters.put(TmaProductOfferingPriceModel.ID, code);
		return (TmaProductOfferingPriceModel) getGenericSearchDao().findUnique(parameters);
	}

	private void findPriceRowsFor(final TmaProductOfferingPriceModel price, final Set<PriceRowModel> priceRows)
	{
		validateParameterNotNull(price, "Price can not be null");
		validateParameterNotNull(priceRows, "Price rows can not be null");

		if (CollectionUtils.isNotEmpty(price.getPriceRows()))
		{
			priceRows.addAll(price.getPriceRows());
		}
		if (CollectionUtils.isNotEmpty(price.getCompositeProdOfferPrices()))
		{
			price.getCompositeProdOfferPrices().forEach(parentPrice -> findPriceRowsFor(parentPrice, priceRows));
		}
	}

	protected List<TmaPopQualifier> getPopQualifiers()
	{
		return popQualifiers;
	}

	protected GenericSearchDao getGenericSearchDao()
	{
		return tmaPopSearchDao;
	}
}
