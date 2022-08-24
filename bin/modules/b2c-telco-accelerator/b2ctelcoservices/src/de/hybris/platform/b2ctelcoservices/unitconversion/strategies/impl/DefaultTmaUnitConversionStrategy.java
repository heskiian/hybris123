/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.unitconversion.strategies.impl;

import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;


/**
 * Default implementation of the {@link TmaUnitConversionStrategy}.
 *
 * @since 6.7
 */
public class DefaultTmaUnitConversionStrategy implements TmaUnitConversionStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaUnitConversionStrategy.class);
	private static final String ERROR_MESSAGE = "Incompitable unit of measure or usageUnitMap is null";

	@Override
	public BigDecimal getUnitConversion(final UsageUnitModel fromUsageUnit,
										final UsageUnitModel toUsageUnit,
										final BigDecimal fromUsageUnitValue)
	{
		BigDecimal toUsageUnitValue = fromUsageUnitValue;
		final MathContext mathContext = new MathContext(4);
		final Pattern pattern = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
		final Map<String, String> usageUnitMap = fromUsageUnit.getUnitConversionMap();

		if (fromUsageUnit.equals(toUsageUnit))
		{
			return toUsageUnitValue;
		}
		else if (!CollectionUtils.isEmpty(usageUnitMap) &&
				!StringUtils.isBlank(toUsageUnit.getId()) &&
				!StringUtils.isBlank(usageUnitMap.get(toUsageUnit.getId())))
		{
			final String conversionFactor = usageUnitMap.get(toUsageUnit.getId()).trim();
			if (!StringUtils.isBlank(conversionFactor) && pattern.matcher(conversionFactor).matches())
			{
				toUsageUnitValue = fromUsageUnitValue.multiply(new BigDecimal(conversionFactor), mathContext);
			}
		}
		else
		{
			LOG.error(ERROR_MESSAGE + "--> usageUnitMap:" + usageUnitMap + ",toUsageUnit Id:" + toUsageUnit.getId());
		}
		return toUsageUnitValue;
	}

}