/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementitem;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.agreementservices.model.AgrAgreementTermOrConditionModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementItem;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementTermOrCondition;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for term or condition attribute between {@link AgrAgreementItemModel} and {@link AgreementItem}
 *
 * @since 2108
 */
public class AgreementItemTermOrConditionAttributeMapper extends AgrAttributeMapper<AgrAgreementItemModel, AgreementItem>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementItemTermOrConditionAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementItemModel source, final AgreementItem target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementTermsOrConditions()))
		{
			return;
		}

		final List<AgreementTermOrCondition> agreementItemList = source.getAgreementTermsOrConditions().stream()
				.map(termOrConditionModel -> getMapperFacade().map(termOrConditionModel, AgreementTermOrCondition.class, context))
				.collect(Collectors.toList());

		target.setTermOrCondition(agreementItemList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementItem target, final AgrAgreementItemModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getTermOrCondition()))
		{
			return;
		}

		source.setAgreementTermsOrConditions(getAllTermsAndConditions(target.getTermOrCondition(), context));
	}

	private Set<AgrAgreementTermOrConditionModel> getAllTermsAndConditions(
			final List<AgreementTermOrCondition> agreementTermOrConditions, final MappingContext context)
	{
		final Set<AgrAgreementTermOrConditionModel> result = new HashSet<>();
		agreementTermOrConditions.forEach(agreementTermOrCondition -> {
			AgrAgreementTermOrConditionModel agreementTermOrConditionModel = (AgrAgreementTermOrConditionModel) getAgrGenericService()
					.getItem(AgrAgreementTermOrConditionModel._TYPECODE, agreementTermOrCondition.getId());
			if (agreementTermOrConditionModel == null)
			{
				agreementTermOrConditionModel = (AgrAgreementTermOrConditionModel) getAgrGenericService()
						.createItem(AgrAgreementTermOrConditionModel.class);
			}
			getMapperFacade().map(agreementTermOrCondition, agreementTermOrConditionModel, context);
			getAgrGenericService().saveItem(agreementTermOrConditionModel);
			result.add(agreementTermOrConditionModel);
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
