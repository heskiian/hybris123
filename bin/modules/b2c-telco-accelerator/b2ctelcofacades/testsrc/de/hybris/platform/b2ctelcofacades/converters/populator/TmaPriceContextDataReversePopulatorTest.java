/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test class of {@link TmaPriceContextDataReversePopulator}.
 *
 * @since 1907
 */
@UnitTest
public class TmaPriceContextDataReversePopulatorTest
{
	private static final String PRODUCT_CODE = "testProduct";
	private static final String REGION_CODE = "testRegion";
	private static final String USER_ID = "testUser";
	private static final String CURRENCY_ISOCODE = "testCurrency";
	private static final String AFFECTED_PRODUCT_CODE = "testAffectedProduct";
	private static final String SUBSCRIPTION_TERM_ID = "testSubscriptionId";
	private static final String REQUIRED_PRODUCT_CODE = "testRequiredProduct";

	private TmaPriceContextDataReversePopulator priceContextDataReversePopulator;

	@Mock
	private TmaPoService tmaPoService;
	@Mock
	private TmaRegionService tmaRegionService;
	@Mock
	private UserService userService;
	@Mock
	private CommonI18NService i18NService;
	@Mock
	private TmaSubscriptionTermService tmaSubscriptionTermService;
	@Mock
	private EnumerationService enumerationService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		priceContextDataReversePopulator = new TmaPriceContextDataReversePopulator();
		priceContextDataReversePopulator.setTmaPoService(tmaPoService);
		priceContextDataReversePopulator.setTmaRegionService(tmaRegionService);
		priceContextDataReversePopulator.setUserService(userService);
		priceContextDataReversePopulator.setI18NService(i18NService);
		priceContextDataReversePopulator.setTmaSubscriptionTermService(tmaSubscriptionTermService);
		priceContextDataReversePopulator.setEnumerationService(enumerationService);
	}


	@Test
	public void testPopulate()
	{
		final TmaPriceContextData source = mock(TmaPriceContextData.class);
		final TmaPriceContext target = new TmaPriceContext();

		given(source.getProductCode()).willReturn(PRODUCT_CODE);
		given(source.getProcessTypeCodes()).willReturn(new HashSet<String>(Arrays.asList(TmaProcessType.ACQUISITION.getCode())));
		given(source.getRegionIsoCodes()).willReturn(new HashSet<String>(Arrays.asList(REGION_CODE)));
		given(source.getUserId()).willReturn(USER_ID);
		given(source.getCurrencyIsoCode()).willReturn(CURRENCY_ISOCODE);
		given(source.getAffectedProductCode()).willReturn(AFFECTED_PRODUCT_CODE);
		given(source.getSubscriptionTermIds()).willReturn(new HashSet<String>(Arrays.asList(SUBSCRIPTION_TERM_ID)));
		given(source.getRequiredProductCodes()).willReturn(new HashSet<String>(Arrays.asList(REQUIRED_PRODUCT_CODE)));

		final TmaProductOfferingModel product = mock(TmaProductOfferingModel.class);
		product.setCode(PRODUCT_CODE);
		Mockito.when(tmaPoService.getPoForCode(PRODUCT_CODE)).thenReturn(product);

		final UserModel user = mock(UserModel.class);
		user.setUid(USER_ID);
		Mockito.when(userService.getUserForUID(USER_ID)).thenReturn(user);

		final CurrencyModel currency = mock(CurrencyModel.class);
		currency.setIsocode(CURRENCY_ISOCODE);
		Mockito.when(i18NService.getCurrency(CURRENCY_ISOCODE)).thenReturn(currency);

		final TmaProductOfferingModel affectedProduct = mock(TmaProductOfferingModel.class);
		affectedProduct.setCode(AFFECTED_PRODUCT_CODE);
		Mockito.when(tmaPoService.getPoForCode(AFFECTED_PRODUCT_CODE)).thenReturn(affectedProduct);

		priceContextDataReversePopulator.populate(source, target);

		Assert.assertNotNull(target.getProduct());
		Assert.assertNotNull(target.getRegions());
		Assert.assertNotNull(target.getProcessTypes());
		Assert.assertNotNull(target.getUser());
	}

	@Test
	public void testPopulateNull()
	{
		final TmaPriceContextData source = mock(TmaPriceContextData.class);
		final TmaPriceContext target = new TmaPriceContext();

		priceContextDataReversePopulator.populate(source, target);

		Assert.assertNull(target.getProduct());
		Assert.assertNull(target.getRegions());
		Assert.assertNull(target.getProcessTypes());
		Assert.assertNull(target.getUser());
	}
}
