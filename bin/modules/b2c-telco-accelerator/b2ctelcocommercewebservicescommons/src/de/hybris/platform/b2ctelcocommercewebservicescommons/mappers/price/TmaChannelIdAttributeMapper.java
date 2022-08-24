/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ChannelWsDTO;
import de.hybris.platform.europe1.enums.PriceRowChannel;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ID attribute between {@link PriceRowChannel} and {@link ChannelWsDTO}
 *
 * @since 1907
 */
public class TmaChannelIdAttributeMapper extends TmaAttributeMapper<PriceRowChannel, ChannelWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceRowChannel source, final ChannelWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}
