/**
 * ContactService Unit Test
 */
package com.eaiti.addressbook;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.eaiti.addressBook.contact.Contact;
import com.eaiti.addressBook.contact.ContactDao;
import com.eaiti.addressBook.contact.ContactService;
import com.eaiti.addressBook.exception.InvalidContactException;
import com.eaiti.addressBook.exception.NotUniqueException;
import com.eaiti.addressBook.util.PropertyUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Shashank Raghunath
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(JUnitPlatform.class)
class ContactServiceTest {

	ContactService contactService;
	@Mock
	ContactDao contactDao;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		new PropertyUtil();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		contactService = new ContactService(contactDao);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		contactService = null;
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#ContactService()}.
	 */
	@DisplayName("Contact Service Null Dependency")
	@Test
	final void testContactService() {
		Assertions.assertThrows(Exception.class, () -> {
			new ContactService(null);
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByName(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Name with null parameter")
	@Test
	final void testGetContactByNameNullParameter() throws IOException {
		Assertions.assertEquals(contactService.getContactByName(null), null);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByName(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Name Happy Path")
	@Test
	final void testGetContactByName() throws JsonParseException, JsonMappingException, IOException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		when(contactDao.getContactByName(any())).thenReturn(contact);
		Assertions.assertNotNull(contact);
		Assertions.assertEquals(contact, contact);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByQuery(int, int, java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Query Happy Path")
	@Test
	final void testGetContactByQuery() throws IOException {
		List<Contact> contacts = new ArrayList<>();
		Contact contact1 = new Contact();
		contact1.setName("abcd");
		contact1.setAddress("asdasd");
		contact1.setPhone("1231231231");
		contacts.add(contact1);
		when(contactDao.getContactByQuery(any(Integer.class), any(Integer.class), any(String.class)))
				.thenReturn(contacts);
		List<Contact> cts = contactService.getContactByQuery(1, 1, "query");
		Assertions.assertNotNull(cts);
		Assertions.assertEquals(cts, contacts);
		Assertions.assertEquals(cts.size(), 1);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByQuery(int, int, java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Query Negative Page Size")
	@Test
	final void testGetContactByQueryNegativePageSize() throws IOException {
		List<Contact> cts = contactService.getContactByQuery(-1, 2, "query");
		Assertions.assertNull(cts);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByQuery(int, int, java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Query Negative Page Offset")
	@Test
	final void testGetContactByQueryNegativeOffset() throws IOException {
		List<Contact> cts = contactService.getContactByQuery(0, 1, "query");
		Assertions.assertNull(cts);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#getContactByQuery(int, int, java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Get Contact By Query Null Query")
	@Test
	final void testGetContactByQueryNullQuery() throws IOException {
		List<Contact> cts = contactService.getContactByQuery(1, 1, null);
		Assertions.assertNull(cts);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#saveContact(com.eaiti.addressBook.contact.Contact)}.
	 * 
	 * @throws IOException
	 * @throws InvalidContactException
	 * @throws NotUniqueException
	 */
	@DisplayName("Save Contact Happy Path")
	@Test
	final void testSaveContact() throws IOException, NotUniqueException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		when(contactDao.saveContact(any(Contact.class))).thenReturn("CREATED");
		String output = contactService.saveContact(contact);
		Assertions.assertNotNull(output);
		Assertions.assertEquals("CREATED", output);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#saveContact(com.eaiti.addressBook.contact.Contact)}.
	 * 
	 * @throws IOException
	 * @throws InvalidContactException
	 * @throws NotUniqueException
	 */
	@DisplayName("Save Contact Already Exists")
	@Test
	final void testSaveContactAlreadyExists() throws IOException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		when(contactDao.getContactByName(any(String.class))).thenReturn(contact);
		Assertions.assertThrows(NotUniqueException.class, () -> {
			contactService.saveContact(contact);
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#saveContact(com.eaiti.addressBook.contact.Contact)}.
	 * 
	 * @throws IOException
	 * @throws InvalidContactException
	 * @throws NotUniqueException
	 */
	@DisplayName("Save Contact Invalid Email")
	@Test
	final void testSaveContactInvalidEmail() throws IOException, NotUniqueException {
		Contact contact = new Contact();
		contact.setName("Abcd123");
		contact.setEmail("Abcddef.com");
		contact.setPhone("1231231231");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.saveContact(contact);
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#saveContact(com.eaiti.addressBook.contact.Contact)}.
	 * 
	 * @throws IOException
	 * @throws InvalidContactException
	 * @throws NotUniqueException
	 */
	@DisplayName("Save Contact Invalid Name")
	@Test
	final void testSaveContactInvalidName() throws IOException, NotUniqueException {
		Contact contact = new Contact();
		contact.setName("Abcd123");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.saveContact(contact);
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#saveContact(com.eaiti.addressBook.contact.Contact)}.
	 * 
	 * @throws IOException
	 * @throws InvalidContactException
	 * @throws NotUniqueException
	 */
	@DisplayName("Save Contact Invalid Phone Number")
	@Test
	final void testSaveContactInvalidPhoneNumber() throws IOException, NotUniqueException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("11asdc");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.saveContact(contact);
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#updateContact(com.eaiti.addressBook.contact.Contact, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws InvalidContactException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@DisplayName("Update Contact Happy Path")
	@Test
	final void testUpdateContact() throws JsonParseException, JsonMappingException, IOException, InterruptedException,
			ExecutionException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		when(contactDao.updateContact(any(Contact.class), any(String.class))).thenReturn("OK");
		String output = contactService.updateContact(contact, contact.getName());
		Assertions.assertNotNull(output);
		Assertions.assertEquals("OK", output);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#updateContact(com.eaiti.addressBook.contact.Contact, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws InvalidContactException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@DisplayName("Update Contact Name Null")
	@Test
	final void testUpdateContactNameNull() throws JsonParseException, JsonMappingException, IOException,
			InterruptedException, ExecutionException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		String output = contactService.updateContact(contact, null);
		Assertions.assertNull(output);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#updateContact(com.eaiti.addressBook.contact.Contact, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws InvalidContactException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@DisplayName("Update Contact Invalid Email")
	@Test
	final void testUpdateContactInvalidEmail() throws JsonParseException, JsonMappingException, IOException,
			InterruptedException, ExecutionException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcddef.com");
		contact.setPhone("1231231231");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.updateContact(contact, contact.getName());
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#updateContact(com.eaiti.addressBook.contact.Contact, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws InvalidContactException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@DisplayName("Update Contact Invalid Name")
	@Test
	final void testUpdateContactInvalidName() throws JsonParseException, JsonMappingException, IOException,
			InterruptedException, ExecutionException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd123123");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("1231231231");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.updateContact(contact, contact.getName());
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#updateContact(com.eaiti.addressBook.contact.Contact, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws InvalidContactException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@DisplayName("Update Contact Invalid Phone")
	@Test
	final void testUpdateContactInvalidPhoneNumber() throws JsonParseException, JsonMappingException, IOException,
			InterruptedException, ExecutionException, InvalidContactException {
		Contact contact = new Contact();
		contact.setName("Abcd");
		contact.setEmail("Abcd@def.com");
		contact.setPhone("12asd");
		Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.updateContact(contact, contact.getName());
		});
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#deleteContact(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Delete Contact Happy Path")
	@Test
	final void testDeleteContact() throws IOException {
		when(contactDao.deleteContact(any(String.class))).thenReturn("OK");
		String output = contactService.deleteContact("Abcd");
		Assertions.assertNotNull(output);
		Assertions.assertEquals("OK", output);
	}

	/**
	 * Test method for
	 * {@link com.eaiti.addressBook.contact.ContactService#deleteContact(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Delete Contact Null Parameter")
	@Test
	final void testDeleteContactNullParameter() throws IOException {
		String output = contactService.deleteContact(null);
		Assertions.assertNull(output);
	}
}
