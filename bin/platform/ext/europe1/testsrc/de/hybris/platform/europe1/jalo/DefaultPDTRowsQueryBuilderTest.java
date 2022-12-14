/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.europe1.jalo;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder.QueryWithParams;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;


@UnitTest
public class DefaultPDTRowsQueryBuilderTest
{
	private static final String TEST_TYPE = PriceRowModel._TYPECODE;
	private static final PK TEST_PRODUCT_PK = PK.fromLong(123456);
	private static final PK TEST_PRODUCT_GROUP_PK = PK.fromLong(234567);
	private static final String TEST_PRODUCT_ID = "test_product";
	private static final PK TEST_USER_PK = PK.fromLong(345678);
	private static final PK TEST_USER_GROUP_PK = PK.fromLong(456789);

	private final PropertyConfigSwitcher userProductIdLegacySwitcher = new PropertyConfigSwitcher(
			DefaultPDTRowsQueryBuilder.USE_LEGACY_PRODUCTID_QUERY_STRATEGY);
	
	private PDTRowsQueryBuilder builder;

	@Before
	public void setUp()
	{
		builder = new DefaultPDTRowsQueryBuilder(TEST_TYPE);
		userProductIdLegacySwitcher.switchToValue("false");
	}

	@After
	public void cleanUp()
	{
		userProductIdLegacySwitcher.switchBackToDefault();
	}
	
	@Test
	public void shouldGenerateProperQueryWhenNoParametersAreGiven()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of();
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow}";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyProduct()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyProduct",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?anyProduct)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct().build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithProduct()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("product", TEST_PRODUCT_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?product)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withProduct(TEST_PRODUCT_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithProductGroup()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("productGroup",
				TEST_PRODUCT_GROUP_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?productGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withProductGroup(TEST_PRODUCT_GROUP_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithProductId()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("matchByProductId",
				Long.valueOf(
						Europe1PriceFactory.MATCH_BY_PRODUCT_ID),
				"productId", TEST_PRODUCT_ID);
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier}=?matchByProductId and {productId}=?productId";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withProductId(TEST_PRODUCT_ID).build();

		assertThat(result).isNotNull();

		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams);

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyUser()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyUser",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?anyUser)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyUser().build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithUser()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("user", TEST_USER_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?user)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withUser(TEST_USER_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithUserGroup()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("userGroup", TEST_USER_GROUP_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?userGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withUserGroup(TEST_USER_GROUP_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyProductAndGivenProduct()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyProduct",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY),
				"product", TEST_PRODUCT_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?anyProduct, ?product)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct().withProduct(TEST_PRODUCT_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyProductGivenProductAndGivenProductGroup()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyProduct",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY),
				"product", TEST_PRODUCT_PK.getLong(),
				"productGroup",
				TEST_PRODUCT_GROUP_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?anyProduct, ?product, ?productGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct()
		                                      .withProduct(TEST_PRODUCT_PK)
		                                      .withProductGroup(TEST_PRODUCT_GROUP_PK)
		                                      .build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyUserAndGivenUser()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyUser",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY),
				"user", TEST_USER_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?anyUser, ?user)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyUser().withUser(TEST_USER_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyUserGivenUserAndGivenUserGroup()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyUser",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY),
				"user", TEST_USER_PK.getLong(), "userGroup",
				TEST_USER_GROUP_PK.getLong());
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?anyUser, ?user, ?userGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyUser().withUser(TEST_USER_PK).withUserGroup(TEST_USER_GROUP_PK).build();

		assertThat(result).isNotNull().extracting(QueryWithParams::getQuery, QueryWithParams::getParams).contains(expectedQuery,
				expectedParams);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyProductGivenProductAndGivenProductIdLegacy()
	{
		userProductIdLegacySwitcher.switchToValue("true");
		
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object>of("anyProduct",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY),
				"product", TEST_PRODUCT_PK.getLong(),
				"matchByProductId",
				Long.valueOf(
						Europe1PriceFactory.MATCH_BY_PRODUCT_ID),
				"productId", TEST_PRODUCT_ID);
		final String expectedQuery = "select x.col_PK from ({{select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?anyProduct, ?product)}} UNION {{select {PK} AS col_PK from {PriceRow} where {productMatchQualifier}=?matchByProductId and {productId}=?productId}}) x";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct()
		                                      .withProduct(TEST_PRODUCT_PK)
		                                      .withProductId(TEST_PRODUCT_ID)
		                                      .build();

		assertThat(result).isNotNull();

		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams);

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithAnyProductGivenProductAndGivenProductId()
	{
		final Map<String, Object> expectedParams = ImmutableMap.<String, Object> of("anyProduct",
				Long.valueOf(Europe1PriceFactory.MATCH_ANY), "product", TEST_PRODUCT_PK.getLong(), "matchByProductId",
				Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID), "productId", TEST_PRODUCT_ID);

		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where ( {productMatchQualifier} in (?anyProduct, ?product) or ( {productMatchQualifier}=?matchByProductId and {productId}=?productId ))";

		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams, Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct()
															.withProduct(TEST_PRODUCT_PK)
															.withProductId(TEST_PRODUCT_ID)
															.build();

		assertThat(result).isNotNull();

		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams);

		assertThat(result).isEqualTo(expectedResult);
	}
	
	@Test
	public void shouldGenerateProperQueryWithAllParametersGivenLegacy()
	{
		userProductIdLegacySwitcher.switchToValue("true");
		
		final Builder<String, Object> expectedParams = ImmutableMap.<String, Object>builder();
		expectedParams.put("anyProduct", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		expectedParams.put("product", TEST_PRODUCT_PK.getLong());
		expectedParams.put("productGroup", TEST_PRODUCT_GROUP_PK.getLong());
		expectedParams.put("anyUser", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		expectedParams.put("user", TEST_USER_PK.getLong());
		expectedParams.put("userGroup", TEST_USER_GROUP_PK.getLong());
		expectedParams.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
		expectedParams.put("productId", TEST_PRODUCT_ID);
		final String expectedQuery = "select x.col_PK from ({{select {PK} AS col_PK from {PriceRow} where {productMatchQualifier} in (?anyProduct, ?product, ?productGroup) and {userMatchQualifier} in (?anyUser, ?user, ?userGroup)}} UNION {{select {PK} AS col_PK from {PriceRow} where {productMatchQualifier}=?matchByProductId and {productId}=?productId and {userMatchQualifier} in (?anyUser, ?user, ?userGroup)}}) x";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams.build(),
				Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct()
		                                      .withAnyUser()
		                                      .withProduct(TEST_PRODUCT_PK)
		                                      .withProductGroup(TEST_PRODUCT_GROUP_PK)
		                                      .withProductId(TEST_PRODUCT_ID)
		                                      .withAnyUser()
		                                      .withUser(TEST_USER_PK)
		                                      .withUserGroup(TEST_USER_GROUP_PK)
		                                      .build();

		assertThat(result).isNotNull();

		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams.build());

		assertThat(result).isEqualTo(expectedResult);
	}
	
	@Test
	public void shouldGenerateProperQueryWithAllParametersGiven()
	{
		final Builder<String, Object> expectedParams = ImmutableMap.<String, Object> builder();
		expectedParams.put("anyProduct", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		expectedParams.put("product", TEST_PRODUCT_PK.getLong());
		expectedParams.put("productGroup", TEST_PRODUCT_GROUP_PK.getLong());
		expectedParams.put("anyUser", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		expectedParams.put("user", TEST_USER_PK.getLong());
		expectedParams.put("userGroup", TEST_USER_GROUP_PK.getLong());
		expectedParams.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
		expectedParams.put("productId", TEST_PRODUCT_ID);

		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where ( {productMatchQualifier} in (?anyProduct, ?product, ?productGroup) or ( {productMatchQualifier}=?matchByProductId and {productId}=?productId )) and {userMatchQualifier} in (?anyUser, ?user, ?userGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams.build(), Collections.emptyList());

		final QueryWithParams result = builder.withAnyProduct()
															.withAnyUser()
															.withProduct(TEST_PRODUCT_PK)
															.withProductGroup(TEST_PRODUCT_GROUP_PK)
															.withProductId(TEST_PRODUCT_ID)
															.withAnyUser()
															.withUser(TEST_USER_PK)
															.withUserGroup(TEST_USER_GROUP_PK)
															.build();

		assertThat(result).isNotNull();

		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams.build());

		assertThat(result).isEqualTo(expectedResult);
	}
	
	@Test
	public void shouldGenerateProperQueryWithUserGroupGivenAndGivenProductId()
	{
		final Builder<String, Object> expectedParams = ImmutableMap.<String, Object> builder();
		expectedParams.put("userGroup", TEST_USER_GROUP_PK.getLong());
		expectedParams.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
		expectedParams.put("productId", TEST_PRODUCT_ID);
		final String expectedQuery = "select {PK} AS col_PK from {PriceRow} where {productMatchQualifier}=?matchByProductId and {productId}=?productId and {userMatchQualifier} in (?userGroup)";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams.build(), Collections.emptyList());

		final QueryWithParams result = builder//
				.withProductId(TEST_PRODUCT_ID) //
				.withUserGroup(TEST_USER_GROUP_PK).build();

		assertThat(result).isNotNull();
		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams.build());

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void shouldGenerateProperQueryWithUserGroupGivenAndGivenProductIdLegacy()
	{
		userProductIdLegacySwitcher.switchToValue("true");

		final Builder<String, Object> expectedParams = ImmutableMap.<String, Object> builder();
		expectedParams.put("userGroup", TEST_USER_GROUP_PK.getLong());
		expectedParams.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
		expectedParams.put("productId", TEST_PRODUCT_ID);
		final String expectedQuery = "select x.col_PK from ({{select {PK} AS col_PK from {PriceRow} where {userMatchQualifier} in (?userGroup)}} UNION {{select {PK} AS col_PK from {PriceRow} where {productMatchQualifier}=?matchByProductId and {productId}=?productId and {userMatchQualifier} in (?userGroup)}}) x";
		final QueryWithParams expectedResult = new QueryWithParams(expectedQuery, expectedParams.build(), Collections.emptyList());

		final QueryWithParams result = builder//
				.withProductId(TEST_PRODUCT_ID) //
				.withUserGroup(TEST_USER_GROUP_PK).build();

		assertThat(result).isNotNull();
		assertThat(result.getQuery()).isEqualTo(expectedQuery);
		assertThat(result.getParams()).isEqualTo(expectedParams.build());

		assertThat(result).isEqualTo(expectedResult);
	}
}
