/* Copyright 2010 by Brian Uri!
   
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
import buri.ddmsence.ddms.security.SecurityAttributesTest;
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
	 * @param element	the element to build from
	 * @return a valid object
	 */
	private VirtualCoverage testConstructor(boolean expectFailure, Element element) {
		VirtualCoverage component = null;
		try {
			component = new VirtualCoverage(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure	true if this operation is expected to succeed, false otherwise
	 * @param address the virtual address (optional)
	 * @param protocol the network protocol (optional, should be used if address is provided)
	 * @return a valid object
	 */
	private VirtualCoverage testConstructor(boolean expectFailure, String address, String protocol) {
		VirtualCoverage component = null;
		try {
			component = new VirtualCoverage(address, protocol, null);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
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
		html.append("<meta name=\"virtual.networkProtocol\" content=\"").append(TEST_PROTOCOL).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Virtual address: ").append(TEST_ADDRESS).append("\n");
		text.append("Network Protocol: ").append(TEST_PROTOCOL).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:virtualCoverage xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("ddms:address=\"").append(TEST_ADDRESS).append("\" ddms:protocol=\"").append(TEST_PROTOCOL).append("\" />");
		return (xml.toString());
	}
	
	public void testNameAndNamespace() {
		VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(VirtualCoverage.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + VirtualCoverage.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
		testConstructor(WILL_SUCCEED, element);
	}
		
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, null, null);
	}
	
	public void testElementConstructorInvalid() {
		// address without protocol
		Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
		Util.addDDMSAttribute(element, "address", TEST_ADDRESS);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testDataConstructorInvalid() {
		// address without protocol		
		testConstructor(WILL_FAIL, TEST_ADDRESS, null);
	}
	
	public void testWarnings() {
		// No warnings
		VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		// Empty element
		Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A completely empty ddms:virtualCoverage element was found.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:virtualCoverage", component.getValidationWarnings().get(0).getLocator());
	}
	
	public void testConstructorEquality() {
		VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		VirtualCoverage dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_PROTOCOL);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_ADDRESS, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		VirtualCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
		
	public void testHTMLOutput() {
		VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		VirtualCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_ADDRESS, TEST_PROTOCOL);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
	
	public void testSecurityAttributes() throws InvalidDDMSException {
		VirtualCoverage component = new VirtualCoverage(TEST_ADDRESS, TEST_PROTOCOL, SecurityAttributesTest.getFixture(false));
		assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());		
	}
}
