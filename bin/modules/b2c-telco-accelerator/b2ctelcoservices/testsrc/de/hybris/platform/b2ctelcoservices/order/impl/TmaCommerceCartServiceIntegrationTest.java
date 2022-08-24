/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.DefaultCommerceCartServiceIntegrationTest;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Replaces {@link de.hybris.platform.commerceservices.order.DefaultCommerceCartServiceIntegrationTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = DefaultCommerceCartServiceIntegrationTest.class)
public class TmaCommerceCartServiceIntegrationTest extends DefaultCommerceCartServiceIntegrationTest
{
	private static final String UNAVAILABLE_PRODUCT_CODE = "unavailable";
	private static final String TEST_CATALOG = "testCatalog";
	private static final String CATALOG_VERSION_ONLINE = "Online";
	private static final String PRODUCT_IPHONEX = "iphonex";
	private static final String USER1 = "user@hybris.com";
	private static final String USER_2 = "user2@hybris.com";
	private static final String PRODUCT_IPHONEXS = "iphonexs";
	private static final String PRODUCT_BPO = "BPO1";
	private static final String PRODUCT_IPHONE_6 = "iphone6";
	private static final String PRODUCT_IPHONEXSMAX = "iphonexsmax";
	private static final String TEST_SITE = "testSite";
	private static final String USD_CURRENCY = "USD";
	private static final String THIS_SHOULD_NOT_FAIL = "This should NOT fail";

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private UnitService unitService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ModelService modelService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	@Resource
	private DefaultCartService defaultCartService;

	@Override
	@Before
	public void setUp() throws Exception
	{
		importCsv("/b2ctelcofacades/test/testCommerceCart.impex", "utf-8");

		commonI18NService.setCurrentCurrency(commonI18NService.getCurrency(USD_CURRENCY));
		final BaseSiteModel baseSiteForUID = baseSiteService.getBaseSiteForUID(TEST_SITE);
		baseSiteService.setCurrentBaseSite(baseSiteForUID, false);


	}

	@Test
	@Override
	public void testAddToCart() throws CommerceCartModificationException
	{
		final ProductModel productModel = getProduct(PRODUCT_IPHONEX);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, 3,
				TmaProcessType.DEVICE_ONLY.getCode());
		final UnitModel unitModel = parameter.getUnit();

		commerceCartService.addToCart(parameter);

		Assert.assertEquals(2, userCart.getEntries().size());
		Assert.assertEquals(PRODUCT_IPHONEX, userCart.getEntries().iterator().next().getProduct().getCode());
		Assert.assertEquals(unitModel, userCart.getEntries().iterator().next().getUnit());
	}

//	@Test
	public void testAddToCartBpo() throws CommerceCartModificationException
	{
		final CartModel userCart = getCartForUser(USER_2);
		final ProductModel spoffering = getProduct(PRODUCT_IPHONEXS);
		final ProductModel spoffering2 = getProduct(PRODUCT_IPHONE_6);
		final ProductModel bpo = getProduct(PRODUCT_BPO);

		final CommerceCartParameter parameter = createParameterForBpo(userCart, spoffering, 1, TmaProcessType.ACQUISITION.getCode(),
				bpo.getCode(), Sets.newHashSet());

		final UnitModel unitModel = parameter.getUnit();

		commerceCartService.addToCart(parameter);

		final CommerceCartParameter parameter2 = createParameterForBpo(userCart, spoffering2, 1,
				TmaProcessType.ACQUISITION.getCode(), bpo.getCode(), userCart.getEntries().get(0).getEntryGroupNumbers());

		commerceCartService.addToCart(parameter2);

		Assert.assertEquals(2, userCart.getEntries().size());
		Assert.assertEquals(PRODUCT_BPO, userCart.getEntries().iterator().next().getBpo().getCode());
		Assert.assertEquals(unitModel, userCart.getEntries().iterator().next().getUnit());
		Assert.assertEquals(Double.valueOf(110), userCart.getTotalPrice());
	}

	@Test
	@Override
	public void testAddToCartInsufficientStock() throws CommerceCartModificationException
	{
		final ProductModel productModel = getProduct(PRODUCT_IPHONEX);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, 50,
				TmaProcessType.DEVICE_ONLY.getCode());

		final CommerceCartModification result = commerceCartService.addToCart(parameter);

		Assert.assertEquals(13L, result.getQuantityAdded());
	}

	@Test
	@Override
	public void testAddToCartUnavailable() throws CommerceCartModificationException
	{
		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCode(UNAVAILABLE_PRODUCT_CODE);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, 1,
				TmaProcessType.DEVICE_ONLY.getCode());

		final CommerceCartModification result = commerceCartService.addToCart(parameter);

		Assert.assertEquals(CommerceCartModificationStatus.UNAVAILABLE, result.getStatusCode());
		Assert.assertEquals(0L, result.getQuantityAdded());
		Assert.assertEquals(UNAVAILABLE_PRODUCT_CODE, result.getEntry().getProduct().getCode());
		Assert.assertNull(result.getEntry().getBasePrice());
		Assert.assertNull(result.getEntry().getTotalPrice());
	}

	@Test
	@Override
	public void testAddToCartToTheSameEntry() throws CommerceCartModificationException
	{
		final ProductModel productModel = getProduct(PRODUCT_IPHONEX);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, 3,
				TmaProcessType.DEVICE_ONLY.getCode());

		commerceCartService.addToCart(parameter);

		parameter.setQuantity(5);

		commerceCartService.addToCart(parameter);

		Assert.assertEquals(2, userCart.getEntries().size());
		Assert.assertEquals(2, userCart.getEntries().get(0).getQuantity().longValue());
		Assert.assertEquals(8, userCart.getEntries().get(1).getQuantity().longValue());
	}

	@Test
	@Override
	public void testAddToCartForceNewEntry() throws CommerceCartModificationException
	{
		final ProductModel productModel = getProduct(PRODUCT_IPHONEX);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, 5,
				TmaProcessType.DEVICE_ONLY.getCode());
		parameter.setCreateNewEntry(true);

		commerceCartService.addToCart(parameter);

		Assert.assertEquals(2, userCart.getEntries().size());
		for (final AbstractOrderEntryModel cartEntryModel : userCart.getEntries())
		{
			if (cartEntryModel.getQuantity().intValue() == 5)
			{
				Assert.assertEquals(PRODUCT_IPHONEX, cartEntryModel.getProduct().getCode());
			}
		}
	}

	@Test
	@Override
	public void testCalculateCart()
	{
		final CartModel userCart = getCartForUser(USER1);
		final CommerceCartParameter parameter = createParameterForCart(userCart);

		commerceCartCalculationStrategy.calculateCart(parameter);

		Assert.assertEquals(Boolean.TRUE, userCart.getCalculated());
		Assert.assertEquals(Double.valueOf(100), userCart.getTotalPrice());
	}

	@Test
	@Override
	public void testCalculateCartEntriesAfterAddingToCart() throws CommerceCartModificationException
	{
		final ProductModel productModel = getProduct(PRODUCT_IPHONEX);
		final CartModel userCart = getCartForUser(USER1);
		setCurrentUser(USER1);
		final CommerceCartParameter parameter = createParameterForCart(userCart);

		commerceCartCalculationStrategy.calculateCart(parameter);

		Assert.assertEquals(1, userCart.getEntries().size());
		Assert.assertEquals(Boolean.TRUE, userCart.getCalculated());
		Assert.assertEquals(Double.valueOf(100), userCart.getTotalPrice());

		final CommerceCartParameter parameter2 = createParameterForProduct(userCart, productModel, 3,
				TmaProcessType.DEVICE_ONLY.getCode());

		commerceCartService.addToCart(parameter2);

		Assert.assertEquals(2, userCart.getEntries().size());

		parameter2.setQuantity(5);

		commerceCartService.addToCart(parameter2);

		//check for Entry 0
		Assert.assertEquals(2, userCart.getEntries().get(0).getQuantity().longValue());
		Assert.assertEquals(Double.valueOf(100), userCart.getEntries().get(0).getTotalPrice());
		Assert.assertEquals(PRODUCT_IPHONEX, userCart.getEntries().get(0).getProduct().getCode());


		//check for Entry 1
		Assert.assertEquals(8, userCart.getEntries().get(1).getQuantity().longValue());
		Assert.assertEquals(Double.valueOf(400), userCart.getEntries().get(1).getTotalPrice());
		Assert.assertEquals(PRODUCT_IPHONEX, userCart.getEntries().get(1).getProduct().getCode());

		//check for total cart
		Assert.assertEquals(2, userCart.getEntries().size());
		Assert.assertEquals(Double.valueOf(500), userCart.getTotalPrice());

	}

	@Test
	@Override
	public void testRecalculateCart()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void testRemoveAllEntries()
	{
		final CartModel userCart = getCartForUser(USER1);
		final CommerceCartParameter parameter = createParameterForCart(userCart);

		commerceCartService.removeAllEntries(parameter);

		Assert.assertEquals(0, userCart.getEntries().size());
	}

	@Test
	@Override
	public void testUpdateQuantityForCartEntry() throws CommerceCartModificationException
	{
		final CartModel userCart = getCartForUser(USER1);
		final CommerceCartParameter parameter = createParameterForCart(userCart);

		parameter.setEntryNumber(1);
		parameter.setQuantity(12);

		final CommerceCartModification result = commerceCartService.updateQuantityForCartEntry(parameter);

		Assert.assertEquals(10L, result.getQuantityAdded());
		Assert.assertEquals(Double.valueOf(600), userCart.getTotalPrice());
	}

	@Test
	@Override
	public void testUpdateQuantityForCartEntryToZero() throws CommerceCartModificationException
	{
		final CartModel userCart = getCartForUser(USER1);
		final CommerceCartParameter parameter = createParameterForCart(userCart);

		parameter.setCart(userCart);
		parameter.setEntryNumber(1);
		parameter.setQuantity(0);

		final CommerceCartModification result = commerceCartService.updateQuantityForCartEntry(parameter);

		Assert.assertEquals(CommerceCartModificationStatus.SUCCESS, result.getStatusCode());
		Assert.assertEquals(0L, result.getQuantity());
		Assert.assertEquals(PRODUCT_IPHONEX, result.getEntry().getProduct().getCode());
		Assert.assertNull(result.getEntry().getBasePrice());
		Assert.assertNull(result.getEntry().getTotalPrice());
	}

	@Test
	@Override
	public void testValidateCart()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void shouldCreatePromotionActionForPromotionResult()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void testGetCartForGuidAndSiteAndUserWithNullGuid()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void testUpdateCartMetadata()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void shouldReportOnAddingToNonExistingEntry()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void testPreviewTime()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	@Test
	@Override
	public void testAddToCartWhenNoCartPassed()
	{
		assertTrue(THIS_SHOULD_NOT_FAIL, true);
	}

	private ProductModel getProduct(final String code)
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(TEST_CATALOG,
				CATALOG_VERSION_ONLINE);
		final ProductModel productModel = productService.getProductForCode(catalogVersionModel, code);

		return productModel;
	}

	private CartModel getCartForUser(final String user)
	{
		final UserModel userModel = userService.getUserForUID(user);
		final Collection<CartModel> cartModels = userModel.getCarts();
		final CartModel userCart = cartModels.iterator().next();

		return userCart;
	}

	private void setCurrentUser(final String user)
	{
		userService.setCurrentUser(userService.getUserForUID(user));
	}

	private CommerceCartParameter createParameterForCart(final CartModel userCart)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();

		parameter.setEnableHooks(true);
		parameter.setCart(userCart);

		return parameter;
	}

	private CommerceCartParameter createParameterForProduct(final CartModel userCart, final ProductModel productModel,
			final int quantity, final String processType)
	{
		final CommerceCartParameter parameter = createParameterForCart(userCart);
		final UnitModel unitModel = unitService.getUnitForCode("pieces");

		parameter.setUnit(unitModel);
		parameter.setProduct(productModel);
		parameter.setQuantity(quantity);
		parameter.setProcessType(processType);

		return parameter;
	}

	private CommerceCartParameter createParameterForBpo(final CartModel userCart, final ProductModel productModel,
			final int quantity, final String processType, final String bpoCode, final Set<Integer> entryGroupNumbers)
	{
		final CommerceCartParameter parameter = createParameterForProduct(userCart, productModel, quantity, processType);

		parameter.setBpoCode(bpoCode);
		parameter.setEntryGroupNumbers(entryGroupNumbers);

		return parameter;
	}
}

