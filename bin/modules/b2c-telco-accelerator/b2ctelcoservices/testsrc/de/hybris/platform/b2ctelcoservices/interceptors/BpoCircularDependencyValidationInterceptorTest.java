/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.MockitoAnnotations.initMocks;


@UnitTest
public class BpoCircularDependencyValidationInterceptorTest
{

	private BpoCircularDependencyValidationInterceptor validationInterceptor;
	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
		Mockito.when(interceptorContext.isModified(Mockito.any())).thenReturn(false);
		validationInterceptor = new BpoCircularDependencyValidationInterceptor();
	}

	@Test
	public void testValidateCallWithBpoButNoAddedProductOfferings() throws Exception
	{
		whenValidateBpoWithoutModifiedPosNothingHappens();
	}

	@Test
	public void testValidateCallWithLastAddedPoAsSpo() throws Exception
	{
		TmaBundledProductOfferingModel validBpoWithLastAddedPoAsSpo = givenBpoWithLastAddedPoAsSpo();
		whenValidateIsCalled(validBpoWithLastAddedPoAsSpo);
		// then nothing happens
	}

	@Test
	public void testValidateHappyCase() throws Exception
	{
		TmaBundledProductOfferingModel validBpo = givenAValidBpo();
		whenValidateIsCalled(validBpo);
		// then nothing happens
	}

	@Test
	public void testValidateBpoWithRootCircularDependency()
	{
		TmaBundledProductOfferingModel rootBpo = givenABpoWithRootCircularDependencies();
		try
		{
			whenValidateIsCalled(rootBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateBpoWithDirectRootCircularDependency()
	{
		TmaBundledProductOfferingModel rootBpo = givenABpoWithDirectRootCircularDependencies();
		try
		{
			whenValidateIsCalled(rootBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateBpoWithChildCircularDependency()
	{
		TmaBundledProductOfferingModel rootBpo = givenABpoWithChildCircularDependencies();
		try
		{
			whenValidateIsCalled(rootBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateBpoWithSiblings() throws InterceptorException
	{
		TmaBundledProductOfferingModel rootBpo = givenABpoWithSiblings();
		whenValidateIsCalled(rootBpo);
		// then nothing happens
	}

	@Test
	public void testValidateBpoWithMultipleParentsAndCircularDependency()
	{
		TmaBundledProductOfferingModel rootBpo = givenABpoWithMultipleParentsAndCircularDependency();
		try
		{
			whenValidateIsCalled(rootBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateChildBpoReferencesParent()
	{
		TmaBundledProductOfferingModel rootBpo = givenChildBpoReferencingParent();
		try
		{
			whenValidateForParentIsCalled(rootBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateLastAddedParentBpoEqualsChild()
	{
		TmaBundledProductOfferingModel modifiedBpo = givenParentBpoEqualToChild();
		try
		{
			whenValidateForParentIsCalled(modifiedBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testBpoWithParentCircularDependency()
	{
		TmaBundledProductOfferingModel modifiedBpo = givenABpoWithChildCircularDependencies();
		try
		{
			whenValidateForParentIsCalled(modifiedBpo);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	private TmaBundledProductOfferingModel givenParentBpoEqualToChild()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo);
		rootBpo.setChildren(Collections.singleton(childBpo1));
		Set<TmaBundledProductOfferingModel> existingParents = new HashSet<>(Arrays.asList(childBpo1, rootBpo));
		childBpo1.setParents(existingParents);
		childBpo1.setChildren(Collections.singleton(childBpo1));

		return childBpo1;
	}

	private TmaBundledProductOfferingModel givenChildBpoReferencingParent()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo, rootBpo);
		rootBpo.setChildren(Collections.singleton(childBpo1));
		rootBpo.setParents(Collections.singleton(childBpo1));
		return childBpo1;
	}

	private TmaBundledProductOfferingModel givenABpoWithSiblings()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo);
		TmaBundledProductOfferingModel childBpo2 = generateBpoForCode("Child 2 Bpo", rootBpo, childBpo1);

		setChildBposToRoot(rootBpo, childBpo1, childBpo2);
		return rootBpo;
	}

	private TmaBundledProductOfferingModel givenABpoWithMultipleParentsAndCircularDependency()
	{
		TmaBundledProductOfferingModel rootBpo1 = generateBpoForCode("Root Bpo 1", null);
		TmaBundledProductOfferingModel rootBpo2 = generateBpoForCode("Root Bpo 2", null);
		TmaBundledProductOfferingModel childBpo1 = generateValidChildBpo1(null);
		childBpo1.setParents(new HashSet<>(Arrays.asList(rootBpo1, rootBpo2)));
		TmaBundledProductOfferingModel childBpo2 = generateBpoForCode("Child Bpo 2", rootBpo2, rootBpo1);
		TmaBundledProductOfferingModel childBpo12 = generateBpoForCode("Child Bpo 12", childBpo1, childBpo2);
		Set<TmaProductOfferingModel> childBpo1Pos = new HashSet<>(childBpo1.getChildren());
		childBpo1Pos.add(childBpo12);
		childBpo1.setChildren(childBpo1Pos);

		rootBpo1.setChildren(Collections.singleton(childBpo1));
		rootBpo2.setChildren(new HashSet<>(Arrays.asList(childBpo1, childBpo2)));

		return childBpo1;
	}

	private void whenValidateIsCalled(TmaBundledProductOfferingModel bpoToBeValidated) throws InterceptorException
	{
		Mockito.when(interceptorContext.isModified(bpoToBeValidated, TmaBundledProductOfferingModel.CHILDREN))
				.thenReturn(true);
		validationInterceptor.onValidate(bpoToBeValidated, interceptorContext);
	}

	private void whenValidateForParentIsCalled(TmaBundledProductOfferingModel bpoToBeValidated) throws InterceptorException
	{
		Mockito.when(interceptorContext.isModified(bpoToBeValidated, TmaBundledProductOfferingModel.PARENTS))
				.thenReturn(true);
		validationInterceptor.onValidate(bpoToBeValidated, interceptorContext);
	}


	private TmaBundledProductOfferingModel givenAValidBpo()
	{
		TmaBundledProductOfferingModel validRootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateValidChildBpo1(validRootBpo);

		TmaBundledProductOfferingModel childBpo2 = generateBpoForCode("Child 2 Bpo", validRootBpo);

		setChildBposToRoot(validRootBpo, childBpo1, childBpo2);
		return validRootBpo;
	}


	private TmaBundledProductOfferingModel givenBpoWithLastAddedPoAsSpo()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaSimpleProductOfferingModel childBpo = new TmaSimpleProductOfferingModel();
		Mockito.when(interceptorContext.isModified(childBpo)).thenReturn(true);
		rootBpo.setChildren(Collections.singleton(childBpo));
		return rootBpo;
	}

	private TmaBundledProductOfferingModel givenABpoWithDirectRootCircularDependencies()
	{
		TmaBundledProductOfferingModel rootBpo = generateBpoForCode("Root Bpo", null);
		rootBpo.setChildren(Collections.singleton(rootBpo));
		return rootBpo;
	}

	private TmaBundledProductOfferingModel givenABpoWithRootCircularDependencies()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo);
		TmaBundledProductOfferingModel childBpo2 = generateBpoForCode("Child 2 Bpo", rootBpo, rootBpo);
		setChildBposToRoot(rootBpo, childBpo1, childBpo2);
		return rootBpo;
	}

	private void setChildBposToRoot(TmaBundledProductOfferingModel rootBpo, TmaBundledProductOfferingModel childBpo1,
			TmaBundledProductOfferingModel childBpo2)
	{
		Set<TmaProductOfferingModel> childBpos = new HashSet<>();
		childBpos.add(childBpo1);
		childBpos.add(childBpo2);

		rootBpo.setChildren(childBpos);
	}

	private TmaBundledProductOfferingModel givenABpoWithChildCircularDependencies()
	{
		TmaBundledProductOfferingModel rootBpo = generateRootBpo();
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo);
		TmaBundledProductOfferingModel childBpo2 = generateBpoForCode("Child 2 Bpo", null, rootBpo);

		TmaBundledProductOfferingModel childBpo11 = generateBpoForCode("Child 11 Bpo", childBpo1, childBpo2);
		childBpo1.setChildren(Collections.singleton(childBpo11));
		childBpo2.setParents(Collections.singleton(childBpo11));
		rootBpo.setParents(Collections.singleton(childBpo2));

		setChildBposToRoot(rootBpo, childBpo2, childBpo1);
		return rootBpo;
	}

	private TmaBundledProductOfferingModel generateValidChildBpo1(TmaBundledProductOfferingModel rootBpo)
	{
		TmaBundledProductOfferingModel childBpo1 = generateBpoForCode("Child 1 Bpo", rootBpo);
		TmaBundledProductOfferingModel childBpo11 = generateBpoForCode("Child 11 Bpo", childBpo1);
		childBpo1.setChildren(Collections.singleton(childBpo11));
		return childBpo1;
	}


	private void whenValidateBpoWithoutModifiedPosNothingHappens() throws InterceptorException
	{
		TmaBundledProductOfferingModel modifiedBpo = new TmaBundledProductOfferingModel();
		Mockito.when(interceptorContext.isModified(modifiedBpo)).thenReturn(true);
		validationInterceptor.onValidate(modifiedBpo, interceptorContext);
	}

	private TmaBundledProductOfferingModel generateRootBpo()
	{
		return generateBpoForCode("Root Bpo", null);
	}


	private TmaBundledProductOfferingModel generateBpoForCode(String code, TmaBundledProductOfferingModel rootBpo,
			TmaBundledProductOfferingModel childBpo)
	{
		TmaBundledProductOfferingModel generatedBpo = generateBpoForCode(code, rootBpo);
		generatedBpo.setChildren(Collections.singleton(childBpo));
		return generatedBpo;
	}

	private TmaBundledProductOfferingModel generateBpoForCode(String code, TmaBundledProductOfferingModel rootBpo)
	{
		TmaBundledProductOfferingModel generatedBpo = new TmaBundledProductOfferingModel();
		generatedBpo.setCode(code);
		generatedBpo.setChildren(Collections.emptySet());
		generatedBpo.setParents(rootBpo == null ? Collections.emptySet() : Collections.singleton(rootBpo));
		return generatedBpo;
	}

	private void thenExceptionIsThrown(String expected, String errorMessage)
	{
		Assert.assertEquals("[" + validationInterceptor + "]:" + expected + "", errorMessage);
	}
}
