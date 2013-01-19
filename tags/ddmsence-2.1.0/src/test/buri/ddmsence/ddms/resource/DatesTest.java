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
package buri.ddmsence.ddms.resource;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.ApproximableDate;
import buri.ddmsence.ddms.ApproximableDateTest;
import buri.ddmsence.ddms.InvalidDDMSException;
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
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Dates getInstance(String message, Element element) {
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
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param acquiredOns a list of acquisition dates (optional, starting in 4.1)
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @param approvedOn the approved on date (optional, starting in 3.1)
	 * @param receivedOn the received on date (optional, starting in 4.0.1)
	 * @return a valid object
	 */
	private Dates getInstance(String message, List<ApproximableDate> acquiredOns, String created, String posted,
		String validTil, String infoCutOff, String approvedOn, String receivedOn) {
		boolean expectFailure = !Util.isEmpty(message);
		Dates component = null;
		try {
			component = new Dates(acquiredOns, created, posted, validTil, infoCutOff, approvedOn, receivedOn);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Generates an getApprovedOn() Date for testing
	 */
	private String getApprovedOn() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("3.1") ? TEST_APPROVED : "");
	}

	/**
	 * Generates a receivedOn Date for testing
	 */
	private String getReceivedOn() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? TEST_RECEIVED : "");
	}
	
	/**
	 * Generates an acquiredOn Date for testing
	 */
	private List<ApproximableDate> getAcquiredOns() throws InvalidDDMSException {
		List<ApproximableDate> list = new ArrayList<ApproximableDate>();
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			list.add(new ApproximableDate(ApproximableDateTest.getFixtureElement("acquiredOn", true)));
		}
		return (list);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("4.1")) {
			for (ApproximableDate acquiredOn : getAcquiredOns()) {
				text.append(acquiredOn.getOutput(isHTML, "dates.", ""));
			}
		}
		text.append(buildOutput(isHTML, "dates.created", TEST_CREATED));
		text.append(buildOutput(isHTML, "dates.posted", TEST_POSTED));
		text.append(buildOutput(isHTML, "dates.validTil", TEST_VALID));
		text.append(buildOutput(isHTML, "dates.infoCutOff", TEST_CUTOFF));
		if (version.isAtLeast("3.1"))
			text.append(buildOutput(isHTML, "dates.approvedOn", TEST_APPROVED));
		if (version.isAtLeast("4.0.1"))
			text.append(buildOutput(isHTML, "dates.receivedOn", TEST_RECEIVED));
		return (text.toString());
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

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Dates
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());

			// No optional fields
			getInstance(SUCCESS, null, "", "", "", "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Wrong date format (using xs:gDay here)
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			Util.addDDMSAttribute(element, "created", "---31");
			getInstance("The date datatype must be one of", element);
		}
	}
	
	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong date format (using xs:gDay here)
			getInstance("The date datatype must be one of", getAcquiredOns(), "---31", TEST_POSTED, TEST_VALID,
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			getInstance("The date datatype must be one of", getAcquiredOns(), TEST_CREATED, "---31", TEST_VALID,
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			getInstance("The date datatype must be one of", getAcquiredOns(), TEST_CREATED, TEST_POSTED, "---31",
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			getInstance("The date datatype must be one of", getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID,
				"---31", getApprovedOn(), getReceivedOn());
			if (version.isAtLeast("3.1"))
				getInstance("The date datatype must be one of", getAcquiredOns(), TEST_CREATED, TEST_POSTED,
					TEST_VALID, TEST_CUTOFF, "---31", getReceivedOn());
			if (version.isAtLeast("4.0.1"))
				getInstance("The date datatype must be one of", getAcquiredOns(), TEST_CREATED, TEST_POSTED,
					TEST_VALID, TEST_CUTOFF, getApprovedOn(), "---31");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			
			// 4.1 ddms:acquiredOn element used
			if (version.isAtLeast("4.1")) {
				assertEquals(1, component.getValidationWarnings().size());	
				String text = "The ddms:acquiredOn element in this DDMS component";
				String locator = "ddms:dates";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
			// No warnings 
			else {
				assertEquals(0, component.getValidationWarnings().size());
			}

			// Empty element
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:dates element was found.";
			String locator = "ddms:dates";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testDeprecatedConstructor() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID,
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			assertTrue(component.getAcquiredOns().isEmpty());
		}
	}
	
	public void testDeprecatedAccessors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Dates component = new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID,
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			assertEquals(TEST_CREATED, component.getCreated().toXMLFormat());
			assertEquals(TEST_POSTED, component.getPosted().toXMLFormat());
			assertEquals(TEST_VALID, component.getValidTil().toXMLFormat());
			assertEquals(TEST_CUTOFF, component.getInfoCutOff().toXMLFormat());
			if (version.isAtLeast("3.1"))
				assertEquals(TEST_APPROVED, component.getApprovedOn().toXMLFormat());
			if (version.isAtLeast("4.0.1"))
				assertEquals(TEST_RECEIVED, component.getReceivedOn().toXMLFormat());
			
			// Not compatible with XMLGregorianCalendar
			if (version.isAtLeast("4.1")) {
				component = new Dates("2012-01-01T01:02Z", "2012-01-01T01:02Z", "2012-01-01T01:02Z",
					"2012-01-01T01:02Z", "2012-01-01T01:02Z", "2012-01-01T01:02Z");
				assertNull(component.getCreated());
				assertNull(component.getPosted());
				assertNull(component.getValidTil());
				assertNull(component.getInfoCutOff());
				assertNull(component.getApprovedOn());
				assertNull(component.getReceivedOn());			
			}
		}		
	}
	
	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Dates dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID,
				TEST_CUTOFF, getApprovedOn(), getReceivedOn());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Dates dataComponent = getInstance(SUCCESS, getAcquiredOns(), "", TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, "", TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, "", TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID, "",
				getApprovedOn(), getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("3.1")) {
				dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID,
					TEST_CUTOFF, "", getReceivedOn());
				assertFalse(elementComponent.equals(dataComponent));
			}

			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID,
					TEST_CUTOFF, getApprovedOn(), "");
				assertFalse(elementComponent.equals(dataComponent));
			}

			if (version.isAtLeast("4.1")) {
				dataComponent = getInstance(SUCCESS, null, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
					getApprovedOn(), "");
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, getAcquiredOns(), TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersionApprovedOn() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(null, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, TEST_APPROVED, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have an approvedOn date ");
		}
	}

	public void testWrongVersionReceivedOn() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(null, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, null, TEST_RECEIVED);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have a receivedOn date ");
		}
	}
	
	public void testWrongVersionAcquiredOn() {
		try {
			DDMSVersion.setCurrentVersion("4.1");
			List<ApproximableDate> acquiredOns = getAcquiredOns();
			DDMSVersion.setCurrentVersion("3.0");
			new Dates(acquiredOns, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have an acquiredOn date");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			Dates.Builder builder = new Dates.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

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

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates.Builder builder = new Dates.Builder();
			builder.setCreated("notAnXmlDate");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The date datatype must be one of");
			}
			builder.setCreated(TEST_CREATED);
			builder.commit();
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Dates.Builder builder = new Dates.Builder();
			assertNotNull(builder.getAcquiredOns().get(1));
		}
	}
}