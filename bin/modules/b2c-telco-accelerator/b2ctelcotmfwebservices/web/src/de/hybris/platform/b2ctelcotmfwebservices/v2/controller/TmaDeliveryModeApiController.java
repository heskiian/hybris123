/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.bundle.TmaCheckoutFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.DeliveryModeApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryMode;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
 * Default implementation of {@link DeliveryModeApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "deliveryMode", description = "the deliveryMode API", tags = { "Delivery Mode" })
public class TmaDeliveryModeApiController extends TmaBaseController implements DeliveryModeApi
{
	/**
	 * Telco Checkout facade
	 */
	@Resource(name = "acceleratorCheckoutFacade")
	private TmaCheckoutFacade tmaCheckoutFacade;

	@ApiOperation(value = "Retrieves a list of 'DeliveryMode'", nickname = "listDeliveryMode", response =
			DeliveryMode.class, responseContainer = "List", tags = { "Delivery Mode", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", responseContainer = "List"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not Allowed", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/deliveryMode",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<DeliveryMode>> listDeliveryMode(
			@NotNull @ApiParam(value = "Unique identifier of the Shopping Cart", required = true) @Valid @RequestParam(value = "shoppingCart.id") String shoppingCartId,
			@NotNull @ApiParam(value = "Unique identifier of the Related Party", required = true) @Valid @RequestParam(value = "relatedParty.id") String relatedPartyId,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) String baseSiteId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "For filtering: Name of deliveryMode") @Valid @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "For filtering: Description of deliveryMode") @Valid @RequestParam(value = "description", required = false) String description)
	{
		try
		{
			final List<DeliveryModeData> deliveryModeDataList = tmaCheckoutFacade
					.getSupportedDeliveryModesForCartAndUser(shoppingCartId, relatedPartyId);

			if (deliveryModeDataList.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			final List<DeliveryMode> deliveryModeList = new ArrayList<>();

			for (DeliveryModeData deliveryModeData : deliveryModeDataList)
			{
				if (!shouldFilter(name, description) || containsFilteredPropertyValues(deliveryModeData, name, description))
				{
					final DeliveryMode deliveryModeWsDto = getDataMapper().map(deliveryModeData, DeliveryMode.class, fields);
					deliveryModeList.add(deliveryModeWsDto);
				}
			}
			return new ResponseEntity(getObjectMapper().writeValueAsString(deliveryModeList),
					HttpStatus.OK);

		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException | IllegalArgumentException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	private boolean shouldFilter(String name, String description)
	{
		return StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(description);
	}

	private boolean containsFilteredPropertyValues(DeliveryModeData deliveryModeData, String name, String description)
	{
		if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(description))
		{
			return deliveryModeData.getName().toLowerCase().contains(name.toLowerCase())
					&& deliveryModeData.getDescription().toLowerCase()
					.contains(description.toLowerCase());
		}

		return StringUtils.isEmpty(name) ? deliveryModeData.getDescription().toLowerCase()
				.contains(description.toLowerCase()) : deliveryModeData.getName().toLowerCase().contains(name.toLowerCase());
	}
}
