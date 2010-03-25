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
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:subtitle elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SubtitleTest extends AbstractComponentTestCase {
		
	private static final String TEST_VALUE = "Version 0.1";
	
	/**
	 * Constructor
	 */
	public SubtitleTest() {
		super("subtitle.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Subtitle testConstructor(boolean expectFailure, Element element) {
		Subtitle component = null;
		try {
			component = new Subtitle(element);
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
	 * @param description the description child text
	 * @return a valid object
	 */
	private Subtitle testConstructor(boolean expectFailure, String description) {
		Subtitle component = null;
		try {
			component = new Subtitle(description, SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"subtitle\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"subtitle.classification\" content=\"U\" />\n");
		html.append("<meta name=\"subtitle.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Subtitle: ").append(TEST_VALUE).append("\n");
		text.append("Subtitle Classification: U\n");
		text.append("Subtitle ownerProducer: USA\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subtitle xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("xmlns:ICISM=\"urn:us:gov:ic:ism\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ddms:subtitle>");
		return (xml.toString());
	}
	
	public void testName() {
		Subtitle component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Subtitle.NAME, component.getName());
	}
	
	public void testElementConstructorValid() throws InvalidDDMSException {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Subtitle.NAME, null);
		SecurityAttributesTest.getFixture(false).addTo(element);
		testConstructor(WILL_SUCCEED, element);
	}
		
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_VALUE);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, "");
	}
	
	public void testWarnings() throws InvalidDDMSException {
		// No warnings
		Subtitle component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		// No value
		Element element = Util.buildDDMSElement(Subtitle.NAME, null);
		SecurityAttributesTest.getFixture(false).addTo(element);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:subtitle element was found with no subtitle value.", component.getValidationWarnings().get(0).getText());
	}
	
	public void testConstructorEquality() {
		Subtitle elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Subtitle dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		Subtitle elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Subtitle dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Subtitle elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		Subtitle component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Subtitle component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Subtitle component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_VALUE);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
}
