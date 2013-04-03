/* Copyright 2010 - 2013 by Brian Uri!
   
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
 * <p>
 * Tests related to the extensible attributes themselves. How they interact with parent classes is tested in those
 * classes.
 * </p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public class ExtensibleAttributesTest extends AbstractBaseTestCase {

	private static final String TEST_NAMESPACE = "http://ddmsence.urizone.net/";

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
			attributes.add(getTestAttribute());
			return (new ExtensibleAttributes(attributes));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns generated test data.
	 */
	private static Attribute getTestAttribute() {
		return (new Attribute("ddmsence:relevance", TEST_NAMESPACE, "95"));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param element the element to build from
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private ExtensibleAttributes getInstance(Element element, String message) {
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
	 * @param builder the builder to commit
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private ExtensibleAttributes getInstance(ExtensibleAttributes.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		ExtensibleAttributes component = null;
		try {
			component = builder.commit();
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns a builder, pre-populated with base data from the test attribute.
	 * 
	 * This builder can then be modified to test various conditions.
	 */
	private ExtensibleAttributes.Builder getBaseBuilder() {
		try {
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(getTestAttribute());
			ExtensibleAttributes component = new ExtensibleAttributes(attributes);
			return (new ExtensibleAttributes.Builder(component));
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(false, e);
		}
		return (null);
	}

	/**
	 * Returns a test element that can be decorated with extensible attributes.
	 */
	private Element getTestElement() {
		try {
			return (new Keyword("testValue", null).getXOMElementCopy());
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(false, e);
		}
		return (null);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "ddmsence.relevance", "95"));
		return (text.toString());
	}

	public void testConstructors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based
			Element element = getTestElement();
			element.addAttribute(new Attribute(getTestAttribute()));
			getInstance(element, SUCCESS);

			// Data-based via Builder
			getBaseBuilder();
		}
	}

	public void testConstructorsMinimal() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, no optional fields
			ExtensibleAttributes elementComponent = getInstance(getTestElement(), SUCCESS);

			// Data-based via Builder, no optional fields
			getInstance(new ExtensibleAttributes.Builder(elementComponent), SUCCESS);
		}
	}

	public void testValidationErrors() {
		// No invalid cases right now. The validation occurs when the attributes are added to some component.
	}

	public void testValidationWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Element element = getTestElement();
			element.addAttribute(new Attribute(getTestAttribute()));
			ExtensibleAttributes component = getInstance(element, SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			Element element = getTestElement();
			element.addAttribute(new Attribute(getTestAttribute()));
			ExtensibleAttributes elementComponent = getInstance(element, SUCCESS);
			ExtensibleAttributes builderComponent = new ExtensibleAttributes.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Wrong class
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));

			// Different attribute list sizes
			ExtensibleAttributes.Builder builder = getBaseBuilder();
			Attribute attr = new Attribute("essence:confidence", "http://essence/", "test");
			builder.getAttributes().add(new ExtensibleAttributes.AttributeBuilder(attr));
			assertFalse(elementComponent.equals(builder.commit()));

			// Different attribute name
			builder = getBaseBuilder();
			builder.getAttributes().clear();
			attr = new Attribute("ddmsence:confidence", TEST_NAMESPACE, "95");
			builder.getAttributes().add(new ExtensibleAttributes.AttributeBuilder(attr));
			assertFalse(elementComponent.equals(builder.commit()));

			// Different attribute namespace
			builder = getBaseBuilder();
			builder.getAttributes().clear();
			attr = new Attribute("essence:relevance", "http://essence/", "95");
			builder.getAttributes().add(new ExtensibleAttributes.AttributeBuilder(attr));
			assertFalse(elementComponent.equals(builder.commit()));
		}
	}

	public void testVersionSpecific() {
		// No tests.
	}

	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element element = getTestElement();
			element.addAttribute(new Attribute(getTestAttribute()));
			ExtensibleAttributes elementComponent = getInstance(element, SUCCESS);
			assertEquals(getExpectedOutput(true), elementComponent.getOutput(true, ""));
			assertEquals(getExpectedOutput(false), elementComponent.getOutput(false, ""));
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ExtensibleAttributes.Builder builder = new ExtensibleAttributes.Builder();
			assertNotNull(builder.commit());
			assertTrue(builder.isEmpty());

			builder.getAttributes().add(new ExtensibleAttributes.AttributeBuilder(getTestAttribute()));
			assertFalse(builder.isEmpty());
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

	public void testAddTo() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleAttributes component = getFixture();

			// Base case
			Element element = Util.buildDDMSElement("sample", null);
			component.addTo(element);
			ExtensibleAttributes output = new ExtensibleAttributes(element);
			assertEquals(component, output);

			// Duplicate
			element = getTestElement();
			element.addAttribute(getTestAttribute());
			try {
				component.addTo(element);
				fail("addTo() allowed duplicate attribute.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The extensible attribute with");
			}
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

			ExtensibleAttributes component = getInstance(getTestElement(), SUCCESS);
			assertTrue(component.isEmpty());
		}
	}
}
