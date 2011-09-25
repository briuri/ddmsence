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
package buri.ddmsence.ddms.format;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:format elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class FormatTest extends AbstractComponentTestCase {

	private static final String TEST_MIME_TYPE = "text/xml";
	private static final String TEST_MEDIUM = "digital";

	/**
	 * Constructor
	 */
	public FormatTest() throws InvalidDDMSException {
		super("format.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Format getFixture() {
		try {
			return (new Format("text/xml", null, null));
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
	private Format getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Format component = null;
		try {
			component = new Format(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param mimeType the mimeType element (required)
	 * @param extent the extent element (may be null)
	 * @param medium the medium element (may be null)
	 * @return a valid object
	 */
	private Format getInstance(String message, String mimeType, Extent extent, String medium) {
		boolean expectFailure = !Util.isEmpty(message);
		Format component = null;
		try {
			component = new Format(mimeType, extent, medium);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to manage the deprecated Media wrapper element
	 * 
	 * @param innerElement the element containing the guts of this component
	 * @return the element itself in DDMS 4.0 or later, or the element wrapped in another element
	 */
	private Element wrapInnerElement(Element innerElement) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String name = Format.getName(version);
		if (version.isAtLeast("4.0")) {
			innerElement.setLocalName(name);
			return (innerElement);
		}
		Element element = Util.buildDDMSElement(name, null);
		element.appendChild(innerElement);
		return (element);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0") ? "format." : "format.Media.";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "mimeType", TEST_MIME_TYPE));
		text.append(ExtentTest.getFixture().getOutput(isHTML, prefix));
		text.append(buildOutput(isHTML, prefix + "medium", TEST_MEDIUM));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:format ").append(getXmlnsDDMS()).append(">\n\t");
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
			xml.append("<ddms:mimeType>text/xml</ddms:mimeType>\n\t");
			xml.append("<ddms:extent ddms:qualifier=\"sizeBytes\" ddms:value=\"75000\" />\n\t");
			xml.append("<ddms:medium>digital</ddms:medium>\n");
		}
		else {
			xml.append("<ddms:Media>\n\t\t");
			xml.append("<ddms:mimeType>text/xml</ddms:mimeType>\n\t\t");
			xml.append("<ddms:extent ddms:qualifier=\"sizeBytes\" ddms:value=\"75000\" />\n\t\t");
			xml.append("<ddms:medium>digital</ddms:medium>\n\t");
			xml.append("</ddms:Media>\n");
		}
		xml.append("</ddms:format>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Format.getName(version));
			getInstance("Unexpected namespace URI and local name encountered: ddms:wrongName", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element mediaElement = Util.buildDDMSElement("Media", null);
			Util.addDDMSChildElement(mediaElement, "mimeType", "text/html");
			getInstance(SUCCESS, wrapInnerElement(mediaElement));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);

			// No optional fields
			getInstance(SUCCESS, TEST_MIME_TYPE, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String extentName = Extent.getName(version);

			// Missing mimeType
			Element mediaElement = Util.buildDDMSElement("Media", null);
			getInstance("mimeType is required.", wrapInnerElement(mediaElement));

			// Empty mimeType
			mediaElement = Util.buildDDMSElement("Media", null);
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", ""));
			getInstance("mimeType is required.", wrapInnerElement(mediaElement));

			// Too many mimeTypes
			mediaElement = Util.buildDDMSElement("Media", null);
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
			getInstance("Exactly 1 mimeType element must exist.", wrapInnerElement(mediaElement));

			// Too many extents
			mediaElement = Util.buildDDMSElement("Media", null);
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
			mediaElement.appendChild(Util.buildDDMSElement(extentName, null));
			mediaElement.appendChild(Util.buildDDMSElement(extentName, null));
			getInstance("No more than 1 extent element can exist.", wrapInnerElement(mediaElement));

			// Too many mediums
			mediaElement = Util.buildDDMSElement("Media", null);
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
			mediaElement.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
			mediaElement.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
			getInstance("No more than 1 medium element can exist.", wrapInnerElement(mediaElement));

			// Invalid Extent
			Element extentElement = Util.buildDDMSElement(extentName, null);
			Util.addDDMSAttribute(extentElement, "value", "test");
			mediaElement = Util.buildDDMSElement("Media", null);
			Util.addDDMSChildElement(mediaElement, "mimeType", "text/html");
			mediaElement.appendChild(extentElement);
			getInstance("qualifier attribute is required.", wrapInnerElement(mediaElement));
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing mimeType
			getInstance("mimeType is required.", null, ExtentTest.getFixture(), TEST_MEDIUM);

			// Empty mimeType
			getInstance("mimeType is required.", "", ExtentTest.getFixture(), TEST_MEDIUM);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Format component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Medium element with no value or empty value
			Element mediaElement = Util.buildDDMSElement("Media", null);
			mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
			mediaElement.appendChild(Util.buildDDMSElement("medium", null));
			component = getInstance(SUCCESS, wrapInnerElement(mediaElement));
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ddms:medium element was found with no value.";
			String locator = version.isAtLeast("4.0") ? "ddms:format" : "ddms:format/ddms:Media";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Nested warnings
			component = getInstance(SUCCESS, TEST_MIME_TYPE, new Extent("sizeBytes", ""), TEST_MEDIUM);
			assertEquals(1, component.getValidationWarnings().size());
			text = "A qualifier has been set without an accompanying value attribute.";
			locator = (version.isAtLeast("4.0")) ? "ddms:format/ddms:extent" : "ddms:format/ddms:Media/ddms:extent";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Format dataComponent = getInstance(SUCCESS, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Format elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Format dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_MIME_TYPE, null, TEST_MEDIUM);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, "TEST_MIME_TYPE", ExtentTest.getFixture(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testExtentReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Extent extent = ExtentTest.getFixture();
			getInstance(SUCCESS, TEST_MIME_TYPE, extent, TEST_MEDIUM);
			getInstance(SUCCESS, TEST_MIME_TYPE, extent, TEST_MEDIUM);
		}
	}

	public void testGetExtentQualifier() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(component.getExtentQualifier(), component.getExtent().getQualifier());
		}
	}

	public void testGetExtentQualifierNoExtent() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, TEST_MIME_TYPE, null, null);
			assertEquals("", component.getExtentQualifier());
		}
	}

	public void testGetExtentValue() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(component.getExtentValue(), component.getExtent().getValue());
		}
	}

	public void testGetExtentValueNoExtent() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, TEST_MIME_TYPE, null, null);
			assertEquals("", component.getExtentValue());
		}
	}

	public void testWrongVersionExtent() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Extent extent = new Extent("test", "test");
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Format(TEST_MIME_TYPE, extent, TEST_MEDIUM);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A child component, ddms:extent, is using a different version of DDMS from its parent.");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Format component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Format.Builder builder = new Format.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Format.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Format.Builder();
			builder.setMedium(TEST_MEDIUM);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "mimeType is required.");
			}

			// No extent vs. empty extent
			builder = new Format.Builder();
			builder.setMimeType(TEST_MIME_TYPE);
			builder.setMedium(TEST_MEDIUM);
			assertNotNull(builder.getExtent());
			assertNull(builder.commit().getExtent());
			builder.getExtent().setQualifier("sizeBytes");
			builder.getExtent().setValue("75000");
			assertNotNull(builder.commit().getExtent());
		}
	}
}
