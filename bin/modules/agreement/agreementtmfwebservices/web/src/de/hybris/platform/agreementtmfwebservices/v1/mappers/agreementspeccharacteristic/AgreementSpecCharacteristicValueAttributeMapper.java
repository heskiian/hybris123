/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspeccharacteristic;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristic;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristicValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreementSpecCharacteristicValue attribute between
 * {@link AgrAgreementSpecCharacteristicModel} and
 * {@link AgreementSpecCharacteristic}
 *
 * @since 2108
 */
public class AgreementSpecCharacteristicValueAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecCharacteristicModel, AgreementSpecCharacteristic>
{
	private final MapperFacade mapperFacade;
	private final AgrGenericService agrGenericService;

	public AgreementSpecCharacteristicValueAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecCharacteristicModel source,
			final AgreementSpecCharacteristic target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementSpecCharacteristicValues()))
		{
			return;
		}

		final List<AgreementSpecCharacteristicValue> specCharacteristicValues = source.getAgreementSpecCharacteristicValues()
				.stream()
				.map(specCharacteristicValue -> getMapperFacade().map(specCharacteristicValue, AgreementSpecCharacteristicValue.class,
						context))
				.collect(Collectors.toList());

		target.setSpecCharacteristicValue(specCharacteristicValues);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecCharacteristic target,
			final AgrAgreementSpecCharacteristicModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getSpecCharacteristicValue()))
		{
			return;
		}

		source.setAgreementSpecCharacteristicValues(getAllSpecCharacteristicValues(target.getSpecCharacteristicValue(), context));
	}

	private Set<AgrAgreementSpecCharacteristicValueModel> getAllSpecCharacteristicValues(
			final List<AgreementSpecCharacteristicValue> agreementSpecCharacteristicValues, final MappingContext context)
	{
		final Set<AgrAgreementSpecCharacteristicValueModel> result = new HashSet<>();
		agreementSpecCharacteristicValues.forEach(agreementSpecCharacteristicValue -> {

			final AgrAgreementSpecCharacteristicValueModel agreementSpecCharacteristicValueModel = (AgrAgreementSpecCharacteristicValueModel) getAgrGenericService()
					.createItem(AgrAgreementSpecCharacteristicValueModel.class);

			getMapperFacade().map(agreementSpecCharacteristicValue, agreementSpecCharacteristicValueModel, context);
			getAgrGenericService().saveItem(agreementSpecCharacteristicValueModel);
			result.add(agreementSpecCharacteristicValueModel);
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
