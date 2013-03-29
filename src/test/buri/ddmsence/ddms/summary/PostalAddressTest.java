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
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private PostalAddress getInstance(String message, Element element) {
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
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param streets the street address lines (0-6)
	 * @param city the city (optional)
	 * @param stateOrProvince the state or province (optional)
	 * @param postalCode the postal code (optional)
	 * @param countryCode the country code (optional)
	 * @param hasState true if the stateOrProvince is a state, false if it is a province (only 1 of state or province
	 *        can exist in a postalAddress)
	 * @return a valid object
	 */
	private PostalAddress getInstance(String message, List<String> streets, String city, String stateOrProvince,
		String postalCode, CountryCode countryCode, boolean hasState) {
		boolean expectFailure = !Util.isEmpty(message);
		PostalAddress component = null;
		try {
			component = new PostalAddress(streets, city, stateOrProvince, postalCode, countryCode, hasState);
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
	 * @param preserveFormatting if true, include line breaks and tabs.
	 * @boolean whether this address has a state or a province
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting, boolean hasState) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:postalAddress ").append(getXmlnsDDMS()).append(">\n\t");
		xml.append("<ddms:street>1600 Pennsylvania Avenue, NW</ddms:street>\n\t");
		xml.append("<ddms:city>Washington</ddms:city>\n\t");
		if (hasState)
			xml.append("<ddms:state>DC</ddms:state>\n\t");
		else
			xml.append("<ddms:province>Alberta</ddms:province>\n\t");
		xml.append("<ddms:postalCode>20500</ddms:postalCode>\n\t");
		if (DDMSVersion.getCurrentVersion().isAtLeast("5.0")) {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getQualifierName()).append(
				"=\"http://api.nsgreg.nga.mil/geo-political/GENC/2/ed1\" ddms:").append(CountryCodeTest.getValueName()).append(
				"=\"US\" />\n");
		}
		else {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getQualifierName()).append(
				"=\"ISO-3166\" ddms:").append(CountryCodeTest.getValueName()).append("=\"USA\" />\n");
		}
		xml.append("</ddms:postalAddress>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				PostalAddress.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, CountryCodeTest.getFixture(),
				true);

			// All fields with a province
			getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);

			// No optional fields
			getInstance(SUCCESS, null, null, null, null, null, false);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String postalName = PostalAddress.getName(version);

			// Either a state or a province but not both.
			Element element = Util.buildDDMSElement(postalName, null);
			element.appendChild(Util.buildDDMSElement("state", TEST_STATE));
			element.appendChild(Util.buildDDMSElement("province", TEST_PROVINCE));
			getInstance("Only 1 of state or province can be used.", element);

			// Too many streets
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 7; i++)
				element.appendChild(Util.buildDDMSElement("street", "street" + i));
			getInstance("No more than 6 street elements can exist.", element);

			// Too many cities
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("city", "city" + i));
			getInstance("No more than 1 city element can exist.", element);

			// Too many states
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("state", "state" + i));
			getInstance("No more than 1 state element can exist.", element);

			// Too many provinces
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("province", "province" + i));
			getInstance("No more than 1 province element can exist.", element);

			// Too many postalCodes
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("postalCode", "postalCode" + i));
			getInstance("No more than 1 postalCode element can exist.", element);

			// Too many country codes
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(new CountryCode("ISO-123" + i, "US" + i).getXOMElementCopy());
			getInstance("No more than 1 countryCode element can exist.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Too many streets
			List<String> streets = new ArrayList<String>();
			for (int i = 0; i < 7; i++)
				streets.add("Street" + i);
			getInstance("No more than 6 street elements can exist.", streets, TEST_CITY, TEST_PROVINCE,
				TEST_POSTAL_CODE, CountryCodeTest.getFixture(), true);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			PostalAddress component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:postalAddress element was found.";
			String locator = "ddms:postalAddress";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PostalAddress elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			PostalAddress dataComponent = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			PostalAddress dataComponent = getInstance(SUCCESS, null, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_STREETS, null, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, null, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, null, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, null,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, null, true);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(false, true), component.toText());

			component = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(false, true), component.toText());

			component = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertEquals(getExpectedOutput(true, false), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true, true), component.toXML());

			component = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(getExpectedXMLOutput(false, true), component.toXML());

			component = getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertEquals(getExpectedXMLOutput(false, false), component.toXML());
		}
	}

	public void testCountryCodeReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode code = CountryCodeTest.getFixture();
			getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, code, true);
			getInstance(SUCCESS, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, code, true);
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress component = getInstance(SUCCESS, getValidElement(sVersion));
			PostalAddress.Builder builder = new PostalAddress.Builder(component);
			assertEquals(component, builder.commit());

			// No country code
			builder = new PostalAddress.Builder(component);
			builder.setCountryCode(new CountryCode.Builder());
			PostalAddress address = builder.commit();
			assertNull(address.getCountryCode());

			// Country code
			CountryCode countryCode = CountryCodeTest.getFixture();
			builder = new PostalAddress.Builder();
			builder.getCountryCode().setQualifier(countryCode.getQualifier());
			builder.getCountryCode().setValue(countryCode.getValue());
			builder.getStreets().add("1600 Pennsylvania Avenue, NW");
			address = builder.commit();
			assertEquals(countryCode, address.getCountryCode());
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

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			PostalAddress.Builder builder = new PostalAddress.Builder();
			builder.setState(TEST_STATE);
			builder.setProvince(TEST_PROVINCE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "Only 1 of state or province can be used.");
			}
			builder.setProvince("");
			builder.commit();
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
