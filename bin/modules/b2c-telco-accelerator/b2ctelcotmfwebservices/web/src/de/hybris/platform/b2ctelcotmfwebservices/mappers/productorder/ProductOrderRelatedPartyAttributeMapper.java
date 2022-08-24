/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for attype attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 1907
 */
public class ProductOrderRelatedPartyAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
			final MappingContext context)
	{
		if (source.getUser() == null)
		{
			return;
		}

		final RelatedPartyRef relatedPartyRef = getMapperFacade().map(source.getUser(), RelatedPartyRef.class, context);

		final List<RelatedPartyRef> relatedPartyRefList = new ArrayList<>();
		relatedPartyRefList.add(relatedPartyRef);

		target.setRelatedParty(relatedPartyRefList);
	}

	@Override
	public void populateSourceAttributeFromTarget(ProductOrder target, OrderData source,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getRelatedParty())) {
			return;
		}

		PrincipalData userData = new PrincipalData();
		userData.setUid(target.getRelatedParty().get(0).getId());

		source.setUser(userData);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
