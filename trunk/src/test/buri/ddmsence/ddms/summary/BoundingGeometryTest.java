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
 * <p>Tests related to ddms:subjectCoverage elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:Subject tag is done separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BoundingGeometryTest extends AbstractComponentTestCase {
	
	private final List<Polygon> TEST_POLYGONS = new ArrayList<Polygon>();
	private final List<Point> TEST_POINTS = new ArrayList<Point>();
	private final SRSAttributes TEST_SRS_ATTRIBUTES;
	
	/**
	 * Constructor
	 */
	public BoundingGeometryTest() throws InvalidDDMSException {
		super("boundingGeometry.xml");
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
		List<Position> positions = new ArrayList<Position>();
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_2, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_3, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));		
		TEST_POLYGONS.add(new Polygon(positions, TEST_SRS_ATTRIBUTES, TEST_ID));
		TEST_POINTS.add(new Point(new Position(PositionTest.TEST_COORDS, null), TEST_SRS_ATTRIBUTES, TEST_ID));
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element	the element to build from
	 * @return a valid object
	 */
	private BoundingGeometry testConstructor(boolean expectFailure, Element element) {
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(element);
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
	 * @param polygons an ordered list of the polygons used in this geometry
	 * @param points an ordered list of the points used in this geometry
	 * @return a valid object
	 */
	private BoundingGeometry testConstructor(boolean expectFailure, List<Polygon> polygons, List<Point> points) {
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(polygons, points);
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
		html.append(TEST_POINTS.get(0).toHTML());
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(TEST_POINTS.get(0).toText());
		return (text.toString());
	}
						
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:boundingGeometry xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\">\n\t");
		xml.append("<gml:Point xmlns:gml=\"http://www.opengis.net/gml/3.2\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\" gml:id=\"IDValue\">\n\t\t");
		xml.append("<gml:pos>32.1 40.1</gml:pos>\n\t");
		xml.append("</gml:Point>\n");
		xml.append("</ddms:boundingGeometry>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testName() {
		BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(BoundingGeometry.NAME, component.getName());
	}
	
	public void testElementConstructorValid() {
		// Point
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// Polygon
		Element element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
		element.appendChild(TEST_POLYGONS.get(0).getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
		
		// Both
		element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
		element.appendChild(TEST_POLYGONS.get(0).getXOMElementCopy());
		element.appendChild(TEST_POINTS.get(0).getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testDataConstructorValid() {
		// Point
		testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		
		// Polygon
		testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
		
		// Both
		testConstructor(WILL_SUCCEED, TEST_POLYGONS, TEST_POINTS);
	}
		
	public void testElementConstructorInvalid() {
		// No polygons or points
		Element element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
		testConstructor(WILL_FAIL, element);
	}	

	public void testDataConstructorInvalid() {
		// No polygons or points
		testConstructor(WILL_FAIL, null, null);
	}
	
	public void testWarnings() {
		// No warnings
		BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		BoundingGeometry dataComponent = testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		BoundingGeometry dataComponent = testConstructor(WILL_SUCCEED, TEST_POLYGONS, TEST_POINTS);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
		assertEquals(TEST_POLYGONS.get(0).toHTML(), component.toHTML());
	}	
	
	public void testTextOutput() {
		BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
		assertEquals(TEST_POLYGONS.get(0).toText(), component.toText());
	}
	
	public void testXMLOutput() {
		BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());

		component = testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		assertEquals(getExpectedXMLOutput(false), component.toXML());	
	}
		
	public void testPointReuse() {
		testConstructor(WILL_SUCCEED, null, TEST_POINTS);
		testConstructor(WILL_SUCCEED, null, TEST_POINTS);
	}
	
	public void testPolygonReuse() {
		testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
		testConstructor(WILL_SUCCEED, TEST_POLYGONS, null);
	}
}
