/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.controllers;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;
import de.hybris.platform.usageconsumptionservices.services.UcPaginationService;
import de.hybris.platform.usageconsumptionservices.services.UcUsageVolumeProductService;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.api.UsageConsumptionReportApi;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.Error;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageConsumptionReport;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeProduct;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.security.IsAuthorizedAdminOrNotAnonymous;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
 * Default implementation of controller for {@link UsageConsumptionReportApi}.
 *
 * @since 2108
 */
@Controller
@Api(tags = "Usage Consumption Report")
public class UcUsageConsumptionReportController extends UcBaseController implements UsageConsumptionReportApi
{
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private final HttpServletRequest request;

	@Resource(name = "ucUsageVolumeProductService")
	private UcUsageVolumeProductService usageVolumeProductService;

	@Resource(name = "ucPaginationService")
	private UcPaginationService ucPaginationService;

	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	public UcUsageConsumptionReportController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	@ApiOperation(value = "List or find UsageConsumptionReport objects", nickname = "listUsageConsumptionReport", notes = "This operation list or find UsageConsumptionReport entities", response = UsageConsumptionReport.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = UsageConsumptionReport.class, responseContainer = "List"),
			@ApiResponse(code = 206, message = "Partial Content", response = UsageConsumptionReport.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/usageConsumptionReport",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@IsAuthorizedAdminOrNotAnonymous
	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<List<UsageConsumptionReport>> listUsageConsumptionReport(
			@ApiParam(value = "Comma-separated properties to be provided in response") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "Related party id value") @Valid @RequestParam(value = "relatedParty.id", required = false) String relatedPartyId,
			@ApiParam(value = "Network product id value") @Valid @RequestParam(value = "product.id", required = false) String productId,
			@ApiParam(value = "Public identifier value") @Valid @RequestParam(value = "publicIdentifier", required = false) String publicIdentifier
	)
	{
		try
		{
			if (StringUtils.isEmpty(relatedPartyId) && StringUtils.isEmpty(productId) && StringUtils
					.isEmpty(publicIdentifier))
			{
				return getUnsuccessfulResponse("At least one filtering option has to be provided!", null, HttpStatus.BAD_REQUEST);
			}

			final UcUsageVolumeProductContext ucUsageVolumeProductContext = new UcUsageVolumeProductContext();
			ucUsageVolumeProductContext.setRelatedPartyId(relatedPartyId);
			ucUsageVolumeProductContext.setProductId(productId);
			ucUsageVolumeProductContext.setPublicIdentifier(publicIdentifier);

			if (!hasRole(ROLE_TRUSTED_CLIENT, SecurityContextHolder.getContext().getAuthentication()))
			{
				final UserModel user = getUserService()
						.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
				ucUsageVolumeProductContext.setRelatedPartyId(user.getUcParty().getId());
			}

			offset = ucPaginationService.checkOffset(offset);
			limit = ucPaginationService.checkLimit(limit);

			final List<UcUsageVolumeProductModel> volumeProductsModels = getUsageVolumeProductService()
					.getUsageVolumeProducts(ucUsageVolumeProductContext, offset, limit);
			final Integer totalCount = getUsageVolumeProductService().getNumberOfUsageVolumeProductFor(ucUsageVolumeProductContext);

			final List<UsageVolumeProduct> usageVolumeProductsDtos = new ArrayList<>();
			volumeProductsModels.forEach(usageVolumeProductModel -> usageVolumeProductsDtos
					.add(getDataMapper().map(usageVolumeProductModel, UsageVolumeProduct.class, fields)));

			final List<UsageConsumptionReport> consumptionDtos = createUsageConsumptionReport(usageVolumeProductsDtos,
					ucUsageVolumeProductContext, volumeProductsModels);

			if (limit < totalCount || offset != 0)
			{
				final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
				final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
				getUcPaginationService().addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()),
						queryStringWithoutParams, Long.valueOf(totalCount), limit, offset);

				return new ResponseEntity(getObjectMapper().writeValueAsString(consumptionDtos), HttpStatus.PARTIAL_CONTENT);
			}
			return new ResponseEntity(getObjectMapper().writeValueAsString(consumptionDtos), HttpStatus.OK);
		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Creates a list of {@link UsageConsumptionReport} based on the usage volume products list and filtering options provided.
	 *
	 * @param usageVolumeProducts
	 * 		the usage volume products
	 * @param ucUsageVolumeProductContext
	 * 		the filtering options
	 * @param volumeProductsModels
	 * 		the volume products models
	 * @return list of {@link UsageConsumptionReport}
	 */
	private List<UsageConsumptionReport> createUsageConsumptionReport(final List<UsageVolumeProduct> usageVolumeProducts,
			final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final List<UcUsageVolumeProductModel> volumeProductsModels)
	{
		final List<UsageConsumptionReport> consumptionDtos = new ArrayList<>();
		final UsageConsumptionReport usageConsumptionReport = new UsageConsumptionReport();

		if (!CollectionUtils.isEmpty(usageVolumeProducts))
		{
			if (StringUtils.isNotEmpty(getFilteringOptions(ucUsageVolumeProductContext)))
			{
				usageConsumptionReport.setName(
						"Usage Consumption Report for " + getFilteringOptions(ucUsageVolumeProductContext));
			}

			usageConsumptionReport.setEffectiveDate(getUsageVolumeProductService().getMostRecentDate(volumeProductsModels));
		}

		usageConsumptionReport.setBucket(usageVolumeProducts);
		consumptionDtos.add(usageConsumptionReport);

		return consumptionDtos;
	}

	/**
	 * Creates a list of the provided filtering options.
	 *
	 * @param ucUsageVolumeProductContext
	 * 		the filtering options
	 * @return list of the provided filtering options.
	 */
	private String getFilteringOptions(final UcUsageVolumeProductContext ucUsageVolumeProductContext)
	{
		final List<String> filteringOptions = new ArrayList<>();
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getRelatedPartyId()))
		{
			filteringOptions.add(ucUsageVolumeProductContext.getRelatedPartyId());
		}

		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getPublicIdentifier()))
		{
			filteringOptions.add(ucUsageVolumeProductContext.getPublicIdentifier());
		}

		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getProductId()))
		{
			filteringOptions.add(ucUsageVolumeProductContext.getProductId());
		}

		return filteringOptions.toString().substring(1, filteringOptions.toString().length()-1);
	}

	protected UcUsageVolumeProductService getUsageVolumeProductService()
	{
		return usageVolumeProductService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected UcPaginationService getUcPaginationService()
	{
		return ucPaginationService;
	}
}
