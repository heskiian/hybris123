/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.security;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.order.TmaOrderFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaInvalidRelatedPartyException;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartUnderscoreCreate;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartUnderscoreUpdate;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 1907
 */
public class UserValidator
{
	private static final Logger LOG = Logger.getLogger(UserValidator.class);

	private static final String ANONYMOUS = "anonymous";
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private final TmaSubscribedProductFacade tmaSubscribedProductFacade;
	private final TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;

	@Resource(name = "tmaSubscriptionAccessFacade")
	private TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "orderFacade")
	private TmaOrderFacade orderFacade;

	public UserValidator(final TmaSubscribedProductFacade tmaSubscribedProductFacade,
			final TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade)
	{
		this.tmaSubscribedProductFacade = tmaSubscribedProductFacade;
		this.tmaSubscriptionBaseFacade = tmaSubscriptionBaseFacade;
	}

	public boolean validateUser(final Authentication authentication, final String userId)
	{
		if (StringUtils.isNotEmpty(userId))
		{
			return isResourceOwner(authentication, userId) ? Boolean.TRUE : Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean isResourceOwner(final Authentication authentication, final String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		return authentication.getPrincipal().equals(userId);
	}

	public boolean isSubscriptionBaseOwner(final Authentication authentication, final String subscriptionBaseId)
	{
		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		final List<TmaSubscriptionAccessData> subscriptionAccesses = tmaSubscriptionAccessFacade
				.findSubscriptionAccessesByPrincipal(customerFacade.getCurrentCustomerUid());

		final Optional<TmaSubscriptionAccessData> subscriptionBaseData = subscriptionAccesses.stream().filter(
				subscriptionAccess -> subscriptionAccess.getSubscriptionBase().getSubscriberIdentity().equals(subscriptionBaseId))
				.findFirst();

		if (subscriptionBaseData.isEmpty())
		{
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * From the list of related parties provided in the productOrder object the first item is used for checking if it
	 * matches with the principal for which authorization has been obtained.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param productOrder
	 * 		the product order from where the related party being checked is obtained
	 * @return true in case the authentication is not client only and if the principal matches with the id of thew first
	 * related party provided
	 */
	public boolean isRelatedPartyAuthorizedToPlaceOrder(final Authentication authentication, final ProductOrder productOrder)
	{
		final List<RelatedPartyRef> relatedParties = (productOrder == null) ? null : productOrder.getRelatedParty();
		if (CollectionUtils.isEmpty(relatedParties))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		return (!((OAuth2Authentication) authentication).isClientOnly()) &&
				(authentication.getPrincipal().equals(relatedParties.get(0).getId()));
	}

	/**
	 * Checks if the user provided is not Anonymous.
	 *
	 * @param userId
	 * 		The userId provided.
	 * @return False if user is anonymous, otherwise true.
	 */
	public boolean isNotAnonymous(final String userId)
	{
		if (userId == null)
		{
			throw new TmaInvalidRelatedPartyException();
		}

		return !ANONYMOUS.equalsIgnoreCase(userId);
	}

	/**
	 * Checks if the user provided is not Anonymous.
	 *
	 * @param productOrder
	 * 		the product order from where the related party being checked is obtained.
	 * @return False if user is anonymous, otherwise true.
	 */
	public boolean isNotAnonymous(final ProductOrder productOrder)
	{
		final List<RelatedPartyRef> relatedParties = (productOrder == null) ? null : productOrder.getRelatedParty();
		if (CollectionUtils.isEmpty(relatedParties))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		final RelatedPartyRef firstRelatedParty = relatedParties.get(0);

		if (firstRelatedParty == null)
		{
			throw new TmaInvalidRelatedPartyException();
		}

		return !ANONYMOUS.equalsIgnoreCase(firstRelatedParty.getId());
	}

	/**
	 * Checks if the user provided is Anonymous and the client is authenticated.
	 *
	 * @param authentication
	 * 		- the authenticated client
	 * @param userId
	 * 		The userId provided.
	 * @return true if user is anonymous, otherwise false.
	 */
	public boolean isAnonymous(final Authentication authentication, final String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		if (StringUtils.equals(userId.toLowerCase(), ANONYMOUS))
		{
			if (((OAuth2Authentication) authentication).isClientOnly())
			{
				return Boolean.TRUE;
			}
			else
			{
				return ANONYMOUS.equals(authentication.getPrincipal().toString());
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * From the list of related parties provided in the{@link ShoppingCartUnderscoreUpdate} object the first item is used for
	 * checking if it matches with the principal for which authorization has been obtained.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true in case the authentication is not client only and if the principal matches with the id of the first related
	 * party provided
	 * @deprecated since 1911. Use {@link #isRelatedPartyAuthorizedShoppingCartUser(Authentication, ShoppingCart)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	public boolean isRelatedPartyAuthorizedToUpdateShoppingCart(final Authentication authentication,
			final ShoppingCartUnderscoreUpdate shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isRelatedPartyAuthorizedUserOrAdmin(authentication, relatedParties);
	}

	/**
	 * From the list of related parties provided in the{@link ShoppingCart} object the first item is used for
	 * checking if it matches with the principal for which authorization has been obtained.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true in case the authentication is not client only and if the principal matches with the id of the first related
	 * party provided
	 */
	public boolean isRelatedPartyAuthorizedShoppingCartUser(final Authentication authentication,
			final ShoppingCart shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isRelatedPartyAuthorizedUserOrAdmin(authentication, relatedParties);
	}

	/**
	 * From the list of related parties provided in the {@link ShoppingCartUnderscoreCreate} object the first item is used for
	 * checking if it matches with the principal for which authorization has been obtained.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true in case the authentication is not client only and if the principal matches with the id of the first related
	 * party provided
	 * @deprecated since 1911. Use {@link #isRelatedPartyAuthorizedShoppingCartUser(Authentication, ShoppingCart)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	public boolean isRelatedPartyAuthorizedToCreateShoppingCart(final Authentication authentication,
			final ShoppingCartUnderscoreCreate shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isRelatedPartyAuthorizedUserOrAdmin(authentication, relatedParties);
	}

	/**
	 * Checks if the first user provided in the list of related parties of the shopping cart is Anonymous and the client is
	 * authenticated.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true if user is anonymous, otherwise false.
	 */
	public boolean isAnonymous(final Authentication authentication, final ShoppingCart shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isAnonymous(authentication, relatedParties);
	}

	/**
	 * Checks if the first user provided in the list of related parties of the shopping cart is Anonymous and the client is
	 * authenticated.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true if user is anonymous, otherwise false.
	 * @deprecated since 1911. Use {@link #isAnonymous(Authentication, ShoppingCart)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	public boolean isAnonymous(final Authentication authentication, final ShoppingCartUnderscoreUpdate shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isAnonymous(authentication, relatedParties);
	}

	/**
	 * Checks if the first user provided in the list of related parties of the shopping cart is Anonymous and the client is
	 * authenticated.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param shoppingCart
	 * 		the shopping cart from where the list of related parties being checked is obtained
	 * @return true if user is anonymous, otherwise false.
	 * @deprecated since 1911. Use {@link #isAnonymous(Authentication, ShoppingCart)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	public boolean isAnonymous(final Authentication authentication, final ShoppingCartUnderscoreCreate shoppingCart)
	{
		final List<RelatedPartyRef> relatedParties = (shoppingCart == null) ? null : shoppingCart.getRelatedParty();
		return isAnonymous(authentication, relatedParties);
	}

	/**
	 * Checks if the first user provided in the list of related parties is Anonymous and the client is authenticated.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param relatedParties
	 * 		the list of related parties
	 * @return true if user is anonymous, otherwise false.
	 */
	public boolean isAnonymous(final Authentication authentication, final List<RelatedPartyRef> relatedParties)
	{
		if (CollectionUtils.isEmpty(relatedParties))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		final RelatedPartyRef firstRelatedParty = relatedParties.get(0);

		if (firstRelatedParty == null)
		{
			throw new TmaInvalidRelatedPartyException();
		}

		return isAnonymous(authentication, firstRelatedParty.getId());
	}

	/**
	 * From the list of related parties provided the first item is used for checking if it matches with the principal for which
	 * authorization has been obtained.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param relatedParties
	 * 		the list of related parties
	 * @return true in case the authentication is not client only and if the principal matches with the id of the first related
	 * party provided
	 */
	public boolean isRelatedPartyAuthorizedUserOrAdmin(final Authentication authentication,
			final List<RelatedPartyRef> relatedParties)
	{
		if (CollectionUtils.isEmpty(relatedParties))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		final RelatedPartyRef firstRelatedParty = relatedParties.get(0);

		if (firstRelatedParty == null)
		{
			throw new TmaInvalidRelatedPartyException();
		}

		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		return (!((OAuth2Authentication) authentication).isClientOnly()) &&
				(authentication.getPrincipal().equals(firstRelatedParty.getId()));

	}

	/**
	 * Checks if the request is for updating cart status, related party id matches with principal for which
	 * authorization has been obtained and if the client id is trusted or not.
	 *
	 * @param authentication
	 * 		authentication object
	 * @param shoppingCart
	 * 		The shopping cart
	 * @return true if request is to update cart status and if the principal matches with the id of the first
	 * related party provided and client is trusted, otherwise false
	 */
	public boolean isRelatedPartyTrustedClientAndUpdateStatus(final Authentication authentication, final ShoppingCart shoppingCart)
	{
		if (!ObjectUtils.isEmpty(shoppingCart))
		{
			final boolean isUpdateStatusRequest = StringUtils.isNotBlank(shoppingCart.getStatus());
			final boolean isSameResource = authentication.getPrincipal() != null
					&& authentication.getPrincipal().equals(shoppingCart.getRelatedParty().get(0).getId());
			final boolean isTrustedClient = hasRole(ROLE_TRUSTED_CLIENT, authentication);

			return isUpdateStatusRequest ? (isTrustedClient && isSameResource)
					: isRelatedPartyAuthorizedShoppingCartUser(authentication, shoppingCart);
		}
		return false;
	}

	/**
	 * Checks if the user has access to the subscribed product or subscription base of given id and the client is
	 * authenticated.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param identifier
	 * 		identifier of subscribed product or subscription base
	 * @return true if user has Owner or Beneficiary access to subscribed product or subscription base, otherwise false.
	 */
	public boolean isAuthorizedSubscriptionUser(final Authentication authentication, final String identifier)
	{
		final String currentUserId = authentication.getPrincipal().toString();

		if (!currentUserId.equalsIgnoreCase(ANONYMOUS) && StringUtils.isNotBlank(identifier))
		{
			if (getTmaSubscriptionBaseFacade().doesSubscriptionBaseExist(identifier))
			{
				return getTmaSubscriptionBaseFacade().isSubscriptionBaseAccessibleToUser(identifier, currentUserId);
			}
			else
			{
				boolean subscribedProductExist = false;
				try
				{
					final TmaSubscribedProductData subscribedProductData = getTmaSubscribedProductFacade()
							.getSubscriptionsById(identifier);
					subscribedProductExist = true;

					final TmaSubscriptionAccessModel tmaSubscriptionAccessModel = tmaSubscriptionAccessFacade
							.getSubscriptionAccessByPrincipalAndSubscriptionBase(currentUserId,
									subscribedProductData.getBillingsystemId(), subscribedProductData.getSubscriptionBaseId());

					return tmaSubscriptionAccessModel.getAccessType().equals(TmaAccessType.OWNER)
							|| tmaSubscriptionAccessModel.getAccessType().equals(TmaAccessType.BENEFICIARY);

				}
				catch (final ModelNotFoundException e)
				{
					if (subscribedProductExist)
					{
						LOG.error("Subscribed Product " + identifier + " is not accessible ", e);
						return false;
					}
					else
					{
						LOG.debug("Subscribed Product for " + identifier + "not found ", e);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the user is able to access orders. The business logic of the access to orders is defined in the corresponding
	 * order service.
	 *
	 * @param authentication
	 * 		the authenticated client
	 * @param relatedPartyId
	 * 		identifier of the related party
	 * @return true if related party provided is able to access orders, otherwise false.
	 */
	public boolean canUserListOrders(final Authentication authentication, final String relatedPartyId)
	{
		if (authentication.getPrincipal() == null)
		{
			return false;
		}

		final String currentUserId = authentication.getPrincipal().toString();

		if (ANONYMOUS.equalsIgnoreCase(relatedPartyId))
		{
			return false;
		}

		return getOrderFacade().hasUserAccessToOrders(currentUserId, relatedPartyId);
	}

	/**
	 * Checks if the authenticated party has required permissions to access the requested product order.
	 *
	 * @param authentication
	 * 		The authentication object
	 * @param productOrder
	 * 		The product order
	 * @return True if the authenticated object has the required permissions to access the requested product order, otherwise false
	 */
	public boolean isAuthorizedForProductOrderUpdate(final Authentication authentication, final ProductOrder productOrder,
			final String orderId)
	{
		final List<RelatedPartyRef> relatedParties = (productOrder == null) ? null : productOrder.getRelatedParty();
		if (CollectionUtils.isEmpty(relatedParties))
		{
			throw new TmaInvalidRelatedPartyException();
		}

		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}

		final boolean isAnonymous = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId()
				.equals(authentication.getPrincipal());
		if (isAnonymous)
		{
			return Boolean.FALSE;
		}

		final String relatedPartyId = relatedParties.iterator().next().getId();

		return getOrderFacade().canUserUpdateOrderStatus(relatedPartyId, (String) authentication.getPrincipal(), orderId);
	}

	/**
	 * Checks if the user has given role.
	 *
	 * @param role
	 * 		The role is provided as string
	 * @param authentication
	 * 		The authentication object
	 * @return False if logged in user doesn't have given role, otherwise true.
	 */
	protected boolean hasRole(final String role, final Authentication authentication)
	{
		for (final GrantedAuthority ga : ((OAuth2Authentication) authentication).getOAuth2Request().getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	protected TmaSubscribedProductFacade getTmaSubscribedProductFacade()
	{
		return tmaSubscribedProductFacade;
	}

	protected TmaSubscriptionBaseFacade getTmaSubscriptionBaseFacade()
	{
		return tmaSubscriptionBaseFacade;
	}

	protected TmaOrderFacade getOrderFacade()
	{
		return orderFacade;
	}
}
