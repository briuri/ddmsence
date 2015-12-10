/* Copyright 2010 - 2016 by Brian Uri!
   
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
import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
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
	 * @param element the element to build from
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private RecordsManagementInfo getInstance(Element element, String message) {
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
	 * @param builder the builder to commit
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * 
	 * @return a valid object
	 */
	private RecordsManagementInfo getInstance(RecordsManagementInfo.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		RecordsManagementInfo component = null;
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
	private RecordsManagementInfo.Builder getBaseBuilder() {
		RecordsManagementInfo component = getInstance(getValidElement(DDMSVersion.getCurrentVersion().getVersion()), SUCCESS);
		return (new RecordsManagementInfo.Builder(component));
	}

	/**
	 * Returns the expected output for the test instance of this component
	 */
	private String getExpectedHTMLTextOutput(OutputFormat format) throws InvalidDDMSException {
		Util.requireHTMLText(format);
		StringBuffer text = new StringBuffer();
		text.append(RecordKeeperTest.getFixture().getHTMLTextOutput(format, "recordsManagementInfo.", ""));
		text.append(ApplicationSoftwareTest.getFixture().getHTMLTextOutput(format, "recordsManagementInfo.", ""));
		text.append(buildHTMLTextOutput(format, "recordsManagementInfo.vitalRecordIndicator", "true"));
		return (text.toString());
	}

	/**
	 * Returns the expected JSON output for this unit test
	 */
	private String getExpectedJSONOutput() {
		StringBuffer json = new StringBuffer();
		json.append("{\"recordKeeper\":").append(RecordKeeperTest.getFixture().toJSON()).append(",");
		json.append("\"applicationSoftware\":").append(ApplicationSoftwareTest.getFixture().toJSON()).append(",");
		json.append("\"vitalRecordIndicator\":true}");
		return (json.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
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
		xml.append(" ism:classification=\"U\" ism:ownerProducer=\"USA\">IRM Generator 2L-9</ddms:applicationSoftware>\n");
		xml.append("</ddms:recordsManagementInfo>");
		return (xml.toString());
	}

	@Test
	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_DDMS_PREFIX,
				RecordsManagementInfo.getName(version));
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

			// Element-based, no optional fields
			Element element = Util.buildDDMSElement(RecordsManagementInfo.getName(version), null);
			RecordsManagementInfo elementComponent = getInstance(element, SUCCESS);

			// Data-based via Builder, no optional fields
			getInstance(new RecordsManagementInfo.Builder(elementComponent), SUCCESS);
		}
	}
	
	@Test
	public void testValidationErrors() throws InvalidDDMSException {
		// No tests.
	}

	@Test
	public void testValidationWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RecordsManagementInfo component = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	@Test
	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			RecordsManagementInfo elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			RecordsManagementInfo builderComponent = new RecordsManagementInfo.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Different values in each field
			RecordsManagementInfo.Builder builder = getBaseBuilder();
			builder.setVitalRecordIndicator(null);
			assertFalse(elementComponent.equals(builder.commit()));

			builder = getBaseBuilder();
			builder.setRecordKeeper(null);
			assertFalse(elementComponent.equals(builder.commit()));
			
			builder = getBaseBuilder();
			builder.setApplicationSoftware(null);
			assertFalse(elementComponent.equals(builder.commit()));
		}
	}

	@Test
	public void testVersionSpecific() throws InvalidDDMSException {
		RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder();
		builder.setVitalRecordIndicator(TEST_VITAL);
		DDMSVersion.setCurrentVersion("2.0");
		getInstance(builder, "The recordsManagementInfo element must not ");
	}

	@Test
	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
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

			RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			
			builder.setVitalRecordIndicator(TEST_VITAL);
			assertFalse(builder.isEmpty());
		}
	}
}
