/* Copyright 2010 - 2019 by Brian Uri!
   
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
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.ddms.resource;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:unknown elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class UnknownTest extends AbstractBaseTestCase {

	private static final List<String> TEST_NAMES = new ArrayList<String>();
	private static final List<String> TEST_PHONES = new ArrayList<String>();
	private static final List<String> TEST_EMAILS = new ArrayList<String>();
	private static final List<String> TEST_AFFILIATIONS = new ArrayList<String>();
	static {
		TEST_NAMES.add("Unknown Entity");
		TEST_PHONES.add("703-882-1000");
		TEST_EMAILS.add("ddms@novetta.com");
		TEST_AFFILIATIONS.add("DISA");
	}

	/**
	 * Constructor
	 */
	public UnknownTest() {
		super("unknown.xml");
		removeSupportedVersions("2.0");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Unknown getFixture() {
		try {
			return (new Unknown(Util.getXsListAsList("UnknownEntity"), null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * @param element the element to build from
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Unknown getInstance(Element element, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		Unknown component = null;
		try {
			component = new Unknown(element);
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
	 * @param builder the builder to commit
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Unknown getInstance(Unknown.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		Unknown component = null;
		try {
			component = builder.commit();
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns a builder, pre-populated with base data from the XML sample.
	 * 
	 * This builder can then be modified to test various conditions.
	 */
	private Unknown.Builder getBaseBuilder() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Unknown component = getInstance(getValidElement(version.getVersion()), SUCCESS);
		return (new Unknown.Builder(component));
	}

	/**
	 * Returns the expected output for the test instance of this component
	 */
	private String getExpectedHTMLTextOutput(OutputFormat format) throws InvalidDDMSException {
		Util.requireHTMLText(format);
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(buildHTMLTextOutput(format, "entityType", Unknown.getName(version)));
		for (String name : TEST_NAMES)
			text.append(buildHTMLTextOutput(format, "name", name));
		for (String phone : TEST_PHONES)
			text.append(buildHTMLTextOutput(format, "phone", phone));
		for (String email : TEST_EMAILS)
			text.append(buildHTMLTextOutput(format, "email", email));
		if (version.isAtLeast("5.0")) {
			for (String affiliation : TEST_AFFILIATIONS)
				text.append(buildHTMLTextOutput(format, "affiliation", affiliation));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected JSON output for this unit test
	 */
	private String getExpectedJSONOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer json = new StringBuffer();
		json.append("{\"entityType\":\"unknown\",\"name\":[\"Unknown Entity\"],\"phone\":[\"703-882-1000\"],\"email\":[\"ddms@novetta.com\"]");
		if (version.isAtLeast("5.0")) {
			json.append(",\"affiliation\":[\"DISA\"]");
		}
		json.append("}");
		return (json.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:").append(Unknown.getName(version)).append(" ").append(getXmlnsDDMS()).append(">\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		for (String phone : TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email : TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		if (version.isAtLeast("5.0")) {
			for (String affiliation : TEST_AFFILIATIONS)
				xml.append("\t<ddms:affiliation>").append(affiliation).append("</ddms:affiliation>\n");
		}
		xml.append("</ddms:").append(Unknown.getName(version)).append(">");
		return (xml.toString());
	}

	@Test
	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_DDMS_PREFIX,
				Unknown.getName(version));
			getInstance(getWrongNameElementFixture(), WRONG_NAME_MESSAGE);
		}
	}
	
	@Test
	public void testConstructors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based
			getInstance(getValidElement(sVersion), SUCCESS);
			
			// Data-based via Builder
			getBaseBuilder();
		}
	}
	
	@Test
	public void testConstructorsMinimal() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, No optional fields
			Element element = Util.buildDDMSElement(Unknown.getName(version), null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			Unknown elementComponent = getInstance(element, SUCCESS);
			
			// Data-based, No optional fields
			getInstance(new Unknown.Builder(elementComponent), SUCCESS);
			
			// Null affiliation list
			new Unknown(TEST_NAMES, null, null, null, null);
		}
	}

	@Test
	public void testValidationErrors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// Missing name
			Unknown.Builder builder = getBaseBuilder();
			builder.getNames().clear();
			getInstance(builder, "At least 1 name element must");
		}
	}

	@Test
	public void testValidationWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// No warnings
			Unknown component = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	@Test
	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			Unknown elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			Unknown builderComponent = new Unknown.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Different values in each field	
			Unknown.Builder builder = getBaseBuilder();
			builder.getNames().set(0, "Ellen");
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.getPhones().clear();
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.getEmails().clear();
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.getPhones().clear();
			assertFalse(elementComponent.equals(builder.commit()));
			
			if (version.isAtLeast("5.0")) {
				builder = getBaseBuilder();
				builder.getAffiliations().add("DISA");
				assertFalse(elementComponent.equals(builder.commit()));
			}
		}
	}

	@Test
	public void testVersionSpecific() throws InvalidDDMSException {
		Unknown.Builder builder = getBaseBuilder();
		DDMSVersion.setCurrentVersion("2.0");
		getInstance(builder, "The Unknown element must not be used");
	}

	@Test
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Unknown elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.HTML), elementComponent.toHTML());
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.TEXT), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(), elementComponent.toXML());
			assertEquals(getExpectedJSONOutput(), elementComponent.toJSON());
			assertValidJSON(elementComponent.toJSON());
		}
	}

	@Test
	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Unknown.Builder builder = new Unknown.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			
			builder.setNames(TEST_NAMES);
			assertFalse(builder.isEmpty());
		}
	}

	@Test
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Unknown.Builder builder = new Unknown.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getPhones().get(1));
			assertNotNull(builder.getEmails().get(1));
		}
	}
}
