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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:subjectCoverage elements</p>
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
			metrics.add(new ProductionMetric("FOOD", "AFG", SecurityAttributesTest.getFixture()));
		return (metrics);
	}

	/**
	 * Creates a test fixture
	 */
	private List<NonStateActor> getActorFixture() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		List<NonStateActor> actors = new ArrayList<NonStateActor>();
		if (version.isAtLeast("4.0"))
			actors.add(new NonStateActor("Laotian Monks", new Integer(1), SecurityAttributesTest.getFixture()));
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
				SecurityAttributesTest.getFixture().addTo(element);
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
				: SecurityAttributesTest.getFixture();
			component = new SubjectCoverage(keywords, categories, metrics, actors, attr);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to manage the deprecated Subject wrapper element
	 * 
	 * @param innerElement the element containing the guts of this component
	 * @return the element itself in DDMS 4.0 or later, or the element wrapped in another element
	 */
	private Element wrapInnerElement(Element innerElement) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String name = SubjectCoverage.getName(version);
		if (version.isAtLeast("4.0")) {
			innerElement.setLocalName(name);
			return (innerElement);
		}
		Element element = Util.buildDDMSElement(name, null);
		element.appendChild(innerElement);
		return (element);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = version.isAtLeast("4.0") ? "subjectCoverage." : "subjectCoverage.Subject.";
		StringBuffer text = new StringBuffer();
		for (Keyword keyword : getKeywords())
			text.append(keyword.getOutput(isHTML, prefix));
		for (Category category : getCategories())
			text.append(category.getOutput(isHTML, prefix));

		if (version.isAtLeast("4.0")) {
			for (ProductionMetric metric : getMetricFixture())
				text.append(metric.getOutput(isHTML, prefix));
			for (NonStateActor actor : getActorFixture())
				text.append(actor.getOutput(isHTML, prefix));
		}
		if (version.isAtLeast("3.0")) {
			text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, prefix));
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
		xml.append("<ddms:subjectCoverage ").append(getXmlnsDDMS());
		if (version.isAtLeast("3.0")) {
			xml.append(" ").append(getXmlnsISM()).append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		}
		xml.append(">\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
			xml.append("\t<ddms:keyword ddms:value=\"Uri\" />\n");
			xml.append("\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ").append(
				"ddms:label=\"DDMS\" />\n");
			xml.append("\t<ddms:productionMetric ddms:subject=\"FOOD\" ddms:coverage=\"AFG\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" />\n");
			xml.append("\t<ddms:nonStateActor ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ddms:order=\"1\">Laotian Monks</ddms:nonStateActor>\n");
		}
		else {
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				SubjectCoverage.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			testConstructor(WILL_SUCCEED, wrapInnerElement(subjectElement));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(), getActorFixture());

			// No optional fields
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No keywords or categories
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			testConstructor(WILL_FAIL, wrapInnerElement(subjectElement));
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No keywords or categories
			testConstructor(WILL_FAIL, null, null, null, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Identical keywords
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			subjectElement.appendChild(getKeywords().get(0).getXOMElementCopy());
			component = testConstructor(WILL_SUCCEED, wrapInnerElement(subjectElement));
			assertEquals(1, component.getValidationWarnings().size());
			String text = "1 or more keywords have the same value.";
			String locator = version.isAtLeast("4.0") ? "ddms:subjectCoverage" : "ddms:subjectCoverage/ddms:Subject";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Identical categories
			subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
			subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
			component = testConstructor(WILL_SUCCEED, wrapInnerElement(subjectElement));
			assertEquals(1, component.getValidationWarnings().size());
			text = "1 or more categories have the same value.";
			locator = version.isAtLeast("4.0") ? "ddms:subjectCoverage" : "ddms:subjectCoverage/ddms:Subject";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Identical productionMetrics
			if (version.isAtLeast("4.0")) {
				subjectElement = Util.buildDDMSElement("Subject", null);
				subjectElement.appendChild(getCategories().get(0).getXOMElementCopy());
				subjectElement.appendChild(getMetricFixture().get(0).getXOMElementCopy());
				subjectElement.appendChild(getMetricFixture().get(0).getXOMElementCopy());
				component = testConstructor(WILL_SUCCEED, wrapInnerElement(subjectElement));
				assertEquals(1, component.getValidationWarnings().size());
				text = "1 or more productionMetrics have the same value.";
				locator = "ddms:subjectCoverage";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(),
				getMetricFixture(), getActorFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SubjectCoverage dataComponent = testConstructor(WILL_SUCCEED, null, getCategories(), getMetricFixture(),
				getActorFixture());
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

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(),
				getActorFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getKeywords(), getCategories(), getMetricFixture(),
				getActorFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCategoryReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Category> categories = getCategories();
			testConstructor(WILL_SUCCEED, null, categories, null, null);
			testConstructor(WILL_SUCCEED, null, categories, null, null);
		}
	}

	public void testKeywordReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Keyword> keywords = getKeywords();
			testConstructor(WILL_SUCCEED, keywords, null, null, null);
			testConstructor(WILL_SUCCEED, keywords, null, null, null);
		}
	}

	public void testMetricReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<ProductionMetric> metrics = getMetricFixture();
			testConstructor(WILL_SUCCEED, getKeywords(), null, metrics, null);
			testConstructor(WILL_SUCCEED, getKeywords(), null, metrics, null);
		}
	}

	public void testActorReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<NonStateActor> actors = getActorFixture();
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, actors);
			testConstructor(WILL_SUCCEED, getKeywords(), null, null, actors);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
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
			new SubjectCoverage(getKeywords(), getCategories(), null, null, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Keyword> keywords = getKeywords();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(keywords, null, null, null, SecurityAttributesTest.getFixture());
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		List<Category> categories = getCategories();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(null, categories, null, null, SecurityAttributesTest.getFixture());
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
			assertNotNull(builder.getKeywords().get(1));
			assertNotNull(builder.getCategories().get(1));
			assertNotNull(builder.getProductionMetrics().get(1));
		}
	}
}
