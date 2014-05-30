/* Copyright 2010 - 2014 by Brian Uri!
   
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
package buri.ddmsence.ddms.summary.tspi;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to tspi:USPSGeneralDeliveryOffice elements </p>
 * 
 * @author Brian Uri!
 * @since 2.2.0
 */
public class USPSGeneralDeliveryOfficeTest extends AbstractBaseTestCase {

	private static final String TEST_ACTION = "ADD";

	/**
	 * Constructor
	 */
	public USPSGeneralDeliveryOfficeTest() {
		super("uspsGeneralDeliveryOffice.xml");
		removeSupportedVersions("2.0 3.0 3.1 4.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static USPSGeneralDeliveryOffice getFixture() {
		try {
			USPSGeneralDeliveryOffice.Builder builder = new USPSGeneralDeliveryOffice.Builder();
			builder.setXml(getExpectedXMLOutput());
			return (builder.commit());
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
	private USPSGeneralDeliveryOffice getInstance(Element element, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		USPSGeneralDeliveryOffice component = null;
		try {
			component = new USPSGeneralDeliveryOffice(element);
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
	private USPSGeneralDeliveryOffice getInstance(USPSGeneralDeliveryOffice.Builder builder, String message) {
		boolean expectFailure = !Util.isEmpty(message);
		USPSGeneralDeliveryOffice component = null;
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
	private USPSGeneralDeliveryOffice.Builder getBaseBuilder() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		USPSGeneralDeliveryOffice component = getInstance(getValidElement(version.getVersion()), SUCCESS);
		return (new USPSGeneralDeliveryOffice.Builder(component));
	}

	/**
	 * Returns the expected output for the test instance of this component
	 */
	private String getExpectedHTMLTextOutput(OutputFormat format) throws InvalidDDMSException {
		Util.requireHTMLText(format);
		StringBuffer text = new StringBuffer();
		text.append(buildHTMLTextOutput(format, "addressType", "USPSGeneralDeliveryOffice"));
		return (text.toString());
	}

	/**
	 * Returns the expected JSON output for this unit test
	 */
	private String getExpectedJSONOutput() {
		StringBuffer json = new StringBuffer();
		json.append("{\"addressType\":\"USPSGeneralDeliveryOffice\"}");
		return (json.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private static String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<tspi:USPSGeneralDeliveryOffice ");
		xml.append("xmlns:tspi=\"").append(DDMSVersion.getCurrentVersion().getTspiNamespace()).append("\" ");
		xml.append("xmlns:addr=\"http://www.fgdc.gov/schema/address/addr\" ");
		xml.append("xmlns:addr_type=\"http://www.fgdc.gov/schema/address/addr_type\" ");
		xml.append("action=\"").append(TEST_ACTION).append("\">");
		xml.append("<addr:USPSGeneralDeliveryPoint>USS Carl Vinson</addr:USPSGeneralDeliveryPoint>");
		xml.append("<addr_type:CompletePlaceName>");
		xml.append("<addr_type:PlaceName>McLean</addr_type:PlaceName>");
		xml.append("</addr_type:CompletePlaceName>");
		xml.append("<addr_type:StateName>Virginia</addr_type:StateName>");
		xml.append("<addr_type:ZipCode>20102</addr_type:ZipCode>");
		xml.append("<addr_type:CountryName>http://api.nsgreg.nga.mil/geo-political/GENC/3/ed1/USA</addr_type:CountryName>");
		xml.append("</tspi:USPSGeneralDeliveryOffice>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(getValidElement(sVersion), SUCCESS), DEFAULT_TSPI_PREFIX,
				USPSGeneralDeliveryOffice.getName(version));
			getInstance(getWrongNameElementFixture(), WRONG_NAME_MESSAGE);
		}
	}

	public void testConstructors() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Element-based
			getInstance(getValidElement(sVersion), SUCCESS);

			// Data-based via Builder
			getBaseBuilder();
		}
	}
	
	public void testConstructorsMinimal() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No optional fields
			USPSGeneralDeliveryOffice.Builder builder = getBaseBuilder();
			String xml = getExpectedXMLOutput();
			xml = xml.replace("action=\"ADD\"", "");
			builder.setXml(xml);
			getInstance(builder, SUCCESS);
		}
	}

	public void testValidationErrors() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			// Invalid action
			String xml = getExpectedXMLOutput();
			xml = xml.replace("\"ADD\"", "\"UPDATE\"");
			USPSGeneralDeliveryOffice.Builder builder = getBaseBuilder();
			builder.setXml(xml);
			getInstance(builder, "The action attribute must be one of");
			
			// Invalid XML case is implicit in Util.commitXml() test.
		}
	}

	public void testValidationWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			USPSGeneralDeliveryOffice component = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Base equality
			USPSGeneralDeliveryOffice elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			USPSGeneralDeliveryOffice builderComponent = new USPSGeneralDeliveryOffice.Builder(elementComponent).commit();
			assertEquals(elementComponent, builderComponent);
			assertEquals(elementComponent.hashCode(), builderComponent.hashCode());

			// Wrong class
			assertFalse(elementComponent.equals(Integer.valueOf(1)));
			
			// Different values in each field
			USPSGeneralDeliveryOffice.Builder builder = getBaseBuilder();
			String xml = getExpectedXMLOutput();
			xml = xml.replace("\"ADD\"", "\"DELETE\"");
			builder.setXml(xml);
			assertFalse(elementComponent.equals(builder.commit()));			
			
			builder = getBaseBuilder();
			xml = getExpectedXMLOutput();
			xml = xml.replace("20102", "20121");
			builder.setXml(xml);
			assertFalse(elementComponent.equals(builder.commit()));	
		}
	}

	public void testVersionSpecific() {
		// Pre-5.0 test is implicit, since TSPI namespace did not exist.
	}

	public void testOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			USPSGeneralDeliveryOffice elementComponent = getInstance(getValidElement(sVersion), SUCCESS);
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.HTML), elementComponent.toHTML());
			assertEquals(getExpectedHTMLTextOutput(OutputFormat.TEXT), elementComponent.toText());
			assertEquals(getExpectedXMLOutput(), elementComponent.toXML());
			assertEquals(getExpectedJSONOutput(), elementComponent.toJSON());
			assertValidJSON(elementComponent.toJSON());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			USPSGeneralDeliveryOffice.Builder builder = new USPSGeneralDeliveryOffice.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			
			builder.setXml("<hello />");
			assertFalse(builder.isEmpty());
		}
	}
}
