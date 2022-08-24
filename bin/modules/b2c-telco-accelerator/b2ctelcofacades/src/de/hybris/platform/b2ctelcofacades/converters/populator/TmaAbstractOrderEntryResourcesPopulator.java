/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderEntryPscvData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populator implementation for {@link de.hybris.platform.core.model.order.AbstractOrderEntryModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.OrderEntryData} as target type, handles resources value
 * information.
 *
 * @since 1911
 */
public class TmaAbstractOrderEntryResourcesPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	private final Converter<TmaAbstractOrderEntryPscvModel, TmaAbstractOrderEntryPscvData> tmaAbstractOrderEntryPscvConverter;
	private final Converter<AddressModel, AddressData> addressConverter;
	private final Converter<RegionModel, RegionData> regionConverter;

	public TmaAbstractOrderEntryResourcesPopulator(
			final Converter<TmaAbstractOrderEntryPscvModel, TmaAbstractOrderEntryPscvData> tmaAbstractOrderEntryPscvConverter,
			final Converter<AddressModel, AddressData> addressConverter, final Converter<RegionModel, RegionData> regionConverter)
	{
		this.tmaAbstractOrderEntryPscvConverter = tmaAbstractOrderEntryPscvConverter;
		this.addressConverter = addressConverter;
		this.regionConverter = regionConverter;
	}

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		target.setProductSpecCharacteristics(
				new HashSet<>(getTmaAbstractOrderEntryPscvConverter().convertAll(source.getProductSpecCharacteristicValues())));
		addAppointmentId(source, target);
		addInstallationAddress(source, target);
		addRegion(source, target);
		addServiceProvider(source, target);
	}

	private void addServiceProvider(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (StringUtils.isNotBlank(orderEntry.getServiceProvider()))
		{
			entry.setServiceProvider(orderEntry.getServiceProvider());
		}
	}

	protected void addAppointmentId(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getAppointmentReference() != null)
		{
			entry.setAppointmentId(orderEntry.getAppointmentReference());
		}
		else
		{
			entry.setAppointmentId(StringUtils.EMPTY);
		}
	}

	protected void addInstallationAddress(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getInstallationAddress() != null)
		{
			entry.setInstallationAddress(getAddressConverter().convert(orderEntry.getInstallationAddress()));
		}
	}

	protected void addRegion(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (!ObjectUtils.isEmpty(orderEntry.getRegion()))
		{
			entry.setRegion(getRegionConverter().convert(orderEntry.getRegion()));
		}
	}

	protected Converter<TmaAbstractOrderEntryPscvModel, TmaAbstractOrderEntryPscvData> getTmaAbstractOrderEntryPscvConverter()
	{
		return tmaAbstractOrderEntryPscvConverter;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	protected Converter<RegionModel, RegionData> getRegionConverter()
	{
		return regionConverter;
	}
}
