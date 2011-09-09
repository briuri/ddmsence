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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:type elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TypeTest extends AbstractComponentTestCase {

	private static final String TEST_DESCRIPTION = "Description";
	private static final String TEST_QUALIFIER = "DCMITYPE";
	private static final String TEST_VALUE = "http://purl.org/dc/dcmitype/Text";

	/**
	 * Constructor
	 */
	public TypeTest() {
		super("type.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Type testConstructor(boolean expectFailure, Element element) {
		Type component = null;
		try {
			component = new Type(element);
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
	 * @param description the description child text
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private Type testConstructor(boolean expectFailure, String description, String qualifier, String value) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Type component = null;
		try {
			component = new Type(description, qualifier, value,
				version.isAtLeast("4.0") ? SecurityAttributesTest.getFixture(false) : null);
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer html = new StringBuffer();
		if (version.isAtLeast("4.0"))
			html.append("<meta name=\"type.description\" content=\"").append(TEST_DESCRIPTION).append("\" />\n");
		html.append("<meta name=\"type.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"type.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		if (version.isAtLeast("4.0")) {
			html.append("<meta name=\"type.classification\" content=\"U\" />\n");
			html.append("<meta name=\"type.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("4.0"))
			text.append("type description: ").append(TEST_DESCRIPTION).append("\n");
		text.append("type qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("type value: ").append(TEST_VALUE).append("\n");
		if (version.isAtLeast("4.0")) {
			text.append("type classification: U\n");
			text.append("type ownerProducer: USA\n");
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:type ").append(getXmlnsDDMS()).append(" ");
		if (version.isAtLeast("4.0")) {
			xml.append(getXmlnsISM()).append(" ");
		}
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE)
			.append("\"");
		if (version.isAtLeast("4.0")) {
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Description</ddms:type>");
		} else
			xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Type.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Type.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "", TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Missing qualifier
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing qualifier
			testConstructor(WILL_FAIL, null, null, TEST_VALUE);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A qualifier has been set without an accompanying value attribute.";
			String locator = "ddms:type";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Neither attribute
			element = Util.buildDDMSElement(Type.getName(version), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			text = "Neither a qualifier nor a value was set on this type.";
			locator = "ddms:type";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Type dataComponent = testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "",
				TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// Backwards compatible constructors
			assertEquals(new Type(TEST_QUALIFIER, TEST_VALUE), new Type(null, TEST_QUALIFIER, TEST_VALUE, null));
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Type dataComponent = testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "",
				TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
			if (version.isAtLeast("4.0")) {
				dataComponent = testConstructor(WILL_SUCCEED, "differentDescription", TEST_QUALIFIER, TEST_VALUE);
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "", TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "", TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, version.isAtLeast("4.0") ? TEST_DESCRIPTION : "", TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testSecurityAttributesWrongVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Type(null, TEST_QUALIFIER, TEST_VALUE, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testDescriptionAttributesWrongVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Type(TEST_DESCRIPTION, TEST_QUALIFIER, TEST_VALUE, null);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Type component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Type.Builder builder = new Type.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Type.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Type.Builder();
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