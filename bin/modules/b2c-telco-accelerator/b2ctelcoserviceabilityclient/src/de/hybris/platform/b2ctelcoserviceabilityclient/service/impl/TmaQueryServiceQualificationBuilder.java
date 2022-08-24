/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;

import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceQualificationItem;


/**
 * Builder class for the creation of the {@link QueryServiceQualification}
 *
 * @since 2102
 *
 */
public class TmaQueryServiceQualificationBuilder
{

	private String description;
	private Boolean instantSyncQualification;
	private ServiceQualificationItem searchCriteria;
	private String type;

	public QueryServiceQualification build()
	{
		final QueryServiceQualification queryServiceQualification = new QueryServiceQualification();
		queryServiceQualification.setDescription(description);
		queryServiceQualification.setAttype(type);
		queryServiceQualification.setInstantSyncQualification(instantSyncQualification);
		queryServiceQualification.setSearchCriteria(searchCriteria);
		return queryServiceQualification;

	}

	public static TmaQueryServiceQualificationBuilder newTmaQueryServiceQualificationBuilder()
	{
		return new TmaQueryServiceQualificationBuilder();
	}

	public TmaQueryServiceQualificationBuilder withDescription(final String description)
	{
		this.description = description;
		return this;
	}

	public TmaQueryServiceQualificationBuilder withInstantSyncQualification(final Boolean instantSyncQualification)
	{
		this.instantSyncQualification = instantSyncQualification;
		return this;
	}

	public TmaQueryServiceQualificationBuilder withSearchCriteria(final ServiceQualificationItem searchCriteria)
	{
		this.searchCriteria = searchCriteria;
		return this;
	}

	public TmaQueryServiceQualificationBuilder withType(final String type)
	{
		this.type = type;
		return this;
	}
}

