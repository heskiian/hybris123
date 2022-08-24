/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionSelectionData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Default implementation of the {@link TmaSubscriptionBaseFacade}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionBaseFacade implements TmaSubscriptionBaseFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaSubscriptionBaseFacade.class);

	private TmaCustomerFacade customerFacade;
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;
	private Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> tmaSubscriptionBaseConverter;
	private Converter<TmaSubscriptionBaseData, TmaSubscriptionBaseModel> tmaSubscriptionBaseReverseConverter;
	private Converter<TmaSubscriptionBaseModel, TmaSubscriptionSelectionData> tmaSubscriptionSelectionConverter;
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter;
	private Converter<TmaSubscriptionAccessModel, TmaDetailedSubscriptionBaseData> tmaDetailedSubscriptionBaseConverter;
	private UserService userService;

	@Override
	public TmaSubscriptionBaseData createSubscriptionBase(final String subscriberIdentity, final String billingSystemId,
			final String billingAccountId)
	{
		final TmaSubscriptionBaseModel createdSubscriptionBase = getTmaSubscriptionBaseService()
				.createSubscriptionBase(subscriberIdentity, billingSystemId, billingAccountId);
		return getTmaSubscriptionBaseConverter().convert(createdSubscriptionBase);
	}

	@Override
	public void deleteSubscriptionBase(final String subscriberIdentity, final String billingSystemId)
	{
		getTmaSubscriptionBaseService().deleteSubscriptionBase(subscriberIdentity, billingSystemId);
	}

	@Override
	public Set<TmaSubscriptionBaseData> getAllSubscriptionBases()
	{
		final Set<TmaSubscriptionBaseModel> subscriptionBases = getTmaSubscriptionBaseService().getAllSubscriptionBases();
		return new HashSet<>(getTmaSubscriptionBaseConverter().convertAll(subscriptionBases));
	}

	@Override
	public TmaSubscriptionBaseData getSubscriptionBaseBySubscriberIdentity(final String subscriberIdentity)
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = getTmaSubscriptionBaseService()
				.getSubscriptionBaseByIdentity(subscriberIdentity);
		return getTmaSubscriptionBaseConverter().convert(subscriptionBaseModel);
	}

	@Override
	public TmaSubscriptionBaseData generateSubscriptionBase(final String billingAccountId)
	{
		return getTmaSubscriptionBaseConverter()
				.convert(getTmaSubscriptionBaseService().generateSubscriptionBase(billingAccountId));
	}

	@Override
	public Set<String> getMainTariffSubscribedProductIdsForSubscriptionBases(
			final List<TmaSubscriptionBaseData> subscriptionBaseList)
	{
		return getTmaSubscriptionBaseService().getMainTariffSubscribedProductIdsForSubscriptionBases(
				getTmaSubscriptionBaseReverseConverter().convertAll(subscriptionBaseList));
	}

	@Override
	public TmaSubscriptionBaseData getSubscriptionBaseForSubscriberIdentity(final String subscriberIdentity,
			final String billingSystemID)
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = tmaSubscriptionBaseService.getSubscriptionBase(subscriberIdentity,
				billingSystemID);

		return getTmaSubscriptionBaseConverter().convert(subscriptionBaseModel);
	}

	@Override
	public List<TmaDetailedSubscriptionBaseData> findSubscriptionBasesByPrincipal(final String principalUid)
	{

		if (!getUserService().isUserExisting(principalUid))
		{
			throw new UnknownIdentifierException("Cannot find user with uid '" + principalUid + "'");
		}

		final List<TmaSubscriptionAccessModel> tmaSubscriptionAccessModelList = getTmaSubscriptionAccessService()
				.getSubscriptionAccessesByPrincipalUid(principalUid);
		final Map<String, List<TmaSubscriptionAccessModel>> subscriptionBaseGroupMap = new HashMap<>();
		for (final TmaSubscriptionAccessModel tmaSubscriptionAccessModel : tmaSubscriptionAccessModelList)
		{
			final String subscriberIdentity = tmaSubscriptionAccessModel.getSubscriptionBase().getSubscriberIdentity();
			if (subscriptionBaseGroupMap.containsKey(subscriberIdentity))
			{
				final List<TmaSubscriptionAccessModel> subscriptionAccessModelList = subscriptionBaseGroupMap.get(subscriberIdentity);
				subscriptionAccessModelList.add(tmaSubscriptionAccessModel);
			}
			else
			{
				final List<TmaSubscriptionAccessModel> subscriptionAccessModelList = new ArrayList<>();
				subscriptionAccessModelList.add(tmaSubscriptionAccessModel);
				subscriptionBaseGroupMap.put(subscriberIdentity, subscriptionAccessModelList);
			}
		}

		final List<TmaDetailedSubscriptionBaseData> detailedSubscriptionBaseDataList = new ArrayList<>();

		for (final Map.Entry<String, List<TmaSubscriptionAccessModel>> subscriptionBaseGroup : subscriptionBaseGroupMap.entrySet())
		{
			final TmaDetailedSubscriptionBaseData detailedSubscriptionBaseData = getTmaDetailedSubscriptionBaseConverter()
					.convert(subscriptionBaseGroup.getValue().get(0));
			final List<TmaSubscriptionAccessData> subscriptionAccessDataList = new ArrayList<>();
			for (final TmaSubscriptionAccessModel tmaSubscriptionAccessModel : subscriptionBaseGroup.getValue())
			{
				final TmaSubscriptionAccessData subscriptionAccessData = getTmaSubscriptionAccessConverter()
						.convert(tmaSubscriptionAccessModel);
				subscriptionAccessDataList.add(subscriptionAccessData);
			}
			detailedSubscriptionBaseData.setSubscriptionAccessData(subscriptionAccessDataList);
			detailedSubscriptionBaseDataList.add(detailedSubscriptionBaseData);
		}

		return detailedSubscriptionBaseDataList;
	}

	@Override
	public boolean isIdenticalBillAgremment(final List<TmaSubscriptionBaseData> subscriptionBases)
	{
		final List<String> billAgreementIds = new ArrayList<>();
		subscriptionBases.forEach(subscriptionBase -> {
			final TmaSubscriptionBaseModel subscriptionBaseModel = getTmaSubscriptionBaseService()
					.getSubscriptionBase(subscriptionBase.getSubscriberIdentity(), subscriptionBase.getBillingSystemId());
			if (!ObjectUtils.isEmpty(subscriptionBaseModel) && !ObjectUtils.isEmpty(subscriptionBaseModel.getBillAgreement()))
			{
				billAgreementIds.add(subscriptionBaseModel.getBillAgreement().getId());
			}
		});

		if (CollectionUtils.isEmpty(billAgreementIds))
		{
			return false;
		}
		return billAgreementIds.stream().allMatch(billAgreementIds.get(0)::equals);
	}

	@Override
	public boolean doesSubscriptionBaseExist(final String subscriptionBaseId)
	{
		return getTmaSubscriptionBaseService().doesSubscriptionBaseExist(subscriptionBaseId);
	}

	@Override
	public boolean isSubscriptionBaseAccessibleToUser(final String subscriptionBaseId, final String userId)
	{
		final TmaSubscriptionBaseModel subscriptionBase = getTmaSubscriptionBaseService()
				.getSubscriptionBaseByIdentity(subscriptionBaseId);
		try
		{
			final boolean defaultAccessToSubscriptionBase = false;
			final TmaSubscriptionAccessModel subscriptionAccess = getTmaSubscriptionAccessService()
					.getSubscriptionAccessByPrincipalAndSubscriptionBase(userId, subscriptionBase.getBillingSystemId(),
							subscriptionBase.getSubscriberIdentity());
			return (subscriptionAccess.getPrincipal().getUid().equalsIgnoreCase(userId))
					? ((subscriptionAccess.getAccessType().equals(TmaAccessType.OWNER))
							|| (subscriptionAccess.getAccessType().equals(TmaAccessType.BENEFICIARY)))
					: (defaultAccessToSubscriptionBase);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.error("Subscribed Base " + subscriptionBaseId + "is not accessible ", e);
			return false;
		}
	}

	protected TmaCustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final TmaCustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	@Required
	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
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

	@Required
	public void setTmaSubscriptionBaseConverter(
			final Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> tmaSubscriptionBaseConverter)
	{
		this.tmaSubscriptionBaseConverter = tmaSubscriptionBaseConverter;
	}

	protected Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> getTmaSubscriptionBaseConverter()
	{
		return tmaSubscriptionBaseConverter;
	}

	protected Converter<TmaSubscriptionBaseData, TmaSubscriptionBaseModel> getTmaSubscriptionBaseReverseConverter()
	{
		return tmaSubscriptionBaseReverseConverter;
	}

	@Required
	public void setTmaSubscriptionBaseReverseConverter(
			final Converter<TmaSubscriptionBaseData, TmaSubscriptionBaseModel> tmaSubscriptionBaseReverseConverter)
	{
		this.tmaSubscriptionBaseReverseConverter = tmaSubscriptionBaseReverseConverter;
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

	protected Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> getTmaSubscriptionAccessConverter()
	{
		return tmaSubscriptionAccessConverter;
	}

	@Required
	public void setTmaSubscriptionAccessConverter(
			final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter)
	{
		this.tmaSubscriptionAccessConverter = tmaSubscriptionAccessConverter;
	}

	protected Converter<TmaSubscriptionAccessModel, TmaDetailedSubscriptionBaseData> getTmaDetailedSubscriptionBaseConverter()
	{
		return tmaDetailedSubscriptionBaseConverter;
	}

	@Required
	public void setTmaDetailedSubscriptionBaseConverter(
			final Converter<TmaSubscriptionAccessModel, TmaDetailedSubscriptionBaseData> tmaDetailedSubscriptionBaseConverter)
	{
		this.tmaDetailedSubscriptionBaseConverter = tmaDetailedSubscriptionBaseConverter;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
