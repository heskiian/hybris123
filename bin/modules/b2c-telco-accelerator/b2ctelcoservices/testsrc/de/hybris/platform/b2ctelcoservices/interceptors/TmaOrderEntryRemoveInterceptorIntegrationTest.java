/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.impl.DefaultTmaPoService;
import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link TmaOrderEntryRemoveInterceptor}
 *
 * @since 2003
 */
@IntegrationTest
public class TmaOrderEntryRemoveInterceptorIntegrationTest extends ServicelayerTest
{

	private static final String CURRENCY_ISO_CODE = "USD";
	private static final String INSTALLATION_ADDRESS_ID = "testAddress";
	private static final String CUSTOMER_ID = "testuser";
	private static final String ORDER_ID = "testOrder";
	private static final String PRODUCT_ID = "int_100";

	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private CustomerService customerService;
	@Resource
	private DefaultTmaPoService tmaPoService;
	@Resource
	private TmaAddressService addressService;

	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_cloneInstallationAddress.impex", "utf-8");
		currency = commonI18NService.getCurrency(CURRENCY_ISO_CODE);

	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldRemoveDuplicatedInstallationAddressFromOrderEntry()
	{
		final OrderModel order = createOrder();
		modelService.save(order);

		final String duplicatedInstallationAddressId = order.getEntries().get(0).getInstallationAddress().getId();
		modelService.remove(order);

		addressService.findAddress(duplicatedInstallationAddressId);
	}

	protected OrderModel createOrder()
	{
		final CustomerModel customer = customerService.getCustomerByCustomerId(CUSTOMER_ID);
		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode(ORDER_ID);
		order.setUser(customer);
		order.setCurrency(currency);
		order.setDate(new Date());
		order.setNet(true);
		setOrderEntry(order);
		return order;
	}

	protected void setOrderEntry(AbstractOrderModel order)
	{
		final OrderEntryModel entry = modelService.create(OrderEntryModel.class);
		entry.setEntryNumber(0);
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(PRODUCT_ID);
		entry.setProduct(productOffering);
		entry.setUnit(productOffering.getUnit());
		entry.setOrder(order);
		entry.setQuantity(1L);
		entry.setProcessType(TmaProcessType.ACQUISITION);
		final AddressModel installationAddress = addressService.findAddress(INSTALLATION_ADDRESS_ID);
		entry.setInstallationAddress(installationAddress);
		modelService.save(entry);
		order.setEntries(Collections.singletonList(entry));
	}
}
