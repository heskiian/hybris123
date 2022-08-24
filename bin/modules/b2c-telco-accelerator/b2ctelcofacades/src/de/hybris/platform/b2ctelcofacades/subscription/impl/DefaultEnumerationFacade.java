/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import de.hybris.platform.b2ctelcofacades.data.EnumerationValueData;
import de.hybris.platform.b2ctelcofacades.subscription.EnumerationFacade;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link EnumerationFacade}.
 *
 * @since 6.6
 */
public class DefaultEnumerationFacade implements EnumerationFacade
{
	private String enumerationTypeCode;
	private EnumerationService enumerationService;
	private Converter<HybrisEnumValue, EnumerationValueData> enumerationValueConverter;

	/**
	 * Initialize the type code of the enumeration for which the values will be retrieved
	 *
	 * @param enumerationTypeCode
	 * 		type code of the enumeration
	 */
	public DefaultEnumerationFacade(final String enumerationTypeCode)
	{
		this.enumerationTypeCode = enumerationTypeCode;
	}

	@Override
	public List<EnumerationValueData> getValues()
	{
		return getEnumerationValueConverter().convertAll(getEnumerationService().getEnumerationValues(enumerationTypeCode));
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected Converter<HybrisEnumValue, EnumerationValueData> getEnumerationValueConverter()
	{
		return enumerationValueConverter;
	}

	@Required
	public void setEnumerationValueConverter(
			Converter<HybrisEnumValue, EnumerationValueData> enumerationValueConverter)
	{
		this.enumerationValueConverter = enumerationValueConverter;
	}
}
