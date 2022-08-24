/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates if the given anonymous cart is valid and are eligible for requested user
 *
 * @since 1911
 */
public class TmaAnonymousCartValidator implements Validator
{
	private UserFacade userFacade;

	private TmaCartFacade tmaCartFacade;

	public TmaAnonymousCartValidator(final UserFacade userFacade, final TmaCartFacade tmaCartFacade)
	{
		this.userFacade = userFacade;
		this.tmaCartFacade = tmaCartFacade;
	}


	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.equals(arg0);
	}

	@Override
	public void validate(final Object cartGUID, final Errors arg1)
	{
		final String fromCartGUID = (String) cartGUID;
		if (userFacade.isAnonymousUser())
		{
			throw new CartException("Anonymous user is not allowed to copy cart!");
		}

		if (!tmaCartFacade.isAnonymousUserCart(fromCartGUID))
		{
			throw new CartException("Cart is not anonymous");
		}
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	protected TmaCartFacade getTmaCartFacade()
	{
		return tmaCartFacade;
	}

	public void setTmaCartFacade(final TmaCartFacade tmaCartFacade)
	{
		this.tmaCartFacade = tmaCartFacade;
	}


}
