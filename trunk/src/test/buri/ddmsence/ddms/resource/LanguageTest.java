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
 * <p>Tests related to ddms:language elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class LanguageTest extends AbstractComponentTestCase {

	private static final String TEST_QUALIFIER = "http://purl.org/dc/elements/1.1/language";
	private static final String TEST_VALUE = "en";

	/**
	 * Constructor
	 */
	public LanguageTest() {
		super("language.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Language testConstructor(boolean expectFailure, Element element) {
		Language component = null;
		try {
			component = new Language(element);
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
	 * @return a valid object
	 */
	private Language testConstructor(boolean expectFailure, String qualifier, String value) {
		Language component = null;
		try {
			component = new Language(qualifier, value);
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
		text.append(buildOutput(isHTML, "language.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "language.value", TEST_VALUE));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:language ").append(getXmlnsDDMS()).append(" ddms:qualifier=\"").append(TEST_QUALIFIER)
			.append("\" ddms:value=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Language.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Language.getName(version),
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
			Element element = Util.buildDDMSElement(Language.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Missing qualifier
			Element element = Util.buildDDMSElement(Language.getName(version), null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing qualifier
			testConstructor(WILL_FAIL, null, TEST_VALUE);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Element element = Util.buildDDMSElement(Language.getName(version), null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A qualifier has been set without an accompanying value attribute.";
			String locator = "ddms:language";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Neither attribute
			element = Util.buildDDMSElement(Language.getName(version), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			text = "Neither a qualifier nor a value was set on this language.";
			locator = "ddms:language";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Language dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Language dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Language component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Language.Builder builder = new Language.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Language.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Language.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.setQualifier(TEST_QUALIFIER);
			builder.commit();
		}
	}
}
