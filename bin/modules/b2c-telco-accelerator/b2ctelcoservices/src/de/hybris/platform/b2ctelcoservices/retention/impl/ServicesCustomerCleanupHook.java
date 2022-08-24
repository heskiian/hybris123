/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.retention.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.audit.dao.WriteAuditRecordsDAO;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * This Hook removes customer related objects such as billingAccount, subscriptionBase and SubscriptionAccess
 *
 * @since 6.6
 */
public class ServicesCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(ServicesCustomerCleanupHook.class);

	private ModelService modelService;
	private WriteAuditRecordsDAO writeAuditRecordsDAO;
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;

	@Override
	public void cleanupRelatedObjects(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up subscription related to customerModel: {}", customerModel);
		}

		final List<TmaSubscriptionAccessModel> subscriptionAccesses = getTypeOwnerSubscriptionAccesses(customerModel);
		final Set<TmaBillingAccountModel> billingAccountsToRemove = new HashSet<>();

		subscriptionAccesses.forEach(subscriptionAccessModel ->
		{
			if (subscriptionAccessModel.getSubscriptionBase() != null)
			{
				final TmaSubscriptionBaseModel subscriptionBaseModel = subscriptionAccessModel.getSubscriptionBase();
				if (isSubscriptionBaseReadyToRemove(subscriptionBaseModel, subscriptionAccessModel))
				{
					if (subscriptionBaseModel.getBillingAccount() != null)
					{
						billingAccountsToRemove.add(subscriptionBaseModel.getBillingAccount());
					}
					removeServices(subscriptionBaseModel);
				}
			}
			removeSubscriptionAccess(subscriptionAccessModel);
		});
		removeBillingAccounts(billingAccountsToRemove);
	}

	protected boolean isSubscriptionBaseReadyToRemove(final TmaSubscriptionBaseModel subscriptionBaseModel,
			final TmaSubscriptionAccessModel subscriptionAccesstoCompare)
	{
		for (final TmaSubscriptionAccessModel subscriptionAccess : subscriptionBaseModel.getSubscriptionAccesses())
		{
			if (equalSubscriptionAccesses(subscriptionAccess, subscriptionAccesstoCompare))
			{
				return true;
			}
		}
		return false;
	}

	private List<TmaSubscriptionAccessModel> getTypeOwnerSubscriptionAccesses(final CustomerModel customerModel)
	{
		return tmaSubscriptionAccessService.getSubscriptionAccessesByType(customerModel.getUid(), TmaAccessType.OWNER);
	}

	private void removeSubscriptionAccess(final TmaSubscriptionAccessModel subscriptionAccess)
	{
		removeItem(TmaSubscriptionAccessModel._TYPECODE, subscriptionAccess.getPk());
	}

	private void removeBillingAccounts(final Set<TmaBillingAccountModel> billingAccountsToRemove)
	{
		billingAccountsToRemove.stream().filter(b -> CollectionUtils.isEmpty(b.getSubscriptionBases()))
				.forEach(billingAccount -> removeItem(TmaBillingAccountModel._TYPECODE, billingAccount.getPk()));
	}

	private void removeServices(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		subscriptionBaseModel.getSubscribedProducts().forEach(subscribedProduct ->
		{
			removeAverageServiceUsages(subscribedProduct);
			removeItem(TmaSubscribedProductModel._TYPECODE, subscribedProduct.getPk());
		});
	}

	protected void removeAverageServiceUsages(final TmaSubscribedProductModel subscribedProductModel)
	{
		if (CollectionUtils.isNotEmpty(subscribedProductModel.getAverageServiceUsages()))
		{
			subscribedProductModel.getAverageServiceUsages()
					.forEach(averageServiceUsage -> removeItem(TmaAverageServiceUsageModel._TYPECODE, averageServiceUsage.getPk()));
		}
	}

	protected void removeItem(final String typeCode, final PK pk)
	{
		getModelService().remove(pk);
		getWriteAuditRecordsDAO().removeAuditRecordsForType(typeCode, pk);
	}

	private boolean equalSubscriptionAccesses(final TmaSubscriptionAccessModel subscriptionAccess,
			final TmaSubscriptionAccessModel subscriptionAccessToCompare)
	{
		if (subscriptionAccess.getPrincipal() != null && subscriptionAccessToCompare.getPrincipal() != null && subscriptionAccess
				.getSubscriptionBase() != null && subscriptionAccessToCompare.getSubscriptionBase() != null)
		{
			return equalPrincipals(subscriptionAccess.getPrincipal(), subscriptionAccessToCompare.getPrincipal())
					&& equalSubscriptionBases(subscriptionAccess
					.getSubscriptionBase(), subscriptionAccessToCompare.getSubscriptionBase());
		}
		return false;
	}

	private boolean equalPrincipals(final PrincipalModel principal, final PrincipalModel principalToCompare)
	{
		if (principal.getUid() != null && principalToCompare.getUid() != null)
		{
			return principal.getUid().equals(principalToCompare.getUid());
		}
		return false;
	}

	private boolean equalSubscriptionBases(final TmaSubscriptionBaseModel subscriptionBase,
			final TmaSubscriptionBaseModel subscriptionBaseToCompare)
	{
		if (subscriptionBase.getBillingSystemId() != null && subscriptionBaseToCompare.getBillingSystemId() != null &&
				subscriptionBase.getSubscriberIdentity() != null && subscriptionBaseToCompare.getSubscriberIdentity() != null)
		{
			return subscriptionBase.getBillingSystemId().equals(subscriptionBaseToCompare.getBillingSystemId()) && subscriptionBase
					.getSubscriberIdentity().equals(
							subscriptionBaseToCompare.getSubscriberIdentity());
		}
		return false;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected WriteAuditRecordsDAO getWriteAuditRecordsDAO()
	{
		return writeAuditRecordsDAO;
	}

	@Required
	public void setWriteAuditRecordsDAO(final WriteAuditRecordsDAO writeAuditRecordsDAO)
	{
		this.writeAuditRecordsDAO = writeAuditRecordsDAO;
	}


	protected TmaSubscriptionAccessService getTmaSubscriptionAccessService()
	{
		return tmaSubscriptionAccessService;
	}

	@Required
	public void setTmaSubscriptionAccessService(final TmaSubscriptionAccessService tmaSubscriptionAccessService)
	{
		this.tmaSubscriptionAccessService = tmaSubscriptionAccessService;
	}
}
