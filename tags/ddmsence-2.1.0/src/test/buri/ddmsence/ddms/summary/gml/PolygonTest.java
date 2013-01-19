/* Copyright 2010 - 2013 by Brian Uri!
   
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
 * <p> Tests related to gml:Polygon elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.0
 */
public class PolygonTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public PolygonTest() throws InvalidDDMSException {
		super("polygon.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<Polygon> getFixtureList() {
		try {
			List<Polygon> polygons = new ArrayList<Polygon>();
			polygons.add(new Polygon(PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), TEST_ID));
			return (polygons);
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
	private Polygon getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Polygon component = null;
		try {
			component = new Polygon(element);
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
	 * @param positions the positions (required)
	 * @param srsAttributes the srs attributes (required)
	 * @param id the id (required)
	 * @return a valid object
	 */
	private Polygon getInstance(String message, List<Position> positions, SRSAttributes srsAttributes, String id) {
		boolean expectFailure = !Util.isEmpty(message);
		Polygon component = null;
		try {
			component = new Polygon(positions, srsAttributes, id);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
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
		String gmlNamespace = DDMSVersion.getCurrentVersion().getGmlNamespace();
		Element ringElement = Util.buildElement(PropertyReader.getPrefix("gml"), "LinearRing", gmlNamespace, null);
		for (Position pos : positions) {
			ringElement.appendChild(pos.getXOMElementCopy());
		}
		Element extElement = Util.buildElement(PropertyReader.getPrefix("gml"), "exterior", gmlNamespace, null);
		extElement.appendChild(ringElement);
		return (extElement);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "Polygon.id", TEST_ID));
		text.append(SRSAttributesTest.getFixture().getOutput(isHTML, "Polygon."));
		for (Position pos : PositionTest.getFixtureList()) {
			text.append(pos.getOutput(isHTML, "Polygon.", ""));
		}
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
		xml.append("<gml:Polygon ").append(getXmlnsGML()).append(" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\" ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">\n\t");
		xml.append("<gml:exterior>\n\t\t");
		xml.append("<gml:LinearRing>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(PositionTest.TEST_COORDS)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(PositionTest.TEST_COORDS_2)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(PositionTest.TEST_COORDS_3)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(PositionTest.TEST_COORDS)).append("</gml:pos>\n\t\t");
		xml.append("</gml:LinearRing>\n\t");
		xml.append("</gml:exterior>\n");
		xml.append("</gml:Polygon>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_GML_PREFIX, Polygon
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE,
				SRSAttributesTest.getFixture().getSrsName());
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance(SUCCESS, element);

			// First position matches last position but has extra whitespace.
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			List<Position> newPositions = new ArrayList<Position>(PositionTest.getFixtureList());
			newPositions.add(PositionTest.getFixtureList().get(1));
			Element posElement = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace,
				"32.1         40.1");
			SRSAttributesTest.getFixture().addTo(posElement);
			Position positionWhitespace = new Position(posElement);
			newPositions.add(positionWhitespace);
			element.appendChild(wrapPositions(newPositions));
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();
			// Missing SRS Name
			Element element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("srsName is required.", element);

			// Empty SRS Name
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("srsName is required.", element);

			// Polygon SRS Name doesn't match pos SRS Name
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("The srsName of each position", element);

			// Missing ID
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("id is required.", element);

			// Empty ID
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "");
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("id is required.", element);

			// ID not NCName
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "1TEST");
			element.appendChild(wrapPositions(PositionTest.getFixtureList()));
			getInstance("\"1TEST\" is not a valid NCName.", element);

			// Missing Positions
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(new ArrayList<Position>()));
			getInstance("At least 4 positions are required", element);

			// First position doesn't match last position.
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			List<Position> newPositions = new ArrayList<Position>(PositionTest.getFixtureList());
			newPositions.add(PositionTest.getFixtureList().get(1));
			element.appendChild(wrapPositions(newPositions));
			getInstance("The first and last position", element);

			// Not enough positions
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			newPositions = new ArrayList<Position>();
			newPositions.add(PositionTest.getFixtureList().get(0));
			element.appendChild(wrapPositions(newPositions));
			getInstance("At least 4 positions are required", element);

			// Tests on shared attributes are done in the PositionTest.
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing SRS Name
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			getInstance("srsName is required.", PositionTest.getFixtureList(), attr, TEST_ID);

			// Empty SRS Name
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			getInstance("srsName is required.", PositionTest.getFixtureList(), attr, TEST_ID);

			// Polygon SRS Name doesn't match pos SRS Name
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			getInstance("The srsName of each position", PositionTest.getFixtureList(), attr, TEST_ID);

			// Missing ID
			getInstance("id is required.", PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), null);

			// Empty ID
			getInstance("id is required.", PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), "");

			// ID not NCName
			getInstance("\"1TEST\" is not a valid NCName.", PositionTest.getFixtureList(), SRSAttributesTest
				.getFixture(), "1TEST");

			// Missing Positions
			getInstance("At least 4 positions are required", null, SRSAttributesTest.getFixture(), TEST_ID);

			// First position doesn't match last position.
			List<Position> newPositions = new ArrayList<Position>(PositionTest.getFixtureList());
			newPositions.add(PositionTest.getFixtureList().get(1));
			getInstance("The first and last position", newPositions, SRSAttributesTest.getFixture(), TEST_ID);

			// Not enough positions
			newPositions = new ArrayList<Position>();
			newPositions.add(PositionTest.getFixtureList().get(0));
			getInstance("At least 4 positions are required", newPositions, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Polygon component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Polygon elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Polygon dataComponent = getInstance(SUCCESS, PositionTest.getFixtureList(), SRSAttributesTest.getFixture(),
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
			Polygon elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Polygon dataComponent = getInstance(SUCCESS, PositionTest.getFixtureList(), attr, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			List<Position> newPositions = new ArrayList<Position>(PositionTest.getFixtureList());
			newPositions.add(PositionTest.getFixtureList().get(1));
			newPositions.add(PositionTest.getFixtureList().get(0));
			dataComponent = getInstance(SUCCESS, newPositions, SRSAttributesTest.getFixture(), TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, PositionTest.getFixtureList(), SRSAttributesTest.getFixture(),
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Polygon elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Polygon component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Polygon component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, PositionTest.getFixtureList(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testPositionReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Position> positions = PositionTest.getFixtureList();
			getInstance(SUCCESS, positions, SRSAttributesTest.getFixture(), TEST_ID);
			getInstance(SUCCESS, positions, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testGetLocatorSuffix() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Because Positions don't have any ValidationWarnings, no existing code uses this locator method right now.
			Polygon component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals("/gml:exterior/gml:LinearRing", component.getLocatorSuffix());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Polygon component = getInstance(SUCCESS, getValidElement(sVersion));
			Polygon.Builder builder = new Polygon.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Polygon.Builder builder = new Polygon.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setId(TEST_ID);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Polygon.Builder builder = new Polygon.Builder();
			builder.setId(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "srsName is required.");
			}
			builder.getSrsAttributes().setSrsName("http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D");
			builder.getPositions().get(0).getCoordinates().get(0).setValue(Double.valueOf(1));
			builder.getPositions().get(0).getCoordinates().get(1).setValue(Double.valueOf(1));
			builder.getPositions().get(1).getCoordinates().get(0).setValue(Double.valueOf(2));
			builder.getPositions().get(1).getCoordinates().get(1).setValue(Double.valueOf(2));
			builder.getPositions().get(2).getCoordinates().get(0).setValue(Double.valueOf(3));
			builder.getPositions().get(2).getCoordinates().get(1).setValue(Double.valueOf(3));
			builder.getPositions().get(3).getCoordinates().get(0).setValue(Double.valueOf(1));
			builder.getPositions().get(3).getCoordinates().get(1).setValue(Double.valueOf(1));
			builder.commit();

			// Skip empty Positions
			builder = new Polygon.Builder();
			builder.setId(TEST_ID);
			Position.Builder emptyBuilder = new Position.Builder();
			Position.Builder fullBuilder1 = new Position.Builder();
			fullBuilder1.getCoordinates().get(0).setValue(Double.valueOf(0));
			fullBuilder1.getCoordinates().get(1).setValue(Double.valueOf(0));
			Position.Builder fullBuilder2 = new Position.Builder();
			fullBuilder2.getCoordinates().get(0).setValue(Double.valueOf(0));
			fullBuilder2.getCoordinates().get(1).setValue(Double.valueOf(1));
			Position.Builder fullBuilder3 = new Position.Builder();
			fullBuilder3.getCoordinates().get(0).setValue(Double.valueOf(1));
			fullBuilder3.getCoordinates().get(1).setValue(Double.valueOf(1));
			builder.getPositions().add(emptyBuilder);
			builder.getPositions().add(fullBuilder1);
			builder.getPositions().add(fullBuilder2);
			builder.getPositions().add(fullBuilder3);
			builder.getPositions().add(fullBuilder1);
			builder.setSrsAttributes(new SRSAttributes.Builder(SRSAttributesTest.getFixture()));
			assertEquals(4, builder.commit().getPositions().size());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Polygon.Builder builder = new Polygon.Builder();
			assertNotNull(builder.getPositions().get(1));
		}
	}
}
