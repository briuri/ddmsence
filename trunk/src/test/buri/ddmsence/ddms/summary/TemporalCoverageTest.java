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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:temporalCoverage elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TemporalCoverageTest extends AbstractComponentTestCase {

	private static final String TEST_NAME = "My Time Period";
	private static final String TEST_START = "1979-09-15";
	private static final String TEST_END = "Not Applicable";

	/**
	 * Constructor
	 */
	public TemporalCoverageTest() {
		super("temporalCoverage.xml");
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private TemporalCoverage testConstructor(boolean expectFailure, Element element) {
		TemporalCoverage component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture(false).addTo(element);
			component = new TemporalCoverage(element);
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
	 * @param timePeriodName the time period name (optional)
	 * @param startString a string representation of the date (required)
	 * @param endString a string representation of the end date (required)
	 * @return a valid object
	 */
	private TemporalCoverage testConstructor(boolean expectFailure, String timePeriodName, String startString,
		String endString) {
		TemporalCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null 
				: SecurityAttributesTest.getFixture(false);
			component = new TemporalCoverage(timePeriodName, startString, endString, attr);
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
		html.append("<meta name=\"temporalCoverage.TimePeriod.name\" content=\"").append(TEST_NAME).append("\" />\n");
		html.append("<meta name=\"temporalCoverage.TimePeriod.start\" content=\"").append(TEST_START).append("\" />\n");
		html.append("<meta name=\"temporalCoverage.TimePeriod.end\" content=\"").append(TEST_END).append("\" />\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			html.append("<meta name=\"temporalCoverage.classification\" content=\"U\" />\n");
			html.append("<meta name=\"temporalCoverage.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("TimePeriod name: ").append(TEST_NAME).append("\n");
		text.append("TimePeriod start: ").append(TEST_START).append("\n");
		text.append("TimePeriod end: ").append(TEST_END).append("\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append("TimePeriod classification: U\n");
			text.append("TimePeriod ownerProducer: USA\n");
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:temporalCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			xml.append(" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIsmNamespace())
				.append("\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t");
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
			xml.append("<ddms:name>").append(TEST_NAME).append("</ddms:name>\n\t");
			xml.append("<ddms:start>").append(TEST_START).append("</ddms:start>\n\t");
			xml.append("<ddms:end>").append(TEST_END).append("</ddms:end>\n");
		}
		else {
			xml.append("<ddms:TimePeriod>\n\t\t");
			xml.append("<ddms:name>").append(TEST_NAME).append("</ddms:name>\n\t\t");
			xml.append("<ddms:start>").append(TEST_START).append("</ddms:start>\n\t\t");
			xml.append("<ddms:end>").append(TEST_END).append("</ddms:end>\n\t");
			xml.append("</ddms:TimePeriod>\n");
		}
		xml.append("</ddms:temporalCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(TemporalCoverage.getName(DDMSVersion.getCurrentVersion()), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + TemporalCoverage.getName(DDMSVersion.getCurrentVersion()), component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String coverageName = TemporalCoverage.getName(DDMSVersion.getCurrentVersion());
			
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_SUCCEED, periodElement);
			}
			else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_SUCCEED, element);
			}

			// No optional fields, empty name element rather than no name element
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("name", ""));
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_SUCCEED, periodElement);
			}
			else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", ""));
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_SUCCEED, element);
			}
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", TEST_START, TEST_END);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String coverageName = TemporalCoverage.getName(DDMSVersion.getCurrentVersion());
			
			// Missing start
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_FAIL, periodElement);
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_FAIL, element);
			}

			// Missing end
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				testConstructor(WILL_FAIL, periodElement);
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_FAIL, element);
			}

			// Wrong date format (using xs:gDay here)
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_FAIL, periodElement);
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_FAIL, element);
			}

			// Wrong extended value
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_FAIL, periodElement);
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("start", "N/A"));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_FAIL, element);
			}

			// Bad range
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				testConstructor(WILL_FAIL, periodElement);
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("start", "2004"));
				periodElement.appendChild(Util.buildDDMSElement("end", "2003"));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				testConstructor(WILL_FAIL, element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Wrong date format (using xs:gDay here)
			testConstructor(WILL_FAIL, TEST_NAME, "---31", TEST_END);

			// Wrong extended value
			testConstructor(WILL_FAIL, TEST_NAME, "N/A", TEST_END);

			// Bad range
			testConstructor(WILL_FAIL, TEST_NAME, "2004", "2003");
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String coverageName = TemporalCoverage.getName(DDMSVersion.getCurrentVersion());
			
			// No warnings
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty name element
			if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
				Element periodElement = Util.buildDDMSElement(coverageName, null);
				periodElement.appendChild(Util.buildDDMSElement("name", null));
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				component = testConstructor(WILL_SUCCEED, periodElement);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("A ddms:name element was found with no value. Defaulting to \"Unknown\".", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:temporalCoverage", 
					component.getValidationWarnings().get(0).getLocator());
			} else {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", null));
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				Element element = Util.buildDDMSElement(coverageName, null);
				element.appendChild(periodElement);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("A ddms:name element was found with no value. Defaulting to \"Unknown\".", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:temporalCoverage/ddms:TimePeriod", 
					component.getValidationWarnings().get(0).getLocator());
			}
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			TemporalCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			TemporalCoverage dataComponent = testConstructor(WILL_SUCCEED, "", TEST_START, TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, "Not Applicable", TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, "2050");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testValidateExtendedDateTypeSuccess() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			try {
				TemporalCoverage.validateExtendedDateType("Not Applicable");
			} catch (InvalidDDMSException e) {
				fail("Did not allow valid data.");
			}
		}
	}

	public void testValidateExtendedDateTypeFailure() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			try {
				TemporalCoverage.validateExtendedDateType("N/A");
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testDefaultValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, "", "", "");
			assertEquals("Unknown", component.getTimePeriodName());
			assertEquals("Unknown", component.getStartString());
			assertEquals("Unknown", component.getEndString());
		}
	}

	public void testDateEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, "2050");
			assertEquals(component.getStart().toXMLFormat(), component.getStartString());
			assertEquals(component.getEnd().toXMLFormat(), component.getEndString());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);			
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			TemporalCoverage component = new TemporalCoverage(TEST_NAME, TEST_START, TEST_END, attr);
			if (!DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}
	
	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new TemporalCoverage(TEST_NAME, TEST_START, TEST_END, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			
			// Equality after Building
			TemporalCoverage.Builder builder = new TemporalCoverage.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new TemporalCoverage.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new TemporalCoverage.Builder();
			builder.setStartString("Invalid");
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