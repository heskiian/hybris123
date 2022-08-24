/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Builder that creates instances of {@link TmaCartValidationModel}
 *
 * @since 1911
 */
public class TmaCartValidationBuilder
{
	private ModelService modelService;
	private KeyGenerator keyGenerator;

	public TmaCartValidationBuilder(ModelService modelService, KeyGenerator keyGenerator)
	{
		this.modelService = modelService;
		this.keyGenerator = keyGenerator;
	}

	/**
	 * Creates a list of cart validation objects based on the messages received
	 *
	 * @param code
	 * 		the code to be set on the validation objects
	 * @param messages
	 * 		the messages to be set on the validation objects
	 * @return the list of cart validation objects
	 */
	public Set<TmaCartValidationModel> createCartValidations(final String code, final List<String> messages)
	{
		Set<TmaCartValidationModel> validationMessages = new HashSet<>();
		messages.forEach(errorMessage -> {
			final TmaCartValidationModel validation = getModelService().create(TmaCartValidationModel.class);
			validation.setId((String) getKeyGenerator().generate());
			validation.setCode(code);
			validation.setMessage(errorMessage);
			getModelService().save(validation);
			validationMessages.add(validation);
		});

		return validationMessages;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
