/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.CartActionInput;
import de.hybris.platform.b2ctelcofacades.data.TmaPlaceData;
import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.b2ctelcofacades.order.converters.populator.TmaOrderEntryPopulator;
import de.hybris.platform.b2ctelcofacades.pagination.TmaPaginationFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaUserFacade;
import de.hybris.platform.b2ctelcoservices.data.TmaProductSpecCharacteristicConfigItem;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.enums.TmaRelatedPartyRole;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdminOrAnonymous;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedShoppingCartCreateUserOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedShoppingCartUpdateUserOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsNotAnonymousOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ShoppingCartApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessTypeRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductCharacteristic;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedParty;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
 * Default implementation of controller for {@link ShoppingCartApi}.
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "shoppingCart", description = "the ShoppingCart management API", tags = { "ShoppingCart" })
public class TmaShoppingCartApiController extends TmaBaseController implements ShoppingCartApi
{

	/**
	 * Group entry number for new groups
	 */
	private static final Integer NEW_GROUP_ITEM_NUMBER = -1;

	/**
	 * Entry number for new entries
	 */
	private static final Integer NEW_ITEM_NUMBER = -1;

	/**
	 * Entry number for new entries
	 */
	private static final Integer ERROR_CODE = 22;

	/**
	 * Cart facade
	 */
	@Resource(name = "cartFacade")
	private TmaCartFacade tmaCartFacade;

	/**
	 * Pagination facade
	 */
	@Resource(name = "tmaPaginationFacade")
	private TmaPaginationFacade tmaPaginationFacade;

	/**
	 * Shopping cart update validator
	 */
	@Resource(name = "tmaShoppingCartUpdateValidator")
	private Validator tmaShoppingCartUpdateValidator;

	/**
	 * Shopping cart create validator
	 */
	@Resource(name = "tmaShoppingCartCreateValidator")
	private Validator tmaShoppingCartCreateValidator;

	/**
	 * Customer facade
	 */
	@Resource(name = "customerFacade")
	private TmaCustomerFacade tmaCustomerFacade;

	/**
	 * User facade
	 */
	@Resource(name = "userFacade")
	private TmaUserFacade tmaUserFacade;

	/**
	 * Http servlet request
	 */
	private final HttpServletRequest request;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	@Autowired
	public TmaShoppingCartApiController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@ApiOperation(value = "List or find 'ShoppingCart' objects", nickname = "listShoppingCart", notes = "This operation retrieves a list of shopping cart entities for the related party.", response = ShoppingCart.class, responseContainer = "List",
			tags = { "ShoppingCart", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = ShoppingCart.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/shoppingCart", produces = { "application/json" }, method = RequestMethod.GET)
	@Override
	@SuppressWarnings({ "unchecked" })
	@IsAuthorizedResourceOwnerOrAdmin
	@IsNotAnonymousOrAdmin
	public ResponseEntity<List<ShoppingCart>> listShoppingCart(
			@NotNull @ApiParam(value = "Identifier of the Customer", required = true) @Valid @RequestParam(value = "relatedParty.Id") final String relatedPartyId,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Requested index for start of resources to be provided in response") @Valid @RequestParam(value = "offset", required = false) final Integer offset,
			@ApiParam(value = "Requested number of resources to be provided in response") @Valid @RequestParam(value = "limit", required = false) final Integer limit,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			List<CartData> cartDataList = tmaCartFacade.getCartsForCustomer(relatedPartyId, true);
			if (cartDataList.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			final int checkedOffset = tmaPaginationFacade.checkOffset(offset);
			final int checkedLimit = tmaPaginationFacade.checkLimit(limit);
			final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
			final String queryStringWithoutParams = getQueryStringWithoutOffsetAndLimit(request);
			final long totalCount = cartDataList.size();
			tmaPaginationFacade
					.addPaginationHeadersToResponse(header, filter(request.getRequestURL().toString()), queryStringWithoutParams,
							totalCount, limit, offset);
			cartDataList = tmaPaginationFacade.filterListByOffsetAndLimit(checkedOffset, checkedLimit, cartDataList);
			final List<ShoppingCart> shoppingCartList = new ArrayList<>();
			for (final CartData cartData : cartDataList)
			{
				final ShoppingCart shoppingCart = getDataMapper().map(cartData, ShoppingCart.class, fields);
				shoppingCartList.add(shoppingCart);
			}

			if (checkedLimit < totalCount || checkedOffset > totalCount || checkedOffset != 0)
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(shoppingCartList),
						header, HttpStatus.PARTIAL_CONTENT);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(shoppingCartList), HttpStatus.OK);
		}
		catch (final JsonProcessingException | CalculationException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Updates partially a 'ShoppingCart' by Id", nickname = "patchShoppingCart", notes = "This API updates the place, process type, cart status and other details like formerSupplier of the customer.", response = ShoppingCart.class,
			tags = { "ShoppingCart", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated", response = ShoppingCart.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/shoppingCart/{id}", produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" }, method = RequestMethod.PATCH)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedShoppingCartUpdateUserOrAdmin
	public ResponseEntity<ShoppingCart> patchShoppingCart(
			@ApiParam(value = "Identifier of the Shopping Cart", required = true) @PathVariable("id") final String id,
			@ApiParam(value = "The Shopping Cart to be updated", required = true) @Valid @RequestBody final ShoppingCart shoppingCart)
	{
		try
		{
			final String errorMessage = validate(shoppingCart, "shoppingCart", tmaShoppingCartUpdateValidator);
			if (StringUtils.isNotEmpty(errorMessage))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(null, null, ERROR_CODE, errorMessage,
						HttpStatus.BAD_REQUEST);
			}

			final String relatedPartyId = shoppingCart.getRelatedParty().get(0).getId();
			final String error = validateCartBelongsToCustomer(id, relatedPartyId);

			if (StringUtils.isNotEmpty(error))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, 1,
						error, HttpStatus.BAD_REQUEST);
			}

			if (!tmaCartFacade.isValidCartStatus(shoppingCart.getStatus()))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, ERROR_CODE, "Incorrect cart status ",
						HttpStatus.BAD_REQUEST);
			}

			tmaUserFacade.setCurrentUser(relatedPartyId);
			tmaCustomerFacade.updateEligibilityContextsByCustomer(relatedPartyId);

			final List<CartActionInput> cartActionInputList = createCartActions(shoppingCart, id, relatedPartyId);

			tmaCartFacade.processCartAction(cartActionInputList);
			return getCart(id, relatedPartyId, null, false);
		}
		catch (final CommerceCartModificationException | UnknownIdentifierException | JsonProcessingException
				| IllegalArgumentException | CalculationException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Retrieves a 'ShoppingCart' by Id", nickname = "retrieveShoppingCart", notes = "This operation retrieves a shopping cart entity using its unique ID.", response = ShoppingCart.class, responseContainer = "List",
			tags = { "ShoppingCart", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = ShoppingCart.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/shoppingCart/{id}", produces = { "application/json;charset=utf-8" }, method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdminOrAnonymous
	public ResponseEntity<List<ShoppingCart>> retrieveShoppingCart(
			@ApiParam(value = "Identifier of the Shopping Cart", required = true) @PathVariable("id") final String id,
			@NotNull @ApiParam(value = "Identifier of the Customer", required = true) @Valid @RequestParam(value = "relatedParty.Id") final String relatedPartyId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		try
		{
			return getCart(id, relatedPartyId, fields, true);
		}
		catch (final UnknownIdentifierException | JsonProcessingException | CalculationException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Creates a 'ShoppingCart'", nickname = "createShoppingCart", notes = "Creates a ShoppingCart. This API supports Location Based Pricing which means the prices will be applied based on the region of customer and stores other details of the customer like formerSuplier.", response = ShoppingCart.class,
			tags = { "ShoppingCart", })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = ShoppingCart.class),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/shoppingCart", produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" }, method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	@IsAuthorizedShoppingCartCreateUserOrAdmin
	@Override
	public ResponseEntity<ShoppingCart> createShoppingCart(
			@ApiParam(value = "The Shopping Cart to be created", required = true) @Valid @RequestBody final ShoppingCart shoppingCart,
			@ApiParam(value = "The id of the base site.") @Valid @RequestParam(value = "baseSiteId", required = false) final String baseSiteId)
	{
		if (shoppingCart == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try
		{
			final String errorMessage = validate(shoppingCart, "shoppingCart", tmaShoppingCartCreateValidator);

			if (StringUtils.isNotEmpty(errorMessage))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, ERROR_CODE, errorMessage,
						HttpStatus.BAD_REQUEST);
			}


			final String relatedPartyId = shoppingCart.getRelatedParty().get(0).getId();
			tmaUserFacade.setCurrentUser(relatedPartyId);
			tmaCustomerFacade.updateEligibilityContextsByCustomer(relatedPartyId);

			tmaUserFacade.setCurrentUser(relatedPartyId);
			tmaCustomerFacade.updateEligibilityContextsByCustomer(relatedPartyId);

			final CartData cartData = tmaCartFacade.createCartForUserAndCurrentBaseSite(relatedPartyId);

			if (cartData == null)
			{
				return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
			}

			if (CollectionUtils.isEmpty(shoppingCart.getCartItem()))
			{
				return new ResponseEntity(getObjectMapper().writeValueAsString(getDataMapper().map(cartData, ShoppingCart.class)),
						HttpStatus.CREATED);
			}

			final List<CartActionInput> cartActionInputList = createCartActions(shoppingCart, cartData.getCode(), relatedPartyId);

			if (CollectionUtils.isEmpty(cartActionInputList))
			{
				return getUnsuccessfulResponseWithErrorRepresentation(BAD_REQUEST, null, ERROR_CODE, "Incorrect cart item "
						+ "identifier.", HttpStatus.BAD_REQUEST);
			}

			tmaCartFacade.processCartAction(cartActionInputList);

			final Optional<CartData> cart = tmaCartFacade.getCartForCodeAndCustomer(cartData.getCode(), relatedPartyId);

			if (!cart.isPresent())
			{
				return new ResponseEntity("Cart not found.", HttpStatus.NOT_FOUND);
			}

			CartData cartDataResult = cart.get();
			return new ResponseEntity(getObjectMapper().writeValueAsString(getDataMapper().map(cartDataResult, ShoppingCart.class)),
					HttpStatus.CREATED);
		}
		catch (final ConversionException | JsonProcessingException | CommerceCartModificationException | IllegalArgumentException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}

	private String validateCartBelongsToCustomer(final String shoppingCartId, final String relatedPartyId)
	{
		final Optional<CartData> cartData = tmaCartFacade.getCartForCodeAndCustomer(shoppingCartId, relatedPartyId);
		if (!cartData.isPresent())
		{
			return MessageFormat.format("User {0} does not have cart with id {1}.", relatedPartyId, shoppingCartId);
		}
		return StringUtils.EMPTY;
	}

	@SuppressWarnings("unchecked")
	private ResponseEntity getCart(final String id, final String relatedPartyId, final String fields, final boolean recalculate)
			throws JsonProcessingException, CalculationException
	{
		final Optional<CartData> cartData = tmaCartFacade.getCartForCodeAndCustomer(id, relatedPartyId, recalculate);

		if (!cartData.isPresent())
		{
			return new ResponseEntity("Cart not found.", HttpStatus.NOT_FOUND);
		}
		CartData cartDataResult = cartData.get();
		final ShoppingCart shoppingCart = getDataMapper().map(cartDataResult, ShoppingCart.class, fields);
		return new ResponseEntity(getObjectMapper().writeValueAsString(shoppingCart), HttpStatus.OK);
	}

	/**
	 * Returns the entry group number from the identifier of the cart item.
	 *
	 * @param cartItemId
	 * 		the identifier of the cart item
	 * @return The entry group number of the PO.
	 */
	private Integer extractGroupNumberFromCartItemId(final String cartItemId)
	{
		return cartItemId.contains(TmaOrderEntryPopulator.SPO_CODE) ? NEW_GROUP_ITEM_NUMBER
				: Integer.valueOf(cartItemId.split(TmaOrderEntryPopulator.SEPARATOR)[1]);
	}

	/**
	 * Returns the entry number from the identifier of the cart item.
	 *
	 * @param cartItemId
	 * 		the identifier of the cart item
	 * @return The entry number of the PO.
	 */
	private Integer extractEntryNumberFromCartItemId(final String cartItemId)
	{
		if (cartItemId.contains(TmaOrderEntryPopulator.SPO_CODE))
		{
			return Integer.valueOf(cartItemId.split(TmaOrderEntryPopulator.SEPARATOR)[1]);
		}

		return StringUtils.countMatches(cartItemId, TmaOrderEntryPopulator.SEPARATOR) == 2
				? Integer.valueOf(cartItemId.split(TmaOrderEntryPopulator.SEPARATOR)[2])
				: NEW_ITEM_NUMBER;
	}

	/**
	 * Creates and returns a list of {@link CartActionInput} from the parameters provided.
	 *
	 * @param shoppingCart
	 * 		the shopping cart that will be used to create actions
	 * @param shoppingCartId
	 * 		the identifier of the shopping cart
	 * @param relatedPartyId
	 * 		the identifier of the related party
	 * @return list of {@link CartActionInput}
	 */
	private List<CartActionInput> createCartActions(final ShoppingCart shoppingCart, final String shoppingCartId,
			final String relatedPartyId)
	{
		final List<CartActionInput> cartActionInputs = new ArrayList<>();
		final String cartStatus = shoppingCart.getStatus();
		if (StringUtils.isNotBlank(cartStatus))
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setStatus(OrderStatus.valueOf(cartStatus));
			cartActionInputs.add(cartActionInput);
		}

		if (hasPaymentWithType(shoppingCart.getPaymentMethod(), PaymentMethodRef.TypeEnum.VOUCHER.toString()))
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setCouponIds(getCouponIds(shoppingCart.getPaymentMethod()));
			cartActionInputs.add(cartActionInput);
		}

		if (!hasPaymentWithType(shoppingCart.getPaymentMethod(), PaymentMethodRef.TypeEnum.VOUCHER.toString())
				&& shoppingCart.getPaymentMethod() != null)
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setCouponIds(new ArrayList<>());
			cartActionInputs.add(cartActionInput);
		}

		if (hasPaymentWithType(shoppingCart.getPaymentMethod(), PaymentMethodRef.TypeEnum.CREDITCARD.toString()))
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setPaymentMethodId(getPaymentMethodId(shoppingCart.getPaymentMethod()));
			cartActionInputs.add(cartActionInput);
		}

		if (!hasPaymentWithType(shoppingCart.getPaymentMethod(), PaymentMethodRef.TypeEnum.CREDITCARD.toString())
				&& shoppingCart.getPaymentMethod() != null)
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setPaymentMethodId(StringUtils.EMPTY);
			cartActionInputs.add(cartActionInput);
		}

		if (CollectionUtils.isNotEmpty(shoppingCart.getPlace()))
		{
			final CartActionInput cartActionInput = new CartActionInput();
			final List<TmaPlaceData> tmaPlaceData = getCartPlaces(shoppingCart);
			if (CollectionUtils.isNotEmpty(tmaPlaceData))
			{
				cartActionInput.setCartId(shoppingCartId);
				cartActionInput.setUserGuid(relatedPartyId);
				cartActionInput.setPlaces(tmaPlaceData);
				cartActionInputs.add(cartActionInput);
			}
		}

		if (CollectionUtils.isNotEmpty(shoppingCart.getCartItem()))
		{
			cartActionInputs.addAll(getCartItemsFromShoppingCart(shoppingCart, shoppingCartId, relatedPartyId));
		}

		if (shoppingCart.getDeliveryMode() != null)
		{
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(shoppingCartId);
			cartActionInput.setUserGuid(relatedPartyId);
			cartActionInput.setDelieryModeId(shoppingCart.getDeliveryMode().getId());
			cartActionInputs.add(cartActionInput);
		}

		return cartActionInputs;
	}

	/**
	 * Creates a list of {@link CartActionInput} from the cart items of the shopping cart.
	 *
	 * @param shoppingCart
	 * 		The shopping cart
	 * @param shoppingCartId
	 * 		The unique identifier of the shopping cart
	 * @param relatedPartyId
	 * 		The unique identifier of the related party
	 * @return List of {@link CartActionInput}
	 */
	private List<CartActionInput> getCartItemsFromShoppingCart(final ShoppingCart shoppingCart, final String shoppingCartId,
			final String relatedPartyId)
	{
		final List<CartActionInput> cartActionInputList = new ArrayList<>();

		shoppingCart.getCartItem().forEach((CartItem cartItem) -> {
			if (StringUtils.isNotEmpty(cartItem.getId()) && CollectionUtils.isNotEmpty(cartItem.getCartItem()))
			{
				cartActionInputList.addAll(createCartItemActionForUpdateExistingBpo(cartItem, shoppingCartId, relatedPartyId));
				return;
			}
			cartActionInputList.add(createCartItemAction(cartItem, getProductCode(cartItem), cartItem.getProcessType(),
					shoppingCartId, relatedPartyId));
		});

		return cartActionInputList;
	}

	/**
	 * Creates a list of {@link CartActionInput} from the cart items of the shopping cart for updating existing BPO.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param shoppingCartId
	 * 		the identifier of the shopping cart
	 * @param relatedPartyId
	 * 		the identifier of the related party
	 * @return List of {@link CartActionInput}
	 */
	private List<CartActionInput> createCartItemActionForUpdateExistingBpo(final CartItem cartItem, final String shoppingCartId,
			final String relatedPartyId)
	{
		final List<CartActionInput> cartActionInputList = new ArrayList<>();

		cartItem.getCartItem().forEach((CartItem childCartItem) -> {
			CartActionInput cartActionInput = createCartItemAction(childCartItem, StringUtils.EMPTY, null, shoppingCartId,
					relatedPartyId);
			cartActionInput.setParentEntryNumber(Integer.valueOf(cartItem.getId()));
			cartActionInputList.add(cartActionInput);
		});

		return cartActionInputList;
	}

	/**
	 * Creates a {@link CartActionInput} for a {@link CartItem}.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param cartId
	 * 		the identifier of the shopping cart
	 * @param relatedPartyId
	 * 		the identifier of the related party
	 * @return instance of {@link CartActionInput}
	 */
	private CartActionInput createCartItemAction(final CartItem cartItem, final String rootBpoCode,
			final ProcessTypeRef processType, final String cartId, final String relatedPartyId)
	{
		final CartActionInput cartActionInput = new CartActionInput();
		cartActionInput.setCartId(cartId);
		cartActionInput.setUserGuid(relatedPartyId);
		cartActionInput.setProcessType(getProcessType(processType));
		cartActionInput.setProductCode(getProductCode(cartItem));
		cartActionInput.setSubscriptionTermId(getSubscriptionTermId(cartItem));
		cartActionInput.setQuantity(getQuantity(cartItem));

		if (!StringUtils.equalsIgnoreCase(cartActionInput.getProductCode(), rootBpoCode))
		{
			cartActionInput.setRootBpoCode(rootBpoCode);
		}

		cartActionInput.setAppointmentId(cartItem.getAppointment() != null ? cartItem.getAppointment().getId() : null);
		cartActionInput.setEntryId(cartItem.getId());
		cartActionInput.setSubscribedProductCode(cartItem.getProduct() != null ? cartItem.getProduct().getId() : null);
		cartActionInput.setServiceProvider(
				cartItem.getProduct() != null ? getServiceProvider(cartItem.getProduct().getRelatedParty()) : null);
		cartActionInput.setContractStartDate(cartItem.getContractStartDate());

		final List<ProductCharacteristic> productCharacteristics = getCharacteristics(cartItem);
		if (CollectionUtils.isNotEmpty(productCharacteristics))
		{
			final Set<TmaProductSpecCharacteristicConfigItem> characteristicConfigItems = productCharacteristics.stream()
					.map(this::getProductSpecCharacteristicConfigItem).collect(Collectors.toSet());
			cartActionInput.setConfigurableProductSpecCharacteristics(characteristicConfigItems);
		}

		final List<TmaPlaceData> tmaPlaceData = getPlaces(cartItem);
		if (CollectionUtils.isNotEmpty(tmaPlaceData))
		{
			cartActionInput.setPlaces(tmaPlaceData);
		}

		cartActionInput.setEntryNumber(
				StringUtils.isEmpty(cartItem.getId()) ? NEW_ITEM_NUMBER : Integer.valueOf(cartItem.getId()));

		if (cartItem.getAction() != null)
		{
			cartActionInput.setAction(TmaSubscribedProductAction.valueOf(cartItem.getAction().name()));
		}

		if (CollectionUtils.isNotEmpty(cartItem.getCartItem()))
		{
			cartActionInput.setChildren(cartItem.getCartItem().stream()
					.map((CartItem childCartItem) -> createCartItemAction(childCartItem, rootBpoCode, processType, cartId,
							relatedPartyId)).collect(Collectors.toList()));
		}

		return cartActionInput;
	}

	private TmaProductSpecCharacteristicConfigItem getProductSpecCharacteristicConfigItem(
			final ProductCharacteristic productCharacteristic)
	{
		final TmaProductSpecCharacteristicConfigItem characteristicConfigItem = new TmaProductSpecCharacteristicConfigItem();
		characteristicConfigItem.setName(productCharacteristic.getName());
		characteristicConfigItem.setValue(productCharacteristic.getValue());
		return characteristicConfigItem;
	}

	private String getProcessType(final ProcessTypeRef processTypeRef)
	{
		return processTypeRef != null ? processTypeRef.getId() : null;
	}

	private String getProductCode(final CartItem cartItem)
	{
		return cartItem.getProductOffering() != null ? cartItem.getProductOffering().getId() : null;
	}

	private String getSubscriptionTermId(final CartItem cartItem)
	{
		final CartTerm firstCartTerm = CollectionUtils.isNotEmpty(cartItem.getItemTerm()) ? cartItem.getItemTerm().get(0) : null;
		return firstCartTerm != null ? firstCartTerm.getId() : null;
	}

	private Long getQuantity(final CartItem cartItem)
	{
		return cartItem.getQuantity() != null ? Long.valueOf(cartItem.getQuantity()) : null;
	}

	/**
	 * Returns the configured characteristics of the product offering if it is present, else it returns empty list.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @return the configured characteristics of the product offering
	 */
	private List<ProductCharacteristic> getCharacteristics(final CartItem cartItem)
	{
		return cartItem.getProduct() != null && CollectionUtils.isNotEmpty(cartItem.getProduct().getCharacteristic())
				? cartItem.getProduct().getCharacteristic()
				: new ArrayList<>();
	}

	/**
	 * Returns the places from the product in the cart item.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @return The {@link TmaPlaceData}s found.
	 */
	private List<TmaPlaceData> getPlaces(final CartItem cartItem)
	{
		final List<TmaPlaceData> tmaPlacesData = new ArrayList<>();
		if (cartItem.getProduct() == null || cartItem.getProduct().getPlace() == null)
		{
			return tmaPlacesData;
		}
		final List<Place> inputPlaces = cartItem.getProduct().getPlace();
		inputPlaces.forEach(inputPlace ->
		{
			final TmaPlaceData placeData = new TmaPlaceData();
			placeData.setId(inputPlace.getId());
			placeData.setRole(TmaPlaceRoleType.valueOf(inputPlace.getRole()));
			tmaPlacesData.add(placeData);
		});
		return tmaPlacesData;
	}

	/**
	 * Returns the places from the product in the cart item.
	 *
	 * @param shoppingCart
	 * 		the cart item
	 * @return The {@link TmaPlaceData}s found.
	 */
	private List<TmaPlaceData> getCartPlaces(final ShoppingCart shoppingCart)
	{
		final List<TmaPlaceData> tmaPlacesData = new ArrayList<>();
		if (shoppingCart.getPlace() == null)
		{
			return tmaPlacesData;
		}
		final List<Place> inputPlaces = shoppingCart.getPlace();
		inputPlaces.forEach(inputPlace ->
		{
			final TmaPlaceData placeData = new TmaPlaceData();
			placeData.setId(inputPlace.getId());
			placeData.setRole(TmaPlaceRoleType.valueOf(inputPlace.getRole()));
			tmaPlacesData.add(placeData);
		});
		return tmaPlacesData;
	}

	/**
	 * Returns the credit card type payment.
	 *
	 * @param paymentMethodList
	 * 		the payment method list
	 * @return The payment found
	 */
	private String getPaymentMethodId(final List<PaymentMethodRef> paymentMethodList)
	{
		final Optional<PaymentMethodRef> creditCardPayment = paymentMethodList.stream()
				.filter(
						(final PaymentMethodRef paymentMethod) -> paymentMethod.getType().equals(PaymentMethodRef.TypeEnum.CREDITCARD))
				.findFirst();
		return creditCardPayment.isPresent() ? creditCardPayment.get().getId() : null;
	}

	/**
	 * Returns the voucher type payments.
	 *
	 * @param paymentMethodList
	 * 		the payment method list
	 * @return The payments found
	 */
	private List<String> getCouponIds(final List<PaymentMethodRef> paymentMethodList)
	{
		return paymentMethodList.stream()
				.filter((final PaymentMethodRef paymentMethod) -> paymentMethod.getType().equals(PaymentMethodRef.TypeEnum.VOUCHER))
				.map(PaymentMethodRef::getCode).collect(
						Collectors.toList());
	}

	/**
	 * Checks if there is a payment method with the provided type.
	 *
	 * @param paymentMethodRefList
	 * 		the list of payment methods
	 * @param type
	 * 		the type of the payment method
	 * @return True if there is a payment method with the provided type, otherwise false
	 */
	private boolean hasPaymentWithType(final List<PaymentMethodRef> paymentMethodRefList, final String type)
	{
		if (CollectionUtils.isEmpty(paymentMethodRefList))
		{
			return false;
		}

		return paymentMethodRefList.stream()
				.anyMatch(
						(final PaymentMethodRef paymentMethod) -> StringUtils.equalsIgnoreCase(paymentMethod.getType().name(), type));
	}

	private String getServiceProvider(final List<RelatedParty> relatedParties)
	{
		if (CollectionUtils.isNotEmpty(relatedParties))
		{
			for (final RelatedParty relatedParty : relatedParties)
			{
				if (StringUtils.equalsIgnoreCase(relatedParty.getRole(), TmaRelatedPartyRole.SERVICE_PROVIDER.name()))
				{
					return relatedParty.getId();
				}

			}
		}
		return null;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}
}
