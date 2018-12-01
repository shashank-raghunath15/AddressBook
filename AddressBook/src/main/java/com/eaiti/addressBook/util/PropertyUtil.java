/**
 * This class reads properties from app.properties file
 */
package com.eaiti.addressBook.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shashank Raghunath
 *
 */
public class PropertyUtil {

	public static Properties properties;
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
	public static String DB_HOST = "localhost";
	public static int DB_PORT = 9200;
	public static int WEB_PORT = 4567;
	public static String DB_PROTOCOL = "http";

	public PropertyUtil() {
		properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("app.properties"));
			DB_HOST = properties.getProperty("dbHost");
			DB_PORT = Integer.parseInt(properties.getProperty("dbPort"));
			WEB_PORT = Integer.parseInt(properties.getProperty("webPort"));
			DB_PROTOCOL = properties.getProperty("dbProtocol");
			LOGGER.info("dbHost " + DB_HOST);
			LOGGER.info("dbPort " + DB_PORT);
			LOGGER.info("webPort " + WEB_PORT);
			LOGGER.info("dbProtocol " + DB_PROTOCOL);
		} catch (IOException e) {
			LOGGER.error("Error reading property file", e);
		}
	}
}
