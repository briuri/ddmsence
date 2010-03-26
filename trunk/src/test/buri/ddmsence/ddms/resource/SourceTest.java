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
 * <p>Tests related to ddms:source elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SourceTest extends AbstractComponentTestCase {
	
	private static final String TEST_QUALIFIER = "URL";
	private static final String TEST_VALUE = "http://www.xmethods.com";
	private static final String TEST_SCHEMA_QUALIFIER = "WSDL";
	private static final String TEST_SCHEMA_HREF = "http://www.xmethods.com/sd/2001/TemperatureService?wsdl"; 
	
	/**
	 * Constructor
	 */
	public SourceTest() {
		super("source.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Source testConstructor(boolean expectFailure, Element element) {
		Source component = null;
		try {
			component = new Source(element);
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
	 * @param schemaQualifier the value of the schemaQualifier attribute
	 * @param schemaHref the value of the schemaHref attribute
	 * @return a valid object
	 */
	private Source testConstructor(boolean expectFailure, String qualifier, String value, String schemaQualifier, String schemaHref) {
		Source component = null;
		try {
			component = new Source(qualifier, value, schemaQualifier, schemaHref, null);
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
		html.append("<meta name=\"source.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"source.value\" content=\"").append(TEST_VALUE).append("\" />\n");
		html.append("<meta name=\"source.schema.qualifier\" content=\"").append(TEST_SCHEMA_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"source.schema.href\" content=\"").append(TEST_SCHEMA_HREF).append("\" />\n");
		return (html.toString());
	}			
	
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Source Qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Source Value: ").append(TEST_VALUE).append("\n");
		text.append("Source Schema Qualifier: ").append(TEST_SCHEMA_QUALIFIER).append("\n");
		text.append("Source Schema href: ").append(TEST_SCHEMA_HREF).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:source xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE).append("\" ");
		xml.append("ddms:schemaQualifier=\"").append(TEST_SCHEMA_QUALIFIER).append("\" ");
		xml.append("ddms:schemaHref=\"").append(TEST_SCHEMA_HREF).append("\" />");
		return (xml.toString());							
	}
	
	public void testName() {
		Source component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Source.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + Source.NAME, component.getQualifiedName());
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Source.NAME, null);
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, "", "", "", "");
	}
	
	public void testElementConstructorInvalid() {
		// Href not URI
		Element element = Util.buildDDMSElement(Source.NAME, null);
		Util.addDDMSAttribute(element, "schemaHref", INVALID_URI);
		testConstructor(WILL_FAIL, element);
	}
	public void testDataConstructorInvalidHrefNotURI() {
		// Href not URI
		testConstructor(WILL_FAIL, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, INVALID_URI);
	}
	
	public void testWarnings() {
		// No warnings
		Source component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		Element element = Util.buildDDMSElement(Source.NAME, null);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A completely empty ddms:source element was found.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:source", component.getValidationWarnings().get(0).getLocator());
	}
	
	public void testConstructorEquality() {
		Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Source dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Source dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, DIFFERENT_VALUE, TEST_SCHEMA_HREF);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Source elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		Source component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Source component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Source component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}	
	
	public void testSecurityAttributes() throws InvalidDDMSException {
		Source component = new Source(TEST_QUALIFIER, TEST_VALUE, TEST_SCHEMA_QUALIFIER, TEST_SCHEMA_HREF, SecurityAttributesTest.getFixture(false));
		assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());		
	}
}