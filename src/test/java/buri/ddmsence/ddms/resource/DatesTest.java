/* Copyright 2010 - 2019 by Brian Uri!
   
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
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.ddms.resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.ApproximableDate;
import buri.ddmsence.ddms.ApproximableDateTest;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:source elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DatesTest extends AbstractBaseTestCase {

	private static final String TEST_CREATED = "2003";
	private static final String TEST_POSTED = "2003-02";
	private static final String TEST_VALID = "2003-02-15";
	private static final String TEST_CUTOFF = "2001-10-31T17:00:00Z";
	private static final String TEST_APPROVED = "2003-02-16";
	private static final String TEST_RECEIVED = "2003-02-17";

	/**
	 * Constructor
	 */
	public DatesTest() {
		super("dates.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Dates getFixture() {
		try {
			return (new Dates(null, "2003", null, null, null, null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Generates an approvedOn Date for testing
	 */
	private String getTestApprovedOn() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("3.1") ? TEST_APPROVED : "");
	}

	/**
	 * Generates a receivedOn Date for testing
	 */
	private String getTestReceivedOn() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? TEST_RECEIVED : "");
	}

	/**
	 * Generates an acquiredOn Date for testing
	 */
	private List<ApproximableDate> getTestAcquiredOns() throws InvalidDDMSException {
		List<ApproximableDate> list = new ArrayList<ApproximableDate>();
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			list.add(new ApproximableDate(ApproximableDateTest.getFixtureElement("acquiredOn", true)));
		}
		return (list);
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param element the element to build from
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Dates getInstance(Element element, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		Dates component = null;
		try {
			component = new Dates(element);
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
	 * @param builder the builder to commit
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private Dates getInstance(Dates.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		Dates component = null;
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
	 * Returns a builder, pre-populated with base data from the XML sample.
	 * 
	 * This builder can then be modified to test various conditions.
	 */
	private Dates.Builder getBaseBuilder() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Dates component = getInstance(getValidElement(version.getVersion()), SUCCESS);
		return (new Dates.Builder(component));
	}

	/**
	 * Returns the expected output for the test instance of this component
	 */
	private String getExpectedHTMLTextOutput(OutputFormat format) throws InvalidDDMSException {
		Util.requireHTMLText(format);
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("4.1")) {
			for (ApproximableDate acquiredOn : getTestAcquiredOns()) {
				text.append(acquiredOn.getHTMLTextOutput(format, "dates.", ""));
			}
		}
		text.append(buildHTMLTextOutput(format, "dates.created", TEST_CREATED));
		text.append(buildHTMLTextOutput(format, "dates.posted", TEST_POSTED));
		text.append(buildHTMLTextOutput(format, "dates.validTil", TEST_VALID));
		text.append(buildHTMLTextOutput(format, "dates.infoCutOff", TEST_CUTOFF));
		if (version.isAtLeast("3.1"))
			text.append(buildHTMLTextOutput(format, "dates.approvedOn", TEST_APPROVED));
		if (version.isAtLeast("4.0.1"))
			text.append(buildHTMLTextOutput(format, "dates.receivedOn", TEST_RECEIVED));
		return (text.toString());
	}

	/**
	 * Returns the expected JSON output for this unit test
	 */
	private String getExpectedJSONOutput() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer json = new StringBuffer();
		json.append("{");
		if (version.isAtLeast("4.1")) {
			json.append("\"acquiredOn\":[");
			for (ApproximableDate acquiredOn : getTestAcquiredOns()) {
				json.append(acquiredOn.toJSON());
			}
			json.append("],");
		}
		json.append("\"created\":\"2003\",");
		json.append("\"posted\":\"2003-02\",");
		json.append("\"validTil\":\"2003-02-15\",");
		json.append("\"infoCutOff\":\"2001-10-31T17:00:00Z\"");
		if (version.isAtLeast("3.1"))
			json.append(",\"approvedOn\":\"2003-02-16\"");
		if (version.isAtLeast("4.0.1"))
			json.append(",\"receivedOn\":\"2003-02-17\"");
		json.append("}");		
		return (json.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:dates ").append(getXmlnsDDMS()).append(" ");
		xml.append("ddms:created=\"").append(TEST_CREATED).append("\" ");
		xml.append("ddms:posted=\"").append(TEST_POSTED).append("\" ");
		xml.append("ddms:validTil=\"").append(TEST_VALID).append("\" ");
		xml.append("ddms:infoCutOff=\"").append(TEST_CUTOFF).append("\"");
		if (version.isAtLeast("3.1"))
			xml.append(" ddms:approvedOn=\"").append(TEST_APPROVED).append("\"");
		if (version.isAtLeast("4.0.1"))
			xml.append(" ddms:receivedOn=\"").append(TEST_RECEIVED).append("\"");
		if (version.isAtLeast("4.1")) {
			xml.append("><ddms:acquiredOn>");
			xml.append("<ddms:description>description</ddms:description>");
			xml.append("<ddms:approximableDate ddms:approximation=\"1st qtr\">2012</ddms:approximableDate>");
			xml.append("<ddms:searchableDate><ddms:start>2012-01</ddms:start>");
			xml.append("<ddms:end>2012-03-31</ddms:end></ddms:searchableDate>");
			xml.append("</ddms:acquiredOn></ddms:dates>");
		}
		else
			xml.append(" />");
		return (xml.toString());
	}

	@Test
	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_DDMS_PREFIX,
				Dates.getName(version));
			getInstance(getWrongNameElementFixture(), WRONG_NAME_MESSAGE);
		}
	}
	
	@Test
	public void testConstructors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based
			getInstance(getValidElement(sVersion), SUCCESS);
			
			// Data-based via Builder
			getBaseBuilder();
		}
	}
	
	@Test
	public void testConstructorsMinimal() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Element-based, No optional fields
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			Dates elementComponent = getInstance(element, SUCCESS);
						
			// Data-based, No optional fields
			getInstance(new Dates.Builder(elementComponent), SUCCESS);
		}
	}

	@Test
	public void testValidationErrors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong date formats (using xs:gDay here)
			Dates.Builder builder = getBaseBuilder();
			builder.setCreated("---31");
			getInstance(builder, "The date datatype must be one of");

			builder = getBaseBuilder();
			builder.setPosted("---31");
			getInstance(builder, "The date datatype must be one of");
			
			builder = getBaseBuilder();
			builder.setValidTil("---31");
			getInstance(builder, "The date datatype must be one of");
			
			builder = getBaseBuilder();
			builder.setInfoCutOff("---31");
			getInstance(builder, "The date datatype must be one of");
			
			if (version.isAtLeast("3.1")) {
				builder = getBaseBuilder();
				builder.setApprovedOn("---31");
				getInstance(builder, "The date datatype must be one of");	
			}
			if (version.isAtLeast("4.0.1")) {
				builder = getBaseBuilder();
				builder.setReceivedOn("---31");
				getInstance(builder, "The date datatype must be one of");	
			}
		}
	}

	@Test
	public void testValidationWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Dates component = getInstance(getValidElement(sVersion), SUCCESS);
			
			if (!"4.1".equals(sVersion)) {
				// No warnings
				assertEquals(0, component.getValidationWarnings().size());
			}
			else {
				// 4.1 ddms:acquiredOn element used
				assertEquals(1, component.getValidationWarnings().size());
				String text = "The ddms:acquiredOn element in this DDMS component";
				String locator = "ddms:dates";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}

			// Completely empty
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			component = getInstance(element, SUCCESS);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:dates element was found.";
			String locator = "ddms:dates";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	@Test
	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			Dates elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			Dates builderComponent = new Dates.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Wrong class
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
			
			// Different values in each field	
			Dates.Builder builder = getBaseBuilder();
			builder.setCreated(null);
			assertFalse(elementComponent.equals(builder.commit()));

			builder = getBaseBuilder();
			builder.setPosted(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setValidTil(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setInfoCutOff(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			if (version.isAtLeast("3.1")) {
				builder = getBaseBuilder();
				builder.setApprovedOn(null);
				assertFalse(elementComponent.equals(builder.commit()));	
			}
			if (version.isAtLeast("4.0.1")) {
				builder = getBaseBuilder();
				builder.setReceivedOn(null);
				assertFalse(elementComponent.equals(builder.commit()));	
			}
		}
	}

	@Test
	public void testVersionSpecific() throws InvalidDDMSException {
		// approvedOn before 3.1
		DDMSVersion.setCurrentVersion("3.0");
		Dates.Builder builder = getBaseBuilder();
		builder.setApprovedOn(TEST_APPROVED);
		getInstance(builder, "This component must not have a");
		
		// receivedOn before 3.1
		DDMSVersion.setCurrentVersion("3.0");
		builder = getBaseBuilder();
		builder.setReceivedOn(TEST_RECEIVED);
		getInstance(builder, "This component must not have a");		
	}
	
	@Test
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.HTML), elementComponent.toHTML());
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.TEXT), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(), elementComponent.toXML());
			assertEquals(getExpectedJSONOutput(), elementComponent.toJSON());
			assertValidJSON(elementComponent.toJSON());
		}
	}

	@Test
	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates.Builder builder = new Dates.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getAcquiredOns().get(0).setDescription("");
			assertTrue(builder.isEmpty());
			builder.getAcquiredOns().get(0).setDescription("test");
			assertFalse(builder.isEmpty());

		}
	}

	@Test
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Dates.Builder builder = new Dates.Builder();
			assertNotNull(builder.getAcquiredOns().get(1));
		}
	}
	
	@Test
	public void testDeprecatedConstructor() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getTestApprovedOn(),
				getTestReceivedOn());
			assertTrue(component.getAcquiredOns().isEmpty());
		}
	}

	@Test
	public void testDateAccessors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Dates component = new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getTestApprovedOn(),
				getTestReceivedOn());
			assertEquals(TEST_CREATED, component.getCreated().toXMLFormat());
			assertEquals(TEST_POSTED, component.getPosted().toXMLFormat());
			assertEquals(TEST_VALID, component.getValidTil().toXMLFormat());
			assertEquals(TEST_CUTOFF, component.getInfoCutOff().toXMLFormat());
			if (version.isAtLeast("3.1"))
				assertEquals(TEST_APPROVED, component.getApprovedOn().toXMLFormat());
			else
				assertNull(component.getApprovedOn());
			if (version.isAtLeast("4.0.1"))
				assertEquals(TEST_RECEIVED, component.getReceivedOn().toXMLFormat());
			else
				assertNull(component.getReceivedOn());

			// ddms custom date types converted into XMLGregorianCalendar
			if (version.isAtLeast("4.1")) {
				component = new Dates("2012-01-01T01:02Z", "2012-01-01T01:02Z", "2012-01-01T01:02Z",
					"2012-01-01T01:02Z", "2012-01-01T01:02Z", "2012-01-01T01:02Z");
				assertNotNull(component.getCreated());
				assertNotNull(component.getPosted());
				assertNotNull(component.getValidTil());
				assertNotNull(component.getInfoCutOff());
				assertNotNull(component.getApprovedOn());
				assertNotNull(component.getReceivedOn());
			}
		}
	}
}