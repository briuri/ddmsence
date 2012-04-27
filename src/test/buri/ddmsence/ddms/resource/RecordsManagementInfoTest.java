/* Copyright 2010 - 2012 by Brian Uri!
   
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
 * <p> Tests related to ddms:recordsManagementInfo elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordsManagementInfoTest extends AbstractBaseTestCase {

	private static final Boolean TEST_VITAL = Boolean.TRUE;

	/**
	 * Constructor
	 */
	public RecordsManagementInfoTest() {
		super("recordsManagementInfo.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static RecordsManagementInfo getFixture() {
		try {
			return (new RecordsManagementInfo(RecordKeeperTest.getFixture(), ApplicationSoftwareTest.getFixture(),
				TEST_VITAL));
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
	private RecordsManagementInfo getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		RecordsManagementInfo component = null;
		try {
			component = new RecordsManagementInfo(element);
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
	 * @param keeper the record keeper (optional)
	 * @param software the software (optional)
	 * @param vitalRecord whether this is a vital record (optional, defaults to false)
	 * @param org the organization
	 */
	private RecordsManagementInfo getInstance(String message, RecordKeeper keeper, ApplicationSoftware software,
		Boolean vitalRecord) {
		boolean expectFailure = !Util.isEmpty(message);
		RecordsManagementInfo component = null;
		try {
			component = new RecordsManagementInfo(keeper, software, vitalRecord);
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
		text.append(RecordKeeperTest.getFixture().getOutput(isHTML, "recordsManagementInfo.", ""));
		text.append(ApplicationSoftwareTest.getFixture().getOutput(isHTML, "recordsManagementInfo.", ""));
		text.append(buildOutput(isHTML, "recordsManagementInfo.vitalRecordIndicator", "true"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:recordsManagementInfo ").append(getXmlnsDDMS());
		xml.append(" ddms:vitalRecordIndicator=\"true\">\n");
		xml.append("\t<ddms:recordKeeper>\n");
		xml.append("\t\t<ddms:recordKeeperID>#289-99202.9</ddms:recordKeeperID>\n");
		xml.append("\t\t<ddms:organization>\n");
		xml.append("\t\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t\t</ddms:organization>\n");
		xml.append("\t</ddms:recordKeeper>\n");
		xml.append("\t<ddms:applicationSoftware ").append(getXmlnsISM());
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">IRM Generator 2L-9</ddms:applicationSoftware>\n");
		xml.append("</ddms:recordsManagementInfo>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				RecordsManagementInfo.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(RecordsManagementInfo.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, RecordKeeperTest.getFixture(), ApplicationSoftwareTest.getFixture(), TEST_VITAL);

			// No optional fields
			getInstance(SUCCESS, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No invalid cases.
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No invalid cases.
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RecordsManagementInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RecordsManagementInfo dataComponent = getInstance(SUCCESS, RecordKeeperTest.getFixture(),
				ApplicationSoftwareTest.getFixture(), TEST_VITAL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RecordsManagementInfo dataComponent = getInstance(SUCCESS, RecordKeeperTest.getFixture(),
				ApplicationSoftwareTest.getFixture(), null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, RecordKeeperTest.getFixture(), null, TEST_VITAL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, null, ApplicationSoftwareTest.getFixture(), TEST_VITAL);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, RecordKeeperTest.getFixture(), ApplicationSoftwareTest.getFixture(),
				TEST_VITAL);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, RecordKeeperTest.getFixture(), ApplicationSoftwareTest.getFixture(),
				TEST_VITAL);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RecordsManagementInfo(null, null, TEST_VITAL);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The recordsManagementInfo element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = getInstance(SUCCESS, getValidElement(sVersion));
			RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setVitalRecordIndicator(TEST_VITAL);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder();
			builder.getApplicationSoftware().setValue("value");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
			builder.getApplicationSoftware().getSecurityAttributes().setClassification("U");
			builder.getApplicationSoftware().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
