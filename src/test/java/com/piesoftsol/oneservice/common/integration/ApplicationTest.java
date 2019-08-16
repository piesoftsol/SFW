package com.piesoftsol.oneservice.common.integration;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.piesoftsol.oneservice.common.integration.Application;

/**
 * Test case for Application.java
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author KARUNAR
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {

	// Exception scenarios
	@Test(expected = IllegalArgumentException.class)
	public void testApplicationException() {
		Application.main(null);
	}
}
