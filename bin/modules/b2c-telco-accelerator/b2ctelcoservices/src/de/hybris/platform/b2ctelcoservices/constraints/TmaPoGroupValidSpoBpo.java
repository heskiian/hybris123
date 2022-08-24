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
 * Triggers the validator to check if the selected TmaProductOfferingGroup has valid children. In this case it prevents
 * the group from having both Simple Product Offering and Bundled Product Offering in the same group.
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel#childProductOfferings()}
 * 
 * @since 6.7
 */

@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = TmaPoGroupValidSpoBpoValidator.class)
@Documented
public @interface TmaPoGroupValidSpoBpo
{
	String message() default "{de.hybris.platform.b2ctelcoservices.constraints.TmaPoGroupValidSpoBpo.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
