/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.pagination;

import de.hybris.platform.b2ctelcofacades.pagination.impl.DefaultTmaPaginationFacade;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultTmaPaginationFacadeUnitTest
{
	   private static Integer DEFAULT_LIMIT=100;
		private TmaPaginationFacade tmaPaginationFacade;

		@Before
		public void setUp()
		{
			tmaPaginationFacade =new DefaultTmaPaginationFacade();
		}

		@Test
		public void testTotalNumberOfPages()
		{
			int noOfItems=25;
			int limit=5;
			int pages=noOfItems/limit;
			Assert.assertEquals((long) pages,(long) tmaPaginationFacade.getTotalNumberOfPages(noOfItems,limit));
		}

		@Test
		public void testOffsetIsNegative()
		{
			int offset=-1;
			Assert.assertEquals((long) 0,(long) tmaPaginationFacade.checkOffset(offset));
		}

		@Test
		public void testLimitOutOfBounds()
		{
			int limit1=-1;
			int limit2=DEFAULT_LIMIT+1;
			Assert.assertEquals("Testing when input limit is lower",(long)DEFAULT_LIMIT,(long) tmaPaginationFacade.checkLimit(limit1));
			Assert.assertEquals("Testing when input limit is higher",(long)DEFAULT_LIMIT,(long) tmaPaginationFacade.checkLimit(limit2));
		}

		@Test
		public void testListFiltering()
		{
			List<Integer> inputList=new ArrayList<>();
			int offset=20;
			int limit=30;
			for (int i=0;i<100;i++)
			{
				inputList.add(i);
			}

			List<Integer> outList= tmaPaginationFacade.filterListByOffsetAndLimit(offset,limit,inputList);
	      Assert.assertEquals(limit,outList.size());
	      Assert.assertEquals((long)offset,(long)outList.get(0));
	      Assert.assertEquals((long)offset+limit-1,(long)outList.get(outList.size()-1));
		}
}
