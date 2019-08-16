package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.REQUEST_PATTERN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import javax.servlet.ServletRequestEvent;

import org.apache.log4j.MDC;
import org.junit.Test;

import com.piesoftsol.oneservice.common.integration.config.RandomIDGenerationContextListener;

/**
 * Test case for RandomIDGenerationContextListener.java.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
public class RandomIDGenerationContextListenerTest {

	@Test
	public void testRequestInitialized() {
		ServletRequestEvent servletRequestEvent = mock(ServletRequestEvent.class);
		RandomIDGenerationContextListener randomIDGenerationContextListener = new RandomIDGenerationContextListener();

		randomIDGenerationContextListener.requestInitialized(servletRequestEvent);

		assertNotNull(MDC.get(REQUEST_PATTERN));
	}

	@Test
	public void testRequestDestroyed() {
		ServletRequestEvent servletRequestEvent = mock(ServletRequestEvent.class);
		RandomIDGenerationContextListener randomIDGenerationContextListener = new RandomIDGenerationContextListener();

		randomIDGenerationContextListener.requestDestroyed(servletRequestEvent);

		assertNull(MDC.get(REQUEST_PATTERN));
	}
}
