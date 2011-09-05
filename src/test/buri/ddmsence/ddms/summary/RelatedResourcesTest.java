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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:relatedResources elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:RelatedResource tag is done separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RelatedResourcesTest extends AbstractComponentTestCase {

	private static final String TEST_RELATIONSHIP = "http://purl.org/dc/terms/references";
	private static final String TEST_DIRECTION = "outbound";

	/**
	 * Constructor
	 */
	public RelatedResourcesTest() throws InvalidDDMSException {
		super("relatedResources.xml");

	}

	/**
	 * Returns a set of links for testing
	 */
	private static List<Link> getLinks() throws InvalidDDMSException {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("http://en.wikipedia.org/wiki/Tank", null, null, null));
		return (links);
	}

	/**
	 * Returns a set of resources for testing
	 */
	private static List<RelatedResource> getResources() throws InvalidDDMSException {
		List<RelatedResource> resources = new ArrayList<RelatedResource>();
		resources.add(new RelatedResource(getLinks(), "http://purl.org/dc/terms/URI",
			"http://en.wikipedia.org/wiki/Tank"));
		return (resources);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private RelatedResources testConstructor(boolean expectFailure, Element element) {
		RelatedResources component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new RelatedResources(element);
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
	 * @param resources the related resources (1 required)
	 * @param relationship the relationship attribute (required)
	 * @param direction the relationship direction (optional)
	 * @return a valid object
	 */
	private RelatedResources testConstructor(boolean expectFailure, List<RelatedResource> resources,
		String relationship, String direction) {
		RelatedResources component = null;
		try {
			component = new RelatedResources(resources, relationship, direction,
				SecurityAttributesTest.getFixture(false));
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"relatedResources.relationship\" content=\"").append(TEST_RELATIONSHIP)
			.append("\" />\n");
		html.append("<meta name=\"relatedResources.direction\" content=\"").append(TEST_DIRECTION).append("\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.qualifier\" content=\"http://purl.org/dc/terms/URI\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.value\" content=\"http://en.wikipedia.org/wiki/Tank\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.type\" content=\"locator\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.href\" content=\"http://en.wikipedia.org/wiki/Tank\" />\n");
		html.append("<meta name=\"relatedResources.classification\" content=\"U\" />\n");
		html.append("<meta name=\"relatedResources.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("relatedResources relationship: ").append(TEST_RELATIONSHIP).append("\n");
		text.append("relatedResources direction: ").append(TEST_DIRECTION).append("\n");
		text.append("Related Resource qualifier: http://purl.org/dc/terms/URI\n");
		text.append("Related Resource value: http://en.wikipedia.org/wiki/Tank\n");
		text.append("Related Resource link type: locator\n");
		text.append("Related Resource link href: http://en.wikipedia.org/wiki/Tank\n");
		text.append("relatedResources classification: U\n");
		text.append("relatedResources ownerProducer: USA\n");

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
		xml.append("<ddms:relatedResources xmlns:ddms=\"").append(version.getNamespace()).append("\" xmlns:ICISM=\"")
			.append(version.getIsmNamespace()).append("\" ");
		xml.append("ddms:relationship=\"").append(TEST_RELATIONSHIP).append("\" ddms:direction=\"");
		xml.append(TEST_DIRECTION).append("\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">\n\t");
		xml.append("<ddms:RelatedResource ddms:qualifier=\"http://purl.org/dc/terms/URI\" ").append(
			"ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n\t\t");
		xml.append("<ddms:link xmlns:xlink=\"").append(version.getXlinkNamespace())
			.append("\" xlink:type=\"locator\" xlink:href=\"http://en.wikipedia.org/wiki/Tank\" />\n\t");
		xml.append("</ddms:RelatedResource>\n");
		xml.append("</ddms:relatedResources>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(RelatedResources.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + RelatedResources.getName(version),
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
			Element element = Util.buildDDMSElement(RelatedResources.getName(version), null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			element.appendChild(getResources().get(0).getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, TEST_DIRECTION);

			// No optional fields
			testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String relatedName = RelatedResources.getName(version);
			// Missing relationship
			Element element = Util.buildDDMSElement(relatedName, null);
			element.appendChild(getResources().get(0).getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Missing resource
			element = Util.buildDDMSElement(relatedName, null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			testConstructor(WILL_FAIL, element);

			// Invalid direction
			element = Util.buildDDMSElement(relatedName, null);
			Util.addDDMSAttribute(element, "relationship", TEST_RELATIONSHIP);
			Util.addDDMSAttribute(element, "direction", "north");
			element.appendChild(getResources().get(0).getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// relationship not URI
			element = Util.buildDDMSElement(relatedName, null);
			Util.addDDMSAttribute(element, "relationship", INVALID_URI);
			element.appendChild(getResources().get(0).getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing relationship
			testConstructor(WILL_FAIL, getResources(), null, TEST_DIRECTION);

			// Missing resouce
			testConstructor(WILL_FAIL, null, TEST_RELATIONSHIP, TEST_DIRECTION);

			// Invalid direction
			testConstructor(WILL_FAIL, getResources(), TEST_RELATIONSHIP, "north");

			// relationship not URI
			testConstructor(WILL_FAIL, getResources(), INVALID_URI, TEST_DIRECTION);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			RelatedResources dataComponent = testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP,
				TEST_DIRECTION);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			RelatedResources dataComponent = testConstructor(WILL_SUCCEED, getResources(), DIFFERENT_VALUE,
				TEST_DIRECTION);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, "inbound");
			assertFalse(elementComponent.equals(dataComponent));

			List<RelatedResource> resources = new ArrayList<RelatedResource>();
			resources.add(new RelatedResource(getLinks(), DIFFERENT_VALUE, DIFFERENT_VALUE));
			dataComponent = testConstructor(WILL_SUCCEED, resources, TEST_RELATIONSHIP, TEST_DIRECTION);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, TEST_DIRECTION);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, TEST_DIRECTION);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getResources(), TEST_RELATIONSHIP, TEST_DIRECTION);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testResourceReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<RelatedResource> resources = getResources();
			testConstructor(WILL_SUCCEED, resources, TEST_RELATIONSHIP, TEST_DIRECTION);
			testConstructor(WILL_SUCCEED, resources, TEST_RELATIONSHIP, TEST_DIRECTION);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = new RelatedResources(getResources(), TEST_RELATIONSHIP, TEST_DIRECTION,
				SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}

	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<RelatedResource> related = getResources();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new RelatedResources(related, TEST_RELATIONSHIP, TEST_DIRECTION, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			RelatedResources.Builder builder = new RelatedResources.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RelatedResources.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RelatedResources.Builder();
			builder.setDirection(TEST_DIRECTION);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Skip empty Resources
			builder = new RelatedResources.Builder();
			builder.setDirection(TEST_DIRECTION);
			builder.setRelationship(TEST_RELATIONSHIP);
			RelatedResource.Builder emptyBuilder = new RelatedResource.Builder();
			RelatedResource.Builder fullBuilder = new RelatedResource.Builder();
			fullBuilder.setQualifier("http://purl.org/dc/terms/URI");
			fullBuilder.setValue("http://en.wikipedia.org/wiki/Tank");
			Link.Builder fullLinkBuilder = new Link.Builder();
			fullLinkBuilder.setHref("http://ddmsence.urizone.net/");
			fullBuilder.getLinks().add(fullLinkBuilder);
			builder.getRelatedResources().add(emptyBuilder);
			builder.getRelatedResources().add(fullBuilder);
			assertEquals(1, builder.commit().getRelatedResources().size());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			RelatedResources.Builder builder = new RelatedResources.Builder();
			assertNotNull(builder.getRelatedResources().get(1));
		}
	}
}