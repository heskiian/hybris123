/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.BpoData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.converters.Populator;


/**
 * Populator for {@link BpoData}.
 *
 * @since 6.7
 */
public class TmaBpoPopulator implements Populator<TmaBundledProductOfferingModel, BpoData>
{

	@Override
	public void populate(final TmaBundledProductOfferingModel source, final BpoData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);
		target.setId(source.getCode());
		target.setName(source.getName());
	}
}
