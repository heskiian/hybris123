/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyPscvValueComparator;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;


/**
 * Checks whether numeric ProductSpecificationCharacteristicValues are within a range.
 *
 * @since 1805
 */
public class TmaPolicyPscvNumericValueComaprator implements TmaPolicyPscvValueComparator
{
	/**
	 * Service used for converting usage unit (e.g. 1024 mbps = 1 gbps).
	 */
	private TmaUnitConversionStrategy conversionStrategy;

	public boolean compare(final String prodPscvValue, final UsageUnitModel prodPscvUnit, final String stMin, final String stMax,
			final UsageUnitModel stUnit)
	{
		final BigDecimal prodPscvBd = BigDecimal.valueOf(Double.valueOf(prodPscvValue));
		final BigDecimal stMinBd = StringUtils.isEmpty(stMin) ? BigDecimal.valueOf(Double.MIN_VALUE)
				: BigDecimal.valueOf(Double.valueOf(stMin));
		final BigDecimal stMaxBd = StringUtils.isEmpty(stMax) ? BigDecimal.valueOf(Double.MAX_VALUE)
				: BigDecimal.valueOf(Double.valueOf(stMax));
		final BigDecimal prodPscvConverted = conversionStrategy.getUnitConversion(prodPscvUnit, stUnit, prodPscvBd);
		return prodPscvConverted.compareTo(stMinBd) >= 0 && prodPscvConverted.compareTo(stMaxBd) <= 0;
	}

	public TmaUnitConversionStrategy getConversionStrategy()
	{
		return conversionStrategy;
	}

	public void setConversionStrategy(final TmaUnitConversionStrategy conversionStrategy)
	{
		this.conversionStrategy = conversionStrategy;
	}
}
