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
package buri.ddmsence.ddms;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to elements of type ddms:ApproximableDateType (includes ddms:acquiredOn, and the ddms:start / ddms:end values in a ddms:temporalCoverage element.</p>
 * 
 * <p> Because these are local components, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.1.0
 */
public class ApproximableDateTest extends AbstractBaseTestCase {

	private static final String TEST_NAME = "acquiredOn";
	private static final String TEST_DESCRIPTION = "description";
	private static final String TEST_APPROXIMABLE_DATE = "2012";
	private static final String TEST_APPROXIMATION = "1st qtr";
	private static final String TEST_START_DATE = "2012-01";
	private static final String TEST_END_DATE = "2012-03-31";
	
	/**
	 * Constructor
	 */
	public ApproximableDateTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1 4.0.1");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @param name the element name
	 * @param includeAllFields true to include optional fields
	 */
	public static Element getFixtureElement(String name, boolean includeAllFields) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(name, null);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		if (includeAllFields) {
			Util.addDDMSChildElement(element, "description", TEST_DESCRIPTION);
			
			Element approximableElment = Util.buildDDMSElement("approximableDate", TEST_APPROXIMABLE_DATE);
			Util.addDDMSAttribute(approximableElment, "approximation", TEST_APPROXIMATION);
			element.appendChild(approximableElment);
			
			Element searchableElement = Util.buildDDMSElement("searchableDate", null);
			Util.addDDMSChildElement(searchableElement, "start", TEST_START_DATE);
			Util.addDDMSChildElement(searchableElement, "end", TEST_END_DATE);
			element.appendChild(searchableElement);
		}
		return (element);
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ApproximableDate getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ApproximableDate component = null;
		try {
			component = new ApproximableDate(element);
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
	 * @param name the name of the element
	 * @param description the description of this approximable date (optional)
	 * @param approximableDate the value of the approximable date (optional)
	 * @param approximation an attribute that decorates the date (optional)
	 * @param searchableStartDate the lower bound for this approximable date (optional)
	 * @param searchableEndDate the upper bound for this approximable date (optional)
	 * @param entity the person or organization in this role
	 * @param org the organization
	 */
	private ApproximableDate getInstance(String message, String name, String description, String approximableDate, String approximation,
		String searchableStartDate, String searchableEndDate) {
		boolean expectFailure = !Util.isEmpty(message);
		ApproximableDate component = null;
		try {
			component = new ApproximableDate(name, description, approximableDate, approximation, searchableStartDate, searchableEndDate);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}
	
	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "acquiredOn.description", TEST_DESCRIPTION));
		text.append(buildOutput(isHTML, "acquiredOn.approximableDate", TEST_APPROXIMABLE_DATE));
		text.append(buildOutput(isHTML, "acquiredOn.approximableDate.approximation", TEST_APPROXIMATION));
		text.append(buildOutput(isHTML, "acquiredOn.searchableDate.start", TEST_START_DATE));
		text.append(buildOutput(isHTML, "acquiredOn.searchableDate.end", TEST_END_DATE));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:acquiredOn ").append(getXmlnsDDMS()).append(">");
		xml.append("<ddms:description>").append(TEST_DESCRIPTION).append("</ddms:description>");
		xml.append("<ddms:approximableDate ddms:approximation=\"").append(TEST_APPROXIMATION).append("\">");
		xml.append(TEST_APPROXIMABLE_DATE).append("</ddms:approximableDate>");
		xml.append("<ddms:searchableDate><ddms:start>").append(TEST_START_DATE).append("</ddms:start>");
		xml.append("<ddms:end>").append(TEST_END_DATE).append("</ddms:end></ddms:searchableDate>");
		xml.append("</ddms:acquiredOn>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement(TEST_NAME, true)), DEFAULT_DDMS_PREFIX, TEST_NAME);
			getInstance("The element name must be one of", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));

			// No fields
			getInstance(SUCCESS, getFixtureElement(TEST_NAME, false));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION, TEST_START_DATE, TEST_END_DATE);

			// No fields
			getInstance(SUCCESS, TEST_NAME, null, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			 
			 // Wrong date format: approximableDate
			Element element = Util.buildDDMSElement(TEST_NAME, null);
			Element approximableElment = Util.buildDDMSElement("approximableDate", "---31");
			element.appendChild(approximableElment);
			getInstance("The date datatype", element);			
			
			 // Invalid approximation
			element = Util.buildDDMSElement(TEST_NAME, null);
			approximableElment = Util.buildDDMSElement("approximableDate", TEST_APPROXIMABLE_DATE);
			Util.addDDMSAttribute(approximableElment, "approximation", "almost-nearly");
			element.appendChild(approximableElment);
			getInstance("The approximation must be one of", element);	
			 
			 // Wrong date format: start
			element = Util.buildDDMSElement(TEST_NAME, null);
			Element searchableElement = Util.buildDDMSElement("searchableDate", null);
			Util.addDDMSChildElement(searchableElement, "start", "---31");
			Util.addDDMSChildElement(searchableElement, "end", TEST_END_DATE);
			element.appendChild(searchableElement);
			getInstance("The date datatype", element);	
			
			 // Wrong date format: end
			element = Util.buildDDMSElement(TEST_NAME, null);
			searchableElement = Util.buildDDMSElement("searchableDate", null);
			Util.addDDMSChildElement(searchableElement, "start", TEST_START_DATE);
			Util.addDDMSChildElement(searchableElement, "end", "---31");
			element.appendChild(searchableElement);
			getInstance("The date datatype", element);	
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			 // Wrong date format: approximableDate
			getInstance("The date datatype", TEST_NAME, TEST_DESCRIPTION, "---31", TEST_APPROXIMATION, TEST_START_DATE, TEST_END_DATE);	
			
			 // Invalid approximation
			getInstance("The approximation", TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, "almost-nearly", TEST_START_DATE, TEST_END_DATE);
			 
			 // Wrong date format: start
			getInstance("The date datatype", TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION, "---31", TEST_END_DATE);
			
			 // Wrong date format: end
			getInstance("The date datatype", TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION, TEST_START_DATE, "---31");
		}
	}
	
	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ApproximableDate component = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			assertEquals(0, component.getValidationWarnings().size());
			
			// Empty element
			component = getInstance(SUCCESS, getFixtureElement(TEST_NAME, false));
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:acquiredOn";
			String locator = "ddms:acquiredOn";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			
			// Description element with no child text
			if (!DDMSVersion.getCurrentVersion().isAtLeast("5.0")) {
				Element element = Util.buildDDMSElement(TEST_NAME, null);
				element.appendChild(Util.buildDDMSElement("description", null));
				Util.addDDMSChildElement(element, "description", null);
				Util.addDDMSChildElement(element, "approximableDate", TEST_APPROXIMABLE_DATE);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				text = "A completely empty ddms:description";
				locator = "ddms:acquiredOn";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate elementComponent = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			ApproximableDate dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE,
				TEST_APPROXIMATION, TEST_START_DATE, TEST_END_DATE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate elementComponent = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			ApproximableDate dataComponent = getInstance(SUCCESS, "approximableStart", TEST_DESCRIPTION,
				TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION, TEST_START_DATE, TEST_END_DATE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, DIFFERENT_VALUE, TEST_APPROXIMABLE_DATE,
				TEST_APPROXIMATION, TEST_START_DATE, TEST_END_DATE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, "2000", TEST_APPROXIMATION,
				TEST_START_DATE, TEST_END_DATE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, "2nd qtr",
				TEST_START_DATE, TEST_END_DATE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE,
				TEST_APPROXIMATION, "2000", TEST_END_DATE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE,
				TEST_APPROXIMATION, TEST_START_DATE, "2500");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate component = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION,
				TEST_START_DATE, TEST_END_DATE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate component = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_NAME, TEST_DESCRIPTION, TEST_APPROXIMABLE_DATE, TEST_APPROXIMATION,
				TEST_START_DATE, TEST_END_DATE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new ApproximableDate(TEST_NAME, null, null, null, null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The acquiredOn element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate component = getInstance(SUCCESS, getFixtureElement(TEST_NAME, true));
			ApproximableDate.Builder builder = new ApproximableDate.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate.Builder builder = new ApproximableDate.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setDescription(TEST_DESCRIPTION);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ApproximableDate.Builder builder = new ApproximableDate.Builder();
			builder.setName(TEST_NAME);
			builder.setApproximableDate(TEST_APPROXIMABLE_DATE);
			builder.setApproximation("almost-nearly");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The approximation");
			}
			builder.setApproximation(TEST_APPROXIMATION);
			builder.commit();
		}
	}
}
