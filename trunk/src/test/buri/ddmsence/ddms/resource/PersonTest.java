/* Copyright 2010 - 2011 by Brian Uri!
   
   This file is part of DDMSence.
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 3.0 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public 
   License along with DDMSence. If not, see <http://www.gnu.org/licenses/>.

   You can contact the author at ddmsence@urizone.net. The DDMSence
   home page is located at http://ddmsence.urizone.net/
 */
package buri.ddmsence.ddms.resource;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:Person elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PersonTest extends AbstractComponentTestCase {

	private static final String TEST_PRODUCER_TYPE = "creator";

	private static final String TEST_SURNAME = "Uri";
	private static final String TEST_USERID = "123";
	private static final String TEST_AFFILIATION = "DISA";
	private static final List<String> TEST_NAMES = new ArrayList<String>();
	private static final List<String> TEST_PHONES = new ArrayList<String>();
	private static final List<String> TEST_EMAILS = new ArrayList<String>();
	static {
		TEST_NAMES.add("Brian");
		TEST_NAMES.add("BU");
		TEST_PHONES.add("703-885-1000");
		TEST_EMAILS.add("ddms@fgm.com");
	}

	/**
	 * Constructor
	 */
	public PersonTest() {
		super("person.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Person testConstructor(boolean expectFailure, Element element) {
		Person component = null;
		try {
			Element producerElement = Util.buildDDMSElement(TEST_PRODUCER_TYPE, null);
			producerElement.appendChild(new Element(element));
			SecurityAttributesTest.getFixture(false).addTo(producerElement);
			component = new Person(producerElement);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param surname the surname of the person
	 * @param names an ordered list of names
	 * @param userID optional unique identifier within an organization
	 * @param affiliation organizational affiliation of the person
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	private Person testConstructor(boolean expectFailure, String surname, List<String> names, String userID,
		String affiliation, List<String> phones, List<String> emails) {
		Person component = null;
		try {
			component = new Person(TEST_PRODUCER_TYPE, surname, names, userID, affiliation, phones, emails,
				SecurityAttributesTest.getFixture(false));
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".entityType\" content=\"Person\" />\n");
		for (String name : TEST_NAMES)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".name\" content=\"").append(name)
				.append("\" />\n");
		for (String phone : TEST_PHONES)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".phone\" content=\"").append(phone)
				.append("\" />\n");
		for (String email : TEST_EMAILS)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".email\" content=\"").append(email)
				.append("\" />\n");
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".classification\" content=\"U\" />\n");
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".ownerProducer\" content=\"USA\" />\n");
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".surname\" content=\"").append(TEST_SURNAME)
			.append("\" />\n");
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".userid\" content=\"").append(TEST_USERID)
			.append("\" />\n");
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".affiliation\" content=\"")
			.append(TEST_AFFILIATION).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(Util.capitalize(TEST_PRODUCER_TYPE)).append(" EntityType: Person\n");
		for (String name : TEST_NAMES)
			text.append("Name: ").append(name).append("\n");
		for (String phone : TEST_PHONES)
			text.append("Phone Number: ").append(phone).append("\n");
		for (String email : TEST_EMAILS)
			text.append("Email: ").append(email).append("\n");
		text.append(Util.capitalize(TEST_PRODUCER_TYPE)).append(" Classification: U\n");
		text.append(Util.capitalize(TEST_PRODUCER_TYPE)).append(" ownerProducer: USA\n");
		text.append("Surname: ").append(TEST_SURNAME).append("\n");
		text.append("UserID: ").append(TEST_USERID).append("\n");
		text.append("Affiliation: ").append(TEST_AFFILIATION).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:").append(TEST_PRODUCER_TYPE).append(" xmlns:ddms=\"").append(
			DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		xml.append("xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIcismNamespace())
			.append("\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"><ddms:Person>\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		xml.append("\t<ddms:surname>").append(TEST_SURNAME).append("</ddms:surname>\n");
		xml.append("\t<ddms:userID>").append(TEST_USERID).append("</ddms:userID>\n");
		xml.append("\t<ddms:affiliation>").append(TEST_AFFILIATION).append("</ddms:affiliation>\n");
		for (String phone : TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email : TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		xml.append("</ddms:Person></ddms:").append(TEST_PRODUCER_TYPE).append(">");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(TEST_PRODUCER_TYPE, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + TEST_PRODUCER_TYPE, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(Person.NAME, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES,
				TEST_EMAILS);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing name
			Element entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			testConstructor(WILL_FAIL, entityElement);

			// Empty name
			entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("name", ""));
			testConstructor(WILL_FAIL, entityElement);

			// Missing surname
			entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_FAIL, entityElement);

			// Empty surname
			entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("surname", ""));
			testConstructor(WILL_FAIL, entityElement);

			// Too many surnames
			Element element = Util.buildDDMSElement(Person.NAME, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_FAIL, entityElement);

			// Too many userIds
			element = Util.buildDDMSElement(Person.NAME, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			element.appendChild(Util.buildDDMSElement("userID", TEST_USERID));
			element.appendChild(Util.buildDDMSElement("userID", TEST_USERID));
			testConstructor(WILL_FAIL, entityElement);

			// Too many affiliations
			element = Util.buildDDMSElement(Person.NAME, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			element.appendChild(Util.buildDDMSElement("affiliation", TEST_AFFILIATION));
			element.appendChild(Util.buildDDMSElement("affiliation", TEST_AFFILIATION));
			testConstructor(WILL_FAIL, entityElement);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing name
			testConstructor(WILL_FAIL, TEST_SURNAME, null, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Empty name
			List<String> names = new ArrayList<String>();
			names.add("");
			testConstructor(WILL_FAIL, TEST_SURNAME, names, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Missing surname
			testConstructor(WILL_FAIL, null, TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Empty surname
			testConstructor(WILL_FAIL, "", TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty userID
			Element entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("name", "name"));
			entityElement.appendChild(Util.buildDDMSElement("surname", "name"));
			entityElement.appendChild(Util.buildDDMSElement("userID", ""));
			Element producerElement = Util.buildDDMSElement("creator", null);
			producerElement.appendChild(entityElement);
			component = new Person(producerElement);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A ddms:userID element was found with no value.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:creator/ddms:Person", component.getValidationWarnings().get(0).getLocator());

			// Empty affiliation
			entityElement = Util.buildDDMSElement(Person.NAME, null);
			entityElement.appendChild(Util.buildDDMSElement("name", "name"));
			entityElement.appendChild(Util.buildDDMSElement("surname", "name"));
			entityElement.appendChild(Util.buildDDMSElement("affiliation", ""));
			producerElement = Util.buildDDMSElement("creator", null);
			producerElement.appendChild(entityElement);
			component = new Person(producerElement);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A ddms:affiliation element was found with no value.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:creator/ddms:Person", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Person dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID,
				TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Person dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_NAMES, TEST_USERID,
				TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			List<String> differentNames = new ArrayList<String>();
			differentNames.add("Brian");
			dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, differentNames, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, DIFFERENT_VALUE, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, DIFFERENT_VALUE,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				null, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testEntityType() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Person.NAME, component.getEntityType());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = new Person(TEST_PRODUCER_TYPE, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS, SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Person component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			Person.Builder builder = new Person.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new Person.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new Person.Builder();
			builder.setProducerType(TEST_PRODUCER_TYPE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
