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
 * <p>Tests related to gml:Point elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PointTest extends AbstractComponentTestCase {

	private final SRSAttributes TEST_SRS_ATTRIBUTES;

	/**
	 * Constructor
	 */
	public PointTest() throws InvalidDDMSException {
		super("point.xml");
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();

	}

	/**
	 * Returns a test fixture position
	 * 
	 * @return a position
	 * @throws InvalidDDMSException
	 */
	private Position getPosition() throws InvalidDDMSException {
		return (new Position(PositionTest.TEST_COORDS, TEST_SRS_ATTRIBUTES));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Point testConstructor(boolean expectFailure, Element element) {
		Point component = null;
		try {
			component = new Point(element);
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
		html.append("<meta name=\"geospatial.boundingGeometry.id\" content=\"").append(TEST_ID).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.type\" content=\"Point\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsName\" content=\"").append(
			TEST_SRS_ATTRIBUTES.getSrsName()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsDimension\" content=\"").append(
			TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.axisLabels\" content=\"").append(
			TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.uomLabels\" content=\"").append(
			TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList()).append("\" />\n");
		html.append(getPosition().toHTML());
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append("Geospatial Geometry ID: ").append(TEST_ID).append("\n");
		text.append("Geospatial Geometry Type: Point\n");
		text.append("Geospatial Geometry SRS Name: ").append(TEST_SRS_ATTRIBUTES.getSrsName()).append("\n");
		text.append("Geospatial Geometry SRS Dimension: ").append(TEST_SRS_ATTRIBUTES.getSrsDimension()).append("\n");
		text.append("Geospatial Geometry Axis Labels: ").append(TEST_SRS_ATTRIBUTES.getAxisLabelsAsXsList()).append(
			"\n");
		text.append("Geospatial Geometry Unit of Measure Labels: ").append(TEST_SRS_ATTRIBUTES.getUomLabelsAsXsList())
			.append("\n");
		text.append(getPosition().toText());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:Point xmlns:gml=\"").append(DDMSVersion.getCurrentVersion().getGmlNamespace()).append("\" ");
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

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Point.NAME, component.getName());
			assertEquals(Point.GML_PREFIX, component.getPrefix());
			assertEquals(Point.GML_PREFIX + ":" + Point.NAME, component.getQualifiedName());

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
			Element element = Util.buildElement(GML_PREFIX, Point.NAME, DDMSVersion.getCurrentVersion()
				.getGmlNamespace(), null);
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE,
				TEST_SRS_ATTRIBUTES.getSrsName());
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, TEST_ID);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing SRS Name
			Element element = Util.buildDDMSElement(Point.NAME, null);
			SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Empty SRS Name
			element = Util.buildDDMSElement(Point.NAME, null);
			attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Point SRS Name doesn't match pos SRS Name
			element = Util.buildDDMSElement(Point.NAME, null);
			attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES
				.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
			attr.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing ID
			element = Util.buildDDMSElement(Point.NAME, null);
			TEST_SRS_ATTRIBUTES.addTo(element);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Empty ID
			element = Util.buildDDMSElement(Point.NAME, null);
			TEST_SRS_ATTRIBUTES.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), "");
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// ID not NCName
			element = Util.buildDDMSElement(Point.NAME, null);
			TEST_SRS_ATTRIBUTES.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), "1TEST");
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing position
			element = Util.buildDDMSElement(Point.NAME, null);
			TEST_SRS_ATTRIBUTES.addTo(element);
			Util.addAttribute(element, GML_PREFIX, "id", DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_ID);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing SRS Name
			SRSAttributes attr = new SRSAttributes(null, TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Empty SRS Name
			attr = new SRSAttributes("", TEST_SRS_ATTRIBUTES.getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Polygon SRS Name doesn't match pos SRS Name
			attr = new SRSAttributes(DIFFERENT_VALUE, TEST_SRS_ATTRIBUTES.getSrsDimension(), TEST_SRS_ATTRIBUTES
				.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Missing ID
			testConstructor(WILL_FAIL, getPosition(), TEST_SRS_ATTRIBUTES, null);

			// Empty ID
			testConstructor(WILL_FAIL, getPosition(), TEST_SRS_ATTRIBUTES, "");

			// ID not NCName
			testConstructor(WILL_FAIL, getPosition(), TEST_SRS_ATTRIBUTES, "1TEST");

			// Missing position
			testConstructor(WILL_FAIL, null, TEST_SRS_ATTRIBUTES, TEST_ID);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Point component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Point dataComponent = testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, TEST_ID);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SRSAttributes attr = new SRSAttributes(TEST_SRS_ATTRIBUTES.getSrsName(), new Integer(11),
				TEST_SRS_ATTRIBUTES.getAxisLabels(), TEST_SRS_ATTRIBUTES.getUomLabels());
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Point dataComponent = testConstructor(WILL_SUCCEED, getPosition(), attr, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			List<Double> newCoords = new ArrayList<Double>();
			newCoords.add(new Double(56.0));
			newCoords.add(new Double(150.0));
			Position newPosition = new Position(newCoords, TEST_SRS_ATTRIBUTES);

			dataComponent = testConstructor(WILL_SUCCEED, newPosition, TEST_SRS_ATTRIBUTES, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, TEST_ID);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, TEST_ID);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getPosition(), TEST_SRS_ATTRIBUTES, TEST_ID);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testPositionReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Position pos = getPosition();
			testConstructor(WILL_SUCCEED, pos, TEST_SRS_ATTRIBUTES, TEST_ID);
			testConstructor(WILL_SUCCEED, pos, TEST_SRS_ATTRIBUTES, TEST_ID);
		}
	}

	public void testWrongVersionPosition() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Double> coords = new ArrayList<Double>();
		coords.add(new Double(32.1));
		coords.add(new Double(42.1));
		Position pos = new Position(coords, null);
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Point(pos, SRSAttributesTest.getFixture(), TEST_ID);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
}