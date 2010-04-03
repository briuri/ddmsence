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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:type elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TypeTest extends AbstractComponentTestCase {
	
	private static final String TEST_QUALIFIER = "DCMITYPE";
	private static final String TEST_VALUE = "http://purl.org/dc/dcmitype/Text";
	
	/**
	 * Constructor
	 */
	public TypeTest() {
		super("3.0/type.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Type testConstructor(boolean expectFailure, Element element) {
		Type component = null;
		try {
			component = new Type(element);
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
	 * @param qualifier the qualifier value
	 * @param value		the value
	 * @return a valid object
	 */
	private Type testConstructor(boolean expectFailure, String qualifier, String value) {
		Type component = null;
		try {
			component = new Type(qualifier, value);
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
		html.append("<meta name=\"type.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"type.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Type qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Type: ").append(TEST_VALUE).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:type xmlns:ddms=\"").append(DDMSVersion.getCurrentNamespace()).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}
	
	public void testNameAndNamespace() {
		Type component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Type.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + Type.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Type.NAME, null);
		testConstructor(WILL_SUCCEED, element);
	}

	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, "", "");
	}
	
	public void testElementConstructorInvalid() {
		// Missing qualifier
		Element element = Util.buildDDMSElement(Type.NAME, null);
		Util.addDDMSAttribute(element, "value", TEST_VALUE);
		testConstructor(WILL_FAIL, element);
	}
		
	public void testDataConstructorInvalid() {
		// Missing qualifier
		testConstructor(WILL_FAIL, null, TEST_VALUE);
	}
	
	public void testWarnings() {
		// No warnings
		Type component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		// Qualifier without value
		Element element = Util.buildDDMSElement(Type.NAME, null);
		Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A qualifier has been set without an accompanying value attribute.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:type", component.getValidationWarnings().get(0).getLocator());
		
		// Neither attribute
		element = Util.buildDDMSElement(Type.NAME, null);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("Neither a qualifier nor a value was set on this type.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:type", component.getValidationWarnings().get(0).getLocator());
	}
	
	public void testConstructorEquality() {
		Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Type dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Type dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Type elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		Type component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Type component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Type component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
	
}
