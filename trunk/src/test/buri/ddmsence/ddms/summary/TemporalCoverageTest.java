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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:temporalCoverage elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TemporalCoverageTest extends AbstractBaseTestCase {

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
	 * Returns a fixture object for testing.
	 */
	public static TemporalCoverage getFixture() {
		try {
			return (new TemporalCoverage(null, "1979-09-15", "Not Applicable", null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private TemporalCoverage getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		TemporalCoverage component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture().addTo(element);
			component = new TemporalCoverage(element);
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
	 * @param timePeriodName the time period name (optional)
	 * @param startString a string representation of the date (required)
	 * @param endString a string representation of the end date (required)
	 * @return a valid object
	 */
	private TemporalCoverage getInstance(String message, String timePeriodName, String startString, String endString) {
		boolean expectFailure = !Util.isEmpty(message);
		TemporalCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture();
			component = new TemporalCoverage(timePeriodName, startString, endString, attr);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to manage the deprecated TimePeriod wrapper element
	 * 
	 * @param innerElement the element containing the guts of this component
	 * @return the element itself in DDMS 4.0 or later, or the element wrapped in another element
	 */
	private Element wrapInnerElement(Element innerElement) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String name = TemporalCoverage.getName(version);
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
		String prefix = "temporalCoverage.";
		if (!version.isAtLeast("4.0"))
			prefix += "TimePeriod.";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "name", TEST_NAME));
		text.append(buildOutput(isHTML, prefix + "start", TEST_START));
		text.append(buildOutput(isHTML, prefix + "end", TEST_END));
		if (version.isAtLeast("3.0"))
			text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, prefix, ""));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:temporalCoverage ").append(getXmlnsDDMS());
		if (version.isAtLeast("3.0"))
			xml.append(" ").append(getXmlnsISM()).append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(">\n\t");
		if (version.isAtLeast("4.0")) {
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				TemporalCoverage.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance(SUCCESS, wrapInnerElement(periodElement));

			// No optional fields, empty name element rather than no name element
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("name", ""));
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance(SUCCESS, wrapInnerElement(periodElement));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_NAME, TEST_START, TEST_END);

			// No optional fields
			getInstance(SUCCESS, "", TEST_START, TEST_END);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing start
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance("Exactly 1 start element must exist.", wrapInnerElement(periodElement));

			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			getInstance("Exactly 1 end element must exist.", wrapInnerElement(periodElement));

			// Wrong date format (using xs:gDay here)
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance("The date datatype must be one of", wrapInnerElement(periodElement));

			// Wrong extended value
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "N/A"));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance("If no date is specified,", wrapInnerElement(periodElement));

			// Bad range
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "2004"));
			periodElement.appendChild(Util.buildDDMSElement("end", "2003"));
			getInstance("The start date is after the end date.", wrapInnerElement(periodElement));
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Wrong date format (using xs:gDay here)
			getInstance("The date datatype must be one of", TEST_NAME, "---31", TEST_END);

			// Wrong extended value
			getInstance("If no date is specified,", TEST_NAME, "N/A", TEST_END);

			// Bad range
			getInstance("The start date is after the end date.", TEST_NAME, "2004", "2003");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty name element
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("name", null));
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			component = getInstance(SUCCESS, wrapInnerElement(periodElement));
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ddms:name element was found with no value.";
			String locator = version.isAtLeast("4.0") ? "ddms:temporalCoverage"
				: "ddms:temporalCoverage/ddms:TimePeriod";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_START, TEST_END);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage dataComponent = getInstance(SUCCESS, "", TEST_START, TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, "Not Applicable", TEST_END);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_START, "2050");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_NAME, TEST_START, TEST_END);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testValidateExtendedDateTypeSuccess() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			try {
				TemporalCoverage.validateExtendedDateType("Not Applicable");
			}
			catch (InvalidDDMSException e) {
				fail("Did not allow valid data.");
			}
		}
	}

	public void testValidateExtendedDateTypeFailure() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			try {
				TemporalCoverage.validateExtendedDateType("N/A");
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "If no date is specified,");
			}
		}
	}

	public void testDefaultValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, "", "", "");
			assertEquals("Unknown", component.getTimePeriodName());
			assertEquals("Unknown", component.getStartString());
			assertEquals("Unknown", component.getEndString());
		}
	}

	public void testDateEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, TEST_NAME, TEST_START, "2050");
			assertEquals(component.getStart().toXMLFormat(), component.getStartString());
			assertEquals(component.getEnd().toXMLFormat(), component.getEndString());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
			TemporalCoverage component = new TemporalCoverage(TEST_NAME, TEST_START, TEST_END, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new TemporalCoverage(TEST_NAME, TEST_START, TEST_END, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage.Builder builder = new TemporalCoverage.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TemporalCoverage.Builder builder = new TemporalCoverage.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setStartString(TEST_START);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TemporalCoverage.Builder builder = new TemporalCoverage.Builder();
			builder.setStartString("Invalid");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "If no date is specified");
			}
			builder.setStartString("2001");
			builder.setEndString("2002");
			builder.commit();
		}
	}
}