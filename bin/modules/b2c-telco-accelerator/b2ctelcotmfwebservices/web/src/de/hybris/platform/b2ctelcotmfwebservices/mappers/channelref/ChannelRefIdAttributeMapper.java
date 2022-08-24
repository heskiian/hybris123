/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.channelref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChannelRef;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for ID attribute between {@link PriceRowChannel} and {@link ChannelRef}
 *
 * @since 1903
 */
public class ChannelRefIdAttributeMapper extends TmaAttributeMapper<PriceRowChannel, ChannelRef>
{

	@Override
	public void populateTargetAttributeFromSource(PriceRowChannel source, ChannelRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}
