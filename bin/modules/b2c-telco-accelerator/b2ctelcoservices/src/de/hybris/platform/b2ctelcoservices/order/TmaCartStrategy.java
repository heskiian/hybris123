/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;


/**
 * Strategy for cart updates.
 *
 * @since 1907
 */
public interface TmaCartStrategy
{
	/**
	 * Processes cart updates.
	 *
	 * @param commerceCartParameterList
	 * 		contains attributes used for cart updates
	 * @return list {@link CommerceCartModification}
	 * @throws CommerceCartModificationException
	 * 		in case of any error occurs during cart update
	 */
	List<CommerceCartModification> processCartAction(List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException;
}
