package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_PATTERN;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_INTIME;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_IP;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.YES;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.PC_REQ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;

/**
 * Context listener class for generating unique Request ID
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KIRANB
 */
@Configuration
@WebListener
public class RandomIDGenerationContextListener extends RequestContextListener {

	private static final AppLogger LOGGER = new AppLogger(RandomIDGenerationContextListener.class.getName());

	public void requestInitialized(ServletRequestEvent requestEvent) {

		MDC.put(REQUEST_PATTERN, UUID.randomUUID().toString());
		if(null != requestEvent.getServletRequest() && null != requestEvent.getServletRequest().getRemoteAddr())
			MDC.put(REQUEST_IP, requestEvent.getServletRequest().getRemoteAddr());
		if(PC_REQ.equals(YES)) {
			Date date = Calendar.getInstance().getTime();  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");  
			String inTime = dateFormat.format(date);
			MDC.put(REQUEST_INTIME, inTime);
		}
		LOGGER.debug("Request Initiated for :: " + MDC.get(REQUEST_PATTERN));
	}

	public void requestDestroyed(ServletRequestEvent requestEvent) {
		LOGGER.debug("Request Completed for :: " + MDC.get(REQUEST_PATTERN));
		MDC.clear();
	}
}