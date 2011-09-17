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
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:requestorInfo elements</p>
 * 
 * <p> Because a ddms:requestorInfo is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RequestorInfoTest extends AbstractComponentTestCase {

	private static final String TEST_NAME = "Name";
	private static final String TEST_SURNAME = "Surname";

	/**
	 * Constructor
	 */
	public RequestorInfoTest() {
		super(null);
	}

	/**
	 * Returns a canned fixed value requestorInfo for testing.
	 * 
	 * @param useOrg true to put an organization in, false for a person
	 * @return a XOM element representing a valid applicationSoftware
	 */
	protected static Element getFixtureElement(boolean useOrg) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(RequestorInfo.getName(version), null);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		element.appendChild(useOrg ? getOrgFixture().getXOMElementCopy() : getPersonFixture().getXOMElementCopy());
		SecurityAttributesTest.getFixture(false).addTo(element);
		return (element);
	}

	/**
	 * Helper method to create a fixture organization to act as an entity
	 */
	private static Organization getOrgFixture() {
		try {
			return (new Organization(Util.getXsListAsList(TEST_NAME), null, null, null, null, null));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture person to act as an entity
	 */
	private static Person getPersonFixture() {
		try {
			return (new Person(Util.getXsListAsList(TEST_NAME), TEST_SURNAME, null, null, null, null, null));
		} catch (InvalidDDMSException e) {
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
	private RequestorInfo testConstructor(boolean expectFailure, Element element) {
		RequestorInfo component = null;
		try {
			component = new RequestorInfo(element);
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
	 * @param entity the person or organization in this role
	 * @param org the organization
	 */
	private RequestorInfo testConstructor(boolean expectFailure, IRoleEntity entity) {
		RequestorInfo component = null;
		try {
			component = new RequestorInfo(entity, SecurityAttributesTest.getFixture(false));
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
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "requestorInfo.entityType", "organization"));
		text.append(buildOutput(isHTML, "requestorInfo.name", TEST_NAME));
		text.append(buildOutput(isHTML, "requestorInfo.classification", "U"));
		text.append(buildOutput(isHTML, "requestorInfo.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:requestorInfo ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t<ddms:organization>\n");
		xml.append("\t\t<ddms:name>").append(TEST_NAME).append("</ddms:name>\n");
		xml.append("\t</ddms:organization>\n");
		xml.append("</ddms:requestorInfo>\n");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			assertEquals(RequestorInfo.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + RequestorInfo.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields, organization
			testConstructor(WILL_SUCCEED, getFixtureElement(true));

			// All fields, person
			testConstructor(WILL_SUCCEED, getFixtureElement(false));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields, organization
			testConstructor(WILL_SUCCEED, getOrgFixture());

			// All fields, person
			testConstructor(WILL_SUCCEED, getPersonFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing entity
			Element element = Util.buildDDMSElement(RequestorInfo.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildDDMSElement(RequestorInfo.getName(version), null);
			element.appendChild(getOrgFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing entity		
			testConstructor(WILL_FAIL, (IRoleEntity) null);

			// Wrong entity
			testConstructor(WILL_FAIL, new Service(Util.getXsListAsList(TEST_NAME), null, null));

			// Missing security attributes
			try {
				new RequestorInfo(getOrgFixture(), null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			RequestorInfo dataComponent = testConstructor(WILL_SUCCEED, getOrgFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			RequestorInfo dataComponent = testConstructor(WILL_SUCCEED, getPersonFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getOrgFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getOrgFixture());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, getOrgFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RequestorInfo(getOrgFixture(), SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Equality after Building, organization
			RequestorInfo component = testConstructor(WILL_SUCCEED, getFixtureElement(true));
			RequestorInfo.Builder builder = new RequestorInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Equality after Building, person
			component = testConstructor(WILL_SUCCEED, getFixtureElement(false));
			builder = new RequestorInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RequestorInfo.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RequestorInfo.Builder();
			builder.getPerson().setNames(Util.getXsListAsList(TEST_NAME));
			builder.getPerson().setSurname(TEST_SURNAME);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
