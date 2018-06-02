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

import com.coupons.annotations.LoginFilterAnnotation;
import com.coupons.classes.ApplicationMessage;
import com.coupons.classes.ResponseCodes;
@Provider
@LoginFilterAnnotation
public class LoginFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest httpRequest;
	//	Default:
	//HttpServletRequest
	//ServletRequest
	
	//	Jersey:
	//ContainerRequestContext
	//ClientRequestContext
	
	@Override
	public void filter(ContainerRequestContext req) throws IOException {
		HttpSession session = httpRequest.getSession();
		if(req.getUriInfo().getPath().contains("login") && session.getAttribute("facade") != null)
			req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new ApplicationMessage(ResponseCodes.OTHER_ERROR, "You have already logged in."))
                    .type(MediaType.APPLICATION_JSON).build());	
		else if(req.getUriInfo().getPath().contains("logout") && session.getAttribute("facade") == null)
			req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new ApplicationMessage(ResponseCodes.OTHER_ERROR, "You have already logged out."))
                    .type(MediaType.APPLICATION_JSON).build());	
	}

}
