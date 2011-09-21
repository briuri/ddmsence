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

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element linkElement = Util.buildDDMSElement(Link.getName(version), null);
		linkElement.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		XLinkAttributesTest.getLocatorFixture().addTo(linkElement);
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
	 * @param attributes the XLink Attributes
	 * @return a valid object
	 */
	private Link testConstructor(boolean expectFailure, XLinkAttributes attributes) {
		Link component = null;
		try {
			component = new Link(attributes);
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
		StringBuffer text = new StringBuffer();
		text.append(XLinkAttributesTest.getLocatorFixture().getOutput(isHTML, "link."));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:link ").append(getXmlnsDDMS()).append(" xmlns:xlink=\"").append(version.getXlinkNamespace())
			.append("\" ");
		xml.append("xlink:type=\"locator\" xlink:href=\"http://en.wikipedia.org/wiki/Tank\" ");
		xml.append("xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" />");
		return (xml.toString());
	}

	/**
	 * Helper method to create a XOM element that can be used to test element constructors
	 * 
	 * @param type the type
	 * @param href the href
	 * @return Element
	 */
	private Element buildComponentElement(String type, String href) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(Link.getName(version), null);
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		String xlinkNamespace = version.getXlinkNamespace();
		if (type != null)
			element.addAttribute(Util.buildAttribute(xlinkPrefix, "type", xlinkNamespace, type));
		if (href != null)
			element.addAttribute(Util.buildAttribute(xlinkPrefix, "href", xlinkNamespace, href));
		element.addAttribute(Util.buildAttribute(xlinkPrefix, "role", xlinkNamespace, TEST_ROLE));
		return (element);
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getFixtureElement()), DEFAULT_DDMS_PREFIX,
				Link.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());

			// No optional fields
			Element element = buildComponentElement(TEST_TYPE, TEST_HREF);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, XLinkAttributesTest.getLocatorFixture());

			// No optional fields
			testConstructor(WILL_SUCCEED, new XLinkAttributes(TEST_HREF, null, null, null));
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// Missing href
			Element element = buildComponentElement(TEST_TYPE, null);
			testConstructor(WILL_FAIL, element);

			// invalid type
			element = buildComponentElement("simple", TEST_HREF);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// Missing attributes
			testConstructor(WILL_FAIL, (XLinkAttributes) null);
			
			// Missing href
			testConstructor(WILL_FAIL, new XLinkAttributes("", TEST_ROLE, TEST_TITLE, TEST_LABEL));
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Link dataComponent = testConstructor(WILL_SUCCEED, XLinkAttributesTest.getLocatorFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Link dataComponent = testConstructor(WILL_SUCCEED, new XLinkAttributes(DIFFERENT_VALUE, TEST_ROLE, TEST_TITLE, TEST_LABEL));
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
			
			component = testConstructor(WILL_SUCCEED, XLinkAttributesTest.getLocatorFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}
	
	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, XLinkAttributesTest.getLocatorFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}
	
	public void testWrongLinkType() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			testConstructor(WILL_FAIL, XLinkAttributesTest.getSimpleFixture());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Link component = testConstructor(WILL_SUCCEED, getFixtureElement());

			// Equality after Building
			Link.Builder builder = new Link.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Link.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Link.Builder();
			builder.getXLinkAttributes().setRole(TEST_ROLE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
