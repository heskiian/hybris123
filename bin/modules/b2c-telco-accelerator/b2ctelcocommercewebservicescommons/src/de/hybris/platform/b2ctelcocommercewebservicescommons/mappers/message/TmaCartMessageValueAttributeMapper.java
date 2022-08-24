/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.message;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MessageWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps the error message between {@link TmaCartValidationData} and {@link MessageWsDTO}.
 *
 * @since 1911
 */
public class TmaCartMessageValueAttributeMapper extends TmaAttributeMapper<TmaCartValidationData, MessageWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaCartValidationData source, final MessageWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (StringUtils.isNotEmpty(source.getMessage()))
		{
			target.setValue(source.getMessage());
		}
	}
}
