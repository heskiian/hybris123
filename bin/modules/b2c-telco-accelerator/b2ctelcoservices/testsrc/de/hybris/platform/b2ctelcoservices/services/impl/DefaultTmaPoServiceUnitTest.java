/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaProductDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultTmaPoServiceUnitTest
{
	private static final int PRICE_VALUE = 699;
	private static final String PRODUCT_OFFERING_SALSA_S = "salsaS";
	private static final String OPERATIONAL_PRODUCT_REMOVE_CODE = "service_request_remove";
	private static final String ACTIONS_WITH_OPERATIONAL_OFFERINGS = "default.subscribedProductActions.withOperationalOfferings";
	private DefaultTmaPoService poService;
	private TmaProductOfferingModel po;
	private TmaBundledProductOfferingModel bpo;

	@Mock
	private ProductService productService;
	@Mock
	private TmaCommercePriceService commercePriceService;
	@Mock
	private TmaProductDao mockProductDao;
	@Mock
	private ConfigurationService mockConfigurationService;
	@Mock
	private Configuration mockConfig;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.poService = new DefaultTmaPoService(mockProductDao);
		this.po = new TmaProductOfferingModel();
		this.bpo = createBpo("code1");
		poService.setProductService(productService);
		poService.setCommercePriceService(commercePriceService);
		poService.setConfigurationService(mockConfigurationService);
		when(mockConfigurationService.getConfiguration()).thenReturn(mockConfig);
		when(mockConfig.getString(ACTIONS_WITH_OPERATIONAL_OFFERINGS)).thenReturn("REMOVE");
		final TmaProductOfferingModel salsa = new TmaProductOfferingModel();
		salsa.setCode(PRODUCT_OFFERING_SALSA_S);
		when(productService.getProductForCode(PRODUCT_OFFERING_SALSA_S)).thenReturn(salsa);
		final TmaOperationalProductOfferingModel operationalProductInput = new TmaOperationalProductOfferingModel();
		final Set<TmaSubscribedProductAction> actions = new HashSet<>();
		actions.add(TmaSubscribedProductAction.REMOVE);
		operationalProductInput.setActions(actions);
		operationalProductInput.setCode(OPERATIONAL_PRODUCT_REMOVE_CODE);
		Mockito.when(mockProductDao.getOperationalProductOffering(TmaSubscribedProductAction.REMOVE))
				.thenReturn(Optional.of(operationalProductInput));
		final List<ProductModel> productList = new ArrayList<>();
		productList.add(salsa);
		when(mockProductDao.findProductsByCode(PRODUCT_OFFERING_SALSA_S)).thenReturn(productList);
	}

	@Test
	public void testFindAllParents()
	{
		final TmaBundledProductOfferingModel bpo1 = createBpo("code1");
		final TmaBundledProductOfferingModel bpo2 = createBpo("code2");
		final TmaBundledProductOfferingModel bpo3 = createBpo("code3");

		givenParentsFor(po, bpo3);
		givenParentsFor(bpo3, bpo2);
		givenParentsFor(bpo2, bpo1);
		bpo1.setParents(new HashSet<>());
		thenParentsAre(po, Arrays.asList(bpo3, bpo2, bpo1));
	}

	@Test
	public void testIsValidParent()
	{
		final TmaBundledProductOfferingModel bpo1 = createBpo("code1");
		final TmaBundledProductOfferingModel bpo2 = createBpo("code2");
		final TmaBundledProductOfferingModel bpo3 = createBpo("code3");

		givenParentsFor(po, bpo3);
		givenParentsFor(bpo3, bpo2);
		givenParentsFor(bpo2, bpo1);
		bpo1.setParents(new HashSet<>());

		thenIsParentValid(bpo3, bpo2);
	}

	@Test
	public void givenGroupWithProductOnPosition0_thenNextGroupIsOnPosition1()
	{
		final TmaProductOfferingGroupModel group1 = createGroup();
		givenGroupsForBpo(bpo, createGroup(po), group1, createGroup());
		thenGroupNextToProductGroupIs(Optional.of(group1));
	}

	@Test
	public void givenGroupWithProductOnPosition1_thenNextGroupIsOnPosition0()
	{
		final TmaProductOfferingGroupModel group0 = createGroup();
		givenGroupsForBpo(bpo, group0, createGroup(po), createGroup());
		thenGroupNextToProductGroupIs(Optional.of(group0));
	}

	@Test
	public void givenGroupWithProductOnPosition2_thenNextGroupIsOnPosition0()
	{
		final TmaProductOfferingGroupModel group0 = createGroup();
		givenGroupsForBpo(bpo, group0, createGroup(), createGroup(po));
		thenGroupNextToProductGroupIs(Optional.of(group0));
	}

	@Test
	public void givenGroupWithProductNotFound_thenReturnEmpty()
	{
		givenGroupsForBpo(bpo, createGroup(), createGroup());
		thenGroupNextToProductGroupIs(Optional.empty());
	}

	@Test
	public void testFindGroupNextToProductGroupIsNotFound()
	{
		final TmaProductOfferingGroupModel group1 = createGroup(po);
		final TmaProductOfferingGroupModel group2 = createGroup();
		final TmaProductOfferingGroupModel group3 = createGroup();

		givenGroupsForBpo(bpo, group1, group2, group3);
		thenGroupNextToProductGroupIs(Optional.of(group2));
	}

	@Test
	public void testGetOfferingGroupForProductAndBpo()
	{
		final TmaProductOfferingGroupModel group1 = createGroup(po);
		final TmaProductOfferingGroupModel group2 = createGroup(po);

		givenGroupsForBpo(bpo, group1, createGroup());
		givenGroupsForBpo(createBpo("code2"), group2);
		thenGroupForPoAndBpo(Optional.of(group1));
	}

	@Test
	public void testGetOfferingGroupForProductAndBpoNoGroupFound()
	{
		final TmaProductOfferingGroupModel group1 = createGroup(po);

		givenGroupsForBpo(createBpo("code2"), group1);
		thenGroupForPoAndBpo(Optional.empty());
	}

	@Test
	public void testGetOfferingGroupForProductAndBpoWithEmptyPoGroup()
	{
		final TmaProductOfferingGroupModel group1 = createGroup();

		givenGroupsForBpo(bpo, group1);
		thenGroupForPoAndBpo(Optional.empty());
	}

	@Test
	public void testGetPoForCodeAndPriceContext()
	{
		final TmaPriceContext priceContext = new TmaPriceContext();
		final PriceRowModel priceRowModel = mock(PriceRowModel.class);
		priceRowModel.setPrice(Double.valueOf(PRICE_VALUE));
		final HashSet<PriceRowModel> priceRow = new HashSet<>();
		priceRow.add(priceRowModel);
		final TmaProductOfferingModel productModel = mock(TmaProductOfferingModel.class);
		final Collection<PriceRowModel> priceRows = new ArrayList<>();
		final PriceRowModel price = mock(PriceRowModel.class);
		priceRows.add(price);
		given(productModel.getEurope1Prices()).willReturn(priceRows);
		Mockito.when(commercePriceService.filterPricesbyPriceContext(priceContext)).thenReturn(priceRow);
		Mockito.when(productService.getProductForCode("code")).thenReturn(productModel);
		final List<ProductModel> productList = new ArrayList<>();
		productList.add(productModel);
		Mockito.when(mockProductDao.findProductsByCode("code")).thenReturn(productList);
		poService.getPoForCodeAndPriceContext("code", priceContext);
		Assert.assertNotNull(productModel);
		Assert.assertNotNull(productModel.getEurope1Prices());
	}

	@Test
	public void testGetOperationalPo()
	{
		final Optional<TmaOperationalProductOfferingModel> operationalProduct = this.poService
				.getOperationalProductOffering(TmaSubscribedProductAction.REMOVE);
		Assert.assertTrue("Operational product should exist", operationalProduct.isPresent());
		Assert.assertEquals(TmaSubscribedProductAction.REMOVE, operationalProduct.get().getActions().iterator().next());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetOperationalPoInvalidAction()
	{
		poService.getProductOffering(null, TmaSubscribedProductAction.ADD);
	}

	@Test
	public void testGetOperationalPoValidAction()
	{
		final TmaOperationalProductOfferingModel operationalProduct = new TmaOperationalProductOfferingModel();
		operationalProduct.setCode(OPERATIONAL_PRODUCT_REMOVE_CODE);
		final TmaOperationalProductOfferingModel operationalProductResult = (TmaOperationalProductOfferingModel) poService
				.getProductOffering(null, TmaSubscribedProductAction.REMOVE);
		Assert.assertNotNull("Operational product should exist", operationalProductResult);
		Assert.assertEquals(operationalProductResult.getCode(), OPERATIONAL_PRODUCT_REMOVE_CODE);
	}

	@Test
	public void testGetOperationalForKeep()
	{
		final TmaSubscribedProductModel subscribedProduct = new TmaSubscribedProductModel();
		subscribedProduct.setProductCode(PRODUCT_OFFERING_SALSA_S);
		final TmaProductOfferingModel productResult = (TmaProductOfferingModel) poService
				.getProductOffering(subscribedProduct.getProductCode(), TmaSubscribedProductAction.KEEP);
		Assert.assertEquals("Product code should match", PRODUCT_OFFERING_SALSA_S, productResult.getCode());
	}

	@Test
	public void testSPOListForBpo()
	{
		final TmaBundledProductOfferingModel bpo1 = createBpo("code1");
		final TmaSimpleProductOfferingModel spo1 = mock(TmaSimpleProductOfferingModel.class);
		spo1.setCode("spoCode1");
		final TmaSimpleProductOfferingModel spo2 = mock(TmaSimpleProductOfferingModel.class);
		spo2.setCode("spoCode2");

		final Set<TmaProductOfferingModel> poChildrenSet = new HashSet<>();
		poChildrenSet.add(spo1);
		poChildrenSet.add(spo2);

		bpo1.setChildren(poChildrenSet);
		final List<TmaSimpleProductOfferingModel> list = poService.getSpoListForBpo(bpo1);
		assertEquals(2, list.size());
	}

	@Test
	public void testAllChildSPOListForBpo()
	{
		final TmaBundledProductOfferingModel bpo1 = createBpo("code1");
		final TmaBundledProductOfferingModel bpo2 = createBpo("code2");
		final TmaSimpleProductOfferingModel spo1 = mock(TmaSimpleProductOfferingModel.class);
		spo1.setCode("spoCode1");
		final TmaSimpleProductOfferingModel spo2 = mock(TmaSimpleProductOfferingModel.class);
		spo2.setCode("spoCode2");

		final Set<TmaProductOfferingModel> bpo1ChildrenSet = new HashSet<>();
		bpo1ChildrenSet.add(bpo2);

		final Set<TmaProductOfferingModel> poChildrenSet = new HashSet<>();
		poChildrenSet.add(spo1);
		poChildrenSet.add(spo2);

		bpo1.setChildren(bpo1ChildrenSet);
		bpo2.setChildren(poChildrenSet);
		final List<TmaSimpleProductOfferingModel> list = poService.getSpoListForBpo(bpo1);
		assertEquals(2, list.size());
	}

	@Test
	public void testEmptySPOListForBpo()
	{
		final List<TmaSimpleProductOfferingModel> spoList = poService.getSpoListForBpo(createBpo("code1"));
		assertEquals(0, spoList.size());

		final List<TmaSimpleProductOfferingModel> spoListByPo = poService.getSpoListForBpo(bpo);
		assertEquals(0, spoListByPo.size());
	}

	private void givenParentsFor(final TmaProductOfferingModel po, final TmaBundledProductOfferingModel... parents)
	{
		po.setParents(Arrays.asList(parents).stream().collect(Collectors.toSet()));
		for (final TmaBundledProductOfferingModel parent : parents)
		{
			final Set<TmaProductOfferingModel> children = new HashSet<>();
			children.add(po);
			if (CollectionUtils.isNotEmpty(parent.getChildren()))
			{
				children.addAll(parent.getChildren());
			}
			parent.setChildren(children);
		}
	}

	private void givenGroupsForBpo(final TmaBundledProductOfferingModel bpo, final TmaProductOfferingGroupModel... groups)
	{
		bpo.setProductOfferingGroups(Arrays.asList(groups));
		for (final TmaProductOfferingGroupModel group : groups)
		{
			group.setParentBundleProductOffering(bpo);
		}
	}

	private TmaBundledProductOfferingModel createBpo(final String code)
	{
		final TmaBundledProductOfferingModel bpo = new TmaBundledProductOfferingModel();
		bpo.setCode(code);
		bpo.setChildren(Collections.EMPTY_SET);
		bpo.setProductOfferingGroups(Collections.EMPTY_LIST);
		return bpo;
	}

	private TmaProductOfferingGroupModel createGroup(final TmaProductOfferingModel... children)
	{
		final TmaProductOfferingGroupModel group = new TmaProductOfferingGroupModel();
		group.setChildProductOfferings(new HashSet<>(Arrays.asList(children)));
		for (final TmaProductOfferingModel child : children)
		{
			final List<TmaProductOfferingGroupModel> poGroups = new ArrayList<>();
			poGroups.add(group);
			if (CollectionUtils.isNotEmpty(child.getAssociatedProductOfferingGroups()))
			{
				poGroups.addAll(child.getAssociatedProductOfferingGroups());
			}
			child.setAssociatedProductOfferingGroups(poGroups);
		}
		return group;
	}

	private void thenParentsAre(final TmaProductOfferingModel po, final List<TmaBundledProductOfferingModel> expectedParents)
	{
		assertEquals(expectedParents, poService.getAllParents(po));
	}

	private void thenIsParentValid(final TmaProductOfferingModel po, final TmaBundledProductOfferingModel parent)
	{
		assertTrue(poService.isValidParent(po, parent));
	}

	private void thenGroupNextToProductGroupIs(final Optional<TmaProductOfferingGroupModel> expectedGroup)
	{
		assertEquals(expectedGroup, poService.getGroupNextToProductGroup(bpo, po));
	}

	private void thenGroupForPoAndBpo(final Optional<TmaProductOfferingGroupModel> expectedGroup)
	{
		assertEquals(expectedGroup, poService.getOfferingGroupForPoAndBpo(po, bpo));
	}
}
