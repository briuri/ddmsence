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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:recordsManagementInfo elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordsManagementInfoTest extends AbstractComponentTestCase {

	private static final Boolean TEST_VITAL = Boolean.TRUE;
	private static final String TEST_ID = "#289-99202.9";
	private static final String TEST_NAME = "AgencyZ";
	private static final String TEST_SOFTWARE = "IRM Generator 2L-9";

	/**
	 * Constructor
	 */
	public RecordsManagementInfoTest() {
		super("recordsManagementInfo.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Helper method to create a fixture
	 */
	private static RecordKeeper getRecordKeeperFixture() {
		try {
			return (new RecordKeeper(TEST_ID, new Organization(Util.getXsListAsList(TEST_NAME), null, null, null, null,
				null)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static ApplicationSoftware getApplicationSoftwareFixture() {
		try {
			return (new ApplicationSoftware(TEST_SOFTWARE, SecurityAttributesTest.getFixture(false)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private RecordsManagementInfo testConstructor(boolean expectFailure, Element element) {
		RecordsManagementInfo component = null;
		try {
			component = new RecordsManagementInfo(element);
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
	 * @param keeper the record keeper (optional)
	 * @param software the software (optional)
	 * @param vitalRecord whether this is a vital record (optional, defaults to false)
	 * @param org the organization
	 */
	private RecordsManagementInfo testConstructor(boolean expectFailure, RecordKeeper keeper,
		ApplicationSoftware software, Boolean vitalRecord) {
		RecordsManagementInfo component = null;
		try {
			component = new RecordsManagementInfo(keeper, software, vitalRecord);
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
		text.append(getRecordKeeperFixture().getOutput(isHTML, "recordsManagementInfo."));
		text.append(getApplicationSoftwareFixture().getOutput(isHTML, "recordsManagementInfo."));
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
		xml.append("\t\t\t<ddms:name>AgencyZ</ddms:name>\n");
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

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				RecordsManagementInfo.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(RecordsManagementInfo.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getRecordKeeperFixture(), getApplicationSoftwareFixture(), TEST_VITAL);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null);
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
			RecordsManagementInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			RecordsManagementInfo dataComponent = testConstructor(WILL_SUCCEED, getRecordKeeperFixture(),
				getApplicationSoftwareFixture(), TEST_VITAL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			RecordsManagementInfo dataComponent = testConstructor(WILL_SUCCEED, getRecordKeeperFixture(),
				getApplicationSoftwareFixture(), null);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getRecordKeeperFixture(), null, TEST_VITAL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, null, getApplicationSoftwareFixture(), TEST_VITAL);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getRecordKeeperFixture(), getApplicationSoftwareFixture(),
				TEST_VITAL);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getRecordKeeperFixture(), getApplicationSoftwareFixture(),
				TEST_VITAL);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RecordsManagementInfo(null, null, TEST_VITAL);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RecordsManagementInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			RecordsManagementInfo.Builder builder = new RecordsManagementInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RecordsManagementInfo.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RecordsManagementInfo.Builder();
			builder.getApplicationSoftware().setValue("value");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.getApplicationSoftware().getSecurityAttributes().setClassification("U");
			builder.getApplicationSoftware().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
