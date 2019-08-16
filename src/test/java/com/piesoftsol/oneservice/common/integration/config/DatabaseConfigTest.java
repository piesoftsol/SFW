package com.piesoftsol.oneservice.common.integration.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.piesoftsol.oneservice.common.integration.config.DatabaseConfig;

/**
 * Test case for DatabaseConfig.java
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author karunar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseConfigTest {

	// Testcase Constants
	private static final String PASSWD = "passwd";

	@Test
	public void testDatabaseConfig() {
		DatabaseConfig databaseConfig = new DatabaseConfig();
		byte[] passwdExpected = new byte[] { 0, 0, 0, 0, 0, 0 };

		byte[] passwd = PASSWD.getBytes();
		DataSource dataSource = databaseConfig.readonlyOneserviceDataSource(passwd);
		assertNotNull(dataSource);
		assertArrayEquals(passwdExpected, passwd);

		passwd = PASSWD.getBytes();
		dataSource = databaseConfig.readwriteOneserviceDataSource(passwd);
		assertNotNull(dataSource);
		assertArrayEquals(passwdExpected, passwd);

		passwd = PASSWD.getBytes();
		dataSource = databaseConfig.masterOneserviceDataSource(passwd);
		assertNotNull(dataSource);
		assertArrayEquals(passwdExpected, passwd);

		databaseConfig.oneserviceJdbcTemplate(dataSource);
	}
}
