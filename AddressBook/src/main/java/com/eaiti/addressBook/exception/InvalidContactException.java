/**
 * Exception class for invalid field values in Contact
 */
package com.eaiti.addressBook.exception;

/**
 * @author Shashank Raghunath
 *
 */

public class InvalidContactException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidContactException(String message) {
		super(message);
	}
}
