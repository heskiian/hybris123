/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecCharacteristic;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for specification characteristic attribute between {@link AgrAgreementSpecificationModel} and {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationSpecCharacteristicsAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	private final MapperFacade mapperFacade;
	private final AgrGenericService agrGenericService;

	public AgreementSpecificationSpecCharacteristicsAttributeMapper(final String sourceAttributeName,
			final String targetAttributeName, final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source, final AgreementSpecification target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementSpecCharacteristics()))
		{
			return;
		}

		final List<AgreementSpecCharacteristic> specificationCharacteristics = source.getAgreementSpecCharacteristics().stream()
				.map(characteristic -> getMapperFacade().map(characteristic, AgreementSpecCharacteristic.class, context))
				.collect(Collectors.toList());

		target.setSpecificationCharacteristic(specificationCharacteristics);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementSpecification target, final AgrAgreementSpecificationModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getSpecificationCharacteristic()))
		{
			return;
		}

		source.setAgreementSpecCharacteristics(getAllSpecCharacteristics(target.getSpecificationCharacteristic(), context));
	}

	private Set<AgrAgreementSpecCharacteristicModel> getAllSpecCharacteristics(
			final List<AgreementSpecCharacteristic> characteristics, final MappingContext context)
	{
		final Set<AgrAgreementSpecCharacteristicModel> result = new HashSet<>();
		characteristics.forEach(characteristic -> {

			final Map parameters = new HashMap();
			parameters.put(AgrAgreementSpecCharacteristicModel.NAME, characteristic.getName());
			AgrAgreementSpecCharacteristicModel agreementSpecCharacteristicModel = (AgrAgreementSpecCharacteristicModel) getAgrGenericService()
					.getItem(AgrAgreementSpecCharacteristicModel._TYPECODE, parameters);

			if (agreementSpecCharacteristicModel == null)
			{
				agreementSpecCharacteristicModel = (AgrAgreementSpecCharacteristicModel) getAgrGenericService()
						.createItem(AgrAgreementSpecCharacteristicModel.class);
			}

			getMapperFacade().map(characteristic, agreementSpecCharacteristicModel, context);
			getAgrGenericService().saveItem(agreementSpecCharacteristicModel);
			result.add(agreementSpecCharacteristicModel);
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
