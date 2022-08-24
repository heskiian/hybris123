/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contactmedium;

import de.hybris.platform.billingaccountservices.model.BaContactMediumModel;
import de.hybris.platform.billingaccountservices.model.BaMediumCharacteristicModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.MediumCharacteristic;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for characteristic attribute between {@link BaContactMediumModel} and {@link ContactMedium}
 *
 * @since 2105
 */
public class ContactMediumMedCharacContTypeAttributeMapper extends BaAttributeMapper<BaContactMediumModel, ContactMedium>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public ContactMediumMedCharacContTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactMediumModel source, final ContactMedium target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getCharacteristic()))
		{
			return;
		}

		target.setCharacteristic(getMapperFacade().map(source.getCharacteristic(), MediumCharacteristic.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final ContactMedium target, final BaContactMediumModel source,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(target.getCharacteristic()))
		{
			return;
		}

		final BaMediumCharacteristicModel baMediumCharacteristicModel = (BaMediumCharacteristicModel) getBaGenericService()
				.createItem(BaMediumCharacteristicModel.class);
		getMapperFacade().map(target.getCharacteristic(), baMediumCharacteristicModel, context);
		source.setCharacteristic(baMediumCharacteristicModel);
	}


	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
