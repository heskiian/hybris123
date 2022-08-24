/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.converters.Populator;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populator for {@link TmaIdentificationData}.
 *
 * @since 1911
 */
public class TmaIdentificationPopulator implements Populator<TmaIdentificationModel, TmaIdentificationData>
{

	@Override
	public void populate(final TmaIdentificationModel source, final TmaIdentificationData target)
	{
		final TmaIdentificationType identificationType = source.getIdentificationType();
		final String identificationNumber = source.getIdentificationNumber();
		if (!ObjectUtils.isEmpty(identificationType) && StringUtils.isNotBlank(identificationNumber))
		{
			target.setIdentificationType(identificationType.getCode());
			target.setIdentificationNumber(identificationNumber);
		}
	}
}
