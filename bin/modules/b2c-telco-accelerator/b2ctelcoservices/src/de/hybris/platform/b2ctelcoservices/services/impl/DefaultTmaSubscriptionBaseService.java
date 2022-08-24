/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionBaseDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingAccountService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;


/**
 * Default implementation of the {@link TmaSubscriptionBaseService}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionBaseService implements TmaSubscriptionBaseService
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaSubscriptionBaseService.class);
	private static final String BPO = "BPO";
	private static final String BILL_ARR = "BILL_ARR";
	private static final String NO_BPO = "NOBPO";
	private ModelService modelService;
	private TmaBillingAccountService tmaBillingAccountService;
	private TmaSubscriptionBaseDao tmaSubscriptionBaseDao;
	private PersistentKeyGenerator tmaSubscriberIdentityGenerator;
	private String defaultBillingSystemId;
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;

	@Override
	public List<TmaSubscriptionBaseModel> getSubscriptionBases(final String customerId)
	{
		final List<TmaSubscriptionAccessModel> subscriptionAccesses = getTmaSubscriptionAccessService()
				.getSubscriptionAccessesByPrincipalUid(customerId);

		if (CollectionUtils.isEmpty(subscriptionAccesses))
		{
			return new ArrayList<>();
		}
		return subscriptionAccesses.stream()
				.map(TmaSubscriptionAccessModel::getSubscriptionBase).collect(Collectors.toList());

	}

	@Override
	public TmaSubscriptionBaseModel getSubscriptionBase(final String subscriberIdentity, final String billingSystemId)
	{
		return getTmaSubscriptionBaseDao().findSubscriptionBase(subscriberIdentity, billingSystemId);
	}

	@Override
	public Set<TmaSubscriptionBaseModel> getAllSubscriptionBases()
	{
		return getTmaSubscriptionBaseDao().findAllSubscriptionBases();
	}

	@Override
	public TmaSubscriptionBaseModel getSubscriptionBaseByIdentity(final String subscriberIdentity)
	{
		return getTmaSubscriptionBaseDao().findSubscriptionBaseByIdentity(subscriberIdentity);
	}

	@Override
	public Set<String> getMainTariffSubscribedProductIdsForSubscriptionBases(
			final List<TmaSubscriptionBaseModel> subscriptionBaseModelList)
	{
		final Set<String> result = new TreeSet<>();
		if (subscriptionBaseModelList == null || subscriptionBaseModelList.isEmpty())
		{
			return result;
		}

		for (final TmaSubscriptionBaseModel subscriptionBase : subscriptionBaseModelList)
		{
			final TmaSubscriptionBaseModel subscriptionBaseModel = getSubscriptionBase(subscriptionBase.getSubscriberIdentity(),
					subscriptionBase.getBillingSystemId());

			final String mainTariffProductCode = getMainTariffSubscribedProductIdForSubscriptionBase(subscriptionBaseModel);

			if (StringUtils.isNotBlank(mainTariffProductCode))
			{
				result.add(mainTariffProductCode);
			}
		}

		return result;
	}

	@Override
	public TmaSubscriptionBaseModel createSubscriptionBase(final String subscriberIdentity, final String billingSystemId,
			final String billingAccountId)
	{
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);
		final TmaBillingAccountModel billingAccountModel = getTmaBillingAccountService().findBillingAccount(billingSystemId,
				billingAccountId);
		final Set<TmaSubscriptionBaseModel> subscriptionBases = billingAccountModel.getSubscriptionBases();
		final Set<TmaSubscriptionBaseModel> subscriptionBasesToBeSaved = new HashSet<>(subscriptionBases);
		final TmaSubscriptionBaseModel subscriptionBaseModel = getModelService().create(TmaSubscriptionBaseModel.class);
		subscriptionBaseModel.setSubscriberIdentity(subscriberIdentity);
		subscriptionBaseModel.setBillingSystemId(billingSystemId);

		getModelService().save(subscriptionBaseModel);
		subscriptionBasesToBeSaved.add(subscriptionBaseModel);

		billingAccountModel.setSubscriptionBases(subscriptionBasesToBeSaved);
		getModelService().save(billingAccountModel);

		return subscriptionBaseModel;
	}

	@Transactional
	@Override
	public void deleteSubscriptionBase(final String subscriberIdentity, final String billingSystemId)
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = getTmaSubscriptionBaseDao().findSubscriptionBase(subscriberIdentity,
				billingSystemId);
		removeSubscriptionBase(subscriptionBaseModel);
	}

	@Override
	public TmaSubscriptionBaseModel generateSubscriptionBase(final String billingAccountId)
	{
		return createSubscriptionBase(getTmaSubscriberIdentityGenerator().generate().toString(), defaultBillingSystemId,
				billingAccountId);
	}

	public TmaSubscriptionBaseModel generateSubscriptionBase(final String customerId, final String billingAccountId,
			final TmaAccessType accessType)
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = createSubscriptionBase(getTmaSubscriberIdentityGenerator()
				.generate().toString(), defaultBillingSystemId, billingAccountId);
		final TmaSubscriptionAccessModel subscriptionAccessModel = getTmaSubscriptionAccessService()
				.createSubscriptionAccessModel(customerId, billingAccountId, subscriptionBaseModel, accessType);
		subscriptionBaseModel.setSubscriptionAccesses(Sets.newSet(subscriptionAccessModel));
		getModelService().save(subscriptionBaseModel);
		getModelService().save(subscriptionAccessModel);
		return subscriptionBaseModel;
	}

	@Override
	public String getMainTariffProductCodeForSubscriptionBase(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		final Set<TmaSubscribedProductModel> subscribedProducts = subscriptionBaseModel.getSubscribedProducts();
		if (subscribedProducts == null || subscribedProducts.isEmpty())
		{
			return null;
		}

		for (final TmaSubscribedProductModel subscribedProduct : subscribedProducts)
		{
			if (TmaServiceType.TARIFF_PLAN.equals(subscribedProduct.getServiceType()))
			{
				return subscribedProduct.getProductCode();
			}
		}

		return null;
	}

	@Override
	public String getMainTariffSubscribedProductIdForSubscriptionBase(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		final Set<TmaSubscribedProductModel> subscribedProducts = subscriptionBaseModel.getSubscribedProducts();
		if (subscribedProducts == null || subscribedProducts.isEmpty())
		{
			return null;
		}

		for (final TmaSubscribedProductModel subscribedProduct : subscribedProducts)
		{
			if (TmaServiceType.TARIFF_PLAN.equals(subscribedProduct.getServiceType()))
			{
				return subscribedProduct.getId();
			}
		}

		return null;
	}

	private void removeSubscriptionBase(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		getModelService().removeAll(subscriptionBaseModel.getSubscriptionAccesses());
		getModelService().removeAll(subscriptionBaseModel.getSubscribedProducts());
		getModelService().remove(subscriptionBaseModel);
	}

	@Override
	public Map<String, List<TmaSubscriptionBaseModel>> groupSubscriptionsByBillingArrangementAndBpo(
			final List<TmaSubscriptionBaseModel> subscriptionBases)
	{
		final Map<String, List<TmaSubscriptionBaseModel>> result = new HashMap<>();

		final Map<String, List<TmaSubscriptionBaseModel>> subscrMapGroupedByBillArr = new HashMap<>();
		for (final TmaSubscriptionBaseModel subscriptionBaseModel : subscriptionBases)
		{

			final String billingAgreementId = subscriptionBaseModel.getBillAgreement().getId();
			final List<TmaSubscriptionBaseModel> alreadyDeterminedSubscriptionsSelectionsByBillAr = subscrMapGroupedByBillArr
					.get(billingAgreementId);

			if (!CollectionUtils.isEmpty(alreadyDeterminedSubscriptionsSelectionsByBillAr))
			{
				continue;
			}

			final List<TmaSubscriptionBaseModel> subscriptionsWithSameBillArr = getSubscriptionBasesByAttribute(BILL_ARR,
					billingAgreementId, subscriptionBases);

			subscrMapGroupedByBillArr.put(billingAgreementId, subscriptionsWithSameBillArr);

			updateResultWithSubscriptionsGroupedByBpo(result, subscriptionBaseModel, subscriptionsWithSameBillArr);
		}

		return result;
	}

	@Override
	public boolean doesSubscriptionBaseExist(final String subscriberIdentity)
	{
		try
		{
			getSubscriptionBaseByIdentity(subscriberIdentity);
			return true;
		}
		catch (final ModelNotFoundException e)
		{
			LOG.debug("Subscribed Base for " + subscriberIdentity + "not found", e);
			return false;
		}
	}

	private List<TmaSubscriptionBaseModel> getSubscriptionBasesByAttribute(final String attributeType, final String attributeValue,
			final List<TmaSubscriptionBaseModel> subscriptionBases)
	{
		final List<TmaSubscriptionBaseModel> result = new ArrayList<>();
		for (final TmaSubscriptionBaseModel currentSubscriptionBase : subscriptionBases)
		{
			if (!StringUtils.isEmpty(attributeValue)
					&& attributeValue.equals(getValueByBpoOrBillArr(attributeType, currentSubscriptionBase)))
			{
				result.add(currentSubscriptionBase);
			}
		}

		return result;
	}

	private String getValueByBpoOrBillArr(final String attributeType, final TmaSubscriptionBaseModel currentSubscriptionBase)
	{
		if (BPO.equals(attributeType))
		{
			return getBpoCode(currentSubscriptionBase);
		}

		return currentSubscriptionBase.getBillAgreement().getId();
	}

	private String getBpoCode(final TmaSubscriptionBaseModel subscriptionBaseModel)
	{
		final List<TmaSubscribedProductModel> tariffPlans = subscriptionBaseModel.getSubscribedProducts().stream()
				.filter(s -> TmaServiceType.TARIFF_PLAN.equals(s.getServiceType())).collect(Collectors.toList());
		if (tariffPlans.isEmpty())
		{
			return StringUtils.EMPTY;
		}
		else
		{
			final TmaSubscribedProductModel mainTariff = tariffPlans.get(0);
			return mainTariff.getBundledProductCode() != null ? mainTariff.getBundledProductCode() : StringUtils.EMPTY;
		}
	}

	/**
	 * Updates the result map given with the subscription selections lists having the same billing agreement and same BPO
	 * as
	 * the subscription selection given, list obtained by iterating over the list of subscription selections given (list
	 * containing subscription selections with same billing agreement) and obtaining groups of subscriptoin selections
	 * with
	 * same BPO as the subscription selection given.
	 *
	 * @param result
	 * 		the result map updated with entries grouping subscription selections by billing agreement and BPO
	 * @param subscriptionSelectionData
	 * 		current subscription selection data for which the similar subscriptions with same bpo
	 * 		will be determined
	 * @param subscriptionsWithSameBillAgr
	 * 		the list of subscription selection with same billing agreement
	 */
	private void updateResultWithSubscriptionsGroupedByBpo(final Map<String, List<TmaSubscriptionBaseModel>> result,
			final TmaSubscriptionBaseModel subscriptionSelectionData,
			final List<TmaSubscriptionBaseModel> subscriptionsWithSameBillAgr)
	{
		final Map<String, List<TmaSubscriptionBaseModel>> subscrMapGroupedByBpo = new HashMap<>();

		int noBpoSelectionId = 0;
		for (final TmaSubscriptionBaseModel subscriptionBaseWithSameBillArr : subscriptionsWithSameBillAgr)
		{
			final String bpoCode = getBpoCode(subscriptionBaseWithSameBillArr);
			final String billingAgreementId = subscriptionSelectionData.getBillAgreement().getId();
			if (StringUtils.isEmpty(bpoCode))
			{
				final String key = billingAgreementId + NO_BPO + noBpoSelectionId;
				result.put(key, Arrays.asList(subscriptionBaseWithSameBillArr));
				noBpoSelectionId++;
				continue;
			}

			final List<TmaSubscriptionBaseModel> alreadyDeterminedSubscriptionsByBpo = subscrMapGroupedByBpo
					.get(getBpoCode(subscriptionSelectionData));

			if (!CollectionUtils.isEmpty(alreadyDeterminedSubscriptionsByBpo))
			{
				final String resultKey = billingAgreementId + bpoCode;
				result.put(resultKey, alreadyDeterminedSubscriptionsByBpo);
				continue;
			}

			final List<TmaSubscriptionBaseModel> subscriptionsGroupedByBpo = getSubscriptionBasesByAttribute(BPO, bpoCode,
					subscriptionsWithSameBillAgr);
			subscrMapGroupedByBpo.put(bpoCode, subscriptionsGroupedByBpo);
			final String resultKey = billingAgreementId + bpoCode;
			result.put(resultKey, subscriptionsGroupedByBpo);
		}
	}

	protected PersistentKeyGenerator getTmaSubscriberIdentityGenerator()
	{
		return tmaSubscriberIdentityGenerator;
	}

	@Required
	public void setTmaSubscriberIdentityGenerator(final PersistentKeyGenerator tmaSubscriberIdentityGenerator)
	{
		this.tmaSubscriberIdentityGenerator = tmaSubscriberIdentityGenerator;
	}

	protected String getDefaultBillingSystemId()
	{
		return defaultBillingSystemId;
	}

	@Required
	public void setDefaultBillingSystemId(final String defaultBillingSystemId)
	{
		this.defaultBillingSystemId = defaultBillingSystemId;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setTmaBillingAccountService(final TmaBillingAccountService tmaBillingAccountService)
	{
		this.tmaBillingAccountService = tmaBillingAccountService;
	}

	protected TmaBillingAccountService getTmaBillingAccountService()
	{
		return tmaBillingAccountService;
	}

	@Required
	public void setTmaSubscriptionBaseDao(final TmaSubscriptionBaseDao tmaSubscriptionBaseDao)
	{
		this.tmaSubscriptionBaseDao = tmaSubscriptionBaseDao;
	}

	protected TmaSubscriptionBaseDao getTmaSubscriptionBaseDao()
	{
		return tmaSubscriptionBaseDao;
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
