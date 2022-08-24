/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * Triggers the validator to check if the selected TmaProductOfferingGroup has valid children.
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel#childProductOfferings()}
 * 
 * @since 6.7
 */

@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = TmaPoGroupImmediateChildrenValidator.class)
@Documented
public @interface TmaPoGroupImmediateChildren
{
	String message() default "{de.hybris.platform.b2ctelcoservices.constraints.TmaPoGroupImmediateChildren.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
