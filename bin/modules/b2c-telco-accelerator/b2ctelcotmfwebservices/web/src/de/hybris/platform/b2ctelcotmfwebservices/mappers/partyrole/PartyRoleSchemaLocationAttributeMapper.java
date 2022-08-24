/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.partyrole;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PartyRole;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link PrincipalData} and {@link PartyRole}
 *
 * @since 1907
 */
public class PartyRoleSchemaLocationAttributeMapper extends TmaAttributeMapper<PrincipalData, PartyRole>
{
	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, PartyRole target, MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
