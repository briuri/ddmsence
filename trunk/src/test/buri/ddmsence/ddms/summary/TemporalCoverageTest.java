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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.ApproximableDate;
import buri.ddmsence.ddms.ApproximableDateTest;
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
	 * Generates an approximableDate for testing
	 */
	private ApproximableDate getApproximableStart(boolean includeAllFields) throws InvalidDDMSException {
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			return (new ApproximableDate(ApproximableDateTest.getFixtureElement("approximableStart", includeAllFields)));
		}
		return (null);
	}

	/**
	 * Generates an approximableDate for testing
	 */
	private ApproximableDate getApproximableEnd(boolean includeAllFields) throws InvalidDDMSException {
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			return (new ApproximableDate(ApproximableDateTest.getFixtureElement("approximableEnd", includeAllFields)));
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
	 * @param startString a string representation of the date (optional) (if empty, defaults to Unknown)
	 * @param approximableStart the start date, as an approximable date (optional)
	 * @param endString a string representation of the end date (optional) (if empty, defaults to Unknown)
	 * @param approximableEnd the end date, as an approximable date (optional)
	 * @return a valid object
	 */
	private TemporalCoverage getInstance(String message, String timePeriodName, String startString,
		ApproximableDate approximableStart, String endString, ApproximableDate approximableEnd) {
		boolean expectFailure = !Util.isEmpty(message);
		TemporalCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture();
			if (approximableStart != null && approximableEnd != null)
				component = new TemporalCoverage(timePeriodName, approximableStart, approximableEnd, attr);
			else if (approximableStart != null && approximableEnd == null)
				component = new TemporalCoverage(timePeriodName, approximableStart, endString, attr);
			else if (approximableStart == null && approximableEnd != null)
				component = new TemporalCoverage(timePeriodName, startString, approximableEnd, attr);
			else
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
	 * @return the element itself in DDMS 4.0.1 or later, or the element wrapped in another element
	 */
	private Element wrapInnerElement(Element innerElement) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String name = TemporalCoverage.getName(version);
		if (version.isAtLeast("4.0.1")) {
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
	private String getExpectedOutput(boolean isHTML, boolean isApproximable) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = "temporalCoverage.";
		if (!version.isAtLeast("4.0.1"))
			prefix += "TimePeriod.";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "name", TEST_NAME));
		if (isApproximable) {
			ApproximableDate start = new ApproximableDate(ApproximableDateTest.getFixtureElement("approximableStart",
				true));
			ApproximableDate end = new ApproximableDate(ApproximableDateTest.getFixtureElement("approximableEnd", true));
			text.append(start.getOutput(isHTML, prefix, ""));
			text.append(end.getOutput(isHTML, prefix, ""));
		}
		else {
			text.append(buildOutput(isHTML, prefix + "start", TEST_START));
			text.append(buildOutput(isHTML, prefix + "end", TEST_END));
		}
		if (version.isAtLeast("3.0"))
			text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, prefix));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput(boolean isApproximable) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:temporalCoverage ").append(getXmlnsDDMS());
		if (version.isAtLeast("3.0"))
			xml.append(" ").append(getXmlnsISM()).append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(">");
		if (!version.isAtLeast("4.0.1"))
			xml.append("<ddms:TimePeriod>");
		xml.append("<ddms:name>").append(TEST_NAME).append("</ddms:name>");
		if (isApproximable) {
			xml.append("<ddms:approximableStart>");
			xml.append("<ddms:description>description</ddms:description>");
			xml.append("<ddms:approximableDate ddms:approximation=\"1st qtr\">2012</ddms:approximableDate>");
			xml.append("<ddms:searchableDate><ddms:start>2012-01</ddms:start>");
			xml.append("<ddms:end>2012-03-31</ddms:end></ddms:searchableDate>");
			xml.append("</ddms:approximableStart>");
			xml.append("<ddms:approximableEnd>");
			xml.append("<ddms:description>description</ddms:description>");
			xml.append("<ddms:approximableDate ddms:approximation=\"1st qtr\">2012</ddms:approximableDate>");
			xml.append("<ddms:searchableDate><ddms:start>2012-01</ddms:start>");
			xml.append("<ddms:end>2012-03-31</ddms:end></ddms:searchableDate>");
			xml.append("</ddms:approximableEnd>");
		}
		else {
			xml.append("<ddms:start>").append(TEST_START).append("</ddms:start>");
			xml.append("<ddms:end>").append(TEST_END).append("</ddms:end>");
		}
		if (!version.isAtLeast("4.0.1"))
			xml.append("</ddms:TimePeriod>");
		xml.append("</ddms:temporalCoverage>");
		return (xml.toString());
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
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields, exact-exact
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance(SUCCESS, wrapInnerElement(periodElement));

			// All fields, approx-approx
			if (version.isAtLeast("4.1")) {
				periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", false));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", false));
				getInstance(SUCCESS, wrapInnerElement(periodElement));
			}

			// No optional fields, empty name element rather than no name element
			periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("name", ""));
			periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance(SUCCESS, wrapInnerElement(periodElement));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields, exact-exact
			getInstance(SUCCESS, TEST_NAME, TEST_START, null, TEST_END, null);

			if (version.isAtLeast("4.1")) {
				// All fields, exact-approx
				getInstance(SUCCESS, TEST_NAME, TEST_START, null, null, getApproximableEnd(false));

				// All fields, approx-exact
				getInstance(SUCCESS, TEST_NAME, null, getApproximableStart(false), TEST_END, null);

				// All fields, approx-approx
				getInstance(SUCCESS, TEST_NAME, null, getApproximableStart(false), null, getApproximableEnd(false));
			}

			// No optional fields
			getInstance(SUCCESS, "", TEST_START, null, TEST_END, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Wrong date format (using xs:gDay here)
			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", "---31"));
			periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
			getInstance("The date datatype must be one of", wrapInnerElement(periodElement));
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Wrong date format (using xs:gDay here)
			getInstance("The date datatype must be one of", TEST_NAME, "---31", null, TEST_END, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings, exact-exact
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty name element
			if (!version.isAtLeast("5.0")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", null));
				periodElement.appendChild(Util.buildDDMSElement("start", TEST_START));
				periodElement.appendChild(Util.buildDDMSElement("end", TEST_END));
				component = getInstance(SUCCESS, wrapInnerElement(periodElement));
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:name element was found with no value.";
				String locator = version.isAtLeast("4.0.1") ? "ddms:temporalCoverage"
					: "ddms:temporalCoverage/ddms:TimePeriod";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}

			// 4.1 ddms:approximableStart/End element used
			if ("4.1".equals(sVersion)) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", true));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", true));
				component = getInstance(SUCCESS, wrapInnerElement(periodElement));
				assertEquals(1, component.getValidationWarnings().size());
				String text = "The ddms:approximableStart or ddms:approximableEnd element";
				String locator = "ddms:temporalCoverage";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// exact-exact
			TemporalCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_START, null, TEST_END, null);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// approx-approx
			if (version.isAtLeast("4.1")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", false));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", false));
				elementComponent = getInstance(SUCCESS, wrapInnerElement(periodElement));
				dataComponent = getInstance(SUCCESS, null, null, getApproximableStart(false), null,
					getApproximableEnd(false));
				assertEquals(elementComponent, dataComponent);
				assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
			}
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage dataComponent = getInstance(SUCCESS, "", TEST_START, null, TEST_END, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, "Not Applicable", null, TEST_END, null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_START, null, "2050", null);
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.1")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", false));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", false));
				elementComponent = getInstance(SUCCESS, wrapInnerElement(periodElement));
				dataComponent = getInstance(SUCCESS, TEST_NAME, null, getApproximableStart(false), null,
					getApproximableEnd(false));
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, null, null, getApproximableStart(false), TEST_END, null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, null, TEST_START, null, null, getApproximableEnd(false));
				assertFalse(elementComponent.equals(dataComponent));
			}
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
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true, false), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());

			component = getInstance(SUCCESS, TEST_NAME, TEST_START, null, TEST_END, null);
			assertEquals(getExpectedOutput(true, false), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());

			if (version.isAtLeast("4.1")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", TEST_NAME));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", true));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", true));
				component = getInstance(SUCCESS, wrapInnerElement(periodElement));
				assertEquals(getExpectedOutput(true, true), component.toHTML());
				assertEquals(getExpectedOutput(false, true), component.toText());

				component = getInstance(SUCCESS, TEST_NAME, null, getApproximableStart(true), null,
					getApproximableEnd(true));
				assertEquals(getExpectedOutput(true, true), component.toHTML());
				assertEquals(getExpectedOutput(false, true), component.toText());
			}
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = getInstance(SUCCESS, TEST_NAME, TEST_START, null, TEST_END, null);
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			if (version.isAtLeast("4.1")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", TEST_NAME));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", true));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", true));
				component = getInstance(SUCCESS, wrapInnerElement(periodElement));
				assertEquals(getExpectedXMLOutput(true), component.toXML());

				component = getInstance(SUCCESS, TEST_NAME, null, getApproximableStart(true), null,
					getApproximableEnd(true));
				assertEquals(getExpectedXMLOutput(true), component.toXML());
			}
		}
	}

	public void testDefaultValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", ""));
			periodElement.appendChild(Util.buildDDMSElement("end", ""));
			TemporalCoverage component = getInstance(SUCCESS, wrapInnerElement(periodElement));
			assertEquals("Unknown", component.getStartString());
			assertEquals("Unknown", component.getEndString());

			component = getInstance(SUCCESS, "", "", null, "", null);
			assertEquals("Unknown", component.getTimePeriodName());
			assertEquals("Unknown", component.getStartString());
			assertEquals("Unknown", component.getEndString());

		}
	}

	public void testDeprecatedAccessors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Element periodElement = Util.buildDDMSElement("TimePeriod", null);
			periodElement.appendChild(Util.buildDDMSElement("start", ""));
			periodElement.appendChild(Util.buildDDMSElement("end", ""));
			TemporalCoverage component = getInstance(SUCCESS, wrapInnerElement(periodElement));
			assertNull(component.getStart());
			assertNull(component.getEnd());

			component = getInstance(SUCCESS, "", TEST_START, null, TEST_START, null);
			assertEquals(TEST_START, component.getStart().toXMLFormat());
			assertEquals(TEST_START, component.getEnd().toXMLFormat());
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

	public void testWrongVersionApproximable() {
		try {
			DDMSVersion.setCurrentVersion("4.1");
			ApproximableDate start = getApproximableStart(false);
			DDMSVersion.setCurrentVersion("3.0");
			new TemporalCoverage(null, start, TEST_END, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Approximable dates cannot be used until DDMS 4.1 or later.");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			TemporalCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			TemporalCoverage.Builder builder = new TemporalCoverage.Builder(component);
			assertEquals(component, builder.commit());

			if (version.isAtLeast("4.1")) {
				Element periodElement = Util.buildDDMSElement("TimePeriod", null);
				periodElement.appendChild(Util.buildDDMSElement("name", TEST_NAME));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableStart", true));
				periodElement.appendChild(ApproximableDateTest.getFixtureElement("approximableEnd", true));
				component = getInstance(SUCCESS, wrapInnerElement(periodElement));
				builder = new TemporalCoverage.Builder(component);
				assertEquals(component, builder.commit());
			}
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
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			TemporalCoverage.Builder builder = new TemporalCoverage.Builder();
			builder.setStartString("Invalid");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The date datatype");
			}
			builder.setStartString("2001");
			builder.setEndString("2002");
			builder.commit();

			if (version.isAtLeast("4.1")) {
				builder.getApproximableStart().setDescription("test");
				try {
					builder.commit();
					fail("Builder allowed invalid data.");
				}
				catch (InvalidDDMSException e) {
					expectMessage(e, "Only 1 of start or approximableStart");
				}

				builder.getApproximableStart().setDescription(null);
				builder.getApproximableEnd().setDescription("test");
				try {
					builder.commit();
					fail("Builder allowed invalid data.");
				}
				catch (InvalidDDMSException e) {
					expectMessage(e, "Only 1 of end or approximableEnd");
				}

				builder.getApproximableStart().setDescription("test");
				builder.setStartString(null);
				builder.setEndString(null);
				builder.commit();
			}
		}
	}
}