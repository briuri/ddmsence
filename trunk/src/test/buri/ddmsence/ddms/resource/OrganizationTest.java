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

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>
 * Tests related to ddms:organization elements
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class OrganizationTest extends AbstractBaseTestCase {

	private static final List<String> TEST_NAMES = new ArrayList<String>();
	private static final List<String> TEST_PHONES = new ArrayList<String>();
	private static final List<String> TEST_EMAILS = new ArrayList<String>();
	static {
		TEST_NAMES.add("DISA");
		TEST_NAMES.add("PEO-GES");
		TEST_PHONES.add("703-882-1000");
		TEST_PHONES.add("703-885-1000");
		TEST_EMAILS.add("ddms@fgm.com");
	}

	/**
	 * Constructor
	 */
	public OrganizationTest() {
		super("organization.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Organization getFixture() {
		try {
			return (new Organization(Util.getXsListAsList("DISA"), null, null, null, null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Generates an acronym for testing.
	 */
	private String getAcronym() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? "DISA" : "");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Organization getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Organization component = null;
		try {
			component = new Organization(element);
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
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param subOrganizations an ordered list of suborganizations
	 * @param acronym the organization acronym
	 */
	private Organization getInstance(String message, List<String> names, List<String> phones, List<String> emails,
		List<SubOrganization> subOrganizations, String acronym) {
		boolean expectFailure = !Util.isEmpty(message);
		Organization component = null;
		try {
			component = new Organization(names, phones, emails, subOrganizations, acronym, null);
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
		text.append(buildOutput(isHTML, "entityType", Organization.getName(version)));
		for (String name : TEST_NAMES)
			text.append(buildOutput(isHTML, "name", name));
		for (String phone : TEST_PHONES)
			text.append(buildOutput(isHTML, "phone", phone));
		for (String email : TEST_EMAILS)
			text.append(buildOutput(isHTML, "email", email));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, "subOrganization", "sub1"));
			text.append(buildOutput(isHTML, "subOrganization.classification", "U"));
			text.append(buildOutput(isHTML, "subOrganization.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, "subOrganization", "sub2"));
			text.append(buildOutput(isHTML, "subOrganization.classification", "U"));
			text.append(buildOutput(isHTML, "subOrganization.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, "acronym", "DISA"));
		}
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
		xml.append("<ddms:").append(Organization.getName(version)).append(" ").append(getXmlnsDDMS());
		if (version.isAtLeast("4.0"))
			xml.append(" ddms:acronym=\"DISA\"");
		xml.append(">\n");
		for (String name : TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		for (String phone : TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email : TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:subOrganization ").append(getXmlnsISM()).append(
				" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">sub1</ddms:subOrganization>\n");
			xml.append("\t<ddms:subOrganization ").append(getXmlnsISM()).append(
				" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">sub2</ddms:subOrganization>\n");
		}
		xml.append("</ddms:").append(Organization.getName(version)).append(">");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Organization
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Organization.getName(version), null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, SubOrganizationTest.getFixtureList(),
				getAcronym());

			// No optional fields
			getInstance(SUCCESS, TEST_NAMES, null, null, SubOrganizationTest.getFixtureList(), getAcronym());
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing name
			Element entityElement = Util.buildDDMSElement(Organization.getName(version), null);
			getInstance("At least 1 name element must exist.", entityElement);

			// Empty name
			entityElement = Util.buildDDMSElement(Organization.getName(version), null);
			entityElement.appendChild(Util.buildDDMSElement("name", ""));
			getInstance("At least 1 name element must have a non-empty value.", entityElement);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing name
			getInstance("At least 1 name element must exist.", null, TEST_PHONES, TEST_EMAILS, SubOrganizationTest.getFixtureList(), getAcronym());

			// Empty name
			List<String> names = new ArrayList<String>();
			names.add("");
			getInstance("At least 1 name element must have a non-empty value.", names, TEST_PHONES, TEST_EMAILS, SubOrganizationTest.getFixtureList(), getAcronym());
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Organization component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty acronym in DDMS 4.0
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Organization.getName(version), null);
				element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
				element.addAttribute(new Attribute("ddms:acronym", version.getNamespace(), ""));
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:acronym attribute was found with no value.";
				String locator = "ddms:organization";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Organization elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Organization dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, SubOrganizationTest
				.getFixtureList(), getAcronym());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Organization elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Organization dataComponent = getInstance(SUCCESS, TEST_NAMES, null, TEST_EMAILS, SubOrganizationTest
				.getFixtureList(), getAcronym());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, null, SubOrganizationTest.getFixtureList(),
				getAcronym());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.0")) {
				dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, null, getAcronym());
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, SubOrganizationTest
					.getFixtureList(), "NewACRONYM");
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Organization component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS,
				SubOrganizationTest.getFixtureList(), getAcronym());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Organization component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS,
				SubOrganizationTest.getFixtureList(), getAcronym());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSubOrganizationReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<SubOrganization> subOrgs = SubOrganizationTest.getFixtureList();
			getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, subOrgs, getAcronym());
			getInstance(SUCCESS, TEST_NAMES, TEST_PHONES, TEST_EMAILS, subOrgs, getAcronym());
		}
	}

	public void testWrongVersionAcronym() {
		DDMSVersion.setCurrentVersion("4.0");
		Organization component = getInstance(SUCCESS, getValidElement("4.0"));
		Organization.Builder builder = new Organization.Builder(component);
		builder.getSubOrganizations().clear();
		try {
			DDMSVersion.setCurrentVersion("3.1");
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "An organization cannot have an acronym");
		}
	}

	public void testWrongVersion() {
		// This test is implicit -- SubOrganization cannot even be instantiated except in DDMS 4.0 or later.
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Organization component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Organization.Builder builder = new Organization.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Organization.Builder();
			assertNull(builder.commit());

			// List Emptiness
			if (version.isAtLeast("4.0")) {
				assertTrue(builder.isEmpty());
				builder.getSubOrganizations().get(0);
				assertTrue(builder.isEmpty());
				builder.getSubOrganizations().get(0).setValue("TEST");
				assertFalse(builder.isEmpty());
			}
			// Validation
			builder = new Organization.Builder();
			builder.setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least 1 name element must exist.");
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Organization.Builder builder = new Organization.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getPhones().get(1));
			assertNotNull(builder.getEmails().get(1));
		}
	}
}
