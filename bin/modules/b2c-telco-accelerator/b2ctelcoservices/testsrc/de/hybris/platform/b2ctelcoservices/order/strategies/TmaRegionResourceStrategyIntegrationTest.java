/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.impl.TmaRegionResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link TmaProductRegionStrategy} class.
 *
 * @since 2003
 */
@IntegrationTest
public class TmaRegionResourceStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String VALID_REGION_ISOCODE = "IN-ND";
	private static final String INVALID_REGION_ISOCODE = "IN-KA";
	private static final String USER = "user1@hybris.com";
	private static final String USER_CART = "user1Cart3";
	private static final int IPHONE_ENTRY_NUMBER = 1;

	@Resource
	private TmaRegionResourceStrategy tmaRegionResourceStrategy;
	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private TmaRegionService tmaRegionService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testSetRegionToCartEntry() throws CommerceCartModificationException
	{

		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final AbstractOrderEntryModel entry = getEntryByNumber(cart.getEntries(), IPHONE_ENTRY_NUMBER);

		final CommerceCartParameter commerceCartParameter = buildCommerceCartParameter(VALID_REGION_ISOCODE);
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		tmaRegionResourceStrategy.updateResource(commerceCartParameter, cartModification);
		Assert.assertNotNull(entry.getRegion());
		Assert.assertEquals(VALID_REGION_ISOCODE, entry.getRegion().getIsocode());
	}


	@Test
	public void testValidateDuplicateProductRegion()
	{
		final CommerceCartParameter commerceCartParameter = buildCommerceCartParameter(VALID_REGION_ISOCODE);

		final RegionModel region = new RegionModel();
		region.setIsocode(VALID_REGION_ISOCODE);

		final TmaRegionPlace tmaRegionPlace = new TmaRegionPlace();
		tmaRegionPlace.setRole(TmaPlaceRoleType.PRODUCT_REGION);
		tmaRegionPlace.setRegion(region);

		commerceCartParameter.getPlaces().add(tmaRegionPlace);
		final TmaCartValidationResult tmaCartValidationResult = tmaRegionResourceStrategy.validateResource(commerceCartParameter);
		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Found multiple regions/places with role: " + TmaPlaceRoleType.PRODUCT_REGION,
				tmaCartValidationResult.getMessage());
	}

	@Test
	public void testInvalidRegion()
	{
		final RegionModel region = new RegionModel();
		region.setIsocode(INVALID_REGION_ISOCODE);

		final TmaRegionPlace tmaRegionPlace = new TmaRegionPlace();
		tmaRegionPlace.setRole(TmaPlaceRoleType.PRODUCT_REGION);

		tmaRegionPlace.setRegion(region);
		final List<TmaPlace> tmaPlaces = new ArrayList<>();
		tmaPlaces.add(tmaRegionPlace);

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setPlaces(tmaPlaces);

		final TmaCartValidationResult tmaCartValidationResult = tmaRegionResourceStrategy.validateResource(commerceCartParameter);
		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Invalid region/place: " + INVALID_REGION_ISOCODE, tmaCartValidationResult.getMessage());
	}

	private CommerceCartParameter buildCommerceCartParameter(final String regionIsoCode)
	{
		final TmaRegionPlace tmaRegionPlace = new TmaRegionPlace();
		tmaRegionPlace.setRole(TmaPlaceRoleType.PRODUCT_REGION);
		tmaRegionPlace.setRegion(tmaRegionService.findRegionByIsocode(regionIsoCode));
		final List<TmaPlace> tmaPlaces = new ArrayList<>();
		tmaPlaces.add(tmaRegionPlace);

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setPlaces(tmaPlaces);

		return commerceCartParameter;
	}

	private AbstractOrderEntryModel getEntryByNumber(final List<AbstractOrderEntryModel> entries, final int entryNumber)
	{
		return entries.stream().filter(entry -> entry.getEntryNumber().equals(entryNumber)).findFirst().orElse(null);
	}
}
