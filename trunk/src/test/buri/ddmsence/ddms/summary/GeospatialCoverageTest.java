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
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
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

	/**
	 * Constructor
	 */
	public GeospatialCoverageTest() throws InvalidDDMSException {
		super("geospatialCoverage.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private GeospatialCoverage testConstructor(boolean expectFailure, Element element) {
		GeospatialCoverage component = null;
		try {
			if (!DDMSVersion.isCurrentVersion("2.0"))
				SecurityAttributesTest.getFixture(false).addTo(element);
			component = new GeospatialCoverage(element);
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
	 * @param geographicIdentifier an identifier (0-1 optional)
	 * @param boundingBox a bounding box (0-1 optional)
	 * @param boundingGeometry a set of bounding geometry (0-1 optional)
	 * @param postalAddress an address (0-1 optional)
	 * @param verticalExtent an extent (0-1 optional)
	 * @return a valid object
	 */
	private GeospatialCoverage testConstructor(boolean expectFailure, GeographicIdentifier geographicIdentifier,
		BoundingBox boundingBox, BoundingGeometry boundingGeometry, PostalAddress postalAddress,
		VerticalExtent verticalExtent) {
		GeospatialCoverage component = null;
		try {
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0")) ? null 
				: SecurityAttributesTest.getFixture(false);
			component = new GeospatialCoverage(geographicIdentifier, boundingBox, boundingGeometry, postalAddress,
				verticalExtent, attr);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the ICISM attributes HTML output, if the DDMS Version supports it.
	 */
	private String getHtmlIcism() {
		if (!DDMSVersion.isCurrentVersion("2.0"))
			return ("<meta name=\"geospatial.classification\" content=\"U\" />\n"
			+ "<meta name=\"geospatial.ownerProducer\" content=\"USA\" />\n");
		return ("");
	}

	/**
	 * Returns the ICISM attributes Text output, if the DDMS Version supports it.
	 */
	private String getTextIcism() {
		if (!DDMSVersion.isCurrentVersion("2.0"))
			return ("Geospatial Classification: U\nGeospatial ownerProducer: USA\n");
		return ("");
	}

	
	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() throws InvalidDDMSException {
		StringBuffer html = new StringBuffer();
		html.append(GeographicIdentifierTest.getFixture().toHTML());
		if (!DDMSVersion.isCurrentVersion("2.0"))
			html.append(getHtmlIcism());
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(GeographicIdentifierTest.getFixture().toText());
		if (!DDMSVersion.isCurrentVersion("2.0"))
			text.append(getTextIcism());
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geospatialCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\"");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			xml.append(" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIcismNamespace())
				.append("\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t");
		xml.append("<ddms:GeospatialExtent>\n\t\t<ddms:geographicIdentifier>\n\t\t\t");
		xml.append("<ddms:facilityIdentifier ddms:beNumber=\"1234DD56789\" ddms:osuffix=\"DD123\" />\n\t\t");
		xml.append("</ddms:geographicIdentifier>\n\t</ddms:GeospatialExtent>\n</ddms:geospatialCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	/**
	 * Helper method to create a XOM element that can be used to test element constructors
	 * 
	 * @param component the child of the GeospatialExtent
	 * @return Element
	 */
	private Element buildComponentElement(IDDMSComponent component) {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		if (component != null)
			list.add(component);
		return (buildComponentElement(list));
	}
	
	/**
	 * Helper method to create a XOM element that can be used to test element constructors
	 * 
	 * @param components the children of the GeospatialExtent
	 * @return Element
	 */
	private Element buildComponentElement(List<IDDMSComponent> components) {
		Element extElement = Util.buildDDMSElement("GeospatialExtent", null);
		for (IDDMSComponent component : components) {
			if (component != null)
				extElement.appendChild(component.getXOMElementCopy());
		}
		Element element = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		element.appendChild(extElement);
		return (element);
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(GeospatialCoverage.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + GeospatialCoverage.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}
	
	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// geographicIdentifier
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// boundingBox
			Element element = buildComponentElement(BoundingBoxTest.getFixture());
			testConstructor(WILL_SUCCEED, element);

			// boundingGeometry
			element = buildComponentElement(BoundingGeometryTest.getFixture());
			testConstructor(WILL_SUCCEED, element);

			// postalAddress
			element = buildComponentElement(PostalAddressTest.getFixture());
			testConstructor(WILL_SUCCEED, element);

			// verticalExtent
			element = buildComponentElement(VerticalExtentTest.getFixture());
			testConstructor(WILL_SUCCEED, element);

			// everything
			List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
			list.add(BoundingBoxTest.getFixture());
			list.add(BoundingGeometryTest.getFixture());
			list.add(PostalAddressTest.getFixture());
			list.add(VerticalExtentTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// geographicIdentifier
			testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getFixture(), null, null, null, null);

			// boundingBox
			testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null);

			// boundingGeometry
			testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null);

			// postalAddress
			testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null);

			// verticalExtent
			testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture());

			// everything
			testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), BoundingGeometryTest.getFixture(),
				PostalAddressTest.getFixture(), VerticalExtentTest.getFixture());
		}
	}


	
	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent 
			// must be used.
			Element element = buildComponentElement((IDDMSComponent) null);
			testConstructor(WILL_FAIL, element);

			// Too many geographicIdentifier
			List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
			list.add(GeographicIdentifierTest.getFixture());
			list.add(GeographicIdentifierTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);

			// Too many boundingBox
			list = new ArrayList<IDDMSComponent>();
			list.add(BoundingBoxTest.getFixture());
			list.add(BoundingBoxTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);

			// Too many boundingGeometry
			list = new ArrayList<IDDMSComponent>();
			list.add(BoundingGeometryTest.getFixture());
			list.add(BoundingGeometryTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);

			// Too many postalAddress
			list = new ArrayList<IDDMSComponent>();
			list.add(PostalAddressTest.getFixture());
			list.add(PostalAddressTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);

			// Too many verticalExtent
			list = new ArrayList<IDDMSComponent>();
			list.add(VerticalExtentTest.getFixture());
			list.add(VerticalExtentTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);

			// If facilityIdentifier is used, nothing else can.
			list = new ArrayList<IDDMSComponent>();
			list.add(GeographicIdentifierTest.getFixture());
			list.add(VerticalExtentTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent 
			// must be used.
			testConstructor(WILL_FAIL, null, null, null, null, null);

			// If facilityIdentifier is used, nothing else can.
			testConstructor(WILL_FAIL, GeographicIdentifierTest.getFixture(), BoundingBoxTest.getFixture(), null, null,
				null);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			GeospatialCoverage dataComponent = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getFixture(),
				null, null, null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// boundingBox
			Element element = buildComponentElement(BoundingBoxTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// boundingGeometry
			element = buildComponentElement(BoundingGeometryTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// postalAddress
			element = buildComponentElement(PostalAddressTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// verticalExtent
			element = buildComponentElement(VerticalExtentTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			GeospatialCoverage dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null,
				null, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getFixture(), null, null, null, null);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null);
			assertEquals(BoundingBoxTest.getFixture().toHTML() + getHtmlIcism(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null);
			assertEquals(BoundingGeometryTest.getFixture().toHTML() + getHtmlIcism(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null);
			assertEquals(PostalAddressTest.getFixture().toHTML() + getHtmlIcism(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture());
			assertEquals(VerticalExtentTest.getFixture().toHTML() + getHtmlIcism(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getFixture(), null, null, null, null);
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null);
			assertEquals(BoundingBoxTest.getFixture().toText() + getTextIcism(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null);
			assertEquals(BoundingGeometryTest.getFixture().toText() + getTextIcism(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null);
			assertEquals(PostalAddressTest.getFixture().toText() + getTextIcism(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture());
			assertEquals(VerticalExtentTest.getFixture().toText() + getTextIcism(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getFixture(), null, null, null, null);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testGeographicIdentifierReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			GeographicIdentifier geoId = GeographicIdentifierTest.getFixture();
			testConstructor(WILL_SUCCEED, geoId, null, null, null, null);
			testConstructor(WILL_SUCCEED, geoId, null, null, null, null);
		}
	}

	public void testBoundingBoxReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingBox box = BoundingBoxTest.getFixture();
			testConstructor(WILL_SUCCEED, null, box, null, null, null);
			testConstructor(WILL_SUCCEED, null, box, null, null, null);
		}
	}

	public void testBoundingGeometryReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			BoundingGeometry geo = BoundingGeometryTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, geo, null, null);
			testConstructor(WILL_SUCCEED, null, null, geo, null, null);
		}
	}

	public void testPostalAddressReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			PostalAddress address = PostalAddressTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, address, null);
			testConstructor(WILL_SUCCEED, null, null, null, address, null);
		}
	}

	public void testVerticalExtentReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VerticalExtent extent = VerticalExtentTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, null, extent);
			testConstructor(WILL_SUCCEED, null, null, null, null, extent);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			GeospatialCoverage component = new GeospatialCoverage(GeographicIdentifierTest.getFixture(), null, null,
				null, null, attr);
			if (DDMSVersion.isCurrentVersion("2.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}
	
	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getFixture(), null, null, null, null,
				SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		BoundingBox box = BoundingBoxTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, box, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		BoundingGeometry geo = BoundingGeometryTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, geo, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		GeographicIdentifier geoId = GeographicIdentifierTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(geoId, null, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		PostalAddress address = PostalAddressTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, null, address, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		VerticalExtent extent = VerticalExtentTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, null, null, extent, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
}
