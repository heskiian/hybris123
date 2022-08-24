/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.security;

import de.hybris.platform.billingaccountservices.model.BaPartyModel;
import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.billingaccountservices.services.BaBillingAccountService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2105
 */
public class BaUserValidator
{
	private static final Logger LOG = Logger.getLogger(BaUserValidator.class);

	private UserService userService;

	private BaBillingAccountService billingAccountService;

	public BaUserValidator(final UserService userService, final BaBillingAccountService billingAccountService)
	{
		this.billingAccountService = billingAccountService;
		this.userService = userService;
	}

	public boolean isResourceOwner(final Authentication authentication, final String billingAccountId)
	{
		if (StringUtils.isBlank(billingAccountId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}

		try
		{
			final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
			final Set<BaPartyRoleModel> partyRoles = getBillingAccountService().getBillingAccount(billingAccountId).getPartyRoles();

			if (CollectionUtils.isNotEmpty(partyRoles))
			{
				final Set<BaPartyModel> relatedParties =
						partyRoles.stream().map(BaPartyRoleModel::getParty).collect(Collectors.toSet());
				return relatedParties.contains(user.getBaParty());
			}
		}
		catch (final ModelNotFoundException e)
		{
			LOG.debug("Object not found ", e);
			return false;
		}

		return Boolean.FALSE;
	}


	protected UserService getUserService()
	{
		return userService;
	}

	protected BaBillingAccountService getBillingAccountService()
	{
		return billingAccountService;
	}
}
