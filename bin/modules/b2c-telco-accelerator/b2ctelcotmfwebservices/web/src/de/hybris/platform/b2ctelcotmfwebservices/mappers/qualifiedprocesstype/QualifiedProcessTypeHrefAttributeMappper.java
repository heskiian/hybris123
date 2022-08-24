/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.qualifiedprocesstype;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for href attribute between {@link PrincipalData} and
 * {@link QualifiedProcessType}
 *
 * @since 1907
 */
public class QualifiedProcessTypeHrefAttributeMappper  extends TmaAttributeMapper<TmaProcessType, QualifiedProcessType>
{
	@Override
	public void populateTargetAttributeFromSource(TmaProcessType source, QualifiedProcessType target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.TMA_QUALIFIED_PROCESS_TYPES_API_URL + source.getCode());
		}
	}
}
