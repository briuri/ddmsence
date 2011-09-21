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
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to gml:Point elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PointTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public PointTest() throws InvalidDDMSException {
		super("point.xml");
	}

	/**
	 * Returns a test fixture position
	 * 
	 * @return a position
	 * @throws InvalidDDMSException
	 */
	private Position getPosition() throws InvalidDDMSException {
		return (new Position(PositionTest.TEST_COORDS, SRSAttributesTest.getFixture()));
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
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "Point.id", TEST_ID));
		text.append(SRSAttributesTest.getFixture().getOutput(isHTML, "Point."));
		text.append(getPosition().getOutput(isHTML, "Point."));
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
		xml.append("srsName=\"").append(attr.getSrsName()).append("\" ");
		xml.append("srsDimension=\"").append(attr.getSrsDimension()).append("\" ");
		xml.append("axisLabels=\"").append(attr.getAxisLabelsAsXsList()).append("\" ");
		xml.append("uomLabels=\"").append(attr.getUomLabelsAsXsList()).append("\" ");
		xml.append("gml:id=\"").append(TEST_ID).append("\">\n\t");
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
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_GML_PREFIX,
				Point.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Point.getName(version),
				version.getGmlNamespace(), null);
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE,
				SRSAttributesTest.getFixture().getSrsName());
			Util.addAttribute(element, PropertyReader.getPrefix("gml"), "id", version.getGmlNamespace(), TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getPosition(), SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();

			// Missing SRS Name
			Element element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Empty SRS Name
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Point SRS Name doesn't match pos SRS Name
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			attr.addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing ID
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Empty ID
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "");
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// ID not NCName
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, "1TEST");
			element.appendChild(getPosition().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing position
			element = Util.buildElement(gmlPrefix, Point.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			Util.addAttribute(element, gmlPrefix, "id", gmlNamespace, TEST_ID);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing SRS Name
			SRSAttributes attr = new SRSAttributes(null, SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Empty SRS Name
			attr = new SRSAttributes("", SRSAttributesTest.getFixture().getSrsDimension(), null, null);
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Polygon SRS Name doesn't match pos SRS Name
			attr = new SRSAttributes(DIFFERENT_VALUE, SRSAttributesTest.getFixture().getSrsDimension(),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			testConstructor(WILL_FAIL, getPosition(), attr, TEST_ID);

			// Missing ID
			testConstructor(WILL_FAIL, getPosition(), SRSAttributesTest.getFixture(), null);

			// Empty ID
			testConstructor(WILL_FAIL, getPosition(), SRSAttributesTest.getFixture(), "");

			// ID not NCName
			testConstructor(WILL_FAIL, getPosition(), SRSAttributesTest.getFixture(), "1TEST");

			// Missing position
			testConstructor(WILL_FAIL, null, SRSAttributesTest.getFixture(), TEST_ID);
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Point component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Point dataComponent = testConstructor(WILL_SUCCEED, getPosition(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SRSAttributes attr = new SRSAttributes(SRSAttributesTest.getFixture().getSrsName(), new Integer(11),
				SRSAttributesTest.getFixture().getAxisLabels(), SRSAttributesTest.getFixture().getUomLabels());
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Point dataComponent = testConstructor(WILL_SUCCEED, getPosition(), attr, TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			List<Double> newCoords = new ArrayList<Double>();
			newCoords.add(new Double(56.0));
			newCoords.add(new Double(150.0));
			Position newPosition = new Position(newCoords, SRSAttributesTest.getFixture());

			dataComponent = testConstructor(WILL_SUCCEED, newPosition, SRSAttributesTest.getFixture(), TEST_ID);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getPosition(), SRSAttributesTest.getFixture(),
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);			
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getPosition(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getPosition(), SRSAttributesTest.getFixture(), TEST_ID);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testPositionReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position pos = getPosition();
			testConstructor(WILL_SUCCEED, pos, SRSAttributesTest.getFixture(), TEST_ID);
			testConstructor(WILL_SUCCEED, pos, SRSAttributesTest.getFixture(), TEST_ID);
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

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Point component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Point.Builder builder = new Point.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Point.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Point.Builder();
			builder.setId(TEST_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.getPosition().getCoordinates().get(0).setValue(new Double(32.1));
			builder.getPosition().getCoordinates().get(1).setValue(new Double(42.1));
			builder.setId("IDValue");
			builder.getSrsAttributes().setSrsName("http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D");
			builder.commit();
		}
	}
}
