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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:source elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SourceTest extends AbstractComponentTestCase {

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
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Source testConstructor(boolean expectFailure, Element element) {
		Source component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture(false).addTo(element);
			component = new Source(element);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @param schemaQualifier the value of the schemaQualifier attribute
	 * @param schemaHref the value of the schemaHref attribute
	 * @return a valid object
	 */
	private Source testConstructor(boolean expectFailure, String qualifier, String value, String schemaQualifier,
		String schemaHref) {
		Source component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0") ? null
				: SecurityAttributesTest.getFixture(false));
			component = new Source(qualifier, value, schemaQualifier, schemaHref, attr);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"source.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"source.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"source.schemaQualifier\" content=\"").append(TEST_SCHEMA_QUALIFIER)
			.append("\" />\n");
		html.append("<meta name=\"source.schemaHref\" content=\"").append(TEST_SCHEMA_HREF).append("\" />\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			html.append("<meta name=\"source.classification\" content=\"U\" />\n");
			html.append("<meta name=\"source.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("source qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("source value: ").append(TEST_VALUE).append("\n");
		text.append("source schemaQualifier: ").append(TEST_SCHEMA_QUALIFIER).append("\n");
		text.append("source schemaHref: ").append(TEST_SCHEMA_HREF).append("\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append("source classification: U\n");
			text.append("source ownerProducer: USA\n");
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:source xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append("xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIsmNamespace()).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE)
			.append("\" ");
		xml.append("ddms:schemaQualifier=\"").append(TEST_SCHEMA_QUALIFIER).append("\" ");
		xml.append("ddms:schemaHref=\"").append(TEST_SCHEMA_HREF).append("\" ");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append("ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\" ");
		xml.append("/>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Source.getName(DDMSVersion.getCurrentVersion()), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Source.getName(DDMSVersion.getCurrentVersion()), component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(Source.getName(DDMSVersion.getCurrentVersion()), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Href not URI
			Element element = Util.buildDDMSElement(Source.getName(DDMSVersion.getCurrentVersion()), null);
			Util.addDDMSAttribute(element, "schemaHref", INVALID_URI);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalidHrefNotURI() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Href not URI
			testConstructor(WILL_FAIL, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, INVALID_URI);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			Element element = Util.buildDDMSElement(Source.getName(DDMSVersion.getCurrentVersion()), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A completely empty ddms:source element was found.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:source", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Source dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Source dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE,
				TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, DIFFERENT_VALUE, TEST_SCHEMA_HREF);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER,
				TEST_SCHEMA_HREF);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			Source component = new Source(TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF, attr);
			if (!DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Source(TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF, SecurityAttributesTest
				.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Source component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			Source.Builder builder = new Source.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new Source.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new Source.Builder();
			builder.getSecurityAttributes().setClassification("SuperSecret");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}