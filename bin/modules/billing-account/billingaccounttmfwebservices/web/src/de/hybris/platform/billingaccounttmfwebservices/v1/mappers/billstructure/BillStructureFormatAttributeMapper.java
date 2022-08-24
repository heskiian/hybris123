/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billstructure;

import de.hybris.platform.billingaccountservices.model.BaBillFormatModel;
import de.hybris.platform.billingaccountservices.model.BaBillStructureModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillFormatRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillStructure;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for format attribute between {@link BaBillStructureModel} and {@link BillStructure}
 *
 * @since 2105
 */
public class BillStructureFormatAttributeMapper extends BaAttributeMapper<BaBillStructureModel, BillStructure>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillStructureFormatAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (ObjectUtils.isEmpty(source.getFormat()))
		{
			return;
		}
		target.setFormat(getMapperFacade().map(source.getFormat(), BillFormatRefOrValue.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillStructure target, final BaBillStructureModel source,
			final MappingContext context)
	{
		if (target.getFormat() == null)
		{
			return;
		}
		source.setFormat(getBillFormat(target.getFormat(), context));
	}

	private BaBillFormatModel getBillFormat(final BillFormatRefOrValue billFormatRefOrValue, final MappingContext context)
	{
		BaBillFormatModel billFormatModel = (BaBillFormatModel) getBaGenericService()
				.getItem(BaBillFormatModel._TYPECODE, billFormatRefOrValue.getId());
		if (billFormatModel == null)
		{
			billFormatModel = (BaBillFormatModel) getBaGenericService().createItem(BaBillFormatModel.class);
		}
		getMapperFacade().map(billFormatRefOrValue, billFormatModel, context);
		getBaGenericService().saveItem(billFormatModel);
		return billFormatModel;
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
