/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrAgreementsService;
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


@IntegrationTest
public class DefaultAgrAgreementServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String MOON_AGREEMENT_ID = "000001";
	private static final String LIGHT_AGREEMENT_ID = "000002";
	private static final String INVALID_AGREEMENT_ID = "invalidId";
	private static final String PARTY_ID = "party_10830";
	private static final String STATUS = "APPROVED";
	private static final String AGREEMENT_ID = "agreementId1";
	private static final String AGREEMENT_ID_2 = "agreementId2";

	@Resource
	private AgrAgreementsService agrAgreementsService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_agreement.impex", "utf-8");
	}

	@Test
	public void testGetAgreementById()
	{
		final AgrAgreementModel agreement = getAgrAgreementsService().getAgreement(MOON_AGREEMENT_ID);
		Assert.assertEquals(MOON_AGREEMENT_ID, agreement.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetAgreementForIncorrectId()
	{
		getAgrAgreementsService().getAgreement(INVALID_AGREEMENT_ID);
	}

	@Test
	public void testGetAgreements()
	{
		final List<AgrAgreementModel> agreementModels = Arrays
				.asList(createAgrAgreement(MOON_AGREEMENT_ID), createAgrAgreement(LIGHT_AGREEMENT_ID));
		Assert.assertEquals(agreementModels.size(),
				getAgrAgreementsService().getAgreements(createAgreementContext(null, null), 0, 2).size());
		Assert.assertTrue(getAgreementIds(getAgrAgreementsService().getAgreements(createAgreementContext(null, null), 0, 2))
				.containsAll(getAgreementIds(agreementModels)));
	}

	@Test
	public void testGetAgreementsFilteredByPartyRole()
	{
		final List<AgrAgreementModel> agreementModels = Arrays
				.asList(createAgrAgreement(MOON_AGREEMENT_ID));
		Assert.assertEquals(agreementModels.size(),
				getAgrAgreementsService().getAgreements(createAgreementContext(PARTY_ID, null), 0, 1).size());
		Assert.assertTrue(
				getAgreementIds(getAgrAgreementsService().getAgreements(createAgreementContext(PARTY_ID, null), 0, 1))
						.containsAll(getAgreementIds(agreementModels)));
	}

	@Test
	public void testGetAgreementsFilteredByStatus()
	{
		final List<AgrAgreementModel> specificationModels = Arrays
				.asList(createAgrAgreement(MOON_AGREEMENT_ID));
		Assert.assertEquals(specificationModels.size(),
				getAgrAgreementsService().getAgreements(createAgreementContext(null, STATUS), 0, 2)
						.size());
		Assert.assertTrue(getAgreementIds(getAgrAgreementsService().getAgreements(createAgreementContext(null, STATUS), 0, 2))
				.containsAll(getAgreementIds(specificationModels)));
	}

	@Test
	public void testCreateAndSaveProduct()
	{
		final AgrAgreementModel agreementModel = getAgrAgreementsService().createAgreement();
		agreementModel.setId(AGREEMENT_ID);
		getAgrAgreementsService().saveAgreement(agreementModel);

		final AgrAgreementModel retrievedAgreementModel = getAgrAgreementsService().getAgreement(AGREEMENT_ID);
		Assert.assertEquals(AGREEMENT_ID, retrievedAgreementModel.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testRemoveProduct()
	{
		final AgrAgreementModel agreementModel = getAgrAgreementsService().createAgreement();
		agreementModel.setId(AGREEMENT_ID_2);
		getAgrAgreementsService().saveAgreement(agreementModel);

		final AgrAgreementModel retrievedAgreementModel = getAgrAgreementsService().getAgreement(AGREEMENT_ID_2);
		Assert.assertEquals(AGREEMENT_ID_2, retrievedAgreementModel.getId());

		getAgrAgreementsService().removeAgreement(retrievedAgreementModel);
		getAgrAgreementsService().getAgreement(AGREEMENT_ID_2);
	}

	private AgrAgreementModel createAgrAgreement(final String id)
	{
		final AgrAgreementModel agreementModel = new AgrAgreementModel();
		agreementModel.setId(id);
		return agreementModel;
	}

	private AgrAgreementContext createAgreementContext(final String partyId, final String status)
	{
		final AgrAgreementContext agreementContext = new AgrAgreementContext();
		agreementContext.setRelatedPartyId(partyId);
		agreementContext.setStatus(status);
		return agreementContext;
	}

	private List<String> getAgreementIds(final List<AgrAgreementModel> agreementModels)
	{
		final List<String> agreementModelsIds = new ArrayList<>();
		agreementModels.forEach(agreementModel -> agreementModelsIds.add(agreementModel.getId()));
		return agreementModelsIds;
	}

	public AgrAgreementsService getAgrAgreementsService()
	{
		return agrAgreementsService;
	}
}
