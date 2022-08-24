/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementspecification;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attachements attribute between {@link AgrAgreementSpecificationModel} and
 * {@link AgreementSpecification}
 *
 * @since 2108
 */
public class AgreementSpecificationAttachmentAttributeMapper
		extends AgrAttributeMapper<AgrAgreementSpecificationModel, AgreementSpecification>
{
	private final MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementSpecificationAttachmentAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementSpecificationModel source,
			final AgreementSpecification target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAttachments()))
		{
			return;
		}


		final List<AttachmentRefOrValue> relatedParties = source.getAttachments().stream()
				.map(attachment -> getMapperFacade().map(attachment, AttachmentRefOrValue.class, context))
				.collect(Collectors.toList());

		target.setAttachment(relatedParties);
	}

	@Override
	public void populateSourceAttributeFromTarget(AgreementSpecification target,
			AgrAgreementSpecificationModel source, MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAttachment()))
		{
			return;
		}
		source.setAttachments(getAllAttachements(target.getAttachment(), context));
	}

	private List<CatalogUnawareMediaModel> getAllAttachements(final List<AttachmentRefOrValue> attachmentRefOrValues,
			final MappingContext context)
	{
		final List<CatalogUnawareMediaModel> result = new ArrayList<>();
		attachmentRefOrValues.forEach(attachmentRefOrValue -> {

			final Map parameters = new HashMap();
			parameters.put(CatalogUnawareMediaModel.CODE, attachmentRefOrValue.getId());

			CatalogUnawareMediaModel catalogUnawareMediaModel = (CatalogUnawareMediaModel) getAgrGenericService().getItem(
					CatalogUnawareMediaModel._TYPECODE, parameters);

			if (catalogUnawareMediaModel == null)
			{
				catalogUnawareMediaModel = (CatalogUnawareMediaModel) getAgrGenericService()
						.createItem(CatalogUnawareMediaModel.class);
			}

			getMapperFacade().map(attachmentRefOrValue, catalogUnawareMediaModel, context);
			getAgrGenericService().saveItem(catalogUnawareMediaModel);

			result.add(catalogUnawareMediaModel);
		});
		return result;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
