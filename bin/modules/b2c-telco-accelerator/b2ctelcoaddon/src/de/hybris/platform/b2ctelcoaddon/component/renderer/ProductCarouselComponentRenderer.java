/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoaddon.component.renderer;

import de.hybris.platform.acceleratorfacades.productcarousel.ProductCarouselFacade;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.b2ctelcoaddon.constants.B2ctelcoaddonConstants;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;


/**
 * Renderer for product carousel component
 *
 * @since 2011
 */
public class ProductCarouselComponentRenderer<C extends ProductCarouselComponentModel> extends DefaultAddOnCMSComponentRenderer<C>
{

	/**
	 * Product Data Constant
	 */
	private static final String PRODUCT_DATA = "productData";

	/**
	 * Title Constant
	 */
	private static final String TITLE = "title";

	/**
	 * Component Constant
	 */
	private static final String COMPONENT = "component";

	/**
	 * Page Size Constant
	 */
	public static final int PAGE_SIZE = 100;

	/**
	 * Product Search Facade
	 */
	private ProductSearchFacade<ProductData> productSearchFacade;

	/**
	 * Product Carousel Facade
	 */
	private ProductCarouselFacade productCarouselFacade;

	public ProductCarouselComponentRenderer(final ProductSearchFacade<ProductData> productSearchFacade,
			final ProductCarouselFacade productCarouselFacade)
	{
		this.productSearchFacade = productSearchFacade;
		this.productCarouselFacade = productCarouselFacade;
	}

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final List<ProductData> products = new ArrayList<>();

		products.addAll(collectLinkedProducts(component));
		products.addAll(collectSearchProducts(component));

		final Map<String, Object> model = super.getVariablesToExpose(pageContext, component);

		model.put(PRODUCT_DATA, products);
		model.put(TITLE, component.getTitle());
		model.put(COMPONENT, component);
		return model;
	}

	protected List<ProductData> collectLinkedProducts(final ProductCarouselComponentModel component)
	{
		return getProductCarouselFacade().collectProducts(component);
	}

	protected List<ProductData> collectSearchProducts(final ProductCarouselComponentModel component)
	{
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(component.getSearchQuery());
		final String categoryCode = component.getCategoryCode();

		if (searchQueryData.getValue() != null && categoryCode != null)
		{
			final SearchStateData searchState = new SearchStateData();
			searchState.setQuery(searchQueryData);

			final PageableData pageableData = new PageableData();
			pageableData.setPageSize(PAGE_SIZE); // Limit to 100 matching results

			return getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData).getResults();
		}

		return Collections.emptyList();
	}

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return B2ctelcoaddonConstants.EXTENSIONNAME;
	}

	protected ProductSearchFacade<ProductData> getProductSearchFacade()
	{
		return productSearchFacade;
	}

	protected ProductCarouselFacade getProductCarouselFacade()
	{
		return productCarouselFacade;
	}
}
