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
package buri.ddmsence.ddms.security;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:security elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SecurityTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public SecurityTest() {
		super("security.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure, Element element) {
		Security component = null;
		try {
			component = new Security(element);
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
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure) {
		Security component = null;
		try {
			component = new Security(SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"security.excludeFromRollup\" content=\"true\" />\n");
		html.append("<meta name=\"security.classification\" content=\"U\" />\n");
		html.append("<meta name=\"security.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Exclude From Rollup: true\n");
		text.append("Classification: U\n");
		text.append("ownerProducer: USA\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:security xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" xmlns:ICISM=\"").append(ICISM_NAMESPACE).append("\" ");
		xml.append("ICISM:excludeFromRollup=\"true\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\" />");
		return (xml.toString());
	}
	
	public void testNameAndNamespace() {
		Security component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Security.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + Security.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
	}

	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED);
	}
	
	public void testElementConstructorInvalid() {
		// Missing excludeFromRollup
		Element element = Util.buildDDMSElement(Security.NAME, null);
		testConstructor(WILL_FAIL, element);
		
		// Invalid excludeFromRollup
		element = Util.buildDDMSElement(Security.NAME, null);
		Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", ICISM_NAMESPACE, "aardvark");
		testConstructor(WILL_FAIL, element);
	}
		
	public void testDataConstructorInvalid() {
		// Bad security attributes
		try {
			new Security((SecurityAttributes) null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testWarnings() {
		// No warnings
		Security component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
	}
	
	public void testConstructorEquality() {
		Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Security dataComponent = testConstructor(WILL_SUCCEED);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		// Can't test this yet.
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		Security component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());

		component = testConstructor(WILL_SUCCEED);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Security component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());

		component = testConstructor(WILL_SUCCEED);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Security component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
	
}
