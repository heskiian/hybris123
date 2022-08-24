/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@UnitTest
public class DefaultTmaRetrieveUserPriceGroupsStrategyTest
{
	private static final String TEST_USER_GROUP = "TEST_USER_GROUP";
	private static final String TEST_USER_GROUP2 = "TEST_USER_GROUP2";
	private static final String TEST_USER_GROUP3 = "TEST_USER_GROUP3";

	private UserPriceGroup priceGroup1;
	private UserPriceGroup priceGroup2;
	private UserPriceGroup priceGroup3;

	private DefaultTmaRetrieveUserPriceGroupsStrategy strategy;

	@Before
	public void setUp()
	{
		strategy = new DefaultTmaRetrieveUserPriceGroupsStrategy();
		priceGroup1 = UserPriceGroup.valueOf(TEST_USER_GROUP);
		priceGroup2 = UserPriceGroup.valueOf(TEST_USER_GROUP2);
		priceGroup3 = UserPriceGroup.valueOf(TEST_USER_GROUP3);
	}

	@Test
	public void testRetrieveUserPriceGroupWhenEmpty()
	{
		//given
		final UserModel user = createUser(null);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertion
		assertEquals("There should be no user price groups returned.", 0, returnedUserPriceGroups.size());
	}

	@Test
	public void testRetrieveUserPriceGroupConfiguredOnUser()
	{
		//given
		final UserModel user = createUser(priceGroup1);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertion
		assertEquals("There should be one user price group returned.", 1, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
	}

	@Test
	public void testRetrieveUserPriceGroupFromUserGroup()
	{
		//given
		final UserGroupModel group1 = createUserGroup(priceGroup1);

		final UserModel user = createUser(null, group1);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertions
		assertEquals("There should be one user price group returned.", 1, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
	}

	@Test
	public void testRetrieveUserPriceGroupsFromBothGroupAndUser()
	{
		//given
		final UserGroupModel group1 = createUserGroup(priceGroup2);

		final UserModel user = createUser(priceGroup1, group1);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertions
		assertEquals("There should be two user price groups returned.", 2, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup2));
	}

	@Test
	public void testRetrieveUserPriceGroupsFromTwoUserGroupsAndUser()
	{
		//given
		final UserGroupModel group1 = createUserGroup(priceGroup2);
		final UserGroupModel group2 = createUserGroup(priceGroup3);

		final UserModel user = createUser(priceGroup1, group1, group2);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertions
		assertEquals("There should be three user price groups returned.", 3, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup2));
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup3));
	}

	@Test
	public void testRetrieveUserPriceGroupsFromTwoHierarchyUserGroups()
	{
		//given
		final UserGroupModel group1 = createUserGroup(priceGroup1);
		final UserGroupModel group2 = createUserGroup(priceGroup2, group1);

		final UserModel user = createUser(null, group1, group2);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertions
		assertEquals("There should be two user price groups returned.", 2, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup2));
	}

	@Test
	public void testRetrieveUserPriceGroupsFromThreeHierarchyUserGroupsAndUser()
	{
		//given
		final UserGroupModel group1 = createUserGroup(null);
		final UserGroupModel group2 = createUserGroup(null, group1);
		final UserGroupModel group3 = createUserGroup(priceGroup2, group2);

		final UserModel user = createUser(priceGroup1, group1, group2, group3);

		//when
		final Set<UserPriceGroup> returnedUserPriceGroups = strategy.getAllUserPriceGroups(user);

		//assertions
		assertEquals("There should be two user price groups returned.", 2, returnedUserPriceGroups.size());
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup1));
		assertTrue("The correct user price group should have been returned.",returnedUserPriceGroups.contains(priceGroup2));
	}

	private UserModel createUser(final UserPriceGroup userPriceGroup, final PrincipalGroupModel ...childGroupsParam)
	{
		final Set<PrincipalGroupModel> childGroups = new HashSet<>(Arrays.asList(childGroupsParam));

		final UserModel user = new UserModel();

		user.setGroups(childGroups);
		user.setEurope1PriceFactory_UPG(userPriceGroup);

		return user;
	}

	private UserGroupModel createUserGroup(final UserPriceGroup userPriceGroup, final PrincipalGroupModel ...childGroupsParam)
	{
		final Set<PrincipalGroupModel> childGroups = new HashSet<>(Arrays.asList(childGroupsParam));

		final UserGroupModel group = new UserGroupModel();

		group.setGroups(childGroups);
		group.setUserPriceGroup(userPriceGroup);

		return group;
	}
}
