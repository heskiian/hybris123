/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaIdentificationService;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerReversePopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates Customer details of {@link CustomerModel} from {@link CustomerData}.
 *
 * @since 1911
 */

public class TmaCustomerReversePopulator extends CustomerReversePopulator implements Populator<CustomerData, CustomerModel>
{
	private Converter<TmaIdentificationData, TmaIdentificationModel> identificationReverseConverter;
	private TmaIdentificationService tmaIdentificationService;

	public TmaCustomerReversePopulator(
			final Converter<TmaIdentificationData, TmaIdentificationModel> identificationReverseConverter,
			final TmaIdentificationService tmaIdentificationService)
	{
		this.identificationReverseConverter = identificationReverseConverter;
		this.tmaIdentificationService = tmaIdentificationService;
	}

	@Override
	public void populate(final CustomerData source, final CustomerModel target) throws ConversionException
	{
		super.populate(source, target);
		final List<TmaIdentificationData> identifications = source.getIdentifications();
		target.setIdentifications(
				CollectionUtils.isNotEmpty(identifications) ? getIdentifications(identifications) : Collections.EMPTY_SET);
	}

	private Set<TmaIdentificationModel> getIdentifications(final List<TmaIdentificationData> identifications)
	{
		final Set<TmaIdentificationModel> identificationsSet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(identifications))
		{
			identifications.stream().forEach(identification ->
			{
				final TmaIdentificationModel identificationModel = getTmaIdentificationService().findIdentificationByTypeAndNumber(
						identification.getIdentificationType(), identification.getIdentificationNumber());
				identificationsSet.add(ObjectUtils.isEmpty(identificationModel)
						? getIdentificationReverseConverter().convert(identification)
						: identificationModel);
			});
		}
		return identificationsSet;
	}

	protected Converter<TmaIdentificationData, TmaIdentificationModel> getIdentificationReverseConverter()
	{
		return identificationReverseConverter;
	}

	protected TmaIdentificationService getTmaIdentificationService()
	{
		return tmaIdentificationService;
	}
}
