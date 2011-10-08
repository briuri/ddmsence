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
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:source elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SourceTest extends AbstractBaseTestCase {

	private static final String TEST_QUALIFIER = "URL";
	private static final String TEST_VALUE = "http://www.xmethods.com";
	private static final String TEST_SCHEMA_QUALIFIER = "WSDL";
	private static final String TEST_SCHEMA_HREF = "http://www.xmethods.com/sd/2001/TemperatureService?wsdl";

	/**
	 * Constructor
	 */
	public SourceTest() {
		super("source.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Source getFixture() {
		try {
			return (new Source(null, "http://www.xmethods.com", null, null, null));
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
	private Source getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Source component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture().addTo(element);
			component = new Source(element);
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
	 * @param schemaQualifier the value of the schemaQualifier attribute
	 * @param schemaHref the value of the schemaHref attribute
	 * @return a valid object
	 */
	private Source getInstance(String message, String qualifier, String value, String schemaQualifier, String schemaHref) {
		boolean expectFailure = !Util.isEmpty(message);
		Source component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0") ? null
				: SecurityAttributesTest.getFixture());
			component = new Source(qualifier, value, schemaQualifier, schemaHref, attr);
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
		text.append(buildOutput(isHTML, "source.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "source.value", TEST_VALUE));
		text.append(buildOutput(isHTML, "source.schemaQualifier", TEST_SCHEMA_QUALIFIER));
		text.append(buildOutput(isHTML, "source.schemaHref", TEST_SCHEMA_HREF));
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append(buildOutput(isHTML, "source.classification", "U"));
			text.append(buildOutput(isHTML, "source.ownerProducer", "USA"));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:source ").append(getXmlnsDDMS()).append(" ");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append(getXmlnsISM()).append(" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append(
			"\" ");
		xml.append("ddms:schemaQualifier=\"").append(TEST_SCHEMA_QUALIFIER).append("\" ");
		xml.append("ddms:schemaHref=\"").append(TEST_SCHEMA_HREF).append("\" ");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ");
		xml.append("/>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Source
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
			Element element = Util.buildDDMSElement(Source.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);

			// No optional fields
			getInstance(SUCCESS, "", "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Href not URI
			Element element = Util.buildDDMSElement(Source.getName(version), null);
			Util.addDDMSAttribute(element, "schemaHref", INVALID_URI);
			getInstance("Invalid URI", element);
		}
	}

	public void testDataConstructorInvalidHrefNotURI() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Href not URI
			getInstance("Invalid URI", TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, INVALID_URI);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Source component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			Element element = Util.buildDDMSElement(Source.getName(version), null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:source element was found.";
			String locator = "ddms:source";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Source elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Source dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Source elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Source dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, DIFFERENT_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, DIFFERENT_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, DIFFERENT_VALUE, TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Source component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Source component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
			Source component = new Source(TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Source(TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF, SecurityAttributesTest
				.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			Source component = getInstance(SUCCESS, getValidElement(sVersion));
			Source.Builder builder = new Source.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Source.Builder builder = new Source.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Source.Builder builder = new Source.Builder();
			builder.getSecurityAttributes().setClassification("SuperSecret");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "SuperSecret is not a valid enumeration token");
			}
			builder.getSecurityAttributes().setClassification("");
			builder.setQualifier(TEST_QUALIFIER);
			builder.setQualifier(TEST_VALUE);
			builder.commit();
		}
	}
}