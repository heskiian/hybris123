/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.security;

import de.hybris.platform.billmanagementservices.model.BmPartyModel;
import de.hybris.platform.billmanagementservices.services.BmPartyBillService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2108
 */
public class BmUserValidator
{
	private static final Logger LOG = Logger.getLogger(BmUserValidator.class);

	private UserService userService;

	private BmPartyBillService partyBillService;

	public BmUserValidator(final UserService userService, final BmPartyBillService partyBillService)
	{
		this.userService = userService;
		this.partyBillService = partyBillService;
	}

	public boolean isResourceOwner(final Authentication authentication, final String customerBillId)
	{
		if (StringUtils.isBlank(customerBillId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}

		try
		{
			final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
			final Set<BmPartyModel> relatedParties = getPartyBillService().getPartyBill(customerBillId).getParties();

			if (CollectionUtils.isNotEmpty(relatedParties))
			{
				return relatedParties.contains(user.getBmParty());
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

	protected BmPartyBillService getPartyBillService()
	{
		return partyBillService;
	}
}
