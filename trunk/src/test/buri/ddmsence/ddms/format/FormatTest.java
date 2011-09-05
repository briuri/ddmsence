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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:Format elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:Media tag is done separately.
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
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Format testConstructor(boolean expectFailure, Element element) {
		Format component = null;
		try {
			component = new Format(element);
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
	 * @param mimeType the mimeType element (required)
	 * @param extent the extent element (may be null)
	 * @param medium the medium element (may be null)
	 * @return a valid object
	 */
	private Format testConstructor(boolean expectFailure, String mimeType, Extent extent, String medium) {
		Format component = null;
		try {
			component = new Format(mimeType, extent, medium);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() throws InvalidDDMSException {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"format.Media.mimeType\" content=\"").append(TEST_MIME_TYPE).append("\" />\n");
		html.append(ExtentTest.getFixture().toHTML());
		html.append("<meta name=\"format.Media.medium\" content=\"").append(TEST_MEDIUM).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append("format.Media.mimeType: ").append(TEST_MIME_TYPE).append("\n");
		text.append(ExtentTest.getFixture().toText());
		text.append("format.Media.medium: ").append(TEST_MEDIUM).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:format xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\">\n\t");
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
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Format.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Format.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Format.getName(version), null);
				Util.addDDMSChildElement(element, "mimeType", "text/html");
				testConstructor(WILL_SUCCEED, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				Util.addDDMSChildElement(mediaElement, "mimeType", "text/html");
				Element element = Util.buildDDMSElement(Format.getName(version), null);
				element.appendChild(mediaElement);
				testConstructor(WILL_SUCCEED, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String extentName = Extent.getName(version);
			String formatName = Format.getName(version);
			
			// Missing mimeType
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(mediaElement);
				testConstructor(WILL_FAIL, element);
			}

			// Empty mimeType
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(Util.buildDDMSElement("mimeType", ""));
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				Element element = Util.buildDDMSElement(formatName, null);
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", ""));
				element.appendChild(mediaElement);
				testConstructor(WILL_FAIL, element);
			}

			// Too many mimeTypes
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				element.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(mediaElement);
				testConstructor(WILL_FAIL, element);
			}

			// Too many extents
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				element.appendChild(Util.buildDDMSElement(extentName, null));
				element.appendChild(Util.buildDDMSElement(extentName, null));
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				mediaElement.appendChild(Util.buildDDMSElement(extentName, null));
				mediaElement.appendChild(Util.buildDDMSElement(extentName, null));
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(mediaElement);
				testConstructor(WILL_FAIL, element);
			}

			// Too many mediums
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				element.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
				element.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				mediaElement.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
				mediaElement.appendChild(Util.buildDDMSElement("medium", TEST_MEDIUM));
				Element element = Util.buildDDMSElement(formatName, null);
				element.appendChild(mediaElement);
				testConstructor(WILL_FAIL, element);
			}

			// Invalid Extent
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(formatName, null);
				Element extentElement = Util.buildDDMSElement(extentName, null);
				Util.addDDMSAttribute(extentElement, "value", "test");
				Util.addDDMSChildElement(element, "mimeType", "text/html");
				element.appendChild(extentElement);
				testConstructor(WILL_FAIL, element);
			}
			else {
				Element extentElement = Util.buildDDMSElement(extentName, null);
				Util.addDDMSAttribute(extentElement, "value", "test");
				Element mediaElement = Util.buildDDMSElement("Media", null);
				Util.addDDMSChildElement(mediaElement, "mimeType", "text/html");
				mediaElement.appendChild(extentElement);
				Element element = Util.buildDDMSElement(formatName, null);
				testConstructor(WILL_FAIL, element);
			}
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing mimeType
			testConstructor(WILL_FAIL, null, ExtentTest.getFixture(), TEST_MEDIUM);

			// Empty mimeType
			testConstructor(WILL_FAIL, "", ExtentTest.getFixture(), TEST_MEDIUM);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Medium element with no value or empty value
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Format.getName(version), null);
				element.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				element.appendChild(Util.buildDDMSElement("medium", null));
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("A ddms:medium element was found with no value.", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:format", component.getValidationWarnings().get(0).getLocator());
			}
			else {
				Element mediaElement = Util.buildDDMSElement("Media", null);
				mediaElement.appendChild(Util.buildDDMSElement("mimeType", TEST_MIME_TYPE));
				mediaElement.appendChild(Util.buildDDMSElement("medium", null));
				Element element = Util.buildDDMSElement(Format.getName(version), null);
				element.appendChild(mediaElement);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("A ddms:medium element was found with no value.", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:format/ddms:Media", component.getValidationWarnings().get(0).getLocator());
			}

			// Nested warnings
			component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, new Extent("sizeBytes", ""), TEST_MEDIUM);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals("A qualifier has been set without an accompanying value attribute.", 
				component.getValidationWarnings().get(0).getText());
			String locatorSuffix = (version.isAtLeast("4.0")) ? "/ddms:format/ddms:extent"
				: "/ddms:format/ddms:Media/ddms:extent";
			assertEquals(locatorSuffix, component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Format dataComponent = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, ExtentTest.getFixture(),
				TEST_MEDIUM);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Format elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Format dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, ExtentTest.getFixture(),
				TEST_MEDIUM);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, null, TEST_MEDIUM);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, "TEST_MIME_TYPE", ExtentTest.getFixture(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, ExtentTest.getFixture(), TEST_MEDIUM);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testExtentReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Extent extent = ExtentTest.getFixture();
			testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, extent, TEST_MEDIUM);
			testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, extent, TEST_MEDIUM);
		}
	}

	public void testGetExtentQualifier() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(component.getExtentQualifier(), component.getExtent().getQualifier());
		}
	}

	public void testGetExtentQualifierNoExtent() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, null, null);
			assertEquals("", component.getExtentQualifier());
		}
	}

	public void testGetExtentValue() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(component.getExtentValue(), component.getExtent().getValue());
		}
	}

	public void testGetExtentValueNoExtent() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, TEST_MIME_TYPE, null, null);
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
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Format component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			
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
				// Good
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
