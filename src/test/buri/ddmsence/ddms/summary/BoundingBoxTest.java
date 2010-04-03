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
 * <p>Tests related to ddms:boundingBox elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BoundingBoxTest extends AbstractComponentTestCase {
	
	private static final double TEST_WEST = 12.3;
	private static final double TEST_EAST = 23.4;
	private static final double TEST_SOUTH = 34.5;
	private static final double TEST_NORTH = 45.6;
	
	/**
	 * Constructor
	 */
	public BoundingBoxTest() {
		super("3.0/boundingBox.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private BoundingBox testConstructor(boolean expectFailure, Element element) {
		BoundingBox component = null;
		try {
			component = new BoundingBox(element);
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
	 * @param westBL the westbound longitude
	 * @param eastBL the eastbound longitude
	 * @param southBL the southbound latitude
	 * @param northBL the northbound latitude
	 * @return a valid object
	 */
	private BoundingBox testConstructor(boolean expectFailure, double westBL, double eastBL, double southBL, double northBL) {
		BoundingBox component = null;
		try {
			component = new BoundingBox(westBL, eastBL, southBL, northBL);
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
		html.append("<meta name=\"geospatial.boundingBox.WestBL\" content=\"").append(TEST_WEST).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingBox.EastBL\" content=\"").append(TEST_EAST).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingBox.SouthBL\" content=\"").append(TEST_SOUTH).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingBox.NorthBL\" content=\"").append(TEST_NORTH).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Bounding Box Westbound Longitude: ").append(TEST_WEST).append("\n");
		text.append("Bounding Box Eastbound Longitude: ").append(TEST_EAST).append("\n");
		text.append("Bounding Box Southbound Latitude: ").append(TEST_SOUTH).append("\n");
		text.append("Bounding Box Northbound Latitude: ").append(TEST_NORTH).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:boundingBox xmlns:ddms=\"").append(DDMSVersion.getCurrentNamespace()).append("\">\n\t");
		xml.append("<ddms:WestBL>").append(TEST_WEST).append("</ddms:WestBL>\n\t");
		xml.append("<ddms:EastBL>").append(TEST_EAST).append("</ddms:EastBL>\n\t");
		xml.append("<ddms:SouthBL>").append(TEST_SOUTH).append("</ddms:SouthBL>\n\t");
		xml.append("<ddms:NorthBL>").append(TEST_NORTH).append("</ddms:NorthBL>\n");
		xml.append("</ddms:boundingBox>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testNameAndNamespace() {
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(BoundingBox.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + BoundingBox.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		testConstructor(WILL_SUCCEED, getValidElement());
	}
	
	public void testDataConstructorValid() {
		testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
	}
	
	public void testElementConstructorInvalid() {
		// Missing values
		Element element = Util.buildDDMSElement(BoundingBox.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// Not Double
		element = Util.buildDDMSElement(BoundingBox.NAME, null);
		element.appendChild(Util.buildDDMSElement("WestBL", String.valueOf("west")));
		element.appendChild(Util.buildDDMSElement("EastBL", String.valueOf(TEST_EAST)));
		element.appendChild(Util.buildDDMSElement("SouthBL", String.valueOf(TEST_SOUTH)));
		element.appendChild(Util.buildDDMSElement("NorthBL", String.valueOf(TEST_NORTH)));
		testConstructor(WILL_FAIL, element);
				
		// Longitude too small
		element = Util.buildDDMSElement(BoundingBox.NAME, null);
		element.appendChild(Util.buildDDMSElement("WestBL", "-181"));
		element.appendChild(Util.buildDDMSElement("EastBL", String.valueOf(TEST_EAST)));
		element.appendChild(Util.buildDDMSElement("SouthBL", String.valueOf(TEST_SOUTH)));
		element.appendChild(Util.buildDDMSElement("NorthBL", String.valueOf(TEST_NORTH)));
		testConstructor(WILL_FAIL, element);
		
		// Longitude too big
		element = Util.buildDDMSElement(BoundingBox.NAME, null);
		element.appendChild(Util.buildDDMSElement("WestBL", "181"));
		element.appendChild(Util.buildDDMSElement("EastBL", String.valueOf(TEST_EAST)));
		element.appendChild(Util.buildDDMSElement("SouthBL", String.valueOf(TEST_SOUTH)));
		element.appendChild(Util.buildDDMSElement("NorthBL", String.valueOf(TEST_NORTH)));
		testConstructor(WILL_FAIL, element);
		
		// Latitude too small
		element = Util.buildDDMSElement(BoundingBox.NAME, null);
		element.appendChild(Util.buildDDMSElement("WestBL", String.valueOf(TEST_WEST)));
		element.appendChild(Util.buildDDMSElement("EastBL", String.valueOf(TEST_EAST)));
		element.appendChild(Util.buildDDMSElement("SouthBL", "-91"));
		element.appendChild(Util.buildDDMSElement("NorthBL", String.valueOf(TEST_NORTH)));
		testConstructor(WILL_FAIL, element);
		
		// Latitude too big
		element = Util.buildDDMSElement(BoundingBox.NAME, null);
		element.appendChild(Util.buildDDMSElement("WestBL", String.valueOf(TEST_WEST)));
		element.appendChild(Util.buildDDMSElement("EastBL", String.valueOf(TEST_EAST)));
		element.appendChild(Util.buildDDMSElement("SouthBL", "91"));
		element.appendChild(Util.buildDDMSElement("NorthBL", String.valueOf(TEST_NORTH)));
		testConstructor(WILL_FAIL, element);
	}
	
	public void testDataConstructorInvalid() {
		// Longitude too small
		testConstructor(WILL_FAIL, -181, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		
		// Longitude too big
		testConstructor(WILL_FAIL, 181, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		
		// Latitude too small
		testConstructor(WILL_FAIL, TEST_WEST, TEST_EAST, -91, TEST_NORTH);
		
		// Latitude too big
		testConstructor(WILL_FAIL, TEST_WEST, TEST_EAST, 91, TEST_NORTH);
	}
		
	public void testWarnings() {
		// No warnings
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		BoundingBox elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		BoundingBox dataComponent = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		BoundingBox elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		BoundingBox dataComponent = testConstructor(WILL_SUCCEED, 10, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_WEST, 10, TEST_SOUTH, TEST_NORTH);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, 10, TEST_NORTH);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, 10);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		BoundingBox elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}	
	
	public void testDoubleEquality() {
		BoundingBox component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(component.getWestBL(), Double.valueOf(TEST_WEST));
		assertEquals(component.getEastBL(), Double.valueOf(TEST_EAST));
		assertEquals(component.getSouthBL(), Double.valueOf(TEST_SOUTH));
		assertEquals(component.getNorthBL(), Double.valueOf(TEST_NORTH));
	}
}
