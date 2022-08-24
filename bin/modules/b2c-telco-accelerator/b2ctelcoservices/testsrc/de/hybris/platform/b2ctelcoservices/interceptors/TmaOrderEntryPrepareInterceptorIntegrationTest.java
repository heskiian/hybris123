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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.fest.assertions.Assertions.assertThat;


/**
 * Integration test for {@link TmaOrderEntryPrepareInterceptor}
 *
 * @since 2003
 */
@IntegrationTest
public class TmaOrderEntryPrepareInterceptorIntegrationTest extends ServicelayerTest
{
	private static final String CURRENCY_ISO_CODE = "USD";
	private static final String ADDRESS_ID = "testAddress";
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

	private AddressModel installationAddress;
	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_cloneInstallationAddress.impex", "utf-8");
		currency = commonI18NService.getCurrency(CURRENCY_ISO_CODE);
		installationAddress = addressService.findAddress(ADDRESS_ID);
	}

	@Test
	public void shouldCloneInstallationAddressFromOrderEntry()
	{
		final OrderModel order = createOrder();

		modelService.save(order);

		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);
		final AddressModel clonedInstallationAddress = orderEntry.getInstallationAddress();

		assertThat(clonedInstallationAddress).isNotSameAs(installationAddress);
		assertTrue(clonedInstallationAddress.getDuplicate());
	}

	@Test
	public void shouldNotCloneInstallationAddressFromOrderEntry()
	{
		final OrderModel order = createOrder();

		modelService.save(order);

		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);
		final AddressModel clonedInstallationAddress = orderEntry.getInstallationAddress();

		order.setNet(false);
		modelService.save(order);

		final AddressModel installationAddress = orderEntry.getInstallationAddress();

		assertThat(installationAddress).isSameAs(clonedInstallationAddress);

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
		entry.setInstallationAddress(installationAddress);
		modelService.save(entry);
		order.setEntries(Collections.singletonList(entry));
	}

}
