/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for user attribute between {@link PriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceUserAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getUser()))
		{
			return;
		}

		final TmaUserData user = source.getUser();
		final UserWsDTO userWsDTO = new UserWsDTO();
		userWsDTO.setCustomerId(user.getUid());
		target.setUser(userWsDTO);
	}
}
