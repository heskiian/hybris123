/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.jalo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JUnit Tests for the B2ctelcoserviceabilityclient extension.
 */
public class B2ctelcoserviceabilityclientTest extends HybrisJUnit4TransactionalTest
{
	/**
	 * Edit the local|project.properties to change logging behaviour (properties log4j2.logger.*).
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(B2ctelcoserviceabilityclientTest.class);

	@Before
	public void setUp()
	{
		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void testB2ctelcoserviceabilityclient()
	{
		final boolean testTrue = true;
		assertThat(testTrue).isTrue();
	}
}
