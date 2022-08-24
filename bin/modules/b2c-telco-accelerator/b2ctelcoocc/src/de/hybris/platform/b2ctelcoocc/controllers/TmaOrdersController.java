/*
 *	Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoocc.controllers;

import de.hybris.platform.b2ctelcocommercewebservicescommons.validator.TmaPlaceOrderCartValidator;
import de.hybris.platform.b2ctelcofacades.bundle.TmaCheckoutFacade;
import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.b2ctelcoservices.order.exception.OrderProcessingException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Web Service Controller for the ORDERS resource
 *
 * @since 2001
 */


@Controller
@RequestMapping(value = "/{baseSiteId}")
@Api(tags = "Orders")
public class TmaOrdersController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaOrdersController.class);

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "tmaCartFacade")
	private TmaCartFacade tmaCartFacade;

	@Resource
	TmaCheckoutFacade tmaCheckoutFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "tmaPlaceOrderCartValidator")
	private TmaPlaceOrderCartValidator tmaPlaceOrderCartValidator;

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST)
	@RequestMappingOverride(priorityProperty = "b2ctelcoocc.TmaOrdersController.placeOrder.priority")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "placeOrder", value = "Place a order.", notes = "Authorizes the cart and places the order. The response contains the new order data.")
	@ApiBaseSiteIdAndUserIdParam
	public OrderWsDTO placeOrder(@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "Cart code for logged in user, cart GUID for guest checkout", required = true) @RequestParam final String cartId)
			throws OrderProcessingException, InvalidCartException
	{
		final String userId = customerFacade.getCurrentCustomerUid();

		cartLoaderStrategy.loadCart(cartId);

		validateCartForPlaceOrder();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Place order cart: code=" + cartId + " | user=" + userId);
		}
		final OrderData orderData = tmaCheckoutFacade.placeOrderFromCart(tmaCartFacade.getSessionCart().getCode(), userId, true);

		return getDataMapper().map(orderData, OrderWsDTO.class, fields);
	}

	protected void validateCartForPlaceOrder() throws InvalidCartException, WebserviceValidationException //NOSONAR
	{
		if (!tmaCheckoutFacade.hasCheckoutCart())
		{
			throw new InvalidCartException("Cannot place order. There was no checkout cart created yet!");
		}

		final CartData cartData = tmaCartFacade.getSessionCart();

		final Errors errors = new BeanPropertyBindingResult(cartData, "sessionCart");
		tmaPlaceOrderCartValidator.validate(cartData, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

}
