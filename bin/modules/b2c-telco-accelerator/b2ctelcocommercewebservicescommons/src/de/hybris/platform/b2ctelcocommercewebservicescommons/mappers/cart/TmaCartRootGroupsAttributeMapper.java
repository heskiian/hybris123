/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RootGroupsWsDTO;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps root groups attribute between {@link CartData} and {@link CartWsDTO}
 *
 * @since 1911
 */
public class TmaCartRootGroupsAttributeMapper extends TmaAttributeMapper<CartData, CartWsDTO>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final CartData source, final CartWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getRootGroups() == null)
		{
			return;
		}
		final List<RootGroupsWsDTO> rootGroups = new ArrayList<>();
		source.getRootGroups().forEach(group ->
		{
			final RootGroupsWsDTO rootGroup = getMapperFacade().map(group, RootGroupsWsDTO.class, context);
			rootGroups.add(rootGroup);
		});
		target.setRootGroups(rootGroups);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
