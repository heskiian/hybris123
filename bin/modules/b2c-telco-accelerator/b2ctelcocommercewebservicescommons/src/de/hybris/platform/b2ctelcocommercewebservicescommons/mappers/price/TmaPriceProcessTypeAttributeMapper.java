/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProcessTypeWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaPriceProcessTypeAttributeMapper populates value of processType attribute from {@link PriceData} to
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceProcessTypeAttributeMapper
		extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getProcessTypes()))
		{
			return;
		}

		final List<ProcessTypeWsDTO> processTypes = source.getProcessTypes().stream()
				.map(processType -> createProcessTypeWsDTO(processType.getCode())).collect(Collectors.toList());

		target.setProcessType(processTypes);
	}

	private ProcessTypeWsDTO createProcessTypeWsDTO(final String code)
	{
		final ProcessTypeWsDTO processTypeDto = new ProcessTypeWsDTO();
		processTypeDto.setId(code);
		return processTypeDto;
	}

}
