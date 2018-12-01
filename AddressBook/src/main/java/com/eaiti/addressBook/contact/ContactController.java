/**
 * ContactController handles REST endpoints for contacts
 */
package com.eaiti.addressBook.contact;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eaiti.addressBook.AddressBook;
import com.eaiti.addressBook.exception.InvalidContactException;
import com.eaiti.addressBook.exception.NotUniqueException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import spark.QueryParamsMap;

/**
 * @author Shashank Raghunath
 *
 */
public class ContactController {

	public static final Logger LOGGER = LoggerFactory.getLogger(AddressBook.class);

	public ContactController(final ContactService service) {
		ObjectMapper mapper = new ObjectMapper();
		get("/contact/:name", (request, response) -> {
			response.type("application/json");
			Contact contact = service.getContactByName(request.params("name"));
			if (contact == null) {
				response.status(HttpStatus.NOT_FOUND_404);
				return mapper.writeValueAsString("Contact not found");
			}
			response.status(HttpStatus.OK_200);
			return mapper.writeValueAsString(contact);
		});
		get("/contact", (request, response) -> {
			response.type("application/json");
			QueryParamsMap map = request.queryMap();
			if (map.get("query").hasValue() && map.get("pageSize").hasValue() && map.get("page").hasValue()) {
				List<Contact> contacts = service.getContactByQuery(map.get("pageSize").integerValue(),
						map.get("page").integerValue(), map.get("query").value());
				if (contacts != null) {
					response.status(HttpStatus.OK_200);
					return mapper.writeValueAsString(contacts);
				}
				response.status(HttpStatus.NOT_FOUND_404);
				return mapper.writeValueAsString("Contacts Not Found!");
			}
			response.status(HttpStatus.BAD_REQUEST_400);
			return mapper.writeValueAsString("Bad Request! Parameters needed are pageSize, page and query");
		});
		post("/contact/", (request, response) -> {
			response.type("application/json");
			response.status(HttpStatus.CREATED_201);
			return "\"" + service.saveContact(mapper.readValue(request.body(), Contact.class)) + "\"";
		});
		put("/contact/:name", (request, response) -> {
			response.type("application/json");
			String result = service.updateContact(mapper.readValue(request.body(), Contact.class),
					request.params("name"));
			if (result != null) {
				response.status(HttpStatus.OK_200);
				return mapper.writeValueAsString(result);
			}
			response.status(HttpStatus.NO_CONTENT_204);
			return mapper.writeValueAsString("Contact with name " + request.params("name") + " Not Found");
		});
		delete("/contact/:name", (request, response) -> {
			response.type("application/json");
			String result = service.deleteContact(request.params("name"));
			if (result != null) {
				response.status(HttpStatus.OK_200);
				return mapper.writeValueAsString(result);
			}
			response.status(HttpStatus.NOT_FOUND_404);
			return mapper.writeValueAsString("Contact with name " + request.params("name") + " Not Found");
		});

		exception(UnrecognizedPropertyException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Invalid Contact", exception.getMessage());
			response.status(HttpStatus.BAD_REQUEST_400);
			response.body("\"Contact has unrecognized fields\"");
		});

		exception(NumberFormatException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Bad Query", exception);
			response.status(HttpStatus.BAD_REQUEST_400);
			response.body("\"Page Size and Page should be integers.\"");
		});
		exception(ElasticsearchStatusException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Exception in ElasticSearch", exception);
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("\"Contact not found.\"");
		});

		exception(ElasticsearchException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Exception in ElasticSearch", exception);
			response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
			response.body("\"Internal Server Error. Please Contact System Administrator.\"");
		});
		exception(NotUniqueException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Contact already exists", exception.getMessage());
			response.status(HttpStatus.CONFLICT_409);
			response.body("\"Contact already exists\"");
		});

		exception(InvalidContactException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Invalid Contact Values", exception.getMessage());
			response.status(HttpStatus.BAD_REQUEST_400);
			response.body("\"" + exception.getMessage() + "\"");
		});

		exception(MismatchedInputException.class, (exception, request, response) -> {
			response.type("application/json");
			LOGGER.error("Invalid Contact Values", exception.getMessage());
			response.status(HttpStatus.BAD_REQUEST_400);
			response.body("\"Invalid Contact Values\"");
		});
	}
}
