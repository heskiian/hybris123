/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.impl.DefaultTmaPriceContextService;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Integration Test of {@link DefaultTmaPriceContextService}.
 *
 * @since 2007
 */

@IntegrationTest
public class DefaultTmaPriceContextServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	DefaultTmaPriceContextService defaultTmaPriceContextService;
	@Resource
	private TmaRegionService tmaRegionService;
	@Resource
	private ProductService productService;
	@Resource
	private TmaSubscriptionTermService tmaSubscriptionTermService;

	private ProductModel productModel;
	private ProductModel bpoProductModel;

	private static final String TEST_QUADPLAY = "test_quadPlay";
	private static final String IN_ND = "IN-ND";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_priceContextCreateData.impex", "utf-8");
		productModel = productService.getProductForCode("iPhone_8_plus");
		bpoProductModel = productService.getProductForCode(TEST_QUADPLAY);
	}

	@Test
	public void createPriceContextWithParentBpoForCommerceCartParameterTest()
	{
		final CommerceCartParameter parameter = getTestDataForCommerceCartParameter();
		parameter.setBpoCode(TEST_QUADPLAY);
		final TmaPriceContext priceContext = defaultTmaPriceContextService.createPriceContext(parameter);
		Assert.assertNotNull(priceContext);
		assertEquals(TEST_QUADPLAY, priceContext.getProduct().getCode());
		assertEquals(TmaProcessType.ACQUISITION, priceContext.getProcessTypes().iterator().next());
		assertEquals(IN_ND, priceContext.getRegions().iterator().next().getIsocode());
	}

	@Test
	public void createPriceContextWithoutParentBpoForCommerceCartParameterTest()
	{
		final TmaPriceContext priceContext = defaultTmaPriceContextService
				.createPriceContext(getTestDataForCommerceCartParameter());
		Assert.assertNotNull(priceContext);
		assertEquals(priceContext.getProduct(), productModel);
		assertEquals(TmaProcessType.ACQUISITION, priceContext.getProcessTypes().iterator().next());
		assertEquals(IN_ND, priceContext.getRegions().iterator().next().getIsocode());
	}

	@Test
	public void createPriceContextWithParentBpoForAbstractOrderEntryModelTest()
	{
		final AbstractOrderEntryModel entry = getTestDataForAbstractOrderEntry();
		final AbstractOrderEntryModel masterEntry = getTestDataForMasterAbstractOrderEntry();
		entry.setBpo((TmaBundledProductOfferingModel) productService.getProductForCode(TEST_QUADPLAY));
		entry.setMasterEntry(masterEntry);
		masterEntry.setChildEntries(Collections.singletonList(entry));

		final TmaPriceContext priceContext = defaultTmaPriceContextService.createPriceContext(entry);
		Assert.assertNotNull(priceContext);
		assertEquals(TEST_QUADPLAY, priceContext.getProduct().getCode());
		assertEquals(TmaProcessType.ACQUISITION, priceContext.getProcessTypes().iterator().next());
		assertEquals(IN_ND, priceContext.getRegions().iterator().next().getIsocode());
	}

	@Test
	public void createPriceContextWithoutParentBpoForAbstractOrderEntryModelTest()
	{
		final TmaPriceContext priceContext = defaultTmaPriceContextService.createPriceContext(getTestDataForAbstractOrderEntry());
		Assert.assertNotNull(priceContext);
		assertEquals(priceContext.getProduct(), productModel);
		assertEquals(TmaProcessType.ACQUISITION, priceContext.getProcessTypes().iterator().next());
		assertEquals(IN_ND, priceContext.getRegions().iterator().next().getIsocode());
	}

	private CommerceCartParameter getTestDataForCommerceCartParameter()
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProcessType("ACQUISITION");
		parameter.setProduct(productModel);
		final TmaCartSubscriptionInfoModel subscriptionInfo = new TmaCartSubscriptionInfoModel();
		subscriptionInfo.setSubscriptionTerm(tmaSubscriptionTermService.getSubscriptionTerm("monthly_12"));
		parameter.setSubscriptionInfo(subscriptionInfo);
		parameter.setPlaces(setUpRegionData());
		return parameter;
	}

	private AbstractOrderEntryModel getTestDataForAbstractOrderEntry()
	{
		final TmaCartSubscriptionInfoModel subscriptionInfo = new TmaCartSubscriptionInfoModel();
		subscriptionInfo.setSubscriptionTerm(tmaSubscriptionTermService.getSubscriptionTerm("monthly_12"));
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setProcessType(TmaProcessType.ACQUISITION);
		entry.setProduct(productModel);
		entry.setSubscriptionInfo(subscriptionInfo);
		entry.setRegion(tmaRegionService.findRegionByIsocode(IN_ND));
		final CartModel cart = new CartModel();
		entry.setOrder(cart);
		cart.setEntries(Collections.singletonList(entry));
		entry.setEntryGroupNumbers(new HashSet<>(Collections.singletonList(Integer.valueOf(1))));
		return entry;
	}

	private AbstractOrderEntryModel getTestDataForMasterAbstractOrderEntry()
	{
		final TmaCartSubscriptionInfoModel subscriptionInfo = new TmaCartSubscriptionInfoModel();
		subscriptionInfo.setSubscriptionTerm(tmaSubscriptionTermService.getSubscriptionTerm("monthly_12"));
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setProcessType(TmaProcessType.ACQUISITION);
		entry.setProduct(bpoProductModel);
		entry.setSubscriptionInfo(subscriptionInfo);
		entry.setRegion(tmaRegionService.findRegionByIsocode(IN_ND));
		final CartModel cart = new CartModel();
		entry.setOrder(cart);
		cart.setEntries(Collections.singletonList(entry));
		return entry;
	}

	private List<TmaPlace> setUpRegionData()
	{
		final RegionModel region = new RegionModel();
		region.setIsocode(IN_ND);

		final TmaRegionPlace tmaRegionPlace = new TmaRegionPlace();
		tmaRegionPlace.setRole(TmaPlaceRoleType.PRODUCT_REGION);

		tmaRegionPlace.setRegion(region);
		final List<TmaPlace> tmaPlaces = new ArrayList<>();
		tmaPlaces.add(tmaRegionPlace);
		return tmaPlaces;
	}
}
