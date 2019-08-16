package com.piesoftsol.oneservice.common.integration.config;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect for monitoring the cross cutting method level logging concerns.
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
@Aspect
@Component
public class LoggingAspect {

	@Pointcut("execution(* com.piesoftsol.*.*.*.*.*(..)) || execution(* com.piesoftsol.*.*.*.*.*.*(..))")
	protected void methodEntryExitLogging() {
	}

	@Pointcut("execution(* com.piesoftsol.*.*.integration.dao.*.*(..))")
	protected void timeTakenLogging() {
	}

	/**
	 * Logs the method at entry level
	 * 
	 * @param joinPoint
	 */
	@Before("methodEntryExitLogging()")
	@Order(1)
	public void logEntry(JoinPoint joinPoint) {
		final Log logger = getLogger(joinPoint);
		final String logPrefix = getLogPrefix(joinPoint);

		logger.debug(logPrefix + "(" + Arrays.toString(joinPoint.getArgs()) + ") :: Start");
	}

	/**
	 * Logs the method at exit level
	 * 
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "methodEntryExitLogging()", returning = "result")
	@Order(2)
	public void logExit(JoinPoint joinPoint, Object result) {
		final Log logger = getLogger(joinPoint);
		final String logPrefix = getLogPrefix(joinPoint);

		logger.debug(logPrefix + " :: End. Return value :: [" + result + "]");
	}

	/**
	 * Logs the method after exception is thrown
	 * 
	 * @param joinPoint
	 * @param throwable
	 */
	@AfterThrowing(pointcut = "methodEntryExitLogging()", throwing = "throwable")
	@Order(3)
	public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
		final Log logger = getLogger(joinPoint);
		final String logPrefix = getLogPrefix(joinPoint);

		logger.debug(logPrefix + " :: An exception has been thrown :: Cause - " + throwable.getCause());
	}

	/**
	 * Logs the time taken by the method
	 * 
	 * @param joinPoint
	 * @return Object - result of executed method
	 * @throws Throwable
	 */
	@Around("timeTakenLogging()")
	@Order(4)
	public Object logTimeTaken(ProceedingJoinPoint joinPoint) throws Throwable {
		final Log logger = getLogger(joinPoint);
		final String logPrefix = getLogPrefix(joinPoint);

		long start = System.nanoTime();
		Object result = joinPoint.proceed();
		long end = System.nanoTime();

		logger.debug(logPrefix + " :: Time taken " + ((double) (end - start) / 1000000) + " ms");

		return result;
	}

	/**
	 * Gets the join point class logger
	 * 
	 * @param joinPoint
	 * @return Log - Class logger
	 */
	private static Log getLogger(JoinPoint joinPoint) {
		return LogFactory.getLog(joinPoint.getTarget().getClass());
	}

	/**
	 * Gets the log prefix string
	 * 
	 * @param joinPoint
	 * @return String - Log prefix
	 */
	private static String getLogPrefix(JoinPoint joinPoint) {
		StringBuilder builder = new StringBuilder();

		builder.append(joinPoint.getSignature().getName());

		return builder.toString();
	}
}