/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.CartModificationPopulator;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;

import org.apache.commons.lang.StringUtils;


/**
 * Populator implementation for {@link CommerceCartModification} as source and {@link CartModificationData} as target.
 * Extends {@link CartModificationPopulator}
 * Populates all the attributes of CartModificationData from CommerceCartModification
 *
 * @since 1911
 */
public class TmaCartModificationPopulator extends CartModificationPopulator
{
	@Override
	public void populate(final CommerceCartModification source, final CartModificationData target)
	{
		super.populate(source, target);

		if (StringUtils.isNotEmpty(source.getClonedCartId()))
		{
			target.setClonedCartId(source.getClonedCartId());
		}

		if (source.getParentEntry() != null)
		{
			target.setParentEntryNumber(source.getParentEntry().getEntryNumber());
		}
	}
}
