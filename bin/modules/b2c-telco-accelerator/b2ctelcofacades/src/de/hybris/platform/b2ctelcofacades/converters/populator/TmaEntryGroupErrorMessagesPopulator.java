/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populator for {@link EntryGroup} error messages attribute
 *
 * @since 1911
 * @deprecated since 1911, use {@link TmaEntryGroupValidationMessagesPopulator} instead
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaEntryGroupErrorMessagesPopulator implements Populator<EntryGroup, EntryGroupData>
{
	@Override
	public void populate(EntryGroup entryGroup, EntryGroupData entryGroupData) throws ConversionException
	{
		entryGroupData.setErrorMessages(entryGroup.getErrorMessages());
	}
}
