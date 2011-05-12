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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:link elements</p>
 * 
 * <p> Because a ddms:link is a local component, we cannot load a valid document from a unit test data file. We have to
 * build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class LinkTest extends AbstractComponentTestCase {

	private static final String TEST_TYPE = "locator";
	private static final String TEST_HREF = "http://en.wikipedia.org/wiki/Tank";
	private static final String TEST_ROLE = "tank";
	private static final String TEST_TITLE = "Tank Page";
	private static final String TEST_LABEL = "tank";

	/**
	 * Constructor
	 */
	public LinkTest() {
		super(null);
	}

	/**
	 * Returns a canned fixed value link for testing.
	 * 
	 * @return a XOM element representing a valid link
	 */
	protected static Element getFixtureElement() throws InvalidDDMSException {
		String xlinkPrefix = PropertyReader.getProperty("xlink.prefix");
		String xlinkNamespace = PropertyReader.getProperty("xlink.xmlNamespace");
		Element linkElement = Util.buildDDMSElement(Link.NAME, null);
		linkElement.addNamespaceDeclaration(PropertyReader.getProperty("ddms.prefix"), DDMSVersion.getCurrentVersion().getNamespace());
		linkElement.addNamespaceDeclaration(xlinkPrefix, xlinkNamespace);
		linkElement.addAttribute(Util.buildAttribute(xlinkPrefix, "type", xlinkNamespace, TEST_TYPE));
		linkElement.addAttribute(Util.buildAttribute(xlinkPrefix, "href", xlinkNamespace, TEST_HREF));
		linkElement.addAttribute(Util.buildAttribute(xlinkPrefix, "role", xlinkNamespace, TEST_ROLE));
		linkElement.addAttribute(Util.buildAttribute(xlinkPrefix, "title", xlinkNamespace, TEST_TITLE));
		linkElement.addAttribute(Util.buildAttribute(xlinkPrefix, "label", xlinkNamespace, TEST_LABEL));
		return (linkElement);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private Link testConstructor(boolean expectFailure, Element element) {
		Link component = null;
		try {
			component = new Link(element);
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
	 * @param href the link href (required)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @return a valid object
	 */
	private Link testConstructor(boolean expectFailure, String href, String role, String title, String label) {
		Link component = null;
		try {
			component = new Link(href, role, title, label);
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
		html.append("<meta name=\"Link.type\" content=\"").append(TEST_TYPE).append("\" />\n");
		html.append("<meta name=\"Link.href\" content=\"").append(TEST_HREF).append("\" />\n");
		html.append("<meta name=\"Link.role\" content=\"").append(TEST_ROLE).append("\" />\n");
		html.append("<meta name=\"Link.title\" content=\"").append(TEST_TITLE).append("\" />\n");
		html.append("<meta name=\"Link.label\" content=\"").append(TEST_LABEL).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Link type: ").append(TEST_TYPE).append("\n");
		text.append("Link href: ").append(TEST_HREF).append("\n");
		text.append("Link role: ").append(TEST_ROLE).append("\n");
		text.append("Link title: ").append(TEST_TITLE).append("\n");
		text.append("Link label: ").append(TEST_LABEL).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:link xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\" xmlns:xlink=\"").append(PropertyReader.getProperty("xlink.xmlNamespace")).append("\" ");
		xml.append("xlink:type=\"").append(TEST_TYPE).append("\" ");
		xml.append("xlink:href=\"").append(TEST_HREF).append("\" ");
		xml.append("xlink:role=\"").append(TEST_ROLE).append("\" ");
		xml.append("xlink:title=\"").append(TEST_TITLE).append("\" ");
		xml.append("xlink:label=\"").append(TEST_LABEL).append("\" />");
		return (xml.toString());
	}
	
	/**
	 * Helper method to create a XOM element that can be used to test element constructors
	 * 
	 * @param type the type
	 * @param href the  href
	 * @return Element
	 */
	private Element buildComponentElement(String type, String href) {
		Element element = Util.buildDDMSElement(Link.NAME, null);
		String xlinkPrefix = PropertyReader.getProperty("xlink.prefix");
		String xlinkNamespace = PropertyReader.getProperty("xlink.xmlNamespace");
		if (type != null)
			element.addAttribute(Util.buildAttribute(xlinkPrefix, "type", xlinkNamespace, type));
		if (href != null)
			element.addAttribute(Util.buildAttribute(xlinkPrefix, "href", xlinkNamespace, href));
		return (element);
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(Link.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Link.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	
	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());

			// No optional fields
			Element element = buildComponentElement(TEST_TYPE, TEST_HREF);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_HREF, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing href
			Element element = buildComponentElement(TEST_TYPE, null);
			testConstructor(WILL_FAIL, element);

			// href not URI
			element = buildComponentElement(TEST_TYPE, INVALID_URI);
			testConstructor(WILL_FAIL, element);

			// invalid type
			element = buildComponentElement("type", TEST_HREF);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing href
			testConstructor(WILL_FAIL, "", TEST_ROLE, TEST_TITLE, TEST_LABEL);

			// href not URI
			testConstructor(WILL_FAIL, INVALID_URI, TEST_ROLE, TEST_TITLE, TEST_LABEL);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Link dataComponent = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Link dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_HREF, DIFFERENT_VALUE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, DIFFERENT_VALUE, TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			
			// Equality after Building
			Link.Builder builder = new Link.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new Link.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new Link.Builder();
			builder.setRole(TEST_ROLE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
