/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.taxdefinition;

import de.hybris.platform.partyservices.model.PmTaxDefinitionModel;
import de.hybris.platform.partytmfwebservices.v1.dto.TaxDefinition;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmTaxDefinitionModel} and {@link TaxDefinition}
 *
 * @since 2108
 */
public class TaxDefinitionAtTypeAttributeMapper extends PmAttributeMapper<PmTaxDefinitionModel, TaxDefinition>
{
	public TaxDefinitionAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmTaxDefinitionModel source, final TaxDefinition target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}
