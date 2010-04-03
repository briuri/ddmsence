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
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:RelatedResource elements</p>
 * 
 * <p>
 * Because a ddms:RelatedResource is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RelatedResourceTest extends AbstractComponentTestCase {
	
	private static final String TEST_QUALIFIER = "http://purl.org/dc/terms/URI";
	private static final String TEST_VALUE = "http://en.wikipedia.org/wiki/Tank";
	private static final List<Link> TEST_LINKS = new ArrayList<Link>();
	
	private static final String XLINK_NAMESPACE = PropertyReader.getProperty("xlink.xmlNamespace");
	
	/**
	 * Constructor
	 */
	public RelatedResourceTest() {
		super(null);
		try {
			TEST_LINKS.clear();
			TEST_LINKS.add(new Link(TEST_VALUE, "tank", "Tank Page", "tank"));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create test data.");
		}
	}
		
	/**
	 * Returns a canned fixed value resource for testing.
	 * 
	 * @return a XOM element representing a valid resource
	 */
	private static Element getFixtureElement() throws InvalidDDMSException {
		Element resourceElement = Util.buildDDMSElement(RelatedResource.NAME, null);
		resourceElement.addNamespaceDeclaration(Util.DDMS_PREFIX, DDMSVersion.getCurrentNamespace());
		resourceElement.addAttribute(Util.buildDDMSAttribute("qualifier", TEST_QUALIFIER));
		resourceElement.addAttribute(Util.buildDDMSAttribute("value", TEST_VALUE));
		resourceElement.appendChild(new Element(LinkTest.getFixtureElement()));
		return (resourceElement);
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element	the element to build from
	 * @return a valid object
	 */
	private RelatedResource testConstructor(boolean expectFailure, Element element) {
		RelatedResource component = null;
		try {
			component = new RelatedResource(element);
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
	 * @param links		a list of links
	 * @param qualifier the qualifier value
	 * @param value		the value
	 * @return a valid object
	 */
	private RelatedResource testConstructor(boolean expectFailure, List<Link> links, String qualifier, String value) {
		RelatedResource component = null;
		try {
			component = new RelatedResource(links, qualifier, value);
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
		html.append("<meta name=\"RelatedResource.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"RelatedResource.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"Link.type\" content=\"locator\" />\n");
		html.append("<meta name=\"Link.href\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"Link.role\" content=\"tank\" />\n");
		html.append("<meta name=\"Link.title\" content=\"Tank Page\" />\n");
		html.append("<meta name=\"Link.label\" content=\"tank\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Related Resource qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Related Resource value: ").append(TEST_VALUE).append("\n");
		text.append("Link type: locator\n");
		text.append("Link href: ").append(TEST_VALUE).append("\n");
		text.append("Link role: tank\n");
		text.append("Link title: Tank Page\n");
		text.append("Link label: tank\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:RelatedResource xmlns:ddms=\"").append(DDMSVersion.getCurrentNamespace()).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append("\">\n\t");
		xml.append("<ddms:link xmlns:xlink=\"").append(XLINK_NAMESPACE).append("\" xlink:type=\"locator\" ");
		xml.append("xlink:href=\"").append(TEST_VALUE).append("\" ");
		xml.append("xlink:role=\"tank\" ");
		xml.append("xlink:title=\"Tank Page\" ");
		xml.append("xlink:label=\"tank\" />");
		xml.append("</ddms:RelatedResource>");
		return (formatXml(xml.toString(), preserveFormatting));
	}
	
	public void testNameAndNamespace() throws InvalidDDMSException {
		RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
		assertEquals(RelatedResource.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + RelatedResource.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() throws InvalidDDMSException {
		testConstructor(WILL_SUCCEED, getFixtureElement());
	}
	
	public void testDataConstructorValid() {
		testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
	}
	
	public void testElementConstructorInvalid() throws InvalidDDMSException {
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

	public void testDataConstructorInvalid() {
		// Missing qualifier
		testConstructor(WILL_FAIL, TEST_LINKS, null, TEST_VALUE);

		// Qualifier not URI
		testConstructor(WILL_FAIL, TEST_LINKS, INVALID_URI, TEST_VALUE);
		
		// Missing value
		testConstructor(WILL_FAIL, TEST_LINKS, TEST_QUALIFIER, null);
		
		// Missing link
		testConstructor(WILL_FAIL, null, TEST_QUALIFIER, TEST_VALUE);
	}
	
	public void testWarnings() throws InvalidDDMSException {
		// No warnings
		RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() throws InvalidDDMSException {
		RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
		RelatedResource dataComponent = testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
		RelatedResource dataComponent = testConstructor(WILL_SUCCEED, TEST_LINKS, DIFFERENT_VALUE, TEST_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
		
		List<Link> differentLinks = new ArrayList<Link>();
		differentLinks.add(new Link("http://different.com", null, null, null));
		dataComponent = testConstructor(WILL_SUCCEED, differentLinks, TEST_QUALIFIER, TEST_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		RelatedResource elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() throws InvalidDDMSException {
		RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() throws InvalidDDMSException {
		RelatedResource component = testConstructor(WILL_SUCCEED, getFixtureElement());
		assertEquals(getExpectedXMLOutput(false), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(false), component.toXML());
	}
	
	public void testLinkReuse() {
		testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
		testConstructor(WILL_SUCCEED, TEST_LINKS, TEST_QUALIFIER, TEST_VALUE);
	}
}
