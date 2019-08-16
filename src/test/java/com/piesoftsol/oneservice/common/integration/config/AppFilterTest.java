package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.SERVICE_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piesoftsol.oneservice.common.integration.config.AppFilter;

/**
 * Test case for AppFilter.java. Also covers AppHttpServletRequestWrapper.java
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppFilterTest {

	static {
		SERVICE_NAME = "test";
	}

	class MockServletInputStream extends ServletInputStream {
		InputStream inputStream;

		MockServletInputStream(String string) {
			this.inputStream = IOUtils.toInputStream(string);
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener arg0) {
		}
	}

	// Testcase Constants
	private static final String REQUEST_URI = "/test";

	private static AppFilter buildAppFilter() {
		System.setProperty(SERVICE_NAME, "test");
		AppFilter appFilter = new AppFilter();
		return appFilter;
	}

	private HttpServletRequest buildRequest(String inputStream) throws IOException {
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		when(httpServletRequest.getRequestURI()).thenReturn(REQUEST_URI);
		when(httpServletRequest.getMethod()).thenReturn(RequestMethod.POST.toString());
		when(httpServletRequest.getInputStream()).thenReturn(new MockServletInputStream(inputStream));

		return httpServletRequest;
	}

	// Success scenarios
	@Test
	public void testDoFilterSuccessScenario() throws IOException, ServletException {
		AppFilter appFilter = buildAppFilter();
		HttpServletRequest httpServletRequest = buildRequest("{\"JUR\": \"MA\", \"WC\": \"5249\"}");

		appFilter.doFilter(httpServletRequest, new MockHttpServletResponse(), new MockFilterChain());
	}

	@Test
	public void testDoFilterNoSchemaSuccessScenario() throws IOException, ServletException {
		AppFilter appFilter = buildAppFilter();
		HttpServletRequest httpServletRequest = buildRequest("{\"JUR\": \"MA\", \"WC\": \"5249\"}");
		when(httpServletRequest.getRequestURI()).thenReturn("abc");

		appFilter.doFilter(httpServletRequest, new MockHttpServletResponse(), new MockFilterChain());
	}

	@Test
	public void testDoFilterSwaggerScenario() throws IOException, ServletException {
		AppFilter appFilter = buildAppFilter();
		HttpServletRequest httpServletRequest = buildRequest("{\"JUR\": \"MA\", \"WC\": \"5249\"}");
		when(httpServletRequest.getRequestURI()).thenReturn("/test/swagger");
		when(httpServletRequest.getMethod()).thenReturn(RequestMethod.GET.toString());

		appFilter.doFilter(httpServletRequest, new MockHttpServletResponse(), new MockFilterChain());
	}

	@Test
	public void testInit() throws ServletException {
		AppFilter appFilter = buildAppFilter();
		appFilter.init(null);
	}

	@Test
	public void testDestroy() throws ServletException {
		AppFilter appFilter = buildAppFilter();
		appFilter.destroy();
	}

	// Error scenarios
	@Test
	public void testDoFilterInvalidJSONScenario() throws IOException, ServletException {
		AppFilter appFilter = buildAppFilter();
		HttpServletRequest httpServletRequest = buildRequest("{\"JUR\": \"MA\", \"WC\": \"\"}");

		//appFilter.doFilter(httpServletRequest, new MockHttpServletResponse(), new MockFilterChain());
		
	}
}
