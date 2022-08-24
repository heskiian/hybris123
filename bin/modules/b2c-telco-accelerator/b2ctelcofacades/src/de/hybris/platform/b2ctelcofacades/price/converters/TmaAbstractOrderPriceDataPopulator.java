/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.converters.Populator;

import org.apache.commons.lang.StringUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderPriceDataPopulator<SOURCE extends TmaAbstractOrderPriceModel, TARGET extends TmaAbstractOrderPriceData>
		implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		target.setId(source.getId());

		if (StringUtils.isNotBlank(source.getCatalogCode()))
		{
			target.setPriceId(source.getCatalogCode());
		}

		if (StringUtils.isNotBlank(source.getName()))
		{
			target.setName(source.getName());
		}
	}
}
