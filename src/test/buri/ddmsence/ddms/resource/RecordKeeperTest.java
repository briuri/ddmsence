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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:recordKeeper elements</p>
 * 
 * <p> Because a ddms:recordKeeper is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordKeeperTest extends AbstractComponentTestCase {

	private static final String TEST_ID = "#289-99202.9";
	private static final String TEST_NAME = "AgencyZ";

	/**
	 * Constructor
	 */
	public RecordKeeperTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @return a XOM element representing a valid applicationSoftware
	 */
	public static Element getFixtureElement() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		Element idElement = Util.buildDDMSElement("recordKeeperID", TEST_ID);
		element.appendChild(idElement);
		element.appendChild(getOrgFixture().getXOMElementCopy());
		return (element);
	}

	/**
	 * Returns a fixture object for testing. organization to act as an entity
	 */
	private static Organization getOrgFixture() {
		try {
			return (new Organization(Util.getXsListAsList(TEST_NAME), null, null, null, null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private RecordKeeper testConstructor(boolean expectFailure, Element element) {
		RecordKeeper component = null;
		try {
			component = new RecordKeeper(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param recordKeeperID ID value
	 * @param org the organization
	 */
	private RecordKeeper testConstructor(boolean expectFailure, String recordKeeperID, Organization org) {
		RecordKeeper component = null;
		try {
			component = new RecordKeeper(recordKeeperID, org);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
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

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getFixtureElement()), DEFAULT_DDMS_PREFIX,
				RecordKeeper.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_ID, getOrgFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing recordKeeperID
			Element element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(getOrgFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Empty recordKeeperID
			element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(Util.buildDDMSElement("recordKeeperID", null));
			element.appendChild(getOrgFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing organization
			element = Util.buildDDMSElement(RecordKeeper.getName(version), null);
			element.appendChild(Util.buildDDMSElement("recordKeeperID", TEST_ID));
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing recordKeeperID		
			testConstructor(WILL_FAIL, null, getOrgFixture());

			// Missing organization
			testConstructor(WILL_FAIL, TEST_ID, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RecordKeeper component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			RecordKeeper dataComponent = testConstructor(WILL_SUCCEED, TEST_ID, getOrgFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			RecordKeeper dataComponent = testConstructor(WILL_SUCCEED, "newID", getOrgFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_ID, new Organization(Util.getXsListAsList("DISA"), null,
				null, null, null));
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_ID, getOrgFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_ID, getOrgFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RecordKeeper(TEST_ID, getOrgFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordKeeper component = testConstructor(WILL_SUCCEED, getFixtureElement());

			// Equality after Building
			RecordKeeper.Builder builder = new RecordKeeper.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RecordKeeper.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RecordKeeper.Builder();
			builder.setRecordKeeperID(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.getOrganization().setNames(Util.getXsListAsList(TEST_NAME));
			builder.commit();
		}
	}
}
