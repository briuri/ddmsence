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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:subjectCoverage elements</p>
 * 
 * <p>Assumes that unit testing on individual components of the ddms:Subject tag is done separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SubjectCoverageTest extends AbstractComponentTestCase {
	
	private final List<Keyword> TEST_KEYWORDS = new ArrayList<Keyword>();
	private final List<Category> TEST_CATEGORIES = new ArrayList<Category>();
	
	/**
	 * Constructor
	 */
	public SubjectCoverageTest() throws InvalidDDMSException {
		super("3.0/subjectCoverage.xml");
		TEST_KEYWORDS.add(new Keyword("DDMSence"));
		TEST_KEYWORDS.add(new Keyword("Uri"));
		TEST_CATEGORIES.add(new Category("urn:buri:ddmsence:categories", "DDMS", "DDMS"));
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element	the element to build from
	 * @return a valid object
	 */
	private SubjectCoverage testConstructor(boolean expectFailure, Element element) {
		SubjectCoverage component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new SubjectCoverage(element);
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
	 * @param keywords list of keywords
	 * @param categories list of categories
	 * @return a valid object
	 */
	private SubjectCoverage testConstructor(boolean expectFailure, List<Keyword> keywords, List<Category> categories) {
		SubjectCoverage component = null;
		try {
			component = new SubjectCoverage(keywords, categories, SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"subject.keyword\" content=\"DDMSence\" />\n");
		html.append("<meta name=\"subject.keyword\" content=\"Uri\" />\n");
		html.append("<meta name=\"subject.category.qualifier\" content=\"urn:buri:ddmsence:categories\" />\n");
		html.append("<meta name=\"subject.category.code\" content=\"DDMS\" />\n");
		html.append("<meta name=\"subject.category.label\" content=\"DDMS\" />\n");
		html.append("<meta name=\"subject.classification\" content=\"U\" />\n");
		html.append("<meta name=\"subject.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("Keyword: DDMSence\n");
		text.append("Keyword: Uri\n");
		text.append("Category Qualifier: urn:buri:ddmsence:categories\n");
		text.append("Category Code: DDMS\n");
		text.append("Category Label: DDMS\n");
		text.append("Subject Classification: U\n");
		text.append("Subject ownerProducer: USA\n");
		return (text.toString());
	}
	
	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subjectCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentNamespace());
		xml.append("\" xmlns:ICISM=\"urn:us:gov:ic:ism\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">\n\t<ddms:Subject>\n");
		xml.append("\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
		xml.append("\t\t<ddms:keyword ddms:value=\"Uri\" />\n");
		xml.append("\t\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ddms:label=\"DDMS\" />\n");
		xml.append("\t</ddms:Subject>\n</ddms:subjectCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));		
	}
	
	public void testNameAndNamespace() {
		SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(SubjectCoverage.NAME, component.getName());
		assertEquals(Util.DDMS_PREFIX, component.getPrefix());
		assertEquals(Util.DDMS_PREFIX + ":" + SubjectCoverage.NAME, component.getQualifiedName());
		
		// Wrong name/namespace
		Element element = Util.buildDDMSElement("wrongName", null);
		testConstructor(WILL_FAIL, element);
	}
	
	public void testElementConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, getValidElement());
		
		// No optional fields
		Element subjectElement = Util.buildDDMSElement("Subject", null);
		subjectElement.appendChild(TEST_KEYWORDS.get(0).getXOMElementCopy());
		Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
		element.appendChild(subjectElement);
		testConstructor(WILL_SUCCEED, element);
	}
	
	public void testDataConstructorValid() {
		// All fields
		testConstructor(WILL_SUCCEED, TEST_KEYWORDS, TEST_CATEGORIES);

		// No optional fields
		testConstructor(WILL_SUCCEED, TEST_KEYWORDS, null);
	}
		
	public void testElementConstructorInvalid() {
		// No keywords or categories
		Element subjectElement = Util.buildDDMSElement("Subject", null);
		Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
		element.appendChild(subjectElement);
		testConstructor(WILL_FAIL, element);
	}	

	public void testDataConstructorInvalid() {
		// No keywords or categories
		testConstructor(WILL_FAIL, null, null);
	}
	
	public void testWarnings() {
		// No warnings
		SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(0, component.getValidationWarnings().size());
		
		// Identical keywords
		Element subjectElement = Util.buildDDMSElement("Subject", null);
		subjectElement.appendChild(TEST_KEYWORDS.get(0).getXOMElementCopy());
		subjectElement.appendChild(TEST_KEYWORDS.get(0).getXOMElementCopy());
		Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
		element.appendChild(subjectElement);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("1 or more keywords have the same value.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());
		
		// Identical categories
		subjectElement = Util.buildDDMSElement("Subject", null);
		subjectElement.appendChild(TEST_CATEGORIES.get(0).getXOMElementCopy());
		subjectElement.appendChild(TEST_CATEGORIES.get(0).getXOMElementCopy());
		element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
		element.appendChild(subjectElement);
		component = testConstructor(WILL_SUCCEED, element);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("1 or more categories have the same value.", component.getValidationWarnings().get(0).getText());
		assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());
	}
	
	public void testConstructorEquality() {
		SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, TEST_KEYWORDS, TEST_CATEGORIES);
		assertEquals(elementComponent, dataComponent);
		assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
	}

	public void testConstructorInequalityDifferentValues() {
		SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, null, TEST_CATEGORIES);
		assertFalse(elementComponent.equals(dataComponent));
		
		dataComponent = testConstructor(WILL_SUCCEED, TEST_KEYWORDS, null);
		assertFalse(elementComponent.equals(dataComponent));
	}
	
	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement());
		Rights wrongComponent = new Rights(true, true, true);
		assertFalse(elementComponent.equals(wrongComponent));
	}
	
	public void testHTMLOutput() {
		SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
		
		component = testConstructor(WILL_SUCCEED, TEST_KEYWORDS, TEST_CATEGORIES);
		assertEquals(getExpectedHTMLOutput(), component.toHTML());
	}	
	
	public void testTextOutput() {
		SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedTextOutput(), component.toText());
		
		component = testConstructor(WILL_SUCCEED, TEST_KEYWORDS, TEST_CATEGORIES);
		assertEquals(getExpectedTextOutput(), component.toText());
	}
	
	public void testXMLOutput() {
		SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement());
		assertEquals(getExpectedXMLOutput(true), component.toXML());

		component = testConstructor(WILL_SUCCEED, TEST_KEYWORDS, TEST_CATEGORIES);
		assertEquals(getExpectedXMLOutput(false), component.toXML());	
	}
		
	public void testCategoryReuse() {
		testConstructor(WILL_SUCCEED, null, TEST_CATEGORIES);
		testConstructor(WILL_SUCCEED, null, TEST_CATEGORIES);
	}
	
	public void testKeywordReuse() {
		testConstructor(WILL_SUCCEED, TEST_KEYWORDS, null);
		testConstructor(WILL_SUCCEED, TEST_KEYWORDS, null);
	}
	
	public void testSecurityAttributes() throws InvalidDDMSException {
		SubjectCoverage component = new SubjectCoverage(TEST_KEYWORDS, TEST_CATEGORIES, SecurityAttributesTest.getFixture(false));
		assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());		
	}
}
