/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.purchaseflow.impl;

import de.hybris.platform.b2ctelcofacades.purchaseflow.TmaProcessTypeFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;




/**
 * Default implementation of process type facade.
 *
 * @since 6.7
 */
public class DefaultTmaProcessTypeFacadeImpl implements TmaProcessTypeFacade
{
	private EnumerationService enumerationService;

	@Override
	public List<String> getProcessTypeNamesFromEnums(final Collection<TmaProcessType> processTypesEnumCollection)
	{
		final List<String> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(processTypesEnumCollection))
		{
			return result;
		}

		processTypesEnumCollection.forEach(tmaProcessType -> result.add(tmaProcessType.getCode()));

		return result;
	}

	@Override
	public TmaProcessType getProcessType(final String code)
	{
		return getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE,code);
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
}
