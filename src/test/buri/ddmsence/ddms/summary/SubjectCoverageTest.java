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

	/**
	 * Creates a test fixture
	 */
	private List<Keyword> getKeywords() throws InvalidDDMSException {
		List<Keyword> keywords = new ArrayList<Keyword>();
		keywords.add(new Keyword("DDMSence", null));
		keywords.add(new Keyword("Uri", null));
		return (keywords);
	}

	/**
	 * Creates a test fixture
	 */
	private List<Category> getCategories() throws InvalidDDMSException {
		List<Category> categories = new ArrayList<Category>();
		categories.add(new Category("urn:buri:ddmsence:categories", "DDMS", "DDMS", null));
		return (categories);
	}

	/**
	 * Creates a test fixture
	 */
	private List<ProductionMetric> getMetricFixture() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		List<ProductionMetric> metrics = new ArrayList<ProductionMetric>();
		if (version.isAtLeast("4.0"))
			metrics.add(new ProductionMetric("FOOD", "AFG", SecurityAttributesTest.getFixture(false)));
		return (metrics);
	}

	/**
	 * Creates a test fixture
	 */
	private List<NonStateActor> getActorFixture() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		List<NonStateActor> actors = new ArrayList<NonStateActor>();
		if (version.isAtLeast("4.0"))
			actors.add(new NonStateActor("Laotian Monks", new Integer(1), SecurityAttributesTest.getFixture(false)));
		return (actors);
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
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
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
	 * @param metrics list of production metrics
	 * @param actors list of non-state actors
	 * @return a valid object
	 */
	private SubjectCoverage testConstructor(boolean expectFailure, List<Keyword> keywords, List<Category> categories,
		List<ProductionMetric> metrics, List<NonStateActor> actors) {
		SubjectCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture(false);
			component = new SubjectCoverage(keywords, categories, metrics, actors, attr);
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = "subjectCoverage.";
		if (!version.isAtLeast("4.0"))
			prefix += "Subject.";		
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"").append(prefix).append("keyword\" content=\"DDMSence\" />\n");
		html.append("<meta name=\"").append(prefix).append("keyword\" content=\"Uri\" />\n");
		html.append("<meta name=\"").append(prefix).append("category.qualifier\" content=\"urn:buri:ddmsence:categories\" />\n");
		html.append("<meta name=\"").append(prefix).append("category.code\" content=\"DDMS\" />\n");
		html.append("<meta name=\"").append(prefix).append("category.label\" content=\"DDMS\" />\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
			html.append("<meta name=\"").append(prefix).append("productionMetric.subject\" content=\"FOOD\" />\n");
			html.append("<meta name=\"").append(prefix).append("productionMetric.coverage\" content=\"AFG\" />\n");
			html.append("<meta name=\"").append(prefix).append("productionMetric.classification\" content=\"U\" />\n");
			html.append("<meta name=\"").append(prefix).append("productionMetric.ownerProducer\" content=\"USA\" />\n");
			html.append("<meta name=\"").append(prefix).append("nonStateActor.value\" content=\"Laotian Monks\" />\n");
			html.append("<meta name=\"").append(prefix).append("nonStateActor.order\" content=\"1\" />\n");
			html.append("<meta name=\"").append(prefix).append("nonStateActor.classification\" content=\"U\" />\n");
			html.append("<meta name=\"").append(prefix).append("nonStateActor.ownerProducer\" content=\"USA\" />\n");
		}
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
			html.append("<meta name=\"subjectCoverage.classification\" content=\"U\" />\n");
			html.append("<meta name=\"subjectCoverage.ownerProducer\" content=\"USA\" />\n");
		}
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = "subjectCoverage.";
		if (!version.isAtLeast("4.0"))
			prefix += "Subject.";	
		StringBuffer text = new StringBuffer();
		text.append(prefix).append("keyword: DDMSence\n");
		text.append(prefix).append("keyword: Uri\n");
		text.append(prefix).append("category qualifier: urn:buri:ddmsence:categories\n");
		text.append(prefix).append("category code: DDMS\n");
		text.append(prefix).append("category label: DDMS\n");
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.0")) {
			text.append(prefix).append("productionMetric.subject: FOOD\n");
			text.append(prefix).append("productionMetric.coverage: AFG\n");
			text.append(prefix).append("productionMetric classification: U\n");
			text.append(prefix).append("productionMetric ownerProducer: USA\n");
			text.append(prefix).append("nonStateActor.value: Laotian Monks\n");
			text.append(prefix).append("nonStateActor.order: 1\n");
			text.append(prefix).append("nonStateActor classification: U\n");
			text.append(prefix).append("nonStateActor ownerProducer: USA\n");
		}
		if (DDMSVersion.getCurrentVersion().isAtLeast("3.0")) {
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subjectCoverage xmlns:ddms=\"").append(version.getNamespace()).append("\"");
		if (version.isAtLeast("3.0")) {
			xml.append(" xmlns:ISM=\"").append(version.getIsmNamespace()).append(
				"\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
			xml.append("\t<ddms:keyword ddms:value=\"Uri\" />\n");
			xml.append("\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ").append(
				"ddms:label=\"DDMS\" />\n");
			xml.append("\t<ddms:productionMetric ddms:subject=\"FOOD\" ddms:coverage=\"AFG\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" />\n");
			xml.append("\t<ddms:nonStateActor ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ddms:order=\"1\">Laotian Monks</ddms:nonStateActor>\n");
		} else {
			xml.append("\t<ddms:Subject>\n");
			xml.append("\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
			xml.append("\t\t<ddms:keyword ddms:value=\"Uri\" />\n");
			xml.append("\t\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ")
				.append("ddms:label=\"DDMS\" />\n");
			xml.append("\t</ddms:Subject>\n");
		}
		xml.append("</ddms:subjectCoverage>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(SubjectCoverage.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + SubjectCoverage.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			if (version.isAtLeast("4.0")) {
				Element subjectElement = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				testConstructor(WILL_SUCCEED, subjectElement);
			}
			else {
				Element subjectElement = Util.buildDDMSElement("Subject", null);
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				Element element = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				element.appendChild(subjectElement);
				testConstructor(WILL_SUCCEED, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), getActorFixture());

			// No optional fields
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No keywords or categories
			if (version.isAtLeast("4.0")) {
				Element subjectElement = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				testConstructor(WILL_FAIL, subjectElement);				
			}
			else {
				Element subjectElement = Util.buildDDMSElement("Subject", null);
				Element element = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				element.appendChild(subjectElement);
				testConstructor(WILL_FAIL, element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No keywords or categories
			testConstructor(WILL_FAIL, null, null, null, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// Identical keywords
			if (version.isAtLeast("4.0")) {
				Element subjectElement = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				component = testConstructor(WILL_SUCCEED, subjectElement);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("1 or more keywords have the same value.", component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:subjectCoverage", component.getValidationWarnings().get(0).getLocator());

			}
			else {
				Element subjectElement = Util.buildDDMSElement("Subject", null);
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
				Element element = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				element.appendChild(subjectElement);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("1 or more keywords have the same value.", component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());
			}

			// Identical categories
			if (version.isAtLeast("4.0")) {
				Element subjectElement = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				component = testConstructor(WILL_SUCCEED, subjectElement);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("1 or more categories have the same value.", component.getValidationWarnings().get(0)
					.getText());
				assertEquals("/ddms:subjectCoverage", component.getValidationWarnings().get(0).getLocator());
			}
			else {
				Element subjectElement = Util.buildDDMSElement("Subject", null);
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				Element element = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				element.appendChild(subjectElement);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("1 or more categories have the same value.", component.getValidationWarnings().get(0)
					.getText());
				assertEquals("/ddms:subjectCoverage/ddms:Subject", component.getValidationWarnings().get(0).getLocator());
			}
			
			// Identical productionMetrics
			if (version.isAtLeast("4.0")) {
				Element subjectElement = Util.buildDDMSElement(SubjectCoverage.getName(version), null);
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				subjectElement.appendChild(getMetricFixture().get(0).getXOMElementCopy());
				subjectElement.appendChild(getMetricFixture().get(0).getXOMElementCopy());
				component = testConstructor(WILL_SUCCEED, subjectElement);
				assertEquals(1, component.getValidationWarnings().size());
				assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
				assertEquals("1 or more productionMetrics have the same value.", component.getValidationWarnings().get(0)
					.getText());
				assertEquals("/ddms:subjectCoverage", component.getValidationWarnings().get(0).getLocator());
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(),
				getMetricFixture(), getActorFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// Backwards compatible constructors
			assertEquals(new SubjectCoverage(getKeywords(), getCategories(), null), new SubjectCoverage(getKeywords(),
				getCategories(), null, null, null));
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, null, getCategories(), getMetricFixture(), getActorFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), null, getMetricFixture(), getActorFixture());
			assertFalse(elementComponent.equals(dataComponent));
			
			if (version.isAtLeast("4.0")) {
				dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), null, getActorFixture());
				assertFalse(elementComponent.equals(dataComponent));
				
				dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), null);
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), getActorFixture());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), getActorFixture());
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), getActorFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCategoryReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<Category> categories = getCategories();
			testConstructor(WILL_SUCCEED, null, categories, null, null);
			testConstructor(WILL_SUCCEED, null, categories, null, null);
		}
	}

	public void testKeywordReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<Keyword> keywords = getKeywords();
			testConstructor(WILL_SUCCEED, keywords, null, null, null);
			testConstructor(WILL_SUCCEED, keywords, null, null, null);
		}
	}

	public void testMetricReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<ProductionMetric> metrics = getMetricFixture();
			testConstructor(WILL_SUCCEED, getKeywords(), null, metrics, null);
			testConstructor(WILL_SUCCEED, getKeywords(), null, metrics, null);
		}
	}
	
	public void testActorReuse() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			List<NonStateActor> actors = getActorFixture();
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, actors);
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, actors);
		}
	}
	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture(false));
			SubjectCoverage component = new SubjectCoverage(getKeywords(), getCategories(), null, null, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new SubjectCoverage(getKeywords(), getCategories(), null, null, SecurityAttributesTest.getFixture(false));
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
			new SubjectCoverage(keywords, null, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		List<Category> categories = getCategories();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(null, categories, null, null, SecurityAttributesTest.getFixture(false));
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

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
			
			if (version.isAtLeast("4.0")) {
				// Skip empty metrics
				builder = new SubjectCoverage.Builder();
				ProductionMetric.Builder emptyProductionMetricBuilder = new ProductionMetric.Builder();
				ProductionMetric.Builder fullProductionMetricBuilder = new ProductionMetric.Builder();
				fullProductionMetricBuilder.setSubject("FOOD");
				fullProductionMetricBuilder.setCoverage("AFG");
				builder.getKeywords().get(0).setValue("test");
				builder.getProductionMetrics().add(emptyProductionMetricBuilder);
				builder.getProductionMetrics().add(fullProductionMetricBuilder);
				assertEquals(1, builder.commit().getProductionMetrics().size());
				
				// Skip empty actors
				builder = new SubjectCoverage.Builder();
				NonStateActor.Builder emptyNonStateActorBuilder = new NonStateActor.Builder();
				NonStateActor.Builder fullNonStateActorBuilder = new NonStateActor.Builder();
				fullNonStateActorBuilder.setValue("Laotian Monks");
				builder.getKeywords().get(0).setValue("test");
				builder.getNonStateActors().add(emptyNonStateActorBuilder);
				builder.getNonStateActors().add(fullNonStateActorBuilder);
				assertEquals(1, builder.commit().getNonStateActors().size());
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
			assertNotNull(builder.getKeywords().get(1));
			assertNotNull(builder.getCategories().get(1));
			assertNotNull(builder.getProductionMetrics().get(1));
		}
	}
}
