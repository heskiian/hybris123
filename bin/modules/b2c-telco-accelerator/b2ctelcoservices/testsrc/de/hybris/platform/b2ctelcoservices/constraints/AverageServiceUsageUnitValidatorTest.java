/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration Test for {@link AverageServiceUsageUnitValidator}
 *
 * @since 6.7
 */

@IntegrationTest
public class AverageServiceUsageUnitValidatorTest extends AbstractConstraintTest
{
	private static final String PSCV_ID_MB = "test_1024_mb";
	private static final String PSCV_ID_MBPS = "test_50_10_data_speed";
	private static final String PSCV_INVALID = "test_invalidPSCV";
	private static final String ASU_ID = "test_ASU";
	private static final String TEST_MB = "test_mb";
	private static final String TEST_SMS = "test_sms";
	private static final String ASU_VALUE1 = "100";
	private static final String INVALID_PSCV_VALUE = "50/10";
	private static final String DEFAULT = "default";
	private static final String ASU_DATA = "ASU_DATA";
	private static final String ASU_COMPATIBLE_ID = "ASU_Compatible_id";
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	@Before
	public void setup() throws Exception
	{
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		try
		{
			createCoreData();
			importCsv("/test/impex/test_customerInventory.impex", "utf-8");
			importCsv("/b2ctelcoservices/import/common/b2ctelcoservices-constraints.impex", "UTF-8");
			JaloSession.getCurrentSession().getSessionContext().setLocale(Locale.ENGLISH);
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}
		validationService.reloadValidationEngine();
	}

	private UsageUnitModel getUnitOfMeasure(final String value)
	{
		final UsageUnitModel unitOfMeasureASU = modelService.create(UsageUnitModel.class);
		unitOfMeasureASU.setId(value);
		return flexibleSearchService.getModelByExample(unitOfMeasureASU);
	}

	@Test
	public void testIsInvalidASUDates() throws ParseException
	{
		final TmaAverageServiceUsageModel tmaAverageServiceUsageModel = modelService.create(TmaAverageServiceUsageModel.class);
		tmaAverageServiceUsageModel.setId(ASU_ID);
		tmaAverageServiceUsageModel.setPscvId(PSCV_ID_MB);
		tmaAverageServiceUsageModel.setUnitOfMeasure(getUnitOfMeasure(TEST_MB));
		tmaAverageServiceUsageModel.setValue(ASU_VALUE1);
		tmaAverageServiceUsageModel.setStartDate(dateFormatter.parse("01.03.2018"));
		tmaAverageServiceUsageModel.setEndDate(dateFormatter.parse("28.02.2018"));
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaAverageServiceUsageModel,
				Collections.singletonList(getGroup(DEFAULT)));
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			Assert.assertFalse(hybrisConstraintViolation.getLocalizedMessage(), violations.isEmpty());
		}
	}

	@Test
	public void testIsSameASUDates() throws ParseException
	{
		final TmaAverageServiceUsageModel tmaAverageServiceUsageModel = modelService.create(TmaAverageServiceUsageModel.class);
		tmaAverageServiceUsageModel.setId(ASU_ID);
		tmaAverageServiceUsageModel.setPscvId(PSCV_ID_MB);
		tmaAverageServiceUsageModel.setUnitOfMeasure(getUnitOfMeasure(TEST_MB));
		tmaAverageServiceUsageModel.setValue(ASU_VALUE1);
		tmaAverageServiceUsageModel.setStartDate(dateFormatter.parse("01.03.2018"));
		tmaAverageServiceUsageModel.setEndDate(dateFormatter.parse("01.03.2018"));
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaAverageServiceUsageModel,
				Collections.singletonList(getGroup(DEFAULT)));
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			Assert.assertFalse(hybrisConstraintViolation.getLocalizedMessage(), violations.isEmpty());
		}
	}

	@Test
	public void testIsValidASUDates() throws ParseException
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_DATA);
		source.setStartDate(dateFormatter.parse("01.02.2018"));
		source.setEndDate(dateFormatter.parse("28.02.2018"));
		final Set<HybrisConstraintViolation> violations = validationService.validate(source,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void testIsValidASU()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_COMPATIBLE_ID);
		final Set<HybrisConstraintViolation> violations = validationService.validate(source,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void testIsCompatibleUnitConversion()
	{
		final TmaAverageServiceUsageModel source = getSource(ASU_COMPATIBLE_ID);
		final Set<HybrisConstraintViolation> violations = validationService.validate(source,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void testIsOfNumericTypeOnly()
	{
		final TmaAverageServiceUsageModel tmaAverageServiceUsageModel = modelService.create(TmaAverageServiceUsageModel.class);
		tmaAverageServiceUsageModel.setId(ASU_ID);
		tmaAverageServiceUsageModel.setPscvId(PSCV_ID_MBPS);
		tmaAverageServiceUsageModel.setUnitOfMeasure(getUnitOfMeasure(TEST_MB));
		tmaAverageServiceUsageModel.setValue(ASU_VALUE1);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaAverageServiceUsageModel,
				Collections.singletonList(getGroup(DEFAULT)));
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			Assert.assertEquals(
					"Invalid ProductSpecCharValues '" + INVALID_PSCV_VALUE + "'.Only numeric and 'UNLIMITED'"
							+ " is allowed ProductSpecCharValues to create AverageServiceUsage.",
					hybrisConstraintViolation.getLocalizedMessage());
		}
	}

	@Test
	public void testInvalidPSCV()
	{
		final TmaAverageServiceUsageModel tmaAverageServiceUsageModel = modelService.create(TmaAverageServiceUsageModel.class);
		tmaAverageServiceUsageModel.setId(ASU_ID);
		tmaAverageServiceUsageModel.setPscvId(PSCV_INVALID);
		tmaAverageServiceUsageModel.setUnitOfMeasure(getUnitOfMeasure(TEST_MB));
		tmaAverageServiceUsageModel.setValue(ASU_VALUE1);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaAverageServiceUsageModel,
				Collections.singletonList(getGroup(DEFAULT)));
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			Assert.assertEquals(
					"Invalid ProductSpecCharValue ID '" + PSCV_INVALID
							+ "'.No ProductSpecCharValues found to create AverageServiceUsage.",
					hybrisConstraintViolation.getLocalizedMessage());
		}
	}

	@Test
	public void testInvalidCompatibleUnitConversion()
	{
		final TmaAverageServiceUsageModel tmaAverageServiceUsageModel = modelService.create(TmaAverageServiceUsageModel.class);
		tmaAverageServiceUsageModel.setId(ASU_ID);
		tmaAverageServiceUsageModel.setPscvId(PSCV_ID_MB);
		tmaAverageServiceUsageModel.setUnitOfMeasure(getUnitOfMeasure(TEST_SMS));
		tmaAverageServiceUsageModel.setValue(ASU_VALUE1);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaAverageServiceUsageModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertFalse(violations.isEmpty());
	}

	private ConstraintGroupModel getGroup(final String id)
	{
		final ConstraintGroupModel sample = new ConstraintGroupModel();
		sample.setId(id);
		return flexibleSearchService.getModelByExample(sample);
	}


	private TmaAverageServiceUsageModel getSource(final String value)
	{
		final TmaAverageServiceUsageModel source = modelService.create(TmaAverageServiceUsageModel.class);
		source.setId(value);
		return flexibleSearchService.getModelByExample(source);
	}
}
