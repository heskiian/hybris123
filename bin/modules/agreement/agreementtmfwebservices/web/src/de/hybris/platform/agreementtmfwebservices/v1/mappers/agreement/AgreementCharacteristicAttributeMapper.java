/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharValueUseModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Characteristic;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for characteristic attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementCharacteristicAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementCharacteristicAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getAgreementSpecCharacteristicValues()))
		{
			return;
		}

		final List<Characteristic> characteristicList = source.getAgreementSpecCharacteristicValues().stream()
				.map(valueModel -> getMapperFacade().map(valueModel, Characteristic.class, context))
				.collect(Collectors.toList());

		target.setCharacteristic(characteristicList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getCharacteristic()))
		{
			return;
		}

		source.setAgreementSpecCharacteristicValues(getAllCharacteristics(target.getCharacteristic(), context));
	}

	private Set<AgrAgreementSpecCharValueUseModel> getAllCharacteristics(final List<Characteristic> characteristics,
			final MappingContext context)
	{
		final Set<AgrAgreementSpecCharValueUseModel> result = new HashSet<>();
		characteristics.forEach(characteristic -> {

			final AgrAgreementSpecCharValueUseModel agreementSpecCharValueUseModel = (AgrAgreementSpecCharValueUseModel) getAgrGenericService()
					.createItem(AgrAgreementSpecCharValueUseModel.class);

			getMapperFacade().map(characteristic, agreementSpecCharValueUseModel, context);
			getAgrGenericService().saveItem(agreementSpecCharValueUseModel);
			result.add(agreementSpecCharValueUseModel);
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
