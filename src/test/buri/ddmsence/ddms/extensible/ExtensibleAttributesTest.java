/* Copyright 2010 - 2012 by Brian Uri!
   
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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to the extensible attributes themselves. How they interact with parent classes is tested in those
 * classes. </p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public class ExtensibleAttributesTest extends AbstractBaseTestCase {

	private static final String TEST_NAMESPACE = "http://ddmsence.urizone.net/";

	private static final Attribute TEST_ATTRIBUTE = new Attribute("ddmsence:relevance", TEST_NAMESPACE, "95");

	/**
	 * Constructor
	 */
	public ExtensibleAttributesTest() {
		super(null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static ExtensibleAttributes getFixture() {
		try {
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			return (new ExtensibleAttributes(attributes));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ExtensibleAttributes getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ExtensibleAttributes attributes = null;
		try {
			attributes = new ExtensibleAttributes(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param attributes a list of attributes (optional)
	 * @return a valid object
	 */
	private ExtensibleAttributes getInstance(String message, List<Attribute> attributes) {
		boolean expectFailure = !Util.isEmpty(message);
		ExtensibleAttributes exAttributes = null;
		try {
			exAttributes = new ExtensibleAttributes(attributes);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			getInstance(SUCCESS, element);

			// No optional fields
			element = new Keyword("testValue", null).getXOMElementCopy();
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			getInstance(SUCCESS, attributes);

			// No optional fields
			getInstance(SUCCESS, (List<Attribute>) null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No invalid cases right now, since reserved names are silently skipped.
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No invalid cases right now. The validation occurs when the attributes are added to some component.
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			getInstance(SUCCESS, element);
			ExtensibleAttributes component = getInstance(SUCCESS, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = getInstance(SUCCESS, element);

			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes dataAttributes = getInstance(SUCCESS, attributes);

			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = getInstance(SUCCESS, element);
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute("essence:confidence", "http://essence/", "test"));
			ExtensibleAttributes dataAttributes = getInstance(SUCCESS, attributes);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = getInstance(SUCCESS, (List<Attribute>) null);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = getInstance(SUCCESS, element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementAttributes.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = getInstance(SUCCESS, element);
			assertEquals(getExpectedOutput(true), elementAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false), elementAttributes.getOutput(false, ""));

			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(new Attribute(TEST_ATTRIBUTE));
			elementAttributes = getInstance(SUCCESS, attributes);
			assertEquals(getExpectedOutput(true), elementAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false), elementAttributes.getOutput(false, ""));
		}
	}

	public void testAddTo() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleAttributes component = getFixture();

			Element element = Util.buildDDMSElement("sample", null);
			component.addTo(element);
			ExtensibleAttributes output = new ExtensibleAttributes(element);
			assertEquals(component, output);
		}
	}

	public void testGetNonNull() throws InvalidDDMSException {
		ExtensibleAttributes component = new ExtensibleAttributes((List) null);
		ExtensibleAttributes output = ExtensibleAttributes.getNonNullInstance(null);
		assertEquals(component, output);

		output = ExtensibleAttributes.getNonNullInstance(getFixture());
		assertEquals(getFixture(), output);
	}

	public void testIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes elementAttributes = getInstance(SUCCESS, element);
			assertFalse(elementAttributes.isEmpty());

			ExtensibleAttributes dataAttributes = getInstance(SUCCESS, (List<Attribute>) null);
			assertTrue(dataAttributes.isEmpty());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element element = new Keyword("testValue", null).getXOMElementCopy();
			element.addAttribute(new Attribute(TEST_ATTRIBUTE));
			ExtensibleAttributes component = getInstance(SUCCESS, element);
			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder();
			assertNotNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getAttributes().add(new ExtensibleAttributes.AttributeBuilder(TEST_ATTRIBUTE));
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No invalid cases right now, because validation cannot occur until these attributes are attached to something.
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder();
			assertNotNull(builder.getAttributes().get(1));
			assertTrue(builder.commit().getAttributes().isEmpty());
		}
	}
}
