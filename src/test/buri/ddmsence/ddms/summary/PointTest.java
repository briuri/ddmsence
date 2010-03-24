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
 * <p>Tests related to gml:Point elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PointTest extends AbstractComponentTestCase {
	
	private final Position TEST_POSITION;
	private final SRSAttributes TEST_SRS_ATTRIBUTES;
	
	/**
	 * Constructor
	 */
	public PointTest() throws InvalidDDMSException {
		super("point.xml", GML_NAMESPACE, GML_XSD);
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
		TEST_POSITION = new Position(PositionTest.TEST_COORDS, TEST_SRS_ATTRIBUTES);
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Point testConstructor(boolean expectFailure, Element element) {
		Point component = null;
		try {
			component = new Point(element);
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
	 * @param position the position (required)
	 * @param srsAttributes the srs attributes (required)
	 * @param id the id (required)
	 * @return a valid object
	 */
	private Point testConstructor(boolean expectFailure, Position position, SRSAttributes srsAttributes, String id) {
		Point component = null;
		try {
			component = new Point(position, srsAttributes, id);
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
		html.append("<meta name=\"geospatial.boundingGeometry.id\" content=\"").append(TEST_ID).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.type\" content=\"Point\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsName\" content=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsDimension\" content=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.axisLabels\" content=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.uomLabels\" content=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\" />\n");
		html.append(TEST_POSITION.toHTML());
		return (html.toString());
	}
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Geospatial Geometry ID: ").append(TEST_ID).append("\n");
		text.append("Geospatial Geometry Type: Point\n");
		text.append("Geospatial Geometry SRS Name: ").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\n");
		text.append("Geospatial Geometry SRS Dimension: ").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\n");
		text.append("Geospatial Geometry Axis Labels: ").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\n");
		text.append("Geospatial Geometry Unit of Measure Labels: ").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\n");
		text.append(TEST_POSITION.toText());
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:Point xmlns:gml=\"").append(GML_NAMESPACE).append("\" ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\">\n\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\">");
		xml.append(PositionTest.TEST_XS_LIST).append("</gml:pos>\n");
		xml.append("</gml:Point>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testName() {
		Point component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Point.NAME, component.getName());
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Point.NAME, null);
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE, TEST_SRS_ATTRIBUTES.getSrsName());
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);		
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
	}

	
	public void testElementConstructorInvalid() throws InvalidDDMSException {
		// Missing SRS Name
		Element element = Util.buildDDMSElement(Point.NAME, null);
		SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// Empty SRS Name
		element = Util.buildDDMSElement(Point.NAME, null);
		attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// Point SRS Name doesn't match pos SRS Name
		element = Util.buildDDMSElement(Point.NAME, null);
		attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// Missing ID
		element = Util.buildDDMSElement(Point.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// Empty ID
		element = Util.buildDDMSElement(Point.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, "");
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);
		
		// ID not NCName
		element = Util.buildDDMSElement(Point.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, "1TEST");
		element.appendChild(TEST_POSITION.getXOMElementCopy());
		testConstructor(WILL_FAIL, element);	
		
		// Missing position
		element = Util.buildDDMSElement(Point.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testDataConstructorInvalid() throws InvalidDDMSException {
		// Missing SRS Name
		SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		testConstructor(WILL_FAIL, TEST_POSITION, attr, TEST_ID);
		
		// Empty SRS Name
		attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		testConstructor(WILL_FAIL, TEST_POSITION, attr, TEST_ID);
		
		// Polygon SRS Name doesn't match pos SRS Name
		attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		testConstructor(WILL_FAIL, TEST_POSITION, attr, TEST_ID);
		
		// Missing ID
		testConstructor(WILL_FAIL, TEST_POSITION, TEST_SRS_ATTRIBUTES, null);
		
		// Empty ID
		testConstructor(WILL_FAIL, TEST_POSITION, TEST_SRS_ATTRIBUTES, "");
		
		// ID not NCName
		testConstructor(WILL_FAIL, TEST_POSITION, TEST_SRS_ATTRIBUTES, "1TEST");

		// Missing position
		testConstructor(WILL_FAIL, null, TEST_SRS_ATTRIBUTES, TEST_ID);		
	}
		
	public void testConstructorEquality() {
		Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Point dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		SRSAttributes attr = new SRSAttributes(TEST_SRS_ATTRIBUTES.getSrsName(), new Integer(11), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Point dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITION, attr, TEST_ID);
		assertFalse(elementComponent.equals(dataComponent));
		
		List<Double> newCoords = new ArrayList<Double>();
		newCoords.add(new Double(56.0));
		newCoords.add(new Double(150.0));
		Position newPosition = new Position(newCoords, TEST_SRS_ATTRIBUTES);
		
		dataComponent = testConstructor(WILL_SUCCEED, newPosition, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));

	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Point component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Point component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Point component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}
	
	public void testPositionReuse() {
		testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
		testConstructor(WILL_SUCCEED, TEST_POSITION, TEST_SRS_ATTRIBUTES, TEST_ID);
	}
}
