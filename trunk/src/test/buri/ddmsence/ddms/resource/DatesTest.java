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
package buri.ddmsence.ddms.resource;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
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
			return (new Dates("2003", null, null, null, null, null));
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
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @param approvedOn the approved on date (optional, starting in 3.1)
	 * @param receivedOn the received on date (optional, starting in 4.0.1)
	 * @return a valid object
	 */
	private Dates getInstance(String message, String created, String posted, String validTil, String infoCutOff,
		String approvedOn, String receivedOn) {
		boolean expectFailure = !Util.isEmpty(message);
		Dates component = null;
		try {
			component = new Dates(created, posted, validTil, infoCutOff, approvedOn, receivedOn);
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
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
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
		xml.append("ddms:infoCutOff=\"").append(TEST_CUTOFF).append("\" ");
		if (version.isAtLeast("3.1"))
			xml.append("ddms:approvedOn=\"").append(TEST_APPROVED).append("\" ");
		if (version.isAtLeast("4.0.1"))
			xml.append("ddms:receivedOn=\"").append(TEST_RECEIVED).append("\" ");
		xml.append("/>");
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

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(), getReceivedOn());

			// No optional fields
			getInstance(SUCCESS, "", "", "", "", "", "");
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

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Wrong date format (using xs:gDay here)
			getInstance("The date datatype must be one of", "---31", TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			component = getInstance(SUCCESS, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:dates element was found.";
			String locator = "ddms:dates";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Dates dataComponent = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Dates elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Dates dataComponent = getInstance(SUCCESS, "", TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_CREATED, "", TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, "", TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, "", getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("3.1")) {
				dataComponent = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, "",
					getReceivedOn());
				assertFalse(elementComponent.equals(dataComponent));
			}

			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
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

			component = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Dates component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersionApprovedOn() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, TEST_APPROVED, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have an approvedOn date ");
		}
	}

	public void testWrongVersionReceivedOn() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, null, TEST_RECEIVED);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have a receivedOn date ");
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
			builder.setCreated(TEST_CREATED);
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
				expectMessage(e, "One or more ddms:dates attributes are not in a valid date format.");
			}
			builder.setCreated(TEST_CREATED);
			builder.commit();
		}
	}
}