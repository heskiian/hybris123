/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


@IntegrationTest
public class DefaultTmaPoServiceIntegrationTest extends ServicelayerTest
{
	private static final String BPO_CODE = "mobileDeal";
	private static final String EXISTING_PO_CODE = "tapasS";
	private static final String PO_CODE_TO_BE_ADDED = "tapasM";

	private static final String NOT_EXISTING_PO_CODE = "salsaS";
	private static final String NON_EXISTING_BPO_CODE = "valentines";

	private static final String IPHONE_8_CODE = "iPhone_8";
	private static final String INT_100_CODE = "int_100";
	private static final String INT_500_CODE = "int_500";
	private static final String INT_1000_CODE = "int_1000";
	private static final String INT_5000_CODE = "int_5000";

	private static final String QUAD_PLAY_CODE = "quadPlay";
	private static final String HOME_DEAL_CODE = "homeDeal";
	private static final String STARTER_HOME_DEAL_CODE = "starterHomeDeal";
	private static final String INTERNET_CODE = "internet";
	private static final String STARTER_INTERNET_CODE = "starterInternet";

	@Resource
	private DefaultTmaPoService tmaPoService;

	private TmaBundledProductOfferingModel parentBPO;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_productOfferings.impex", "utf-8");
		parentBPO = (TmaBundledProductOfferingModel) getTmaProductOfferingByCode(BPO_CODE);
	}

	@Test
	public void testGetProductForCode()
	{
		TmaProductOfferingModel poModel = tmaPoService.getPoForCode(BPO_CODE);
		Assert.assertEquals(BPO_CODE, poModel.getCode());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testNotExistingPoByCode()
	{
		tmaPoService.getPoForCode(NOT_EXISTING_PO_CODE);
	}

	@Test
	public void testAddNewChildToParent()
	{
		TmaProductOfferingModel child = getTmaProductOfferingByCode(PO_CODE_TO_BE_ADDED);
		tmaPoService.addPoChildToParent(parentBPO, child);

		assertTrue("Child's parent is not correctly set", child.getParents().contains(parentBPO));
		assertTrue("Child Product Offering was not added", parentBPO.getChildren().contains(child));
	}

	@Test
	public void testRemoveChildFromParent()
	{
		TmaProductOfferingModel child = getTmaProductOfferingByCode(EXISTING_PO_CODE);
		tmaPoService.removePoChildFromParent(parentBPO, child);

		assertFalse("Child's parent is not correctly updated", child.getParents().contains(parentBPO));
		assertFalse("Child Product Offering was not removed", parentBPO.getChildren().contains(child));
	}

	@Test
	public void testGetBpoByCode()
	{
		try
		{
			TmaBundledProductOfferingModel bpo = tmaPoService.getBpoForCode(BPO_CODE);
			Assert.assertEquals(BPO_CODE, bpo.getCode());
		}
		catch (UnknownIdentifierException | ModelNotFoundException exception)
		{
			assertTrue("There is no TmaBundledProductOffering with code " + BPO_CODE, false);
		}
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetBpoByCodeWithInvalidCode()
	{
		tmaPoService.getBpoForCode(NON_EXISTING_BPO_CODE);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetBpoByCodeWithSpoCode()
	{
		tmaPoService.getBpoForCode(EXISTING_PO_CODE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBundledProdOfferOptionFor_nullBpo()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(IPHONE_8_CODE);
		tmaPoService.getBundledProdOfferOptionFor(null, productOffering);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBundledProdOfferOptionFor_nullPo()
	{
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(BPO_CODE);
		tmaPoService.getBundledProdOfferOptionFor(bundledProductOffering, null);
	}

	@Test
	public void getBundledProdOfferOptionFor_noBpoOptions()
	{
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(IPHONE_8_CODE);

		assertFalse(tmaPoService.getBundledProdOfferOptionFor(bundledProductOffering, productOffering).isPresent());
	}

	@Test
	public void getBundledProdOfferOptionFor_bpoWithoutBpoOptionForPo()
	{
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(BPO_CODE);
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(EXISTING_PO_CODE);

		assertFalse(tmaPoService.getBundledProdOfferOptionFor(bundledProductOffering, productOffering).isPresent());
	}

	@Test
	public void getBundledProdOfferOptionFor_bpoWithBpoOptionForPo()
	{
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(BPO_CODE);
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(IPHONE_8_CODE);

		final Optional<TmaBundledProdOfferOptionModel> bundledProdOfferOption = tmaPoService
				.getBundledProdOfferOptionFor(bundledProductOffering, productOffering);

		assertTrue(bundledProdOfferOption.isPresent());
		assertEquals(0, (int) bundledProdOfferOption.get().getLowerLimit());
		assertEquals(1, (int) bundledProdOfferOption.get().getUpperLimit());
		assertEquals(0, (int) bundledProdOfferOption.get().getDefault());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getIntermediateBpos_nullPo()
	{
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);

		tmaPoService.getIntermediateBpos(null, bundledProductOffering);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getIntermediateBpos_nullBpo()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(INT_100_CODE);

		tmaPoService.getIntermediateBpos(productOffering, null);
	}

	@Test
	public void getIntermediateBpos_noIntermediateBpos()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(INT_1000_CODE);
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);

		final List<TmaBundledProductOfferingModel> intermediateBpos = tmaPoService
				.getIntermediateBpos(productOffering, bundledProductOffering);

		assertTrue(CollectionUtils.isEmpty(intermediateBpos));
	}

	@Test
	public void getIntermediateBpos_oneIntermediateBpo()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(INT_5000_CODE);
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);

		final List<TmaBundledProductOfferingModel> intermediateBpos = tmaPoService
				.getIntermediateBpos(productOffering, bundledProductOffering);

		assertEquals(1, intermediateBpos.size());
		assertEquals(QUAD_PLAY_CODE, intermediateBpos.get(0).getCode());

	}

	@Test
	public void getIntermediateBpos_multipleIntermediateBpoPaths()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(INT_100_CODE);
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);

		final List<TmaBundledProductOfferingModel> intermediateBpos = tmaPoService
				.getIntermediateBpos(productOffering, bundledProductOffering);

		assertEquals(3, intermediateBpos.size());
		assertEquals(INTERNET_CODE, intermediateBpos.get(0).getCode());
		assertEquals(HOME_DEAL_CODE, intermediateBpos.get(1).getCode());
		assertEquals(QUAD_PLAY_CODE, intermediateBpos.get(2).getCode());
	}

	@Test
	public void getIntermediateBpos_multipleIntermedateBpoPathsDifferentOrder()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(INT_500_CODE);
		final TmaBundledProductOfferingModel bundledProductOffering = tmaPoService.getBpoForCode(QUAD_PLAY_CODE);

		final List<TmaBundledProductOfferingModel> intermediateBpos = tmaPoService
				.getIntermediateBpos(productOffering, bundledProductOffering);

		assertEquals(3, intermediateBpos.size());
		assertEquals(STARTER_INTERNET_CODE, intermediateBpos.get(0).getCode());
		assertEquals(STARTER_HOME_DEAL_CODE, intermediateBpos.get(1).getCode());
		assertEquals(QUAD_PLAY_CODE, intermediateBpos.get(2).getCode());
	}

	private TmaProductOfferingModel getTmaProductOfferingByCode(String productOfferingCode)
	{
		try
		{
			return tmaPoService.getPoForCode(productOfferingCode);
		}
		catch (ModelNotFoundException exception)
		{
			assertTrue("There is no TmaProductOffering with code " + productOfferingCode, false);
			return null;
		}
	}
}
