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
 * <p>Tests related to gml:pos elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PositionTest extends AbstractComponentTestCase {

	public static final List<Double> TEST_COORDS = new ArrayList<Double>();
	static {
		TEST_COORDS.add(new Double(32.1));
		TEST_COORDS.add(new Double(40.1));
	}
	protected static final String TEST_XS_LIST = "32.1 40.1";

	/**
	 * Constructor
	 */
	public PositionTest() throws InvalidDDMSException {
		super("position.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Position testConstructor(boolean expectFailure, Element element) {
		Position component = null;
		try {
			component = new Position(element);
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
	 * @param coordinates the coordinates
	 * @param srsAttributes the srs attributes (optional)
	 * @return a valid object
	 */
	private Position testConstructor(boolean expectFailure, List<Double> coordinates, SRSAttributes srsAttributes) {
		Position component = null;
		try {
			component = new Position(coordinates, srsAttributes);
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
		text.append(buildOutput(isHTML, "pos", TEST_XS_LIST));
		text.append(SRSAttributesTest.getFixture().getOutput(isHTML, "pos."));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:pos ").append(getXmlnsGML()).append(" ");
		xml.append("srsName=\"").append(SRSAttributesTest.TEST_SRS_NAME).append("\" ");
		xml.append("srsDimension=\"").append(SRSAttributesTest.TEST_SRS_DIMENSION).append("\" ");
		xml.append("axisLabels=\"").append(Util.getXsList(SRSAttributesTest.TEST_AXIS_LABELS)).append("\" ");
		xml.append("uomLabels=\"").append(Util.getXsList(SRSAttributesTest.TEST_UOM_LABELS)).append("\">");
		xml.append(TEST_XS_LIST).append("</gml:pos>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_GML_PREFIX,
				Position.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, TEST_XS_LIST);
			testConstructor(WILL_SUCCEED, element);

			// Empty coordinate
			element = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, "25.0    26.0");
			testConstructor(WILL_SUCCEED, element);

		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_COORDS, SRSAttributesTest.getFixture());

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_COORDS, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();
			// Missing coordinates
			Element element = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, null);
			SRSAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// At least 2 coordinates
			element = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, "25.0");
			SRSAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// No more than 3 coordinates
			element = Util
				.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, TEST_XS_LIST + " 25.0 35.0");
			SRSAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Each coordinate is a Double
			element = Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace, "25.0 Dog");
			SRSAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing coordinates
			testConstructor(WILL_FAIL, null, SRSAttributesTest.getFixture());

			// At least 2 coordinates
			List<Double> newCoords = new ArrayList<Double>();
			newCoords.add(new Double(12.3));
			testConstructor(WILL_FAIL, newCoords, SRSAttributesTest.getFixture());

			// No more than 3 coordinates
			newCoords = new ArrayList<Double>();
			newCoords.add(new Double(12.3));
			newCoords.add(new Double(12.3));
			newCoords.add(new Double(12.3));
			newCoords.add(new Double(12.3));
			testConstructor(WILL_FAIL, newCoords, SRSAttributesTest.getFixture());
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Position component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Position dataComponent = testConstructor(WILL_SUCCEED, TEST_COORDS, SRSAttributesTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testEqualityWhitespace() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();
			Position position = new Position(Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace,
				TEST_XS_LIST));
			Position positionEqual = new Position(Util.buildElement(gmlPrefix, Position.getName(version), gmlNamespace,
				TEST_XS_LIST));
			Position positionEqualWhitespace = new Position(Util.buildElement(gmlPrefix, Position.getName(version),
				gmlNamespace, TEST_XS_LIST + "   "));
			Position positionUnequal2d = new Position(Util.buildElement(gmlPrefix, Position.getName(version),
				gmlNamespace, "32.1 40.0"));
			Position positionUnequal3d = new Position(Util.buildElement(gmlPrefix, Position.getName(version),
				gmlNamespace, TEST_XS_LIST + " 40.0"));
			assertEquals(position, positionEqual);
			assertEquals(position, positionEqualWhitespace);
			assertFalse(position.equals(positionUnequal2d));
			assertFalse(position.equals(positionUnequal3d));
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Position dataComponent = testConstructor(WILL_SUCCEED, TEST_COORDS, null);
			assertFalse(elementComponent.equals(dataComponent));

			List<Double> newCoords = new ArrayList<Double>(TEST_COORDS);
			newCoords.add(new Double(100.0));
			dataComponent = testConstructor(WILL_SUCCEED, newCoords, SRSAttributesTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}
	
	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_COORDS, SRSAttributesTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_COORDS, SRSAttributesTest.getFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Position.Builder builder = new Position.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Position.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Position.Builder();
			builder.getCoordinates().get(0).setValue(Double.valueOf(0));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Skip empty Coordinates
			builder = new Position.Builder();
			builder.setSrsAttributes(new SRSAttributes.Builder(SRSAttributesTest.getFixture()));
			builder.getCoordinates().get(0).setValue(null);
			builder.getCoordinates().get(1).setValue(Double.valueOf(0));
			builder.getCoordinates().get(2).setValue(Double.valueOf(1));
			assertEquals(2, builder.commit().getCoordinates().size());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Position.Builder builder = new Position.Builder();
			assertNotNull(builder.getCoordinates().get(1));
		}
	}

	public void testGetStringAsDouble() {
		assertNull(Position.getStringAsDouble(null));
		assertEquals(new Double(2.1), Position.getStringAsDouble("2.1"));
		assertNull(Position.getStringAsDouble("notADouble"));
	}
}