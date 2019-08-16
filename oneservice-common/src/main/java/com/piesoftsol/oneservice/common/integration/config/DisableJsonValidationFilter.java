package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_URL;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;
import com.piesoftsol.oneservice.common.integration.util.DisableJsonValidation;

/**
 * Application Filter class
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
@SuppressWarnings("deprecation")
@Conditional(DisableJsonValidation.class)
@Component
public class DisableJsonValidationFilter implements Filter {

	private static final AppLogger LOGGER = new AppLogger(DisableJsonValidationFilter.class.getName());

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		final String methodName = "doFilter";
		LOGGER.startMethod(methodName);
		// Creating a wrapper class for servletResponse
		final AppHttpServletResponseWrapper responseWrapper = new AppHttpServletResponseWrapper(
				(HttpServletResponse) servletResponse);

		// Creating a wrapper class for servletRequest
		final AppHttpServletRequestWrapper requestWrapper = new AppHttpServletRequestWrapper(
				(HttpServletRequest) servletRequest);
		String requestURI = requestWrapper.getRequestURI();
		final String requestMethod = requestWrapper.getMethod();
		if (isNotBlank(requestURI)) {
			MDC.put(REQUEST_URL, requestURI);
			LOGGER.debug("Request Initiated for :: " + MDC.get(REQUEST_URL));
		}
		
		LOGGER.info("Incoming Request URI :: " + requestURI + ". Request Method :: " + requestMethod);
		
		chain.doFilter(requestWrapper, responseWrapper);

		responseWrapper.flushBuffer();
		LOGGER.info("Outgoing Response Body :: " + new String(responseWrapper.getCopy()));

		LOGGER.endMethod(methodName);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
