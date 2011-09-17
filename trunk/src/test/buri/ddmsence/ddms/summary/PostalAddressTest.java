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
package buri.ddmsence.ddms.summary;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:postalAddress elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:countryCode tag is done separately.</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PostalAddressTest extends AbstractComponentTestCase {

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
	 * Returns a postal address fixture
	 */
	protected static PostalAddress getFixture() throws InvalidDDMSException {
		return (new PostalAddress(null, null, "VA", null, null, true));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private PostalAddress testConstructor(boolean expectFailure, Element element) {
		PostalAddress component = null;
		try {
			component = new PostalAddress(element);
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
	 * @param streets the street address lines (0-6)
	 * @param city the city (optional)
	 * @param stateOrProvince the state or province (optional)
	 * @param postalCode the postal code (optional)
	 * @param countryCode the country code (optional)
	 * @param hasState true if the stateOrProvince is a state, false if it is a province (only 1 of state or province
	 * can exist in a postalAddress)
	 * @return a valid object
	 */
	private PostalAddress testConstructor(boolean expectFailure, List<String> streets, String city,
		String stateOrProvince, String postalCode, CountryCode countryCode, boolean hasState) {
		PostalAddress component = null;
		try {
			component = new PostalAddress(streets, city, stateOrProvince, postalCode, countryCode, hasState);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
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
		text.append(buildOutput(isHTML, "postalAddress.countryCode.qualifier", "ISO-3166"));
		text.append(buildOutput(isHTML, "postalAddress.countryCode.value", "USA"));
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
		xml.append("<ddms:countryCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		xml.append("</ddms:postalAddress>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(PostalAddress.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + PostalAddress.getName(version),
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
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);

			// All fields with a province
			testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, null, null, false);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String postalName = PostalAddress.getName(version);
			// Either a state or a province but not both.
			Element element = Util.buildDDMSElement(postalName, null);
			element.appendChild(Util.buildDDMSElement("state", TEST_STATE));
			element.appendChild(Util.buildDDMSElement("province", TEST_PROVINCE));
			testConstructor(WILL_FAIL, element);

			// Too many streets
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 7; i++)
				element.appendChild(Util.buildDDMSElement("street", "street" + i));
			testConstructor(WILL_FAIL, element);

			// Too many cities
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("city", "city" + i));
			testConstructor(WILL_FAIL, element);

			// Too many states
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("state", "state" + i));
			testConstructor(WILL_FAIL, element);

			// Too many provinces
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("province", "province" + i));
			testConstructor(WILL_FAIL, element);

			// Too many postalCodes
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(Util.buildDDMSElement("postalCode", "postalCode" + i));
			testConstructor(WILL_FAIL, element);

			// Too many country codes
			element = Util.buildDDMSElement(postalName, null);
			for (int i = 0; i < 2; i++)
				element.appendChild(new CountryCode("ISO-123" + i, "US" + i).getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Too many streets
			List<String> streets = new ArrayList<String>();
			for (int i = 0; i < 7; i++)
				streets.add("Street" + i);
			testConstructor(WILL_FAIL, streets, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:postalAddress element was found.";
			String locator = "ddms:postalAddress";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			PostalAddress elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			PostalAddress dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE,
				TEST_POSTAL_CODE, CountryCodeTest.getFixture(), true);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			PostalAddress elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			PostalAddress dataComponent = testConstructor(WILL_SUCCEED, null, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, null, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, null, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, null, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, null,
				CountryCodeTest.getFixture(), true);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, null,
				true);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			PostalAddress elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true, true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(getExpectedOutput(true, true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertEquals(getExpectedOutput(true, false), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false, true), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(getExpectedOutput(false, true), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertEquals(getExpectedOutput(false, false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true, true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), true);
			assertEquals(getExpectedXMLOutput(false, true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_PROVINCE, TEST_POSTAL_CODE,
				CountryCodeTest.getFixture(), false);
			assertEquals(getExpectedXMLOutput(false, false), component.toXML());
		}
	}

	public void testCountryCodeReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			CountryCode code = CountryCodeTest.getFixture();
			testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, code, true);
			testConstructor(WILL_SUCCEED, TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, code, true);
		}
	}

	public void testWrongVersionCountryCode() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		CountryCode code = CountryCodeTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new PostalAddress(TEST_STREETS, TEST_CITY, TEST_STATE, TEST_POSTAL_CODE, code, true);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			PostalAddress component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			PostalAddress.Builder builder = new PostalAddress.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new PostalAddress.Builder();
			assertNull(builder.commit());

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

			// Validation
			builder = new PostalAddress.Builder();
			builder.setState(TEST_STATE);
			builder.setProvince(TEST_PROVINCE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			PostalAddress.Builder builder = new PostalAddress.Builder();
			assertNotNull(builder.getStreets().get(1));
		}
	}
}
