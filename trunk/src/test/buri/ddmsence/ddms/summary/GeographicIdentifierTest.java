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
import buri.ddmsence.ddms.AbstractComponentTestCase;
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
	protected static GeographicIdentifier getFixture() throws InvalidDDMSException {
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
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() throws InvalidDDMSException {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"geospatialCoverage.GeospatialExtent.geographicIdentifier.name\" content=\"").append(TEST_NAMES.get(0)).append("\" />\n");
		html.append("<meta name=\"geospatialCoverage.GeospatialExtent.geographicIdentifier.region\" content=\"").append(TEST_REGIONS.get(0))
			.append("\" />\n");
		html.append(CountryCodeTest.getFixture(GeographicIdentifier.NAME).toHTML());
		if (isDDMS40OrGreater())
			html.append(SubDivisionCodeTest.getFixture().toHTML());
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append("geographicIdentifier name: ").append(TEST_NAMES.get(0)).append("\n");
		text.append("geographicIdentifier region: ").append(TEST_REGIONS.get(0)).append("\n");
		text.append(CountryCodeTest.getFixture(GeographicIdentifier.NAME).toText());
		if (isDDMS40OrGreater())
			text.append(SubDivisionCodeTest.getFixture().toText());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geographicIdentifier xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\">\n\t");
		xml.append("<ddms:name>The White House</ddms:name>\n\t");
		xml.append("<ddms:region>Mid-Atlantic States</ddms:region>\n\t");
		xml.append("<ddms:countryCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		if (isDDMS40OrGreater())
			xml.append("\t<ddms:subDivisionCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		xml.append("</ddms:geographicIdentifier>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(GeographicIdentifier.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + GeographicIdentifier.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
			testConstructor(WILL_SUCCEED, element);

			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(Util.buildDDMSElement("region", TEST_REGIONS.get(0)));
			testConstructor(WILL_SUCCEED, element);

			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(CountryCodeTest.getFixture(GeographicIdentifier.NAME).getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);

			if (isDDMS40OrGreater()) {
				element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_SUCCEED, element);
			}
			
			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);

			// No optional fields		
			testConstructor(WILL_SUCCEED, TEST_NAMES, null, null, null, null);
			testConstructor(WILL_SUCCEED, null, TEST_REGIONS, null, null, null);
			testConstructor(WILL_SUCCEED, null, null, CountryCodeTest.getFixture(GeographicIdentifier.NAME), null, null);
			if (isDDMS40OrGreater())
				testConstructor(WILL_SUCCEED, null, null, null, subCode, null);
			testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// At least 1 name, region, countryCode, or facilityIdentifier must exist.
			Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			testConstructor(WILL_FAIL, element);

			// No more than 1 countryCode
			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(CountryCodeTest.getFixture(GeographicIdentifier.NAME).getXOMElementCopy());
			element.appendChild(CountryCodeTest.getFixture(GeographicIdentifier.NAME).getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 subDivisionCode
			if (isDDMS40OrGreater()) {
				element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				element.appendChild(SubDivisionCodeTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_FAIL, element);
			}
			
			// No more than 1 facilityIdentifier
			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// facilityIdentifier must be alone
			element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(CountryCodeTest.getFixture(GeographicIdentifier.NAME).getXOMElementCopy());
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// At least 1 name, region, countryCode, subDivisionCode or facilityIdentifier must exist.
			testConstructor(WILL_FAIL, null, null, null, null, null);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS,
				CountryCodeTest.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			element.appendChild(FacilityIdentifierTest.getFixture().getXOMElementCopy());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, null, TEST_REGIONS, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, subCode, null);
			assertFalse(elementComponent.equals(dataComponent));

			if (isDDMS40OrGreater()) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest
					.getFixture(GeographicIdentifier.NAME), null, null);
				assertFalse(elementComponent.equals(dataComponent));
			}
			
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, FacilityIdentifierTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubDivisionCode subCode = isDDMS40OrGreater() ? SubDivisionCodeTest.getFixture() : null;
			
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, CountryCodeTest
				.getFixture(GeographicIdentifier.NAME), subCode, null);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCountryCodeReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode code = CountryCodeTest.getFixture(GeographicIdentifier.NAME);
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, code, null, null);
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, code, null, null);
		}
	}
	
	public void testSubDivisionCodeReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			
			if (!isDDMS40OrGreater())
				continue;
			
			SubDivisionCode code = SubDivisionCodeTest.getFixture();
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, code, null);
			testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, code, null);
		}
	}

	public void testFacilityIdentifierReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			FacilityIdentifier facId = FacilityIdentifierTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, null, facId);
			testConstructor(WILL_SUCCEED, null, null, null, null, facId);
		}
	}

	public void testWrongVersionCountryCode() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		CountryCode code = CountryCodeTest.getFixture(GeographicIdentifier.NAME);
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeographicIdentifier(TEST_NAMES, TEST_REGIONS, code);
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
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
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
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			// Non-FacID-based
			builder = new GeographicIdentifier.Builder();
			builder.getNames().add("Name");
			builder.getRegions().add("Region");
			CountryCode countryCode = CountryCodeTest.getFixture(GeographicIdentifier.NAME);
			builder.getCountryCode().setParentType(countryCode.getParentType());
			builder.getCountryCode().setQualifier(countryCode.getQualifier());
			builder.getCountryCode().setValue(countryCode.getValue());
			builder.commit();
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeographicIdentifier.Builder builder = new GeographicIdentifier.Builder();
			assertNotNull(builder.getNames().get(1));
			assertNotNull(builder.getRegions().get(1));
		}
	}
}
