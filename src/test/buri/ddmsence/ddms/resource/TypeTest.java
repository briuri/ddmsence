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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:type elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TypeTest extends AbstractBaseTestCase {

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
	 * Returns a fixture object for testing.
	 */
	public static Type getFixture() {
		try {
			return (new Type(null, "DCMITYPE", "http://purl.org/dc/dcmitype/Text", null));
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
	private Type getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Type component = null;
		try {
			component = new Type(element);
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
	 * @param description the description child text
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private Type getInstance(String message, String description, String qualifier, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Type component = null;
		try {
			component = new Type(description, qualifier, value,
				version.isAtLeast("4.0.1") ? SecurityAttributesTest.getFixture() : null);
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("4.0.1"))
			text.append(buildOutput(isHTML, "type.description", TEST_DESCRIPTION));
		text.append(buildOutput(isHTML, "type.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "type.value", TEST_VALUE));
		if (version.isAtLeast("4.0.1")) {
			text.append(buildOutput(isHTML, "type.classification", "U"));
			text.append(buildOutput(isHTML, "type.ownerProducer", "USA"));
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
		if (version.isAtLeast("4.0.1")) {
			xml.append(getXmlnsISM()).append(" ");
		}
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append(
			"\"");
		if (version.isAtLeast("4.0.1")) {
			xml.append(" ism:classification=\"U\" ism:ownerProducer=\"USA\">Description</ddms:type>");
		}
		else
			xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Type.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, version.isAtLeast("4.0.1") ? TEST_DESCRIPTION : "", TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			getInstance(SUCCESS, "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing qualifier
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			getInstance("qualifier attribute is required.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing qualifier
			getInstance("qualifier attribute is required.", null, null, TEST_VALUE);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Type component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Element element = Util.buildDDMSElement(Type.getName(version), null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A qualifier has been set without an accompanying value attribute.";
			String locator = "ddms:type";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Neither attribute
			element = Util.buildDDMSElement(Type.getName(version), null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			text = "Neither a qualifier nor a value was set on this type.";
			locator = "ddms:type";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Type elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Type dataComponent = getInstance(SUCCESS, version.isAtLeast("4.0.1") ? TEST_DESCRIPTION : "",
				TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Type elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Type dataComponent = getInstance(SUCCESS, version.isAtLeast("4.0.1") ? TEST_DESCRIPTION : "",
				TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, "differentDescription", TEST_QUALIFIER, TEST_VALUE);
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Type component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, version.isAtLeast("4.0.1") ? TEST_DESCRIPTION : "", TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Type component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, version.isAtLeast("4.0.1") ? TEST_DESCRIPTION : "", TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Type(null, TEST_QUALIFIER, TEST_VALUE, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testWrongVersionDescriptionAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Type(TEST_DESCRIPTION, TEST_QUALIFIER, TEST_VALUE, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot contain description");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Type component = getInstance(SUCCESS, getValidElement(sVersion));
			Type.Builder builder = new Type.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Type.Builder builder = new Type.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Type.Builder builder = new Type.Builder();
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