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
	private final SRSAttributes TEST_SRS_ATTRIBUTES;
	
	/**
	 * Constructor
	 */
	public PositionTest() throws InvalidDDMSException {
		super("3.0/position.xml");
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Position testConstructor(boolean expectFailure, Element element) {
		Position component = null;
		try {
			component = new Position(element);
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
	 * @param coordinates the coordinates
	 * @param srsAttributes	the srs attributes (optional)
	 * @return a valid object
	 */
	private Position testConstructor(boolean expectFailure, List<Double> coordinates, SRSAttributes srsAttributes) {
		Position component = null;
		try {
			component = new Position(coordinates, srsAttributes);
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
		html.append("<meta name=\"geospatial.boundingGeometry.position\" content=\"").append(TEST_XS_LIST).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.position.srsName\" content=\"").append(SRSAttributesTest.TEST_SRS_NAME).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.position.srsDimension\" content=\"").append(SRSAttributesTest.TEST_SRS_DIMENSION).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.position.axisLabels\" content=\"").append(Util.getXsList(SRSAttributesTest.TEST_AXIS_LABELS)).append("\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.position.uomLabels\" content=\"").append(Util.getXsList(SRSAttributesTest.TEST_UOM_LABELS)).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Geospatial Geometry Position: ").append(TEST_XS_LIST).append("\n");
		text.append("Geospatial Geometry Position SRS Name: ").append(SRSAttributesTest.TEST_SRS_NAME).append("\n");
		text.append("Geospatial Geometry Position SRS Dimension: ").append(SRSAttributesTest.TEST_SRS_DIMENSION).append("\n");
		text.append("Geospatial Geometry Position Axis Labels: ").append(Util.getXsList(SRSAttributesTest.TEST_AXIS_LABELS)).append("\n");
		text.append("Geospatial Geometry Position Unit of Measure Labels: ").append(Util.getXsList(SRSAttributesTest.TEST_UOM_LABELS)).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<gml:pos xmlns:gml=\"").append(DDMSVersion.getCurrentVersion().getGmlNamespace()).append("\" ");
		xml.append("srsName=\"").append(SRSAttributesTest.TEST_SRS_NAME).append("\" ");
		xml.append("srsDimension=\"").append(SRSAttributesTest.TEST_SRS_DIMENSION).append("\" ");
		xml.append("axisLabels=\"").append(Util.getXsList(SRSAttributesTest.TEST_AXIS_LABELS)).append("\" ");
		xml.append("uomLabels=\"").append(Util.getXsList(SRSAttributesTest.TEST_UOM_LABELS)).append("\">");
		xml.append(TEST_XS_LIST).append("</gml:pos>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testNameAndNamespace() {
		Position component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Position.NAME, component.getName());
		assertEquals(Position.GML_PREFIX, component.getPrefix());
		assertEquals(Position.GML_PREFIX + ":" + Position.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildElement(GML_PREFIX, Position.NAME, DDMSVersion.getCurrentVersion().getGmlNamespace(), TEST_XS_LIST);		
		testConstructor(WILL_SUCCEED, element);		
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_COORDS, TEST_SRS_ATTRIBUTES);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, TEST_COORDS, null);
	}

	public void testElementConstructorInvalid() {
		// Missing coordinates
		Element element = Util.buildDDMSElement(Position.NAME, null);
		TEST_SRS_ATTRIBUTES.addTo(element);
		testConstructor(WILL_FAIL, element);
				
		// At least 2 coordinates
		element = Util.buildDDMSElement(Position.NAME, "25.0");
		TEST_SRS_ATTRIBUTES.addTo(element);
		testConstructor(WILL_FAIL, element);
		
		// No more than 3 coordinates
		element = Util.buildDDMSElement(Position.NAME, TEST_XS_LIST + " 25.0 35.0");
		TEST_SRS_ATTRIBUTES.addTo(element);
		testConstructor(WILL_FAIL, element);
		
		// Each coordinate is a Double
		element = Util.buildDDMSElement(Position.NAME, "25.0 Dog");
		TEST_SRS_ATTRIBUTES.addTo(element);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testDataConstructorInvalid() {
		// Missing coordinates
		testConstructor(WILL_FAIL, null, TEST_SRS_ATTRIBUTES);
		
		// At least 2 coordinates
		List<Double> newCoords = new ArrayList<Double>();
		newCoords.add(new Double(12.3));
		testConstructor(WILL_FAIL, newCoords, TEST_SRS_ATTRIBUTES);
		
		// No more than 3 coordinates
		newCoords = new ArrayList<Double>();
		newCoords.add(new Double(12.3));
		newCoords.add(new Double(12.3));
		newCoords.add(new Double(12.3));
		newCoords.add(new Double(12.3));
		testConstructor(WILL_FAIL, newCoords, TEST_SRS_ATTRIBUTES);
	}
		
	public void testWarnings() {
		// No warnings
		Position component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Position dataComponent = testConstructor(WILL_SUCCEED, TEST_COORDS, TEST_SRS_ATTRIBUTES);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Position dataComponent = testConstructor(WILL_SUCCEED, TEST_COORDS, null);
		assertFalse(elementComponent.equals(dataComponent));

		List<Double> newCoords = new ArrayList<Double>(TEST_COORDS);
		newCoords.add(new Double(100.0));
		dataComponent = testConstructor(WILL_SUCCEED, newCoords, TEST_SRS_ATTRIBUTES);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Position elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Position component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_COORDS, TEST_SRS_ATTRIBUTES);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Position component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_COORDS, TEST_SRS_ATTRIBUTES);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Position component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_COORDS, TEST_SRS_ATTRIBUTES);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}	
}
