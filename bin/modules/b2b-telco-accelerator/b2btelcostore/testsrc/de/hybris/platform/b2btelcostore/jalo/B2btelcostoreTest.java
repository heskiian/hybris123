/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcostore.jalo;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * JUnit Tests for the B2btelcostore extension
 */
public class B2btelcostoreTest extends HybrisJUnit4TransactionalTest
{
	/**
	 * Edit the local|project.properties to change logging behaviour (properties log4j.*).
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(B2btelcostoreTest.class.getName());

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
	public void testB2btelcostore()
	{
		final boolean testTrue = true;
		assertTrue("true is not true", testTrue);
	}
}
