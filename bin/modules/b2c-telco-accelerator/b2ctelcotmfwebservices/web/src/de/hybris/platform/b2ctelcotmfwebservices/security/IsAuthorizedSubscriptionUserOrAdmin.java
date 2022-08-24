/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Annotation for securing rest endpoints.<br>
 * Only Client having role TRUSTED_CLIENT or current user having OWNER or BENEFICIARY access to the subscription is able
 * to consume the endpoint.
 *
 * @since 2102
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR " + "@userValidator.isAuthorizedSubscriptionUser(authentication, #id)")
public @interface IsAuthorizedSubscriptionUserOrAdmin
{
}
