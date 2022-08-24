/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementItem;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreement item attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementAgrItemAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementAgrItemAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source, final Agreement target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementItems()))
		{
			return;
		}

		final List<AgreementItem> agreementItemList = source.getAgreementItems().stream()
				.map(itemModel -> getMapperFacade().map(itemModel, AgreementItem.class, context))
				.collect(Collectors.toList());

		target.setAgreementItem(agreementItemList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAgreementItem()))
		{
			return;
		}

		source.setAgreementItems(getAllAgreementItems(target.getAgreementItem(), context));
	}

	private Set<AgrAgreementItemModel> getAllAgreementItems(final List<AgreementItem> agreementItems, final MappingContext context)
	{
		final Set<AgrAgreementItemModel> result = new HashSet<>();
		agreementItems.forEach(agreementItem -> {

			final AgrAgreementItemModel agreementItemModel = (AgrAgreementItemModel) getAgrGenericService()
					.createItem(AgrAgreementItemModel.class);

			getMapperFacade().map(agreementItem, agreementItemModel, context);
			getAgrGenericService().saveItem(agreementItemModel);
			result.add(agreementItemModel);
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
