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
 * Validates the Simple Product OFferings used in creating a new TmaBpoPreConfigModel
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel#getPreConfigSpos()()}
 * .
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = TmaBpoPreconfigurationValidator.class)
@Documented
public @interface TmaBpoPreConfiguration
{
	String message() default "{de.hybris.platform.b2ctelcoservices.constraints.TmaBpoPreConfiguration.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
