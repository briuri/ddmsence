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

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:recordKeeper elements </p>
 * 
 * <p> Because a ddms:recordKeeper is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordKeeperTest extends AbstractBaseTestCase {

	private static final String TEST_ID = "#289-99202.9";
	private static final String TEST_NAME = "DISA";

	/**
	 * Constructor
	 */
	public RecordKeeperTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		Element idElement = Util.buildDDMSElement("recordKeeperID", TEST_ID);
		element.appendChild(idElement);
		element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
		return (element);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static RecordKeeper getFixture() {
		try {
			return (new RecordKeeper(getFixtureElement()));
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
	private RecordKeeper getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		RecordKeeper component = null;
		try {
			component = new RecordKeeper(element);
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
	 * @param recordKeeperID ID value
	 * @param org the organization
	 */
	private RecordKeeper getInstance(String message, String recordKeeperID, Organization org) {
		boolean expectFailure = !Util.isEmpty(message);
		RecordKeeper component = null;
		try {
			component = new RecordKeeper(recordKeeperID, org);
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
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "recordKeeper.recordKeeperID", TEST_ID));
		text.append(buildOutput(isHTML, "recordKeeper.entityType", "organization"));
		text.append(buildOutput(isHTML, "recordKeeper.name", TEST_NAME));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:recordKeeper ").append(getXmlnsDDMS()).append(">");
		xml.append("<ddms:recordKeeperID>").append(TEST_ID).append("</ddms:recordKeeperID>");
		xml.append("<ddms:organization>");
		xml.append("<ddms:name>").append(TEST_NAME).append("</ddms:name>");
		xml.append("</ddms:organization>");
		xml.append("</ddms:recordKeeper>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_DDMS_PREFIX, RecordKeeper
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_ID, OrganizationTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing recordKeeperID
			Element element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
			getInstance("record keeper ID is required.", element);

			// Empty recordKeeperID
			element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(Util.buildDDMSElement("recordKeeperID", null));
			element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
			getInstance("record keeper ID is required.", element);

			// Missing organization
			element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(Util.buildDDMSElement("recordKeeperID", TEST_ID));
			getInstance("organization is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing recordKeeperID
			getInstance("record keeper ID is required.", null, OrganizationTest.getFixture());

			// Missing organization
			getInstance("organization is required.", TEST_ID, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RecordKeeper component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper elementComponent = getInstance(SUCCESS, getFixtureElement());
			RecordKeeper dataComponent = getInstance(SUCCESS, TEST_ID, OrganizationTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper elementComponent = getInstance(SUCCESS, getFixtureElement());
			RecordKeeper dataComponent = getInstance(SUCCESS, "newID", OrganizationTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_ID, new Organization(Util.getXsListAsList("DARPA"), null, null,
				null, null));
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_ID, OrganizationTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_ID, OrganizationTest.getFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RecordKeeper(TEST_ID, OrganizationTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The recordKeeper element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			RecordKeeper component = getInstance(SUCCESS, getFixtureElement());
			RecordKeeper.Builder builder = new RecordKeeper.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper.Builder builder = new RecordKeeper.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setRecordKeeperID(TEST_ID);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper.Builder builder = new RecordKeeper.Builder();
			builder.setRecordKeeperID(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "organization is required.");
			}
			builder.getOrganization().setNames(Util.getXsListAsList(TEST_NAME));
			builder.commit();
		}
	}
}
