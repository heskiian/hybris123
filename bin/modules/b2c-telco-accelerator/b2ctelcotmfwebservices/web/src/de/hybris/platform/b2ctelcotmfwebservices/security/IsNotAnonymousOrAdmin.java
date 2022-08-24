/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for securing rest endpoints. <br>
 * Only users that have role TRUSTED_CLIENT can retrieve data regarding the anonymous user from this endpoint.
 *
 * @since 1907
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.isNotAnonymous(#relatedPartyId)")
public @interface IsNotAnonymousOrAdmin
{
}
