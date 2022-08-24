/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */

package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.Date;
import java.util.List;


/**
 * Builder used to create instances of {@link TmaPolicyContext}
 *
 * @since 1907
 */
public class TmaPolicyContextBuilder
{
	private boolean subscribed;
	private TmaProductOfferingGroupModel group;
	private Integer quantity;
	private Date subscriptionEndDate;
	private TmaProcessType processType;
	private AbstractOrderEntryModel orderEntry;
	private EntryGroup entryGroup;
	private List<TmaProductOfferingModel> productOfferings;
	private CatalogVersionModel catalogVersion;
	private TmaSubscribedProductModel subscribedProduct;

	public static TmaPolicyContextBuilder newPolicyContextBuilder()
	{
		return new TmaPolicyContextBuilder();
	}

	public TmaPolicyContextBuilder withSubscribed(final boolean subscribed)
	{
		this.subscribed = subscribed;
		return this;
	}

	public TmaPolicyContextBuilder withGroup(final TmaProductOfferingGroupModel group)
	{
		this.group = group;
		return this;
	}

	public TmaPolicyContextBuilder withQuantity(final Integer quantity)
	{
		this.quantity = quantity;
		return this;
	}

	public TmaPolicyContextBuilder withSubscriptionEndDate(final Date subscriptionEndDate)
	{
		this.subscriptionEndDate = subscriptionEndDate;
		return this;
	}

	public TmaPolicyContextBuilder withProcessType(final TmaProcessType processType)
	{
		this.processType = processType;
		return this;
	}

	public TmaPolicyContextBuilder withOrderEntry(final AbstractOrderEntryModel orderEntry)
	{
		this.orderEntry = orderEntry;
		return this;
	}

	public TmaPolicyContextBuilder withEntryGroup(final EntryGroup entryGroup)
	{
		this.entryGroup = entryGroup;
		return this;
	}

	public TmaPolicyContextBuilder withProductOfferings(final List<TmaProductOfferingModel> productOfferings)
	{
		this.productOfferings = productOfferings;
		return this;
	}

	public TmaPolicyContextBuilder withCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		this.catalogVersion = catalogVersion;
		return this;
	}

	public TmaPolicyContextBuilder withSubscribedProduct(final TmaSubscribedProductModel subscribedProduct)
	{
		this.subscribedProduct = subscribedProduct;
		return this;
	}

	public TmaPolicyContext build()
	{
		final TmaPolicyContext tmaPolicyContext = new TmaPolicyContext();
		tmaPolicyContext.setSubscribed(subscribed);
		tmaPolicyContext.setGroup(group);
		tmaPolicyContext.setQuantity(quantity);
		tmaPolicyContext.setSubscriptionEndDate(subscriptionEndDate);
		tmaPolicyContext.setProcessType(processType);
		tmaPolicyContext.setOrderEntry(orderEntry);
		tmaPolicyContext.setEntryGroup(entryGroup);
		tmaPolicyContext.setProductOfferings(productOfferings);
		tmaPolicyContext.setPolicyCatalogVersion(catalogVersion);
		tmaPolicyContext.setSubscribedProduct(subscribedProduct);
		return tmaPolicyContext;
	}
}
