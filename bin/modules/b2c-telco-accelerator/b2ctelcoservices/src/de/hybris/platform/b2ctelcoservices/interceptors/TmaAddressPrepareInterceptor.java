/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AddressModel}  that are newly created or cloned.
 *
 * @since 1911
 */
public class TmaAddressPrepareInterceptor implements PrepareInterceptor<AddressModel>
{
	private KeyGenerator keyGenerator;
	private TmaAddressService addressService;

	public TmaAddressPrepareInterceptor(final KeyGenerator keyGenerator, final TmaAddressService addressService)
	{
		this.keyGenerator = keyGenerator;
		this.addressService = addressService;
	}

	@Override
	public void onPrepare(final AddressModel addressModel, final InterceptorContext ctx)
	{
		if ((StringUtils.isEmpty(addressModel.getId())) || isAddressCreatedByOrder(addressModel, ctx))
		{
			addressModel.setId((String) getKeyGenerator().generate());
		}
	}

	/**
	 * @param addressModel
	 * 		the input Address
	 * @param ctx
	 * 		the InterceptorContext
	 * @return true if the address was created in a place order or create order flow; otherwise false.
	 */
	private boolean isAddressCreatedByOrder(final AddressModel addressModel, final InterceptorContext ctx)
	{
		final String addressId = addressModel.getId();
		return ctx.isNew(addressModel) && (getAddressService().doesAddressExist(addressId) || addressModel.getDuplicate());
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	protected TmaAddressService getAddressService()
	{
		return addressService;
	}
}
