/**
 * ContactService handles business logic and validation
 */
package com.eaiti.addressBook.contact;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eaiti.addressBook.AddressBook;
import com.eaiti.addressBook.exception.InitializationException;
import com.eaiti.addressBook.exception.InvalidContactException;
import com.eaiti.addressBook.exception.NotUniqueException;

/**
 * @author Shashank Raghunath
 *
 */
public class ContactService {

	private ContactDao contactDao;
	public static final Logger LOGGER = LoggerFactory.getLogger(AddressBook.class);

	public ContactService(ContactDao contactDao) throws InitializationException {
		if (contactDao == null)
			throw new InitializationException("ContactDao null");
		this.contactDao = contactDao;
	}

	public Contact getContactByName(String name) throws ElasticsearchStatusException, IOException {
		if (name != null) {
			return contactDao.getContactByName(name);
		}
		return null;
	}

	public List<Contact> getContactByQuery(int pageSize, int page, String query) throws IOException {
		if (pageSize <= 0 || page < 0 || query == null)
			return null;
		return contactDao.getContactByQuery(pageSize, page, query);
	}

	public String saveContact(Contact contact) throws NotUniqueException, IOException, InvalidContactException {
		contact.validate();
		if (contactDao.getContactByName(contact.getName()) != null)
			throw new NotUniqueException(contact.getName());
		return contactDao.saveContact(contact);
	}

	public String updateContact(Contact contact, String name)
			throws InterruptedException, ExecutionException, IOException, InvalidContactException {
		if (name == null || contact == null)
			return null;
		contact.validate();
		return contactDao.updateContact(contact, name);
	}

	public String deleteContact(String name) throws IOException {
		if (name == null)
			return null;
		return contactDao.deleteContact(name);
	}

}
