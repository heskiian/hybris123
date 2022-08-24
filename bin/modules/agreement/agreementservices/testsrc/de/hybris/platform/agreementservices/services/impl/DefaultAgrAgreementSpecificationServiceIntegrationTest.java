/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrAgreementSpecificationService;
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
public class DefaultAgrAgreementSpecificationServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String MOON_AGREEMENT_SPECIFICATION_ID = "100001";
	private static final String LIGHT_AGREEMENT_SPECIFICATION_ID = "100002";
	private static final String INVALID_AGREEMENT_SPECIFICATION_ID = "invalidId";
	private static final String PARTY_ID = "party_10830";
	private static final String LIFECYCLE_STATUS = "APPROVED";

	@Resource
	private AgrAgreementSpecificationService agrAgreementSpecificationService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_agreementSpecification.impex", "utf-8");
	}

	@Test
	public void testGetAgreementById()
	{
		final AgrAgreementSpecificationModel agreement = getAgrAgreementSpecificationService()
				.getAgreementSpecification(MOON_AGREEMENT_SPECIFICATION_ID);
		Assert.assertEquals(MOON_AGREEMENT_SPECIFICATION_ID, agreement.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetAgreementForIncorrectId()
	{
		getAgrAgreementSpecificationService().getAgreementSpecification(INVALID_AGREEMENT_SPECIFICATION_ID);
	}

	@Test
	public void testGetAgreements()
	{
		final List<AgrAgreementSpecificationModel> specificationModels = Arrays
				.asList(createAgrAgreementSpecification(MOON_AGREEMENT_SPECIFICATION_ID),
						createAgrAgreementSpecification(LIGHT_AGREEMENT_SPECIFICATION_ID));
		Assert.assertEquals(specificationModels.size(),
				getAgrAgreementSpecificationService().getAgreementSpecifications(createAgreementContext(null, null), 0, 2).size());
		Assert.assertTrue(getAgreementSpecificationIds(
				getAgrAgreementSpecificationService().getAgreementSpecifications(createAgreementContext(null, null), 0, 2))
				.containsAll(getAgreementSpecificationIds(specificationModels)));
	}

	@Test
	public void testGetAgreementsFilteredByPartyRole()
	{
		final List<AgrAgreementSpecificationModel> specificationModels = Arrays
				.asList(createAgrAgreementSpecification(MOON_AGREEMENT_SPECIFICATION_ID));
		Assert.assertEquals(specificationModels.size(),
				getAgrAgreementSpecificationService().getAgreementSpecifications(createAgreementContext(PARTY_ID, null), 0, 1)
						.size());
		Assert.assertTrue(
				getAgreementSpecificationIds(
						getAgrAgreementSpecificationService().getAgreementSpecifications(createAgreementContext(PARTY_ID, null), 0, 1))
						.containsAll(getAgreementSpecificationIds(specificationModels)));
	}

	@Test
	public void testGetAgreementsFilteredByLifecycleStatus()
	{
		final List<AgrAgreementSpecificationModel> specificationModels = Arrays
				.asList(createAgrAgreementSpecification(MOON_AGREEMENT_SPECIFICATION_ID));
		Assert.assertEquals(specificationModels.size(),
				getAgrAgreementSpecificationService().getAgreementSpecifications(createAgreementContext(null, LIFECYCLE_STATUS), 0, 2)
						.size());
		Assert.assertTrue(getAgreementSpecificationIds(getAgrAgreementSpecificationService()
				.getAgreementSpecifications(createAgreementContext(null, LIFECYCLE_STATUS), 0, 2))
				.containsAll(getAgreementSpecificationIds(specificationModels)));
	}

	private AgrAgreementSpecificationModel createAgrAgreementSpecification(final String id)
	{
		final AgrAgreementSpecificationModel specificationModel = new AgrAgreementSpecificationModel();
		specificationModel.setId(id);
		return specificationModel;
	}

	private AgrAgreementContext createAgreementContext(final String partyId, final String lifecycleStatus)
	{
		final AgrAgreementContext agreementContext = new AgrAgreementContext();
		agreementContext.setRelatedPartyId(partyId);
		agreementContext.setLifecycleStatus(lifecycleStatus);
		return agreementContext;
	}

	private List<String> getAgreementSpecificationIds(final List<AgrAgreementSpecificationModel> agreementSpecificationModels)
	{
		final List<String> agreementSpecificationModelsIds = new ArrayList<>();
		agreementSpecificationModels.forEach(specificationModel -> agreementSpecificationModelsIds.add(specificationModel.getId()));
		return agreementSpecificationModelsIds;
	}


	public AgrAgreementSpecificationService getAgrAgreementSpecificationService()
	{
		return agrAgreementSpecificationService;
	}
}
