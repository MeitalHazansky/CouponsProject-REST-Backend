package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.MainCoupons.UserType;
import com.coupons.classes.ApplicationMessage;
import com.coupons.classes.ResponseCodes;
import com.exceptions.ConnectionException;
import com.exceptions.DatabaseException;
import com.facades.AdminFacade;
import com.facades.CompanyFacade;
import com.facades.Shop;

@Path("SessionService")
public class SessionService {

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("logout")
	public void logout() {
		this.httpRequest.getSession().invalidate();
	}

	@GET
	@Path("CheckSession")
	public Object checkSession() {
		HttpSession session = this.httpRequest.getSession();
		if (session.getAttribute("facade") == null)
			return UserType.GUEST;
		if (session.getAttribute("facade") instanceof AdminFacade)
			return UserType.ADMIN;
		if (session.getAttribute("facade") instanceof CompanyFacade)
			return UserType.COMPANY;
		return UserType.CUSTOMER;
	}

	@GET
	@Path("Store")
	public Object getStore() {

		try {
			return new Shop().getAllCoupons();
		} catch (ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

}
