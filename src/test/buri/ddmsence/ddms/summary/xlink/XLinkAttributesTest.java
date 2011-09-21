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

	private static final String TEST_HREF = "http://en.wikipedia.org/wiki/Tank";
	private static final String TEST_ROLE = "tank";
	private static final String TEST_TITLE = "Tank Page";
	private static final String TEST_LABEL = "tank";
	private static final String TEST_ARCROLE = "arcrole";
	private static final String TEST_SHOW = "new";
	private static final String TEST_ACTUATE = "onLoad";

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
	public static XLinkAttributes getLocatorFixture() throws InvalidDDMSException {
		return (new XLinkAttributes(TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL));
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @return XLinkAttributes
	 */
	public static XLinkAttributes getSimpleFixture() throws InvalidDDMSException {
		return (new XLinkAttributes(TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE));
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @return XLinkAttributes
	 */
	public static XLinkAttributes getResourceFixture() throws InvalidDDMSException {
		return (new XLinkAttributes(TEST_ROLE, TEST_TITLE, TEST_LABEL));
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
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @return a valid object
	 */
	private XLinkAttributes testConstructor(boolean expectFailure, String role, String title, String label) {
		XLinkAttributes attributes = null;
		try {
			attributes = new XLinkAttributes(role, title, label);
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
	private XLinkAttributes testConstructor(boolean expectFailure, String href, String role, String title, String label) {
		XLinkAttributes attributes = null;
		try {
			attributes = new XLinkAttributes(href, role, title, label);
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
	 * @param arcrole the arcrole attribute (optional)
	 * @param show the show token (optional)
	 * @param actuate the actuate token (optional)
	 * @return a valid object
	 */
	private XLinkAttributes testConstructor(boolean expectFailure, String href, String role, String title,
		String arcrole, String show, String actuate) {
		XLinkAttributes attributes = null;
		try {
			attributes = new XLinkAttributes(href, role, title, arcrole, show, actuate);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML, String type) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "type", type));
		if (!"resource".equals(type))
			text.append(buildOutput(isHTML, "href", TEST_HREF));
		text.append(buildOutput(isHTML, "role", TEST_ROLE));
		text.append(buildOutput(isHTML, "title", TEST_TITLE));
		if (!"simple".equals(type))
			text.append(buildOutput(isHTML, "label", TEST_LABEL));
		if ("simple".equals(type)) {
			text.append(buildOutput(isHTML, "arcrole", TEST_ARCROLE));
			text.append(buildOutput(isHTML, "show", TEST_SHOW));
			text.append(buildOutput(isHTML, "actuate", TEST_ACTUATE));
		}
		return (text.toString());
	}

	/**
	 * Helper method to add attributes to a XOM element. The element is not validated.
	 * 
	 * @param element element
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param arcrole the arcrole attribute (optional)
	 * @param show the show token (optional)
	 * @param actuate the actuate token (optional)
	 */
	private void addAttributes(Element element, String href, String role, String title, String arcrole, String show,
		String actuate) {
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		String xlinkNamespace = DDMSVersion.getCurrentVersion().getXlinkNamespace();
		Util.addAttribute(element, xlinkPrefix, "type", xlinkNamespace, "simple");
		Util.addAttribute(element, xlinkPrefix, "href", xlinkNamespace, href);
		Util.addAttribute(element, xlinkPrefix, "role", xlinkNamespace, role);
		Util.addAttribute(element, xlinkPrefix, "title", xlinkNamespace, title);
		Util.addAttribute(element, xlinkPrefix, "arcrole", xlinkNamespace, arcrole);
		Util.addAttribute(element, xlinkPrefix, "show", xlinkNamespace, show);
		Util.addAttribute(element, xlinkPrefix, "actuate", xlinkNamespace, actuate);
	}

	/**
	 * Helper method to add attributes to a XOM element. The element is not validated.
	 * 
	 * @param element element
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 */
	private void addAttributes(Element element, String href, String role, String title, String label) {
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		String xlinkNamespace = DDMSVersion.getCurrentVersion().getXlinkNamespace();
		Util.addAttribute(element, xlinkPrefix, "type", xlinkNamespace, "locator");
		Util.addAttribute(element, xlinkPrefix, "href", xlinkNamespace, href);
		Util.addAttribute(element, xlinkPrefix, "role", xlinkNamespace, role);
		Util.addAttribute(element, xlinkPrefix, "title", xlinkNamespace, title);
		Util.addAttribute(element, xlinkPrefix, "label", xlinkNamespace, label);
	}

	/**
	 * Helper method to add attributes to a XOM element. The element is not validated.
	 * 
	 * @param element element
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 */
	private void addAttributes(Element element, String role, String title, String label) {
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		String xlinkNamespace = DDMSVersion.getCurrentVersion().getXlinkNamespace();
		Util.addAttribute(element, xlinkPrefix, "type", xlinkNamespace, "resource");
		Util.addAttribute(element, xlinkPrefix, "role", xlinkNamespace, role);
		Util.addAttribute(element, xlinkPrefix, "title", xlinkNamespace, title);
		Util.addAttribute(element, xlinkPrefix, "label", xlinkNamespace, label);
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields (locator)
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			testConstructor(WILL_SUCCEED, element);

			// All fields (simple)
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE);
			testConstructor(WILL_SUCCEED, element);

			// All fields (resource)
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields (all)
			element = Util.buildDDMSElement("link", null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields (locator)
			testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);

			// All fields (simple)
			testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE);

			// All fields (resource)
			testConstructor(WILL_SUCCEED, TEST_ROLE, TEST_TITLE, TEST_LABEL);

			// No optional fields (all)
			testConstructor(WILL_SUCCEED, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// href is not valid URI
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, INVALID_URI, null, null, null);
			testConstructor(WILL_FAIL, element);

			// role is not valid URI
			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement("link", null);
				addAttributes(element, null, INVALID_URI, null, null);
				testConstructor(WILL_FAIL, element);
			}

			// label is not valid NCName
			if (version.isAtLeast("4.0")) {
				element = Util.buildDDMSElement("link", null);
				addAttributes(element, null, null, null, "ddms:prefix& GML");
				testConstructor(WILL_FAIL, element);
			}

			// invalid arcrole
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, null, null, null, INVALID_URI, null, null);

			// invalid show
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, null, null, null, null, "notInTheTokenList", null);

			// invalid actuate
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, null, null, null, null, null, "notInTheTokenList");
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// href is not valid URI
			testConstructor(WILL_FAIL, INVALID_URI, null, null, null);

			// role is not valid URI
			if (version.isAtLeast("4.0")) {
				testConstructor(WILL_FAIL, null, INVALID_URI, null, null);
			}

			// label is not valid NCName
			if (version.isAtLeast("4.0")) {
				testConstructor(WILL_FAIL, null, null, null, "ddms:prefix& GML");
			}

			// invalid arcrole
			testConstructor(WILL_FAIL, null, null, null, INVALID_URI, null, null);

			// invalid show
			testConstructor(WILL_FAIL, null, null, null, null, "notInTheTokenList", null);

			// invalid actuate
			testConstructor(WILL_FAIL, null, null, null, null, null, "notInTheTokenList");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes component = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// locator version
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());

			// simple version
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE);
			elementAttributes = testConstructor(WILL_SUCCEED, element);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW,
				TEST_ACTUATE);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());

			// resource version
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			elementAttributes = testConstructor(WILL_SUCCEED, element);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());

		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// locator version
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_ROLE, TEST_TITLE,
				TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, DIFFERENT_VALUE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, DIFFERENT_VALUE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, DIFFERENT_VALUE);
			assertFalse(elementAttributes.equals(dataAttributes));

			// simple version
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE);

			dataAttributes = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_ROLE, TEST_TITLE, TEST_ARCROLE,
				TEST_SHOW, TEST_ACTUATE);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, DIFFERENT_VALUE, TEST_TITLE, TEST_ARCROLE,
				TEST_SHOW, TEST_ACTUATE);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, DIFFERENT_VALUE, TEST_ARCROLE,
				TEST_SHOW, TEST_ACTUATE);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, DIFFERENT_VALUE,
				TEST_SHOW, TEST_ACTUATE);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, "replace",
				TEST_ACTUATE);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW,
				"onRequest");
			assertFalse(elementAttributes.equals(dataAttributes));

			// resource version
			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			elementAttributes = testConstructor(WILL_SUCCEED, element);
			dataAttributes = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_TITLE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_ROLE, DIFFERENT_VALUE, TEST_LABEL);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_ROLE, TEST_TITLE, DIFFERENT_VALUE);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes attributes = new XLinkAttributes(element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(attributes.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			XLinkAttributes attributes = new XLinkAttributes(element);
			assertEquals(getExpectedOutput(true, "locator"), attributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "locator"), attributes.getOutput(false, ""));

			XLinkAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(getExpectedOutput(true, "locator"), dataAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "locator"), dataAttributes.getOutput(false, ""));

			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW, TEST_ACTUATE);
			attributes = new XLinkAttributes(element);
			assertEquals(getExpectedOutput(true, "simple"), attributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "simple"), attributes.getOutput(false, ""));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_HREF, TEST_ROLE, TEST_TITLE, TEST_ARCROLE, TEST_SHOW,
				TEST_ACTUATE);
			assertEquals(getExpectedOutput(true, "simple"), dataAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "simple"), dataAttributes.getOutput(false, ""));

			element = Util.buildDDMSElement("link", null);
			addAttributes(element, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			attributes = new XLinkAttributes(element);
			assertEquals(getExpectedOutput(true, "resource"), attributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "resource"), attributes.getOutput(false, ""));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_ROLE, TEST_TITLE, TEST_LABEL);
			assertEquals(getExpectedOutput(true, "resource"), dataAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false, "resource"), dataAttributes.getOutput(false, ""));
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			XLinkAttributes component = getLocatorFixture();

			// Equality after Building
			XLinkAttributes.Builder builder = new XLinkAttributes.Builder(component);
			assertEquals(builder.commit(), component);

			// Validation
			builder = new XLinkAttributes.Builder();
			builder.setType("locator");
			builder.setHref(INVALID_URI);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.setType("locator");
			builder.setHref(TEST_HREF);
			builder.commit();
			builder.setType("simple");
			builder.commit();
			builder.setType("resource");
			builder.commit();

			// Empty Tests
			builder = new XLinkAttributes.Builder();
			assertTrue(builder.isEmpty());
			builder.setLabel(TEST_LABEL);
			assertFalse(builder.isEmpty());

		}
	}
}
