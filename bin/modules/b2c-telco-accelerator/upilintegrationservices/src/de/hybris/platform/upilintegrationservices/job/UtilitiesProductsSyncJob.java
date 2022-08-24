/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.job;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.UpilResponse;
import de.hybris.platform.upilintegrationservices.data.UpilResponseList;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;
import de.hybris.platform.upilintegrationservices.model.IsuProductSyncCronJobModel;
import de.hybris.platform.upilintegrationservices.service.UpilUtilitiesProductService;
import de.hybris.platform.upilintegrationservices.service.impl.DefaultUpilPricePlanChangeDetectionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.ImmutableMap;


/**
 * Job class to perform the delta detection on {@link SubscriptionPricePlanModel} and sync the changed items with
 * billing system.
 * system.
 *
 * @since 1911
 */
public class UtilitiesProductsSyncJob extends AbstractJobPerformable<IsuProductSyncCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(UtilitiesProductsSyncJob.class);
	private static final String SPO_PRICES_SELECTOR = "{item.catalogVersion} IN (?cv) AND {item.isuCreationDate} IS NULL AND {item.pk} in ("
			+ "{{ select {pp.pk} from { SubscriptionPricePlan as pp  join TmaProductOffering as po on {pp.product}={po.pk} join TmaProductSpecification as ps on {po.productSpecification}={ps.pk}}"
			+ " where {po.itemtype}= ({{select {pk} from {ComposedType} where {code}='TmaSimpleProductOffering'}}) AND {ps.type} in (?ps) }})";
	private static final String BPO_OVERRIDES_SELECTOR = "{item.catalogVersion} IN (?cv) AND {item.isuCreationDate} IS NULL AND {item.product} in ("
			+ "{{ select {spp.product} from { SubscriptionPricePlan as spp  join TmaProductOffering as bpo on {spp.product}={bpo.pk}} where {bpo.itemtype}= ({{select {pk} from {ComposedType} where {code}='TmaBundledProductOffering'}}) }}) AND {item.pk} in ("
			+ "{{ select {pp.pk} from { SubscriptionPricePlan as pp  join TmaProductOffering as po on {pp.affectedProductOffering}={po.pk} join TmaProductSpecification as ps on {po.productSpecification}={ps.pk}}"
			+ " where {ps.type} in (?ps) }})";
	private static final String STREAM_ID_FOR_SPO = "isuProductSyncStreamIdForSpo";
	private static final String STREAM_ID_FOR_BPO = "isuProductSyncStreamIdForBpo";


	private TypeService typeService;
	private DefaultUpilPricePlanChangeDetectionService changeDetectionService;
	private UpilUtilitiesProductService upilUtilitiesProductService;

	@Override
	public PerformResult perform(final IsuProductSyncCronJobModel isuProductSyncCronJobModel)
	{
		try
		{
			final List<SubscriptionPricePlanModel> subscriptionPricePlanList = isuProductSyncCronJobModel.getAppliedPricePlans();
			if (CollectionUtils.isEmpty(subscriptionPricePlanList))
			{
				performBatchSync(isuProductSyncCronJobModel.getAppliedCatalogVersions(),
						isuProductSyncCronJobModel.getAppliedProductTypes());
			}
			else
			{
				isuProductSyncCronJobModel.setAppliedPricePlans(null);
				modelService.save(isuProductSyncCronJobModel);
				performItemSync(subscriptionPricePlanList);
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final UpilintegrationservicesException e)
		{
			LOG.error("Cronjob Failure", e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}
	}

	private void performItemSync(final List<SubscriptionPricePlanModel> subscriptionPricePlanList)
			throws UpilintegrationservicesException
	{
		final List<SubscriptionPricePlanModel> commodityPricePlans = new ArrayList<>();
		final Map<String, ItemChangeDTO> productChangesToConsume = new HashMap<>();
		subscriptionPricePlanList.forEach(commodityPricePlan ->
		{
			ItemChangeDTO change = null;
			if (commodityPricePlan.getProduct() instanceof TmaBundledProductOfferingModel)
			{
				change = getChangeDetectionService().getChangeForExistingItem(commodityPricePlan,
						STREAM_ID_FOR_BPO);
			}
			else
			{
				change = getChangeDetectionService().getChangeForExistingItem(commodityPricePlan,
						STREAM_ID_FOR_SPO);
			}
			final SubscriptionPricePlanModel pricePlan = modelService.get(PK.fromLong(change.getItemPK()));
			commodityPricePlans.add(pricePlan);
			productChangesToConsume.put(pricePlan.getCode(), change);
		});
		createUpilProduct(commodityPricePlans, productChangesToConsume);
	}

	private void performBatchSync(final Collection<CatalogVersionModel> catalogVersionCollection,
			final List<TmaProductSpecTypeModel> productSpecTypeList) throws UpilintegrationservicesException
	{
		if (CollectionUtils.isNotEmpty(catalogVersionCollection) && CollectionUtils.isNotEmpty(productSpecTypeList))
		{
			final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(SubscriptionPricePlanModel.class);
			final InMemoryChangesCollector changesCollector = new InMemoryChangesCollector();
			getChangeDetectionService().collectChangesForType(composedType,
					getStreamConfig(catalogVersionCollection, productSpecTypeList, SPO_PRICES_SELECTOR, STREAM_ID_FOR_SPO),
					changesCollector);
			getChangeDetectionService().collectChangesForType(composedType,
					getStreamConfig(catalogVersionCollection, productSpecTypeList, BPO_OVERRIDES_SELECTOR, STREAM_ID_FOR_BPO),
					changesCollector);
			final List<ItemChangeDTO> changesCollected = changesCollector.getChanges();
			if (!CollectionUtils.isEmpty(changesCollected))
			{
				final Map<String, ItemChangeDTO> productChangesToConsume = new HashMap<>();
				final List<SubscriptionPricePlanModel> commodityPricePlans = new ArrayList<>();
				changesCollector.getChanges().forEach(changedItem ->
				{
					final SubscriptionPricePlanModel pricePlan = modelService.get(PK.fromLong(changedItem.getItemPK()));
					commodityPricePlans.add(pricePlan);
					productChangesToConsume.put(pricePlan.getCode(), changedItem);
				});
				createUpilProduct(commodityPricePlans, productChangesToConsume);
			}
			else
			{
				LOG.info("No new products to sync");
			}
		}
		else
		{
			LOG.info("Empty catalog version / product spec type for sync");
		}
	}

	private StreamConfiguration getStreamConfig(final Collection<CatalogVersionModel> catalogVersionCollection,
			final List<TmaProductSpecTypeModel> productSpecTypeList, final String selector, final String streamId)
	{
		final Map<String, Object> params = ImmutableMap.of("cv", catalogVersionCollection, "ps",
				productSpecTypeList);
		return StreamConfiguration.buildFor(streamId)
				.withItemSelector(selector)
				.withParameters(params);
	}

	private void createUpilProduct(final List<SubscriptionPricePlanModel> commodityPricePlans,
			final Map<String, ItemChangeDTO> productChangesToConsume)
			throws UpilintegrationservicesException
	{
		final UpilResponseList upilResponseList = getUpilUtilitiesProductService().createUpilProduct(commodityPricePlans);
		if (!ObjectUtils.isEmpty(upilResponseList))
		{
			final List<String> successProductCodes = new ArrayList<>();
			upilResponseList.getUpilResponses().stream()
					.filter(upilResponse -> !ObjectUtils.isEmpty(upilResponse.getSuccessCode()))
					.map(UpilResponse::getSuccessCode).forEachOrdered(successProductCodes::add);

			final Map<String, String> errors = new HashMap<>();
			upilResponseList.getUpilResponses().stream().filter(upilResponse -> !ObjectUtils.isEmpty(upilResponse.getError()))
					.map(UpilResponse::getError).forEach(errors::putAll);

			if (CollectionUtils.isNotEmpty(successProductCodes))
			{
				final List<ItemChangeDTO> changesToConsume = new ArrayList<>();
				successProductCodes.forEach(productId ->
				{
					if (productChangesToConsume.containsKey(productId))
					{
						changesToConsume.add(productChangesToConsume.get(productId));
					}
				});
				updatePricePlansWithIsuCreationDate(successProductCodes, commodityPricePlans);
				getChangeDetectionService().consumeChanges(changesToConsume);
				commodityPricePlans.forEach(changedItem ->
				{
					if (changedItem.getProduct() instanceof TmaBundledProductOfferingModel)
					{
						getChangeDetectionService().changeVersionMarkerStatus(STREAM_ID_FOR_BPO, changedItem.getPk().getLongValue());
					}
					else
					{
						getChangeDetectionService().changeVersionMarkerStatus(STREAM_ID_FOR_SPO, changedItem.getPk().getLongValue());
					}
				});
				LOG.info("\n Successfully synchronized products with IS-U : " + String.join(", ", successProductCodes));

			}
			if (!ObjectUtils.isEmpty(errors))
			{
				LOG.error("\n Products synchronization with IS-U failed : \n ");
				errors.forEach((k, v) -> LOG.error((k + " : " + v + "\n")));
				throw new UpilintegrationservicesException("Products synchronization with IS-U failed");
			}
		}
	}

	private void updatePricePlansWithIsuCreationDate(final List<String> pricIds,
			final List<SubscriptionPricePlanModel> commodityPricePlans)
	{
		final List<SubscriptionPricePlanModel> pricesToUpdateIsuCreationDate = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(commodityPricePlans) && CollectionUtils.isNotEmpty(pricIds))
		{
			commodityPricePlans.forEach(pricePlan ->
			{
				if (pricIds.contains(pricePlan.getCode()))
				{
					pricePlan.setIsuCreationDate(new Date());
					pricesToUpdateIsuCreationDate.add(pricePlan);
				}
			});
		}
		modelService.saveAll(pricesToUpdateIsuCreationDate);
	}

	@Autowired
	public void setTypeService(final TypeService typeService)

	{
		this.typeService = typeService;
	}

	protected TypeService getTypeService()
	{
		return this.typeService;
	}

	@Autowired
	public void setChangeDetectionService(final DefaultUpilPricePlanChangeDetectionService changeDetectionService)
	{
		this.changeDetectionService = changeDetectionService;
	}

	protected DefaultUpilPricePlanChangeDetectionService getChangeDetectionService()
	{
		return this.changeDetectionService;
	}

	protected UpilUtilitiesProductService getUpilUtilitiesProductService()
	{
		return upilUtilitiesProductService;
	}

	@Autowired
	public void setUpilUtilitiesProductService(final UpilUtilitiesProductService upilUtilitiesProductService)
	{
		this.upilUtilitiesProductService = upilUtilitiesProductService;
	}
}
