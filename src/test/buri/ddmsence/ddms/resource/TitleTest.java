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
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:title elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class TitleTest extends AbstractComponentTestCase {
		
	private static final String TEST_VALUE = "DDMSence";
	
	/**
	 * Constructor
	 */
	public TitleTest() {
		super("3.0/title.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Title testConstructor(boolean expectFailure, Element element) {
		Title component = null;
		try {
			component = new Title(element);
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
	 * @param title the title child text
	 * @return a valid object
	 */
	private Title testConstructor(boolean expectFailure, String title) {
		Title component = null;
		try {
			component = new Title(title, SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"title\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"title.classification\" content=\"U\" />\n");
		html.append("<meta name=\"title.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Title: ").append(TEST_VALUE).append("\n");
		text.append("Title Classification: U\n");
		text.append("Title ownerProducer: USA\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:title xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("xmlns:ICISM=\"urn:us:gov:ic:ism\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ddms:title>");
		return (xml.toString());
	}
	
	public void testNameAndNamespace() {
		Title component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Title.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + Title.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		testConstructor(WILL_SUCCEED, getValidElement());
	}
		
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_VALUE);
	}
	
	public void testElementConstructorInvalid() {
		// Missing child text
		Element element = Util.buildDDMSElement(Title.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// Empty child text
		element = Util.buildDDMSElement(Title.NAME, "");
		testConstructor(WILL_FAIL, element);
	}
		
	public void testDataConstructorInvalid() {
		// Missing child text
		testConstructor(WILL_FAIL, (String) null);
		
		// Empty child text
		testConstructor(WILL_FAIL, "");
	}
		
	public void testWarnings() {
		// No warnings
		Title component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
		
	public void testConstructorEquality() {
		Title elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Title dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		Title elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Title dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Title elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Title component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Title component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Title component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());
		
		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}	
}
