/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.partyrole;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndividualIdentification;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for identification id attribute between {@link TmaIdentificationData} and
 * {@link IndividualIdentification}
 *
 * @since 1911
 */
public class PartyRoleIdentificationIdAttributeMapper extends TmaAttributeMapper<TmaIdentificationData, IndividualIdentification>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaIdentificationData source,
			final IndividualIdentification target, final MappingContext context)
	{
		final String identificationNumber = source.getIdentificationNumber();

		if (StringUtils.isNotBlank(identificationNumber))
		{
			target.setIdentificationId(identificationNumber);
		}

	}
}
