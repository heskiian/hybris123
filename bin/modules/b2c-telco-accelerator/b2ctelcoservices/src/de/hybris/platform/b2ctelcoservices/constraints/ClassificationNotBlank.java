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
 * The annotated element (a product's classification attribute that is assigned to the product's category) must not be
 * empty or <code>null</code>. Supported type is {@link de.hybris.platform.core.model.product.ProductModel} and its sub types.
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ClassificationNotBlankValidator.class)
@Documented
public @interface ClassificationNotBlank
{
	String message() default "{de.hybris.platform.b2ctelcoservices.constraints.ClassificationNotBlank.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String category();

	String classification();

}
