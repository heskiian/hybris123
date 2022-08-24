/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcotmfwebservices.v2.api.CategoryApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Category;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcofacades.catalog.TmaCatalogFacade;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.site.BaseSiteService;

import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link CategoryApi}
 *
 * @since 1903
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "category", description = "Catalog Management APIs", tags = {"Product Catalog Management"})
public class TmaCategoryApiController extends TmaBaseController implements CategoryApi
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaCategoryApiController.class);

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "catalogFacade")
	private TmaCatalogFacade catalogFacade;

	@ApiOperation(value = "Retrieves a 'Category' by Id", nickname = "retrieveCategory", response = Category.class, responseContainer = "List", tags = {
			"Product Catalog Management", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = Category.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/category/{id}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	public ResponseEntity<List<Category>> retrieveCategory(
			@ApiParam(value = "Identifier of the Category", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) String baseSiteId)
	{
		try
		{
			final CategoryHierarchyData categoryHierarchyData = searchCategory(id);
			final Category category = getDataMapper().map(categoryHierarchyData, Category.class, fields);
			final List<Category> categories = new ArrayList<>();
			categories.add(category);
			return new ResponseEntity(getObjectMapper().writeValueAsString(categories), HttpStatus.OK);

		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected static Set<CatalogOption> getOptions()
	{
		final Set<CatalogOption> opts = new HashSet<>();
		opts.add(CatalogOption.BASIC);
		opts.add(CatalogOption.CATEGORIES);
		opts.add(CatalogOption.SUBCATEGORIES);
		opts.add(CatalogOption.PRODUCTS);
		return opts;
	}

	private CategoryHierarchyData searchCategory(final String categoryId)
	{
		List<CatalogModel> catalogModels = baseSiteService.getProductCatalogs(baseSiteService.getCurrentBaseSite());
		if (CollectionUtils.isEmpty(catalogModels))
		{
			return null;
		}
		CatalogModel catalogModel = catalogModels.get(0);
		return catalogFacade
				.getCategoryById(catalogModel.getId(), catalogModel.getActiveCatalogVersion().getVersion(), categoryId,
						PageOption.createWithoutLimits(), getOptions());
	}
}
