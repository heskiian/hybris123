/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.categoryref;

import de.hybris.platform.agreementservices.model.AgrCategoryModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.CategoryRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link AgrCategoryModel} and
 * {@link CategoryRef}
 *
 * @since 2108
 */
public class CategoryRefSchemaLocationAttributeMapper
		extends AgrAttributeMapper<AgrCategoryModel, CategoryRef>
{
	public CategoryRefSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrCategoryModel source, final CategoryRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(AgreementtmfwebservicesConstants.AGR_API_SCHEMA);
		}
	}
}
