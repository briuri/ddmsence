/* Copyright 2010 - 2013 by Brian Uri!
   
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
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:applicationSoftware elements </p>
 * 
 * <p> Because a ddms:applicationSoftware is a local component, we cannot load a valid document from a unit test data
 * file. We have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ApplicationSoftwareTest extends AbstractBaseTestCase {

	private static final String TEST_VALUE = "IRM Generator 2L-9";

	/**
	 * Constructor
	 */
	public ApplicationSoftwareTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildDDMSElement(ApplicationSoftware.getName(version), TEST_VALUE);
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ism"), version.getIsmNamespace());
			SecurityAttributesTest.getFixture().addTo(element);
			return (element);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static ApplicationSoftware getFixture() {
		try {
			return (new ApplicationSoftware(getFixtureElement()));
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
	private ApplicationSoftware getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ApplicationSoftware component = null;
		try {
			component = new ApplicationSoftware(element);
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
	 * @param value the child text
	 * @return a valid object
	 */
	private ApplicationSoftware getInstance(String message, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		ApplicationSoftware component = null;
		try {
			component = new ApplicationSoftware(value, SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "applicationSoftware", TEST_VALUE));
		text.append(buildOutput(isHTML, "applicationSoftware.classification", "U"));
		text.append(buildOutput(isHTML, "applicationSoftware.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:applicationSoftware ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(
			" ism:classification=\"U\" ism:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ddms:applicationSoftware>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_DDMS_PREFIX,
				ApplicationSoftware.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement());

			// No optional fields
			Element element = Util.buildDDMSElement(ApplicationSoftware.getName(version), null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_VALUE);

			// No optional fields
			getInstance(SUCCESS, "");
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Bad security attributes
			Element element = Util.buildDDMSElement(ApplicationSoftware.getName(version), null);
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Bad security attributes
			try {
				new ApplicationSoftware(TEST_VALUE, (SecurityAttributes) null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ApplicationSoftware component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			if (!version.isAtLeast("5.0")) {
				Element element = Util.buildDDMSElement(ApplicationSoftware.getName(version), null);
				SecurityAttributesTest.getFixture().addTo(element);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:applicationSoftware element was found with no value.";
				String locator = "ddms:applicationSoftware";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware elementComponent = getInstance(SUCCESS, getFixtureElement());
			ApplicationSoftware dataComponent = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware elementComponent = getInstance(SUCCESS, getFixtureElement());
			ApplicationSoftware dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new ApplicationSoftware(TEST_VALUE, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The applicationSoftware element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware component = getInstance(SUCCESS, getFixtureElement());
			ApplicationSoftware.Builder builder = new ApplicationSoftware.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware.Builder builder = new ApplicationSoftware.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApplicationSoftware.Builder builder = new ApplicationSoftware.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
