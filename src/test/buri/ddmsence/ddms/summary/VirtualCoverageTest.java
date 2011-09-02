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
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
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
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null : SecurityAttributesTest
				.getFixture(false);
			component = new VirtualCoverage(address, protocol, attr);
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
		html.append("<meta name=\"virtual.address\" content=\"").append(TEST_ADDRESS).append("\" />\n");
		html.append("<meta name=\"virtual.protocol\" content=\"").append(TEST_PROTOCOL).append("\" />\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			html.append("<meta name=\"virtual.classification\" content=\"U\" />\n");
			html.append("<meta name=\"virtual.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("virtual address: ").append(TEST_ADDRESS).append("\n");
		text.append("virtual protocol: ").append(TEST_PROTOCOL).append("\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			text.append("virtual classification: U\n");
			text.append("virtual ownerProducer: USA\n");
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:virtualCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			xml.append(" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIsmNamespace()).append("\"");
		}
		xml.append(" ddms:address=\"").append(TEST_ADDRESS).append("\" ddms:protocol=\"").append(TEST_PROTOCOL)
			.append("\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			xml.append(" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(VirtualCoverage.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + VirtualCoverage.NAME, component.getQualifiedName());

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
			Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// address without protocol
			Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
			Util.addDDMSAttribute(element, "address", TEST_ADDRESS);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// address without protocol		
			testConstructor(WILL_FAIL, TEST_ADDRESS, null);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty element
			Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("A completely empty ddms:virtualCoverage element was found.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:virtualCoverage", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_PROTOCOL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);			
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			VirtualCoverage component = new VirtualCoverage(TEST_ADDRESS, TEST_PROTOCOL, attr);
			if (!DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
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
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
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
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
