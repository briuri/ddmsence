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
package buri.ddmsence.ddms.summary;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:postalAddress elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PostalAddressTest extends AbstractBaseTestCase {

	private static final List<String> TEST_STREETS = new ArrayList<String>();
	static {
		TEST_STREETS.add("1600 Pennsylvania Avenue, NW");
	}
	private static final String TEST_CITY = "Washington";
	private static final String TEST_STATE = "DC";
	private static final String TEST_PROVINCE = "Alberta";
	private static final String TEST_POSTAL_CODE = "20500";

	/**
	 * Constructor
	 */
	public PostalAddressTest() throws InvalidDDMSException {
		super("postalAddress.xml");
		// TODO: Restore 5.0 after postalAddress is ready.
		removeSupportedVersions("5.0");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static PostalAddress getFixture() {
		try {
			return (new PostalAddress(null, null, "VA", null, null, true));
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
	private PostalAddress getInstance(Element element, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		PostalAddress component = null;
		try {
			component = new PostalAddress(element);
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
	private PostalAddress getInstance(PostalAddress.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		PostalAddress component = null;
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
	private PostalAddress.Builder getBaseBuilder() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		PostalAddress component = getInstance(getValidElement(version.getVersion()), SUCCESS);
		return (new PostalAddress.Builder(component));
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML, boolean hasState) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "postalAddress.street", TEST_STREETS.get(0)));
		text.append(buildOutput(isHTML, "postalAddress.city", TEST_CITY));
		if (hasState)
			text.append(buildOutput(isHTML, "postalAddress.state", TEST_STATE));
		else
			text.append(buildOutput(isHTML, "postalAddress.province", TEST_PROVINCE));
		text.append(buildOutput(isHTML, "postalAddress.postalCode", TEST_POSTAL_CODE));
		text.append(CountryCodeTest.getFixture().getOutput(isHTML, "postalAddress.", ""));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @boolean whether this address has a state or a province
	 */
	private String getExpectedXMLOutput(boolean hasState) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:postalAddress ").append(getXmlnsDDMS()).append(">");
		xml.append("<ddms:street>1600 Pennsylvania Avenue, NW</ddms:street>");
		xml.append("<ddms:city>Washington</ddms:city>");
		if (hasState)
			xml.append("<ddms:state>DC</ddms:state>");
		else
			xml.append("<ddms:province>Alberta</ddms:province>");
		xml.append("<ddms:postalCode>20500</ddms:postalCode>");
		if (DDMSVersion.getCurrentVersion().isAtLeast("5.0")) {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getTestQualifierName());
			xml.append("=\"http://api.nsgreg.nga.mil/geo-political/GENC/2/ed1\" ");
			xml.append("ddms:").append(CountryCodeTest.getTestValueName()).append("=\"US\" />");
		}
		else {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getTestQualifierName());
			xml.append("=\"ISO-3166\" ddms:").append(CountryCodeTest.getTestValueName()).append("=\"USA\" />");
		}
		xml.append("</ddms:postalAddress>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_DDMS_PREFIX,
				PostalAddress.getName(version));
			getInstance(getWrongNameElementFixture(), WRONG_NAME_MESSAGE);
		}
	}

	public void testConstructors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, state
			getInstance(getValidElement(sVersion), SUCCESS);

			// Data-based via Builder, state
			getBaseBuilder();
			
			// Data-based via Builder, province
			PostalAddress.Builder builder = getBaseBuilder();
			builder.setState(null);
			builder.setProvince(TEST_PROVINCE);
			getInstance(builder, SUCCESS);
		}
	}
	
	public void testConstructorsMinimal() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, No optional fields
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			PostalAddress elementComponent = getInstance(element, SUCCESS);

			// Data-based, No optional fields
			getInstance(new PostalAddress.Builder(elementComponent), SUCCESS);
		}
	}
	
	public void testValidationErrors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String postalName = PostalAddress.getName(version);

			// Element-based, Either a state or a province but not both.
			Element element = Util.buildDDMSElement(postalName, null);
			element.appendChild(Util.buildDDMSElement("state", TEST_STATE));
			element.appendChild(Util.buildDDMSElement("province", TEST_PROVINCE));
			getInstance(element, "Only 1 of state or province must be used.");

			// Data-based, Either a state or a province but not both.
			PostalAddress.Builder builder = getBaseBuilder();
			builder.setProvince(TEST_PROVINCE);
			getInstance(builder, "Only 1 of state or province");
			
			// Too many streets
			 builder = getBaseBuilder();
			for (int i = 0; i < 7; i++)
				builder.getStreets().add("Street " + i);
			getInstance(builder, "No more than 6 street elements must exist.");
		}
	}

	public void testValidationWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			// No warnings
			PostalAddress component = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());

			// Completely empty
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			component = getInstance(element, SUCCESS);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:postalAddress element was found.";
			String locator = "ddms:postalAddress";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			PostalAddress elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			PostalAddress builderComponent = new PostalAddress.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Wrong class
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
			
			// Different values in each field
			PostalAddress.Builder builder = getBaseBuilder();
			builder.setStreets(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setCity(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setState(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setPostalCode(null);
			assertFalse(elementComponent.equals(builder.commit()));

			builder = getBaseBuilder();
			builder.setCountryCode(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setState(null);
			builder.setProvince(TEST_PROVINCE);
			assertFalse(elementComponent.equals(builder.commit()));
		}
	}

	public void testVersionSpecific() throws InvalidDDMSException {
		// No tests yet.
	}
	
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(getExpectedOutput(true, true), elementComponent.toHTML());
			assertEquals(getExpectedOutput(false, true), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(true), elementComponent.toXML());
			
			PostalAddress.Builder builder = getBaseBuilder();
			builder.setState(null);
			builder.setProvince(TEST_PROVINCE);
			elementComponent = builder.commit();
			assertEquals(getExpectedOutput(true, false), elementComponent.toHTML());
			assertEquals(getExpectedOutput(false, false), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(false), elementComponent.toXML());
		}
	}
	
	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress.Builder builder = new PostalAddress.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			
			builder.setCity(TEST_CITY);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PostalAddress.Builder builder = new PostalAddress.Builder();
			assertNotNull(builder.getStreets().get(1));
		}
	}
}
