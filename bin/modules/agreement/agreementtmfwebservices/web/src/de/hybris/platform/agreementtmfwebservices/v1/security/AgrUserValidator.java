/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.security;

import de.hybris.platform.agreementservices.model.AgrPartyModel;
import de.hybris.platform.agreementservices.model.AgrPartyRoleModel;
import de.hybris.platform.agreementservices.services.AgrAgreementSpecificationService;
import de.hybris.platform.agreementservices.services.AgrAgreementsService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2108
 */
public class AgrUserValidator
{
	private UserService userService;
	private AgrAgreementsService agreementsService;
	private AgrAgreementSpecificationService agreementSpecificationService;

	public AgrUserValidator(final UserService userService, final AgrAgreementsService agreementsService,
			final AgrAgreementSpecificationService agreementSpecificationService)
	{
		this.agreementsService = agreementsService;
		this.userService = userService;
		this.agreementSpecificationService = agreementSpecificationService;
	}

	public boolean isAgreementOwner(final Authentication authentication, final String agreementId)
	{
		if (StringUtils.isBlank(agreementId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}
		final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
		final Set<AgrPartyRoleModel> partyRoles = getAgreementsService().getAgreement(agreementId).getPartyRoles();
		if (CollectionUtils.isNotEmpty(partyRoles))
		{
			final Set<AgrPartyModel> relatedParties =
					partyRoles.stream().map(AgrPartyRoleModel::getParty).collect(Collectors.toSet());
			return relatedParties.contains(user.getAgrParty());
		}

		return Boolean.FALSE;
	}

	public boolean isAgreementSpecificationOwner(final Authentication authentication, final String agreementSpecificationId)
	{
		if (StringUtils.isBlank(agreementSpecificationId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}
		final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
		final Set<AgrPartyRoleModel> partyRoles = getAgreementSpecificationService()
				.getAgreementSpecification(agreementSpecificationId).getPartyRoles();
		if (CollectionUtils.isNotEmpty(partyRoles))
		{
			final Set<AgrPartyModel> relatedParties =
					partyRoles.stream().map(AgrPartyRoleModel::getParty).collect(Collectors.toSet());
			return relatedParties.contains(user.getAgrParty());
		}

		return Boolean.FALSE;
	}


	protected UserService getUserService()
	{
		return userService;
	}

	protected AgrAgreementsService getAgreementsService()
	{
		return agreementsService;
	}

	protected AgrAgreementSpecificationService getAgreementSpecificationService()
	{
		return agreementSpecificationService;
	}
}
