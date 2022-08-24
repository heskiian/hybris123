/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.user.impl;

import de.hybris.platform.b2ctelcofacades.converters.populator.TmaAddressReversePopulator;
import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionSelectionData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaIdentificationService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;


/**
 * Tma implementation of customer facade. It implements telco specific operations around customers.
 *
 * @since 6.7
 */
public class TmaDefaultCustomerFacade extends DefaultCustomerFacade implements TmaCustomerFacade
{
	private static final String SUBSCRIPTIONS_SEPARATOR = ", ";
	private static final String SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR = "__";
	private TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;
	private TmaEligibilityContextService tmaEligibilityContextService;
	private TmaCustomerInventoryService tmaCustomerInventoryService;
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	private Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> tmaSubscriptionBaseConverter;
	private Converter<TmaSubscriptionBaseModel, TmaSubscriptionSelectionData> tmaSubscriptionSelectionConverter;
	private EnumerationService enumerationService;
	private TmaIdentificationService tmaIdentificationService;
	/**
	 * Converts from {@link UserModel} to {@link PrincipalData}
	 */
	private Converter<UserModel, PrincipalData> principalConverter;

	public TmaDefaultCustomerFacade(
			final EnumerationService enumerationService, final TmaIdentificationService tmaIdentificationService)
	{
		this.enumerationService = enumerationService;
		this.tmaIdentificationService = tmaIdentificationService;
	}

	@Override
	public List<TmaSubscriptionBaseData> getValidSubscriptionDataForCustomer(final List<TmaSubscriptionBaseData> subscriptionBases)
	{
		final List<TmaSubscriptionBaseData> result = new ArrayList<>();
		if (getUserFacade().isAnonymousUser())
		{
			return result;
		}

		final CustomerData currentCustomer = getCurrentCustomer();
		final List<TmaSubscriptionAccessData> currentCustomerSubscriptionAccesses = getTmaSubscriptionAccessFacade()
				.findSubscriptionAccessesByPrincipal(currentCustomer.getUid());

		if (currentCustomerSubscriptionAccesses == null || currentCustomerSubscriptionAccesses.isEmpty())
		{
			return result;
		}

		for (final TmaSubscriptionBaseData subscriptionBase : subscriptionBases)
		{
			for (final TmaSubscriptionAccessData subscriptionAccess : currentCustomerSubscriptionAccesses)
			{
				if (StringUtils.equals(subscriptionBase.getSubscriberIdentity(), subscriptionAccess.getSubscriberIdentity())
						&& StringUtils.equals(subscriptionBase.getBillingSystemId(), subscriptionAccess.getBillingSystemId()))
				{
					result.add(subscriptionBase);
					break;
				}
			}
		}

		return result;
	}

	@Override
	public Optional<TmaSubscriptionBaseData> getEligibleSubscriptionData(final String subscriberIdentity,
			final String billingSystemId, final TmaProcessType processType)
	{
		final Optional<TmaSubscriptionBaseModel> tmaSubscriptionBaseModel = getTmaCustomerInventoryService()
				.retrieveEligibleSubscriptions(subscriberIdentity, billingSystemId, processType);

		return tmaSubscriptionBaseModel
				.map(subscriptionBaseModel -> getTmaSubscriptionBaseConverter().convert(subscriptionBaseModel));
	}

	@Override
	public List<TmaSubscriptionBaseData> getSubscriptionBasesFromString(final String subscriberIdentities)
	{
		final List<TmaSubscriptionBaseData> result = new ArrayList<>();
		final String[] subscriptions = StringUtils.splitByWholeSeparator(subscriberIdentities, SUBSCRIPTIONS_SEPARATOR);
		if (ArrayUtils.isEmpty(subscriptions))
		{
			return result;
		}

		for (final String subscription : subscriptions)
		{
			final String[] subscriptionDetails = StringUtils.splitByWholeSeparator(subscription,
					SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR);
			if (subscriptionDetails == null || subscriptionDetails.length <= 1)
			{
				continue;
			}

			addSubscriptionBaseToResult(result, subscriptionDetails);
		}

		return result;
	}

	@Override
	public Set<TmaProcessType> retrieveEligibleProcessTypes()
	{
		return getTmaCustomerInventoryService().retrieveProcesses();
	}

	@Override
	public Set<TmaProcessType> retrieveEligibleProcessTypesByCustomerId(final String customerId)
	{
		return getTmaCustomerInventoryService().retrieveProcessesByCustomerId(customerId);
	}

	@Override
	public Set<TmaSubscriptionBaseData> retrieveEligibleSubscriptions(final TmaProcessType processType)
	{
		final Set<TmaSubscriptionBaseModel> subscriptions = getTmaCustomerInventoryService()
				.retrieveSubscriptionsForProcess(processType);
		return new HashSet<>(getTmaSubscriptionBaseConverter().convertAll(subscriptions));
	}

	@Override
	public Set<TmaSubscriptionBaseData> retrieveEligibleSubscriptions(final TmaProcessType processType, final String userId)
	{
		final Set<TmaSubscriptionBaseModel> subscriptions = getTmaCustomerInventoryService()
				.retrieveSubscriptionsForProcess(processType, userId);
		return new HashSet<>(getTmaSubscriptionBaseConverter().convertAll(subscriptions));
	}

	@Override
	public Map<String, List<TmaSubscriptionSelectionData>> determineEligibleSubscriptionSelections(
			final TmaProcessType processType)
	{
		final Set<TmaSubscriptionBaseModel> subscriptions = getTmaCustomerInventoryService()
				.retrieveSubscriptionsForProcess(processType);
		final Map<String, List<TmaSubscriptionSelectionData>> groupedSubscriptionSelections = new HashMap<>();
		final Map<String, List<TmaSubscriptionBaseModel>> groupedSubscriptions = getTmaCustomerInventoryService()
				.groupSubscriptionsByBillingArrangementAndBpo(new ArrayList<>(subscriptions));
		for (final String billingArrangementId : groupedSubscriptions.keySet())
		{
			final List<TmaSubscriptionBaseModel> subscriptionsForBillingArrangement = groupedSubscriptions.get(billingArrangementId);

			final List<TmaSubscriptionSelectionData> subscriptionSelections = getTmaSubscriptionSelectionConverter()
					.convertAll(subscriptionsForBillingArrangement);

			groupedSubscriptionSelections.put(billingArrangementId, subscriptionSelections);
		}
		return groupedSubscriptionSelections;
	}

	@Override
	public Set<String> getMainTariffProductCodesForSubscriptionBases(final List<TmaSubscriptionBaseData> subscriptionDatas)
	{
		final Set<String> result = new TreeSet<>();
		if (CollectionUtils.isEmpty(subscriptionDatas))
		{
			return result;
		}

		subscriptionDatas.forEach(subscriptionBase ->
		{
			final TmaSubscriptionBaseModel subscriptionBaseModel = getTmaSubscriptionBaseService()
					.getSubscriptionBase(subscriptionBase.getSubscriberIdentity(), subscriptionBase.getBillingSystemId());
			final String mainTariffProductCode = getTmaSubscriptionBaseService()
					.getMainTariffProductCodeForSubscriptionBase(subscriptionBaseModel);

			if (StringUtils.isNotBlank(mainTariffProductCode))
			{
				result.add(mainTariffProductCode);
			}
		});

		return result;
	}

	@Override
	public List<CCPaymentInfoData> getCcPaymentInfosForUser(final String userCode)
	{
		final UserModel userModel = getUserService().getUserForUID(userCode);
		if (!(userModel instanceof CustomerModel))
		{
			return Collections.emptyList();
		}

		final CustomerModel customerModel = (CustomerModel) userModel;
		final List<CCPaymentInfoData> paymentInfoDataList = new ArrayList<>();
		final List<CreditCardPaymentInfoModel> paymentInfoModelList = getCustomerAccountService()
				.getCreditCardPaymentInfos(customerModel, false);

		for (final CreditCardPaymentInfoModel paymentInfoModel : paymentInfoModelList)
		{
			final CCPaymentInfoData paymentInfoData = getCreditCardPaymentInfoConverter().convert(paymentInfoModel);
			paymentInfoData.setUser(getPrincipalConverter().convert(customerModel));
			paymentInfoDataList.add(paymentInfoData);
		}

		return paymentInfoDataList;
	}

	@Override
	public void updateEligibilityContexts(final boolean forceRefresh)
	{
		getTmaEligibilityContextService().updateEligibilityContexts(forceRefresh);
	}

	@Override
	public void loginSuccess()
	{
		getTmaEligibilityContextService().updateEligibilityContexts(true);
		super.loginSuccess();
	}

	@Override
	public boolean hasCurrentAuthenticationRoleProvided(final String role)
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
		{
			return false;
		}
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (role.equals(ga.getAuthority()))
			{
				return true;
			}
		}
		return false;
	}

	private void addSubscriptionBaseToResult(final List<TmaSubscriptionBaseData> result, final String[] subscriberIdentityStrings)
	{
		final TmaSubscriptionBaseData subscriptionBaseData = new TmaSubscriptionBaseData();
		subscriptionBaseData.setSubscriberIdentity(subscriberIdentityStrings[0]);
		subscriptionBaseData.setBillingSystemId(subscriberIdentityStrings[1]);
		result.add(subscriptionBaseData);
	}

	public Set<TmaProcessType> getAllProcessTypes()
	{
		return getTmaCustomerInventoryService().getAllProcessTypes();
	}

	public void updateEligibilityContextsByCustomer(final String userId)
	{
		final UserModel userModel = getUserService().getUserForUID(userId);

		if (!(userModel instanceof CustomerModel))
		{
			return;
		}

		getTmaEligibilityContextService().updateEligibilityContextsByCustomer(true, (CustomerModel) userModel);
	}

	@Override
	protected void setCommonPropertiesForRegister(final RegisterData registerData, final CustomerModel customerModel)
	{
		final List<TmaIdentificationData> identificationList = registerData.getIdentifications();
		if (CollectionUtils.isNotEmpty(identificationList))
		{
			setIdentificationDetails(identificationList, customerModel);
		}
		super.setCommonPropertiesForRegister(registerData, customerModel);
	}

	private void setIdentificationDetails(final List<TmaIdentificationData> identificationsList, final CustomerModel customerModel)
	{
		final Set<TmaIdentificationModel> identifications = new HashSet<>();
		if (CollectionUtils.isNotEmpty(identificationsList))
		{
			identificationsList.stream()
					.forEach(identificationData -> createIdentificationIfNotExists(identificationData, identifications));
		}
		customerModel.setIdentifications(identifications);
	}

	private void createIdentificationIfNotExists(final TmaIdentificationData identificationData,
			final Set<TmaIdentificationModel> identifications)
	{
		final String identificationTypeName = identificationData.getIdentificationType();
		final String identificationNumber = identificationData.getIdentificationNumber();
		if (StringUtils.isBlank(identificationTypeName) && StringUtils.isBlank(identificationNumber))
		{
			return;
		}
		final TmaIdentificationModel existingIdentificationModel = getTmaIdentificationService()
				.findIdentificationByTypeAndNumber(identificationTypeName, identificationNumber);
		if (ObjectUtils.isEmpty(existingIdentificationModel))
		{
			final TmaIdentificationModel newIdentificationModel = getModelService().create(TmaIdentificationModel.class);
			newIdentificationModel.setIdentificationType(
					getEnumerationService().getEnumerationValue(TmaIdentificationType.class, identificationTypeName));
			newIdentificationModel.setIdentificationNumber(identificationNumber);
			identifications.add(newIdentificationModel);
		}
		else
		{
			identifications.add(existingIdentificationModel);
		}
	}

	protected TmaSubscriptionAccessFacade getTmaSubscriptionAccessFacade()
	{
		return tmaSubscriptionAccessFacade;
	}

	@Required
	public void setTmaSubscriptionAccessFacade(final TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade)
	{
		this.tmaSubscriptionAccessFacade = tmaSubscriptionAccessFacade;
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

	protected TmaCustomerInventoryService getTmaCustomerInventoryService()
	{
		return tmaCustomerInventoryService;
	}

	@Required
	public void setTmaCustomerInventoryService(final TmaCustomerInventoryService tmaCustomerInventoryService)
	{
		this.tmaCustomerInventoryService = tmaCustomerInventoryService;
	}

	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
	}

	@Required
	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

	protected Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> getTmaSubscriptionBaseConverter()
	{
		return tmaSubscriptionBaseConverter;
	}

	@Required
	public void setTmaSubscriptionBaseConverter(
			final Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> tmaSubscriptionBaseConverter)
	{
		this.tmaSubscriptionBaseConverter = tmaSubscriptionBaseConverter;
	}

	protected Converter<TmaSubscriptionBaseModel, TmaSubscriptionSelectionData> getTmaSubscriptionSelectionConverter()
	{
		return tmaSubscriptionSelectionConverter;
	}

	@Required
	public void setTmaSubscriptionSelectionConverter(
			final Converter<TmaSubscriptionBaseModel, TmaSubscriptionSelectionData> tmaSubscriptionSelectionConverter)
	{
		this.tmaSubscriptionSelectionConverter = tmaSubscriptionSelectionConverter;
	}

	protected Converter<UserModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(final Converter<UserModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}

	@Override
	public TmaAddressReversePopulator getAddressReversePopulator()
	{
		return (TmaAddressReversePopulator) super.getAddressReversePopulator();
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	protected TmaIdentificationService getTmaIdentificationService()
	{
		return tmaIdentificationService;
	}
}

