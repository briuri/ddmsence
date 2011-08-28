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
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:RelatedResource elements</p>
 * 
 * <p> Because a ddms:RelatedResource is a local component, we cannot load a valid document from a unit test data file.
 * We have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RelatedResourceTest extends AbstractComponentTestCase {

	private static final String TEST_QUALIFIER = "http://purl.org/dc/terms/URI";
	private static final String TEST_VALUE = "http://en.wikipedia.org/wiki/Tank";

	/**
	 * Constructor
	 */
	public RelatedResourceTest() {
		super(null);
	}
	
	/**
	 * Returns a list of links for testing
	 */
	private static List<Link> getLinks() throws InvalidDDMSException {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(TEST_VALUE, "tank", "Tank Page", "tank"));
		return (links);
	}

	/**
	 * Returns a canned fixed value resource for testing.
	 * 
	 * @return a XOM element representing a valid resource
	 */
	private static Element getFixtureElement() throws InvalidDDMSException {
		Element resourceElement = Util.buildDDMSElement(RelatedResource.NAME, null);
		resourceElement.addNamespaceDeclaration(PropertyReader.getProperty("ddms.prefix"), DDMSVersion.getCurrentVersion().getNamespace());
		resourceElement.addAttribute(Util.buildDDMSAttribute("qualifier", TEST_QUALIFIER));
		resourceElement.addAttribute(Util.buildDDMSAttribute("value", TEST_VALUE));
		resourceElement.appendChild(new Element(LinkTest.getFixtureElement()));
		return (resourceElement);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private RelatedResource testConstructor(boolean expectFailure, List<Link> links, String qualifier, String value) {
		RelatedResource component = null;
		try {
			component = new RelatedResource(links, qualifier, value);
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
		html.append("<meta name=\"relatedResources.RelatedResource.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.type\" content=\"locator\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.href\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.role\" content=\"tank\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.title\" content=\"Tank Page\" />\n");
		html.append("<meta name=\"relatedResources.RelatedResource.link.label\" content=\"tank\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Related Resource qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Related Resource value: ").append(TEST_VALUE).append("\n");
		text.append("Related Resource link type: locator\n");
		text.append("Related Resource link href: ").append(TEST_VALUE).append("\n");
		text.append("Related Resource link role: tank\n");
		text.append("Related Resource link title: Tank Page\n");
		text.append("Related Resource link label: tank\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:RelatedResource xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE)
			.append("\">\n\t");
		xml.append("<ddms:link xmlns:xlink=\"").append(DDMSVersion.getCurrentVersion().getXlinkNamespace()).append("\" xlink:type=\"locator\" ");
		xml.append("xlink:href=\"").append(TEST_VALUE).append("\" ");
		xml.append("xlink:role=\"tank\" ");
		xml.append("xlink:title=\"Tank Page\" ");
		xml.append("xlink:label=\"tank\" />");
		xml.append("</ddms:RelatedResource>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(RelatedResource.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + RelatedResource.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			testConstructor(WILL_SUCCEED, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing qualifier
			Element element = Util.buildDDMSElement(RelatedResource.NAME, null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			element.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// qualifier not URI
			element = Util.buildDDMSElement(RelatedResource.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", INVALID_URI);
			element.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Missing value
			element = Util.buildDDMSElement(RelatedResource.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			element.appendChild(new Element(LinkTest.getFixtureElement()));
			testConstructor(WILL_FAIL, element);

			// Missing link
			element = Util.buildDDMSElement(RelatedResource.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing qualifier
			testConstructor(WILL_FAIL, getLinks(), null, TEST_VALUE);

			// Qualifier not URI
			testConstructor(WILL_FAIL, getLinks(), INVALID_URI, TEST_VALUE);

			// Missing value
			testConstructor(WILL_FAIL, getLinks(), TEST_QUALIFIER, null);

			// Missing link
			testConstructor(WILL_FAIL, null, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			RelatedResource dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			RelatedResource dataComponent = testConstructor(WILL_SUCCEED, getLinks(), DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			List<Link> differentLinks = new ArrayList<Link>();
			differentLinks.add(new Link("http://different.com", null, null, null));
			dataComponent = testConstructor(WILL_SUCCEED, differentLinks, TEST_QUALIFIER, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, getLinks(), TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testLinkReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			List<Link> links = getLinks();
			testConstructor(WILL_SUCCEED, links, TEST_QUALIFIER, TEST_VALUE);
			testConstructor(WILL_SUCCEED, links, TEST_QUALIFIER, TEST_VALUE);
		}
	}
	
	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Link> links = getLinks();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new RelatedResource(links, TEST_QUALIFIER, TEST_VALUE);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
			
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
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			
			// Skip empty Links
			builder = new RelatedResource.Builder();
			builder.setQualifier(TEST_QUALIFIER);
			builder.setValue(TEST_VALUE);
			Link.Builder emptyBuilder = new Link.Builder();
			Link.Builder fullBuilder = new Link.Builder();
			fullBuilder.setHref("http://ddmsence.urizone.net/");
			builder.getLinks().add(emptyBuilder);
			builder.getLinks().add(fullBuilder);
			assertEquals(1, builder.commit().getLinks().size());
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			RelatedResource.Builder builder = new RelatedResource.Builder();
			assertNotNull(builder.getLinks().get(1));
		}
	}
}
