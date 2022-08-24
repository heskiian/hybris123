/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.converters.Populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator implementation for {@link TmaExternalIdentifierModel} as source and {@link TmaExternalIdentifierData} as target type.
 *
 * @since 2102
 */
public class TmaExternalIdentifierPopulator implements Populator<TmaExternalIdentifierModel, TmaExternalIdentifierData>
{
	@Override
	public void populate(final TmaExternalIdentifierModel source, final TmaExternalIdentifierData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		target.setId(source.getBillingSystemExtId());
		target.setOwner(source.getBillingSystemId());
		target.setResourceType(source.getResourceType());
	}
}