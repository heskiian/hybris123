/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates if the given user cart are valid and is eligible for requested user
 *
 * @since 1911
 */
public class TmaUserCartValidator implements Validator
{
	private final TmaCartFacade tmaCartFacade;

	public TmaUserCartValidator(final TmaCartFacade tmaCartFacade)
	{
		this.tmaCartFacade = tmaCartFacade;
	}

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.equals(arg0);
	}

	@Override
	public void validate(final Object arg0, final Errors arg1)
	{
		String evaluatedToMergeCartGuid = (String) arg0;
		if (StringUtils.isEmpty(evaluatedToMergeCartGuid))
		{
			evaluatedToMergeCartGuid = getTmaCartFacade().getSessionCart().getGuid();
		}
		
		if (!getTmaCartFacade().isCurrentUserCart(evaluatedToMergeCartGuid))
		{
			throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, evaluatedToMergeCartGuid);
		}
	}

	public TmaCartFacade getTmaCartFacade()
	{
		return tmaCartFacade;
	}

}
