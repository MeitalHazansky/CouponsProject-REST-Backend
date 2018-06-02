package com.coupons.services.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.coupons.annotations.SessionFilterAnnotation;
import com.facades.AdminFacade;
import com.facades.CompanyFacade;
import com.facades.CustomerFacade;

@Provider
@SessionFilterAnnotation
public class SessionFilter implements ContainerRequestFilter{

	@Context 
	private HttpServletRequest httpRequest;
	
	@Override
	public void filter(ContainerRequestContext req) throws IOException {
		HttpSession session = httpRequest.getSession();
		if(req.getUriInfo().getPath().contains("AdminService") && !(session.getAttribute("facade") instanceof AdminFacade))
			req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You must be admin to use admin services.")
                    .type(MediaType.APPLICATION_JSON).build());	
		else if(req.getUriInfo().getPath().contains("CompanyService") && !(session.getAttribute("facade") instanceof CompanyFacade))
			req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You must be company to use company services.")
                    .type(MediaType.APPLICATION_JSON).build());
		else if(req.getUriInfo().getPath().contains("CustomerService") && !(session.getAttribute("facade") instanceof CustomerFacade))
		req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You must be customer to use customer services.")
                .type(MediaType.APPLICATION_JSON).build());
			
	}

}
