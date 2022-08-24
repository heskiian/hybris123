/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.model.AgrCategoryModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.CategoryRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for serviceCategory attribute between {@link AgrAgreementSpecificationModel}
 * and
 * {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationServiceCategoryAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	private final MapperFacade mapperFacade;
	private final AgrGenericService agrGenericService;

	public AgreementSpecificationServiceCategoryAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source, final AgreementSpecification target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getServiceCategory()))
		{
			return;
		}

		final CategoryRef category = getMapperFacade().map(source.getServiceCategory(), CategoryRef.class, context);
		target.setServiceCategory(category);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final AgrAgreementSpecificationModel source,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(target.getServiceCategory()))
		{
			return;
		}

		AgrCategoryModel categoryModel = (AgrCategoryModel) getAgrGenericService()
				.getItem(AgrCategoryModel._TYPECODE, target.getServiceCategory().getId());

		if (categoryModel == null)
		{
			categoryModel = (AgrCategoryModel) getAgrGenericService()
					.createItem(AgrCategoryModel.class);
		}

		getMapperFacade().map(target.getServiceCategory(), categoryModel, context);
		getAgrGenericService().saveItem(categoryModel);
		source.setServiceCategory(categoryModel);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}

}
