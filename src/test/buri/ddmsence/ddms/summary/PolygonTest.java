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
 * <p>Tests related to gml:Polygon elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.0
 */
public class PolygonTest extends AbstractComponentTestCase {

	public static final List<Double> TEST_COORDS_1 = new ArrayList<Double>();
	static {
		TEST_COORDS_1.add(new Double(32.1));
		TEST_COORDS_1.add(new Double(40.1));
	}	
	public static final List<Double> TEST_COORDS_2 = new ArrayList<Double>();
	static {
		TEST_COORDS_2.add(new Double(42.1));
		TEST_COORDS_2.add(new Double(40.1));
	}
	public static final List<Double> TEST_COORDS_3 = new ArrayList<Double>();
	static {
		TEST_COORDS_3.add(new Double(42.1));
		TEST_COORDS_3.add(new Double(50.1));
	}	
	private final List<Position> TEST_POSITIONS = new ArrayList<Position>();
	private final SRSAttributes TEST_SRS_ATTRIBUTES;
	
	/**
	 * Constructor
	 */
	public PolygonTest() throws InvalidDDMSException {
		super("polygon.xml", GML_NAMESPACE, GML_XSD);
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
		TEST_POSITIONS.add(new Position(TEST_COORDS_1, TEST_SRS_ATTRIBUTES));
		TEST_POSITIONS.add(new Position(TEST_COORDS_2, TEST_SRS_ATTRIBUTES));
		TEST_POSITIONS.add(new Position(TEST_COORDS_3, TEST_SRS_ATTRIBUTES));
		TEST_POSITIONS.add(new Position(TEST_COORDS_1, TEST_SRS_ATTRIBUTES));
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Polygon testConstructor(boolean expectFailure, Element element) {
		Polygon component = null;
		try {
			component = new Polygon(element);
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
	 * @param positions the positions (required)
	 * @param srsAttributes the srs attributes (required)
	 * @param id the id (required)
	 * @return a valid object
	 */
	private Polygon testConstructor(boolean expectFailure, List<Position> positions, SRSAttributes srsAttributes, String id) {
		Polygon component = null;
		try {
			component = new Polygon(positions, srsAttributes, id);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}
	
	/**
	 * Wraps a list of positions into the nested elements needed for a valid construct
	 * 
	 * @param positions the positions
	 * @return an exterior element containing a LinearRing element containing the positions
	 */
	private Element wrapPositions(List<Position> positions) {
		Element ringElement = Util.buildElement(GML_PREFIX, "LinearRing", GML_NAMESPACE, null);
		for (Position pos : positions) {
			ringElement.appendChild(pos.getXOMElementCopy());
		}
		Element extElement = Util.buildElement(GML_PREFIX, "exterior", GML_NAMESPACE, null);
		extElement.appendChild(ringElement);
		return (extElement);
	}
	
	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"geospatial.boundingGeometry.id\" content=\"").append(TEST_ID).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.type\" content=\"Polygon\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsName\" content=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsDimension\" content=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.axisLabels\" content=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.uomLabels\" content=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\" />\n");
		for (Position pos : TEST_POSITIONS) {
			html.append(pos.toHTML());
		}
		return (html.toString());
	}
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Geospatial Geometry ID: ").append(TEST_ID).append("\n");
		text.append("Geospatial Geometry Type: Polygon\n");
		text.append("Geospatial Geometry SRS Name: ").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\n");
		text.append("Geospatial Geometry SRS Dimension: ").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\n");
		text.append("Geospatial Geometry Axis Labels: ").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\n");
		text.append("Geospatial Geometry Unit of Measure Labels: ").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\n");
		for (Position pos : TEST_POSITIONS) {
			text.append(pos.toText());
		}
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:Polygon xmlns:gml=\"").append(GML_NAMESPACE).append("\" ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\">\n\t");
		xml.append("<gml:exterior>\n\t\t");
		xml.append("<gml:LinearRing>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_1)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_2)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_3)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_1)).append("</gml:pos>\n\t\t");
		xml.append("</gml:LinearRing>\n\t");
		xml.append("</gml:exterior>\n");
		xml.append("</gml:Polygon>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}	
	
	public void testName() {
		Polygon component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Polygon.NAME, component.getName());
		assertEquals(Polygon.GML_PREFIX, component.getPrefix());
		assertEquals(Polygon.GML_PREFIX + ":" + Polygon.NAME, component.getQualifiedName());
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Polygon.NAME, null);
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE, TEST_SRS_ATTRIBUTES.getSrsName());
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_SUCCEED, element);		
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
	}
	 
	public void testElementConstructorInvalid() throws InvalidDDMSException {
		// Missing SRS Name
		Element element = Util.buildDDMSElement(Polygon.NAME, null);
		SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);
		
		// Empty SRS Name
		element = Util.buildDDMSElement(Polygon.NAME, null);
		attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);
		
		// Polygon SRS Name doesn't match pos SRS Name
		element = Util.buildDDMSElement(Polygon.NAME, null);
		attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		attr.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);
		
		// Missing ID
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);
		
		// Empty ID
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, "");
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);
		
		// ID not NCName
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, "1TEST");
		element.appendChild(wrapPositions(TEST_POSITIONS));
		testConstructor(WILL_FAIL, element);

		// Missing Positions
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		element.appendChild(wrapPositions(new ArrayList<Position>()));
		testConstructor(WILL_FAIL, element);
		
		// First position doesn't match last position.
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		List<Position> newPositions = new ArrayList<Position>(TEST_POSITIONS);
		newPositions.add(TEST_POSITIONS.get(1));
		element.appendChild(wrapPositions(newPositions));
		testConstructor(WILL_FAIL, element);
		
		// Not enough positions
		element = Util.buildDDMSElement(Polygon.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		Util.addAttribute(element, GML_PREFIX, "id", GML_NAMESPACE, TEST_ID);
		newPositions = new ArrayList<Position>();
		newPositions.add(TEST_POSITIONS.get(0));
		element.appendChild(wrapPositions(newPositions));
		testConstructor(WILL_FAIL, element);
		
		// Tests on shared attributes are done in the PositionTest.		
	}
	
	public void testDataConstructorInvalid() throws InvalidDDMSException {
		// Missing SRS Name
		SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		testConstructor(WILL_FAIL, TEST_POSITIONS, attr, TEST_ID);
		
		// Empty SRS Name
		attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
		testConstructor(WILL_FAIL, TEST_POSITIONS, attr, TEST_ID);
		
		// Polygon SRS Name doesn't match pos SRS Name
		attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		testConstructor(WILL_FAIL, TEST_POSITIONS, attr, TEST_ID);
		
		// Missing ID
		testConstructor(WILL_FAIL, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, null);
		
		// Empty ID
		testConstructor(WILL_FAIL, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, "");
		
		// ID not NCName
		testConstructor(WILL_FAIL, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, "1TEST");

		// Missing Positions
		testConstructor(WILL_FAIL, null, TEST_SRS_ATTRIBUTES, TEST_ID);
		
		// First position doesn't match last position.
		List<Position> newPositions = new ArrayList<Position>(TEST_POSITIONS);
		newPositions.add(TEST_POSITIONS.get(1));		
		testConstructor(WILL_FAIL, newPositions, TEST_SRS_ATTRIBUTES, TEST_ID);

		// Not enough positions
		newPositions = new ArrayList<Position>();
		newPositions.add(TEST_POSITIONS.get(0));		
		testConstructor(WILL_FAIL, newPositions, TEST_SRS_ATTRIBUTES, TEST_ID);	
	}
		
	public void testWarnings() {
		// No warnings
		Polygon component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Polygon dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		SRSAttributes attr = new SRSAttributes(TEST_SRS_ATTRIBUTES.getSrsName(), new Integer(11), TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
		Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Polygon dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITIONS, attr, TEST_ID);
		assertFalse(elementComponent.equals(dataComponent));

		List<Position> newPositions = new ArrayList<Position>(TEST_POSITIONS);
		newPositions.add(TEST_POSITIONS.get(1));
		newPositions.add(TEST_POSITIONS.get(0));		
		dataComponent = testConstructor(WILL_SUCCEED, newPositions, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));

	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Polygon component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Polygon component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Polygon component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}
	
	public void testPositionReuse() {
		testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
		testConstructor(WILL_SUCCEED, TEST_POSITIONS, TEST_SRS_ATTRIBUTES, TEST_ID);
	}
}
