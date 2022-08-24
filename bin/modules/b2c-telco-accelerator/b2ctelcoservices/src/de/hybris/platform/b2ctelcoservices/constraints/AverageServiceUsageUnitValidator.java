/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Triggers the validator to check if the Selected tmaAverageServiceUsage unitOfMeasure and ProductSpecCharValue
 * unitOfMeasure are compatible units or not The method check if the AverageServiceUsage
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel#getUnitOfMeasure()} and
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel#getUnitOfMeasure()} are
 * compatible units
 *
 * @since 18.05
 */

public class AverageServiceUsageUnitValidator extends TmaTypeValidatorHelper
		implements ConstraintValidator<AverageServiceUsageUnit, ItemModel>
{
	private static final String VALIDATE_MESSAGE = "Validating object is null";
	private static final String NUMERIC_ONLY = "[0-9]+";
	private static final String UNLIMITED = "Unlimited";

	@Override
	public void initialize(final AverageServiceUsageUnit annotation)
	{
		Registry.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}

	@Override
	public boolean isValid(final ItemModel value, final ConstraintValidatorContext context)
	{
		validateParameterNotNull(value, VALIDATE_MESSAGE);
		return isValidASU((TmaAverageServiceUsageModel) value, context);
	}

	private boolean isValidASU(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel,
			final ConstraintValidatorContext context)
	{
		final String template_numericError = "{de.hybris.platform.b2ctelcoservices.constraints.AverageServiceUsageNumericOnly.message}";
		final String template_notfound = "{de.hybris.platform.b2ctelcoservices.constraints.AverageServiceUsagePscvNotFound.message}";
		final String template_dateError = "{de.hybris.platform.b2ctelcoservices.constraints.AverageServiceUsageDates.message}";
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel = tmaAverageServiceUsageModel
				.getProductSpecCharValue();
		if (null == tmaProductSpecCharacteristicValueModel)
		{
			buildErrorMessage(context, template_notfound, tmaAverageServiceUsageModel.getPscvId());
			return false;
		}
		if (!isOfNumericTypeOnly(tmaProductSpecCharacteristicValueModel))
		{
			buildErrorMessage(context, template_numericError,
					tmaProductSpecCharacteristicValueModel.getValue());
			return false;
		}
		if (!isCompatibleUsageUnit(tmaAverageServiceUsageModel, tmaProductSpecCharacteristicValueModel)
				&& !isCompatibleUnitConversionMap(tmaAverageServiceUsageModel, tmaProductSpecCharacteristicValueModel))
		{
			buildErrorMessage(context, tmaAverageServiceUsageModel.getUnitOfMeasure().getId(),
					tmaProductSpecCharacteristicValueModel.getUnitOfMeasure().getId());
			return false;
		}
		if (!hasStartDateBeforeEndDate(tmaAverageServiceUsageModel))
		{
			buildErrorMessage(context, template_dateError, tmaAverageServiceUsageModel.getStartDate(),
					tmaAverageServiceUsageModel.getEndDate());
			return false;
		}
		return true;
	}

	/**
	 * Validate if the AverageServiceUsage startDate should before or equal with endDate.
	 *
	 * @param averageServiceUsage
	 * @return true if startDate compatible with endDate
	 */
	private boolean hasStartDateBeforeEndDate(final TmaAverageServiceUsageModel averageServiceUsage)
	{
		return (averageServiceUsage.getStartDate().before(averageServiceUsage.getEndDate())
				|| averageServiceUsage.getEndDate().after(averageServiceUsage.getStartDate())
				|| averageServiceUsage.getStartDate().equals(averageServiceUsage.getEndDate()));
	}

	/**
	 * Validate if the ProductSpecCharValue contains only numeric value.
	 *
	 * @param tmaProductSpecCharacteristicValueModel
	 * @return true if ProductSpecCharValue is numeric
	 */

	private boolean isOfNumericTypeOnly(final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel)
	{
		final String productSpecificCharValue = tmaProductSpecCharacteristicValueModel.getValue().trim();
		return (productSpecificCharValue.matches(NUMERIC_ONLY) || UNLIMITED.equalsIgnoreCase(productSpecificCharValue));
	}

	/**
	 * Validate if the UnitOfMeasure and ProductSpecCharValue UnitOfMeasure have compatible
	 *
	 * @param tmaAverageServiceUsageModel
	 * @return true if the units are compatible
	 */
	private boolean isCompatibleUsageUnit(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel,
			final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel)
	{
		return (tmaProductSpecCharacteristicValueModel.getUnitOfMeasure().getId()
				.equalsIgnoreCase(tmaAverageServiceUsageModel.getUnitOfMeasure().getId()));
	}

	/**
	 * Validate if the UnitOfMeasure UnitConversionMap contain the key of ProductSpecCharValue UnitOfMeasure
	 *
	 * @param tmaAverageServiceUsageModel
	 * @return true if the units are compatible
	 */
	private boolean isCompatibleUnitConversionMap(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel,
			final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel)
	{
		return (tmaAverageServiceUsageModel.getUnitOfMeasure().getUnitConversionMap().keySet()
				.contains(tmaProductSpecCharacteristicValueModel.getUnitOfMeasure().getId()));
	}
}
