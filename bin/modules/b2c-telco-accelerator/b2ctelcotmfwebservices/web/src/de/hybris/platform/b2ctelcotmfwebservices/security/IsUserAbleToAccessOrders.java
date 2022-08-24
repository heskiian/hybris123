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
 * This annotation checks if the user is able to access orders.
 *
 * @since 2105
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.canUserListOrders(authentication, #relatedPartyId)")
public @interface IsUserAbleToAccessOrders
{
}
