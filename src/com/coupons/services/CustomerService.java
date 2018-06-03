package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.exceptions.ConnectionException;
import com.exceptions.CouponDoesntExist;
import com.exceptions.CouponOutOfDate;
import com.exceptions.CouponOutOfStock;
import com.exceptions.CustomerAlreadyBroughtCoupon;
import com.exceptions.CustomerDoesNotExist;
import com.exceptions.CustomerIsNotLoggedIn;
import com.exceptions.DatabaseException;
import com.facades.CustomerFacade;

@Path("CustomerService")
public class CustomerService {

	@Context
	private HttpServletRequest request;

	public CustomerService() {
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@LoginFilterAnnotation
	public Object login(LoginInfo loginInfo) {
		try {

			CustomerFacade customer = (CustomerFacade) CouponSystem.getInstance().login(loginInfo.getUserName(),
					loginInfo.getPassword(), UserType.CUSTOMER);

			if (customer == null)
				return new ApplicationMessage(ResponseCodes.OTHER_ERROR, "There is a problem with info.");

			request.getSession().setAttribute("facade", customer);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Logged in successfully.");
		} catch (ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@POST
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object purchaseCoupon(Coupon coupon) {

		CustomerFacade customer = (CustomerFacade) request.getSession().getAttribute("facade");

		try {
			customer.purchaseCoupon(coupon);

			BusinessDelegate.BusinessDelegate.storeIncome(customer.getCurrentCustomer().getCustName(),
					"CUSTOMER_PURCHASE", coupon.getPrice(), UserType.CUSTOMER);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon has been purchased successfully.");
		} catch (CustomerIsNotLoggedIn | CouponDoesntExist | CouponOutOfStock | CouponOutOfDate
				| CustomerAlreadyBroughtCoupon | ConnectionException | DatabaseException
				| BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCoupons() {

		CustomerFacade customer = (CustomerFacade) request.getSession().getAttribute("facade");

		try {
			return customer.getAllPurchasedCoupons();
		} catch (CustomerIsNotLoggedIn | ConnectionException | DatabaseException | CustomerDoesNotExist e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("couponByType")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCouponsByType(@QueryParam("type") CouponType couponType) {
		CustomerFacade customer = (CustomerFacade) request.getSession().getAttribute("facade");

		try {
			return customer.getAllPurchasedCouponsByType(couponType);
		} catch (CustomerIsNotLoggedIn | ConnectionException | DatabaseException | CustomerDoesNotExist e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("couponByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCouponsByPrice(@QueryParam("price") double price) {
		CustomerFacade customer = (CustomerFacade) request.getSession().getAttribute("facade");

		try {
			return customer.getAllPurchasedCouponsByPrice(price);
		} catch (CustomerIsNotLoggedIn | ConnectionException | DatabaseException | CustomerDoesNotExist e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@GET
	@Path("customerInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCustomerInfo() {

		CustomerFacade customer = (CustomerFacade) request.getSession().getAttribute("facade");
		return customer.getCurrentCustomer();
	}

}
