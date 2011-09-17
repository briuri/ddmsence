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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:source elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DatesTest extends AbstractComponentTestCase {

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
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Dates testConstructor(boolean expectFailure, Element element) {
		Dates component = null;
		try {
			component = new Dates(element);
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
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @param approvedOn the approved on date (optional, starting in 3.1)
	 * @param receivedOn the received on date (optional, starting in 4.0)
	 * @return a valid object
	 */
	private Dates testConstructor(boolean expectFailure, String created, String posted, String validTil,
		String infoCutOff, String approvedOn, String receivedOn) {
		Dates component = null;
		try {
			component = new Dates(created, posted, validTil, infoCutOff, approvedOn, receivedOn);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
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
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? TEST_RECEIVED : "");
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
		if (version.isAtLeast("4.0"))
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
		if (version.isAtLeast("4.0"))
			xml.append("ddms:receivedOn=\"").append(TEST_RECEIVED).append("\" ");
		xml.append("/>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Dates.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Dates.getName(version),
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
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "", "", "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Wrong date format (using xs:gDay here)
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			Util.addDDMSAttribute(element, "created", "---31");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// Wrong date format (using xs:gDay here)
			testConstructor(WILL_FAIL, "---31", TEST_POSTED, TEST_VALID, TEST_CUTOFF, getApprovedOn(), getReceivedOn());
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(Dates.getName(version), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:dates element was found.";
			String locator = "ddms:dates";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Dates dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Dates dataComponent = testConstructor(WILL_SUCCEED, "", TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, "", TEST_VALID, TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, "", TEST_CUTOFF, getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, "", getApprovedOn(),
				getReceivedOn());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("3.1")) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, "",
					getReceivedOn());
				assertFalse(elementComponent.equals(dataComponent));
			}

			if (version.isAtLeast("4.0")) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
					getApprovedOn(), "");
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF,
				getApprovedOn(), getReceivedOn());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testApprovedOnWrongVersion() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, TEST_APPROVED, null);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testReceivedOnWrongVersion() {
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new Dates(TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, null, TEST_RECEIVED);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Dates.Builder builder = new Dates.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Dates.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Dates.Builder();
			builder.setCreated("notAnXmlDate");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.setCreated(TEST_CREATED);
			builder.commit();
		}
	}
}