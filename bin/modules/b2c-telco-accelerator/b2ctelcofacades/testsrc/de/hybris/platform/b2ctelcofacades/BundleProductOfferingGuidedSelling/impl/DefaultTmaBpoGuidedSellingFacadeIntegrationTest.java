/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.BundleProductOfferingGuidedSelling.impl;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.bpoguidedselling.TmaBpoGuidedSellingFacade;
import de.hybris.platform.b2ctelcofacades.bpoguidedselling.impl.DefaultTmaBpoGuidedSellingFacade;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingDashBoardData;
import de.hybris.platform.b2ctelcofacades.data.GuidedSellingStepData;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.Map;

import javax.annotation.Resource;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Test class for {@link DefaultTmaBpoGuidedSellingFacade}
 *
 */
@IntegrationTest
public class DefaultTmaBpoGuidedSellingFacadeIntegrationTest extends BaseCommerceBaseTest
{
	@Resource(name = "i18NService")
	private I18NService i18NService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private TmaBpoGuidedSellingFacade tmaBpoGuidedSellingFacade;

	@Resource
	private TmaPoService tmaPoService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws Exception
	{
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		try
		{
			importCsv("/b2ctelcofacades/test/testBundleProductOfferingData.impex", "utf-8");
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("testSite"), true);
	}

	@Test
	public void testGuidedSellingDashBoardDataForBpoCodeTest1()
	{

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO1", null);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testGuidedSellingDashBoardDataForBpoCodeTest2()
	{

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO2", null);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testGuidedSellingDashBoardDataForBpoCodeTest3()
	{

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO3", null);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testGuidedSellingDashBoardDataForBpoCodeTest4()
	{

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO4", null);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testGuidedSellingDashBoardDataForBpoCodeWithGroupNumber() throws IllegalArgumentException
	{
		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO1", 1);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testGetFirstStepOfBpoCode()
	{
		final String stepId = tmaBpoGuidedSellingFacade.getFirstStepForBPO("BPO1");
		assertNotNull(stepId);
	}

	@Test
	public void testGuidedSellingDashBoardDataForWrongBpoCode() throws UnknownIdentifierException
	{
		thrown.expect(UnknownIdentifierException.class);

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPOWrong",
				null);
	}

	@Test
	public void testGetCalculatedStepsForBundledProductOfferingForBPO()
	{

		final Map<String, GuidedSellingStepData> guidedSellingDashBoardData = tmaBpoGuidedSellingFacade
				.getCalculatedStepsForBPO("BPO1");
		assertThat(guidedSellingDashBoardData, IsMapContaining.hasKey("GRP11"));
	}

	@Test
	public void testGetCalculatedStepsForBundledProductOfferingForWrongBPO() throws UnknownIdentifierException
	{
		thrown.expect(UnknownIdentifierException.class);

		final Map<String, GuidedSellingStepData> guidedSellingDashBoardData = tmaBpoGuidedSellingFacade
				.getCalculatedStepsForBPO("BPOWrong");
	}

	@Test
	public void testDashBoardDataForBpoCodeWithGroupNumberAndGroupId() throws IllegalArgumentException
	{
		thrown.expect(IllegalArgumentException.class);

		final GuidedSellingDashBoardData guidedSellingDashBoardData = tmaBpoGuidedSellingFacade.getDashBoardForBPO("BPO1", "GRP1",
				1);
		assertThat(guidedSellingDashBoardData.getDashBoardEntries(), not(IsEmptyCollection.empty()));
	}

	@Test
	public void testValidateAndGetBPOEntryGroup() throws IllegalArgumentException
	{
		thrown.expect(IllegalArgumentException.class);
		tmaBpoGuidedSellingFacade.validateAndGetBPOEntryGroup("BPO1", 1);
	}
}

