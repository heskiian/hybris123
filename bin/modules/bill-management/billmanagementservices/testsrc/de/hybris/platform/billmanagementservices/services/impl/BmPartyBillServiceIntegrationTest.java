/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementservices.services.BmPartyBillService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * IntegrationTest of {@link BmPartyBillService}.
 *
 * @since 2108
 */
@IntegrationTest
public class BmPartyBillServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String BILL_NO_1 = "cycleBill1";
	private static final String BILL_NO_2 = "cycleBill2";
	private static final String BILL_NO_3 = "cycleBill3";
	private static final String INVALID_BILL_NO = "invalidBillNo";
	private static final String PARTY_ID = "party_10830";

	@Resource
	private BmPartyBillService bmPartyBillService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_customer_bill.impex", "utf-8");
	}

	@Test
	public void testGetPartyBillById()
	{
		final BmPartyBillModel partyBillModel = getBmPartyBillService().getPartyBill(BILL_NO_1);
		Assert.assertEquals(BILL_NO_1, partyBillModel.getBillNo());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetPartyBillForIncorrectId()
	{
		getBmPartyBillService().getPartyBill(INVALID_BILL_NO);
	}

	@Test
	public void testGetPartyBills()
	{
		final List<BmPartyBillModel> partyBillModels = Arrays
				.asList(createBmPartyBill(BILL_NO_1), createBmPartyBill(BILL_NO_2), createBmPartyBill(BILL_NO_3));
		final List<BmPartyBillModel> partyBillModelsRetrived = getBmPartyBillService()
				.getPartyBills(createPartyBillContext(null), 0, 10);
		Assert.assertEquals(partyBillModels.size(), partyBillModelsRetrived.size());
		Assert.assertTrue(getPartyBillIds(partyBillModelsRetrived).containsAll(getPartyBillIds(partyBillModels)));
	}

	@Test
	public void testGetPartyBillsFilteredByPartyRole()
	{
		final List<BmPartyBillModel> partyBillModels = Arrays
				.asList(createBmPartyBill(BILL_NO_3));
		final List<BmPartyBillModel> partyBillModelsRetrived = getBmPartyBillService()
				.getPartyBills(createPartyBillContext(PARTY_ID), 0, 10);
		Assert.assertEquals(partyBillModels.size(), partyBillModelsRetrived.size());
		Assert.assertTrue(getPartyBillIds(partyBillModelsRetrived).containsAll(getPartyBillIds(partyBillModels)));
	}

	private BmPartyBillModel createBmPartyBill(final String id)
	{
		final BmPartyBillModel partyBillModel = new BmPartyBillModel();
		partyBillModel.setBillNo(id);
		return partyBillModel;
	}

	private BmPartyBillContext createPartyBillContext(final String relatedPartyId)
	{
		final BmPartyBillContext partyBillContext = new BmPartyBillContext();
		partyBillContext.setRelatedPartyId(relatedPartyId);
		return partyBillContext;
	}

	private List<String> getPartyBillIds(final List<BmPartyBillModel> partyBillModels)
	{
		final List<String> partyBillModelsIds = new ArrayList<>();
		partyBillModels.forEach(partyBillModel -> partyBillModelsIds.add(partyBillModel.getBillNo()));
		return partyBillModelsIds;
	}

	private BmPartyBillService getBmPartyBillService()
	{
		return bmPartyBillService;
	}
}
