/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration Test for {@link TmaBpoPreconfigurationValidator}
 *
 * @since 6.7
 */

@IntegrationTest
public class TmaBpoPreConfigValidationTest extends AbstractConstraintTest
{
	private static final Logger LOG = Logger.getLogger(TmaBpoPreConfigValidationTest.class);
	private static final String LL_VOIP = "ll_voip";
	private static final String TV_S = "tv_S";
	private static final String INT_100 = "int_100";
	private static final String INTERNET_PACK_CASUAL = "internet_pack_casual";
	private static final String TAPAS_S = "tapasS";
	private static final String _2047052 = "2047052";
	private static final String DEFAULT = "default";
	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setup() throws Exception
	{
		LOG.debug("Preparing test data");
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		try
		{
			createCoreData();
			importCsv("/b2ctelcofacades/test/testTmaBpoPreConfigData.impex", "utf-8");
			importCsv("/b2ctelcoservices/import/common/b2ctelcoservices-constraints.impex", "UTF-8");
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}
		validationService.reloadValidationEngine();
	}

	@Test
	public void valid_sposSelectedArePartOfTheRootBpo() throws ImpExException
	{
		final TmaBpoPreConfigModel tmaBpoPreConfigModel = getTmaBpoPreConfigModel("preconfig_1", "preconfig_1_name",
				"mobileDeal");
		final Set<TmaSimpleProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo(_2047052));
		childPos.add(this.getPo(TAPAS_S));
		childPos.add(this.getPo(INTERNET_PACK_CASUAL));
		tmaBpoPreConfigModel.setPreConfigSpos(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaBpoPreConfigModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void valid_sposSelectedArePartOfTheNestedRootBpo() throws ImpExException
	{
		final TmaBpoPreConfigModel tmaBpoPreConfigModel = getTmaBpoPreConfigModel("preconfig_2", "preconfig_2_name",
				"homeDeal");
		final Set<TmaSimpleProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo(INT_100));
		childPos.add(this.getPo(TV_S));
		childPos.add(this.getPo(LL_VOIP));
		tmaBpoPreConfigModel.setPreConfigSpos(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaBpoPreConfigModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void invalid_sposSelectedAreNotPartOfTheRootBpo() throws ImpExException
	{
		final TmaBpoPreConfigModel tmaBpoPreConfigModel = getTmaBpoPreConfigModel("preconfig_3", "preconfig_3_name",
				"mobileDeal");
		final Set<TmaSimpleProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo(_2047052));
		childPos.add(this.getPo(TV_S));
		childPos.add(this.getPo(LL_VOIP));
		tmaBpoPreConfigModel.setPreConfigSpos(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaBpoPreConfigModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertEquals("There should be one constraint violations", 1,
				violations.size());
	}

	private TmaBpoPreConfigModel getTmaBpoPreConfigModel(final String preConfigCode, final String preConfigName,
			final String parentBpoCode)
	{
		final TmaBpoPreConfigModel tmaBpoPreConfigModel = modelService.create(TmaBpoPreConfigModel.class);
		tmaBpoPreConfigModel.setCode(preConfigCode);
		tmaBpoPreConfigModel.setName(preConfigName);
		tmaBpoPreConfigModel.setRootBpo(this.getBpo(parentBpoCode));
		tmaBpoPreConfigModel.setCatalogVersion(catalogVersionService.getCatalogVersion("testCatalog", "Online"));
		return tmaBpoPreConfigModel;
	}

	private TmaBundledProductOfferingModel getBpo(final String code)
	{
		final TmaBundledProductOfferingModel bpo = new TmaBundledProductOfferingModel();
		bpo.setCode(code);
		return flexibleSearchService.getModelByExample(bpo);
	}

	private TmaSimpleProductOfferingModel getPo(final String code)
	{
		final TmaSimpleProductOfferingModel po = new TmaSimpleProductOfferingModel();
		po.setCode(code);
		return flexibleSearchService.getModelByExample(po);
	}

	private ConstraintGroupModel getGroup(final String id)
	{
		final ConstraintGroupModel sample = new ConstraintGroupModel();
		sample.setId(id);
		return flexibleSearchService.getModelByExample(sample);
	}

}
