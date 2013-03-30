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
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to DDMS 2.0, 3.0, 3.1 ddms:RelatedResources elements or DDMS 4.0.1 ddms:relatedResource elements
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RelatedResourceTest extends AbstractBaseTestCase {

	private static final String TEST_RELATIONSHIP = "http://purl.org/dc/terms/references";
	private static final String TEST_DIRECTION = "outbound";
	private static final String TEST_QUALIFIER = "http://purl.org/dc/terms/URI";
	private static final String TEST_VALUE = "http://en.wikipedia.org/wiki/Tank";

	/**
	 * Constructor
	 */
	public RelatedResourceTest() {
		super("relatedResources.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static RelatedResource getFixture() {
		try {
			List<Link> links = new ArrayList<Link>();
			links.add(new Link(new XLinkAttributes("http://en.wikipedia.org/wiki/Tank", "role", null, null)));
			return (new RelatedResource(links, "http://purl.org/dc/terms/references", "outbound",
				"http://purl.org/dc/terms/URI", "http://en.wikipedia.org/wiki/Tank", null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private RelatedResource getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		RelatedResource component = null;
		try {
			component = new RelatedResource(element);
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
	 * @param links a list of links
	 * @param relationship the relationship attribute (required)
	 * @param direction the relationship direction (optional)
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private RelatedResource getInstance(String message, List<Link> links, String relationship, String direction,
		String qualifier, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		RelatedResource component = null;
		try {
			component = new RelatedResource(links, relationship, direction, qualifier, value,
				SecurityAttributesTest.getFixture());
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
		String prefix = DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? "relatedResource."
			: "relatedResources.RelatedResource.";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "relationship", TEST_RELATIONSHIP));
		text.append(buildOutput(isHTML, prefix + "direction", TEST_DIRECTION));
		text.append(buildOutput(isHTML, prefix + "qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, prefix + "value", TEST_VALUE));
		text.append(buildOutput(isHTML, prefix + "link.type", "locator"));
		text.append(buildOutput(isHTML, prefix + "link.href", TEST_VALUE));
		text.append(buildOutput(isHTML, prefix + "link.role", "tank"));
		text.append(buildOutput(isHTML, prefix + "link.title", "Tank Page"));
		text.append(buildOutput(isHTML, prefix + "link.label", "tank"));
		text.append(buildOutput(isHTML, prefix + "classification", "U"));
		text.append(buildOutput(isHTML, prefix + "ownerProducer", "USA"));
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
		if (version.isAtLeast("4.0.1")) {
			xml.append("<ddms:relatedResource ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
			xml.append("ddms:relationship=\"http://purl.org/dc/terms/references\" ddms:direction=\"outbound\" ");
			xml.append("ddms:qualifier=\"http://purl.org/dc/terms/URI\" ddms:value=\"http://en.wikipedia.org/wiki/Tank\" ");
			xml.append("ism:classification=\"U\" ism:ownerProducer=\"USA\">\n");
			xml.append("\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ");
			xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" />\n");
			xml.append("</ddms:relatedResource>");
		}
		else {
			xml.append("<ddms:relatedResources ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
			xml.append("ddms:relationship=\"http://purl.org/dc/terms/references\" ddms:direction=\"outbound\" ");
			xml.append("ism:classification=\"U\" ism:ownerProducer=\"USA\">\n");
			xml.append("\t<ddms:RelatedResource ddms:qualifier=\"http://purl.org/dc/terms/URI\" ");
			xml.append("ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n");
			xml.append("\t\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ");
			xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" />\n");
			xml.append("\t</ddms:RelatedResource>\n");
			xml.append("</ddms:relatedResources>\n");
		}
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				RelatedResource.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			Element innerElement = version.isAtLeast("4.0.1") ? element
				: Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, null, TEST_QUALIFIER,
				TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing relationship
			Element element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Element innerElement = version.isAtLeast("4.0.1") ? element
				: Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("relationship attribute is required.", element);

			// Invalid direction
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			Util.addDDMSAttribute(element, "direction", "veeringLeft");
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("The direction attribute must be one of", element);

			// Relationship not URI
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", INVALID_URI);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("Invalid URI", element);

			// Missing qualifier
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("qualifier attribute is required.", element);

			// qualifier not URI
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", INVALID_URI);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("Invalid URI", element);

			// Missing value
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			getInstance("value attribute is required.", element);

			// Missing link
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			getInstance("At least 1 link must exist.", element);

			// Security Attributes
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0.1") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0.1"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			Link link = new Link(XLinkAttributesTest.getLocatorFixture(), SecurityAttributesTest.getFixture());
			innerElement.appendChild(link.getXOMElementCopy());
			getInstance("Security attributes cannot be applied", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing relationship
			getInstance("relationship attribute is required.", LinkTest.getLocatorFixtureList(false), null,
				TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);

			// Invalid direction
			getInstance("The direction attribute must be one of", LinkTest.getLocatorFixtureList(false),
				TEST_RELATIONSHIP, "veeringLeft", TEST_QUALIFIER, TEST_VALUE);

			// Relationship not URI
			getInstance("Invalid URI", LinkTest.getLocatorFixtureList(false), INVALID_URI, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);

			// Missing qualifier
			getInstance("qualifier attribute is required.", LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP,
				TEST_DIRECTION, null, TEST_VALUE);

			// Qualifier not URI
			getInstance("Invalid URI", LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, TEST_DIRECTION,
				INVALID_URI, TEST_VALUE);

			// Missing value
			getInstance("value attribute is required.", LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP,
				TEST_DIRECTION, TEST_QUALIFIER, null);

			// Missing link
			getInstance("At least 1 link must exist.", null, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER,
				TEST_VALUE);

			// Security Attributes
			try {
				Link link = new Link(XLinkAttributesTest.getLocatorFixture(), SecurityAttributesTest.getFixture());
				List<Link> links = new ArrayList<Link>();
				links.add(link);
				new RelatedResource(links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE, null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "Security attributes cannot be applied");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			RelatedResource component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Pre-DDMS 4.0.1, too many relatedResource children
			if (!version.isAtLeast("4.0.1")) {
				Element element = new Element(getValidElement(sVersion));
				Element child = Util.buildDDMSElement("RelatedResource", null);
				child.addAttribute(Util.buildDDMSAttribute("qualifier", "ignoreMe"));
				child.addAttribute(Util.buildDDMSAttribute("value", "ignoreMe"));
				child.appendChild(new Element(LinkTest.getFixtureElement()));
				element.appendChild(child);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:RelatedResources element contains more than 1";
				String locator = "ddms:relatedResources";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			RelatedResource elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RelatedResource dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false),
				TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			RelatedResource elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RelatedResource dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false),
				DIFFERENT_VALUE, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, "inbound",
				TEST_QUALIFIER, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP,
				TEST_DIRECTION, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP,
				TEST_DIRECTION, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			List<Link> differentLinks = new ArrayList<Link>();
			differentLinks.add(new Link(XLinkAttributesTest.getLocatorFixture()));
			differentLinks.add(new Link(XLinkAttributesTest.getLocatorFixture()));
			dataComponent = getInstance(SUCCESS, differentLinks, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER,
				TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			RelatedResource component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			RelatedResource component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(false), TEST_RELATIONSHIP, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(false), component.toXML());

		}
	}

	public void testLinkReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Link> links = LinkTest.getLocatorFixtureList(false);
			getInstance(SUCCESS, links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
			getInstance(SUCCESS, links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RelatedResource component = getInstance(SUCCESS, getValidElement(sVersion));
			RelatedResource.Builder builder = new RelatedResource.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RelatedResource.Builder builder = new RelatedResource.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setQualifier(TEST_QUALIFIER);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RelatedResource.Builder builder = new RelatedResource.Builder();
			builder.setQualifier(TEST_QUALIFIER);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "relationship attribute is required.");
			}
			builder.setRelationship(TEST_RELATIONSHIP);
			builder.setQualifier(TEST_QUALIFIER);
			builder.setValue(TEST_VALUE);
			builder.getLinks().get(0).getXLinkAttributes().setType("locator");
			builder.getLinks().get(0).getXLinkAttributes().setHref("http://ddmsence.urizone.net/");
			builder.getLinks().get(0).getXLinkAttributes().setRole("role");
			builder.commit();

			// Skip empty Links
			builder = new RelatedResource.Builder();
			builder.setDirection(TEST_DIRECTION);
			builder.setRelationship(TEST_RELATIONSHIP);
			builder.setQualifier(TEST_QUALIFIER);
			builder.setValue(TEST_VALUE);
			Link.Builder emptyBuilder = new Link.Builder();
			Link.Builder fullBuilder = new Link.Builder();
			fullBuilder.getXLinkAttributes().setType("locator");
			fullBuilder.getXLinkAttributes().setHref("http://ddmsence.urizone.net/");
			fullBuilder.getXLinkAttributes().setRole("role");
			builder.getLinks().add(emptyBuilder);
			builder.getLinks().add(fullBuilder);
			assertEquals(1, builder.commit().getLinks().size());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			RelatedResource.Builder builder = new RelatedResource.Builder();
			assertNotNull(builder.getLinks().get(1));
		}
	}
}
