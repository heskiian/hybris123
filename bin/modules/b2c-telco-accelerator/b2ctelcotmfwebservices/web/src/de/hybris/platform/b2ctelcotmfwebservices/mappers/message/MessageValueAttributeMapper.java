/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.message;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Message;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps value of message cart between {@link String} and {@link Message}
 *
 * @since 1907
 */
public class MessageValueAttributeMapper extends TmaAttributeMapper<String, Message>
{
	@Override
	public void populateTargetAttributeFromSource(String source, Message target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source))
		{
			target.setValue(source);
		}
	}
}
