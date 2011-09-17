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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.UnsupportedVersionException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
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

	/**
	 * Constructor
	 */
	public PolygonTest() throws InvalidDDMSException {
		super("polygon.xml");
	}

	/**
	 * Returns a test fixture of positions
	 * 
	 * @return List of positions
	 * @throws InvalidDDMSException
	 */
	private List<Position> getPositions() throws InvalidDDMSException {
		List<Position> positions = new ArrayList<Position>();
		positions.add(new Position(TEST_COORDS_1, SRSAttributesTest.getFixture()));
		positions.add(new Position(TEST_COORDS_2, SRSAttributesTest.getFixture()));
		positions.add(new Position(TEST_COORDS_3, SRSAttributesTest.getFixture()));
		positions.add(new Position(TEST_COORDS_1, SRSAttributesTest.getFixture()));
		return (positions);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Polygon testConstructor(boolean expectFailure, Element element) {
		Polygon component = null;
		try {
			component = new Polygon(element);
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
	 * @param positions the positions (required)
	 * @param srsAttributes the srs attributes (required)
	 * @param id the id (required)
	 * @return a valid object
	 */
	private Polygon testConstructor(boolean expectFailure, List<Position> positions, SRSAttributes srsAttributes,
		String id) {
		Polygon component = null;
		try {
			component = new Polygon(positions, srsAttributes, id);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
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
		String gmlNamespace = DDMSVersion.getCurrentVersion().getGmlNamespace();
		Element ringElement = Util.buildElement(PropertyReader.getPrefix("gml"), "LinearRing", gmlNamespace,
			null);
		for (Position pos : positions) {
			ringElement.appendChild(pos.getXOMElementCopy());
		}
		Element extElement = Util
			.buildElement(PropertyReader.getPrefix("gml"), "exterior", gmlNamespace, null);
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
		for (Position pos : getPositions()) {
			text.append(pos.getOutput(isHTML, "Polygon."));
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
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\">\n\t");
		xml.append("<gml:exterior>\n\t\t");
		xml.append("<gml:LinearRing>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_1)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_2)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_3)).append("</gml:pos>\n\t\t\t");
		xml.append("<gml:pos ");
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\">");
		xml.append(Util.getXsList(TEST_COORDS_1)).append("</gml:pos>\n\t\t");
		xml.append("</gml:LinearRing>\n\t");
		xml.append("</gml:exterior>\n");
		xml.append("</gml:Polygon>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Polygon.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("gml"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("gml") + ":" + Polygon.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildElement("gml", "wrongName", version.getGmlNamespace(), null);
			testConstructor(WILL_FAIL, element);
			try {
				element = Util.buildElement("gml", Polygon.getName(version), "http://wrongNs/", null);
				new Polygon(element);
				fail("Allowed invalid data.");
			} catch (UnsupportedVersionException e) {
				// Good
			}
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE,
				SRSAttributesTest.getFixture().getSrsName());
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_SUCCEED, element);

			// First position matches last position but has extra whitespace.
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			List<Position> newPositions = new ArrayList<Position>(getPositions());
			newPositions.add(getPositions().get(1));
			Element posElement = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace,
				"32.1         40.1");
			SRSAttributesTest.getFixture().addTo(posElement);
			Position positionWhitespace = new Position(posElement);
			newPositions.add(positionWhitespace);
			element.appendChild(wrapPositions(newPositions));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();
			// Missing SRS Name
			Element element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// Empty SRS Name
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// Polygon SRS Name doesn't match pos SRS Name
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// Missing ID
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// Empty ID
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "");
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// ID not NCName
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "1TEST");
			element.appendChild(wrapPositions(getPositions()));
			testConstructor(WILL_FAIL, element);

			// Missing Positions
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(wrapPositions(new ArrayList<Position>()));
			testConstructor(WILL_FAIL, element);

			// First position doesn't match last position.
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			List<Position> newPositions = new ArrayList<Position>(getPositions());
			newPositions.add(getPositions().get(1));
			element.appendChild(wrapPositions(newPositions));
			testConstructor(WILL_FAIL, element);

			// Not enough positions
			element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			newPositions = new ArrayList<Position>();
			newPositions.add(getPositions().get(0));
			element.appendChild(wrapPositions(newPositions));
			testConstructor(WILL_FAIL, element);

			// Tests on shared attributes are done in the PositionTest.		
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing SRS Name
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPositions(), attr, TEST_ID);

			// Empty SRS Name
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPositions(), attr, TEST_ID);

			// Polygon SRS Name doesn't match pos SRS Name
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			testConstructor(WILL_FAIL, getPositions(), attr, TEST_ID);

			// Missing ID
			testConstructor(WILL_FAIL, getPositions(), SRSAttributesTest.getFixture(), null);

			// Empty ID
			testConstructor(WILL_FAIL, getPositions(), SRSAttributesTest.getFixture(), "");

			// ID not NCName
			testConstructor(WILL_FAIL, getPositions(), SRSAttributesTest.getFixture(), "1TEST");

			// Missing Positions
			testConstructor(WILL_FAIL, null, SRSAttributesTest.getFixture(), TEST_ID);

			// First position doesn't match last position.
			List<Position> newPositions = new ArrayList<Position>(getPositions());
			newPositions.add(getPositions().get(1));
			testConstructor(WILL_FAIL, newPositions, SRSAttributesTest.getFixture(), TEST_ID);

			// Not enough positions
			newPositions = new ArrayList<Position>();
			newPositions.add(getPositions().get(0));
			testConstructor(WILL_FAIL, newPositions, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Polygon dataComponent = testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(),
				TEST_ID);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SRSAttributes attr = new SRSAttributes(SRSAttributesTest.getFixture().getSrsName(), new Integer(11),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Polygon dataComponent = testConstructor(WILL_SUCCEED, getPositions(), attr, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			List<Position> newPositions = new ArrayList<Position>(getPositions());
			newPositions.add(getPositions().get(1));
			newPositions.add(getPositions().get(0));
			dataComponent = testConstructor(WILL_SUCCEED, newPositions, SRSAttributesTest.getFixture(), TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(),
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getPositions(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testPositionReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<Position> positions = getPositions();
			testConstructor(WILL_SUCCEED, positions, SRSAttributesTest.getFixture(), TEST_ID);
			testConstructor(WILL_SUCCEED, positions, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testGetLocatorSuffix() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Because Positions don't have any ValidationWarnings, no existing code uses this locator method right now.
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals("/gml:exterior/gml:LinearRing", component.getLocatorSuffix());
		}
	}

	public void testWrongVersionPosition() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Position> positions = getPositions();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Polygon(positions, SRSAttributesTest.getFixture(), TEST_ID);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Polygon.Builder builder = new Polygon.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Polygon.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Polygon.Builder();
			builder.setId(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

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
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Polygon.Builder builder = new Polygon.Builder();
			assertNotNull(builder.getPositions().get(1));
		}
	}
}
