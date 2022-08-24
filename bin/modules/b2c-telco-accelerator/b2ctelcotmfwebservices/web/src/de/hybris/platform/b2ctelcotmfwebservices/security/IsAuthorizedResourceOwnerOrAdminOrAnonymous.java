/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Annotation for securing rest endpoints. <br>
 * Users that have role TRUSTED_CLIENT, users that own the resource or anonymous users can retrieve data from this endpoint.
 *
 * @since 1907
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('TRUSTED_CLIENT') OR @userValidator.isAnonymous(authentication, #relatedPartyId) OR "
		+ "@userValidator.isResourceOwner(authentication, #relatedPartyId)")
public @interface IsAuthorizedResourceOwnerOrAdminOrAnonymous
{
}
