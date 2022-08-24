/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link TmaAverageServiceUsageData} from a {@link TmaAverageServiceUsageModel} entity.
 *
 * @since 6.7
 */
public class TmaAverageServiceUsagePopulator implements Populator<TmaAverageServiceUsageModel, TmaAverageServiceUsageData>
{
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> tmaProductSpecCharacteristicValueConverter;
	private TmaUnitConversionStrategy tmaUnitConversionStrategy;

	@Override
	public void populate(final TmaAverageServiceUsageModel source, final TmaAverageServiceUsageData target)
	{
		target.setId(source.getId());
		target.setValue(source.getValue());
		target.setUnitOfMeasure(source.getUnitOfMeasure().getId());
		target.setUnitOfMeasureName(getUnitOfMeasureName(source));
		target.setUsageType(source.getUsageType().getCode());
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setConvertedUnitValue(getConvertedUnitValue(source.getUnitOfMeasure(),
				source.getProductSpecCharValue().getUnitOfMeasure(), source.getValue()));
		target.setTmaProductSpecCharacteristicValueData(
				getTmaProductSpecCharacteristicValueConverter().convert(source.getProductSpecCharValue()));
		target.setRemainingValue(getRemainingBalance(source));
	}

	protected String getUnitOfMeasureName(final TmaAverageServiceUsageModel source)
	{
		return (StringUtils.isNotEmpty(source.getUnitOfMeasure().getName()) ? source.getUnitOfMeasure().getName()
				: source.getUnitOfMeasure().getId());
	}

	protected Long getRemainingBalance(final TmaAverageServiceUsageModel source)
	{
		if (source.getProductSpecCharValue() == null && StringUtils.isEmpty(source.getValue()))
		{
			throw new IllegalArgumentException(TmaAverageServiceUsageModel._TYPECODE + " value is null");
		}
		final String poProductSpecCharValue = source.getProductSpecCharValue().getValue();
		if (!StringUtils.isNumeric(poProductSpecCharValue))
		{
			throw new NumberFormatException(TmaAverageServiceUsageModel._TYPECODE + " with numeric " + poProductSpecCharValue
					+ " productSpecCharValue not found");
		}
		final BigDecimal productSpecCharValue = BigDecimal.valueOf(Double.parseDouble(poProductSpecCharValue));
		final BigDecimal avgSerUsageValue = getTmaUnitConversionStrategy().getUnitConversion(source.getUnitOfMeasure(),
				source.getProductSpecCharValue().getUnitOfMeasure(), BigDecimal.valueOf(Double.parseDouble(source.getValue())));
		final BigDecimal remainingValueBalance = productSpecCharValue.subtract(avgSerUsageValue);
		final BigDecimal remainingValue = getTmaUnitConversionStrategy().getUnitConversion(
				source.getProductSpecCharValue().getUnitOfMeasure(), source.getUnitOfMeasure(), remainingValueBalance);
		return (long) Math.round(remainingValue.floatValue());
	}

	private String getConvertedUnitValue(final UsageUnitModel fromUsageUnit, final UsageUnitModel toUsageUnit,
			final String fromUsageUnitValue)
	{
		return tmaUnitConversionStrategy.getUnitConversion(fromUsageUnit, toUsageUnit, new BigDecimal(fromUsageUnitValue))
				.toString();
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> getTmaProductSpecCharacteristicValueConverter()
	{
		return tmaProductSpecCharacteristicValueConverter;
	}

	@Required
	public void setTmaProductSpecCharacteristicValueConverter(
			final Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData> tmaProductSpecCharacteristicValueConverter)
	{
		this.tmaProductSpecCharacteristicValueConverter = tmaProductSpecCharacteristicValueConverter;
	}

	protected TmaUnitConversionStrategy getTmaUnitConversionStrategy()
	{
		return tmaUnitConversionStrategy;
	}

	@Required
	public void setTmaUnitConversionStrategy(final TmaUnitConversionStrategy tmaUnitConversionStrategy)
	{
		this.tmaUnitConversionStrategy = tmaUnitConversionStrategy;
	}
}
