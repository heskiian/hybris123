/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration Test for {@link TmaPoGroupImmediateChildrenValidator}
 * {@link TmaPoGroupValidSpoBpoValidator}.
 *
 * @since 6.7
 */
@IntegrationTest
public class TmaProductOfferingGroupValidationTest extends AbstractConstraintTest
{
	private static final Logger LOG = Logger.getLogger(TmaProductOfferingGroupValidationTest.class);
	private static final String TEST_2047052 = "test_2047052";
	private static final String TEST_PAYG_SERVICE_1 = "Test_PAYG_Service_1";
	private static final String DEFAULT = "default";
	private static final String ONLINE = "Online";
	private static final String TEST_CATALOG = "testCatalog";
	private static final String TEST_PAYG_DEVICE = "Test_PAYG_Device";
	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private ModelService modelService;
	@Resource
	private BaseSiteService baseSiteService;
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
			importCsv("/b2ctelcofacades/test/testTmaProductOfferingGroupData.impex", "utf-8");
			importCsv("/b2ctelcoservices/import/common/b2ctelcoservices-constraints.impex", "UTF-8");
			baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
			JaloSession.getCurrentSession().getSessionContext().setLocale(Locale.ENGLISH);
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}
		validationService.reloadValidationEngine();
	}

	@Test
	public void validScenario() throws ImpExException
	{
		final TmaProductOfferingGroupModel tmaProductOfferingGroupModel = getTmaProductOfferingGroup("po_group_1",
				TEST_PAYG_DEVICE);
		final Set<TmaProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo(TEST_2047052));
		childPos.add(this.getPo("test_3417125"));
		tmaProductOfferingGroupModel.setChildProductOfferings(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaProductOfferingGroupModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void poGroupDoesNotConsistOfImmediateChildrens() throws ImpExException
	{
		final TmaProductOfferingGroupModel tmaProductOfferingGroupModel = getTmaProductOfferingGroup("po_group_2",
				TEST_PAYG_DEVICE);
		final Set<TmaProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo(TEST_2047052));
		childPos.add(this.getPo("test_2093587"));
		tmaProductOfferingGroupModel.setChildProductOfferings(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaProductOfferingGroupModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertEquals("There should be two constraint violations", 2, violations.size());
	}

	@Test
	public void poGroupConsistOfBothBpoAndSpo() throws ImpExException
	{
		final TmaProductOfferingGroupModel tmaProductOfferingGroupModel = getTmaProductOfferingGroup("po_group_3",
				"Test_PAYG_Service");
		final Set<TmaProductOfferingModel> childPos = new HashSet<>();
		childPos.add(this.getPo("test_3417125"));
		childPos.add(this.getBpo(TEST_PAYG_SERVICE_1));
		tmaProductOfferingGroupModel.setChildProductOfferings(childPos);
		final Set<HybrisConstraintViolation> violations = validationService.validate(tmaProductOfferingGroupModel,
				Collections.singletonList(getGroup(DEFAULT)));
		Assert.assertEquals("There should be one constraint violations", 1, violations.size());
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			Assert.assertEquals(
					"The child product offerings of 'po_group_3' TmaProductOfferingGroup should either consist entirely of Simple Product Offerings or Bundled Product Offerings.",
					hybrisConstraintViolation.getLocalizedMessage());
		}
	}

	private TmaProductOfferingGroupModel getTmaProductOfferingGroup(final String poGroupcode, final String parentBpoCode)
	{
		final TmaProductOfferingGroupModel tmaProductOfferingGroupModel = modelService.create(TmaProductOfferingGroupModel.class);
		tmaProductOfferingGroupModel.setCode(poGroupcode);
		tmaProductOfferingGroupModel.setCatalogVersion(catalogVersionService.getCatalogVersion(TEST_CATALOG, ONLINE));
		tmaProductOfferingGroupModel.setParentBundleProductOffering(this.getBpo(parentBpoCode));
		return tmaProductOfferingGroupModel;
	}

	private TmaBundledProductOfferingModel getBpo(final String code)
	{
		final TmaBundledProductOfferingModel bpo = new TmaBundledProductOfferingModel();
		bpo.setCode(code);
		return flexibleSearchService.getModelByExample(bpo);
	}

	private TmaProductOfferingModel getPo(final String code)
	{
		final TmaProductOfferingModel po = new TmaProductOfferingModel();
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
