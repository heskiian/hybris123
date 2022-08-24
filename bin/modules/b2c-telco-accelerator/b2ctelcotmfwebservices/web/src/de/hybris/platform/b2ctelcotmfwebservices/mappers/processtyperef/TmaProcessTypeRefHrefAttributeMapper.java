/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.processtyperef;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessTypeRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProcessType} and {@link ProcessTypeRef}
 *
 * @since 1907
 */
public class TmaProcessTypeRefHrefAttributeMapper extends TmaAttributeMapper<TmaProcessType, ProcessTypeRef>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaProcessType source, ProcessTypeRef target, MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getCode()))
		  {
				target.setHref(B2ctelcotmfwebservicesConstants.TMA_PROCESS_TYPES_API_URL + source.getCode());
		  }
	 }
}
