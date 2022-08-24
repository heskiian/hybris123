/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiPartyModel;
import de.hybris.platform.subscribedproductservices.model.SpiPartyRoleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParty attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductRelatedPartyAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPartyRoles()))
		{
			return;
		}

		final List<RelatedParty> relatedParties = new ArrayList<>();
		source.getPartyRoles().forEach(partyRoleModel -> {
			final RelatedParty relatedParty = getMapperFacade().map(partyRoleModel.getParty(), RelatedParty.class, context);
			relatedParty.setRole(partyRoleModel.getRole());
			relatedParties.add(relatedParty);
		});

		target.setRelatedParty(relatedParties);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getRelatedParty()))
		{
			return;
		}

		source.setPartyRoles(getAllPartyRoles(target.getRelatedParty(), context));
	}

	private Set<SpiPartyRoleModel> getAllPartyRoles(final List<RelatedParty> relatedParties, final MappingContext context)
	{
		final Set<SpiPartyRoleModel> result = new HashSet<>();
		final Set<SpiPartyRoleModel> spiPartyRoleModels = new HashSet<>();
		relatedParties.forEach(relatedParty -> {
			SpiPartyModel spiPartyModel = (SpiPartyModel) getSpiGenericService()
					.getItem(SpiPartyModel._TYPECODE, relatedParty.getId());
			if (spiPartyModel == null)
			{
				spiPartyModel = (SpiPartyModel) getSpiGenericService().createItem(SpiPartyModel.class);
			}
			getMapperFacade().map(relatedParty, spiPartyModel, context);
			getSpiGenericService().saveItem(spiPartyModel);

			final SpiPartyRoleModel spiPartyRoleModel = (SpiPartyRoleModel) getSpiGenericService()
					.createItem(SpiPartyRoleModel.class);
			if (StringUtils.isNotEmpty(relatedParty.getRole()))
			{
				spiPartyRoleModel.setRole(relatedParty.getRole());
			}
			spiPartyRoleModel.setParty(spiPartyModel);
			spiPartyRoleModels.add(spiPartyRoleModel);
			spiPartyModel.setPartyRoles(spiPartyRoleModels);
			result.addAll(spiPartyModel.getPartyRoles());
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
