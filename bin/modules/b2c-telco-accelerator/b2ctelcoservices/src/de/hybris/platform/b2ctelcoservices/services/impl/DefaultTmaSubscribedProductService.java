/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscribedProductDao;
import de.hybris.platform.b2ctelcoservices.model.TmaNormalizedTermOfServiceFrequencyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscribedProductService}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscribedProductService implements TmaSubscribedProductService
{
	private TmaSubscribedProductDao tmaSubscribedProductDao;
	private ModelService modelService;
	private DefaultGenericDao<TmaNormalizedTermOfServiceFrequencyModel> termsOfServiceDao;
	private PersistentKeyGenerator tmaSubscribedProductIdGenerator;
	private PersistentKeyGenerator tmaBillingSubscriptionIdGenerator;

	@Override
	public TmaSubscribedProductModel createSubscribedProduct(final TmaSubscribedProductModel subscribedProductModel)
	{
		final String generatedId = getTmaSubscribedProductIdGenerator().generate().toString();
		subscribedProductModel.setId(generatedId);
		if (StringUtils.isBlank(subscribedProductModel.getBillingSubscriptionId()))
		{
			subscribedProductModel.setBillingSubscriptionId(tmaBillingSubscriptionIdGenerator.generate().toString());
		}
		getModelService().save(subscribedProductModel);
		return subscribedProductModel;
	}

	public TmaSubscribedProductModel createNewSubscribedProduct(final Date startDate, final String billingSystemId,
			final String subscriptionStatus)
	{
		final TmaSubscribedProductModel subscribedProduct = getModelService().create(TmaSubscribedProductModel.class);
		final String generatedId = getTmaSubscribedProductIdGenerator().generate().toString();
		subscribedProduct.setId(generatedId);
		subscribedProduct.setPlacedOn(startDate);
		subscribedProduct.setStartDate(startDate);
		subscribedProduct.setBillingsystemId(billingSystemId);
		subscribedProduct.setSubscriptionStatus(subscriptionStatus);
		subscribedProduct.setBillingSubscriptionId(getTmaBillingSubscriptionIdGenerator().generate().toString());

		getModelService().save(subscribedProduct);
		return subscribedProduct;
	}

	@Override
	public TmaSubscribedProductModel findSubscribedProduct(final String billingSystemId, final String billingSubscriptionId)
	{
		return getTmaSubscribedProductDao().findSubscribedProduct(billingSystemId, billingSubscriptionId);
	}


	@Override
	public TmaSubscribedProductModel findSubscribedProductById(final String subscribedProductId)
	{
		try
		{
			return getTmaSubscribedProductDao().findSubscribedProductById(subscribedProductId);
		}
		catch (final ModelNotFoundException e)
		{
			throw new ModelNotFoundException("Could not find " + TmaSubscribedProductModel._TYPECODE + " for subscribed product Id "
					+ subscribedProductId, e);
		}
	}

	@Override
	public List<TmaSubscribedProductModel> findSubscriptionsForPaymentMethod(final String paymentMethodId)
	{
		return getTmaSubscribedProductDao().findSubscriptionsForPaymentMethod(paymentMethodId);
	}

	@Override
	public void deleteSubscribedProduct(final String billingSystemId, final String billingSubscriptionId)
	{
		getModelService().remove(findSubscribedProduct(billingSystemId, billingSubscriptionId));
	}

	@Override
	@Nonnull
	public Date getSubscriptionServiceEndDate(final String frequencyCode, final Integer duration, final Date startDate)
	{
		final TermOfServiceFrequency frequency = getTermOfServiceFrequency(frequencyCode);
		final Integer normalizedDuration = getTimeNormalizationFactor(frequency) * duration;
		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MONTH, normalizedDuration);
		return cal.getTime();
	}

	private TermOfServiceFrequency getTermOfServiceFrequency(final String frequencyCode)
	{
		return Arrays.stream(TermOfServiceFrequency.values())
				.filter(frequency -> frequency.getCode().equalsIgnoreCase(frequencyCode)).findFirst()
				.orElse(TermOfServiceFrequency.MONTHLY);
	}

	private Integer getTimeNormalizationFactor(final TermOfServiceFrequency frequency)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(TmaNormalizedTermOfServiceFrequencyModel.SOURCE, TermOfServiceFrequency.MONTHLY);
		params.put(TmaNormalizedTermOfServiceFrequencyModel.TARGET, frequency);
		final List<TmaNormalizedTermOfServiceFrequencyModel> normalizedFrequency = termsOfServiceDao.find(params);
		return normalizedFrequency == null || normalizedFrequency.isEmpty() ? Integer.valueOf(1)
				: normalizedFrequency.get(0).getNormalizationFactor();
	}


	@Required
	public void setTmaSubscribedProductDao(final TmaSubscribedProductDao tmaSubscribedProductDao)
	{
		this.tmaSubscribedProductDao = tmaSubscribedProductDao;
	}

	protected TmaSubscribedProductDao getTmaSubscribedProductDao()
	{
		return tmaSubscribedProductDao;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setTmaSubscribedProductIdGenerator(final PersistentKeyGenerator tmaSubscribedProductIdGenerator)
	{
		this.tmaSubscribedProductIdGenerator = tmaSubscribedProductIdGenerator;
	}

	protected PersistentKeyGenerator getTmaSubscribedProductIdGenerator()
	{
		return tmaSubscribedProductIdGenerator;
	}

	protected PersistentKeyGenerator getTmaBillingSubscriptionIdGenerator()
	{
		return tmaBillingSubscriptionIdGenerator;
	}

	@Required
	public void setTmaBillingSubscriptionIdGenerator(final PersistentKeyGenerator tmaBillingSubscriptionIdGenerator)
	{
		this.tmaBillingSubscriptionIdGenerator = tmaBillingSubscriptionIdGenerator;
	}

	protected DefaultGenericDao<TmaNormalizedTermOfServiceFrequencyModel> getTermsOfServiceDao()
	{
		return termsOfServiceDao;
	}

	@Required
	public void setTermsOfServiceDao(
			final DefaultGenericDao<TmaNormalizedTermOfServiceFrequencyModel> termsOfServiceDao)
	{
		this.termsOfServiceDao = termsOfServiceDao;
	}
}
