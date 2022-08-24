/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.characteristic;


import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharValueUseModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristicValue;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for value attribute between {@link AgrAgreementSpecCharValueUseModel} and {@link Characteristic}
 *
 * @since 2108
 */
public class CharacteristicValueAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharValueUseModel, Characteristic>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public CharacteristicValueAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharValueUseModel source, final Characteristic target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getAgreementSpecCharValue()))
		{
			return;
		}

		target.setValue(
				getMapperFacade().map(source.getAgreementSpecCharValue(), AgreementSpecCharacteristicValue.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Characteristic target, final AgrAgreementSpecCharValueUseModel source,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(target.getValue()))
		{
			return;
		}

		final AgrAgreementSpecCharacteristicValueModel agreementSpecCharacteristicValueModel = (AgrAgreementSpecCharacteristicValueModel) getAgrGenericService()
				.createItem(AgrAgreementSpecCharacteristicValueModel.class);

		getMapperFacade().map(target.getValue(), agreementSpecCharacteristicValueModel, context);
		getAgrGenericService().saveItem(agreementSpecCharacteristicValueModel);
		source.setAgreementSpecCharValue(agreementSpecCharacteristicValueModel);
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
