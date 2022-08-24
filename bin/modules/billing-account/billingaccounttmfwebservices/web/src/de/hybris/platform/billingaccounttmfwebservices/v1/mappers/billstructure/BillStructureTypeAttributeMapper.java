/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billstructure;

import de.hybris.platform.billingaccountservices.model.BaBillStructureModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillStructure;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link BaBillStructureModel} and
 * {@link BillStructure}
 *
 * @since 2105
 */
public class BillStructureTypeAttributeMapper
		extends BaAttributeMapper<BaBillStructureModel, BillStructure>
{
	public BillStructureTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillStructureModel source, final BillStructure target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
