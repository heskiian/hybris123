/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilbackoffice.renders;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.model.IsuProductSyncCronJobModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaPanelRenderer;


/**
 * Renderer for displaying the action to synchronize the products with billing system.
 *
 * @since 1911
 */
public class UpilProductSyncEditorAreaRenderer extends AbstractEditorAreaPanelRenderer<Object>
{
	private static final String SYNC_PRODUCT_TO_UPIL_LABEL = "backoffice.section.prices.upilSync";
	private static final String SYNC_PRODUCT_TO_UPIL_BTN_LABEL = "sync.upil.product.btn.label";
	private static final String SYNC_PRODUCT_TO_UPIL_SUCCESS_MSG = "sync.upil.product.success.message";
	private static final String SYNC_PRODUCT_TO_UPIL_FAILURE_MSG = "sync.upil.product.failure.message";
	private static final String NO_NEW_PRODUCT_TO_SYNC_UPIL_MSG = "sync.upil.product.no.product.message";
	private static final String SYNC_BUTTON_CLASS_NAME = "billing-system-product-sync-btn";

	private static final String SAVE_BUTTON_LISTENER = "SAVE_BUTTON_LISTENER_ID";
	private static final String REFRESH_BUTTON_LISTENER = "REFRESH_BUTTON_LISTENER_ID";
	private static final String ID_FOR_DELTADETECTION_CRONJOB = "utilitiesProductsSyncCronJob";

	private ModelService modelService;
	private CronJobService cronJobService;

	@Override
	public void render(final Component parent, final AbstractPanel configuration, final Object data,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		if (data instanceof TmaProductOfferingModel)
		{
			EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), REFRESH_BUTTON_LISTENER,
					event -> widgetInstanceManager.getWidgetslot().updateView(), false);
			EditorAreaRendererUtils.setAfterSaveListener(widgetInstanceManager.getModel(), SAVE_BUTTON_LISTENER,
					event -> widgetInstanceManager.getWidgetslot().updateView(), false);

			final IsuProductSyncCronJobModel utilitiesProductsSyncCronJob = (IsuProductSyncCronJobModel) getCronjobService()
					.getCronJob(ID_FOR_DELTADETECTION_CRONJOB);

			final TmaProductOfferingModel selectedProduct = (TmaProductOfferingModel) data;
			final Button synchButton = new Button(Labels.getLabel(SYNC_PRODUCT_TO_UPIL_BTN_LABEL));
			synchButton.setSclass(SYNC_BUTTON_CLASS_NAME);
			synchButton.setTooltiptext(Labels.getLabel(SYNC_PRODUCT_TO_UPIL_LABEL));
			synchButton.setDisabled(isSyncEnable(selectedProduct, utilitiesProductsSyncCronJob.getAppliedCatalogVersions(),
					utilitiesProductsSyncCronJob.getAppliedProductTypes()));
			parent.appendChild(synchButton);
			synchButton.addEventListener(Events.ON_CLICK, new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception
				{
					final List<SubscriptionPricePlanModel> subscriptionPricePlanList = getNewCommodityPricePlanForProduct(
							selectedProduct.getOwnEurope1Prices(), utilitiesProductsSyncCronJob.getAppliedProductTypes());
					if (!CollectionUtils.isEmpty(subscriptionPricePlanList))
					{
						utilitiesProductsSyncCronJob.setAppliedPricePlans(subscriptionPricePlanList);
						getModelService().save(utilitiesProductsSyncCronJob);
						getCronjobService().performCronJob(utilitiesProductsSyncCronJob, true);
						modelService.refresh(selectedProduct);
						if (CronJobResult.SUCCESS.getCode().equalsIgnoreCase(utilitiesProductsSyncCronJob.getResult().getCode()))
						{
							Messagebox.show(Labels.getLabel(SYNC_PRODUCT_TO_UPIL_SUCCESS_MSG));
						}
						else
						{
							Messagebox.show(Labels.getLabel(SYNC_PRODUCT_TO_UPIL_FAILURE_MSG));
						}
					}
					else
					{
						Messagebox.show(Labels.getLabel(NO_NEW_PRODUCT_TO_SYNC_UPIL_MSG));
					}
				}
			});
		}
	}

	private boolean isSyncEnable(final TmaProductOfferingModel selectedProduct,
			final Collection<CatalogVersionModel> appliedCatalogVersions,
			final List<TmaProductSpecTypeModel> appliedProductTypes)
	{
		boolean disableSyncButton = true;
		if (!ObjectUtils.isEmpty(selectedProduct) && !CollectionUtils.isEmpty(appliedCatalogVersions)
				&& CollectionUtils.contains(appliedCatalogVersions.iterator(),
						selectedProduct.getCatalogVersion())
				&& isCommodityPricePlanAvailable(appliedProductTypes, selectedProduct))
		{
			disableSyncButton = false;
		}
		return disableSyncButton;
	}

	private boolean isCommodityPricePlanAvailable(final List<TmaProductSpecTypeModel> appliedProductTypes,
			final TmaProductOfferingModel selectedProduct)
	{
		final Collection<PriceRowModel> ownEurope1Prices = selectedProduct.getOwnEurope1Prices();
		if (!CollectionUtils.isEmpty(ownEurope1Prices) && !CollectionUtils.isEmpty(appliedProductTypes) && !CollectionUtils.isEmpty(
				getSubscriptionPricePlanForProduct(appliedProductTypes, ownEurope1Prices)))
		{
			return true;
		}
		return false;
	}

	private List<SubscriptionPricePlanModel> getSubscriptionPricePlanForProduct(
			final List<TmaProductSpecTypeModel> productSpecTypes, final Collection<PriceRowModel> priceRowCollection)
	{
		final List<SubscriptionPricePlanModel> subscriptionPricePlanList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(productSpecTypes)
				&& !CollectionUtils.isEmpty(priceRowCollection))
		{
			priceRowCollection.forEach(priceRow ->
			{
				if (priceRow instanceof SubscriptionPricePlanModel
						&& productSpecTypes.contains(((SubscriptionPricePlanModel) priceRow).getProductSpecType()))
				{
					subscriptionPricePlanList.add((SubscriptionPricePlanModel) priceRow);
				}
			});
		}
		return subscriptionPricePlanList;
	}

	private List<SubscriptionPricePlanModel> getNewCommodityPricePlanForProduct(
			final Collection<PriceRowModel> ownEurope1Prices, final List<TmaProductSpecTypeModel> productSpecTypes)
	{
		final List<SubscriptionPricePlanModel> subscriptionPricePlanList = new ArrayList<>();
		final List<SubscriptionPricePlanModel> subscriptionPricePlanForProduct = getSubscriptionPricePlanForProduct(
				productSpecTypes,
				ownEurope1Prices);
		if (!CollectionUtils.isEmpty(subscriptionPricePlanForProduct))
		{
			subscriptionPricePlanForProduct.forEach(subscriptionPricePlan ->
			{
				if (ObjectUtils.isEmpty(subscriptionPricePlan.getIsuCreationDate()))
				{
					subscriptionPricePlanList.add(subscriptionPricePlan);
				}
			});
		}
		return subscriptionPricePlanList;
	}

	protected CronJobService getCronjobService()
	{
		return this.cronJobService;
	}

	@Autowired
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	@Autowired
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
