/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.controllers;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.subscribedproductservices.enums.SpiProductStatusType;
import de.hybris.platform.subscribedproductservices.model.SpiProductBundleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiProductService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.api.ListenerApi;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Error;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.EventSubscription;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductAttributeValueChangeEvent;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductCreateEvent;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductDeleteEvent;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductStateChangeEvent;
import de.hybris.platform.subscribedproducttmfwebservices.v1.validators.SpiCreateEventValidator;
import de.hybris.platform.subscribedproducttmfwebservices.v1.validators.SpiDeleteEventValidator;
import de.hybris.platform.subscribedproducttmfwebservices.v1.validators.SpiStateChangeEventValidator;
import de.hybris.platform.subscribedproducttmfwebservices.v1.validators.SpiUpdateEventValidator;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ListenerApi}.
 *
 * @since 2111
 */
@Controller
@Secured("ROLE_TRUSTED_CLIENT")
@Api(tags = "Subscribed Product Inventory Notification Listeners")
public class SpiListenerController extends SpiBaseController implements ListenerApi
{
	@Resource(name = "spiProductService")
	private SpiProductService spiProductService;

	@Resource(name = "spiCreateEventValidator")
	private SpiCreateEventValidator spiCreateEventValidator;

	@Resource(name = "spiUpdateEventValidator")
	private SpiUpdateEventValidator spiUpdateEventValidator;

	@Resource(name = "spiStateChangeEventValidator")
	private SpiStateChangeEventValidator spiStateChangeEventValidator;

	@Resource(name = "spiDeleteEventValidator")
	private SpiDeleteEventValidator spiDeleteEventValidator;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;

	@Override
	@ApiOperation(value = "Client listener for entity ProductAttributeValueChangeEvent", nickname = "listenToProductAttributeValueChangeEvent", notes = "Example of a client listener for receiving the notification ProductAttributeValueChangeEvent", response = EventSubscription.class, tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listener/productAttributeValueChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity listenToProductAttributeValueChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody ProductAttributeValueChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getSpiUpdateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Product product = data.getEvent().getProduct();

		try
		{
			final SpiProductModel productModel = getSpiProductService().getProduct(data.getEvent().getProduct().getId());

			getTxTemplate().execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus status)
				{
					getDataMapper().map(product, productModel, false);
					getSpiProductService().saveProduct(productModel);
				}
			});
		}
		catch (ModelNotFoundException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Client listener for entity ProductStateChangeEvent", nickname = "listenToProductStateChangeEvent", notes = "Example of a client listener for receiving the notification ProductStateChangeEvent", response = EventSubscription.class, tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/listener/productStateChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public ResponseEntity listenToProductStateChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody ProductStateChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getSpiStateChangeEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Product product = data.getEvent().getProduct();

		final SpiProductModel productModel = getSpiProductService().getProduct(product.getId());
		productModel.setStatus(SpiProductStatusType.valueOf(product.getStatus().name()));
		getSpiProductService().saveProduct(productModel);

		return new ResponseEntity(HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Client listener for entity ProductCreateEvent", nickname = "listenToProductCreateEvent", notes = "Example of a client listener for receiving the notification ProductCreateEvent", response = EventSubscription.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listener/productCreateEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity listenToProductCreateEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody ProductCreateEvent data)
	{

		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getSpiCreateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Product product = data.getEvent().getProduct();
		final SpiProductModel productModel = getSpiProductService()
				.createProduct(
						Boolean.TRUE.equals(product.isIsBundle()) ? SpiProductBundleModel.class : SpiProductComponentModel.class);

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(product, productModel);
				getSpiProductService().saveProduct(productModel);
			}
		});

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity ProductDeleteEvent", nickname = "listenToProductDeleteEvent", notes = "Example of a client listener for receiving the notification ProductDeleteEvent", response = EventSubscription.class, tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/listener/productDeleteEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public ResponseEntity listenToProductDeleteEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody ProductDeleteEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getSpiDeleteEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Product product = data.getEvent().getProduct();

		final SpiProductModel productModel = getSpiProductService().getProduct(product.getId());
		getSpiProductService().removeProduct(productModel);

		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	protected SpiProductService getSpiProductService()
	{
		return spiProductService;
	}

	protected SpiCreateEventValidator getSpiCreateEventValidator()
	{
		return spiCreateEventValidator;
	}

	protected SpiUpdateEventValidator getSpiUpdateEventValidator()
	{
		return spiUpdateEventValidator;
	}

	protected SpiStateChangeEventValidator getSpiStateChangeEventValidator()
	{
		return spiStateChangeEventValidator;
	}

	protected SpiDeleteEventValidator getSpiDeleteEventValidator()
	{
		return spiDeleteEventValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
