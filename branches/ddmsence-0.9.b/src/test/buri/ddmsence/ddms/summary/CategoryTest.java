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
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:category elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class CategoryTest extends AbstractComponentTestCase {
		
	private static final String TEST_QUALIFIER = "http://metadata.dod.mil/mdr/artifiact/MET/severeWeatherCode_enum/xml";
	private static final String TEST_CODE = "T";
	private static final String TEST_LABEL = "TORNADO";
	
	/**
	 * Constructor
	 */
	public CategoryTest() {
		super("category.xml");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * @param expectFailure	true if this operation is expected to fail, false otherwise
	 * @param element	the element to build from
	 * 
	 * @return a valid object
	 */
	private Category testConstructor(boolean expectFailure, Element element) {
		Category component = null;
		try {
			component = new Category(element);
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
	 * @param qualifier the qualifier (optional)
	 * @param code the code (optional)
	 * @param label the label (required)
	 * @return a valid object
	 */
	private Category testConstructor(boolean expectFailure, String qualifier, String code, String label) {
		Category component = null;
		try {
			component = new Category(qualifier, code, label);
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
		html.append("<meta name=\"subject.category.qualifier\" content=\"").append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"subject.category.code\" content=\"").append(TEST_CODE).append("\" />\n");
		html.append("<meta name=\"subject.category.label\" content=\"").append(TEST_LABEL).append("\" />\n");
		
		return (html.toString());
	}
 					
	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Category Qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append("Category Code: ").append(TEST_CODE).append("\n");
		text.append("Category Label: ").append(TEST_LABEL).append("\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:category xmlns:ddms=\"").append(DDMS_NAMESPACE).append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ");
		xml.append("ddms:code=\"").append(TEST_CODE).append("\" ");
		xml.append("ddms:label=\"").append(TEST_LABEL).append("\" />");
		return (xml.toString());
	}
	
	public void testName() {
		Category component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(Category.NAME, component.getName());
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element element = Util.buildDDMSElement(Category.NAME, null);
		Util.addDDMSAttribute(element, "label", TEST_LABEL);
		testConstructor(WILL_SUCCEED, element);
	}

	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
		
		// No optional fields
		testConstructor(WILL_SUCCEED, "", "", TEST_LABEL);
	}
	
	public void testElementConstructorInvalid() {
		// Missing label
		Element element = Util.buildDDMSElement(Category.NAME, null);
		testConstructor(WILL_FAIL, element);
	}
			
	public void testDataConstructorInvalid() {
		// Missing label
		testConstructor(WILL_FAIL, TEST_QUALIFIER, TEST_CODE, null);
		
		// Qualifier not URI
		testConstructor(WILL_FAIL, INVALID_URI, TEST_CODE, TEST_LABEL);
	}
	
	public void testConstructorEquality() {
		Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Category dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}
	
	public void testConstructorInequalityDifferentValues() {
		Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Category dataComponent = testConstructor(WILL_SUCCEED, "", TEST_CODE, TEST_LABEL);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, "", TEST_LABEL);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, DIFFERENT_VALUE);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}

	public void testHTMLOutput() {
		Category component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	

	public void testTextOutput() {
		Category component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		Category component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
		assertEquals(getExpectedXMLOutput(), component.toXML());
	}
}
