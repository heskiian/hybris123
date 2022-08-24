/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.partyrole;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PartyRole;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link PrincipalData} and {@link PartyRole}
 *
 * @since 1907
 */
public class PartyRoleNameAttributeMapper extends TmaAttributeMapper<PrincipalData, PartyRole>
{
	@Override
	public void populateTargetAttributeFromSource(PrincipalData source, PartyRole target,
			MappingContext context)
	{
		if (source.getName() != null)
		{
			target.setName(source.getName());
		}
	}
}
