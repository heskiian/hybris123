/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;


/**
 * Integration Test for {@link TmaAverageServiceUsagePopulator}
 *
 * @since 6.7
 */
@IntegrationTest
public class TmaAverageServiceUsagePopulatorIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	@Resource
	private TmaAverageServiceUsagePopulator tmaAverageServiceUsagePopulator;
	@Resource
	private TmaUnitConversionStrategy tmaUnitConversionStrategy;

	@Resource
	private ModelService modelService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	private static final String ASU_ID = "ASU_ID";
	private static final String ASU_DATA = "ASU_DATA";
	private static final String ASU_TEST = "ASU_TEST";
	private static final String ASU_PSCV = "ASU_UNLIMITED";
	private TmaAverageServiceUsageModel source;
	private TmaAverageServiceUsageData target;

	@Test
	public void forNonNullSource()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_ID);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
		assertEquals(source.getId(), target.getId());
		assertEquals(source.getValue(), target.getValue());
		assertEquals(source.getUnitOfMeasure().getId(), target.getUnitOfMeasure());
		assertEquals(source.getUsageType().getCode(), target.getUsageType());
		assertEquals(source.getStartDate(), target.getStartDate());
		assertEquals(source.getEndDate(), target.getEndDate());
	}

	@Test
	public void forASUNameValue()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_DATA);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
		assertEquals(source.getUnitOfMeasure().getName(), target.getUnitOfMeasureName());
		assertEquals(source.getProductSpecCharValue().getValue(), target.getTmaProductSpecCharacteristicValueData().getValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void forASUValueNull()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_DATA);
		source.setValue(StringUtils.EMPTY);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
	}

	@Test(expected = NullPointerException.class)
	public void forProductSpecCharValueIsNull()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_DATA);
		source.setPscvId(StringUtils.EMPTY);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
	}

	@Test
	public void forASUProductSpecCharValueIsNumber()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_DATA);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
		Assert.assertTrue(StringUtils.isNumeric(target.getTmaProductSpecCharacteristicValueData().getValue()));
	}

	@Test(expected = NumberFormatException.class)
	public void forASUProductSpecCharValueInvalid()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_PSCV);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
	}

	@Test(expected = NumberFormatException.class)
	public void forASUException()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_TEST);
		target = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(source, target);
	}

	private UsageUnitModel getUnitOfMeasure(final String value)
	{
		final UsageUnitModel unitOfMeasureASU = modelService.create(UsageUnitModel.class);
		unitOfMeasureASU.setId(value);
		return flexibleSearchService.getModelByExample(unitOfMeasureASU);
	}

	private TmaAverageServiceUsageModel getSource(final String value)
	{
		final TmaAverageServiceUsageModel source = modelService.create(TmaAverageServiceUsageModel.class);
		source.setId(value);
		return flexibleSearchService.getModelByExample(source);
	}
}
