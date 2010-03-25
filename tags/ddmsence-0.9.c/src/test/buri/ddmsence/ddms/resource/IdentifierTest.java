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
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:Identifier elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class IdentifierTest extends AbstractComponentTestCase {
	
	private static final String TEST_QUALIFIER = "URI";
	private static final String TEST_VALUE = "urn:buri:ddmsence:testIdentifier";
	
	/**
	 * Constructor
	 */
	public IdentifierTest() {
		super("identifier.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Identifier testConstructor(boolean expectFailure, Element element) {
		Identifier component = null;
		try {
			component = new Identifier(element);
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
	private Identifier testConstructor(boolean expectFailure, String qualifier, String value) {
		Identifier component = null;
		try {
			component = new Identifier(qualifier, value);
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
		html.append("<meta name=\"identifier.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"identifier.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Identifier qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Identifier value: ").append(TEST_VALUE).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:identifier xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}
	
	public void testName() {
		Identifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Identifier.NAME, component.getName());
	}
	
	public void testElementConstructorValid() {
		testConstructor(WILL_SUCCEED, getValidElement());
	}
	
	public void testDataConstructorValid() {
		testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
	}
	
	public void testElementConstructorInvalid() {
		// Missing qualifier
		Element element = Util.buildDDMSElement(Identifier.NAME, null);
		Util.addDDMSAttribute(element, "value", TEST_VALUE);
		testConstructor(WILL_FAIL, element);
		
		// Empty qualifier
		element = Util.buildDDMSElement(Identifier.NAME, null);
		Util.addDDMSAttribute(element, "qualifier", "");
		Util.addDDMSAttribute(element, "value", TEST_VALUE);
		testConstructor(WILL_FAIL, element);
		
		// Missing value
		element = Util.buildDDMSElement(Identifier.NAME, null);
		Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
		testConstructor(WILL_FAIL, element);
		
		// Empty value
		element = Util.buildDDMSElement(Identifier.NAME, null);
		Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
		Util.addDDMSAttribute(element, "value", "");
		testConstructor(WILL_FAIL, element);
		
		// Qualifier not URI
		element = Util.buildDDMSElement(Identifier.NAME, null);
		Util.addDDMSAttribute(element, "qualifier", INVALID_URI);
		Util.addDDMSAttribute(element, "value", TEST_VALUE);
		testConstructor(WILL_FAIL, element);
	}

	public void testDataConstructorInvalid() {
		// Missing qualifier
		testConstructor(WILL_FAIL, null, TEST_VALUE);
		
		// Empty qualifier
		testConstructor(WILL_FAIL, "", TEST_VALUE);
		
		// Missing value
		testConstructor(WILL_FAIL, TEST_QUALIFIER, null);
		
		// Empty value
		testConstructor(WILL_FAIL, TEST_QUALIFIER, "");
		
		// Qualifier not URI
		testConstructor(WILL_FAIL, INVALID_URI, TEST_VALUE);
	}
	
	public void testWarnings() {
		// No warnings
		Identifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		Identifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Identifier dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		Identifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Identifier dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Identifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Identifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Identifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Identifier component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
}
