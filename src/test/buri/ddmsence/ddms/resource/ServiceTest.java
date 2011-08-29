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
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:service elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ServiceTest extends AbstractComponentTestCase {

	private static final String TEST_PARENT_TYPE = Creator.NAME;

	private static final List<String> TEST_NAMES = new ArrayList<String>();
	private static final List<String> TEST_PHONES = new ArrayList<String>();
	private static final List<String> TEST_EMAILS = new ArrayList<String>();
	static {
		TEST_NAMES.add("https://metadata.dod.mil/ebxmlquery/soap");
		TEST_PHONES.add("703-882-1000");
		TEST_EMAILS.add("ddms@fgm.com");
	}

	/**
	 * Constructor
	 */
	public ServiceTest() {
		super("service.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Service testConstructor(boolean expectFailure, Element element) {
		Service component = null;
		try {
			component = new Service(TEST_PARENT_TYPE, element);
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
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	private Service testConstructor(boolean expectFailure, List<String> names, List<String> phones, 
		List<String> emails) {
		Service component = null;
		try {
			component = new Service(TEST_PARENT_TYPE, names, phones, emails);
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
		html.append("<meta name=\"").append(TEST_PARENT_TYPE).append(".entityType\" content=\"Service\" />\n");
		for (String name : TEST_NAMES)
			html.append("<meta name=\"").append(TEST_PARENT_TYPE).append(".name\" content=\"").append(name)
			.append("\" />\n");
		for (String phone : TEST_PHONES)
			html.append("<meta name=\"").append(TEST_PARENT_TYPE).append(".phone\" content=\"").append(phone)
			.append("\" />\n");
		for (String email : TEST_EMAILS)
			html.append("<meta name=\"").append(TEST_PARENT_TYPE).append(".email\" content=\"").append(email)
			.append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(TEST_PARENT_TYPE).append(" EntityType: Service\n");
		for (String name : TEST_NAMES)
			text.append("name: ").append(name).append("\n");
		for (String phone : TEST_PHONES)
			text.append("phone: ").append(phone).append("\n");
		for (String email : TEST_EMAILS)
			text.append("email: ").append(email).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:Service xmlns:ddms=\"").append(
			DDMSVersion.getCurrentVersion().getNamespace()).append("\">\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		for (String phone : TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email : TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		xml.append("</ddms:Service>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Service.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Service.NAME, component.getQualifiedName());

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
			Element element = Util.buildDDMSElement(Service.NAME, null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing name
			Element element = Util.buildDDMSElement(Service.NAME, null);
			testConstructor(WILL_FAIL, element);

			// Empty name
			element = Util.buildDDMSElement(Service.NAME, null);
			element.appendChild(Util.buildDDMSElement("name", ""));
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing name		
			testConstructor(WILL_FAIL, null, TEST_PHONES, TEST_EMAILS);

			// Empty name
			List<String> names = new ArrayList<String>();
			names.add("");
			testConstructor(WILL_FAIL, names, TEST_PHONES, TEST_EMAILS);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Service dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Service dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			Service.Builder builder = new Service.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new Service.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new Service.Builder();
			builder.setParentType(TEST_PARENT_TYPE);
			builder.setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Service.Builder builder = new Service.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getPhones().get(1));
			assertNotNull(builder.getEmails().get(1));			
		}
	}
}
