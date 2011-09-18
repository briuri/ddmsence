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
package buri.ddmsence.ddms.summary.xlink;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to the XLINK attributes on ddms:link and ddms:taskID elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class XLinkAttributesTest extends AbstractComponentTestCase {

	private static final String TEST_TYPE = "locator";
	private static final String TEST_HREF = "http://en.wikipedia.org/wiki/Tank";
	private static final String TEST_ROLE = "tank";
	private static final String TEST_TITLE = "Tank Page";
	private static final String TEST_LABEL = "tank";

	/**
	 * Constructor
	 */
	public XLinkAttributesTest() {
		super(null);
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @return XLinkAttributes
	 */
	public static XLinkAttributes getFixture() throws InvalidDDMSException {
		return (new XLinkAttributes(TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private XLinkAttributes testConstructor(boolean expectFailure, Element element) {
		XLinkAttributes attributes = null;
		try {
			attributes = new XLinkAttributes(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @return a valid object
	 */
	private XLinkAttributes testConstructor(boolean expectFailure, String type, String href, String role, String title,
		String label) {
		XLinkAttributes attributes = null;
		try {
			attributes = new XLinkAttributes(type, href, role, title, label);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "type", TEST_TYPE));
		text.append(buildOutput(isHTML, "href", TEST_HREF));
		text.append(buildOutput(isHTML, "role", TEST_ROLE));
		text.append(buildOutput(isHTML, "title", TEST_TITLE));
		text.append(buildOutput(isHTML, "label", TEST_LABEL));
		return (text.toString());
	}

	/**
	 * Helper method to add srs attributes to a XOM element. The element is not validated.
	 * 
	 * @param element element
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 */
	private void addAttributes(Element element, String type, String href, String role, String title, String label) {
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		String xlinkNamespace = DDMSVersion.getCurrentVersion().getXlinkNamespace();
		Util.addAttribute(element, xlinkPrefix, "type", xlinkNamespace, type);
		Util.addAttribute(element, xlinkPrefix, "href", xlinkNamespace, href);
		Util.addAttribute(element, xlinkPrefix, "role", xlinkNamespace, role);
		Util.addAttribute(element, xlinkPrefix, "title", xlinkNamespace, title);
		Util.addAttribute(element, xlinkPrefix, "label", xlinkNamespace, label);
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			
			// All fields
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildDDMSElement("link", null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			// type is not locator or simple
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, "retriever", null, null, null, null);
			testConstructor(WILL_FAIL, element);
			
			// href is not valid URI
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, null, INVALID_URI, null, null, null);
			testConstructor(WILL_FAIL, element);
			
			// role is not valid URI
			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement("link", null);
				addAttributes(element, null, null, INVALID_URI, null, null);
				testConstructor(WILL_FAIL, element);
			}
			
			// label is not valid NCName
			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement("link", null);
				addAttributes(element, null, null, null, null, "ddms:prefix& GML");
				testConstructor(WILL_FAIL, element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			// type is not locator or simple
			testConstructor(WILL_FAIL, "retriever", null, null, null, null);
			
			// href is not valid URI
			testConstructor(WILL_FAIL, null, INVALID_URI, null, null, null);
			
			// role is not valid URI
			if (version.isAtLeast("4.0")) {
				testConstructor(WILL_FAIL, null, null, INVALID_URI, null, null);
			}
			
			// label is not valid NCName
			if (version.isAtLeast("4.0")) {
				testConstructor(WILL_FAIL, null, null, null, null, "ddms:prefix& GML");
			}
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			
			// No warnings
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes component = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE,
				TEST_LABEL);

			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, "simple", TEST_HREF,
				TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, DIFFERENT_VALUE, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, DIFFERENT_VALUE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));
			
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, DIFFERENT_VALUE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));
			
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, DIFFERENT_VALUE);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes attributes = new XLinkAttributes(element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(attributes.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes attributes = new XLinkAttributes(element);
			assertEquals(getExpectedOutput(true), attributes.getOutput(true, ""));

			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE,
				TEST_LABEL);
			assertEquals(getExpectedOutput(true), dataAttributes.getOutput(true, ""));
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes attributes = new XLinkAttributes(element);
			assertEquals(getExpectedOutput(false), attributes.getOutput(false, ""));

			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_TYPE, TEST_HREF, TEST_ROLE, TEST_TITLE,
				TEST_LABEL);
			assertEquals(getExpectedOutput(false), dataAttributes.getOutput(false, ""));
		}
	}

//	public void testWrongVersionAttributes() throws InvalidDDMSException {
//		DDMSVersion.setCurrentVersion("3.0");
//		XLinkAttributes attr = getFixture();
//		DDMSVersion.setCurrentVersion("2.0");
//		try {
//			// TODO
//			fail("Allowed different versions.");
//		} catch (InvalidDDMSException e) {
//			// Good
//		}
//	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			XLinkAttributes component = getFixture();

			// Equality after Building
			XLinkAttributes.Builder builder = new XLinkAttributes.Builder(component);
			assertEquals(builder.commit(), component);

			// Validation
			builder = new XLinkAttributes.Builder();
			builder.setType("retriever");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Empty Tests
			builder = new XLinkAttributes.Builder();
			assertTrue(builder.isEmpty());
			builder.setLabel(TEST_LABEL);
			assertFalse(builder.isEmpty());

		}
	}
}
