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
 * A client application performing a call to this API for a client having trusted role, should be able to update any order. In
 * case there is an authenticated user that user should be the approver of the company for which order is to be updated otherwise
 * the operation should not be permitted.
 *
 * @since 2105
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.isAuthorizedForProductOrderUpdate(authentication, #productOrder, #id)")
public @interface IsAuthorizedProductOrderUpdate
{
}
