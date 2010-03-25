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
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:geospatialCoverage elements</p>
 * 
 * <p>Assumes that unit testing on individual components is done separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class GeospatialCoverageTest extends AbstractComponentTestCase {
	
	private final GeographicIdentifier TEST_GEO_ID;
	private final BoundingBox TEST_BOX;
	private final BoundingGeometry TEST_GEOMETRY;
	private final PostalAddress TEST_ADDRESS;
	private final VerticalExtent TEST_EXTENT;
	private final SRSAttributes TEST_SRS_ATTRIBUTES;
	
	/**
	 * Constructor
	 */
	public GeospatialCoverageTest() throws InvalidDDMSException {
		super("geospatialCoverage.xml");
		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
		TEST_GEO_ID = new GeographicIdentifier(new FacilityIdentifier("1234DD56789", "DD123"));
		TEST_BOX = new BoundingBox(1.1, 2.2, 3.3, 4.4);
		
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(new Position(PositionTest.TEST_COORDS, TEST_SRS_ATTRIBUTES), TEST_SRS_ATTRIBUTES, TEST_ID));
		TEST_GEOMETRY = new BoundingGeometry(null, points);
		TEST_ADDRESS = new PostalAddress(null, null, "VA", null, null, true);
		TEST_EXTENT = new VerticalExtent(1.1, 2.2, "Meter", "HAE");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private GeospatialCoverage testConstructor(boolean expectFailure, Element element) {
		GeospatialCoverage component = null;
		try {
			component = new GeospatialCoverage(element);
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
	 * @param geographicIdentifier an identifier (0-1 optional)
	 * @param boundingBox a bounding box (0-1 optional)
	 * @param boundingGeometry a set of bounding geometry (0-1 optional)
	 * @param postalAddress an address (0-1 optional)
	 * @param verticalExtent an extent (0-1 optional)
	 * @return a valid object
	 */
	private GeospatialCoverage testConstructor(boolean expectFailure, GeographicIdentifier geographicIdentifier, BoundingBox boundingBox, BoundingGeometry boundingGeometry,
			PostalAddress postalAddress, VerticalExtent verticalExtent) {
		GeospatialCoverage component = null;
		try {
			component = new GeospatialCoverage(geographicIdentifier, boundingBox, boundingGeometry, postalAddress, verticalExtent, null);
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
		html.append(TEST_GEO_ID.toHTML());
		return (html.toString());
	}
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(TEST_GEO_ID.toText());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geospatialCoverage xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\">\n\t");
		xml.append("<ddms:GeospatialExtent>\n\t\t<ddms:geographicIdentifier>\n\t\t\t");
		xml.append("<ddms:facilityIdentifier ddms:beNumber=\"1234DD56789\" ddms:osuffix=\"DD123\" />\n\t\t");
		xml.append("</ddms:geographicIdentifier>\n\t</ddms:GeospatialExtent>\n</ddms:geospatialCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testName() {
		GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(GeospatialCoverage.NAME, component.getName());
	}

	public void testElementConstructorValid() {
		// geographicIdentifier
		testConstructor(WILL_SUCCEED, getValidElement());

		// boundingBox
		Element extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_BOX.getXOMElementCopy());
		Element element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_SUCCEED, element);
		
		// boundingGeometry
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_SUCCEED, element);
		
		// postalAddress
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_ADDRESS.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_SUCCEED, element);
		
		// verticalExtent
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_SUCCEED, element);
		
		// everything
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_BOX.getXOMElementCopy());
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		extElement.appendChild(TEST_ADDRESS.getXOMElementCopy());
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testDataConstructorValid() {
		// geographicIdentifier
		testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);

		// boundingBox
		testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		
		// boundingGeometry
		testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		
		// postalAddress
		testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		
		// verticalExtent
		testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		
		// everything
		testConstructor(WILL_SUCCEED, null, TEST_BOX, TEST_GEOMETRY, TEST_ADDRESS, TEST_EXTENT);
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent must be used.
		Element extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		Element element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// Too many geographicIdentifier
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEO_ID.getXOMElementCopy());
		extElement.appendChild(TEST_GEO_ID.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
		
		// Too many boundingBox
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_BOX.getXOMElementCopy());
		extElement.appendChild(TEST_BOX.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
		
		// Too many boundingGeometry
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
		
		// Too many postalAddress
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_ADDRESS.getXOMElementCopy());
		extElement.appendChild(TEST_ADDRESS.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
		
		// Too many verticalExtent
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
		
		// If facilityIdentifier is used, nothing else can.
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEO_ID.getXOMElementCopy());
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		testConstructor(WILL_FAIL, element);
	}

	public void testDataConstructorInvalid() {		
		// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent must be used.
		testConstructor(WILL_FAIL, null, null, null, null, null);
		
		// If facilityIdentifier is used, nothing else can.
		testConstructor(WILL_FAIL, TEST_GEO_ID, TEST_BOX, null, null, null);
	}
	
	public void testWarnings() {
		// No warnings
		GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		GeospatialCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
		// boundingBox
		Element extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_BOX.getXOMElementCopy());
		Element element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		elementComponent = testConstructor(WILL_SUCCEED, element);
		dataComponent = testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
		// boundingGeometry
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_GEOMETRY.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		elementComponent = testConstructor(WILL_SUCCEED, element);
		dataComponent = testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
		// postalAddress
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_ADDRESS.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		elementComponent = testConstructor(WILL_SUCCEED, element);
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		
		// verticalExtent
		extElement = Util.buildDDMSElement("GeospatialExtent", null);
		extElement.appendChild(TEST_EXTENT.getXOMElementCopy());
		element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		elementComponent = testConstructor(WILL_SUCCEED, element);
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		GeospatialCoverage dataComponent = testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		assertFalse(elementComponent.equals(dataComponent));

		dataComponent = testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		assertEquals(TEST_BOX.toHTML(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		assertEquals(TEST_GEOMETRY.toHTML(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		assertEquals(TEST_ADDRESS.toHTML(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		assertEquals(TEST_EXTENT.toHTML(), component.toHTML());
	}	
	
	public void testTextOutput() {
		GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		assertEquals(TEST_BOX.toText(), component.toText());

		component = testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		assertEquals(TEST_GEOMETRY.toText(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		assertEquals(TEST_ADDRESS.toText(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		assertEquals(TEST_EXTENT.toText(), component.toText());
	}
	
	public void testXMLOutput() {
		GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}	

	public void testGeographicIdentifierReuse() {
		testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
		testConstructor(WILL_SUCCEED, TEST_GEO_ID, null, null, null, null);
	}
	
	public void testBoundingBoxReuse() {
		testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
		testConstructor(WILL_SUCCEED, null, TEST_BOX, null, null, null);
	}
	
	public void testBoundingGeometryReuse() {
		testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
		testConstructor(WILL_SUCCEED, null, null, TEST_GEOMETRY, null, null);
	}
	
	public void testPostalAddressReuse() {
		testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
		testConstructor(WILL_SUCCEED, null, null, null, TEST_ADDRESS, null);
	}
	
	public void testVerticalExtentReuse() {
		testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
		testConstructor(WILL_SUCCEED, null, null, null, null, TEST_EXTENT);
	}
	
	public void testSecurityAttributes() throws InvalidDDMSException {
		GeospatialCoverage component = new GeospatialCoverage(TEST_GEO_ID, null, null, null, null, SecurityAttributesTest.getFixture(false));
		assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());		
	}
}
