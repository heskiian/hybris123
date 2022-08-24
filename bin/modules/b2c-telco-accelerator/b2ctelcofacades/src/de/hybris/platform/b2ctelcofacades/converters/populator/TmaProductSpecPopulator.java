/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates information from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}.
 *
 * @since 1903
 */
public class TmaProductSpecPopulator implements Populator<TmaProductSpecificationModel, TmaProductSpecificationData>
{
	@Override
	public void populate(final TmaProductSpecificationModel source, final TmaProductSpecificationData target)
	{
		if (source == null)
		{
			return;
		}

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setBrand(source.getBrand());
		target.setLifecycleStatus(source.getApprovalStatus());
		target.setOnlineDate(source.getOnlineDate());
		target.setOfflineDate(source.getOfflineDate());
	}
}
