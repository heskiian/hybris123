/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardEntryData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardPopulatorParameters;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaBpoGuidedSellingDashBoardDataPopulator}
 *
 */
@UnitTest
public class TmaBpoGuidedSellingDashBoardDataPopulatorTest
{
	private static final Logger LOG = Logger.getLogger(TmaBpoGuidedSellingDashBoardDataPopulatorTest.class);

	@InjectMocks
	private final TmaBpoGuidedSellingDashBoardDataPopulator bpoGuidedSellingDashBoardDataPopulator = new TmaBpoGuidedSellingDashBoardDataPopulator();

	@Before
	public void setUp()
	{
		LOG.debug("Preparing test data");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPopulate()
	{
		final GuidedSellingDashBoardPopulatorParameters guidedSellingDashBoardPopulatorParameters = new GuidedSellingDashBoardPopulatorParameters();
		final GuidedSellingDashBoardData guidedSellingDashBoardData = new GuidedSellingDashBoardData();
		guidedSellingDashBoardData.setIsAnyProductSelected(false);
		final GuidedSellingStepData stepData = new GuidedSellingStepData();
		List<GuidedSellingDashBoardEntryData> entryDataList = new ArrayList<GuidedSellingDashBoardEntryData>();

		stepData.setId("Test");
		stepData.setName("Test_Step");

		guidedSellingDashBoardPopulatorParameters.setStepData(Arrays.asList(stepData));
		bpoGuidedSellingDashBoardDataPopulator.populate(guidedSellingDashBoardPopulatorParameters, guidedSellingDashBoardData);
		entryDataList = guidedSellingDashBoardData.getDashBoardEntries();

		Assert.assertEquals("Check if steps data is empty ", false,
				CollectionUtils.isNotEmpty(entryDataList.get(0).getDashBoardOrderEntries()));
		Assert.assertEquals("Size should be false", false, guidedSellingDashBoardData.isIsAnyProductSelected());
		Assert.assertEquals("Size should be 1", 1, entryDataList.size());
		Assert.assertEquals("Step Id ", "Test", entryDataList.get(0).getStepId());
		Assert.assertEquals("Step Name ", "Test_Step", entryDataList.get(0).getStepName());

	}
}
