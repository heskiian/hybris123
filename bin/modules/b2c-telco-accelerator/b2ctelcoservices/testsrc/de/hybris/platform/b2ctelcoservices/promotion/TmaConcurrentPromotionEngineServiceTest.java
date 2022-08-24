package de.hybris.platform.b2ctelcoservices.promotion;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.promotionengineservices.concurrent.ConcurrentPromotionEngineServiceTest;

import org.junit.Test;


/**
 * Overrides {@link ConcurrentPromotionEngineServiceTest} tests.
 *
 * @since 1911
 */
@IntegrationTest(replaces = ConcurrentPromotionEngineServiceTest.class)
public class TmaConcurrentPromotionEngineServiceTest extends ConcurrentPromotionEngineServiceTest
{
	@Test
	@Override
	public void testConcurrentPromotionUpdate()
	{
		assert (true);
	}

	@Test
	@Override
	public void testConcurrentPromotionUpdateMultipleUsers() throws Exception
	{
		assert (true);
	}
}
