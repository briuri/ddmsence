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
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to DDMS 2.0, 3.0, 3.1 ddms:RelatedResources elements or DDMS 4.0 ddms:relatedResource elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RelatedResourceTest extends AbstractComponentTestCase {

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
	 * Returns a list of links for testing
	 */
	private static List<Link> getLinks() throws InvalidDDMSException {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(new XLinkAttributes("locator", TEST_VALUE, "role", null, null)));
		return (links);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private RelatedResource testConstructor(boolean expectFailure, Element element) {
		RelatedResource component = null;
		try {
			component = new RelatedResource(element);
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
	 * @param links a list of links
	 * @param relationship the relationship attribute (required)
	 * @param direction the relationship direction (optional)
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private RelatedResource testConstructor(boolean expectFailure, List<Link> links, String relationship,
		String direction, String qualifier, String value) {
		RelatedResource component = null;
		try {
			component = new RelatedResource(links, relationship, direction, qualifier, value,
				SecurityAttributesTest.getFixture(false));
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
		String prefix = DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? "relatedResource."
			: "relatedResources.RelatedResource.";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "relationship", TEST_RELATIONSHIP));
		text.append(buildOutput(isHTML, prefix + "direction", TEST_DIRECTION));
		text.append(buildOutput(isHTML, prefix + "qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, prefix + "value", TEST_VALUE));
		text.append(buildOutput(isHTML, prefix + "link.type", "locator"));
		text.append(buildOutput(isHTML, prefix + "link.href", TEST_VALUE));
		text.append(buildOutput(isHTML, prefix + "link.role", "role"));
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
		if (version.isAtLeast("4.0")) {
			xml.append("<ddms:relatedResource ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
			xml.append("ddms:relationship=\"http://purl.org/dc/terms/references\" ddms:direction=\"outbound\" ");
			xml.append("ddms:qualifier=\"http://purl.org/dc/terms/URI\" ddms:value=\"http://en.wikipedia.org/wiki/Tank\" ");
			xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ");
			xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"role\" />\n");
			xml.append("</ddms:relatedResource>");
		} else {
			xml.append("<ddms:relatedResources ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
			xml.append("ddms:relationship=\"http://purl.org/dc/terms/references\" ddms:direction=\"outbound\" ");
			xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t<ddms:RelatedResource ddms:qualifier=\"http://purl.org/dc/terms/URI\" ");
			xml.append("ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n");
			xml.append("\t\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ");
			xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"role\" />\n");
			xml.append("\t</ddms:RelatedResource>\n");
			xml.append("</ddms:relatedResources>\n");
		}
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(RelatedResource.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + RelatedResource.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			Element innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// All fields
			testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, null, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			// Missing relationship
			Element element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Element innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Invalid direction
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			Util.addDDMSAttribute(element, "direction", "veeringLeft");
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Relationship not URI
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", INVALID_URI);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Missing qualifier
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// qualifier not URI
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", INVALID_URI);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Missing value
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			innerElement.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Missing link
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);

			// Security Attributes
			element = Util.buildDDMSElement(RelatedResource.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			innerElement = version.isAtLeast("4.0") ? element : Util.buildDDMSElement("RelatedResource", null);
			if (!version.isAtLeast("4.0"))
				element.appendChild(innerElement);
			Util.addDDMSAttribute(innerElement, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(innerElement, "value", TEST_VALUE);
			Link link = new Link(XLinkAttributesTest.getFixture(), SecurityAttributesTest.getFixture(false));
			innerElement.appendChild(link.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// Missing relationship
			testConstructor(WILL_FAIL, getLinks(), null, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);

			// Invalid direction
			testConstructor(WILL_FAIL, getLinks(), TEST_RELATIONSHIP, "veeringLeft", TEST_QUALIFIER, TEST_VALUE);

			// Relationship not URI
			testConstructor(WILL_FAIL, getLinks(), INVALID_URI, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);

			// Missing qualifier
			testConstructor(WILL_FAIL, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, null, TEST_VALUE);

			// Qualifier not URI
			testConstructor(WILL_FAIL, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, INVALID_URI, TEST_VALUE);

			// Missing value
			testConstructor(WILL_FAIL, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, null);

			// Missing link
			testConstructor(WILL_FAIL, null, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);

			// Security Attributes
			try {
				Link link = new Link(XLinkAttributesTest.getFixture(),
					SecurityAttributesTest.getFixture(false));
				List<Link> links = new ArrayList<Link>();
				links.add(link);
				new RelatedResource(links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE, null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Pre-DDMS 4.0, too many relatedResource children
			if (!version.isAtLeast("4.0")) {
				Element element = new Element(getValidElement(versionString));
				Element child = Util.buildDDMSElement("RelatedResource", null);
				child.addAttribute(Util.buildDDMSAttribute("qualifier", "ignoreMe"));
				child.addAttribute(Util.buildDDMSAttribute("value", "ignoreMe"));
				child.appendChild(new Element(LinkTest.getFixtureElement()));
				element.appendChild(child);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:RelatedResources element contains more than 1 ddms:relatedResource. "
					+ "To ensure consistency between versions of DDMS, each ddms:RelatedResources element "
					+ "should contain only 1 ddms:RelatedResource. DDMSence will only process the first child.";
				String locator = "ddms:relatedResources";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			RelatedResource dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP,
				TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			RelatedResource dataComponent = testConstructor(WILL_SUCCEED, getLinks(), DIFFERENT_VALUE, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, "inbound", TEST_QUALIFIER,
				TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION,
				DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION,
				TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			List<Link> differentLinks = new ArrayList<Link>();
			differentLinks.add(new Link(XLinkAttributesTest.getFixture()));
			differentLinks.add(new Link(XLinkAttributesTest.getFixture()));
			dataComponent = testConstructor(WILL_SUCCEED, differentLinks, TEST_RELATIONSHIP, TEST_DIRECTION,
				TEST_QUALIFIER, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER,
				TEST_VALUE);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testLinkReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<Link> links = getLinks();
			testConstructor(WILL_SUCCEED, links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
			testConstructor(WILL_SUCCEED, links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Link> links = getLinks();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new RelatedResource(links, TEST_RELATIONSHIP, TEST_DIRECTION, TEST_QUALIFIER, TEST_VALUE, null);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			RelatedResource.Builder builder = new RelatedResource.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RelatedResource.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RelatedResource.Builder();
			builder.setQualifier(TEST_QUALIFIER);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

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
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResource.Builder builder = new RelatedResource.Builder();
			assertNotNull(builder.getLinks().get(1));
		}
	}
}
