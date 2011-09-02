/* Copyright 2010 - 2011 by Brian Uri!
   
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
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
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

	/**
	 * Constructor
	 */
	public SubjectCoverageTest() throws InvalidDDMSException {
		super("subjectCoverage.xml");
		
	}

	private static List<Keyword> getKeywords() throws InvalidDDMSException {
		List<Keyword> keywords = new ArrayList<Keyword>();
		keywords.add(new Keyword("DDMSence"));
		keywords.add(new Keyword("Uri"));
		return (keywords);
	}
	
	private static List<Category> getCategories() throws InvalidDDMSException {
		List<Category> categories = new ArrayList<Category>();
		categories.add(new Category("urn:buri:ddmsence:categories", "DDMS", "DDMS", null));
		return (categories);
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private SubjectCoverage testConstructor(boolean expectFailure, Element element) {
		SubjectCoverage component = null;
		try {
			if (!DDMSVersion.isCurrentVersion("2.0"))
				SecurityAttributesTest.getFixture(false).addTo(element);
			component = new SubjectCoverage(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param keywords list of keywords
	 * @param categories list of categories
	 * @return a valid object
	 */
	private SubjectCoverage testConstructor(boolean expectFailure, List<Keyword> keywords, List<Category> categories) {
		SubjectCoverage component = null;
		try {
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0")) ? null : SecurityAttributesTest
				.getFixture(false);
			component = new SubjectCoverage(keywords, categories, attr);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"subjectCoverage.Subject.keyword\" content=\"DDMSence\" />\n");
		html.append("<meta name=\"subjectCoverage.Subject.keyword\" content=\"Uri\" />\n");
		html.append("<meta name=\"subjectCoverage.Subject.category.qualifier\" content=\"urn:buri:ddmsence:categories\" />\n");
		html.append("<meta name=\"subjectCoverage.Subject.category.code\" content=\"DDMS\" />\n");
		html.append("<meta name=\"subjectCoverage.Subject.category.label\" content=\"DDMS\" />\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			html.append("<meta name=\"subjectCoverage.classification\" content=\"U\" />\n");
			html.append("<meta name=\"subjectCoverage.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append("keyword: DDMSence\n");
		text.append("keyword: Uri\n");
		text.append("category qualifier: urn:buri:ddmsence:categories\n");
		text.append("category code: DDMS\n");
		text.append("category label: DDMS\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			text.append("subjectCoverage classification: U\n");
			text.append("subjectCoverage ownerProducer: USA\n");
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subjectCoverage xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\"");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			xml.append(" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIsmNamespace())
				.append("\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n\t<ddms:Subject>\n");
		xml.append("\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
		xml.append("\t\t<ddms:keyword ddms:value=\"Uri\" />\n");
		xml.append("\t\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ")
			.append("ddms:label=\"DDMS\" />\n");
		xml.append("\t</ddms:Subject>\n</ddms:subjectCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(SubjectCoverage.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + SubjectCoverage.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
			element.appendChild(subjectElement);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getKeywords(), getCategories());

			// No optional fields
			testConstructor(WILL_SUCCEED, getKeywords(), null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No keywords or categories
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
			element.appendChild(subjectElement);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No keywords or categories
			testConstructor(WILL_FAIL, null, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());

			// Identical keywords
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
			element.appendChild(subjectElement);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("1 or more keywords have the same value.", component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());

			// Identical categories
			subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
			subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
			element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
			element.appendChild(subjectElement);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
			assertEquals("1 or more categories have the same value.", 
				component.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), getCategories());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, null, getCategories());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories());
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCategoryReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			List<Category> categories = getCategories();
			testConstructor(WILL_SUCCEED, null, categories);
			testConstructor(WILL_SUCCEED, null, categories);
		}
	}

	public void testKeywordReuse() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			List<Keyword> keywords = getKeywords();
			testConstructor(WILL_SUCCEED, keywords, null);
			testConstructor(WILL_SUCCEED, keywords, null);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			SubjectCoverage component = new SubjectCoverage(getKeywords(), getCategories(), attr);
			if (DDMSVersion.isCurrentVersion("2.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}
	
	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new SubjectCoverage(getKeywords(), getCategories(), SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Keyword> keywords = getKeywords();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(keywords, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		List<Category> categories = getCategories();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(null, categories, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			SubjectCoverage.Builder builder = new SubjectCoverage.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new SubjectCoverage.Builder();
			assertNull(builder.commit());
			
			
			// Skip empty Keywords
			builder = new SubjectCoverage.Builder();
			Keyword.Builder emptyBuilder = new Keyword.Builder();
			Keyword.Builder fullBuilder = new Keyword.Builder();
			fullBuilder.setValue("keyword");
			builder.getKeywords().add(emptyBuilder);
			builder.getKeywords().add(fullBuilder);
			assertEquals(1, builder.commit().getKeywords().size());

			// Skip empty Categories
			builder = new SubjectCoverage.Builder();
			Category.Builder emptyCategoryBuilder = new Category.Builder();
			Category.Builder fullCategoryBuilder = new Category.Builder();
			fullCategoryBuilder.setLabel("label");
			builder.getCategories().add(emptyCategoryBuilder);
			builder.getCategories().add(fullCategoryBuilder);
			assertEquals(1, builder.commit().getCategories().size());
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
			assertNotNull(builder.getKeywords().get(1));
			assertNotNull(builder.getCategories().get(1));
		}
	}
}
