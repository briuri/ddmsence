/* Copyright 2010 - 2013 by Brian Uri!
   
   This file is part of DDMSence.
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 3.0 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
   GNU Lesser General Public License for more processingInfo.
   
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
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:processingInfo elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProcessingInfoTest extends AbstractBaseTestCase {

	private static final String TEST_VALUE = "XSLT Transformation to convert DDMS 2.0 to DDMS 3.1.";
	private static final String TEST_DATE_PROCESSED = "2011-08-19";

	/**
	 * Constructor
	 */
	public ProcessingInfoTest() {
		super("processingInfo.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static ProcessingInfo getFixture() {
		try {
			return (new ProcessingInfo(TEST_VALUE, TEST_DATE_PROCESSED, SecurityAttributesTest.getFixture()));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<ProcessingInfo> getFixtureList() {
		List<ProcessingInfo> infos = new ArrayList<ProcessingInfo>();
		infos.add(getFixture());
		return (infos);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ProcessingInfo getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ProcessingInfo component = null;
		try {
			component = new ProcessingInfo(element);
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
	 * @param value the child text
	 * @param dateProcessed the processing date
	 * @return a valid object
	 */
	private ProcessingInfo getInstance(String message, String value, String dateProcessed) {
		boolean expectFailure = !Util.isEmpty(message);
		ProcessingInfo component = null;
		try {
			component = new ProcessingInfo(value, dateProcessed, SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "processingInfo", TEST_VALUE));
		text.append(buildOutput(isHTML, "processingInfo.dateProcessed", TEST_DATE_PROCESSED));
		text.append(buildOutput(isHTML, "processingInfo.classification", "U"));
		text.append(buildOutput(isHTML, "processingInfo.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:processingInfo ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM());
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ");
		xml.append("ddms:dateProcessed=\"").append(TEST_DATE_PROCESSED).append("\">");
		xml.append(TEST_VALUE).append("</ddms:processingInfo>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				ProcessingInfo.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			Util.addDDMSAttribute(element, "dateProcessed", TEST_DATE_PROCESSED);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_VALUE, TEST_DATE_PROCESSED);

			// No optional fields
			getInstance(SUCCESS, "", TEST_DATE_PROCESSED);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing date
			Element element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("dateProcessed is required.", element);

			// Wrong date format (using xs:gDay here)
			element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			Util.addDDMSAttribute(element, "dateProcessed", "---31");
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("The date datatype must be one of", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing date
			getInstance("dateProcessed is required.", TEST_VALUE, null);

			// Invalid date format
			getInstance("The date datatype must be one of", TEST_VALUE, "baboon");

			// Wrong date format (using xs:gDay here)
			getInstance("The date datatype must be one of", TEST_VALUE, "---31");

			// Bad security attributes
			try {
				new ProcessingInfo(TEST_VALUE, TEST_DATE_PROCESSED, null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ProcessingInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			if (!version.isAtLeast("5.0")) {
				Element element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
				Util.addDDMSAttribute(element, "dateProcessed", TEST_DATE_PROCESSED);
				SecurityAttributesTest.getFixture().addTo(element);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:processingInfo element was found with no value.";
				String locator = "ddms:processingInfo";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testDeprecatedAccessors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(TEST_DATE_PROCESSED, component.getDateProcessed().toXMLFormat());

			// Not compatible with XMLGregorianCalendar
			if (version.isAtLeast("4.1")) {
				component = new ProcessingInfo(TEST_VALUE, "2012-01-01T01:02Z", SecurityAttributesTest.getFixture());
				assertNull(component.getDateProcessed());
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			ProcessingInfo dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			ProcessingInfo dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_DATE_PROCESSED);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, "2011");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new ProcessingInfo(TEST_VALUE, TEST_DATE_PROCESSED, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The processingInfo element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			ProcessingInfo.Builder builder = new ProcessingInfo.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo.Builder builder = new ProcessingInfo.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo.Builder builder = new ProcessingInfo.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "dateProcessed is required.");
			}
			builder.setDateProcessed(TEST_DATE_PROCESSED);
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
