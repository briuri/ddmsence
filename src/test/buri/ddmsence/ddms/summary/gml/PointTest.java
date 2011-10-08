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
package buri.ddmsence.ddms.summary.gml;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to gml:Point elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PointTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public PointTest() throws InvalidDDMSException {
		super("point.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<Point> getFixtureList() {
		try {
			List<Point> points = new ArrayList<Point>();
			points
				.add(new Point(new Position(PositionTest.TEST_COORDS, null), SRSAttributesTest.getFixture(), TEST_ID));
			return (points);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Point getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Point component = null;
		try {
			component = new Point(element);
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
	 * @param position the position (required)
	 * @param srsAttributes the srs attributes (required)
	 * @param id the id (required)
	 * @return a valid object
	 */
	private Point getInstance(String message, Position position, SRSAttributes srsAttributes, String id) {
		boolean expectFailure = !Util.isEmpty(message);
		Point component = null;
		try {
			component = new Point(position, srsAttributes, id);
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
		text.append(buildOutput(isHTML, "Point.id", TEST_ID));
		text.append(SRSAttributesTest.getFixture().getOutput(isHTML, "Point."));
		text.append(PositionTest.getFixture().getOutput(isHTML, "Point.", ""));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) throws InvalidDDMSException {
		SRSAttributes attr = SRSAttributesTest.getFixture();
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:Point ").append(getXmlnsGML()).append(" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\" ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">\n\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(PositionTest.TEST_XS_LIST).append("</gml:pos>\n");
		xml.append("</gml:Point>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_GML_PREFIX, Point
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Point.getName(version), version
				.getGmlNamespace(), null);
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE,
				SRSAttributesTest.getFixture().getSrsName());
			Util.addAttribute(element, PropertyReader.getPrefix("gml"), "id", version.getGmlNamespace(), TEST_ID);
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, PositionTest.getFixture(), SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();

			// Missing SRS Name
			Element element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("srsName is required.", element);

			// Empty SRS Name
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("srsName is required.", element);

			// Point SRS Name doesn't match pos SRS Name
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("The srsName of the position must match", element);

			// Missing ID
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("id is required.", element);

			// Empty ID
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "");
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("id is required.", element);

			// ID not NCName
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "1TEST");
			element.appendChild(PositionTest.getFixture().getXOMElementCopy());
			getInstance("\"1TEST\" is not a valid NCName.", element);

			// Missing position
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			getInstance("position is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing SRS Name
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			getInstance("srsName is required.", PositionTest.getFixture(), attr, TEST_ID);

			// Empty SRS Name
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			getInstance("srsName is required.", PositionTest.getFixture(), attr, TEST_ID);

			// Polygon SRS Name doesn't match pos SRS Name
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			getInstance("The srsName of the position must match", PositionTest.getFixture(), attr, TEST_ID);

			// Missing ID
			getInstance("id is required.", PositionTest.getFixture(), SRSAttributesTest.getFixture(), null);

			// Empty ID
			getInstance("id is required.", PositionTest.getFixture(), SRSAttributesTest.getFixture(), "");

			// ID not NCName
			getInstance("\"1TEST\" is not a valid NCName.", PositionTest.getFixture(), SRSAttributesTest.getFixture(),
				"1TEST");

			// Missing position
			getInstance("position is required.", null, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Point component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Point dataComponent = getInstance(SUCCESS, PositionTest.getFixture(), SRSAttributesTest.getFixture(),
				TEST_ID);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SRSAttributes attr = new SRSAttributes(SRSAttributesTest.getFixture().getSrsName(), Integer.valueOf(11),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			Point elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Point dataComponent = getInstance(SUCCESS, PositionTest.getFixture(), attr, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			List<Double> newCoords = new ArrayList<Double>();
			newCoords.add(new Double(56.0));
			newCoords.add(new Double(150.0));
			Position newPosition = new Position(newCoords, SRSAttributesTest.getFixture());

			dataComponent = getInstance(SUCCESS, newPosition, SRSAttributesTest.getFixture(), TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, PositionTest.getFixture(), SRSAttributesTest.getFixture(),
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, PositionTest.getFixture(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, PositionTest.getFixture(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testPositionReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position pos = PositionTest.getFixture();
			getInstance(SUCCESS, pos, SRSAttributesTest.getFixture(), TEST_ID);
			getInstance(SUCCESS, pos, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Point component = getInstance(SUCCESS, getValidElement(sVersion));
			Point.Builder builder = new Point.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Point.Builder builder = new Point.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setId(TEST_ID);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Point.Builder builder = new Point.Builder();
			builder.setId(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "srsName is required.");
			}
			builder.getPosition().getCoordinates().get(0).setValue(new Double(32.1));
			builder.getPosition().getCoordinates().get(1).setValue(new Double(42.1));
			builder.setId("IDValue");
			builder.getSrsAttributes().setSrsName("http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D");
			builder.commit();
		}
	}
}
