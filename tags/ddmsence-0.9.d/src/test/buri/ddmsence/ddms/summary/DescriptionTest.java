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
 * <p>Tests related to ddms:description elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DescriptionTest extends AbstractComponentTestCase {
		
	private static final String TEST_VALUE = "A transformation service.";
	
	/**
	 * Constructor
	 */
	public DescriptionTest() {
		super("description.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Description testConstructor(boolean expectFailure, Element element) {
		Description component = null;
		try {
			component = new Description(element);
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
	 * @param value the description
	 * @return a valid object
	 */
	private Description testConstructor(boolean expectFailure, String value) {
		Description component = null;
		try {
			component = new Description(value, SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"description\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"description.classification\" content=\"U\" />\n");
		html.append("<meta name=\"description.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Description: ").append(TEST_VALUE).append("\n");
		text.append("Description Classification: U\n");
		text.append("Description ownerProducer: USA\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:description xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("xmlns:ICISM=\"urn:us:gov:ic:ism\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ddms:description>");
		return (xml.toString());
	}
	
	public void testNameAndNamespace() {
		Description component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Description.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + Description.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() throws InvalidDDMSException {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Description.NAME, null);
		SecurityAttributesTest.getFixture(false).addTo(element);
		testConstructor(WILL_SUCCEED, element);
	}
		
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_VALUE);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, "");
	}
	
	public void testDataConstructorInvalid() {
		// Bad security attributes
		try {
			new Description(TEST_VALUE, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testWarnings() throws InvalidDDMSException {
		// No warnings
		Description component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		// No value
		Element element = Util.buildDDMSElement(Description.NAME, null);
		SecurityAttributesTest.getFixture(false).addTo(element);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:description element was found with no description value.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:description", component.getValidationWarnings().get(0).getLocator());
	}
	
	public void testConstructorEquality() {
		Description elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Description dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		Description elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Description dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Description elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Description component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Description component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Description component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
}
