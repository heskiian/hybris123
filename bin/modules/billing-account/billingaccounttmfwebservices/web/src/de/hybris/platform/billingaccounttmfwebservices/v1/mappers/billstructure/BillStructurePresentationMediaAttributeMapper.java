/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billstructure;

import de.hybris.platform.billingaccountservices.model.BaBillPresentationMediaModel;
import de.hybris.platform.billingaccountservices.model.BaBillStructureModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillPresentationMediaRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillStructure;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for presentationMedia attribute between
 * {@link BaBillStructureModel} and {@link BillStructure}
 *
 * @since 2105
 */
public class BillStructurePresentationMediaAttributeMapper
		extends BaAttributeMapper<BaBillStructureModel, BillStructure>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillStructurePresentationMediaAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillStructureModel source, final BillStructure target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPresentationMedias()))
		{
			return;
		}

		final List<BillPresentationMediaRefOrValue> presentationMedias = source.getPresentationMedias().stream()
				.map(media -> getMapperFacade().map(media, BillPresentationMediaRefOrValue.class, context))
				.collect(Collectors.toList());


		target.setPresentationMedia(presentationMedias);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillStructure target, final BaBillStructureModel source,
			final MappingContext context)
	{
		if (target.getPresentationMedia() == null)
		{
			return;
		}

		source.setPresentationMedias(getAllPresentationMedias(target.getPresentationMedia(), context));
	}

	private Set<BaBillPresentationMediaModel> getAllPresentationMedias(
			final List<BillPresentationMediaRefOrValue> presentationMediaRefOrValues, final MappingContext context)
	{
		final Set<BaBillPresentationMediaModel> result = new HashSet<>();
		presentationMediaRefOrValues.forEach(billPresentationMediaRefOrValue -> {
			BaBillPresentationMediaModel billPresentationMediaModel = (BaBillPresentationMediaModel) getBaGenericService()
					.getItem(BaBillPresentationMediaModel._TYPECODE, billPresentationMediaRefOrValue.getId());
			if (billPresentationMediaModel == null)
			{
				billPresentationMediaModel = (BaBillPresentationMediaModel) getBaGenericService()
						.createItem(BaBillPresentationMediaModel.class);
			}
			getMapperFacade().map(billPresentationMediaRefOrValue, billPresentationMediaModel, context);
			getBaGenericService().saveItem(billPresentationMediaModel);
			result.add(billPresentationMediaModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
