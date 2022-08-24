/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.data.TmaProductSpecCharacteristicConfigItem;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Resource strategy implementation. Validates and updates configurable characteristic resource.
 *
 * @since 1911
 */
public class TmaConfigProductSpecCharResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private static final String CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN = "configurable.characteristic.value.pattern";
	private TmaPoService tmaPoService;
	private ModelService modelService;
	private ConfigurationService configurationService;

	public TmaConfigProductSpecCharResourceStrategy(TmaPoService tmaPoService, ModelService modelService,
			ConfigurationService configurationService)
	{
		this.tmaPoService = tmaPoService;
		this.modelService = modelService;
		this.configurationService = configurationService;
	}

	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);

		final Set<TmaProductSpecCharacteristicConfigItem> paramCharacteristics = parameter
				.getProductSpecCharacteristicConfigs();
		if (CollectionUtils.isEmpty(paramCharacteristics))
		{
			return result;
		}

		return validateParamCharacteristics(parameter, result, paramCharacteristics);
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException
	{
		if (CollectionUtils.isEmpty(commerceCartParameter.getProductSpecCharacteristicConfigs()))
		{
			return;
		}

		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();

		if (abstractOrderEntryModel == null)
		{
			throw new CommerceCartModificationException(
					"Unable to add product specification characteristic to entry '" + commerceCartParameter.getEntryNumber()
							+ "' in cart '" + commerceCartParameter.getCart().getCode() + "'.");
		}

		final Set<TmaProductSpecCharacteristicConfigItem> paramCharacteristics = commerceCartParameter
				.getProductSpecCharacteristicConfigs();

		clearExistingConfigurablePscvs(abstractOrderEntryModel, paramCharacteristics);

		final Set<TmaAbstractOrderEntryPscvModel> abstractOrderEntryCharacteristics = new HashSet<>();
		abstractOrderEntryCharacteristics.addAll(abstractOrderEntryModel.getProductSpecCharacteristicValues());

		abstractOrderEntryCharacteristics.addAll(paramCharacteristics.stream()
				.filter(characteristic -> characteristic.getValue() != null)
				.map(characteristic -> {
					final TmaAbstractOrderEntryPscvModel abstractOrderCharacteristic = getModelService()
							.create(TmaAbstractOrderEntryPscvModel.class);
					abstractOrderCharacteristic.setName(characteristic.getName());
					abstractOrderCharacteristic.setValue(characteristic.getValue());
					return abstractOrderCharacteristic;
				}).collect(Collectors.toSet()));

		abstractOrderEntryModel.setProductSpecCharacteristicValues(abstractOrderEntryCharacteristics);


		getModelService().save(abstractOrderEntryModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);
		commerceCartModification.setPscv(abstractOrderEntryCharacteristics);
	}

	/**
	 * Removes the configurable characteristics from the entry which are present in the request.
	 *
	 * @param entry
	 * 		The entry
	 * @param requestCharacteristics
	 * 		The characteristics present in the request
	 */
	protected void clearExistingConfigurablePscvs(final AbstractOrderEntryModel entry,
			final Set<TmaProductSpecCharacteristicConfigItem> requestCharacteristics)
	{
		if (requestCharacteristics == null)
		{
			return;
		}

		if (CollectionUtils.isEmpty(requestCharacteristics))
		{
			entry.setProductSpecCharacteristicValues(null);
			getModelService().save(entry);
			return;
		}

		requestCharacteristics.forEach((TmaProductSpecCharacteristicConfigItem characteristic) -> {
			final Set<TmaAbstractOrderEntryPscvModel> matchingCharacteristics = new HashSet<>();
			final Set<TmaAbstractOrderEntryPscvModel> nonMatchingCharacteristics = new HashSet<>();
			entry.getProductSpecCharacteristicValues().forEach((TmaAbstractOrderEntryPscvModel entryPscv) -> {
				if (entryPscv.getName().equalsIgnoreCase(characteristic.getName()))
				{
					matchingCharacteristics.add(entryPscv);
					return;
				}

				nonMatchingCharacteristics.add(entryPscv);
			});

			if (CollectionUtils.isEmpty(matchingCharacteristics))
			{
				return;
			}

			entry.setProductSpecCharacteristicValues(nonMatchingCharacteristics);
			getModelService().save(entry);

			getModelService().removeAll(matchingCharacteristics);
		});
	}

	private TmaCartValidationResult validateParamCharacteristics(final CommerceCartParameter parameter,
			TmaCartValidationResult result, final Set<TmaProductSpecCharacteristicConfigItem> paramCharacteristics)
	{
		Optional<TmaProductSpecCharacteristicConfigItem> first = paramCharacteristics.stream()
				.filter(characteristic -> characteristic.getName() == null).findFirst();
		if (first.isPresent())
		{
			result.setValid(false);
			result.setMessage("Invalid characteristic: Name missing.");
			return result;
		}

		if (!(parameter.getProduct() instanceof TmaProductOfferingModel))
		{
			result.setValid(false);
			result.setMessage("Invalid product.");
			return result;
		}

		final Set<TmaProductSpecCharValueUseModel> pscvus = ((TmaProductOfferingModel) parameter.getProduct())
				.getProductSpecCharValueUses();
		if (CollectionUtils.isEmpty(pscvus))
		{
			result.setValid(false);
			result.setMessage("Invalid product specification characteristics");
			return result;
		}

		final Set<String> productCharacteristicNames = pscvus.stream()
				.map(pscvu -> pscvu.getName().toLowerCase(Locale.ENGLISH))
				.collect(Collectors.toSet());

		final Set<String> paramCharacteristicsNames = paramCharacteristics.stream()
				.map(productSpecCharacteristic -> productSpecCharacteristic.getName().toLowerCase(Locale.ENGLISH))
				.collect(Collectors.toSet());

		if (!productCharacteristicNames.containsAll(paramCharacteristicsNames))
		{
			result.setValid(false);
			result.setMessage("Invalid product specification characteristics");
			return result;
		}

		for (String paramCharacteristicsName : paramCharacteristicsNames)
		{
			Set<TmaProductSpecCharValueUseModel> applicablePscvus = pscvus.stream()
					.filter(pscvu -> StringUtils.equalsIgnoreCase(paramCharacteristicsName, pscvu.getName()))
					.collect(Collectors.toSet());
			Set<String> paramsCharacteristicValues = paramCharacteristics.stream()
					.filter(
							param -> StringUtils.equalsIgnoreCase(paramCharacteristicsName, param.getName()) && param.getValue() != null)
					.map(TmaProductSpecCharacteristicConfigItem::getValue)
					.collect(Collectors.toSet());
			Set<TmaProductSpecCharValueUseModel> configurablePscvus = applicablePscvus.stream()
					.filter(pscvu -> CollectionUtils.isEmpty(pscvu.getProductSpecCharacteristicValues())
							|| pscvu.getProductSpecCharacteristicValues().stream().anyMatch(pscv -> pscv.getValue()
							.equals(getConfigurationService().getConfiguration().getString(CONFIGURABLE_CHARACTERISTIC_VALUE_PATTERN))))
					.collect(Collectors.toSet());
			Integer maxCardinality = applicablePscvus.stream().allMatch(pscvu -> pscvu.getMaxCardinality() != null)
					? applicablePscvus.stream().mapToInt(TmaProductSpecCharValueUseModel::getMaxCardinality).sum()
					: null;
			if (maxCardinality != null && paramsCharacteristicValues.size() > maxCardinality)
			{
				result.setValid(false);
				result.setMessage("Invalid number of product specification characteristics value defined");
				return result;
			}
			if (CollectionUtils.isEmpty(configurablePscvus))
			{
				return validateApplicablePscvus(result, paramCharacteristicsName, applicablePscvus, paramsCharacteristicValues);
			}
		}
		return result;
	}

	private TmaCartValidationResult validateApplicablePscvus(TmaCartValidationResult result, String paramCharacteristicsName,
			Set<TmaProductSpecCharValueUseModel> applicablePscvus, Set<String> paramsCharacteristicValues)
	{
		Set<String> applicableValues = new HashSet<>();
		for (TmaProductSpecCharValueUseModel applicablePscvu : applicablePscvus)
		{
			Set<String> paramValues = new HashSet<>(paramsCharacteristicValues);
			paramValues.retainAll(applicablePscvu.getProductSpecCharacteristicValues().stream()
					.map(TmaProductSpecCharacteristicValueModel::getValue)
					.collect(Collectors.toSet()));
			if (applicablePscvu.getMaxCardinality() != null && paramValues.size() > applicablePscvu.getMaxCardinality())
			{
				result.setValid(false);
				result.setMessage("Invalid number of product specification characteristics value defined");
				return result;
			}
			applicableValues.addAll(applicablePscvu.getProductSpecCharacteristicValues().stream()
					.map(TmaProductSpecCharacteristicValueModel::getValue)
					.collect(Collectors.toSet()));
		}
		if (!applicableValues.containsAll(paramsCharacteristicValues))
		{
			result.setValid(false);
			result.setMessage("Invalid product specification characteristics value for " + paramCharacteristicsName);
			return result;
		}

		return result;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
