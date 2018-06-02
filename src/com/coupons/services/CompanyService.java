package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.CouponSystem.CouponSystem;
import com.MainCoupons.Coupon;
import com.MainCoupons.CouponType;
import com.MainCoupons.UserType;
import com.coupons.annotations.LoginFilterAnnotation;
import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.business_delegate.BusinessDelegate;
import com.coupons.classes.ApplicationMessage;
import com.coupons.classes.LoginInfo;
import com.coupons.classes.ResponseCodes;
import com.coupons.rest_exceptions.BusinessDelegateException;
import com.exceptions.CompanyIsNotLoggedIn;
import com.exceptions.ConnectionException;
import com.exceptions.CouponAlreadyExists;
import com.exceptions.CouponDoesntExist;
import com.exceptions.DatabaseException;
import com.facades.CompanyFacade;

@Path("CompanyService")
public class CompanyService {

	@Context
	private HttpServletRequest request;

	public CompanyService() {
	}

	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@LoginFilterAnnotation
	public Object login(LoginInfo loginInfo) {
		try {

			CompanyFacade company = (CompanyFacade) CouponSystem.getInstance().login(loginInfo.getUserName(),
					loginInfo.getPassword(), UserType.COMPANY);

			if (company == null)
				return new ApplicationMessage(ResponseCodes.OTHER_ERROR,
						"The information you have provided is incorrect.");

			request.getSession().setAttribute("facade", company);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Logged in successfully.");
		} catch (ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("coupon")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object createCoupon(Coupon coupon) {
		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");
		System.out.println(coupon);

		try {
			company.createCoupon(coupon);

			BusinessDelegate.BusinessDelegate.storeIncome(company.getCompanyInfo().getCompName(), "COMPANY_NEW_COUPON",
					100, UserType.COMPANY);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon has been created successfully.");
		} catch (CompanyIsNotLoggedIn | CouponAlreadyExists | ConnectionException | DatabaseException
				| BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("coupon/{id}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object removeCoupon(@PathParam("id") int id) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			company.removeCoupon(company.getCouponByID(id));

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon have been removed successfully");

		} catch (CompanyIsNotLoggedIn | CouponDoesntExist | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("coupon")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCoupon(Coupon coupon) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			company.updateCoupon(coupon);

			BusinessDelegate.BusinessDelegate.storeIncome(company.getCompanyInfo().getCompName(),
					"COMPANY_UPDATE_COUPON", 10, UserType.COMPANY);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon updated successfully.");
		} catch (CompanyIsNotLoggedIn | CouponDoesntExist | ConnectionException | DatabaseException
				| BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("coupon/{id}")
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@SessionFilterAnnotation
	public Object getCoupon(@PathParam("id") int id) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			return company.getCouponByID(id);
		} catch (CompanyIsNotLoggedIn | ConnectionException | DatabaseException | CouponDoesntExist e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("coupon")
	@GET
	@SessionFilterAnnotation
	public Object getAllCoupon() {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			return company.getAllCoupons();
		} catch (CompanyIsNotLoggedIn | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("couponByType/{couponType}")
	@GET
	@SessionFilterAnnotation
	public Object getAllCouponByType(@PathParam("couponType") CouponType couponType) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			return company.getCouponByType(couponType);
		} catch (CompanyIsNotLoggedIn | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("couponUpToDate")
	@SessionFilterAnnotation
	public Object getCouponUpToDate(@QueryParam("date") long date) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");
		try {
			return company.getCouponsUntilDate(new java.util.Date(date));
		} catch (CompanyIsNotLoggedIn | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("couponUpToPrice")
	@SessionFilterAnnotation
	public Object getCouponUpToPrice(@QueryParam("price") double price) {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			return company.getCouponsUpToPrice(price);
		} catch (CompanyIsNotLoggedIn | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("company")
	@SessionFilterAnnotation
	public Object getCompanyInformation() {

		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");
		try {
			return company.getCompanyInfo();
		} catch (CompanyIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@GET
	@Path("income")
	@SessionFilterAnnotation
	public Object getCompanyIncomeInfo() {
		CompanyFacade company = (CompanyFacade) request.getSession().getAttribute("facade");

		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCompany(company.getCompanyInfo().getCompName());
		} catch (BusinessDelegateException | CompanyIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
}
