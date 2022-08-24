/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.EnumerationValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.HybrisEnumValue;


/**
 * Populates the target {@link EnumerationValueData} from a {@link HybrisEnumValue}.
 *
 * @since 6.6
 */
public class EnumerationValuePopulator implements Populator<HybrisEnumValue, EnumerationValueData>
{
	@Override
	public void populate(final HybrisEnumValue source, final EnumerationValueData target)
	{
		target.setCode(source.getCode());
	}
}
