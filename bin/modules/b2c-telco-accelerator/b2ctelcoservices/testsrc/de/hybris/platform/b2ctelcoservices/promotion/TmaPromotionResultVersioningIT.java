package de.hybris.platform.b2ctelcoservices.promotion;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotionengineservices.versioning.PromotionResultVersioningIT;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;


/**
 * Overrides {@link PromotionResultVersioningIT} tests.
 *
 * @since 1911
 */
@IntegrationTest(replaces = PromotionResultVersioningIT.class)
public class TmaPromotionResultVersioningIT extends PromotionResultVersioningIT
{
	@Resource
	private CurrencyDao currencyDao;
	@Resource
	private CartService cartService;

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		final List<CurrencyModel> currencyList = currencyDao.findCurrenciesByCode("EUR");

		CartModel cart = cartService.getSessionCart();
		cart.setCurrency(currencyList.get(0));
	}
}
