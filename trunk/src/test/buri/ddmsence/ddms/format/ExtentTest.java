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
package buri.ddmsence.ddms.format;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:extent elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ExtentTest extends AbstractBaseTestCase {

	private static final String TEST_QUALIFIER = "sizeBytes";
	private static final String TEST_VALUE = "75000";

	/**
	 * Constructor
	 */
	public ExtentTest() {
		super("extent.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Extent getFixture() {
		try {
			return (new Extent(TEST_QUALIFIER, TEST_VALUE));
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
	private Extent getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Extent component = null;
		try {
			component = new Extent(element);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private Extent getInstance(String message, String qualifier, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		Extent component = null;
		try {
			component = new Extent(qualifier, value);
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
	private String getExpectedOutput(boolean isHTML) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "extent.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "extent.value", TEST_VALUE));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:extent ").append(getXmlnsDDMS()).append(" ddms:qualifier=\"").append(TEST_QUALIFIER).append(
			"\" ddms:value=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Extent.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Extent.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			getInstance(SUCCESS, "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing qualifier
			Element element = Util.buildDDMSElement(Extent.getName(version), null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			getInstance("qualifier attribute is required.", element);

			// Qualifier not URI
			element = Util.buildDDMSElement(Extent.getName(version), null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			Util.addDDMSAttribute(element, "qualifier", INVALID_URI);
			getInstance("Invalid URI", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing qualifier
			getInstance("qualifier attribute is required.", null, TEST_VALUE);

			// Qualifier not URI
			getInstance("Invalid URI", INVALID_URI, TEST_VALUE);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String extentName = Extent.getName(version);

			// No warnings
			Extent component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Element element = Util.buildDDMSElement(extentName, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());

			String text = "A qualifier has been set without an accompanying value attribute.";
			String locator = "ddms:extent";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Neither attribute
			element = Util.buildDDMSElement(extentName, null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			text = "A completely empty ddms:extent element was found.";
			locator = "ddms:extent";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Extent dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Extent dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent component = getInstance(SUCCESS, getValidElement(sVersion));
			Extent.Builder builder = new Extent.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent.Builder builder = new Extent.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent.Builder builder = new Extent.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "qualifier attribute is required.");
			}
			builder.setQualifier(TEST_QUALIFIER);
			builder.commit();
		}
	}
}
