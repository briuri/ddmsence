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
	 * @param builder the builder to commit
	 * @return a valid object
	 */
	private Extent getInstance(String message, Extent.Builder builder) {
		boolean expectFailure = !Util.isEmpty(message);
		Extent component = null;
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
	 *  This builder can then be modified to test various conditions.
	 */
	private Extent.Builder getBaseBuilder() {
		Extent component = getInstance(SUCCESS, getValidElement(DDMSVersion.getCurrentVersion().getVersion()));
		return (new Extent.Builder(component));
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
		xml.append("<ddms:extent ").append(getXmlnsDDMS()).append(" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ");
		xml.append("ddms:value=\"").append(TEST_VALUE).append("\" />");
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

	public void testConstructors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// Element-based
			getInstance(SUCCESS, getValidElement(sVersion));
			
			// Data-based via Builder
			getBaseBuilder();
		}
	}
	
	public void testConstructorsMinimal() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
		
			// Element-based, no optional fields
			Element element = Util.buildDDMSElement(Extent.getName(version), null);
			Extent elementComponent = getInstance(SUCCESS, element);
			
			// Data-based via Builder, no optional fields
			getInstance(SUCCESS, new Extent.Builder(elementComponent));			
		}
	}
	
	public void testValidationErrors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
		
			// Missing qualifier
			Extent.Builder builder = getBaseBuilder();
			builder.setQualifier(null);
			getInstance("qualifier attribute is required.", builder);

			// Qualifier not URI
			builder = getBaseBuilder();
			builder.setQualifier(INVALID_URI);
			getInstance("Invalid URI", builder);			
		}
	}

	public void testValidationWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			final String locator = "ddms:extent";
			
			// No warnings
			Extent component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Extent.Builder builder = getBaseBuilder();
			builder.setValue(null);
			component = getInstance(SUCCESS, builder);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A qualifier has been set without an accompanying value attribute.";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Completely empty
			Element element = Util.buildDDMSElement(Extent.getName(version), null);
			Extent elementComponent = getInstance(SUCCESS, element);
			assertEquals(1, elementComponent.getValidationWarnings().size());
			text = "A completely empty ddms:extent element was found.";
			assertWarningEquality(text, locator, elementComponent.getValidationWarnings().get(0));
		}
	}

	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			Extent elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Extent builderComponent = new Extent.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Different values in each field
			Extent.Builder builder = getBaseBuilder();
			builder.setQualifier(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setValue(DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(builder.commit()));
		}
	}

	public void testVersionSpecific() {
		// No tests.
	}
	
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Extent elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), elementComponent.toHTML());
			assertEquals(getExpectedOutput(false), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(), elementComponent.toXML());
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
}
