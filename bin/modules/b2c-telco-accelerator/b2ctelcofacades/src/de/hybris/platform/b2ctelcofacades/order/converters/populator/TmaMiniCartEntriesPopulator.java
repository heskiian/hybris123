/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the count of all entries and the list of last added/modified item(s)
 *
 * @since 6.5
 */
public class TmaMiniCartEntriesPopulator implements Populator<CartModel, CartData>
{
	private Converter<AbstractOrderEntryModel, OrderEntryData> telcoOrderEntryConverter;

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		if (source != null)
		{
			target.setEntries(getEntries(source, target));
		}
	}

	/**
	 * Return the cart entries that will be displayed in the mini cart.
	 *
	 * @param source
	 * 		order model
	 * @param target
	 * 		order data
	 * @return list containing last modified entries
	 */
	protected List<OrderEntryData> getEntries(final AbstractOrderModel source, final AbstractOrderData target)
	{
		return CollectionUtils.isEmpty(target.getEntries())
				? getTelcoOrderEntryConverter().convertAll(source.getEntries()) : target.getEntries();
	}

	protected Converter<AbstractOrderEntryModel, OrderEntryData> getTelcoOrderEntryConverter()
	{
		return telcoOrderEntryConverter;
	}

	@Required
	public void setTelcoOrderEntryConverter(final Converter<AbstractOrderEntryModel, OrderEntryData> telcoOrderEntryConverter)
	{
		this.telcoOrderEntryConverter = telcoOrderEntryConverter;
	}
}
