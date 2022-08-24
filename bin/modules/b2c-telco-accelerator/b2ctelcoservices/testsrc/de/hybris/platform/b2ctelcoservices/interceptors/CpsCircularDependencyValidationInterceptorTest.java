/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.MockitoAnnotations.initMocks;


@UnitTest
public class CpsCircularDependencyValidationInterceptorTest
{

	private CpsCircularDependencyValidationInterceptor validationInterceptor;
	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
		Mockito.when(interceptorContext.isModified(Mockito.any())).thenReturn(false);
		validationInterceptor = new CpsCircularDependencyValidationInterceptor();
	}

	@Test
	public void testValidateCallWithCpsButNoAddedProductSpecification() throws Exception
	{
		whenValidateCpsWithoutModifiedPsNothingHappens();
	}

	@Test
	public void testValidateCallWithLastAddedPs() throws Exception
	{
		TmaCompositeProductSpecificationModel validCpsWithLastAddedPs = givenPcsWithLastAddedPoAsSpo();
		whenValidateIsCalled(validCpsWithLastAddedPs);
		// then nothing happens
	}

	@Test
	public void testValidateHappyCase() throws Exception
	{
		TmaCompositeProductSpecificationModel validCps = givenAValidCps();
		whenValidateIsCalled(validCps);
		// then nothing happens
	}

	@Test
	public void testValidatePcsWithRootCircularDependency()
	{
		TmaCompositeProductSpecificationModel rootCps = givenAPcsWithRootCircularDependencies();
		try
		{
			whenValidateIsCalled(rootCps);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidatePcsWithDirectRootCircularDependency()
	{
		TmaCompositeProductSpecificationModel rootPcs = givenAPcsWithDirectRootCircularDependencies();
		try
		{
			whenValidateIsCalled(rootPcs);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidatePcsWithChildCircularDependency()
	{
		TmaCompositeProductSpecificationModel rootPcs = givenAPcsWithChildCircularDependencies();
		try
		{
			whenValidateIsCalled(rootPcs);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);

		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidatePcsWithSiblings() throws InterceptorException
	{
		TmaCompositeProductSpecificationModel rootPcs = givenAPcsWithSiblings();
		whenValidateIsCalled(rootPcs);
		// then nothing happens
	}

	@Test
	public void testValidateCpsWithMultipleParentsAndCircularDependency()
	{
		TmaCompositeProductSpecificationModel rootCps = givenAPcsWithMultipleParentsAndCircularDependency();
		try
		{
			whenValidateIsCalled(rootCps);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateChildCpsReferencesParent()
	{
		TmaCompositeProductSpecificationModel rootCps = givenChildPcsReferencingParent();
		try
		{
			whenValidateForParentIsCalled(rootCps);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testValidateLastAddedParentPcsEqualsChild()
	{
		TmaCompositeProductSpecificationModel modifiedPcs = givenParentPcsEqualToChild();
		try
		{
			whenValidateForParentIsCalled(modifiedPcs);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	@Test
	public void testCpsWithParentCircularDependency()
	{
		TmaCompositeProductSpecificationModel modifiedCps = givenAPcsWithChildCircularDependencies();
		try
		{
			whenValidateForParentIsCalled(modifiedCps);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException e)
		{
			thenExceptionIsThrown("Cannot save current configuration as it contains a cyclic dependency: "
					+ "Found duplicate Item reference", e.getMessage());
		}
	}

	private TmaCompositeProductSpecificationModel givenParentPcsEqualToChild()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateCpsForCode("Child 1 Pcs", rootPcs);
		rootPcs.setChildren(Collections.singleton(childPcs1));
		Set<TmaCompositeProductSpecificationModel> existingParents = new HashSet<>(Arrays.asList(childPcs1, rootPcs));
		childPcs1.setParents(existingParents);
		childPcs1.setChildren(Collections.singleton(childPcs1));

		return childPcs1;
	}

	private TmaCompositeProductSpecificationModel givenChildPcsReferencingParent()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateCpsForCode("Child 1 Pcs", rootPcs, rootPcs);
		rootPcs.setChildren(Collections.singleton(childPcs1));
		rootPcs.setParents(Collections.singleton(childPcs1));
		return childPcs1;
	}

	private TmaCompositeProductSpecificationModel givenAPcsWithSiblings()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateCpsForCode("Child 1 Pcs", rootPcs);
		TmaCompositeProductSpecificationModel childPcs2 = generateCpsForCode("Child 2 Pcs", rootPcs, childPcs1);

		setChildPcsToRoot(rootPcs, childPcs1, childPcs2);
		return rootPcs;
	}

	private TmaCompositeProductSpecificationModel givenAPcsWithMultipleParentsAndCircularDependency()
	{
		TmaCompositeProductSpecificationModel rootPcs1 = generateCpsForCode("Root Pcs 1", null);
		TmaCompositeProductSpecificationModel rootPcs2 = generateCpsForCode("Root Pcs 2", null);
		TmaCompositeProductSpecificationModel childPcs1 = generateValidChildCps1(null);
		childPcs1.setParents(new HashSet<>(Arrays.asList(rootPcs1, rootPcs2)));
		TmaCompositeProductSpecificationModel childPcs2 = generateCpsForCode("Child Pcs 2", rootPcs2, rootPcs1);
		TmaCompositeProductSpecificationModel childPcs12 = generateCpsForCode("Child Pcs 12", childPcs1, childPcs2);
		Set<TmaProductSpecificationModel> childPcs1Pos = new HashSet<>(childPcs1.getChildren());
		childPcs1Pos.add(childPcs12);
		childPcs1.setChildren(childPcs1Pos);

		rootPcs1.setChildren(Collections.singleton(childPcs1));
		rootPcs2.setChildren(new HashSet<>(Arrays.asList(childPcs1, childPcs2)));

		return childPcs1;
	}

	private void whenValidateIsCalled(TmaCompositeProductSpecificationModel cpsToBeValidated) throws InterceptorException
	{
		Mockito.when(interceptorContext.isModified(cpsToBeValidated, TmaCompositeProductSpecificationModel.CHILDREN))
				.thenReturn(true);
		validationInterceptor.onValidate(cpsToBeValidated, interceptorContext);
	}

	private void whenValidateForParentIsCalled(TmaCompositeProductSpecificationModel cpsToBeValidated) throws InterceptorException
	{
		Mockito.when(interceptorContext.isModified(cpsToBeValidated, TmaCompositeProductSpecificationModel.PARENTS))
				.thenReturn(true);
		validationInterceptor.onValidate(cpsToBeValidated, interceptorContext);
	}


	private TmaCompositeProductSpecificationModel givenAValidCps()
	{
		TmaCompositeProductSpecificationModel validRootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateValidChildCps1(validRootPcs);

		TmaCompositeProductSpecificationModel childPcs2 = generateCpsForCode("Child 2 Pcs", validRootPcs);

		setChildPcsToRoot(validRootPcs, childPcs1, childPcs2);
		return validRootPcs;
	}


	private TmaCompositeProductSpecificationModel givenPcsWithLastAddedPoAsSpo()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaProductSpecificationModel childPcs = new TmaProductSpecificationModel();
		Mockito.when(interceptorContext.isModified(childPcs)).thenReturn(true);
		rootPcs.setChildren(Collections.singleton(childPcs));
		return rootPcs;
	}

	private TmaCompositeProductSpecificationModel givenAPcsWithDirectRootCircularDependencies()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateCpsForCode("Root Pcs", null);
		rootPcs.setChildren(Collections.singleton(rootPcs));
		return rootPcs;
	}

	private TmaCompositeProductSpecificationModel givenAPcsWithRootCircularDependencies()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateCpsForCode("Child 1 Pcs", rootPcs);
		TmaCompositeProductSpecificationModel childPcs2 = generateCpsForCode("Child 2 Pcs", rootPcs, rootPcs);
		setChildPcsToRoot(rootPcs, childPcs1, childPcs2);
		return rootPcs;
	}

	private void setChildPcsToRoot(TmaCompositeProductSpecificationModel rootPcs, TmaCompositeProductSpecificationModel childPcs1,
			TmaCompositeProductSpecificationModel childPcs2)
	{
		Set<TmaProductSpecificationModel> childPcs = new HashSet<>();
		childPcs.add(childPcs1);
		childPcs.add(childPcs2);

		rootPcs.setChildren(childPcs);
	}

	private TmaCompositeProductSpecificationModel givenAPcsWithChildCircularDependencies()
	{
		TmaCompositeProductSpecificationModel rootPcs = generateRootCps();
		TmaCompositeProductSpecificationModel childPcs1 = generateCpsForCode("Child 1 Pcs", rootPcs);
		TmaCompositeProductSpecificationModel childPcs2 = generateCpsForCode("Child 2 Pcs", null, rootPcs);

		TmaCompositeProductSpecificationModel childPcs11 = generateCpsForCode("Child 11 Pcs", childPcs1, childPcs2);
		childPcs1.setChildren(Collections.singleton(childPcs11));
		childPcs2.setParents(Collections.singleton(childPcs11));
		rootPcs.setParents(Collections.singleton(childPcs2));

		setChildPcsToRoot(rootPcs, childPcs2, childPcs1);
		return rootPcs;
	}

	private TmaCompositeProductSpecificationModel generateValidChildCps1(TmaCompositeProductSpecificationModel rootCps)
	{
		TmaCompositeProductSpecificationModel childCps1 = generateCpsForCode("Child 1 Cps", rootCps);
		TmaCompositeProductSpecificationModel childCps11 = generateCpsForCode("Child 11 Cps", childCps1);
		childCps1.setChildren(Collections.singleton(childCps11));
		return childCps1;
	}


	private void whenValidateCpsWithoutModifiedPsNothingHappens() throws InterceptorException
	{
		TmaCompositeProductSpecificationModel modifiedCps = new TmaCompositeProductSpecificationModel();
		Mockito.when(interceptorContext.isModified(modifiedCps)).thenReturn(true);
		validationInterceptor.onValidate(modifiedCps, interceptorContext);
	}

	private TmaCompositeProductSpecificationModel generateRootCps()
	{
		return generateCpsForCode("Root Cps", null);
	}


	private TmaCompositeProductSpecificationModel generateCpsForCode(String code, TmaCompositeProductSpecificationModel rootCps,
			TmaCompositeProductSpecificationModel childCps)
	{
		TmaCompositeProductSpecificationModel generatedCps = generateCpsForCode(code, rootCps);
		generatedCps.setChildren(Collections.singleton(childCps));
		return generatedCps;
	}

	private TmaCompositeProductSpecificationModel generateCpsForCode(String code, TmaCompositeProductSpecificationModel rootCps)
	{
		TmaCompositeProductSpecificationModel generatedPcs = new TmaCompositeProductSpecificationModel();
		generatedPcs.setId(code);
		generatedPcs.setChildren(Collections.emptySet());
		generatedPcs.setParents(rootCps == null ? Collections.emptySet() : Collections.singleton(rootCps));
		return generatedPcs;
	}

	private void thenExceptionIsThrown(String expected, String errorMessage)
	{
		Assert.assertEquals("[" + validationInterceptor + "]:" + expected + "", errorMessage);
	}
}
