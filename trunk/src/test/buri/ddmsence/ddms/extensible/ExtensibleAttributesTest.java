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
package buri.ddmsence.ddms.extensible;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.util.DDMSVersion;

/**
 * <p>Tests related to the extensible attributes themselves. How they interact with parent classes is tested in those
 * classes.</p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public class ExtensibleAttributesTest extends AbstractComponentTestCase {

	private static final String TEST_NAMESPACE = "http://ddmsence.urizone.net/";

	private static final Attribute TEST_ATTRIBUTE = new Attribute("ddmsence:relevance", TEST_NAMESPACE, "95");

	/**
	 * Constructor
	 */
	public ExtensibleAttributesTest() {
		super(null);
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @return ExtensibleAttributes
	 */
	public static ExtensibleAttributes getFixture() throws InvalidDDMSException {
		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(TEST_ATTRIBUTE));
		return (new ExtensibleAttributes(attributes));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ExtensibleAttributes testConstructor(boolean expectFailure, Element element) {
		ExtensibleAttributes attributes = null;
		try {
			attributes = new ExtensibleAttributes(element);
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
	 * @param attributes a list of attributes (optional)
	 * @return a valid object
	 */
	private ExtensibleAttributes testConstructor(boolean expectFailure, List<Attribute> attributes) {
		ExtensibleAttributes exAttributes = null;
		try {
			exAttributes = new ExtensibleAttributes(attributes);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (exAttributes);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "ddmsence.relevance", "95"));
		return (text.toString());
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// All fields
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = new Keyword("testValue", null).getXOMElementCopy();
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// All fields
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			testConstructor(WILL_SUCCEED, attributes);

			// No optional fields
			testConstructor(WILL_SUCCEED, (List<Attribute>) null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No invalid cases right now, since reserved names are silently skipped.
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No invalid cases right now. The validation occurs when the attributes are added to some component.
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			testConstructor(WILL_SUCCEED, element);
			ExtensibleAttributes component = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);

			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes dataAttributes = testConstructor(WILL_SUCCEED, attributes);

			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testIsEmpty() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			assertFalse(elementAttributes.isEmpty());

			ExtensibleAttributes dataAttributes = testConstructor(WILL_SUCCEED, (List<Attribute>) null);
			assertTrue(dataAttributes.isEmpty());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute("essence:confidence", "http://essence/", "test"));
			ExtensibleAttributes dataAttributes = testConstructor(WILL_SUCCEED, attributes);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, (List<Attribute>) null);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementAttributes.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			assertEquals(getExpectedOutput(true), elementAttributes.getOutput(true, ""));

			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			elementAttributes = testConstructor(WILL_SUCCEED, attributes);
			assertEquals(getExpectedOutput(true), elementAttributes.getOutput(true, ""));
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			assertEquals(getExpectedOutput(false), elementAttributes.getOutput(false, ""));

			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			elementAttributes = testConstructor(WILL_SUCCEED, attributes);
			assertEquals(getExpectedOutput(false), elementAttributes.getOutput(false,""));
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes component = testConstructor(WILL_SUCCEED, element);

			// Equality after Building
			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty Case
			builder = new ExtensibleAttributes.Builder();
			assertTrue(builder.commit().getAttributes().isEmpty());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder();
			assertNotNull(builder.getAttributes().get(1));
			assertTrue(builder.commit().getAttributes().isEmpty());
		}
	}
}
