/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.catalog.TmaCatalogFacade;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.CatalogApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Catalog;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ClassificationSystem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ContentCatalog;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link CatalogApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "catalog", description = "Catalog Management API", tags = { "Product Catalog Management" })
public class TmaCatalogApiController extends TmaBaseController implements CatalogApi
{
	/**
	 * Catalog facade
	 */
	@Resource(name = "catalogFacade")
	private TmaCatalogFacade catalogFacade;

	@ApiOperation(value = "List or find 'Catalog' objects", nickname = "listCatalog", response = Catalog.class, responseContainer = "List", tags = {
			"Product Catalog Management", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = Catalog.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/catalog",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<List<Catalog>> listCatalog(
			@ApiParam(value = "Identifier of the BaseStore") @Valid @RequestParam(value = "baseStore.id", required = false) String baseStorePeriodid,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "For filtering: Name of the catalog") @Valid @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "For filtering: Indicates the (class) type of catalog. For entity catalogs, this will be 'EntityCatalog'.") @Valid @RequestParam(value = "@type", required = false) String attype,
			@ApiParam(value = "For filtering: This field provides a link to the schema describing this REST resource") @Valid @RequestParam(value = "@schemaLocation", required = false) String atschemaLocation,
			@ApiParam(value = "For filtering: Indicates<b> </b>the base (class) type of this REST resource") @Valid @RequestParam(value = "@baseType", required = false) String atbaseType,
			@ApiParam(value = "For filtering: Catalog version") @Valid @RequestParam(value = "version", required = false) String version,
			@ApiParam(value = "For filtering: An instant of time, starting at the TimePeriod") @Valid @RequestParam(value = "validFor.startDateTime", required = false) Date validForPeriodstartDateTime,
			@ApiParam(value = "For filtering: An instant of time, ending at the TimePeriod.") @Valid @RequestParam(value = "validFor.endDateTime", required = false) Date validForPeriodendDateTime,
			@ApiParam(value = "For filtering: Date and time of the last update") @Valid @RequestParam(value = "lastUpdate", required = false) Date lastUpdate,
			@ApiParam(value = "For filtering: Used to indicate the current lifecycle status") @Valid @RequestParam(value = "lifecycleStatus", required = false) String lifecycleStatus)
	{
		try
		{
			if (StringUtils.isEmpty(baseStorePeriodid))
			{
				baseStorePeriodid = Config.getParameter(B2ctelcotmfwebservicesConstants.SESSION_DEFAULT_BASESTORE);
			}

			final Collection<CatalogData> catalogDataList = catalogFacade.getAllCatalogsForCurrentBaseStore(baseStorePeriodid);

			if (CollectionUtils.isEmpty(catalogDataList))
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			final List<Catalog> catalogList = new ArrayList<>();

			for (CatalogData catalogData : catalogDataList)
			{
				if (containsFilteredPropertyValues(catalogData, name))
				{
					switch (catalogData.getType())
					{
						case ClassificationSystemModel._TYPECODE:
							final ClassificationSystem classificationSystem = getDataMapper()
									.map(catalogData, ClassificationSystem.class, fields);
							catalogList.add(classificationSystem);
							break;
						case ContentCatalogModel._TYPECODE:
							final ContentCatalog contentCatalog = getDataMapper().map(catalogData, ContentCatalog.class, fields);
							catalogList.add(contentCatalog);
							break;
						default:
							final Catalog catalog = getDataMapper().map(catalogData, Catalog.class, fields);
							catalogList.add(catalog);
					}
				}
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(catalogList), HttpStatus.OK);

		}
		catch (
				final UnknownIdentifierException e)

		{
			getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException e)

		{
			getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private boolean containsFilteredPropertyValues(CatalogData catalogData, String name)
	{
		return StringUtils.isEmpty(name) || catalogData.getName().toLowerCase().contains(name.toLowerCase());
	}
}
