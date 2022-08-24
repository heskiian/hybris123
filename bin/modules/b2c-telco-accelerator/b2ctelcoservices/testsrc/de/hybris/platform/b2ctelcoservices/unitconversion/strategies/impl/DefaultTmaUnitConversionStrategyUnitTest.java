/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.unitconversion.strategies.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * For testing {@link DefaultTmaUnitConversionStrategy}, which is used for unit conversion,
 */
@UnitTest
public class DefaultTmaUnitConversionStrategyUnitTest
{
	private static final String TMA_SERVICE_FROM_USAGE_UNIT_OF_MEASURE = "MB";
	private static final String TMA_SERVICE_TO_UNIT_OF_MEASURE = "GB";
	private static final String TMA_SERVICE_FROM_USAGE_VALUE = "125";
	private TmaUnitConversionStrategy tmaUnitConversionStrategy;
	private UsageUnitModel fromUsageUnitModel;
	private UsageUnitModel toUsageUnitModel;
	private UsageUnitModel fromUsageUnitInvalidModel;
	private UsageUnitModel toUsageUnitInvalidModel;
	private UsageUnitModel fromUsageUnitNullModel;
	private UsageUnitModel toUsageUnitNullModel;
	private static BigDecimal fromValue = new BigDecimal(TMA_SERVICE_FROM_USAGE_VALUE);

	@Mock
	private ModelService modelService;

	@Before
	public void setUp()
	{
		initMocks(this);
		setUpValidData();
		setUpInvalidData();
		setUpNullData();
		tmaUnitConversionStrategy = new DefaultTmaUnitConversionStrategy();
		final BigDecimal fromValue = new BigDecimal(TMA_SERVICE_FROM_USAGE_VALUE);
	}

	@Test
	public void testConversionStrategy()
	{
		final BigDecimal conversionFactor = new BigDecimal("0.0009765625");
		final MathContext mathContext = new MathContext(4);
		final BigDecimal convertedValue = fromValue.multiply(conversionFactor, mathContext);
		assertTrue("It hasn't the expected Conversion unit",
				convertedValue.equals(tmaUnitConversionStrategy.getUnitConversion(fromUsageUnitModel, toUsageUnitModel, fromValue)));
	}

	@Test
	public void testConversionStrategyForInvalid()
	{
		assertTrue("It hasn't the expected Conversion unit for invalid value", fromValue
				.equals(tmaUnitConversionStrategy.getUnitConversion(fromUsageUnitInvalidModel, toUsageUnitInvalidModel, fromValue)));
	}

	@Test
	public void testConversionStrategyForNull()
	{
		assertTrue("It hasn't the expected Conversion unit for null value", fromValue
				.equals(tmaUnitConversionStrategy.getUnitConversion(fromUsageUnitNullModel, toUsageUnitNullModel, fromValue)));
	}

	private void setUpValidData()
	{
		final Map<String, String> toUsageUnitMap = new HashMap();
		toUsageUnitMap.put("MB", "1024");
		final Map<String, String> fromUsageUnitMap = new HashMap();
		fromUsageUnitMap.put("GB", "0.0009765625");
		toUsageUnitModel = new UsageUnitModel();
		toUsageUnitModel.setId(TMA_SERVICE_TO_UNIT_OF_MEASURE);
		toUsageUnitModel.setUnitConversionMap(toUsageUnitMap);
		modelService.save(toUsageUnitModel);
		fromUsageUnitModel = new UsageUnitModel();
		fromUsageUnitModel.setId(TMA_SERVICE_FROM_USAGE_UNIT_OF_MEASURE);
		fromUsageUnitModel.setUnitConversionMap(fromUsageUnitMap);
		modelService.save(fromUsageUnitModel);
	}

	private void setUpInvalidData()
	{
		final Map<String, String> toUsageUnitInvalidMap = new HashMap();
		toUsageUnitInvalidMap.put("MB", "InvalidMB");
		final Map<String, String> fromUsageUnitInvalidMap = new HashMap();
		fromUsageUnitInvalidMap.put("GB", "InvalidGB");
		toUsageUnitInvalidModel = new UsageUnitModel();
		toUsageUnitInvalidModel.setId(TMA_SERVICE_TO_UNIT_OF_MEASURE);
		toUsageUnitInvalidModel.setUnitConversionMap(toUsageUnitInvalidMap);
		modelService.save(toUsageUnitInvalidModel);
		fromUsageUnitInvalidModel = new UsageUnitModel();
		fromUsageUnitInvalidModel.setId(TMA_SERVICE_FROM_USAGE_UNIT_OF_MEASURE);
		fromUsageUnitInvalidModel.setUnitConversionMap(fromUsageUnitInvalidMap);
		modelService.save(fromUsageUnitInvalidModel);
	}

	private void setUpNullData()
	{
		toUsageUnitNullModel = new UsageUnitModel();
		toUsageUnitNullModel.setId(TMA_SERVICE_TO_UNIT_OF_MEASURE);
		modelService.save(toUsageUnitNullModel);
		fromUsageUnitNullModel = new UsageUnitModel();
		fromUsageUnitNullModel.setId(TMA_SERVICE_FROM_USAGE_UNIT_OF_MEASURE);
		modelService.save(fromUsageUnitNullModel);
	}
}
