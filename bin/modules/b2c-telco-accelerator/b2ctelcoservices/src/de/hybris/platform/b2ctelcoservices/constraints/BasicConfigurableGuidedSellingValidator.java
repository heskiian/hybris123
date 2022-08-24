/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;


/**
 * Base class for Guided Selling Journey and Guided Selling Step validators.
 * Despite validators are not spring beans, this class and it's derivables support Spring DI.
 *
 * @param <C>
 * 		validator's annotation class
 */
public abstract class BasicConfigurableGuidedSellingValidator<C extends Annotation> extends TmaTypeValidatorHelper
		implements ConstraintValidator<C, ItemModel>
{
	@Override
	public void initialize(final C annotation)
	{
		Registry.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}
}
