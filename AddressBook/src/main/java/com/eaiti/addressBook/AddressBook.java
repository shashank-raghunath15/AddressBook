/**
 * Entry Point of application AddressBook 
 */
package com.eaiti.addressBook;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eaiti.addressBook.contact.ContactController;
import com.eaiti.addressBook.contact.ContactDao;
import com.eaiti.addressBook.contact.ContactService;
import com.eaiti.addressBook.exception.InitializationException;
import com.eaiti.addressBook.util.PropertyUtil;

import spark.Spark;

public class AddressBook {

	public static final Logger LOGGER = LoggerFactory.getLogger(AddressBook.class);

	public static void main(String[] args) {
		new PropertyUtil();
		LOGGER.info("Starting AddressBook Service at" + PropertyUtil.WEB_PORT);

		try {
			new ContactController(new ContactService(new ContactDao()));
		} catch (InitializationException e) {
			LOGGER.error("Initialization failed", e);
			Spark.stop();
		}
		get("*", (request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("\"URL not found\"");
			return "\"URL NOT FOUND\"";
		});
		put("*", (request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("\"URL not found\"");
			return "\"URL NOT FOUND\"";
		});
		post("*", (request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("\"URL not found\"");
			return "\"URL NOT FOUND\"";
		});
		delete("*", (request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("\"URL not found\"");
			return "\"URL NOT FOUND\"";
		});
	}
}
