package com.piesoftsol.oneservice.common.integration.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrapper class for HttpServletResponse
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
public class AppHttpServletResponseWrapper extends HttpServletResponseWrapper {

	private ServletOutputStream servletOutputStream;
	private PrintWriter printWriter;
	private ServiceResponseStream serviceResponseStream;

	/**
	 * Constructor
	 * 
	 * @param response
	 * @throws IOException
	 */
	public AppHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (printWriter != null) {
			throw new IllegalStateException("getWriter() has already been called on this response.");
		}

		if (servletOutputStream == null) {
			servletOutputStream = getResponse().getOutputStream();
			serviceResponseStream = new ServiceResponseStream(servletOutputStream);
		}
		return serviceResponseStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (servletOutputStream != null) {
			throw new IllegalStateException("getOutputStream() has already been called on this response.");
		}

		if (printWriter == null) {
			serviceResponseStream = new ServiceResponseStream(getResponse().getOutputStream());
			printWriter = new PrintWriter(
					new OutputStreamWriter(serviceResponseStream, getResponse().getCharacterEncoding()), true);
		}
		return printWriter;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (printWriter != null) {
			printWriter.flush();
		} else if (servletOutputStream != null) {
			serviceResponseStream.flush();
		}
	}

	/**
	 * Copies the response output stream
	 * 
	 * @return byte[]
	 */
	public byte[] getCopy() {
		if (serviceResponseStream != null) {
			return serviceResponseStream.getOutputStream();
		} else {
			return new byte[0];
		}
	}

	/**
	 * Wrapper class for ServletOutputStream
	 * 
	 * <!-- This Class DOES NOT require any modification.-->
	 * 
	 * @author KARUNAR
	 */
	private class ServiceResponseStream extends ServletOutputStream {

		private OutputStream outputStream;
		private ByteArrayOutputStream byteArrayOutputStream;

		/**
		 * Constructor
		 * 
		 * @param outputStream
		 */
		public ServiceResponseStream(OutputStream outputStream) {
			this.outputStream = outputStream;
			this.byteArrayOutputStream = new ByteArrayOutputStream(1024);
		}

		@Override
		public void write(int b) throws IOException {
			outputStream.write(b);
			byteArrayOutputStream.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
		}

		/**
		 * Gets ouput stream
		 * 
		 * @return byte[]
		 */
		public byte[] getOutputStream() {
			return byteArrayOutputStream.toByteArray();
		}
	}
}