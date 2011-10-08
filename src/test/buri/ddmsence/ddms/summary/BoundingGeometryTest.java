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

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.summary.gml.Point;
import buri.ddmsence.ddms.summary.gml.PointTest;
import buri.ddmsence.ddms.summary.gml.Polygon;
import buri.ddmsence.ddms.summary.gml.PolygonTest;
import buri.ddmsence.ddms.summary.gml.Position;
import buri.ddmsence.ddms.summary.gml.PositionTest;
import buri.ddmsence.ddms.summary.gml.SRSAttributes;
import buri.ddmsence.ddms.summary.gml.SRSAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:subjectCoverage elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BoundingGeometryTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public BoundingGeometryTest() throws InvalidDDMSException {
		super("boundingGeometry.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static BoundingGeometry getFixture() {
		try {
			return (new BoundingGeometry(null, PointTest.getFixtureList()));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private BoundingGeometry getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(element);
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
	 * @param polygons an ordered list of the polygons used in this geometry
	 * @param points an ordered list of the points used in this geometry
	 * @return a valid object
	 */
	private BoundingGeometry getInstance(String message, List<Polygon> polygons, List<Point> points) {
		boolean expectFailure = !Util.isEmpty(message);
		BoundingGeometry component = null;
		try {
			component = new BoundingGeometry(polygons, points);
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
		StringBuffer text = new StringBuffer();
		text.append(PointTest.getFixtureList().get(0).getOutput(isHTML, "boundingGeometry."));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:boundingGeometry ").append(getXmlnsDDMS()).append(">\n\t");
		xml.append("<gml:Point ").append(getXmlnsGML()).append(" ");
		xml.append(
			"gml:id=\"IDValue\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" ")
			.append("axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\">\n\t\t");
		xml.append("<gml:pos>32.1 40.1</gml:pos>\n\t");
		xml.append("</gml:Point>\n");
		xml.append("</ddms:boundingGeometry>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				BoundingGeometry.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Point
			getInstance(SUCCESS, getValidElement(sVersion));

			// Polygon
			Element element = Util.buildDDMSElement(BoundingGeometry.getName(version), null);
			element.appendChild(PolygonTest.getFixtureList().get(0).getXOMElementCopy());
			getInstance(SUCCESS, element);

			// Both
			element = Util.buildDDMSElement(BoundingGeometry.getName(version), null);
			element.appendChild(PolygonTest.getFixtureList().get(0).getXOMElementCopy());
			element.appendChild(PointTest.getFixtureList().get(0).getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Point
			getInstance(SUCCESS, null, PointTest.getFixtureList());

			// Polygon
			getInstance(SUCCESS, PolygonTest.getFixtureList(), null);

			// Both
			getInstance(SUCCESS, PolygonTest.getFixtureList(), PointTest.getFixtureList());
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No polygons or points
			Element element = Util.buildDDMSElement(BoundingGeometry.getName(version), null);
			getInstance("At least 1 of ", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No polygons or points
			getInstance("At least 1 of ", null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			BoundingGeometry component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			BoundingGeometry dataComponent = getInstance(SUCCESS, null, PointTest.getFixtureList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			BoundingGeometry dataComponent = getInstance(SUCCESS, PolygonTest.getFixtureList(), PointTest
				.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, PolygonTest.getFixtureList(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, null, PointTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, PolygonTest.getFixtureList(), null);
			assertEquals(PolygonTest.getFixtureList().get(0).getOutput(true, "boundingGeometry."), component.toHTML());
			assertEquals(PolygonTest.getFixtureList().get(0).getOutput(false, "boundingGeometry."), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, null, PointTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingGeometry component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building (Point-based)
			BoundingGeometry.Builder builder = new BoundingGeometry.Builder(component);
			assertEquals(component, builder.commit());

			// Equality after Building (Polygon-based)
			component = new BoundingGeometry(PolygonTest.getFixtureList(), null);
			builder = new BoundingGeometry.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingGeometry.Builder builder = new BoundingGeometry.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getPoints().get(0).setId(TEST_ID);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingGeometry.Builder builder = new BoundingGeometry.Builder();
			for (Point point : PointTest.getFixtureList()) {
				Point.Builder pointBuilder = new Point.Builder(point);
				pointBuilder.setId("");
				builder.getPoints().add(pointBuilder);
			}
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "id is required.");
			}
			builder = new BoundingGeometry.Builder();
			for (Polygon polygon : PolygonTest.getFixtureList()) {
				builder.getPolygons().add(new Polygon.Builder(polygon));
			}
			builder.commit();

			// Skip empty Points
			builder = new BoundingGeometry.Builder();
			Point.Builder emptyBuilder = new Point.Builder();
			Point.Builder fullBuilder = new Point.Builder();
			fullBuilder.setSrsAttributes(new SRSAttributes.Builder(SRSAttributesTest.getFixture()));
			fullBuilder.setId(TEST_ID);
			fullBuilder.getPosition().getCoordinates().get(0).setValue(Double.valueOf(0));
			fullBuilder.getPosition().getCoordinates().get(1).setValue(Double.valueOf(0));
			builder.getPoints().add(emptyBuilder);
			builder.getPoints().add(fullBuilder);
			assertEquals(1, builder.commit().getPoints().size());

			// Skip empty Polygons
			builder = new BoundingGeometry.Builder();
			Polygon.Builder emptyPolygonBuilder = new Polygon.Builder();
			Polygon.Builder fullPolygonBuilder = new Polygon.Builder();
			fullPolygonBuilder.setSrsAttributes(new SRSAttributes.Builder(SRSAttributesTest.getFixture()));
			fullPolygonBuilder.setId(TEST_ID);
			fullPolygonBuilder.getPositions().add(new Position.Builder());
			fullPolygonBuilder.getPositions().add(new Position.Builder());
			fullPolygonBuilder.getPositions().add(new Position.Builder());
			fullPolygonBuilder.getPositions().add(new Position.Builder());
			fullPolygonBuilder.getPositions().get(0).getCoordinates().get(0).setValue(PositionTest.TEST_COORDS.get(0));
			fullPolygonBuilder.getPositions().get(0).getCoordinates().get(1).setValue(PositionTest.TEST_COORDS.get(1));

			fullPolygonBuilder.getPositions().get(1).getCoordinates().get(0)
				.setValue(PositionTest.TEST_COORDS_2.get(0));
			fullPolygonBuilder.getPositions().get(1).getCoordinates().get(1)
				.setValue(PositionTest.TEST_COORDS_2.get(1));

			fullPolygonBuilder.getPositions().get(2).getCoordinates().get(0)
				.setValue(PositionTest.TEST_COORDS_3.get(0));
			fullPolygonBuilder.getPositions().get(2).getCoordinates().get(1)
				.setValue(PositionTest.TEST_COORDS_3.get(1));

			fullPolygonBuilder.getPositions().get(3).getCoordinates().get(0).setValue(PositionTest.TEST_COORDS.get(0));
			fullPolygonBuilder.getPositions().get(3).getCoordinates().get(1).setValue(PositionTest.TEST_COORDS.get(1));

			builder.getPolygons().add(emptyPolygonBuilder);
			builder.getPolygons().add(fullPolygonBuilder);
			assertEquals(1, builder.commit().getPolygons().size());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry.Builder builder = new BoundingGeometry.Builder();
			assertNotNull(builder.getPoints().get(1));
			assertNotNull(builder.getPolygons().get(1));
		}
	}
}
