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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
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
	 * @param approvedOn the approved on date (optional, starting in v3.1)
	 * @return a valid object
	 */
	private Dates testConstructor(boolean expectFailure, String created, String posted, String validTil,
		String infoCutOff, String approvedOn) {
		Dates component = null;
		try {
			component = new Dates(created, posted, validTil, infoCutOff, approvedOn);
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
		html.append("<meta name=\"dates.created\" content=\"").append(TEST_CREATED).append("\" />\n");
		html.append("<meta name=\"dates.posted\" content=\"").append(TEST_POSTED).append("\" />\n");
		html.append("<meta name=\"dates.validTil\" content=\"").append(TEST_VALID).append("\" />\n");
		html.append("<meta name=\"dates.infoCutOff\" content=\"").append(TEST_CUTOFF).append("\" />\n");
		if (DDMSVersion.isCurrentVersion("3.1"))
			html.append("<meta name=\"dates.approvedOn\" content=\"").append(TEST_APPROVED).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("created: ").append(TEST_CREATED).append("\n");
		text.append("posted: ").append(TEST_POSTED).append("\n");
		text.append("validTil: ").append(TEST_VALID).append("\n");
		text.append("infoCutOff: ").append(TEST_CUTOFF).append("\n");
		if (DDMSVersion.isCurrentVersion("3.1"))
			text.append("approvedOn: ").append(TEST_APPROVED).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:dates xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		xml.append("ddms:created=\"").append(TEST_CREATED).append("\" ");
		xml.append("ddms:posted=\"").append(TEST_POSTED).append("\" ");
		xml.append("ddms:validTil=\"").append(TEST_VALID).append("\" ");
		xml.append("ddms:infoCutOff=\"").append(TEST_CUTOFF).append("\" ");
		if (DDMSVersion.isCurrentVersion("3.1"))
			xml.append("ddms:approvedOn=\"").append(TEST_APPROVED).append("\" ");
		xml.append("/>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Dates.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Dates.NAME, component.getQualifiedName());

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
			Element element = Util.buildDDMSElement(Dates.NAME, null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			
			// All fields
			testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "", "", "", "");
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Wrong date format (using xs:gDay here)
			Element element = Util.buildDDMSElement(Dates.NAME, null);
			Util.addDDMSAttribute(element, "created", "---31");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			
			// Wrong date format (using xs:gDay here)
			testConstructor(WILL_FAIL, "---31", TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(Dates.NAME, null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A completely empty ddms:dates element was found.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:dates", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			
			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Dates dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			
			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Dates dataComponent = testConstructor(WILL_SUCCEED, "", TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, "", TEST_VALID, TEST_CUTOFF, approvedOn);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, "", TEST_CUTOFF, approvedOn);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, "", approvedOn);
			assertFalse(elementComponent.equals(dataComponent));

			if (version.equals("3.1")) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, "");
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Dates elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String approvedOn = (version.equals("3.1") ? TEST_APPROVED : "");
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_CREATED, TEST_POSTED, TEST_VALID, TEST_CUTOFF, approvedOn);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Dates component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
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
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.setCreated(TEST_CREATED);
			builder.commit();
		}
	}
}