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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:subOrganization elements</p>
 * 
 * <p> Because a ddms:subOrganization is a local component, we cannot load a valid document from a unit test data file.
 * We have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class SubOrganizationTest extends AbstractComponentTestCase {

	private static final String TEST_VALUE = "PEO-GES";

	/**
	 * Constructor
	 */
	public SubOrganizationTest() {
		super(null);
	}

	/**
	 * Generates a list of suborganizations for testing.
	 */
	public static List<SubOrganization> getFixtureList() throws InvalidDDMSException {
		if (!DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
			return (null);
		List<SubOrganization> subOrgs = new ArrayList<SubOrganization>();
		subOrgs.add(new SubOrganization("sub1", SecurityAttributesTest.getFixture(false)));
		subOrgs.add(new SubOrganization("sub2", SecurityAttributesTest.getFixture(false)));
		return (subOrgs);
	}

	/**
	 * Returns a canned fixed value resource for testing.
	 * 
	 * @return a XOM element representing a valid resource
	 */
	private static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(SubOrganization.getName(version), TEST_VALUE);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ism"), version.getIsmNamespace());
		SecurityAttributesTest.getFixture(false).addTo(element);
		return (element);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private SubOrganization testConstructor(boolean expectFailure, Element element) {
		SubOrganization component = null;
		try {
			component = new SubOrganization(element);
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
	 * @param value the value
	 * @return a valid object
	 */
	private SubOrganization testConstructor(boolean expectFailure, String value) {
		SubOrganization component = null;
		try {
			component = new SubOrganization(value, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "subOrganization", TEST_VALUE));
		text.append(buildOutput(isHTML, "subOrganization.classification", "U"));
		text.append(buildOutput(isHTML, "subOrganization.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subOrganization ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM())
			.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE);
		xml.append("</ddms:subOrganization>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(SubOrganization.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + SubOrganization.getName(version),
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

			testConstructor(WILL_SUCCEED, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			testConstructor(WILL_SUCCEED, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing child text
			Element element = Util.buildDDMSElement(SubOrganization.getName(version), null);
			testConstructor(WILL_FAIL, element);

			// Empty child text
			element = Util.buildDDMSElement(SubOrganization.getName(version), "");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing child text
			testConstructor(WILL_FAIL, (String) null);

			// Empty child text
			testConstructor(WILL_FAIL, "");
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			SubOrganization dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			SubOrganization dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new SubOrganization(TEST_VALUE, SecurityAttributesTest.getFixture(false));
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

			SubOrganization component = testConstructor(WILL_SUCCEED, getFixtureElement());

			// Equality after Building
			SubOrganization.Builder builder = new SubOrganization.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new SubOrganization.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new SubOrganization.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
