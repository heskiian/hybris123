/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiAgreementItemModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.AgreementItemRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreement attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductAgreementAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductAgreementAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAgreementItems()))
		{
			return;
		}

		final List<AgreementItemRef> agreements = new ArrayList<>();
		source.getAgreementItems().forEach(agreementItem -> {
			final AgreementItemRef agreementItemRef = getMapperFacade().map(agreementItem, AgreementItemRef.class, context);
			agreements.add(agreementItemRef);
		});

		target.setAgreement(agreements);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAgreement()))
		{
			return;
		}

		source.setAgreementItems(getAllAgreements(target.getAgreement(), context));
	}

	private Set<SpiAgreementItemModel> getAllAgreements(final List<AgreementItemRef> agreementItemRefs,
			final MappingContext context)
	{
		final Set<SpiAgreementItemModel> result = new HashSet<>();
		agreementItemRefs.forEach(agreementItemRef -> {
			SpiAgreementItemModel agreementItemModel = (SpiAgreementItemModel) getSpiGenericService()
					.getItem(SpiAgreementItemModel._TYPECODE, agreementItemRef.getId());
			if (agreementItemModel == null)
			{
				agreementItemModel = (SpiAgreementItemModel) getSpiGenericService()
						.createItem(SpiAgreementItemModel.class);
			}
			getMapperFacade().map(agreementItemRef, agreementItemModel, context);
			getSpiGenericService().saveItem(agreementItemModel);
			result.add(agreementItemModel);
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
