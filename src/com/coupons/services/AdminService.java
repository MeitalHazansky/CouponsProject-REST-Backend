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
import com.MainCoupons.Company;
import com.MainCoupons.Customer;
import com.MainCoupons.UserType;
import com.coupons.annotations.LoginFilterAnnotation;
import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.business_delegate.BusinessDelegate;
import com.coupons.classes.ApplicationMessage;
import com.coupons.classes.LoginInfo;
import com.coupons.classes.ResponseCodes;
import com.coupons.rest_exceptions.BusinessDelegateException;
import com.exceptions.AdminIsNotLoggedIn;
import com.exceptions.CompanyAlreadyExists;
import com.exceptions.CompanyDoesntExist;
import com.exceptions.ConnectionException;
import com.exceptions.CustomerAlreadyExists;
import com.exceptions.CustomerDoesNotExist;
import com.exceptions.DatabaseException;
import com.facades.AdminFacade;

@Path("AdminService")
public class AdminService {

	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@LoginFilterAnnotation
	public Object login(LoginInfo loginInfo) {

		try {
			AdminFacade admin = (AdminFacade) CouponSystem.getInstance().login(loginInfo.getUserName(),
					loginInfo.getPassword(), UserType.ADMIN);

			if (admin == null)
				return new ApplicationMessage(ResponseCodes.OTHER_ERROR,
						"The information you have provided is incorrect.");

			request.getSession().setAttribute("facade", admin);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Logged in successfully.");
		} catch (ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@POST
	@Path("company")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object createCompany(Company company) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.createCompany(company);

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Company created successfully.");
		} catch (CompanyAlreadyExists | ConnectionException | DatabaseException | AdminIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@DELETE
	@Path("company/{id}")	
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object removeCompany(@PathParam("id") int id) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.removeCompany(admin.getCompany(id));

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Company removed successfully.");
		} catch (CompanyDoesntExist | ConnectionException | DatabaseException | AdminIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@PUT
	@Path("company")	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCompany(Company company) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.updateCompany(company);

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Company updated successfully.");
		} catch (CompanyDoesntExist | ConnectionException | DatabaseException | AdminIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@GET
	@Path("company/{id}")	
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCompany(@PathParam("id") int id) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			return admin.getCompany(id);
		} catch (CompanyDoesntExist | ConnectionException | DatabaseException | AdminIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllCompanies() {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			return admin.getAllCompanies();
		} catch (ConnectionException | DatabaseException | AdminIsNotLoggedIn e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@POST
	@Path("customer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object createCustomer(Customer customer) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.createCustomer(customer);

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Customer created successfully.");
		} catch (AdminIsNotLoggedIn | CustomerAlreadyExists | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@DELETE
	@Path("customer/{id}")	
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object removeCustomer(@PathParam("id") int id) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.removeCustomer(id);

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Customer has been removed successfully.");
		} catch (AdminIsNotLoggedIn | CustomerDoesNotExist | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@PUT
	@Path("customer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCustomer(Customer customer) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			admin.updateCustomer(customer);

			return new ApplicationMessage(ResponseCodes.SUCCESS, "Customer has been updated successfully.");
		} catch (AdminIsNotLoggedIn | CustomerDoesNotExist | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("customer/{id}")	
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getCustomer(@PathParam("id") int id) {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			return admin.getCustomerByID(id);
		} catch (AdminIsNotLoggedIn | CustomerDoesNotExist | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("customer")	
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllCustomers() {
		AdminFacade admin = (AdminFacade) request.getSession().getAttribute("facade");

		try {
			return admin.getAllCustomers();
		} catch (AdminIsNotLoggedIn | ConnectionException | DatabaseException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("income")	
	@SessionFilterAnnotation
	public Object getAllIncome() {

		try {
			return BusinessDelegate.BusinessDelegate.viewAllIncome();
		} catch (BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("income/company")	
	@SessionFilterAnnotation
	public Object getIncomeByCompany(@QueryParam("name") String name) {

		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCompany(name);
		} catch (BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}
	
	@GET
	@Path("income/customer")	
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getIncomeByCustomer(@QueryParam("name") String name) {

		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCustomer(name);
		} catch (BusinessDelegateException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

}
