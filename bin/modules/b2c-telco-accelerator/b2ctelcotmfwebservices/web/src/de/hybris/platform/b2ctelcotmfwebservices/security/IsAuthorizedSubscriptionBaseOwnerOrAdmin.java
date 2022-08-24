/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Annotation for securing rest endpoints. <br>
 * Only users that have role TRUSTED_CLIENT or users that own the subscription can retrieve data from this endpoint.
 *
 * @since 2102
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.isSubscriptionBaseOwner(authentication, #subscriptionBaseId)")
public @interface IsAuthorizedSubscriptionBaseOwnerOrAdmin
{
}
