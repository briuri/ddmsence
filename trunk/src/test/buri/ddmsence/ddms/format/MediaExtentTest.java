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
package buri.ddmsence.ddms.format;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:extent elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class MediaExtentTest extends AbstractComponentTestCase {

	private static final String TEST_QUALIFIER = "sizeBytes";
	private static final String TEST_VALUE = "75000";

	/**
	 * Constructor
	 */
	public MediaExtentTest() {
		super("media-extent.xml");
	}

	/**
	 * Returns a valid test fixture.
	 * 
	 * @return MediaExtent
	 * @throws InvalidDDMSException
	 */
	protected static MediaExtent getFixture() throws InvalidDDMSException {
		return (new MediaExtent("sizeBytes", "75000"));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private MediaExtent testConstructor(boolean expectFailure, Element element) {
		MediaExtent component = null;
		try {
			component = new MediaExtent(element);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private MediaExtent testConstructor(boolean expectFailure, String qualifier, String value) {
		MediaExtent component = null;
		try {
			component = new MediaExtent(qualifier, value);
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
		html.append("<meta name=\"format.extentqualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"format.extent\" content=\"").append(TEST_VALUE).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Extent qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Extent: ").append(TEST_VALUE).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:extent xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append(
			"\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(MediaExtent.NAME, component.getName());
			assertEquals(Util.DDMS_PREFIX, component.getPrefix());
			assertEquals(Util.DDMS_PREFIX + ":" + MediaExtent.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(MediaExtent.NAME, null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing qualifier
			Element element = Util.buildDDMSElement(MediaExtent.NAME, null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);

			// Qualifier not URI
			element = Util.buildDDMSElement(MediaExtent.NAME, null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			Util.addDDMSAttribute(element, "qualifier", INVALID_URI);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing qualifier
			testConstructor(WILL_FAIL, null, TEST_VALUE);

			// Qualifier not URI
			testConstructor(WILL_FAIL, INVALID_URI, TEST_VALUE);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			MediaExtent component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Qualifier without value
			Element element = Util.buildDDMSElement(MediaExtent.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A qualifier has been set without an accompanying value attribute.", component
				.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:extent", component.getValidationWarnings().get(0).getLocator());

			// Neither attribute
			element = Util.buildDDMSElement(MediaExtent.NAME, null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A completely empty ddms:extent element was found.", component.getValidationWarnings().get(0)
				.getText());
			assertEquals("/ddms:extent", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			MediaExtent dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			MediaExtent dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			MediaExtent component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}
}
