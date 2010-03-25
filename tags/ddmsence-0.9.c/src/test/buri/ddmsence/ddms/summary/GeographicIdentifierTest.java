/* Copyright 2010 by Brian Uri!
   
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
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:geographicIdentifier elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:countryCode/ddms:facilityIdentifier tags is done separately.
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
	private CountryCode TEST_COUNTRY_CODE;
	private FacilityIdentifier TEST_FAC_ID;
	
	/**
	 * Constructor
	 */
	public GeographicIdentifierTest() throws InvalidDDMSException {
		super("geographicIdentifier.xml");
		TEST_COUNTRY_CODE = new CountryCode(GeographicIdentifier.NAME, "ISO-3166", "USA");
		TEST_FAC_ID = new FacilityIdentifier("1234DD56789", "DD123");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private GeographicIdentifier testConstructor(boolean expectFailure, Element element) {
		GeographicIdentifier component = null;
		try {
			component = new GeographicIdentifier(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure	true if this operation is expected to succeed, false otherwise
	 * @param names the names (optional)
	 * @param regions the region names (optional)
	 * @param countryCode the country code (optional)
	 * @param facilityIdentifier the facility identifier (optional)
	 * @return a valid object
	 */
	private GeographicIdentifier testConstructor(boolean expectFailure, List<String> names, List<String> regions, CountryCode countryCode, FacilityIdentifier facilityIdentifier) {
		GeographicIdentifier component = null;
		try {
			if (facilityIdentifier != null)
				component = new GeographicIdentifier(facilityIdentifier);
			else
				component = new GeographicIdentifier(names, regions, countryCode);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}
		
	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"geospatial.identifier.name\" content=\"").append(TEST_NAMES.get(0)).append("\" />\n");
		html.append("<meta name=\"geospatial.identifier.region\" content=\"").append(TEST_REGIONS.get(0)).append("\" />\n");
		html.append(TEST_COUNTRY_CODE.toHTML());
		return (html.toString());
	}
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Geographic Identifier Name: ").append(TEST_NAMES.get(0)).append("\n");
		text.append("Geographic Identifier Region: ").append(TEST_REGIONS.get(0)).append("\n");
		text.append(TEST_COUNTRY_CODE.toText());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geographicIdentifier xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\">\n\t");
		xml.append("<ddms:name>The White House</ddms:name>\n\t");
		xml.append("<ddms:region>Mid-Atlantic States</ddms:region>\n\t");
		xml.append("<ddms:countryCode ddms:qualifier=\"ISO-3166\" ddms:value=\"USA\" />\n");
		xml.append("</ddms:geographicIdentifier>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}

	public void testName() {
		GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(GeographicIdentifier.NAME, component.getName());
	}

	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());

		// No optional fields
		Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
		testConstructor(WILL_SUCCEED, element);
		
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(Util.buildDDMSElement("region", TEST_REGIONS.get(0)));
		testConstructor(WILL_SUCCEED, element);
		
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_COUNTRY_CODE.getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
		
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_FAC_ID.getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		
		// No optional fields		
		testConstructor(WILL_SUCCEED, TEST_NAMES, null, null, null);
		testConstructor(WILL_SUCCEED, null, TEST_REGIONS, null, null);
		testConstructor(WILL_SUCCEED, null, null, TEST_COUNTRY_CODE, null);
		testConstructor(WILL_SUCCEED, null, null, null, TEST_FAC_ID);
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		// At least 1 name, region, countryCode, or facilityIdentifier must exist.
		Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// No more than 1 countryCode
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_COUNTRY_CODE.getXOMElementCopy());
		element.appendChild(TEST_COUNTRY_CODE.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// No more than 1 facilityIdentifier
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_FAC_ID.getXOMElementCopy());
		element.appendChild(TEST_FAC_ID.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// facilityIdentifier must be alone
		element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_COUNTRY_CODE.getXOMElementCopy());
		element.appendChild(TEST_FAC_ID.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
	}

	public void testDataConstructorInvalid() {		
		// At least 1 name, region, countryCode, or facilityIdentifier must exist.
		testConstructor(WILL_FAIL, null, null, null, null);
	}
	
	public void testWarnings() {
		// No warnings
		GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
		Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		element.appendChild(TEST_FAC_ID.getXOMElementCopy());
		elementComponent = testConstructor(WILL_SUCCEED, element);		
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, TEST_FAC_ID);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
	}

	public void testConstructorInequalityDifferentValues() {
		GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		GeographicIdentifier dataComponent = testConstructor(WILL_SUCCEED, null, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		assertFalse(elementComponent.equals(dataComponent));

		dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, TEST_COUNTRY_CODE, null);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, null, null);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, TEST_FAC_ID);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		GeographicIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	
	
	public void testTextOutput() {
		GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		GeographicIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}	

	public void testCountryCodeReuse() {
		testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
		testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_REGIONS, TEST_COUNTRY_CODE, null);
	}
	
	public void testFacilityIdentifierReuse() {
		testConstructor(WILL_SUCCEED, null, null, null, TEST_FAC_ID);
		testConstructor(WILL_SUCCEED, null, null, null, TEST_FAC_ID);
	}
}
