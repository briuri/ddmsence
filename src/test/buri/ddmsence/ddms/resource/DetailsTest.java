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
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:details elements</p>
 * 
 * <p> Because a ddms:details is a local component, we cannot load a valid document from a unit test data file. We have to
 * build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class DetailsTest extends AbstractComponentTestCase {
		
	private static final String TEST_VALUE = "This is a revision recall.";

	/**
	 * Constructor
	 */
	public DetailsTest() {
		super(null);
	}
	
	/**
	 * Returns a canned fixed value details for testing.
	 * 
	 * @return a XOM element representing a valid details
	 */
	protected static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(Details.getName(version), TEST_VALUE);
		element.addNamespaceDeclaration(PropertyReader.getProperty("ddms.prefix"), version.getNamespace());
		element.addNamespaceDeclaration(PropertyReader.getProperty("ism.prefix"), version.getIsmNamespace());
		SecurityAttributesTest.getFixture(false).addTo(element);
		return (element);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Details testConstructor(boolean expectFailure, Element element) {
		Details component = null;
		try {
			component = new Details(element);
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
	 * @param value the child text
	 * @return a valid object
	 */
	private Details testConstructor(boolean expectFailure, String value) {
		Details component = null;
		try {
			component = new Details(value, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "details", TEST_VALUE));
		text.append(buildOutput(isHTML, "details.classification", "U"));
		text.append(buildOutput(isHTML, "details.ownerProducer", "USA"));
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:details ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM())
			.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ddms:details>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(Details.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Details.getName(version),
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
			
			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());

			// No optional fields
			Element element = Util.buildDDMSElement(Details.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE);

			// No optional fields
			testConstructor(WILL_SUCCEED, "");
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			// Wrong name
			Element element = Util.buildDDMSElement("unknownName", null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			// Bad security attributes
			try {
				new Details(TEST_VALUE, (SecurityAttributes) null);
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
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			Element element = Util.buildDDMSElement(Details.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ddms:details element was found with no value.";
			String locator = "ddms:details";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			Details elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Details dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			Details elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Details dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			Details elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;
			
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());
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
			
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());
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
			
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new Details(TEST_VALUE, SecurityAttributesTest.getFixture(false));
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
			
			Details component = testConstructor(WILL_SUCCEED, getFixtureElement());

			// Equality after Building
			Details.Builder builder = new Details.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Details.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Details.Builder();
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
