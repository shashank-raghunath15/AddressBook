/**
 * Contact model class
 */
package com.eaiti.addressBook.contact;

import com.eaiti.addressBook.exception.InvalidContactException;

import lombok.Data;

/**
 * @author Shashank Raghunath
 *
 */

@Data
public class Contact {

	private String name;
	private String email;
	private String phone;
	private String address;

	public void validate() throws InvalidContactException {
		if (name == null || !name.matches("^[a-zA-Z \\-\\.\\']*$"))
			throw new InvalidContactException("Invalid name");
		if (phone == null || !validatePhoneNumber(phone))
			throw new InvalidContactException("Invalid phone number");
		if (email != null && !email.matches(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			throw new InvalidContactException("Invalid email");
	}

	private static boolean validatePhoneNumber(String phoneNo) {
		if (phoneNo.matches("\\d{10}"))
			return true;
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		else
			return false;
	}
}
