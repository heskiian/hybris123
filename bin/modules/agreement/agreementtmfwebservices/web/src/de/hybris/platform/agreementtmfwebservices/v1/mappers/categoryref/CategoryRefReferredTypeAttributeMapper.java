/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.categoryref;

import de.hybris.platform.agreementservices.model.AgrCategoryModel;
import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.CategoryRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referredType attribute between {@link AgrCategoryModel} and
 * {@link CategoryRef}
 *
 * @since 2108
 */
public class CategoryRefReferredTypeAttributeMapper
		extends AgrAttributeMapper<AgrCategoryModel, CategoryRef>
{
	public CategoryRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrCategoryModel source, final CategoryRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(AgreementtmfwebservicesConstants.CATEGORY_REF_DEFAULT_REFERRED));
		}
	}
}
