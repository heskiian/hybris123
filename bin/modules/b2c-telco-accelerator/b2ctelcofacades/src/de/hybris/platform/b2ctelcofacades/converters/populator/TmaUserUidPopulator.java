/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;

import org.springframework.util.Assert;


/**
 * Populates the {@link TmaUserData} uid field using the {@link UserModel} uid field
 *
 * @since 1810
 */
public class TmaUserUidPopulator implements Populator<UserModel, TmaUserData>
{
	@Override
	public void populate(final UserModel source, final TmaUserData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setUid(source.getUid());
	}
}
