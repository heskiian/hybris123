/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;



/**
 * Populator implementation for {@link AbstractOrderModel} as source and
 * {@link AbstractOrderData} as target type, handles pricing information.
 *
 * @since 1907
 */
public class TmaAbstractOrderUserPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{
	private Converter<UserModel, PrincipalData> principalConverter;

	@Override
	public void populate(final AbstractOrderModel source,final  AbstractOrderData target)
	{
		target.setUser(getPrincipalConverter().convert(source.getUser()));
	}

	public Converter<UserModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(
			Converter<UserModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}
}
