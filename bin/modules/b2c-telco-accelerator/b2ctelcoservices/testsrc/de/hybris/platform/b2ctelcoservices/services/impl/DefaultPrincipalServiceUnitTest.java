/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultPrincipalDao;
import de.hybris.platform.core.model.security.PrincipalModel;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;


@UnitTest
public class DefaultPrincipalServiceUnitTest
{
	private static final String UID = "uid";

	@Mock
	private DefaultPrincipalDao dao;

	private DefaultPrincipalService service;

	private PrincipalModel model;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		service = new DefaultPrincipalService();
		service.setPrincipalDao(dao);

		model = new PrincipalModel();
		model.setUid(UID);
	}

	@Test
	public void testFoundNothing(){
		when(dao.findPrincipalByUid(UID)).thenReturn(null);

		PrincipalModel expectedModel = service.findPrincipalByUid(UID);
		assertNull("Does not match expected result", expectedModel);
	}

	@Test
	public void testFoundOnePrincipal(){
		when(dao.findPrincipalByUid(UID)).thenReturn(model);

		PrincipalModel expectedModel = service.findPrincipalByUid(UID);
		assertTrue("The model has the expected uid", UID.equals(expectedModel.getUid()));
	}
}
