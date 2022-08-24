/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.security;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscribedproductservices.model.SpiPartyModel;
import de.hybris.platform.subscribedproductservices.model.SpiPartyRoleModel;
import de.hybris.platform.subscribedproductservices.services.SpiProductService;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2105
 */
public class SpiUserValidator
{
	private UserService userService;
	private SpiProductService productService;

	public SpiUserValidator(final UserService userService, final SpiProductService productService)
	{
		this.productService = productService;
		this.userService = userService;
	}

	public boolean isResourceOwner(final Authentication authentication, final String productId)
	{
		if (StringUtils.isBlank(productId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}
		final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
		final Set<SpiPartyRoleModel> partyRoles = getProductService().getProduct(productId).getPartyRoles();
		if (CollectionUtils.isNotEmpty(partyRoles))
		{
			final Set<SpiPartyModel> relatedParties =
					partyRoles.stream().map(SpiPartyRoleModel::getParty).collect(Collectors.toSet());
			return relatedParties.contains(user.getSpiParty());
		}

		return Boolean.FALSE;
	}


	protected UserService getUserService()
	{
		return userService;
	}

	protected SpiProductService getProductService()
	{
		return productService;
	}
}
