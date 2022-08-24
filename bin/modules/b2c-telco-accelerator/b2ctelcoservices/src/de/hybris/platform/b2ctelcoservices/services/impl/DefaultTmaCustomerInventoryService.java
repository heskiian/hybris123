/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductCategoryModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductLineModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingAccountService;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Default implementation for {@link TmaCustomerInventoryService}.
 *
 * @since 1810
 */
public class DefaultTmaCustomerInventoryService implements TmaCustomerInventoryService
{
	private TmaSubscriptionBaseService subscriptionBaseService;
	private TmaBillingAccountService billingAccountService;
	private EnumerationService enumerationService;
	private TmaEligibilityContextService tmaEligibilityContextService;
	private UserService userService;
	private ModelService modelService;
	private TmaSubscriptionTermService subscriptionTermService;

	@Override
	public TmaSubscriptionBaseModel createNewSubscriptionHierarchy(final String customerId, final TmaAccessType accessType)
	{
		//generate new billing account
		final TmaBillingAccountModel billingAccountModel = getBillingAccountService().generateBillingAccount();
		//generate subscription base and access type
		return getSubscriptionBaseService().generateSubscriptionBase(customerId, billingAccountModel.getBillingAccountId(),
				accessType);
	}

	@Override
	public Map<String, List<TmaSubscriptionBaseModel>> groupSubscriptionsByBillingArrangementAndBpo(
			final List<TmaSubscriptionBaseModel> subscriptionBases)
	{
		return getSubscriptionBaseService().groupSubscriptionsByBillingArrangementAndBpo(subscriptionBases);
	}

	@Override
	public Set<TmaProcessType> retrieveProcesses()
	{
		final Set<TmaEligibilityContextModel> customerEligibilityContext = getTmaEligibilityContextService()
				.extractEligibilityContext();
		final Set<TmaProcessType> eligibleProcessTypes = new HashSet<>();
		if (CollectionUtils.isEmpty(customerEligibilityContext))
		{
			return eligibleProcessTypes;
		}

		final Optional<TmaEligibilityContextModel> eligibilityContextForAllProcesses = customerEligibilityContext.stream()
				.filter(eligibilityContext ->
				{
					if (CollectionUtils.isEmpty(eligibilityContext.getProcessesCodes()))
					{
						return true;
					}
					eligibleProcessTypes.addAll(getProcessTypesFromCodes(eligibilityContext.getProcessesCodes()));
					return false;
				}).findFirst();

		if (eligibilityContextForAllProcesses.isPresent())
		{
			return getAllProcessTypes();
		}

		return eligibleProcessTypes;
	}

	@Override
	public Set<TmaProcessType> retrieveProcessesByCustomerId(String customerId)
	{
		final Set<TmaEligibilityContextModel> customerEligibilityContext;
		final Set<TmaProcessType> eligibleProcessTypes = new HashSet<>();
		final CustomerModel user = (CustomerModel) getUserService().getUserForUID(customerId);

		getTmaEligibilityContextService().updateEligibilityContextsByCustomer(true, user);

		if (StringUtils.isBlank(customerId))
		{
			customerEligibilityContext = getTmaEligibilityContextService().extractEligibilityContext();
		}
		else
		{
			customerEligibilityContext = getTmaEligibilityContextService().extractEligibilityContextByCustomer(customerId);
		}

		if (CollectionUtils.isEmpty(customerEligibilityContext))
		{
			return eligibleProcessTypes;
		}

		final Optional<TmaEligibilityContextModel> eligibilityContextForAllProcesses = customerEligibilityContext.stream()
				.filter(eligibilityContext ->
				{
					if (CollectionUtils.isEmpty(eligibilityContext.getProcessesCodes()))
					{
						return true;
					}
					eligibleProcessTypes.addAll(getProcessTypesFromCodes(eligibilityContext.getProcessesCodes()));
					return false;
				}).findFirst();

		if (eligibilityContextForAllProcesses.isPresent())
		{
			return getAllProcessTypes();
		}

		return eligibleProcessTypes;
	}

	@Override
	public Set<TmaSubscriptionBaseModel> retrieveSubscriptionsForProcess(final TmaProcessType processType)
	{
		final Set<TmaEligibilityContextModel> customerEligibilityContext = getTmaEligibilityContextService()
				.extractEligibilityContext();
		final Set<TmaSubscriptionBaseModel> subscriptionBases = new HashSet<>();
		customerEligibilityContext.stream()
				.forEach(eligibilityContext ->
				{
					if (CollectionUtils.isEmpty(eligibilityContext.getProcessesCodes())
							|| eligibilityContext.getProcessesCodes().contains(processType.getCode()))
					{
						subscriptionBases.add(getSubscriptionBaseService()
								.getSubscriptionBase(eligibilityContext.getSubscriberId(),
										eligibilityContext.getBillingSystemId()));
					}
				});
		return subscriptionBases;
	}

	@Override
	public Set<TmaSubscriptionBaseModel> retrieveSubscriptionsForProcess(final TmaProcessType processType,
			final String userId)
	{
		final Set<TmaEligibilityContextModel> customerEligibilityContext =
				getTmaEligibilityContextService().extractEligibilityContextByCustomer(userId);
		final Set<TmaSubscriptionBaseModel> subscriptionBases = new HashSet<>();
		customerEligibilityContext.forEach((TmaEligibilityContextModel eligibilityContext) ->
		{
			if (CollectionUtils.isEmpty(eligibilityContext.getProcessesCodes())
					|| eligibilityContext.getProcessesCodes().contains(processType.getCode()))
			{
				subscriptionBases.add(getSubscriptionBaseService()
						.getSubscriptionBase(eligibilityContext.getSubscriberId(),
								eligibilityContext.getBillingSystemId()));
			}
		});
		return subscriptionBases;
	}

	@Override
	public List<TmaSubscribedProductModel> getAllSubscribedProducts(final CustomerModel customerModel)
	{
		final List<TmaSubscribedProductModel> subscribedProducts = new ArrayList<>();
		for (TmaSubscriptionBaseModel subscriptionBase : getSubscriptionBaseService()
				.getSubscriptionBases(customerModel.getUid()))
		{
			subscribedProducts.addAll(subscriptionBase.getSubscribedProducts());
		}

		return subscribedProducts;
	}

	@Override
	public Optional<TmaSubscriptionBaseModel> retrieveEligibleSubscriptions(final String subscriberIdentity,
			final String billingSystemId, final TmaProcessType processType)
	{
		Set<TmaSubscriptionBaseModel> subscriptionsForProcess = retrieveSubscriptionsForProcess(processType);
		return subscriptionsForProcess.stream().filter(
				eligibleSubscription -> eligibleSubscription.getSubscriberIdentity().equals(subscriberIdentity)
						&& eligibleSubscription.getBillingSystemId().equals(billingSystemId)).findFirst();
	}

	public Set<TmaProcessType> getAllProcessTypes()
	{
		return new HashSet<>(getEnumerationService().getEnumerationValues(TmaProcessType.class));
	}

	@Override
	public TmaCartSubscriptionInfoModel createNewCartSubscriptionInfo(String subscriberIdentity, String billingId,
			String subscriptionTermId, String processType)
	{
		TmaCartSubscriptionInfoModel subscriptionInfoModel = null;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(subscriptionTermId))
		{
			subscriptionInfoModel = getModelService().create(TmaCartSubscriptionInfoModel.class);
			final SubscriptionTermModel subscriptionTerm = getSubscriptionTermService().getSubscriptionTerm(
					subscriptionTermId);
			if (subscriptionTerm != null)
			{
				subscriptionInfoModel.setSubscriptionTerm(subscriptionTerm);
			}
		}

		if (org.apache.commons.lang.StringUtils.isEmpty(subscriberIdentity) || org.apache.commons.lang.StringUtils
				.isEmpty(billingId))
		{
			return subscriptionInfoModel;
		}

		final Optional<TmaSubscriptionBaseModel> validSubscription = retrieveEligibleSubscriptions(subscriberIdentity, billingId,
				getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE, processType));
		if (validSubscription.isPresent())
		{
			if (subscriptionInfoModel == null)
			{
				subscriptionInfoModel = getModelService().create(TmaCartSubscriptionInfoModel.class);
			}
			subscriptionInfoModel.setSubscriberIdentity(subscriberIdentity);
			subscriptionInfoModel.setBillingSystemId(billingId);

			final Set<String> mainTariffProductCodes = getSubscriptionBaseService()
					.getMainTariffSubscribedProductIdsForSubscriptionBases(Collections.singletonList(validSubscription.get()));
			if (mainTariffProductCodes != null && !mainTariffProductCodes.isEmpty())
			{
				subscriptionInfoModel.setSubscribedProductId(mainTariffProductCodes.iterator().next());
			}
		}

		return subscriptionInfoModel;

	}

	@Override
	public Optional<TmaSubscribedProductModel> getSubscribedProductById(final String subscribedProductId,
			final CustomerModel customer)
	{
		final List<TmaSubscribedProductModel> subscribedProducts = getAllSubscribedProducts(customer);
		return Optional.ofNullable(
				subscribedProducts.stream().filter(product -> product.getId().equals(subscribedProductId)).findFirst().orElse(null));
	}

	@Override
	public boolean canReplaceSubscribedProductWithOffering(final TmaSubscribedProductModel subscribedProduct,
			final TmaProductOfferingModel productOffering)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("subscribedProduct", subscribedProduct);
		ServicesUtil.validateParameterNotNullStandardMessage("productOffering", productOffering);

		final Set<TmaProductSpecTypeModel> poProductSpecTypes = productOffering.getProductSpecification() != null ?
				productOffering.getProductSpecification().getProductSpecificationTypes() : null;
		final Set<TmaProductSpecTypeModel> subscribedProductSpecTypes = subscribedProduct.getProductSpecification() != null ?
				subscribedProduct.getProductSpecification().getProductSpecificationTypes() : null;
		if (CollectionUtils.isEmpty(poProductSpecTypes) || CollectionUtils.isEmpty(subscribedProductSpecTypes))
		{
			return true;
		}
		return checkMatchingProductTypes(poProductSpecTypes, subscribedProductSpecTypes, TmaProductLineModel.class)
				&& checkMatchingProductTypes(poProductSpecTypes, subscribedProductSpecTypes, TmaProductCategoryModel.class);
	}

	/**
	 * This method checks if the instances of subtype of {@link TmaProductSpecTypeModel} given by the
	 * productSpecificationTypeClass parameter match between the product offering and the subscribed product.
	 *
	 * @param poProductSpecTypes
	 * 		The product specification types of the product offering
	 * @param subscribedProductSpecTypes
	 * 		The product specification types of the subscribed product.
	 * @param productSpecificationTypeClass
	 * 		The subtype of the productSpecificationType to be checked.
	 * @return true if the specification types match; false otherwise.
	 */
	protected boolean checkMatchingProductTypes(final Set<TmaProductSpecTypeModel> poProductSpecTypes,
			final Set<TmaProductSpecTypeModel> subscribedProductSpecTypes, Class productSpecificationTypeClass)
	{
		final Set<TmaProductSpecTypeModel> poProductSpecType =
				poProductSpecTypes.stream().filter(productSpecificationTypeClass::isInstance)
						.collect(Collectors.toSet());
		final Set<TmaProductSpecTypeModel> subscribedProductSpecType =
				subscribedProductSpecTypes.stream().filter(productSpecificationTypeClass::isInstance).collect(
						Collectors.toSet());
		return poProductSpecType.containsAll(subscribedProductSpecType);
	}

	protected List<TmaProcessType> getProcessTypesFromCodes(final List<String> processTypeCodes)
	{
		return processTypeCodes.stream().map(TmaProcessType::valueOf).collect(Collectors.toList());
	}

	protected TmaSubscriptionBaseService getSubscriptionBaseService()
	{
		return subscriptionBaseService;
	}

	public void setSubscriptionBaseService(final TmaSubscriptionBaseService subscriptionBaseService)
	{
		this.subscriptionBaseService = subscriptionBaseService;
	}

	protected TmaBillingAccountService getBillingAccountService()
	{
		return billingAccountService;
	}

	public void setBillingAccountService(final TmaBillingAccountService billingAccountService)
	{
		this.billingAccountService = billingAccountService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected TmaEligibilityContextService getTmaEligibilityContextService()
	{
		return tmaEligibilityContextService;
	}

	@Required
	public void setTmaEligibilityContextService(final TmaEligibilityContextService tmaEligibilityContextService)
	{
		this.tmaEligibilityContextService = tmaEligibilityContextService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(UserService userService)
	{
		this.userService = userService;
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

	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}

	@Required
	public void setSubscriptionTermService(final TmaSubscriptionTermService subscriptionTermService)
	{
		this.subscriptionTermService = subscriptionTermService;
	}
}
