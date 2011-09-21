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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:virtualCoverage elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class VirtualCoverageTest extends AbstractComponentTestCase {

	private static final String TEST_ADDRESS = "123.456.789.0";
	private static final String TEST_PROTOCOL = "IP";

	/**
	 * Constructor
	 */
	public VirtualCoverageTest() {
		super("virtualCoverage.xml");
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private VirtualCoverage testConstructor(boolean expectFailure, Element element) {
		VirtualCoverage component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture(false).addTo(element);
			component = new VirtualCoverage(element);
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
	 * @param address the virtual address (optional)
	 * @param protocol the network protocol (optional, should be used if address is provided)
	 * @return a valid object
	 */
	private VirtualCoverage testConstructor(boolean expectFailure, String address, String protocol) {
		VirtualCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture(false);
			component = new VirtualCoverage(address, protocol, attr);
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
		text.append(buildOutput(isHTML, "virtualCoverage.address", TEST_ADDRESS));
		text.append(buildOutput(isHTML, "virtualCoverage.protocol", TEST_PROTOCOL));
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append(buildOutput(isHTML, "virtualCoverage.classification", "U"));
			text.append(buildOutput(isHTML, "virtualCoverage.ownerProducer", "USA"));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:virtualCoverage ").append(getXmlnsDDMS());
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append(" ").append(getXmlnsISM());
		xml.append(" ddms:address=\"").append(TEST_ADDRESS).append("\" ddms:protocol=\"").append(TEST_PROTOCOL)
			.append("\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				VirtualCoverage.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(VirtualCoverage.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// address without protocol
			Element element = Util.buildDDMSElement(VirtualCoverage.getName(version), null);
			Util.addDDMSAttribute(element, "address", TEST_ADDRESS);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// address without protocol		
			testConstructor(WILL_FAIL, TEST_ADDRESS, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(VirtualCoverage.getName(version), null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A completely empty ddms:virtualCoverage element was found.";
			String locator = "ddms:virtualCoverage";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_PROTOCOL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
			
			component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture(false));
			VirtualCoverage component = new VirtualCoverage(TEST_ADDRESS, TEST_PROTOCOL, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new VirtualCoverage(TEST_ADDRESS, TEST_PROTOCOL, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			VirtualCoverage.Builder builder = new VirtualCoverage.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new VirtualCoverage.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new VirtualCoverage.Builder();
			builder.setAddress(TEST_ADDRESS);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
