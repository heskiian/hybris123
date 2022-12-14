/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;


/**
 * Test model converter that simulates servicelayer.prefetch=all
 */
public class PrefetchAllModelConverter extends ItemModelConverter
{

	public PrefetchAllModelConverter(final ModelService modelService, final I18NService i18nService,
	                                 final CommonI18NService commonI18NService, final String type,
	                                 final Class<? extends AbstractItemModel> modelClass,
	                                 final SerializationStrategy serializationStrategy, final SourceTransformer sourceTransformer,
	                                 final SelfHealingService selfHealingService)
	{
		super(modelService, i18nService, commonI18NService, type, modelClass, serializationStrategy, sourceTransformer,
				selfHealingService);
	}

	@Override
	protected AttributePrefetchMode readPrefetchSettings()
	{
		return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_ALL;
	}
}
