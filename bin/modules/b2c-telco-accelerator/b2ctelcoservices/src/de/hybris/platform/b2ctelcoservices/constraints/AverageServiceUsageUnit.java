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
 * Triggers when if the Selected AverageServiceUsage and ProductSpecCharValue have compatible units
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel#getUnitOfMeasure()} and
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel#getUnitOfMeasure()} are
 * compatible
 *
 * @since 6.7
 */

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = AverageServiceUsageUnitValidator.class)
@Documented
public @interface AverageServiceUsageUnit
{
	String message() default "{de.hybris.platform.b2ctelcoservices.constraints.AverageServiceUsageUnit.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

