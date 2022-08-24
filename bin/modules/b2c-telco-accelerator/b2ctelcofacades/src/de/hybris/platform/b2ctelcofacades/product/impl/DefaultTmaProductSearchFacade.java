/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.data.ProductOfferingSearchContextData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductSearchFacade;
import de.hybris.platform.b2ctelcoservices.data.TmaProductOfferingSearchContext;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductResultData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.search.solrfacetsearch.impl.DefaultSolrProductSearchFacade;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * {@inheritDoc}
 *
 * @param <T>
 *           Class that extends {@link ProductData}
 * @since 1907
 */
public class DefaultTmaProductSearchFacade<T extends ProductData> extends DefaultSolrProductSearchFacade<T>
		implements TmaProductSearchFacade<T>
{
	/**
	 * Separator for facet search option identifier
	 */
	private static final String FACET_SEARCH_OPTION_ID_SEPARATOR = "__";

	/**
	 * Used for converting data from {@link SolrSearchQueryData} to {@link SearchStateData}
	 */
	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
	private CatalogVersionService catalogVersionService;
	private ProductCategoriesPopulator productCategoriesPopulator;
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	private Converter<ProductModel, ProductData> productConverter;
	private TmaPoService tmaPoService;
	private Converter<ProductOfferingSearchContextData, TmaProductOfferingSearchContext> commerceProductOfferingSearchContextConverter;

	/**
	 * Calculates page number from offset and limit and returns those page/pages which contain the requested Product
	 * Offerings.
	 *
	 * @param searchQuery
	 *           The query to be executed
	 * @param offset
	 *           The nth Product Offering from which limit number of Product Offerings will be sent (starts from 0)
	 * @param limit
	 *           The number of Product Offerings
	 * @return List of {@link ProductSearchPageData} which contains the requested product offerings
	 */
	@Override
	public List<FacetSearchPageData<SearchStateData, T>> getSolrSearchResults(final SolrSearchQueryData searchQuery,
			final Integer offset, final Integer limit)
	{
		final List<FacetSearchPageData<SearchStateData, T>> sourceResults = new ArrayList<>();

		FacetSearchPageData<SearchStateData, T> sourceResult = searchProducts(searchQuery, offset / limit, limit);
		if (sourceResult == null)
		{
			return sourceResults;
		}

		sourceResults.add(sourceResult);

		if (sourceResult.getPagination().getTotalNumberOfResults() == 0)
		{
			final FacetSearchPageData<SearchStateData, T> temporarySourceResult = searchProducts(searchQuery, 0, limit);
			sourceResult.getPagination().setTotalNumberOfResults(temporarySourceResult.getPagination().getTotalNumberOfResults());
			sourceResults.add(sourceResult);
			return sourceResults;
		}

		if (offset == 0)
		{
			return sourceResults;
		}

		sourceResult = searchProducts(searchQuery, (offset / limit) + 1, limit);
		if (sourceResult != null)
		{
			sourceResults.add(sourceResult);
		}

		return sourceResults;
	}

	@Override
	public ProductResultData getProductOfferingsForOptions(final ProductOfferingSearchContextData productOfferingSearchContextData,
			final Collection<ProductOption> options)
	{
		final String catalog = productOfferingSearchContextData.getCatalog();
		final String version = productOfferingSearchContextData.getVersion();
		final int start = productOfferingSearchContextData.getCurrentPage();
		final int count = productOfferingSearchContextData.getPageSize();
		final Collection<CatalogVersionModel> exportedCatalogVersions = getCatalogVerionByName(catalog, version);
		final TmaProductOfferingSearchContext productOfferingSearchContext = getCommerceProductOfferingSearchContextConverter()
				.convert(productOfferingSearchContextData);
		final SearchPageData<TmaProductOfferingModel> searchResult = getTmaPoService().getProductOfferings(exportedCatalogVersions,
				productOfferingSearchContext);
		final List<TmaProductOfferingModel> models = searchResult.getResults();
		final List<ProductData> productsData = convertPoModelsToDTOs(options, models);
		return createResultDataWithPagination(start, count, productsData, searchResult.getPagination().getPageSize(),
				searchResult.getPagination().getTotalNumberOfResults());
	}

	@Override
	public List<SolrSearchQueryTermData> decodeFacetSearchQuery(final List<String> facetSearchOptionList)
	{
		if (CollectionUtils.isEmpty(facetSearchOptionList))
		{
			return Collections.emptyList();
		}

		final List<SolrSearchQueryTermData> solrSearchQueryTermDataList = new ArrayList<>();

		for (final String facetSearchOption : facetSearchOptionList)
		{
			if (StringUtils.countMatches(facetSearchOption, FACET_SEARCH_OPTION_ID_SEPARATOR) != 1)
			{
				throw new IllegalArgumentException("Invalid facet search option: '" + facetSearchOption + "'.");
			}

			final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
			solrSearchQueryTermData.setKey(facetSearchOption.split(FACET_SEARCH_OPTION_ID_SEPARATOR)[0]);
			solrSearchQueryTermData.setValue(facetSearchOption.split(FACET_SEARCH_OPTION_ID_SEPARATOR)[1]);
			solrSearchQueryTermDataList.add(solrSearchQueryTermData);
		}

		return solrSearchQueryTermDataList;
	}

	protected List<ProductData> convertPoModelsToDTOs(final Collection<ProductOption> options,
			final List<TmaProductOfferingModel> models)
	{
		final List<ProductData> productsData = new ArrayList<>();
		models.forEach(model -> {
			final ProductData data = getProductConverter().convert(model);

			if (options != null)
			{
				getProductConfiguredPopulator().populate(model, data, options);
			}

			getProductCategoriesPopulator().populate(model, data);

			productsData.add(data);
		});

		return productsData;
	}

	protected ProductResultData createResultDataWithPagination(final int start, final int count,
			final List<ProductData> productsData, final int pageSize, final long totalNumberOfResults)
	{
		final ProductResultData result = new ProductResultData();
		result.setProducts(productsData);
		result.setCount(pageSize);
		result.setRequestedCount(count);
		result.setRequestedStart(start);
		result.setTotalCount((int) totalNumberOfResults);
		return result;
	}

	protected Collection<CatalogVersionModel> getCatalogVerionByName(final String catalog, final String version)
	{
		if (catalog == null && version == null)
		{
			return getCatalogVersionService().getSessionCatalogVersions();
		}
		else
		{
			return Collections.singletonList(getCatalogVersionService().getCatalogVersion(catalog, version));
		}
	}

	/**
	 * Gets from Solr a page of Product Offerings.
	 *
	 * @param searchQueryData
	 *           The query to be executed
	 * @param offset
	 *           The nth Product Offering from which limit number of Product Offerings will be sent (starts from 0)
	 * @param limit
	 *           The number of Product Offerings
	 * @return A {@link ProductSearchPageData} containing the Product Offerings
	 */
	private FacetSearchPageData<SearchStateData, T> searchProducts(final SolrSearchQueryData searchQueryData, final Integer offset,
			final Integer limit)
	{
		final PageableData pageable = createPageableData(offset, limit);

		return super.textSearch(getSolrSearchStateConverter().convert(searchQueryData), pageable);
	}

	/**
	 * Creates a {@link PageableData} object and sets currentPage and pageSize attributes.
	 *
	 * @param currentPage
	 *           The current page
	 * @param pageSize
	 *           The page size
	 * @return A {@link PageableData} object
	 */
	private PageableData createPageableData(final int currentPage, final int pageSize)
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		return pageable;
	}

	public Converter<SolrSearchQueryData, SearchStateData> getSolrSearchStateConverter()
	{
		return solrSearchStateConverter;
	}

	@Required
	public void setSolrSearchStateConverter(final Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter)
	{
		this.solrSearchStateConverter = solrSearchStateConverter;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected ProductCategoriesPopulator getProductCategoriesPopulator()
	{
		return productCategoriesPopulator;
	}

	@Required
	public void setProductCategoriesPopulator(final ProductCategoriesPopulator productCategoriesPopulator)
	{
		this.productCategoriesPopulator = productCategoriesPopulator;
	}

	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	@Required
	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected Converter<ProductOfferingSearchContextData, TmaProductOfferingSearchContext> getCommerceProductOfferingSearchContextConverter()
	{
		return commerceProductOfferingSearchContextConverter;
	}

	@Required
	public void setCommerceProductOfferingSearchContextConverter(
			final Converter<ProductOfferingSearchContextData, TmaProductOfferingSearchContext> commerceProductOfferingSearchContextConverter)
	{
		this.commerceProductOfferingSearchContextConverter = commerceProductOfferingSearchContextConverter;
	}
}
