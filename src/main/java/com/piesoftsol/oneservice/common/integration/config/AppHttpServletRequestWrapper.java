package com.piesoftsol.oneservice.common.integration.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * Wrapper class for HttpServletRequest
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
public class AppHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] body;

	/**
	 * Constructor
	 * 
	 * @param request
	 * @throws IOException
	 */
	public AppHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);

		if (null != request && null != request.getInputStream()) {
			// Read the request input stream and convert to byte array
			body = IOUtils.toByteArray(request.getInputStream());
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		// Construct a input stream from the class level byte array
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

		ServletInputStream servletInputStream = new ServletInputStream() {
			@Override
			public int read() throws IOException {
				// Read from the newly created input stream
				return byteArrayInputStream.read();
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
			public void setReadListener(ReadListener listener) {
			}
		};
		return servletInputStream;
	}

	/**
	 * Getter for body
	 * 
	 * @return byte[] - body
	 */
	public byte[] getBody() {
		return this.body;
	}
}