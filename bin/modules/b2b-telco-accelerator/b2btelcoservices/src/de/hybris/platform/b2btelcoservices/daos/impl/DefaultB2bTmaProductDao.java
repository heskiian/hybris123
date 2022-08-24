/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaProductDao;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultTmaProductDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link TmaProductDao}
 *
 * @since 2105
 */
public class DefaultB2bTmaProductDao extends DefaultTmaProductDao
{
	private static final String APPLY_ALLOWED_PRINCIPAL_FLAG = "applyAllowedPrincipalFlag";

	private static final String PRODUCT_OFFERING_QUERY = "SELECT {p:" + ProductModel.PK + "}" + "FROM {" + ProductModel._TYPECODE
			+ " AS p }" + " WHERE" + " ({p:" + ProductModel.PK + "} IN " + " ({{SELECT {pr:" + ProductModel.PK + "}" + " FROM {"
			+ ProductModel._TYPECODE + " AS pr} WHERE (NOT EXISTS ({{SELECT {cr.source} from "
			+ "{ProductOffering2PrincipalRelation as cr} WHERE ({cr.source} = {pr.pk} )}}))" + " AND {pr:" + ProductModel.CODE
			+ "}=?" + ProductModel.CODE + "}}))" + " OR ({p:" + ProductModel.PK + "}" + " IN ({{SELECT {po:" + ProductModel.PK + "}"
			+ " FROM {" + ProductModel._TYPECODE + " AS po  JOIN ProductOffering2PrincipalRelation AS r ON {po.pk}={r.source} "
			+ " JOIN " + PrincipalModel._TYPECODE + " as a ON {r.target}={a.pk}} WHERE {po:" + ProductModel.CODE + "}=?"
			+ ProductModel.CODE + " AND {a :" + PrincipalModel.UID + "}IN (?uids)}}))";


	private final UserService userService;
	private final SessionService sessionService;

	public DefaultB2bTmaProductDao(final String typecode, final UserService userService, final SessionService sessionService)
	{
		super(typecode);
		this.userService = userService;
		this.sessionService = sessionService;
	}

	@Override
	public List<ProductModel> findProductsByCode(final String code)
	{
		final UserModel currentUserModel = getUserService().getCurrentUser();
		final boolean applyAllowedPrincipalFilter = getAllowedPrincipalFlag();

		if (getUserService().isAnonymousUser(currentUserModel) && !Boolean.TRUE.equals(applyAllowedPrincipalFilter))
		{
			return super.findProductsByCode(code);
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(PRODUCT_OFFERING_QUERY);
		query.addQueryParameter("uids", getAllPrincipals(currentUserModel));
		query.addQueryParameter(ProductModel.CODE, code);

		return getFlexibleSearchService().<ProductModel>search(query).getResult();
	}

	private List<String> getAllPrincipals(final UserModel currentUserModel)
	{
		final List<String> uids = new ArrayList<>();
		uids.add(currentUserModel.getUid());

		if (!getUserService().isAnonymousUser(currentUserModel))
		{
			final Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(currentUserModel);
			uids.addAll(userGroups.stream().filter(Objects::nonNull).map(PrincipalModel::getUid).collect(Collectors.toList()));
		}

		return uids;
	}

	private boolean getAllowedPrincipalFlag()
	{
		return getSessionService().getAttribute(APPLY_ALLOWED_PRINCIPAL_FLAG) != null
				? getSessionService().getAttribute(APPLY_ALLOWED_PRINCIPAL_FLAG)
				: Boolean.TRUE;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}
