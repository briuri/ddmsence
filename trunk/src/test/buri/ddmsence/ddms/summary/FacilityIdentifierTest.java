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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:facilityIdentifier elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class FacilityIdentifierTest extends AbstractComponentTestCase {
	private static final String TEST_BENUMBER = "1234DD56789";
	private static final String TEST_OSUFFIX = "DD123";
	
	/**
	 * Constructor
	 */
	public FacilityIdentifierTest() {
		super("3.0/facilityIdentifier.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private FacilityIdentifier testConstructor(boolean expectFailure, Element element) {
		FacilityIdentifier component = null;
		try {
			component = new FacilityIdentifier(element);
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
	 * @param beNumber the beNumber (required)
	 * @param osuffix the Osuffix (required, because beNumber is required)
	 * @return a valid object
	 */
	private FacilityIdentifier testConstructor(boolean expectFailure, String beNumber, String osuffix) {
		FacilityIdentifier component = null;
		try {
			component = new FacilityIdentifier(beNumber, osuffix);
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
		html.append("<meta name=\"geospatial.identifier.facility.BEnumber\" content=\"").append(TEST_BENUMBER).append("\" />\n");
		html.append("<meta name=\"geospatial.identifier.facility.Osuffix\" content=\"").append(TEST_OSUFFIX).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Geographic Identifier BEnumber: ").append(TEST_BENUMBER).append("\n");
		text.append("Geographic Identifier Osuffix: ").append(TEST_OSUFFIX).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:facilityIdentifier xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		xml.append("ddms:beNumber=\"").append(TEST_BENUMBER).append("\" ");
		xml.append("ddms:osuffix=\"").append(TEST_OSUFFIX).append("\" />");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testNameAndNamespace() {
		FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(FacilityIdentifier.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + FacilityIdentifier.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		testConstructor(WILL_SUCCEED, getValidElement());
	}
	
	public void testDataConstructorValid() {
		testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
	}
	 
	public void testElementConstructorInvalid() {
		// Missing beNumber
		Element element = Util.buildDDMSElement(FacilityIdentifier.NAME, null);
		Util.addDDMSAttribute(element, "osuffix", TEST_OSUFFIX);
		testConstructor(WILL_FAIL, element);
		
		// Empty beNumber
		element = Util.buildDDMSElement(FacilityIdentifier.NAME, null);
		Util.addDDMSAttribute(element, "beNumber", "");
		Util.addDDMSAttribute(element, "osuffix", TEST_OSUFFIX);
		testConstructor(WILL_FAIL, element);
		
		// Missing osuffix
		element = Util.buildDDMSElement(FacilityIdentifier.NAME, null);
		Util.addDDMSAttribute(element, "beNumber", TEST_BENUMBER);
		testConstructor(WILL_FAIL, element);
		
		// Empty osuffix
		element = Util.buildDDMSElement(FacilityIdentifier.NAME, null);
		Util.addDDMSAttribute(element, "beNumber", TEST_BENUMBER);
		Util.addDDMSAttribute(element, "osuffix", "");
		testConstructor(WILL_FAIL, element);
	}
	
	public void testDataConstructorInvalid() {
		// Missing beNumber
		testConstructor(WILL_FAIL, null, TEST_OSUFFIX);
		
		// Empty beNumber
		testConstructor(WILL_FAIL, "", TEST_OSUFFIX);
		
		// Missing osuffix
		testConstructor(WILL_FAIL, TEST_BENUMBER, null);
		
		// Empty osuffix
		testConstructor(WILL_FAIL, TEST_BENUMBER, "");
	}
	
	public void testWarnings() {
		// No warnings
		FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		FacilityIdentifier dataComponent = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		FacilityIdentifier dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_OSUFFIX);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_BENUMBER, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}	
}
