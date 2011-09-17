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
 * <p>Tests related to ddms:geographicIdentifier elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:countryCode/ddms:facilityIdentifier tags is done
 * separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class GeographicIdentifierTest extends AbstractComponentTestCase {

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
	 * Returns a geo id fixture
	 */
	protected static GeographicIdentifier getCountryCodeBasedFixture() throws InvalidDDMSException {
		return new GeographicIdentifier(null, null, new CountryCode(
			"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1", "LAO"), null);
	}

	/**
	 * Returns a geo id fixture
	 */
	protected static GeographicIdentifier getFacIdBasedFixture() throws InvalidDDMSException {
		return (new GeographicIdentifier(FacilityIdentifierTest.getFixture()));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private GeographicIdentifier testConstructor(boolean expectFailure, Element element) {
		GeographicIdentifier component = null;
		try {
			component = new GeographicIdentifier(element);
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
	 * @param names the names (optional)
	 * @param regions the region names (optional)
	 * @param countryCode the country code (optional)
	 * @param subDivisionCode the subdivision code (optional, starting in DDMS 4.0)
	 * @param facilityIdentifier the facility identifier (optional)
	 * @return a valid object
	 */
	private GeographicIdentifier testConstructor(boolean expectFailure, List<String> names, List<String> regions,
		CountryCode countryCode, SubDivisionCode subDivisionCode, FacilityIdentifier facilityIdentifier) {
		GeographicIdentifier component = null;
		try {
			if (facilityIdentifier != null)
				component = new GeographicIdentifier(facilityIdentifier);
			else
				component = new GeographicIdentifier(names, regions, countryCode, subDivisionCode);
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "geographicIdentifier.name", TEST_NAMES.get(0)));
		text.append(buildOutput(isHTML, "geographicIdentifier.region", TEST_REGIONS.get(0)));
		text.append(CountryCodeTest.getFixture().getOutput(isHTML, "geographicIdentifier."));
		if (version.isAtLeast("4.0"))
			text.append(SubDivisionCodeTest.getFixture().getOutput(isHTML, "geographicIdentifier."));
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
		xml.append("<ddms:countryCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		if (version.isAtLeast("4.0"))
			xml.append("\t<ddms:subDivisionCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		xml.append("</ddms:geographicIdentifier>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(GeographicIdentifier.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + GeographicIdentifier.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String geoIdName = GeographicIdentifier.getName(version);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_SUCCEED, element);

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(Util.buildDDMSElement("region", TEST_REGIONS.get(0)));
			testConstructor(WILL_SUCCEED, element);

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);

			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement(geoIdName, null);
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_SUCCEED, element);
			}

			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode, null);

			// No optional fields		
			testConstructor(WILL_SUCCEED, TEST_NAMES, null, null, null, null);
			testConstructor(WILL_SUCCEED, null, TEST_REGIONS, null, null, null);
			testConstructor(WILL_SUCCEED, null, null, CountryCodeTest.getFixture(), null, null);
			if (version.isAtLeast("4.0"))
				testConstructor(WILL_SUCCEED, null, null, null, subCode, null);
			testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String geoIdName = GeographicIdentifier.getName(version);

			// At least 1 name, region, countryCode, or facilityIdentifier must exist.
			Element element = Util.buildDDMSElement(geoIdName, null);
			testConstructor(WILL_FAIL, element);

			// No more than 1 countryCode
			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 subDivisionCode
			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement(geoIdName, null);
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_FAIL, element);
			}

			// No more than 1 facilityIdentifier
			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// facilityIdentifier must be alone
			element = Util.buildDDMSElement(geoIdName, null);
			element.appendChild(CountryCodeTest.getFixture().getXOMElementCopy());
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// At least 1 name, region, countryCode, subDivisionCode or facilityIdentifier must exist.
			testConstructor(WILL_FAIL, null, null, null, null, null);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS,
				CountryCodeTest.getFixture(), subCode, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			Element element = Util.buildDDMSElement(GeographicIdentifier.getName(version), null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, null, TEST_REGIONS,
				CountryCodeTest.getFixture(), subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, CountryCodeTest.getFixture(), subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.0")) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(),
					null, null);
				assertFalse(elementComponent.equals(dataComponent));
			}

			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode,
				null);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testHTMLFacIdOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier component = getFacIdBasedFixture();
			StringBuffer facIdOutput = new StringBuffer();
			facIdOutput
				.append("<meta name=\"geographicIdentifier.facilityIdentifier.beNumber\" content=\"1234DD56789\" />\n");
			facIdOutput.append("<meta name=\"geographicIdentifier.facilityIdentifier.osuffix\" content=\"DD123\" />\n");
			assertEquals(facIdOutput.toString(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode,
				null);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testTextFacIdOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier component = getFacIdBasedFixture();
			StringBuffer facIdOutput = new StringBuffer();
			facIdOutput.append("geographicIdentifier.facilityIdentifier.beNumber: 1234DD56789\n");
			facIdOutput.append("geographicIdentifier.facilityIdentifier.osuffix: DD123\n");
			assertEquals(facIdOutput.toString(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubDivisionCode subCode = SubDivisionCodeTest.getFixture();

			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest.getFixture(), subCode,
				null);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCountryCodeReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			CountryCode code = CountryCodeTest.getFixture();
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, code, null, null);
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, code, null, null);
		}
	}

	public void testSubDivisionCodeReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode code = SubDivisionCodeTest.getFixture();
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, code, null);
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, code, null);
		}
	}

	public void testFacilityIdentifierReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, null, facId);
			testConstructor(WILL_SUCCEED, null, null, null, null, facId);
		}
	}

	public void testWrongVersionCountryCode() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		CountryCode code = CountryCodeTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeographicIdentifier(TEST_NAMES, TEST_REGIONS, code, null);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testWrongVersionSubDivisionCode() throws InvalidDDMSException {
		// Can't test this until SubDivisionCode can be used in more than 1 valid DDMS version.
	}

	public void testWrongVersionFacilityIdentifier() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeographicIdentifier(facId);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building (CountryCode-based)
			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder(component);
			assertEquals(builder.commit(), component);

			// Equality after Building (FacID-based)
			FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
			component = new GeographicIdentifier(facId);
			builder = new GeographicIdentifier.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new GeographicIdentifier.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new GeographicIdentifier.Builder();
			builder.getFacilityIdentifier().setBeNumber("1234DD56789");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
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
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getRegions().get(1));
		}
	}
}
