/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.basestoreref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BaseStoreRef;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BaseStoreData} and {@link BaseStoreRef}
 *
 * @since 1907
 */
public class BaseStoreRefHrefAttributeMapper extends TmaAttributeMapper<BaseStoreData, BaseStoreRef>
{
	@Override
	public void populateTargetAttributeFromSource(BaseStoreData source, BaseStoreRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getUid()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.BASE_STORE_REF_API_URL + source.getUid());
		}
	}
}
