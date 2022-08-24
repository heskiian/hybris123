/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import org.springframework.util.ObjectUtils;

import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.impl.DefaultChangeDetectionService;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;

/**
 * Extending the default implementation of the {@link DefaultChangeDetectionService} to change the VersionMarker status.
 * @since 1909
 */
public class DefaultUpilPricePlanChangeDetectionService extends DefaultChangeDetectionService{
	public void changeVersionMarkerStatus(String streamId, Long itemPk){
		ItemVersionMarkerModel itemVersionMarker = findVersionMarkerByItemPK(streamId, itemPk);
		if(!ObjectUtils.isEmpty(itemVersionMarker)){
			itemVersionMarker.setStatus(ItemVersionMarkerStatus.DELETED);
			getModelService().save(itemVersionMarker);
		}
	}
}
