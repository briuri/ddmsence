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
package buri.ddmsence.ddms.resource;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:Unknown elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class UnknownTest extends AbstractComponentTestCase {
			
	private static final String TEST_PRODUCER_TYPE = "creator";
	
	private static final List<String> TEST_NAMES = new ArrayList<String>();
	private static final List<String> TEST_PHONES = new ArrayList<String>();
	private static final List<String> TEST_EMAILS = new ArrayList<String>();
	static {
		TEST_NAMES.add("Unknown Entity");
		TEST_PHONES.add("703-882-1000");
		TEST_EMAILS.add("ddms@fgm.com");
	}
	
	/**
	 * Constructor
	 */
	public UnknownTest() {
		super("unknown.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Unknown testConstructor(boolean expectFailure, Element element) {
		Unknown component = null;
		try {
			Element producerElement = Util.buildDDMSElement(TEST_PRODUCER_TYPE, null);
			producerElement.appendChild(new Element(element));
			component = new Unknown(producerElement);
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
	 * @param names			an ordered list of names
	 * @param phones		an ordered list of phone numbers
	 * @param emails		an ordered list of email addresses
	 */
	private Unknown testConstructor(boolean expectFailure, List<String> names, List<String> phones, List<String> emails) {
		Unknown component = null;
		try {
			component = new Unknown(TEST_PRODUCER_TYPE, names, phones, emails, null);
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
		html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".entityType\" content=\"Unknown\" />\n");
		for (String name: TEST_NAMES)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".name\" content=\"").append(name).append("\" />\n");
		for (String phone: TEST_PHONES)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".phone\" content=\"").append(phone).append("\" />\n");
		for (String email: TEST_EMAILS)
			html.append("<meta name=\"").append(TEST_PRODUCER_TYPE).append(".email\" content=\"").append(email).append("\" />\n");
		return (html.toString());
	}
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(Util.capitalize(TEST_PRODUCER_TYPE)).append(" EntityType: Unknown\n");
		for (String name: TEST_NAMES)
			text.append("Name: ").append(name).append("\n");
		for (String phone: TEST_PHONES)
			text.append("Phone Number: ").append(phone).append("\n");
		for (String email: TEST_EMAILS)
			text.append("Email: ").append(email).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:").append(TEST_PRODUCER_TYPE).append(" xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\"><ddms:Unknown>\n");
		for (String name: TEST_NAMES)
			xml.append("\t<ddms:name>").append(name).append("</ddms:name>\n");
		for (String phone: TEST_PHONES)
			xml.append("\t<ddms:phone>").append(phone).append("</ddms:phone>\n");
		for (String email: TEST_EMAILS)
			xml.append("\t<ddms:email>").append(email).append("</ddms:email>\n");
		xml.append("</ddms:Unknown></ddms:").append(TEST_PRODUCER_TYPE).append(">");		
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testNameAndNamespace() {
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(TEST_PRODUCER_TYPE, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + TEST_PRODUCER_TYPE, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Unknown.NAME, null);
		element.appendChild(Util.buildDDMSElement("name", TEST_NAMES.get(0)));
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testElementConstructorInvalid() {
		// Missing name
		Element element = Util.buildDDMSElement(Unknown.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// Empty name
		element = Util.buildDDMSElement(Unknown.NAME, null);
		element.appendChild(Util.buildDDMSElement("name", ""));
		testConstructor(WILL_FAIL, element);
	}	
	
	public void testDataConstructorInvalid() {
		// Missing name		
		testConstructor(WILL_FAIL, null, TEST_PHONES, TEST_EMAILS);
		
		// Empty name
		List<String> names = new ArrayList<String>();
		names.add("");
		testConstructor(WILL_FAIL, names, TEST_PHONES, TEST_EMAILS);
	}
		
	public void testWarnings() {
		// No warnings
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
		
	public void testConstructorEquality() {
		Unknown elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Unknown dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		Unknown elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Unknown dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, null, TEST_EMAILS);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, null);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Unknown elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	
	
	public void testTextOutput() {
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_NAMES, TEST_PHONES, TEST_EMAILS);
		assertEquals(getExpectedXMLOutput(false), component.toXML());	
	}
		
	public void testEntityType() {
		Unknown component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Unknown.NAME, component.getEntityType());
	}
	
	public void testSecurityAttributes() throws InvalidDDMSException {
		Unknown component = new Unknown(TEST_PRODUCER_TYPE, TEST_NAMES, TEST_PHONES, TEST_EMAILS, SecurityAttributesTest.getFixture(false));
		assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());		
	}
}
