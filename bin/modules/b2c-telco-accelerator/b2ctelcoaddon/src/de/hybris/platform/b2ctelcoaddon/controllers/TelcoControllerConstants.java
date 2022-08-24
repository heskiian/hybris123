/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoaddon.controllers;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.DynamicBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.acceleratorcms.model.components.PurchasedCategorySuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SubCategoryListComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.ProductReferencesAndClassificationsComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.ProductReferencesAndClassificationsForDevicesComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.ServiceUsageUpSellComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.SubscriptionCrossSellComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.SubscriptionSelectionComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.TmaMandatoryDashboardCmsComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.TmaOptionalDashboardCmsComponentModel;
import de.hybris.platform.b2ctelcoaddon.model.TmaProcessTypeListingComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;


/**
 * Controller constants.
 */
public interface TelcoControllerConstants //NOSONAR
{
	String ADDON_PREFIX = "addon:/b2ctelcoaddon/";

	/**
	 * Class with action name constants.
	 */
	interface Actions
	{
		interface Cms //NOSONAR
		{
			String PREFIX = "/view/";
			String SUFFIX = "Controller";

			/**
			 * CMS components that have specific handlers.
			 */
			String PURCHASED_CATEGORY_SUGGESTION_COMPONENT = PREFIX + PurchasedCategorySuggestionComponentModel._TYPECODE + SUFFIX;
			String CART_SUGGESTION_COMPONENT = PREFIX + CartSuggestionComponentModel._TYPECODE + SUFFIX;
			String PRODUCT_REFERENCES_COMPONENT = PREFIX + ProductReferencesComponentModel._TYPECODE + SUFFIX;
			String PRODUCT_CAROUSEL_COMPONENT = PREFIX + ProductCarouselComponentModel._TYPECODE + SUFFIX;
			String MINI_CART_COMPONENT = PREFIX + MiniCartComponentModel._TYPECODE + SUFFIX;
			String PRODUCT_FEATURE_COMPONENT = PREFIX + ProductFeatureComponentModel._TYPECODE + SUFFIX;
			String CATEGORY_FEATURE_COMPONENT = PREFIX + CategoryFeatureComponentModel._TYPECODE + SUFFIX;
			String NAVIGATION_BAR_COMPONENT = PREFIX + NavigationBarComponentModel._TYPECODE + SUFFIX;
			String CMS_LINK_COMPONENT = PREFIX + CMSLinkComponentModel._TYPECODE + SUFFIX;
			String DYNAMIC_BANNER_COMPONENT = PREFIX + DynamicBannerComponentModel._TYPECODE + SUFFIX;
			String SUBCATEGORY_LIST_COMPONENT = PREFIX + SubCategoryListComponentModel._TYPECODE + SUFFIX;
			String SIMPLE_RESPONSIVE_BANNER_COMPONENT = PREFIX + SimpleResponsiveBannerComponentModel._TYPECODE + SUFFIX;
			String PRODUCT_REFERENCES_AND_CLASSIFICATIONS_COMPONENT = PREFIX
					+ ProductReferencesAndClassificationsComponentModel._TYPECODE + SUFFIX;
			String TMA_PROCESSTYPE_LISTING_COMPONENT = PREFIX + TmaProcessTypeListingComponentModel._TYPECODE + SUFFIX;
			String PRODUCT_REFERENCES_AND_CLASSIFICATIONS_FOR_DEVICES_COMPONENT = PREFIX
					+ ProductReferencesAndClassificationsForDevicesComponentModel._TYPECODE + SUFFIX;
			String SUBSCRIPTION_CROSSSELL_COMPONENT = PREFIX + SubscriptionCrossSellComponentModel._TYPECODE + SUFFIX;
			String SUBSCRIPTION_SELECTION_COMPONENT = PREFIX + SubscriptionSelectionComponentModel._TYPECODE + SUFFIX;
			String SERVICE_USAGE_UPSELL_COMPONENT = PREFIX + ServiceUsageUpSellComponentModel._TYPECODE + SUFFIX;
			String TMA_MANDATORY_DASHBOARD_CMS_COMPONENT = PREFIX + TmaMandatoryDashboardCmsComponentModel._TYPECODE + SUFFIX;
			String TMA_OPTIONAL_DASHBOARD_CMS_COMPONENT = PREFIX + TmaOptionalDashboardCmsComponentModel._TYPECODE + SUFFIX;
		}
	}

	interface Views
	{

		interface Pages
		{
			interface Account //NOSONAR
			{
				String ACCOUNT_LOGIN_PAGE = "pages/account/accountLoginPage";
				String ACCOUNT_PAYMENT_METHOD_SUBSCRIPTIONS_PAGE = ADDON_PREFIX
						+ "pages/account/accountPaymentMethodSubscriptionsPage";
			}

			interface Checkout //NOSONAR
			{
				String CHECKOUT_LOGIN_PAGE = "pages/checkout/checkoutLoginPage";
			}

			interface GuidedSelling //NOSONAR
			{
				String EDIT_COMPONENT_SOLR_STYLE_PAGE = ADDON_PREFIX + "pages/guidedselling/editComponentSolrStylePage";
				String EDIT_COMPONENT_ACCORDEON_STYLE_PAGE = ADDON_PREFIX + "pages/guidedselling/editComponentAccordeonStylePage";
			}

			interface Product //NOSONAR
			{
				String SERVICE_PLAN_LAYOUT_PAGE = ADDON_PREFIX + "pages/product/servicePlanLayoutPage";
				String WRITE_REVIEW = "pages/product/writeReview";
			}

			interface Error //NOSONAR
			{
				String ERROR_NOT_FOUND_PAGE = "pages/error/errorNotFoundPage";
			}

			interface Cart //NOSONAR
			{
				String CART_PAGE = "pages/cart/cartPage";
			}

			interface Misc //NOSONAR
			{
				String MISC_BPO_LISTING = ADDON_PREFIX + "fragments/product/bpoListing";
			}
		}

		interface Fragments
		{
			interface Cart //NOSONAR
			{
				String ADD_TO_CART_POPUP = ADDON_PREFIX + "fragments/cart/addToCartPopup";
				String MINI_CART_PANEL = "fragments/cart/miniCartPanel";
				String CART_POPUP = ADDON_PREFIX + "fragments/cart/cartPopup";
			}

			interface Checkout //NOSONAR
			{
				String BILLING_ADDRESS_FORM = "addon:/b2ctelcoaddon/fragments/checkout/billingAddressForm";
			}

			interface Product //NOSONAR
			{
				String QUICK_VIEW_POPUP = ADDON_PREFIX + "fragments/product/quickViewPopup";
				String ZOOM_IMAGES_POPUP = "fragments/product/zoomImagesPopup";
				String REVIEWS_TAB = "fragments/product/reviewsTab";
				String OFFER_LISTING_PANEL = ADDON_PREFIX + "fragments/product/offerListingPanel";
			}
		}
	}
}
