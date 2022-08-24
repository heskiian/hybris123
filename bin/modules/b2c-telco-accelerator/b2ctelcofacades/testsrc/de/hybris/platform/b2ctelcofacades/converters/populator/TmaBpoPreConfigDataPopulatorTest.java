/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBpoPreConfigData;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;



/**
 * Unit Test for {@link TmaBpoPreConfigDataPopulator}
 */
public class TmaBpoPreConfigDataPopulatorTest
{
	private TmaBpoPreConfigDataPopulator bpoPreConfigDataPopulator;
	private TmaBpoPreConfigModel source;
	private TmaBpoPreConfigData target;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		this.bpoPreConfigDataPopulator = new TmaBpoPreConfigDataPopulator();
		this.source = new TmaBpoPreConfigModel();
		this.target = new TmaBpoPreConfigData();
	}

	@Test
	public void testPopulateEntireSourceObject()
	{
		createSource();
		this.bpoPreConfigDataPopulator.populate(source, target);
		Assert.assertEquals(source.getCode(), target.getCode());
		Assert.assertEquals(source.getRootBpo().getCode(), target.getRootBpoCode());
		Assert.assertEquals(source.getPreConfigSpos().size(), target.getSpoList().size());
	}

	@Test
	public void testPopulateEmptySpoListAndRootBpo()
	{
		source.setCode("config_1");
		this.bpoPreConfigDataPopulator.populate(source, target);
		Assert.assertEquals(null, target.getSpoList());
		Assert.assertEquals(null, target.getRootBpoCode());
	}


	@Test
	public void testSourceAsNull()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter source can not be null");
		source = null;
		this.bpoPreConfigDataPopulator.populate(source, target);
	}


	private void createSource()
	{
		source.setCode("config_1");
		final TmaBundledProductOfferingModel bpoModel = new TmaBundledProductOfferingModel();
		source.setRootBpo(bpoModel);
		final Set<TmaSimpleProductOfferingModel> spoList = new HashSet<>();
		final TmaSimpleProductOfferingModel spo_1 = new TmaSimpleProductOfferingModel();
		spo_1.setCode("Iphone_6");
		spoList.add(spo_1);
		final TmaSimpleProductOfferingModel spo_2 = new TmaSimpleProductOfferingModel();
		spo_1.setCode("Iphone_7");
		spoList.add(spo_1);
		source.setPreConfigSpos(spoList);
	}
}
