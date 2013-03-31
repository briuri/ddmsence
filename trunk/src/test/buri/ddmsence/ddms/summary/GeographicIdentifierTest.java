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
 * <p> Tests related to ddms:geographicIdentifier elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class GeographicIdentifierTest extends AbstractBaseTestCase {

	private static final List<String> TEST_NAMES = new ArrayList<String>();
	static {
		TEST_NAMES.add("The White House");
	}

	private static final List<String> TEST_REGIONS = new ArrayList<String>();
	static {
		TEST_REGIONS.add("Mid-Atlantic States");
	}

	/**
	 * Constructor
	 */
	public GeographicIdentifierTest() throws InvalidDDMSException {
		super("geographicIdentifier.xml");
	}

	/**
	 * Returns a fixture object for testing. This object will be based on a country code.
	 */
	public static GeographicIdentifier getCountryCodeBasedFixture() throws InvalidDDMSException {
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("5.0")) {
				return new GeographicIdentifier(null, null, new CountryCode(
					"urn:us:gov:dod:nga:def:geo-political:GENC:3:ed1", "USA"), null);
			}
			return new GeographicIdentifier(null, null, new CountryCode(
				"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1", "LAO"), null);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing. This object will be based on a facility ID
	 */
	public static GeographicIdentifier getFacIdBasedFixture() throws InvalidDDMSException {
		try {
			return (new GeographicIdentifier(FacilityIdentifierTest.getFixture()));
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
	private GeographicIdentifier getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		GeographicIdentifier component = null;
		try {
			component = new GeographicIdentifier(element);
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
	 * @param names the names (optional)
	 * @param regions the region names (optional)
	 * @param countryCode the country code (optional)
	 * @param subDivisionCode the subdivision code (optional, starting in DDMS 4.0.1)
	 * @param facilityIdentifier the facility identifier (optional)
	 * @return a valid object
	 */
	private GeographicIdentifier getInstance(String message, List<String> names, List<String> regions,
		CountryCode countryCode, SubDivisionCode subDivisionCode, FacilityIdentifier facilityIdentifier) {
		boolean expectFailure = !Util.isEmpty(message);
		GeographicIdentifier component = null;
		try {
			if (facilityIdentifier != null)
				component = new GeographicIdentifier(facilityIdentifier);
			else
				component = new GeographicIdentifier(names, regions, countryCode, subDivisionCode);
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
		text.append(buildOutput(isHTML, "geographicIdentifier.name", TEST_NAMES.get(0)));
		text.append(buildOutput(isHTML, "geographicIdentifier.region", TEST_REGIONS.get(0)));
		text.append(CountryCodeTest.getFixture().getOutput(isHTML, "geographicIdentifier.", ""));
		if (version.isAtLeast("4.0.1"))
			text.append(SubDivisionCodeTest.getFixture().getOutput(isHTML, "geographicIdentifier.", ""));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geographicIdentifier ").append(getXmlnsDDMS()).append(">\n\t");
		xml.append("<ddms:name>The White House</ddms:name>\n\t");
		xml.append("<ddms:region>Mid-Atlantic States</ddms:region>\n\t");
		if (version.isAtLeast("5.0")) {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getQualifierName()).append(
				"=\"http://api.nsgreg.nga.mil/geo-political/GENC/2/ed1\" ddms:").append(CountryCodeTest.getValueName()).append(
				"=\"US\" />\n");
		}
		else {
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getQualifierName()).append(
				"=\"ISO-3166\" ddms:").append(CountryCodeTest.getValueName()).append("=\"USA\" />\n");
		}
		if (version.isAtLeast("4.0.1"))
			xml.append("\t<ddms:subDivisionCode ddms:").append(CountryCodeTest.getQualifierName()).append(
				"=\"ISO-3166\" ddms:").append(CountryCodeTest.getValueName()).append("=\"USA\" />\n");
		xml.append("</ddms:geographicIdentifier>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				GeographicIdentifier.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String geoIdName = GeographicIdentifier.getName(version);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			getInstance(SUCCESS, element);

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(Util.buildDDMSElement("region", TEST_REGIONS.get(0)));
			getInstance(SUCCESS, element);

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);

			if (version.isAtLeast("4.0.1")) {
				element = Util.buildDDMSElement(geoIdName, null);
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				getInstance(SUCCESS, element);
			}

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			// All fields
			getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode, null);

			// No optional fields
			getInstance(SUCCESS, TEST_NAMES, null, null, null, null);
			getInstance(SUCCESS, null, TEST_REGIONS, null, null, null);
			getInstance(SUCCESS, null, null, CountryCodeTest.getFixture(), null, null);
			if (version.isAtLeast("4.0.1"))
				getInstance(SUCCESS, null, null, null, subCode, null);
			getInstance(SUCCESS, null, null, null, null, FacilityIdentifierTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String geoIdName = GeographicIdentifier.getName(version);

			// At least 1 name, region, countryCode, or facilityIdentifier must exist.
			Element element = Util.buildDDMSElement(geoIdName, null);
			getInstance("At least 1 of ", element);

			// facilityIdentifier must be alone
			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			getInstance("facilityIdentifier cannot be used", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// At least 1 name, region, countryCode, subDivisionCode or facilityIdentifier must exist.
			getInstance("At least 1 of ", null, null, null, null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			int expectedWarningCount = (version.isAtLeast("5.0") ? 1 : 0);
			GeographicIdentifier component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(expectedWarningCount, component.getValidationWarnings().size());
			if (version.isAtLeast("5.0")) {
				String text = "The ddms:countryCode is syntactically correct";
				String locator = "ddms:geographicIdentifier";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			GeographicIdentifier dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS,
				CountryCodeTest.getFixture(), subCode, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			Element element = Util.buildDDMSElement(GeographicIdentifier.getName(version), null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			elementComponent = getInstance(SUCCESS, element);
			dataComponent = getInstance(SUCCESS, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			GeographicIdentifier dataComponent = getInstance(SUCCESS, null, TEST_REGIONS, CountryCodeTest.getFixture(),
				subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAMES, null, CountryCodeTest.getFixture(), subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, null, subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), null, null);
				assertFalse(elementComponent.equals(dataComponent));
			}

			dataComponent = getInstance(SUCCESS, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode, null);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testHTMLFacIdOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			GeographicIdentifier component = getFacIdBasedFixture();
			StringBuffer facIdOutput = new StringBuffer();
			facIdOutput.append("<meta name=\"geographicIdentifier.facilityIdentifier.beNumber\" content=\"1234DD56789\" />\n");
			facIdOutput.append("<meta name=\"geographicIdentifier.facilityIdentifier.osuffix\" content=\"DD123\" />\n");
			assertEquals(facIdOutput.toString(), component.toHTML());
		}
	}

	public void testTextFacIdOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			GeographicIdentifier component = getFacIdBasedFixture();
			StringBuffer facIdOutput = new StringBuffer();
			facIdOutput.append("geographicIdentifier.facilityIdentifier.beNumber: 1234DD56789\n");
			facIdOutput.append("geographicIdentifier.facilityIdentifier.osuffix: DD123\n");
			assertEquals(facIdOutput.toString(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode, null);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCountryCodeReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode code = CountryCodeTest.getFixture();
			getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, code, null, null);
			getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, code, null, null);
		}
	}

	public void testSubDivisionCodeReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0.1"))
				continue;

			SubDivisionCode code = SubDivisionCodeTest.getFixture();
			getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, null, code, null);
			getInstance(SUCCESS, TEST_NAMES, TEST_REGIONS, null, code, null);
		}
	}

	public void testFacilityIdentifierReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
			getInstance(SUCCESS, null, null, null, null, facId);
			getInstance(SUCCESS, null, null, null, null, facId);
		}
	}

	public void testGencCountryCodeSuccess() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("5.0");
		GeographicIdentifier.validateGencCountryCode(new CountryCode(
			"http://api.nsgreg.nga.mil/geo-political/GENC/2/ed1", "US"));
		GeographicIdentifier.validateGencCountryCode(new CountryCode("urn:us:gov:dod:nga:def:geo-political:GENC:3:ed1",
			"USA"));
		GeographicIdentifier.validateGencCountryCode(new CountryCode("geo-political:GENC:3:ed1", "USA"));
		GeographicIdentifier.validateGencCountryCode(new CountryCode("ge:GENC:n:ed1", "123"));
	}

	public void testGencCountryCodeFailure() {
		DDMSVersion.setCurrentVersion("5.0");
		try {
			GeographicIdentifier.validateGencCountryCode(new CountryCode("ISO-3166", "US"));
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "ddms:countryCode must use a geo-political");
		}
		try {
			GeographicIdentifier.validateGencCountryCode(new CountryCode(
				"urn:us:gov:dod:nga:def:geo-political:GENC:3:ed1", "US"));
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A GENC country code in a 3-alpha codespace");
		}
		try {
			GeographicIdentifier.validateGencCountryCode(new CountryCode(
				"urn:us:gov:dod:nga:def:geo-political:GENC:2:ed1", "USA"));
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A GENC country code in a 2-alpha codespace");
		}
		try {
			GeographicIdentifier.validateGencCountryCode(new CountryCode(
				"urn:us:gov:dod:nga:def:geo-political:GENC:n:ed1", "USA"));
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A GENC country code in a numeric");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			GeographicIdentifier component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building (CountryCode-based)
			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder(component);
			assertEquals(component, builder.commit());

			// Equality after Building (FacID-based)
			FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
			component = new GeographicIdentifier(facId);
			builder = new GeographicIdentifier.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setNames(TEST_NAMES);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder();
			builder.getFacilityIdentifier().setBeNumber("1234DD56789");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "osuffix is required.");
			}
			builder.getFacilityIdentifier().setOsuffix("osuffix");
			builder.commit();

			// Non-FacID-based
			builder = new GeographicIdentifier.Builder();
			builder.getNames().add("Name");
			builder.getRegions().add("Region");
			CountryCode countryCode = CountryCodeTest.getFixture();
			builder.getCountryCode().setQualifier(countryCode.getQualifier());
			builder.getCountryCode().setValue(countryCode.getValue());
			builder.commit();
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getRegions().get(1));
		}
	}
}
