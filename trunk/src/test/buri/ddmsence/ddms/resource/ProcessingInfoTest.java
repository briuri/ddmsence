/* Copyright 2010 - 2011 by Brian Uri!
   
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

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:processingInfo elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProcessingInfoTest extends AbstractComponentTestCase {

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
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ProcessingInfo testConstructor(boolean expectFailure, Element element) {
		ProcessingInfo component = null;
		try {
			component = new ProcessingInfo(element);
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
	 * @param value the child text
	 * @param dateProcessed the processing date
	 * @return a valid object
	 */
	private ProcessingInfo testConstructor(boolean expectFailure, String value, String dateProcessed) {
		ProcessingInfo component = null;
		try {
			component = new ProcessingInfo(value, dateProcessed, SecurityAttributesTest.getFixture(false));
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
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

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				ProcessingInfo.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			Util.addDDMSAttribute(element, "dateProcessed", TEST_DATE_PROCESSED);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_DATE_PROCESSED);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", TEST_DATE_PROCESSED);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong name
			Element element = Util.buildDDMSElement("unknownName", null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing date
			element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Wrong date format (using xs:gDay here)
			element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			Util.addDDMSAttribute(element, "dateProcessed", "---31");
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing date
			testConstructor(WILL_FAIL, TEST_VALUE, null);

			// Invalid date format
			testConstructor(WILL_FAIL, TEST_VALUE, "baboon");

			// Wrong date format (using xs:gDay here)
			testConstructor(WILL_FAIL, TEST_VALUE, "---31");

			// Bad security attributes
			try {
				new ProcessingInfo(TEST_VALUE, TEST_DATE_PROCESSED, null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ProcessingInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			Element element = Util.buildDDMSElement(ProcessingInfo.getName(version), null);
			Util.addDDMSAttribute(element, "dateProcessed", TEST_DATE_PROCESSED);
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ddms:processingInfo element was found with no value.";
			String locator = "ddms:processingInfo";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ProcessingInfo dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ProcessingInfo dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_DATE_PROCESSED);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, "2011");
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_DATE_PROCESSED);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new ProcessingInfo(TEST_VALUE, TEST_DATE_PROCESSED, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProcessingInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			ProcessingInfo.Builder builder = new ProcessingInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new ProcessingInfo.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new ProcessingInfo.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
