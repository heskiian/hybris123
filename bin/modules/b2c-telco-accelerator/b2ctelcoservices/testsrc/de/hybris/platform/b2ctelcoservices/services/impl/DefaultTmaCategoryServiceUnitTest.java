/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaCategoryDao;
import de.hybris.platform.b2ctelcoservices.services.TmaCategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


@UnitTest
public class DefaultTmaCategoryServiceUnitTest
{

	private TmaCategoryService categoryService;

	@Mock
	private TmaCategoryDao categoryDao;
	@Mock
	private CategoryModel mockModel1;
	@Mock
	private CategoryModel mockModel2;

	private Map<String, Object> params;
	private List<CategoryModel> categoryModelList;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		categoryService=new DefaultTmaCategoryService();
		((DefaultTmaCategoryService) categoryService).setTmaCategoryDao(categoryDao);
		this.params = new HashMap<>();
		this.categoryModelList = new ArrayList<>();
	}

	@Test
	public void testFilteringByName()
	{
		String name = "Devices";
		List<CategoryModel> mockList=new ArrayList<>();
		when(mockModel1.getName()).thenReturn(name);
		mockList.add(mockModel1);
		params.put(CategoryModel.NAME, name);
		when(categoryDao.findAllCategories(params)).thenReturn(mockList);
		categoryModelList = (List<CategoryModel>) categoryService.getAllCategories(params);
		Assert.assertEquals(categoryModelList.size(), 1);
		Assert.assertEquals(categoryModelList.get(0).getName(), name);
	}

	@Test
	public void testFilteringByRoot()
	{
		String categoryName = "isRoot";
		List<CategoryModel> emptyCategoriesList=new ArrayList<>();
		when(mockModel1.getAllSupercategories()).thenReturn(emptyCategoriesList);
		when(mockModel2.getAllSupercategories()).thenReturn(emptyCategoriesList);
		List<CategoryModel> mockList=new ArrayList<>();
		mockList.add(mockModel1);
		mockList.add(mockModel2);
		when(categoryDao.findAllCategories(params)).thenReturn(mockList);
		params.put(categoryName, true);
		Assert.assertTrue(checkListForRoot((List<CategoryModel>) categoryService.getAllCategories(params)));
	}

	private boolean checkListForRoot(List<CategoryModel> categoryModels)
	{
		for (CategoryModel categoryModel : categoryModels)
		{
			if (categoryModel.getAllSupercategories().size() != 0)
				return false;
		}
		return true;
	}
}
