/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;
import de.hybris.platform.upilintegrationservices.daos.impl.DefaultIsuConfigSyncDao;
import de.hybris.platform.upilintegrationservices.enums.SemanticType;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;
import de.hybris.platform.upilintegrationservices.service.UpilintegrationservicesService;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Default implementation of the {@link UpilintegrationservicesService}.
 *
 * @since 1911
 */
public class DefaultUpilintegrationservicesService implements UpilintegrationservicesService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultUpilintegrationservicesService.class);

	private ModelService modelService;
	private EnumerationService enumerationService;
	private DefaultUpilIntegrationClientService upilIntegrationClientService;
	private DefaultIsuConfigSyncDao isuConfigSyncDao;

	@Override
	public Boolean syncIsuConfigWithTua(final CatalogVersionModel catalogVersionModel)
	{
		try
		{
			final Boolean isSyncIsuReferenceTypes = syncIsuReferenceTypes();
			final Boolean isSyncIsuSemantics = syncIsuSemantics(catalogVersionModel);
			return isSyncIsuReferenceTypes && isSyncIsuSemantics ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Error occurred during config sync from UIL", e);
			return Boolean.FALSE;
		}
	}

	private Boolean syncIsuReferenceTypes()
	{
		try
		{
			final List<ODataEntry> isuReferenceTypes = getUpilIntegrationClientService().getIsuReferenceTypes();
			if (CollectionUtils.isNotEmpty(isuReferenceTypes))
			{
				isuReferenceTypes.forEach(entry ->
				{
					final Map<String, Object> properties = entry.getProperties();
					createReferenceType(String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_REFERENCE_PRODUCT)),
							String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_REFERENCE_PRODUCT_DESCRIPTION)));

				});
				LOG.info(" Successfully synchronized ReferenceType from UIL");
				return Boolean.TRUE;
			}
		}
		catch (final UpilintegrationservicesException e)
		{
			LOG.error("Error occurred during ReferenceType sync from UIL \n" + e.getMessage(), e);
		}
		return Boolean.FALSE;
	}

	private Boolean syncIsuSemantics(final CatalogVersionModel catalogVersionModel)
	{
		try
		{
			final List<ODataEntry> isuSemanticEntries = getUpilIntegrationClientService().getIsuSemantics();
			if (CollectionUtils.isNotEmpty(isuSemanticEntries))
			{
				isuSemanticEntries.forEach(entry ->
				{
					createSemantics(catalogVersionModel, entry.getProperties());
				});
			}
			LOG.info(" Successfully synchronized Semantics from UIL");
			return Boolean.TRUE;
		}
		catch (final UpilintegrationservicesException e)
		{
			LOG.error("Error occurred during semantics sync from UIL \n" + e.getMessage(), e);
		}
		return Boolean.FALSE;
	}

	private void createSemantics(final CatalogVersionModel catalogVersionModel, final Map<String, Object> properties)
	{
		final String semanticType = getSemanticsType(
				String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_TYPE)));
		if (StringUtils.isNotBlank(semanticType)
				&& CollectionUtils.isEmpty(getIsuConfigSyncDao().findUpilSemantics(catalogVersionModel,
						properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1),
						properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2), semanticType)))
		{
			final UpilSemanticsModel upilSemanticsModel = getModelService().create(UpilSemanticsModel.class);
			upilSemanticsModel
					.setSemanticsName1(String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1)));
			upilSemanticsModel
					.setSemanticsName2(String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2)));
			upilSemanticsModel.setSemanticType(getEnumerationService().getEnumerationValue(SemanticType.class, semanticType));
			upilSemanticsModel
					.setDescription(String.valueOf(properties.get(UpilintegrationservicesConstants.UTILS_SEMANTICS_DESCRIPTION)));
			upilSemanticsModel.setCatalogVersion(catalogVersionModel);
			getModelService().save(upilSemanticsModel);
		}
	}

	private void createReferenceType(final String referenceType, final String description)
	{
		if (StringUtils.isNotBlank(referenceType)
				&& CollectionUtils.isEmpty(getIsuConfigSyncDao().getTmaProductSpecTypeForCode(referenceType)))
		{
			final TmaProductSpecTypeModel tmaProductSpecTypeModel = getModelService().create(TmaProductSpecTypeModel.class);
			tmaProductSpecTypeModel.setCode(referenceType);
			tmaProductSpecTypeModel.setDescription(description);
			getModelService().save(tmaProductSpecTypeModel);
		}
	}

	private String getSemanticsType(final String utilsSemanticsType)
	{
		String semanticType = null;
		switch (utilsSemanticsType)
		{
			case "1":
				semanticType = UpilintegrationservicesConstants.UPIL_BILLING_ATTRIBUTES;
				break;
			case "2":
				semanticType = UpilintegrationservicesConstants.UPIL_SALES_ATTRIBUTES;
				break;
			case "3":
				semanticType = UpilintegrationservicesConstants.RECURRING_CHARGE_ENTRY;
				break;
			case "4":
				semanticType = UpilintegrationservicesConstants.USAGE_CHARGE_ENTRY;
				break;
			case "5":
				semanticType = UpilintegrationservicesConstants.ONETIME_CHARGE_ENTRY;
				break;
			case "6":
				semanticType = UpilintegrationservicesConstants.DISCOUNT;
				break;
		}
		return semanticType;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Autowired
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected DefaultUpilIntegrationClientService getUpilIntegrationClientService()
	{
		return upilIntegrationClientService;
	}

	@Autowired
	public void setUpilIntegrationClientService(final DefaultUpilIntegrationClientService upilIntegrationClientService)
	{
		this.upilIntegrationClientService = upilIntegrationClientService;
	}

	protected DefaultIsuConfigSyncDao getIsuConfigSyncDao()
	{
		return isuConfigSyncDao;
	}

	@Autowired
	public void setIsuConfigSyncDao(final DefaultIsuConfigSyncDao isuConfigSyncDao)
	{
		this.isuConfigSyncDao = isuConfigSyncDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Autowired
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
