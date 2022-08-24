/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link TmaPoVariantValidateInterceptor}
 *
 * @since 1810
 */
@UnitTest
public class TmaPoVariantValidateInterceptorTest
{

	private TmaPoVariantValidateInterceptor variantInterceptor;

	@Mock
	private InterceptorContext interceptorContext;
	@Mock
	private L10NService l10NService;
	@Mock
	private SessionService sessionService;
	@Mock
	private TmaPoVariantModel poVariant;
	@Mock
	private CategoryModel devicesCategory;
	@Mock
	private VariantCategoryModel colorCategory;
	@Mock
	private VariantCategoryModel storageSizeCategory;
	@Mock
	private VariantValueCategoryModel silverCategory;
	@Mock
	private VariantValueCategoryModel gb16Category;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.variantInterceptor = new TmaPoVariantValidateInterceptor();
		this.variantInterceptor.setL10NService(l10NService);
		this.variantInterceptor.setSessionService(sessionService);

		Mockito.when(storageSizeCategory.getSupercategories()).thenReturn(Arrays.asList(colorCategory));
		Mockito.when(silverCategory.getSupercategories()).thenReturn(Arrays.asList(colorCategory));
		Mockito.when(gb16Category.getSupercategories()).thenReturn(Arrays.asList(storageSizeCategory));
	}

	@Test
	public void givenSyncIsActive_thenValidationPasses() throws InterceptorException
	{
		givenSyncActive(true);
		whenValidateInterceptorIsCalled();
	}

	@Test
	public void givenBaseProductWithoutCategories_thenValidationPasses() throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x");
		givenPoVariant("iPhone_x_silver", silverCategory);
		whenValidateInterceptorIsCalled();
	}

	@Test
	public void givenVariantWithOneCategory_thenValidationPasses() throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory);
		givenPoVariant("iPhone_x_silver", silverCategory);
		whenValidateInterceptorIsCalled();
	}

	@Test
	public void givenVariantWithTwoCategories_thenValidationPasses() throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory, storageSizeCategory);
		givenPoVariant("iPhone_x_silver_16_gb", silverCategory, gb16Category);
		whenValidateInterceptorIsCalled();
	}

	@Test
	public void givenVariantWithOneExtraCategory_thenValidationPasses() throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory);
		givenPoVariant("iPhone_x_silver", silverCategory, devicesCategory);
		whenValidateInterceptorIsCalled();
	}


	@Test(expected = InterceptorException.class)
	public void givenVariantWithoutBaseProduct_thenThrowNoBaseProductError() throws InterceptorException
	{
		givenSyncActive(false);
		givenPoVariant("iPhone_x_silver", silverCategory);
		whenValidateInterceptorIsCalled();
	}


	@Test(expected = InterceptorException.class)
	public void givenVariantWithoutSupercategories_thenThrowWrongSupercategoryError()
			throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory, storageSizeCategory);
		givenPoVariant("iphone_x_silver");
		whenValidateInterceptorIsCalled();
	}

	@Test(expected = InterceptorException.class)
	public void givenVariantCategoriesDoNotMatchBaseProductCategories_thenThrowNoSameAmountOfVariantCategoriesError() throws
			InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory, storageSizeCategory);
		givenPoVariant("iPhone_x_silver", silverCategory);
		whenValidateInterceptorIsCalled();
	}

	@Test(expected = InterceptorException.class)
	public void givenVariantCategoriesAreNotInlineWithBaseProductCategories_thenThrowNoSameAmountOfVariantCategoriesError
			() throws	InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", colorCategory, storageSizeCategory);
		givenPoVariant("iPhone_x_silver", silverCategory, silverCategory);
		whenValidateInterceptorIsCalled();
	}

	@Test(expected = InterceptorException.class)
	public void givenVariantCategoryDoesNotHaveBaseProductCategoryAsSuperCategory_thenThrowVariantCategoryNotInBaseProductError
			() throws InterceptorException
	{
		givenSyncActive(false);
		givenBaseProduct("iPhone_x", storageSizeCategory);
		givenPoVariant("iPhone_x_silver", silverCategory);
		whenValidateInterceptorIsCalled();
	}

	private void givenBaseProduct(String code, CategoryModel... supercategories)
	{
		TmaSimpleProductOfferingModel basePo = Mockito.mock(TmaSimpleProductOfferingModel.class);
		Mockito.when(basePo.getCode()).thenReturn(code);
		Mockito.when(basePo.getSupercategories()).thenReturn(Arrays.asList(supercategories));
		Mockito.when(poVariant.getTmaBasePo()).thenReturn(basePo);
	}

	private void givenPoVariant(String code, CategoryModel... supercategories)
	{
		Mockito.when(poVariant.getCode()).thenReturn(code);
		Mockito.when(poVariant.getSupercategories()).thenReturn(Arrays.asList(supercategories));
	}

	private void givenSyncActive(boolean isActive)
	{
		Session currentSession = Mockito.mock(Session.class);
		Mockito.when(currentSession.getAttribute(TmaPoVariantValidateInterceptor.CATALOG_SYNC_ACTIVE_ATTRIBUTE))
				.thenReturn(isActive);
		Mockito.when(sessionService.getCurrentSession()).thenReturn(currentSession);
	}

	private void whenValidateInterceptorIsCalled() throws InterceptorException
	{
		variantInterceptor.onValidate(poVariant, interceptorContext);
	}
}
