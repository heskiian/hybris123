/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Set;


/**
 * Populates identification details of {@link CustomerData} based on {@link CustomerModel}.
 *
 * @since 1911
 */
public class TmaCustomerIdentificationsPopulator implements Populator<CustomerModel, CustomerData>
{
	private Converter<TmaIdentificationModel, TmaIdentificationData> tmaIdentificationConverter;

	public TmaCustomerIdentificationsPopulator(
			final Converter<TmaIdentificationModel, TmaIdentificationData> tmaIdentificationConverter)
	{
		this.tmaIdentificationConverter = tmaIdentificationConverter;
	}

	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		final Set<TmaIdentificationModel> identifications = source.getIdentifications();
		target.setIdentifications(getTmaIdentificationConverter().convertAll(identifications));
	}

	protected Converter<TmaIdentificationModel, TmaIdentificationData> getTmaIdentificationConverter()
	{
		return tmaIdentificationConverter;
	}
}
