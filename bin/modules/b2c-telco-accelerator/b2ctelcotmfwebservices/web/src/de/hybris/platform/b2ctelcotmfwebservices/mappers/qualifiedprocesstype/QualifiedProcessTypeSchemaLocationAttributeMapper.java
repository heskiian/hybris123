/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.qualifiedprocesstype;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.QualifiedProcessType;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link PrincipalData} and
 * {@link QualifiedProcessType}
 *
 * @since 1907
 */
public class QualifiedProcessTypeSchemaLocationAttributeMapper extends TmaAttributeMapper<PrincipalData, QualifiedProcessType>
{
	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, QualifiedProcessType target, MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
