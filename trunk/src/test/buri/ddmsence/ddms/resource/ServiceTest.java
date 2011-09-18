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
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:service elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ServiceTest extends AbstractComponentTestCase {

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
			component = new Service(element);
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
	private Service testConstructor(boolean expectFailure, List<String> names, List<String> phones, List<String> emails) {
		Service component = null;
		try {
			component = new Service(names, phones, emails);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "entityType", Service.getName(version)));
		for (String name : TEST_NAMES)
			text.append(buildOutput(isHTML, "name", name));
		for (String phone : TEST_PHONES)
			text.append(buildOutput(isHTML, "phone", phone));
		for (String email : TEST_EMAILS)
			text.append(buildOutput(isHTML, "email", email));
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
		xml.append("<ddms:").append(Service.getName(version)).append(" ").append(getXmlnsDDMS()).append(">\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		for (String phone : TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email : TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		xml.append("</ddms:").append(Service.getName(version)).append(">");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Service.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + Service.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(Service.getName(version), null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Missing name
			Element element = Util.buildDDMSElement(Service.getName(version), null);
			testConstructor(WILL_FAIL, element);

			// Empty name
			element = Util.buildDDMSElement(Service.getName(version), null);
			element.appendChild(Util.buildDDMSElement("name", ""));
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing name		
			testConstructor(WILL_FAIL, null, TEST_PHONES, TEST_EMAILS);

			// Empty name
			List<String> names = new ArrayList<String>();
			names.add("");
			testConstructor(WILL_FAIL, names, TEST_PHONES, TEST_EMAILS);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Service component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Service dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Service dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, TEST_EMAILS);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
			
			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Service.Builder builder = new Service.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Service.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Service.Builder();
			builder.setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Service.Builder builder = new Service.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getPhones().get(1));
			assertNotNull(builder.getEmails().get(1));
		}
	}
}
