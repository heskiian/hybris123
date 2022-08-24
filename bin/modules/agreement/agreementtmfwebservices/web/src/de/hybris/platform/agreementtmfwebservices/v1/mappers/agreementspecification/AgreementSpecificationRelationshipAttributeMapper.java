/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementRelationshipModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationRelationship;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for specificationRelationship attribute between {@link AgrAgreementSpecificationModel}
 * and
 * {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationRelationshipAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	private final MapperFacade mapperFacade;
	private final AgrGenericService agrGenericService;

	public AgreementSpecificationRelationshipAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source,
			final AgreementSpecification target, final MappingContext context)
	{
		final List<AgreementSpecificationRelationship> specificationRelationships = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(source.getAgreementSpecCharReferences()))
		{
			specificationRelationships.addAll(source.getAgreementSpecCharReferences().stream()
					.map(reference -> getMapperFacade().map(reference, AgreementSpecificationRelationship.class, context))
					.collect(Collectors.toList()));
		}

		if (CollectionUtils.isNotEmpty(specificationRelationships))
		{
			target.setSpecificationRelationship(specificationRelationships);
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final
	AgrAgreementSpecificationModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getSpecificationRelationship()))
		{
			return;
		}

		source.setAgreementSpecCharReferences(getAllSpecRelations(source, target.getSpecificationRelationship(), context));
	}

	private Set<AgrAgreementRelationshipModel> getAllSpecRelations(
			final AgrAgreementSpecificationModel agreementSpecificationModel,
			final List<AgreementSpecificationRelationship> relationships, final MappingContext context)
	{
		final Set<AgrAgreementRelationshipModel> result = new HashSet<>();
		relationships.forEach(relationship -> {
			AgrAgreementRelationshipModel agreementRelationshipModel = (AgrAgreementRelationshipModel) getAgrGenericService()
					.getItem(AgrAgreementRelationshipModel._TYPECODE, relationship.getId());

			if (agreementRelationshipModel == null)
			{
				agreementRelationshipModel = (AgrAgreementRelationshipModel) getAgrGenericService()
						.createItem(AgrAgreementRelationshipModel.class);
			}

			getMapperFacade().map(relationship, agreementRelationshipModel, context);
			agreementRelationshipModel.setSourceAgreementSpecification(agreementSpecificationModel);
			getAgrGenericService().saveItem(agreementRelationshipModel);
			result.add(agreementRelationshipModel);
		});
		return result;
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
