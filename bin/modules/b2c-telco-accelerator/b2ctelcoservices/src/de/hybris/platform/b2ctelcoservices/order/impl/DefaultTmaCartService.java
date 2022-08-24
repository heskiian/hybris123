/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserNetCheckingStrategy;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaCartService}
 *
 * @since 1907
 */
public class DefaultTmaCartService extends DefaultCartService implements TmaCartService
{
	private transient UserNetCheckingStrategy userNetCheckingStrategy;
	private transient KeyGenerator keyGenerator;
	private transient CommonI18NService commonI18NService;
	private transient BaseStoreService baseStoreService;
	private transient TmaCustomerInventoryService customerInventoryService;

	public DefaultTmaCartService(BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	public CartModel createCartForUserAndBaseSite(UserModel userModel, BaseSiteModel baseSiteModel)
	{
		final CurrencyModel currency = getCommonI18NService().getCurrentCurrency();
		final String cartModelTypeCode = Config.getString(JaloSession.CART_TYPE, "Cart");
		final CartModel cart = getModelService().create(cartModelTypeCode);
		cart.setCode(String.valueOf(getKeyGenerator().generate()));
		cart.setUser(userModel);
		cart.setCurrency(currency);
		cart.setDate(new Date());
		cart.setNet(getUserNetCheckingStrategy().isNetUser(userModel));
		cart.setSite(baseSiteModel);
		cart.setStore(getBaseStoreService().getCurrentBaseStore());
		getModelService().save(cart);
		return cart;
	}

	@Override
	public boolean validateCartForEligibilityRules(final AbstractOrderModel cart)
	{
		final Set<TmaProcessType> eligibleProcessesForUser = getCustomerInventoryService()
				.retrieveProcessesByCustomerId(cart.getUser().getUid());

		final Set<AbstractOrderEntryModel> invalidEntry = cart.getEntries().stream()
				.filter(entry -> entry.getProcessType() != null && !(eligibleProcessesForUser.contains(entry.getProcessType())))
				.collect(Collectors.toSet());
		if (!(invalidEntry).isEmpty())
		{
			return true;
		}

		return false;
	}

	protected UserNetCheckingStrategy getUserNetCheckingStrategy()
	{
		return userNetCheckingStrategy;
	}

	@Required
	public void setUserNetCheckingStrategy(final UserNetCheckingStrategy userNetCheckingStrategy)
	{
		this.userNetCheckingStrategy = userNetCheckingStrategy;
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	@Required
	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	@Required
	public void setCustomerInventoryService(final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}
}
