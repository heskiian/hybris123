/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.StringUtils;


/**
 * Populates the attributes of {@link TmaIdentificationModel} from {@link TmaIdentificationData}
 *
 * @since 1911
 */
public class TmaIdentificationReversePopulator implements Populator<TmaIdentificationData, TmaIdentificationModel>
{
	@Override
	public void populate(final TmaIdentificationData source, final TmaIdentificationModel target) throws ConversionException
	{
		final String identificationTypeString = source.getIdentificationType();
		final String identificationNumber = source.getIdentificationNumber();
		if (StringUtils.isNotBlank(identificationTypeString) && StringUtils.isNotBlank(identificationNumber))
		{
			target.setIdentificationType(TmaIdentificationType.valueOf(identificationTypeString));
			target.setIdentificationNumber(identificationNumber);
		}
	}
}
