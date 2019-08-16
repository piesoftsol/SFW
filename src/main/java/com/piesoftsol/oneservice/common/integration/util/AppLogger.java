package com.piesoftsol.oneservice.common.integration.util;

import org.apache.log4j.*;

public class AppLogger {

	private Logger logger;

	public AppLogger(String className) {
		logger = LogManager.getLogger(className);
	}

	@Deprecated
	public void startMethod(String methodName) {
		logger.debug(methodName + " :::: Start");
	}

	@Deprecated
	public void endMethod(String methodName) {
		logger.debug(methodName + " :::: End");
	}

	public void catchInfo(String methodName) {
		logger.info(methodName + " :::: Inside catch block");
	}

	public void error(String methodName, String message) {
		logger.error(methodName + " :::: " + message);
	}

	public void info(String... message) {

		StringBuilder stringBuilder = new StringBuilder();
		// stringBuilder.append(methodName).append("::::");

		for (String msg : message) {
			stringBuilder.append(msg);
		}
		logger.info(stringBuilder.toString());
	}

	/*
	 * public void info(String methodName, String message) {
	 * 
	 * logger.info(methodName + " :::: " + message); }
	 */

	public void debug(String methodName, String message) {
		logger.debug(methodName + " :::: " + message);
	}

	public void debug(String methodName, String... strings) {
		if (logger.isDebugEnabled()) {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(methodName).append("::::");
			for (String str : strings) {
				stringBuilder.append(str);
			}

			logger.debug(methodName + " :::: " + stringBuilder.toString());
		}
	}

	public void error(Throwable throwable) {
		logger.error(throwable.getMessage());
		stackTraceToString(throwable);
	}

	public void stackTraceToString(Throwable throwable) {
		logger.error(throwable.getMessage());
		StringBuilder stringBuilder = new StringBuilder();
		for (StackTraceElement element : throwable.getStackTrace()) {
			stringBuilder.append(element.toString());
			stringBuilder.append("\n");
		}
		logger.error(stringBuilder.toString());
	}

	public void info(Object... message) {
		StringBuilder stringBuilder = new StringBuilder();

		for (Object msg : message) {
			stringBuilder.append(msg);
		}

		logger.info(stringBuilder.toString());
	}
}
