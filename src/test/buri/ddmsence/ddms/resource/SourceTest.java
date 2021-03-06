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
import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
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
	 * @param element the element to build from
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Source getInstance(Element element, String message) {
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
	 * @param builder the builder to commit
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Source getInstance(Source.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		Source component = null;
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
	private Source.Builder getBaseBuilder() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Source component = getInstance(getValidElement(version.getVersion()), SUCCESS);
		return (new Source.Builder(component));
	}

	/**
	 * Returns the expected output for the test instance of this component
	 */
	private String getExpectedHTMLTextOutput(OutputFormat format) throws InvalidDDMSException {
		Util.requireHTMLText(format);
		StringBuffer text = new StringBuffer();
		text.append(buildHTMLTextOutput(format, "source.qualifier", TEST_QUALIFIER));
		text.append(buildHTMLTextOutput(format, "source.value", TEST_VALUE));
		text.append(buildHTMLTextOutput(format, "source.schemaQualifier", TEST_SCHEMA_QUALIFIER));
		text.append(buildHTMLTextOutput(format, "source.schemaHref", TEST_SCHEMA_HREF));
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append(buildHTMLTextOutput(format, "source.classification", "U"));
			text.append(buildHTMLTextOutput(format, "source.ownerProducer", "USA"));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected JSON output for this unit test
	 */
	private String getExpectedJSONOutput() {
		StringBuffer json = new StringBuffer();
		json.append("{\"qualifier\":\"URL\",\"value\":\"http://www.xmethods.com\",\"schemaQualifier\":\"WSDL\",");
		json.append("\"schemaHref\":\"http://www.xmethods.com/sd/2001/TemperatureService?wsdl\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			json.append(",").append(SecurityAttributesTest.getBasicJSON());
		}
		json.append("}");
		return (json.toString());
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
			xml.append("ism:classification=\"U\" ism:ownerProducer=\"USA\" ");
		xml.append("/>");
		return (xml.toString());
	}

	@Test
	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_DDMS_PREFIX,
				Source.getName(version));
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
	public void testConstructorsMinimal() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, No optional fields
			Element element = Util.buildDDMSElement(Source.getName(version), null);
			Source elementComponent = getInstance(element, SUCCESS);

			// Data-based, No optional fields
			Source.Builder builder = new Source.Builder(elementComponent);
			getInstance(builder, SUCCESS);
		}
	}
	
	@Test
	public void testValidationErrors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Href not a URI
			Source.Builder builder = getBaseBuilder();
			builder.setSchemaHref(INVALID_URI);
			getInstance(builder, "Invalid URI");	
		}
	}
	
	@Test
	public void testValidationWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			// No warnings
			Source component = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());

			// Completely empty
			Element element = Util.buildDDMSElement(Source.getName(version), null);
			component = getInstance(element, SUCCESS);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:source element was found.";
			String locator = "ddms:source";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	@Test
	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			Source elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			Source builderComponent = new Source.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());
			
			// Different values in each field	
			Source.Builder builder = getBaseBuilder();
			builder.setQualifier(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));

			builder = getBaseBuilder();
			builder.setValue(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setSchemaQualifier(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setSchemaHref(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));
		}
	}

	@Test
	public void testVersionSpecific() throws InvalidDDMSException {
		// No security attributes in DDMS 2.0
		Source.Builder builder = getBaseBuilder();
		DDMSVersion.setCurrentVersion("2.0");
		getInstance(builder, "Security attributes must not be applied");
	}
	
	@Test
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Source elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
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

			Source.Builder builder = new Source.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());
		}
	}
}