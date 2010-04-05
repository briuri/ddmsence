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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
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
			if (!DDMSVersion.isCurrentVersion("2.0"))
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
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0")) ? null : SecurityAttributesTest
				.getFixture(false);
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
		html.append("<meta name=\"temporal.TimePeriod\" content=\"").append(TEST_NAME).append("\" />\n");
		html.append("<meta name=\"temporal.DateStart\" content=\"").append(TEST_START).append("\" />\n");
		html.append("<meta name=\"temporal.DateEnd\" content=\"").append(TEST_END).append("\" />\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			html.append("<meta name=\"temporal.classification\" content=\"U\" />\n");
			html.append("<meta name=\"temporal.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Time Period: ").append(TEST_NAME).append("\n");
		text.append("Date Start: ").append(TEST_START).append("\n");
		text.append("Date End: ").append(TEST_END).append("\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			text.append("Time Period Classification: U\n");
			text.append("Time Period ownerProducer: USA\n");
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
		xml.append("<ddms:temporalCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace()).append("\"");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			xml.append(" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIcismNamespace()).append(
				"\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t");
		xml.append("<ddms:TimePeriod>\n\t\t");
		xml.append("<ddms:name>").append(TEST_NAME).append("</ddms:name>\n\t\t");
		xml.append("<ddms:start>").append(TEST_START).append("</ddms:start>\n\t\t");
		xml.append("<ddms:end>").append(TEST_END).append("</ddms:end>\n\t");
		xml.append("</ddms:TimePeriod>\n");
		xml.append("</ddms:temporalCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(TemporalCoverage.NAME, component.getName());
			assertEquals(Util.DDMS_PREFIX, component.getPrefix());
			assertEquals(Util.DDMS_PREFIX + ":" + TemporalCoverage.NAME, component.getQualifiedName());

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
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			Element element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields, empty name element rather than no name element
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("name", ""));
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", TEST_START, TEST_END);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing start
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			Element element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_FAIL, element);

			// Missing end
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_FAIL, element);

			// Wrong date format (using xs:gDay here)
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_FAIL, element);

			// Wrong extended value
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "N/A"));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_FAIL, element);

			// Bad range
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "2004"));
			periodElement.appendChild(Util.buildDDMSElement("end", "2003"));
			element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Wrong date format (using xs:gDay here)
			testConstructor(WILL_FAIL, TEST_NAME, "---31", TEST_END);

			// Wrong extended value
			testConstructor(WILL_FAIL, TEST_NAME, "N/A", TEST_END);

			// Bad range
			testConstructor(WILL_FAIL, TEST_NAME, "2004", "2003");
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty name element
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("name", null));
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			Element element = Util.buildDDMSElement(TemporalCoverage.NAME, null);
			element.appendChild(periodElement);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A ddms:name element was found with no value. Defaulting to \"Unknown\".", component
				.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:temporalCoverage/ddms:TimePeriod", component.getValidationWarnings().get(0)
				.getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			TemporalCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			TemporalCoverage dataComponent = testConstructor(WILL_SUCCEED, "", TEST_START, TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, "Not Applicable", TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, "2050");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testValidateExtendedDateTypeSuccess() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			try {
				TemporalCoverage.validateExtendedDateType("Not Applicable");
			} catch (InvalidDDMSException e) {
				fail("Did not allow valid data.");
			}
		}
	}

	public void testValidateExtendedDateTypeFailure() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			try {
				TemporalCoverage.validateExtendedDateType("N/A");
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testDefaultValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, "", "", "");
			assertEquals("Unknown", component.getTimePeriodName());
			assertEquals("Unknown", component.getStartString());
			assertEquals("Unknown", component.getEndString());
		}
	}

	public void testDateEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			TemporalCoverage component = testConstructor(WILL_SUCCEED, TEST_NAME, TEST_START, "2050");
			assertEquals(component.getStart().toXMLFormat(), component.getStartString());
			assertEquals(component.getEnd().toXMLFormat(), component.getEndString());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);			
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0") ? null : SecurityAttributesTest.getFixture(false));
			TemporalCoverage component = new TemporalCoverage(TEST_NAME, TEST_START, TEST_END, attr);
			if (DDMSVersion.isCurrentVersion("2.0"))
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
}