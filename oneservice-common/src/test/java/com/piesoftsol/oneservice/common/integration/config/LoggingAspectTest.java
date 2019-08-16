package com.piesoftsol.oneservice.common.integration.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import com.piesoftsol.oneservice.common.integration.config.LoggingAspect;

/**
 * Test case for LoggingAspect.java
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
public class LoggingAspectTest {

	class TestClass {
		public void logMethod() {
		}
	}

	private ProceedingJoinPoint getJoinPoint() throws NoSuchMethodException, SecurityException {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		MethodSignature signature = mock(MethodSignature.class);

		when(joinPoint.getTarget()).thenReturn(new TestClass());
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.getMethod()).thenReturn(myMethod());

		return joinPoint;
	}

	private Method myMethod() throws NoSuchMethodException, SecurityException {
		return TestClass.class.getDeclaredMethod("logMethod");
	}

	@Test
	public void testLoggingAspect() throws Throwable {
		LoggingAspect loggingAspect = new LoggingAspect();
		ProceedingJoinPoint joinPoint = getJoinPoint();

		loggingAspect.methodEntryExitLogging();
		loggingAspect.timeTakenLogging();
		loggingAspect.logEntry(joinPoint);
		loggingAspect.logExit(joinPoint, "");
		loggingAspect.logAfterThrowing(joinPoint, new NullPointerException());
		loggingAspect.logTimeTaken(joinPoint);
	}
}
