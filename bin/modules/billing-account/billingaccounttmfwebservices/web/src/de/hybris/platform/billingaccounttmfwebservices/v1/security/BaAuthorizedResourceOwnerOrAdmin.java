/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for securing rest endpoints. <br>
 * Only users that have role TRUSTED_CLIENT or users that own the resource can retrieve data from this endpoint.
 *
 * @since 2105
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.isResourceOwner(authentication, #id)")
public @interface BaAuthorizedResourceOwnerOrAdmin
{
}
