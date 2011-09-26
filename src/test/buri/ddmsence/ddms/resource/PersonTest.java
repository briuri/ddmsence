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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:person elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PersonTest extends AbstractComponentTestCase {

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
	 * Returns a fixture object for testing.
	 */
	public static Person getFixture() {
		try {
			return (new Person(Util.getXsListAsList("Brian"), TEST_SURNAME, null, null, null, null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}
		
	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Person getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Person component = null;
		try {
			component = new Person(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param surname the surname of the person
	 * @param names an ordered list of names
	 * @param userID optional unique identifier within an organization
	 * @param affiliation organizational affiliation of the person
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	private Person getInstance(String message, String surname, List<String> names, String userID,
		String affiliation, List<String> phones, List<String> emails) {
		boolean expectFailure = !Util.isEmpty(message);
		Person component = null;
		try {
			component = new Person(names, surname, phones, emails, userID, affiliation);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "entityType", Person.getName(version)));
		for (String name : TEST_NAMES)
			text.append(buildOutput(isHTML, "name", name));
		for (String phone : TEST_PHONES)
			text.append(buildOutput(isHTML, "phone", phone));
		for (String email : TEST_EMAILS)
			text.append(buildOutput(isHTML, "email", email));
		text.append(buildOutput(isHTML, "surname", TEST_SURNAME));
		text.append(buildOutput(isHTML, "userID", TEST_USERID));
		text.append(buildOutput(isHTML, "affiliation", TEST_AFFILIATION));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:").append(Person.getName(version)).append(" ").append(getXmlnsDDMS()).append(">\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		xml.append("\t<ddms:surname>").append(TEST_SURNAME).append("</ddms:surname>\n");
		if (version.isAtLeast("4.0")) {
			for (String phone : TEST_PHONES)
				xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
			for (String email : TEST_EMAILS)
				xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
			xml.append("\t<ddms:userID>").append(TEST_USERID).append("</ddms:userID>\n");
			xml.append("\t<ddms:affiliation>").append(TEST_AFFILIATION).append("</ddms:affiliation>\n");
		}
		else {
			xml.append("\t<ddms:userID>").append(TEST_USERID).append("</ddms:userID>\n");
			xml.append("\t<ddms:affiliation>").append(TEST_AFFILIATION).append("</ddms:affiliation>\n");
			for (String phone : TEST_PHONES)
				xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
			for (String email : TEST_EMAILS)
				xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		}
		xml.append("</ddms:").append(Person.getName(version)).append(">");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Person.getName(version));
			getInstance("Unexpected namespace URI and local name encountered: ddms:wrongName", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Person.getName(version), null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES,
				TEST_EMAILS);

			// No optional fields
			getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String personName = Person.getName(version);
			// Missing name
			Element entityElement = Util.buildDDMSElement(personName, null);
			entityElement.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			getInstance("moo", entityElement);

			// Empty name
			entityElement = Util.buildDDMSElement(personName, null);
			entityElement.appendChild(Util.buildDDMSElement("name", ""));
			getInstance("moo", entityElement);

			// Missing surname
			entityElement = Util.buildDDMSElement(personName, null);
			entityElement.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			getInstance("moo", entityElement);

			// Empty surname
			entityElement = Util.buildDDMSElement(personName, null);
			entityElement.appendChild(Util.buildDDMSElement("surname", ""));
			getInstance("moo", entityElement);

			// Too many surnames
			Element element = Util.buildDDMSElement(personName, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			getInstance("moo", entityElement);

			// Too many userIds
			element = Util.buildDDMSElement(personName, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			element.appendChild(Util.buildDDMSElement("userID", TEST_USERID));
			element.appendChild(Util.buildDDMSElement("userID", TEST_USERID));
			getInstance("moo", entityElement);

			// Too many affiliations
			element = Util.buildDDMSElement(personName, null);
			element.appendChild(Util.buildDDMSElement("surname", TEST_SURNAME));
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			element.appendChild(Util.buildDDMSElement("affiliation", TEST_AFFILIATION));
			element.appendChild(Util.buildDDMSElement("affiliation", TEST_AFFILIATION));
			getInstance("moo", entityElement);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing name
			getInstance("moo", TEST_SURNAME, null, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Empty name
			List<String> names = new ArrayList<String>();
			names.add("");
			getInstance("moo", TEST_SURNAME, names, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Missing surname
			getInstance("moo", null, TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);

			// Empty surname
			getInstance("moo", "", TEST_NAMES, TEST_USERID, TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Person component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty userID
			Element entityElement = Util.buildDDMSElement(Person.getName(version), null);
			entityElement.appendChild(Util.buildDDMSElement("name", "name"));
			entityElement.appendChild(Util.buildDDMSElement("surname", "name"));
			entityElement.appendChild(Util.buildDDMSElement("userID", ""));
			component = new Person(entityElement);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ddms:userID element was found with no value.";
			String locator = "ddms:" + Person.getName(version);
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Empty affiliation
			entityElement = Util.buildDDMSElement(Person.getName(version), null);
			entityElement.appendChild(Util.buildDDMSElement("name", "name"));
			entityElement.appendChild(Util.buildDDMSElement("surname", "name"));
			entityElement.appendChild(Util.buildDDMSElement("affiliation", ""));
			component = new Person(entityElement);
			assertEquals(1, component.getValidationWarnings().size());
			text = "A ddms:affiliation element was found with no value.";
			locator = "ddms:" + Person.getName(version);
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Person dataComponent = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID,
				TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Person dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_NAMES, TEST_USERID,
				TEST_AFFILIATION, TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			List<String> differentNames = new ArrayList<String>();
			differentNames.add("Brian");
			dataComponent = getInstance(SUCCESS, TEST_SURNAME, differentNames, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, DIFFERENT_VALUE, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, DIFFERENT_VALUE,
				TEST_PHONES, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				null, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_SURNAME, TEST_NAMES, TEST_USERID, TEST_AFFILIATION,
				TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Person.Builder builder = new Person.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Person.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Person.Builder();
			builder.setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "moo");
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Person.Builder builder = new Person.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getPhones().get(1));
			assertNotNull(builder.getEmails().get(1));
		}
	}
}
