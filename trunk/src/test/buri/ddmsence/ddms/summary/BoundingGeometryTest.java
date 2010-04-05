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
import buri.ddmsence.util.DDMSVersion;
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

	/**
	 * Constructor
	 */
	public BoundingGeometryTest() throws InvalidDDMSException {
		super("boundingGeometry.xml");
	}

	/**
	 * Returns a bounding box fixture
	 */
	protected static BoundingGeometry getFixture() throws InvalidDDMSException {
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(new Position(PositionTest.TEST_COORDS, SRSAttributesTest.getFixture()), SRSAttributesTest.getFixture(), TEST_ID));
		return (new BoundingGeometry(null, points));
	}
	
	/**
	 * Gets a test instance of a list of polygons
	 * 
	 * @return list of points
	 */
	private List<Polygon> getPolygons() throws InvalidDDMSException {
		List<Position> positions = new ArrayList<Position>();
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_2, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_3, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));

		List<Polygon> polygons = new ArrayList<Polygon>();
		polygons.add(new Polygon(positions, SRSAttributesTest.getFixture(), TEST_ID));
		return (polygons);
	}

	/**
	 * Gets a test instance of a list of points
	 * 
	 * @return list of points
	 */
	private List<Point> getPoints() throws InvalidDDMSException {
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(new Position(PositionTest.TEST_COORDS, null), SRSAttributesTest.getFixture(), TEST_ID));
		return (points);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private BoundingGeometry testConstructor(boolean expectFailure, Element element) {
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(element);
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
	 * @param polygons an ordered list of the polygons used in this geometry
	 * @param points an ordered list of the points used in this geometry
	 * @return a valid object
	 */
	private BoundingGeometry testConstructor(boolean expectFailure, List<Polygon> polygons, List<Point> points) {
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(polygons, points);
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
		html.append(getPoints().get(0).toHTML());
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(getPoints().get(0).toText());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:boundingGeometry xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\">\n\t");
		xml.append("<gml:Point xmlns:gml=\"").append(DDMSVersion.getCurrentVersion().getGmlNamespace()).append("\" ");
		xml
			.append("srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\" gml:id=\"IDValue\">\n\t\t");
		xml.append("<gml:pos>32.1 40.1</gml:pos>\n\t");
		xml.append("</gml:Point>\n");
		xml.append("</ddms:boundingGeometry>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(BoundingGeometry.NAME, component.getName());
			assertEquals(Util.DDMS_PREFIX, component.getPrefix());
			assertEquals(Util.DDMS_PREFIX + ":" + BoundingGeometry.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Point
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// Polygon
			Element element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
			element.appendChild(getPolygons().get(0).getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);

			// Both
			element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
			element.appendChild(getPolygons().get(0).getXOMElementCopy());
			element.appendChild(getPoints().get(0).getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Point
			testConstructor(WILL_SUCCEED, null, getPoints());

			// Polygon
			testConstructor(WILL_SUCCEED, getPolygons(), null);

			// Both
			testConstructor(WILL_SUCCEED, getPolygons(), getPoints());
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No polygons or points
			Element element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No polygons or points
			testConstructor(WILL_FAIL, null, null);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			BoundingGeometry dataComponent = testConstructor(WILL_SUCCEED, null, getPoints());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			BoundingGeometry dataComponent = testConstructor(WILL_SUCCEED, getPolygons(), getPoints());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getPolygons(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, null, getPoints());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getPolygons(), null);
			assertEquals(getPolygons().get(0).toHTML(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, getPoints());
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getPolygons(), null);
			assertEquals(getPolygons().get(0).toText(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, null, getPoints());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Position> positions = new ArrayList<Position>();
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_2, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_3, null));
		positions.add(new Position(PolygonTest.TEST_COORDS_1, null));
		List<Polygon> polygons = new ArrayList<Polygon>();
		polygons.add(new Polygon(positions, SRSAttributesTest.getFixture(), TEST_ID));
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new BoundingGeometry(polygons, null);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(new Position(PositionTest.TEST_COORDS, null), SRSAttributesTest.getFixture(), TEST_ID));
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new BoundingGeometry(null, points);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
}
