/**
 * Exception for inserting a contact which already exists
 */
package com.eaiti.addressBook.exception;

/**
 * @author Shashank Raghunath
 *
 */
public class NotUniqueException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotUniqueException(String message) {
		super(message);
	}
}
