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
package buri.ddmsence.ddms.summary;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.gml.PointTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:geospatialCoverage elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class GeospatialCoverageTest extends AbstractBaseTestCase {

	private static final String TEST_PRECEDENCE = "Primary";
	private static final Integer TEST_ORDER = Integer.valueOf(1);

	/**
	 * Constructor
	 */
	public GeospatialCoverageTest() throws InvalidDDMSException {
		super("geospatialCoverage.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @param a fixed order value
	 */
	public static GeospatialCoverage getFixture(int order) {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			return (new GeospatialCoverage(null, null, null, PostalAddressTest.getFixture(), null, null,
				version.isAtLeast("4.0.1") ? Integer.valueOf(order) : null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static GeospatialCoverage getFixture() {
		try {
			return (new GeospatialCoverage(null, null, new BoundingGeometry(null, PointTest.getFixtureList()), null,
				null, null, null, null));
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
	private GeospatialCoverage getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		GeospatialCoverage component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture().addTo(element);
			component = new GeospatialCoverage(element);
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
	 * @param geographicIdentifier an identifier (0-1 optional)
	 * @param boundingBox a bounding box (0-1 optional)
	 * @param boundingGeometry a set of bounding geometry (0-1 optional)
	 * @param postalAddress an address (0-1 optional)
	 * @param verticalExtent an extent (0-1 optional)
	 * @param precedence the precedence value (optional, DDMS 4.0.1)
	 * @param order the order value (optional, DDMS 4.0.1)
	 * @return a valid object
	 */
	private GeospatialCoverage getInstance(String message, GeographicIdentifier geographicIdentifier,
		BoundingBox boundingBox, BoundingGeometry boundingGeometry, PostalAddress postalAddress,
		VerticalExtent verticalExtent, String precedence, Integer order) {
		boolean expectFailure = !Util.isEmpty(message);
		GeospatialCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture();
			component = new GeospatialCoverage(geographicIdentifier, boundingBox, boundingGeometry, postalAddress,
				verticalExtent, precedence, order, attr);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns the ISM attributes HTML output, if the DDMS Version supports it.
	 */
	private String getHtmlIcism() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0.1") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
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
		String prefix = version.isAtLeast("4.0.1") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
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
		String prefix = version.isAtLeast("4.0.1") ? "geospatialCoverage." : "geospatialCoverage.GeospatialExtent.";
		StringBuffer text = new StringBuffer();
		text.append(GeographicIdentifierTest.getCountryCodeBasedFixture().getOutput(isHTML, prefix, ""));
		if (version.isAtLeast("4.0.1")) {
			text.append(buildOutput(isHTML, prefix + "precedence", "Primary"));
			text.append(buildOutput(isHTML, prefix + "order", "1"));
		}
		if (version.isAtLeast("3.0"))
			text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, prefix));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:geospatialCoverage ").append(getXmlnsDDMS());
		if (version.isAtLeast("3.0")) {
			xml.append(" ").append(getXmlnsISM()).append(" ");
			if (version.isAtLeast("4.0.1"))
				xml.append("ddms:precedence=\"Primary\" ddms:order=\"1\" ");
			xml.append("ism:classification=\"U\" ism:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t");
		if (version.isAtLeast("5.0")) {
			xml.append("<ddms:geographicIdentifier>\n\t\t");
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getTestQualifierName()).append(
				"=\"urn:us:gov:dod:nga:def:geo-political:GENC:3:ed1\" ddms:").append(CountryCodeTest.getTestValueName()).append(
				"=\"USA\" />\n\t");
			xml.append("</ddms:geographicIdentifier>\n");
		}
		else if (version.isAtLeast("4.0.1")) {
			xml.append("<ddms:geographicIdentifier>\n\t\t");
			xml.append("<ddms:countryCode ddms:").append(CountryCodeTest.getTestQualifierName()).append(
				"=\"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1\" ddms:").append(
				CountryCodeTest.getTestValueName()).append("=\"LAO\" />\n\t");
			xml.append("</ddms:geographicIdentifier>\n");
		}
		else {
			xml.append("<ddms:GeospatialExtent>\n\t\t<ddms:geographicIdentifier>\n\t\t\t");
			xml.append("<ddms:countryCode ddms:qualifier=\"urn:us:gov:ic:cvenum:irm:coverage:iso3166:trigraph:v1\" ddms:value=\"LAO\" />\n\t\t");
			xml.append("</ddms:geographicIdentifier>\n\t</ddms:GeospatialExtent>\n");
		}
		xml.append("</ddms:geospatialCoverage>");
		return (xml.toString());
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
		Element extElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("GeospatialExtent", null);
		for (IDDMSComponent component : components) {
			if (component != null)
				extElement.appendChild(component.getXOMElementCopy());
		}
		if (!version.isAtLeast("4.0.1"))
			element.appendChild(extElement);
		return (element);
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				GeospatialCoverage.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// geographicIdentifier
			getInstance(SUCCESS, getValidElement(sVersion));

			if (!version.isAtLeast("5.0")) {
				// boundingBox
				Element element = buildComponentElement(BoundingBoxTest.getFixture());
				getInstance(SUCCESS, element);

				// verticalExtent
				element = buildComponentElement(VerticalExtentTest.getFixture());
				getInstance(SUCCESS, element);
			}

			// boundingGeometry
			Element element = buildComponentElement(BoundingGeometryTest.getFixture());
			getInstance(SUCCESS, element);

			// postalAddress
			element = buildComponentElement(PostalAddressTest.getFixture());
			getInstance(SUCCESS, element);

			// everything
			List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
			list.add(BoundingBoxTest.getFixture());
			list.add(BoundingGeometryTest.getFixture());
			list.add(PostalAddressTest.getFixture());
			list.add(VerticalExtentTest.getFixture());
			element = buildComponentElement(list);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// geographicIdentifier
			getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null,
				null);

			// geographicIdentifier with DDMS 4.0.1 attributes
			if (version.isAtLeast("4.0.1")) {
				getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null,
					TEST_PRECEDENCE, TEST_ORDER);
			}

			if (!version.isAtLeast("5.0")) {
				// boundingBox
				getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), null, null, null, null, null);

				// verticalExtent
				getInstance(SUCCESS, null, null, null, null, VerticalExtentTest.getFixture(), null, null);
			}

			// boundingGeometry
			getInstance(SUCCESS, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);

			// postalAddress
			getInstance(SUCCESS, null, null, null, PostalAddressTest.getFixture(), null, null, null);

			// everything
			getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), BoundingGeometryTest.getFixture(),
				PostalAddressTest.getFixture(), VerticalExtentTest.getFixture(), null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent
			// must be used.
			Element element = buildComponentElement((IDDMSComponent) null);
			getInstance("At least 1 of ", element);

			// Too many geographicIdentifier
			List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
			list.add(GeographicIdentifierTest.getCountryCodeBasedFixture());
			list.add(GeographicIdentifierTest.getCountryCodeBasedFixture());
			element = buildComponentElement(list);
			getInstance("No more than 1 geographicIdentifier", element);

			if (!version.isAtLeast("5.0")) {
				// Too many boundingBox
				list = new ArrayList<IDDMSComponent>();
				list.add(BoundingBoxTest.getFixture());
				list.add(BoundingBoxTest.getFixture());
				element = buildComponentElement(list);
				getInstance("No more than 1 boundingBox", element);

				// Too many verticalExtent
				list = new ArrayList<IDDMSComponent>();
				list.add(VerticalExtentTest.getFixture());
				list.add(VerticalExtentTest.getFixture());
				element = buildComponentElement(list);
				getInstance("No more than 1 verticalExtent", element);
			}

			// Too many boundingGeometry
			list = new ArrayList<IDDMSComponent>();
			list.add(BoundingGeometryTest.getFixture());
			list.add(BoundingGeometryTest.getFixture());
			element = buildComponentElement(list);
			getInstance("No more than 1 boundingGeometry", element);

			// Too many postalAddress
			list = new ArrayList<IDDMSComponent>();
			list.add(PostalAddressTest.getFixture());
			list.add(PostalAddressTest.getFixture());
			element = buildComponentElement(list);
			getInstance("No more than 1 postalAddress", element);

			// If facilityIdentifier is used, nothing else can.
			list = new ArrayList<IDDMSComponent>();
			list.add(GeographicIdentifierTest.getFacIdBasedFixture());
			list.add(BoundingGeometryTest.getFixture());
			element = buildComponentElement(list);
			getInstance("A geographicIdentifier containing a facilityIdentifier", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent
			// must be used.
			getInstance("At least 1 of ", null, null, null, null, null, null, null);

			// If facilityIdentifier is used, nothing else can.
			getInstance("A geographicIdentifier containing a facilityIdentifier",
				GeographicIdentifierTest.getFacIdBasedFixture(), null, BoundingGeometryTest.getFixture(), null, null,
				null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			int expectedWarningCount = (version.isAtLeast("5.0") ? 1 : 0);
			GeospatialCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(expectedWarningCount, component.getValidationWarnings().size());
			if (version.isAtLeast("5.0")) {
				String text = "The ddms:countryCode is syntactically correct";
				String locator = "ddms:geospatialCoverage/ddms:geographicIdentifier";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String precedence = version.isAtLeast("4.0.1") ? TEST_PRECEDENCE : null;
			Integer order = version.isAtLeast("4.0.1") ? TEST_ORDER : null;

			GeospatialCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			GeospatialCoverage dataComponent = getInstance(SUCCESS,
				GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, precedence, order);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			if (!version.isAtLeast("5.0")) {
				// boundingBox
				Element element = buildComponentElement(BoundingBoxTest.getFixture());
				elementComponent = getInstance(SUCCESS, element);
				dataComponent = getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
				assertEquals(elementComponent, dataComponent);
				assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

				// verticalExtent
				element = buildComponentElement(VerticalExtentTest.getFixture());
				elementComponent = getInstance(SUCCESS, element);
				dataComponent = getInstance(SUCCESS, null, null, null, null, VerticalExtentTest.getFixture(), null,
					null);
				assertEquals(elementComponent, dataComponent);
				assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
			}

			// boundingGeometry
			Element element = buildComponentElement(BoundingGeometryTest.getFixture());
			elementComponent = getInstance(SUCCESS, element);
			dataComponent = getInstance(SUCCESS, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// postalAddress
			element = buildComponentElement(PostalAddressTest.getFixture());
			elementComponent = getInstance(SUCCESS, element);
			dataComponent = getInstance(SUCCESS, null, null, null, PostalAddressTest.getFixture(), null, null, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			GeospatialCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			GeospatialCoverage dataComponent = null;
			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null,
					null, null, TEST_PRECEDENCE, null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null,
					null, null, null, TEST_ORDER);
				assertFalse(elementComponent.equals(dataComponent));
			}
			if (!version.isAtLeast("5.0")) {
				dataComponent = getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, null, null, null, null, VerticalExtentTest.getFixture(), null,
					null);
				assertFalse(elementComponent.equals(dataComponent));
			}

			dataComponent = getInstance(SUCCESS, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, null, null, null, PostalAddressTest.getFixture(), null, null, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String prefix = "geospatialCoverage.";
			if (!version.isAtLeast("4.0.1"))
				prefix += "GeospatialExtent.";
			String precedence = version.isAtLeast("4.0.1") ? TEST_PRECEDENCE : null;
			Integer order = version.isAtLeast("4.0.1") ? TEST_ORDER : null;

			GeospatialCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null,
				null, precedence, order);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			if (!version.isAtLeast("5.0")) {
				component = getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
				assertEquals(BoundingBoxTest.getFixture().getOutput(true, prefix, "") + getHtmlIcism(),
					component.toHTML());
				assertEquals(BoundingBoxTest.getFixture().getOutput(false, prefix, "") + getTextIcism(),
					component.toText());

				component = getInstance(SUCCESS, null, null, null, null, VerticalExtentTest.getFixture(), null, null);
				assertEquals(VerticalExtentTest.getFixture().getOutput(true, prefix, "") + getHtmlIcism(),
					component.toHTML());
				assertEquals(VerticalExtentTest.getFixture().getOutput(false, prefix, "") + getTextIcism(),
					component.toText());
			}

			component = getInstance(SUCCESS, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);
			assertEquals(BoundingGeometryTest.getFixture().getOutput(true, prefix, "") + getHtmlIcism(),
				component.toHTML());
			assertEquals(BoundingGeometryTest.getFixture().getOutput(false, prefix, "") + getTextIcism(),
				component.toText());

			component = getInstance(SUCCESS, null, null, null, PostalAddressTest.getFixture(), null, null, null);
			assertEquals(PostalAddressTest.getFixture().getOutput(true, prefix, "") + getHtmlIcism(),
				component.toHTML());
			assertEquals(PostalAddressTest.getFixture().getOutput(false, prefix, "") + getTextIcism(),
				component.toText());
		}
	}

	public void testGeographicIdentifierReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			GeographicIdentifier geoId = GeographicIdentifierTest.getCountryCodeBasedFixture();
			getInstance(SUCCESS, geoId, null, null, null, null, null, null);
			getInstance(SUCCESS, geoId, null, null, null, null, null, null);
		}
	}

	public void testBoundingBoxReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("5.0")) {
				BoundingBox box = BoundingBoxTest.getFixture();
				getInstance(SUCCESS, null, box, null, null, null, null, null);
				getInstance(SUCCESS, null, box, null, null, null, null, null);
			}
		}
	}

	public void testBoundingGeometryReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingGeometry geo = BoundingGeometryTest.getFixture();
			getInstance(SUCCESS, null, null, geo, null, null, null, null);
			getInstance(SUCCESS, null, null, geo, null, null, null, null);
		}
	}

	public void testPostalAddressReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PostalAddress address = PostalAddressTest.getFixture();
			getInstance(SUCCESS, null, null, null, address, null, null, null);
			getInstance(SUCCESS, null, null, null, address, null, null, null);
		}
	}

	public void testVerticalExtentReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("5.0")) {
				VerticalExtent extent = VerticalExtentTest.getFixture();
				getInstance(SUCCESS, null, null, null, null, extent, null, null);
				getInstance(SUCCESS, null, null, null, null, extent, null, null);
			}
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
			GeospatialCoverage component = new GeospatialCoverage(
				GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null, null, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null,
				null, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testWrongVersionPrecedenceOrder() {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null,
				TEST_PRECEDENCE, null, null);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The ddms:precedence attribute cannot be used");
		}
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null, null,
				TEST_ORDER, null);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The ddms:order attribute cannot be used");
		}
	}

	public void testPrecedenceRestrictions() {
		DDMSVersion.setCurrentVersion("4.0.1");
		try {
			new GeospatialCoverage(GeographicIdentifierTest.getCountryCodeBasedFixture(), null, null, null, null,
				"Tertiary", null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The ddms:precedence attribute must have a value from");
		}
		try {
			new GeospatialCoverage(null, BoundingBoxTest.getFixture(), null, null, null, TEST_PRECEDENCE, null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The ddms:precedence attribute should only be applied");
		}
	}

	public void testGetLocatorSuffix() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			GeospatialCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			String suffix = version.isAtLeast("4.0.1") ? "" : "/ddms:GeospatialExtent";
			assertEquals(suffix, component.getLocatorSuffix());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			GeospatialCoverage component = getInstance(SUCCESS, GeographicIdentifierTest.getCountryCodeBasedFixture(),
				null, null, null, null, null, null);
			GeospatialCoverage.Builder builder = new GeospatialCoverage.Builder(component);
			assertEquals(component, builder.commit());

			if (!version.isAtLeast("5.0")) {
				component = getInstance(SUCCESS, null, BoundingBoxTest.getFixture(), null, null, null, null, null);
				builder = new GeospatialCoverage.Builder(component);
				assertEquals(component, builder.commit());

				component = getInstance(SUCCESS, null, null, null, null, VerticalExtentTest.getFixture(), null, null);
				builder = new GeospatialCoverage.Builder(component);
				assertEquals(component, builder.commit());
			}

			component = getInstance(SUCCESS, null, null, BoundingGeometryTest.getFixture(), null, null, null, null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(component, builder.commit());

			component = getInstance(SUCCESS, null, null, null, PostalAddressTest.getFixture(), null, null, null);
			builder = new GeospatialCoverage.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			GeospatialCoverage.Builder builder = new GeospatialCoverage.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setOrder(TEST_ORDER);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			GeospatialCoverage.Builder builder = new GeospatialCoverage.Builder();
			builder.getVerticalExtent().setDatum("AGL");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "A ddms:verticalExtent requires ");
			}
			builder.setVerticalExtent(null);
			builder.getGeographicIdentifier().getFacilityIdentifier().setBeNumber("beNumber");
			builder.getGeographicIdentifier().getFacilityIdentifier().setOsuffix("osuffix");
			builder.commit();
		}
	}
}
