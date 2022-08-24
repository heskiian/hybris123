/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.catalog.impl;

import de.hybris.platform.b2ctelcofacades.catalog.TmaCatalogFacade;
import de.hybris.platform.b2ctelcoservices.services.TmaCategoryService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.catalog.impl.DefaultCatalogFacade;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;


/**
 * Default implementation for {@link TmaCatalogFacade}.
 *
 * @since 1903
 */
public class DefaultTmaCatalogFacade extends DefaultCatalogFacade implements TmaCatalogFacade
{
	private static final String SLASH = "/";

	/**
	 * Converter for catalog entity from {@link CatalogModel} to {@link CatalogData}
	 */
	private Converter<CatalogModel, CatalogData> catalogConverter;

	/**
	 * The base store service
	 */
	private BaseStoreService baseStoreService;

	/**
	 * Base store converter
	 */
	private Converter<BaseStoreModel, BaseStoreData> baseStoreConverter;

	@Override
	public List<CategoryHierarchyData> getAllCategories(final String catalogId, final String catalogVersionId,
			final Map<String, Object> params,
			final PageOption page, final Set<CatalogOption> opts)
	{
		final List<CategoryHierarchyData> categoryHierarchyDataList = new ArrayList<CategoryHierarchyData>();

		final Collection<CategoryModel> categoryModelList = getCategoryService().getAllCategories(params);
		for (final CategoryModel categoryModel : categoryModelList)
		{
			final CategoryHierarchyData categoryHierarchyData = new CategoryHierarchyData();
			categoryHierarchyData.setUrl(catalogId + SLASH + catalogVersionId);
			getCategoryHierarchyPopulator().populate(categoryModel, categoryHierarchyData, opts, page);
			categoryHierarchyDataList.add(categoryHierarchyData);
		}

		return categoryHierarchyDataList;
	}

	@Override
	public Collection<CatalogData> getAllCatalogsForCurrentBaseStore(final String baseStoreId)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getBaseStoreForUid(baseStoreId);
		final Map<String, CatalogData> catalogDataMap = new HashMap<>();
		final BaseStoreData baseStoreData = new BaseStoreData();
		baseStoreData.setUid(baseStoreId);

		for (final CatalogModel catalogModel : baseStoreModel.getCatalogs())
		{
			final CatalogData catalogData = catalogConverter.convert(catalogModel);
			if (catalogData != null && !catalogDataMap.containsKey(catalogData.getId()))
			{
				catalogData.setBaseStore(baseStoreData);
				catalogDataMap.put(catalogData.getId(), catalogData);
			}
		}

		for (final BaseSiteModel baseSiteModel : baseStoreModel.getCmsSites())
		{
			final CMSSiteModel cmsSiteModel = (CMSSiteModel) baseSiteModel;
			for (final CatalogModel catalogModel : cmsSiteModel.getContentCatalogs())
			{
				final CatalogData catalogData = catalogConverter.convert(catalogModel);
				if (catalogData != null && !catalogDataMap.containsKey(catalogData.getId()))
				{
					catalogData.setBaseStore(baseStoreData);
					catalogDataMap.put(catalogData.getId(), catalogData);
				}
			}
		}
		return catalogDataMap.values();
	}

	@Override
	protected TmaCategoryService getCategoryService()
	{
		return (TmaCategoryService) super.getCategoryService();
	}

	protected Converter<CatalogModel, CatalogData> getCatalogConverter()
	{
		return catalogConverter;
	}

	@Required
	public void setCatalogConverter(
			final Converter<CatalogModel, CatalogData> catalogConverter)
	{
		this.catalogConverter = catalogConverter;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected Converter<BaseStoreModel, BaseStoreData> getBaseStoreConverter()
	{
		return baseStoreConverter;
	}

	@Required
	public void setBaseStoreConverter(
			final Converter<BaseStoreModel, BaseStoreData> baseStoreConverter)
	{
		this.baseStoreConverter = baseStoreConverter;
	}
}
