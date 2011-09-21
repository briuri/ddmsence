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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:geospatialCoverage elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class GeospatialCoverageTest extends AbstractComponentTestCase {

	private static final String TEST_PRECEDENCE = "Primary";
	private static final Integer TEST_ORDER = new Integer(1);

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
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
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
	 * @param precedence the precedence value (optional, DDMS 4.0)
	 * @param order the order value (optional, DDMS 4.0)
	 * @return a valid object
	 */
	private GeospatialCoverage testConstructor(boolean expectFailure, GeographicIdentifier geographicIdentifier,
		BoundingBox boundingBox, BoundingGeometry boundingGeometry, PostalAddress postalAddress,
		VerticalExtent verticalExtent, String precedence, Integer order) {
		GeospatialCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture(false);
			component = new GeospatialCoverage(geographicIdentifier, boundingBox, boundingGeometry, postalAddress,
				verticalExtent, precedence, order, attr);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the ISM attributes HTML output, if the DDMS Version supports it.
	 */
	private String getHtmlIcism() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			return (buildOutput(true, prefix + "classification", "U") + buildOutput(true, prefix + "ownerProducer",
				"USA"));
		return ("");
	}

	/**
	 * Returns the ISM attributes Text output, if the DDMS Version supports it.
	 */
	private String getTextIcism() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			return (buildOutput(false, prefix + "classification", "U") + buildOutput(false, prefix + "ownerProducer",
				"USA"));
		return ("");
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
		StringBuffer text = new StringBuffer();
		text.append(GeographicIdentifierTest.getCountryCodeBasedFixture().getOutput(isHTML, prefix));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, prefix + "precedence", "Primary"));
			text.append(buildOutput(isHTML, prefix + "order", "1"));
		}
		if (version.isAtLeast("3.0"))
			text.append(SecurityAttributesTest.getFixture(false).getOutput(isHTML, prefix));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geospatialCoverage ").append(getXmlnsDDMS());
		if (version.isAtLeast("3.0")) {
			xml.append(" ").append(getXmlnsISM()).append(" ");
			if (version.isAtLeast("4.0"))
				xml.append("ddms:precedence=\"Primary\" ddms:order=\"1\" ");
			xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t");
		if (version.isAtLeast("4.0")) {
			xml.append("<ddms:geographicIdentifier>\n\t\t");
			xml.append("<ddms:countryCode ddms:qualifier=\"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1\" ddms:value=\"LAO\" />\n\t");
			xml.append("</ddms:geographicIdentifier>\n");
		} else {
			xml.append("<ddms:GeospatialExtent>\n\t\t<ddms:geographicIdentifier>\n\t\t\t");
			xml.append("<ddms:countryCode ddms:qualifier=\"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1\" ddms:value=\"LAO\" />\n\t\t");
			xml.append("</ddms:geographicIdentifier>\n\t</ddms:GeospatialExtent>\n");
		}
		xml.append("</ddms:geospatialCoverage>");
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(GeospatialCoverage.getName(DDMSVersion.getCurrentVersion()), null);
		Element extElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("GeospatialExtent", null);
		for (IDDMSComponent component : components) {
			if (component != null)
				extElement.appendChild(component.getXOMElementCopy());
		}
		if (!version.isAtLeast("4.0"))
			element.appendChild(extElement);
		return (element);
	}

	public void testNameAndNamespace() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				GeospatialCoverage.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// geographicIdentifier
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

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
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// geographicIdentifier
			testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null,
				null, null, null);

			// geographicIdentifier with DDMS 4.0 attributes
			if (version.isAtLeast("4.0")) {
				testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null,
					null, TEST_PRECEDENCE, TEST_ORDER);
			}

			// boundingBox
			testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null, null);

			// boundingGeometry
			testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);

			// postalAddress
			testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null, null, null);

			// verticalExtent
			testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture(), null, null);

			// everything
			testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), BoundingGeometryTest.getFixture(),
				PostalAddressTest.getFixture(), VerticalExtentTest.getFixture(), null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent 
			// must be used.
			Element element = buildComponentElement((IDDMSComponent) null);
			testConstructor(WILL_FAIL, element);

			// Too many geographicIdentifier
			List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
			list.add(GeographicIdentifierTest.getCountryCodeBasedFixture());
			list.add(GeographicIdentifierTest.getCountryCodeBasedFixture());
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
			list.add(GeographicIdentifierTest.getFacIdBasedFixture());
			list.add(VerticalExtentTest.getFixture());
			element = buildComponentElement(list);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent 
			// must be used.
			testConstructor(WILL_FAIL, null, null, null, null, null, null, null);

			// If facilityIdentifier is used, nothing else can.
			testConstructor(WILL_FAIL, GeographicIdentifierTest.getFacIdBasedFixture(), BoundingBoxTest.getFixture(),
				null, null, null, null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String precedence = version.isAtLeast("4.0") ? TEST_PRECEDENCE : null;
			Integer order = version.isAtLeast("4.0") ? TEST_ORDER : null;

			GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			GeospatialCoverage dataComponent = testConstructor(WILL_SUCCEED,
				GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, precedence, order);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// boundingBox
			Element element = buildComponentElement(BoundingBoxTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null,
				null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// boundingGeometry
			element = buildComponentElement(BoundingGeometryTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null,
				null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// postalAddress
			element = buildComponentElement(PostalAddressTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null, null,
				null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// verticalExtent
			element = buildComponentElement(VerticalExtentTest.getFixture());
			elementComponent = testConstructor(WILL_SUCCEED, element);
			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture(),
				null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			GeospatialCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			GeospatialCoverage dataComponent = null;
			if (version.isAtLeast("4.0")) {
				dataComponent = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(),
					null, null, null, null, TEST_PRECEDENCE, null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(),
					null, null, null, null, null, TEST_ORDER);
				assertFalse(elementComponent.equals(dataComponent));
			}
			dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null,
				null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null,
				null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null,
				null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null,
				null, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null, null,
				null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture(),
				null, null);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String prefix = "geospatialCoverage.";
			if (!version.isAtLeast("4.0"))
				prefix += "GeospatialExtent.";
			String precedence = version.isAtLeast("4.0") ? TEST_PRECEDENCE : null;
			Integer order = version.isAtLeast("4.0") ? TEST_ORDER : null;

			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(), null,
				null, null, null, precedence, order);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
			assertEquals(BoundingBoxTest.getFixture().getOutput(true, prefix) + getHtmlIcism(), component.toHTML());
			assertEquals(BoundingBoxTest.getFixture().getOutput(false, prefix) + getTextIcism(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null, null,
				null);
			assertEquals(BoundingGeometryTest.getFixture().getOutput(true, prefix) + getHtmlIcism(), component.toHTML());
			assertEquals(BoundingGeometryTest.getFixture().getOutput(false, prefix) + getTextIcism(),
				component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null, null,
				null);
			assertEquals(PostalAddressTest.getFixture().getOutput(true, prefix) + getHtmlIcism(), component.toHTML());
			assertEquals(PostalAddressTest.getFixture().getOutput(false, prefix) + getTextIcism(), component.toText());

			component = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture(), null,
				null);
			assertEquals(VerticalExtentTest.getFixture().getOutput(true, prefix) + getHtmlIcism(), component.toHTML());
			assertEquals(VerticalExtentTest.getFixture().getOutput(false, prefix) + getTextIcism(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String precedence = version.isAtLeast("4.0") ? TEST_PRECEDENCE : null;
			Integer order = version.isAtLeast("4.0") ? TEST_ORDER : null;

			GeospatialCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifierTest.getCountryCodeBasedFixture(), null,
				null, null, null, precedence, order);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testGeographicIdentifierReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			GeographicIdentifier geoId = GeographicIdentifierTest.getCountryCodeBasedFixture();
			testConstructor(WILL_SUCCEED, geoId, null, null, null, null, null, null);
			testConstructor(WILL_SUCCEED, geoId, null, null, null, null, null, null);
		}
	}

	public void testBoundingBoxReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox box = BoundingBoxTest.getFixture();
			testConstructor(WILL_SUCCEED, null, box, null, null, null, null, null);
			testConstructor(WILL_SUCCEED, null, box, null, null, null, null, null);
		}
	}

	public void testBoundingGeometryReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry geo = BoundingGeometryTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, geo, null, null, null, null);
			testConstructor(WILL_SUCCEED, null, null, geo, null, null, null, null);
		}
	}

	public void testPostalAddressReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PostalAddress address = PostalAddressTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, address, null, null, null);
			testConstructor(WILL_SUCCEED, null, null, null, address, null, null, null);
		}
	}

	public void testVerticalExtentReuse() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VerticalExtent extent = VerticalExtentTest.getFixture();
			testConstructor(WILL_SUCCEED, null, null, null, null, extent, null, null);
			testConstructor(WILL_SUCCEED, null, null, null, null, extent, null, null);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture(false));
			GeospatialCoverage component = new GeospatialCoverage(
				GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null, null, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null,
				null, SecurityAttributesTest.getFixture(false));
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
			new GeospatialCoverage(null, box, null, null, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		BoundingGeometry geo = BoundingGeometryTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, geo, null, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		GeographicIdentifier geoId = GeographicIdentifierTest.getCountryCodeBasedFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(geoId, null, null, null, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		PostalAddress address = PostalAddressTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, null, address, null, null, null,
				SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		VerticalExtent extent = VerticalExtentTest.getFixture();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new GeospatialCoverage(null, null, null, null, extent, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testWrongVersionPrecedenceOrder() {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null,
				TEST_PRECEDENCE, null, null);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null,
				TEST_ORDER, null);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testPrecedenceRestrictions() {
		DDMSVersion.setCurrentVersion("4.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null,
				"Tertiary", null, null);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		try {
			new GeospatialCoverage(null, BoundingBoxTest.getFixture(), null, null, null, TEST_PRECEDENCE, null, null);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Equality after Building
			GeospatialCoverage component = testConstructor(WILL_SUCCEED,
				GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null, null);
			GeospatialCoverage.Builder builder = new GeospatialCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			component = testConstructor(WILL_SUCCEED, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			component = testConstructor(WILL_SUCCEED, null, null, BoundingGeometryTest.getFixture(), null, null, null,
				null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			component = testConstructor(WILL_SUCCEED, null, null, null, PostalAddressTest.getFixture(), null, null,
				null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			component = testConstructor(WILL_SUCCEED, null, null, null, null, VerticalExtentTest.getFixture(), null,
				null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new GeospatialCoverage.Builder();
			assertNull(builder.commit());
			// Validation
			builder = new GeospatialCoverage.Builder();
			builder.getVerticalExtent().setDatum("datum");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
