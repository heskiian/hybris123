/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistService;
import de.hybris.platform.b2ctelcoservices.checklist.context.TmaChecklistContext;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.Assert;

import java.util.Arrays;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Integration Test for {@link TmaChecklistService}
 *
 * @since 1907
 */
@IntegrationTest
public class DefaultTmaChecklistServiceTest extends ServicelayerTransactionalTest
{
	private static final String SALSA_S = "salsa_s";
	private static final String INTERNET_FOR_HOME = "internet_for_home";
	private static final String SOMBRERO_S = "sombrero_s";
	private static final String USER = "user1@hybris.com";
	private static final String CART_WITHOUT_MSISDN = "user1Cart1";
	private static final String CART_WITH_MSISDN = "user1Cart2";
	private static final String CART_WITHOUT_APPOINTMENT = "user1Cart3";
	private static final String CART_WITH_APPOINTMENT = "user1Cart4";
	private static final String CART_WITHOUT_ADDRESS = "user1Cart5";
	private static final String CART_WITH_ADDRESS = "user1Cart6";
	private static final String SALSA_S_PRODUCT_CODE = "salsaS";
	private static final String SOMBRERO_PRODUCT_CODE = "sombreroS";

	@Resource
	private TmaChecklistService tmaChecklistService;

	@Resource
	private TmaPoService tmaPoService;
	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_checklistPolicies.impex", "utf-8");
		importCsv("/test/impex/test_placeOrderValidation.impex", "utf-8");
	}

	@Test
	public void findActionWithWrongProduct()
	{
		final TmaProductOfferingModel productOffering1 = getTmaPoService().getPoForCode(INTERNET_FOR_HOME);

		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.ACQUISITION, productOffering1);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		Assert.assertEquals(0, results.size());
	}

	@Test
	public void findActionWithRightProduct()
	{
		final TmaProductOfferingModel productOffering1 = getTmaPoService().getPoForCode(SALSA_S);

		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.ACQUISITION, productOffering1);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		doAssertionsOnResult(results, "requires_msisdn_selection", "requires_msisdn");
	}

	@Test
	public void findActionWithMultipleProductContext()
	{
		final TmaProductOfferingModel productOffering1 = getTmaPoService().getPoForCode(SALSA_S);
		final TmaProductOfferingModel productOffering2 = getTmaPoService().getPoForCode(INTERNET_FOR_HOME);

		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.ACQUISITION, productOffering1,
				productOffering2);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		doAssertionsOnResult(results, "requires_msisdn_selection", "requires_msisdn");
	}

	@Test
	public void findActionWithRequiredAppointment()
	{

		final TmaProductOfferingModel productOffering2 = getTmaPoService().getPoForCode(INTERNET_FOR_HOME);

		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.RELOCATION, productOffering2);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		doAssertionsOnResult(results, "requires_do_appointment", "do_appointment");
	}

	@Test
	public void findActionWithMultiProductContextRequiredAppointment()
	{

		final TmaProductOfferingModel productOffering1 = getTmaPoService().getPoForCode(SALSA_S);
		final TmaProductOfferingModel productOffering2 = getTmaPoService().getPoForCode(INTERNET_FOR_HOME);

		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.RELOCATION, productOffering1,
				productOffering2);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		doAssertionsOnResult(results, "requires_do_appointment", "do_appointment");
	}


	@Test
	public void findActionWithRequiredServiceAbilityCheck()
	{

		final TmaProductOfferingModel productOffering = getTmaPoService().getPoForCode(SOMBRERO_S);
		final TmaChecklistContext tmaChecklistContext = createContext(TmaProcessType.ACQUISITION, productOffering);

		final Set<RuleEvaluationResult> results = getTmaChecklistService().findActions(tmaChecklistContext);

		doAssertionsOnResult(results, "requires_serviceability_check", "serviceability_check");
	}

	@Test
	public void testValidationForMissingMsisdnOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITHOUT_MSISDN, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(false, areActionsFulfilled);

		final CartEntryModel salsaSEntry = (CartEntryModel) getEntryByProductCode(cart, SALSA_S_PRODUCT_CODE);
		assertFalse(salsaSEntry.getCartEntryValidations().isEmpty());
		assertNotNull(getValidationByCode(salsaSEntry, TmaChecklistActionType.MSISDN.getCode()));
	}

	@Test
	public void testValidationWhenMsisdnIsSetOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITH_MSISDN, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(true, areActionsFulfilled);

		final CartEntryModel salsaSEntry = (CartEntryModel) getEntryByProductCode(cart, SALSA_S_PRODUCT_CODE);
		assertTrue(salsaSEntry.getCartEntryValidations().isEmpty());
		assertNull(getValidationByCode(salsaSEntry, TmaChecklistActionType.MSISDN.getCode()));
	}

	@Test
	public void testValidationForMissingAppointmentOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITHOUT_APPOINTMENT, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(false, areActionsFulfilled);

		final CartEntryModel internetEntry = (CartEntryModel) getEntryByProductCode(cart, INTERNET_FOR_HOME);
		assertFalse(internetEntry.getCartEntryValidations().isEmpty());
		assertNotNull(getValidationByCode(internetEntry, TmaChecklistActionType.APPOINTMENT.getCode()));
	}

	@Test
	public void testValidationForAppointmentSetOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITH_APPOINTMENT, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(true, areActionsFulfilled);

		final CartEntryModel salsaSEntry = (CartEntryModel) getEntryByProductCode(cart, INTERNET_FOR_HOME);
		assertTrue(salsaSEntry.getCartEntryValidations().isEmpty());
		assertNull(getValidationByCode(salsaSEntry, TmaChecklistActionType.APPOINTMENT.getCode()));
	}

	@Test
	public void testValidationForInstallationAddressNotSetOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITHOUT_ADDRESS, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(false, areActionsFulfilled);

		final CartEntryModel internetEntry = (CartEntryModel) getEntryByProductCode(cart, SOMBRERO_PRODUCT_CODE);
		assertFalse(internetEntry.getCartEntryValidations().isEmpty());
		assertNotNull(getValidationByCode(internetEntry, TmaChecklistActionType.INSTALLATION_ADDRESS.getCode()));
	}

	@Test
	public void testValidationForInstallationAddressSetOnOrderEntry()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(CART_WITH_ADDRESS, user);

		boolean areActionsFulfilled = getTmaChecklistService().areActionsFulfilled(cart);
		Assert.assertEquals(true, areActionsFulfilled);

		final CartEntryModel salsaSEntry = (CartEntryModel) getEntryByProductCode(cart, SOMBRERO_PRODUCT_CODE);
		assertTrue(salsaSEntry.getCartEntryValidations().isEmpty());
		assertNull(getValidationByCode(salsaSEntry, TmaChecklistActionType.INSTALLATION_ADDRESS.getCode()));
	}

	protected TmaCartValidationModel getValidationByCode(final CartEntryModel salsaSEntry, final String code)
	{
		return salsaSEntry.getCartEntryValidations().stream()
				.filter(cartEntryValidation -> cartEntryValidation.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	protected AbstractOrderEntryModel getEntryByProductCode(final CartModel cart, final String productCode)
	{
		return cart.getEntries().stream().filter(entry -> entry.getProduct().getCode().equals(productCode)).findFirst()
				.orElse(null);
	}

	protected TmaChecklistContext createContext(final TmaProcessType processType,
			final TmaProductOfferingModel... productOfferings)
	{
		final TmaChecklistContext tmaChecklistContext = new TmaChecklistContext();
		tmaChecklistContext.setProcessType(processType);
		tmaChecklistContext.setProductOfferings(Arrays.asList(productOfferings));
		return tmaChecklistContext;
	}

	protected void doAssertionsOnResult(final Set<RuleEvaluationResult> results, final String actionCode,
			String statementCode)
	{
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(actionCode, results.iterator().next().getAction().getCode());
		Assert.assertEquals(statementCode, results.iterator().next().getAction().getStatement().getCode());
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected TmaChecklistService getTmaChecklistService()
	{
		return tmaChecklistService;
	}
}
