/* Copyright 2010 - 2013 by Brian Uri!
   
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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:subjectCoverage elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SubjectCoverageTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public SubjectCoverageTest() throws InvalidDDMSException {
		super("subjectCoverage.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @param order an order value for a nonstate actor inside
	 */
	public static SubjectCoverage getFixture(int order) {
		try {
			List<NonStateActor> actors = new ArrayList<NonStateActor>();
			actors.add(NonStateActorTest.getFixture(order));
			return (new SubjectCoverage(KeywordTest.getFixtureList(), null, null, DDMSVersion.getCurrentVersion()
				.isAtLeast("4.0.1") ? actors : null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static SubjectCoverage getFixture() {
		try {
			List<Keyword> keywords = new ArrayList<Keyword>();
			keywords.add(new Keyword("DDMSence", null));
			return (new SubjectCoverage(keywords, null, null, null, null));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param element the element to build from
	 * @return a valid object
	 */
	private SubjectCoverage getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		SubjectCoverage component = null;
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("3.0"))
				SecurityAttributesTest.getFixture().addTo(element);
			component = new SubjectCoverage(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param keywords list of keywords
	 * @param categories list of categories
	 * @param metrics list of production metrics
	 * @param actors list of non-state actors
	 * @return a valid object
	 */
	private SubjectCoverage getInstance(String message, List<Keyword> keywords, List<Category> categories,
		List<ProductionMetric> metrics, List<NonStateActor> actors) {
		boolean expectFailure = !Util.isEmpty(message);
		SubjectCoverage component = null;
		try {
			SecurityAttributes attr = (!DDMSVersion.getCurrentVersion().isAtLeast("3.0")) ? null
				: SecurityAttributesTest.getFixture();
			component = new SubjectCoverage(keywords, categories, metrics, actors, attr);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to manage the deprecated Subject wrapper element
	 * 
	 * @param innerElement the element containing the guts of this component
	 * @return the element itself in DDMS 4.0.1 or later, or the element wrapped in another element
	 */
	private Element wrapInnerElement(Element innerElement) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String name = SubjectCoverage.getName(version);
		if (version.isAtLeast("4.0.1")) {
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
		String prefix = version.isAtLeast("4.0.1") ? "subjectCoverage." : "subjectCoverage.Subject.";
		StringBuffer text = new StringBuffer();
		for (Keyword keyword : KeywordTest.getFixtureList())
			text.append(keyword.getOutput(isHTML, prefix, ""));
		for (Category category : CategoryTest.getFixtureList())
			text.append(category.getOutput(isHTML, prefix, ""));

		if (version.isAtLeast("4.0.1")) {
			for (ProductionMetric metric : ProductionMetricTest.getFixtureList())
				text.append(metric.getOutput(isHTML, prefix, ""));
			for (NonStateActor actor : NonStateActorTest.getFixtureList())
				text.append(actor.getOutput(isHTML, prefix, ""));
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
		if (version.isAtLeast("4.0.1")) {
			xml.append("\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
			xml.append("\t<ddms:keyword ddms:value=\"Uri\" />\n");
			xml.append("\t<ddms:category ddms:qualifier=\"urn:buri:ddmsence:categories\" ddms:code=\"DDMS\" ").append(
				"ddms:label=\"DDMS\" />\n");
			xml.append("\t<ddms:productionMetric ddms:subject=\"FOOD\" ddms:coverage=\"AFG\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" />\n");
			xml.append("\t<ddms:nonStateActor ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ddms:order=\"1\"");
			if (version.isAtLeast("4.1")) {
				xml.append(" ddms:qualifier=\"urn:sample\"");
			}
			xml.append(">Laotian Monks</ddms:nonStateActor>\n");
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

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				SubjectCoverage.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(KeywordTest.getFixtureList().get(0).getXOMElementCopy());
			getInstance(SUCCESS, wrapInnerElement(subjectElement));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest.getFixtureList(), ProductionMetricTest
				.getFixtureList(), NonStateActorTest.getFixtureList());

			// No optional fields
			getInstance(SUCCESS, KeywordTest.getFixtureList(), null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No keywords or categories
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			getInstance("At least 1 keyword or category must exist.", wrapInnerElement(subjectElement));
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No keywords or categories
			getInstance("At least 1 keyword or category must exist.", null, null, null, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			
			SubjectCoverage component = getInstance(SUCCESS, getValidElement(sVersion));

			// 4.1 ddms:qualifier element used
			if (version.isAtLeast("4.1")) {
				assertEquals(1, component.getValidationWarnings().size());	
				String text = "The ddms:qualifier attribute in this DDMS component";
				String locator = "ddms:subjectCoverage/ddms:nonStateActor";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
			// No warnings 
			else {
				assertEquals(0, component.getValidationWarnings().size());
			}
			
			// Identical keywords
			Element subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(KeywordTest.getFixtureList().get(0).getXOMElementCopy());
			subjectElement.appendChild(KeywordTest.getFixtureList().get(0).getXOMElementCopy());
			component = getInstance(SUCCESS, wrapInnerElement(subjectElement));
			assertEquals(1, component.getValidationWarnings().size());
			String text = "1 or more keywords have the same value.";
			String locator = version.isAtLeast("4.0.1") ? "ddms:subjectCoverage" : "ddms:subjectCoverage/ddms:Subject";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Identical categories
			subjectElement = Util.buildDDMSElement("Subject", null);
			subjectElement.appendChild(CategoryTest.getFixtureList().get(0).getXOMElementCopy());
			subjectElement.appendChild(CategoryTest.getFixtureList().get(0).getXOMElementCopy());
			component = getInstance(SUCCESS, wrapInnerElement(subjectElement));
			assertEquals(1, component.getValidationWarnings().size());
			text = "1 or more categories have the same value.";
			locator = version.isAtLeast("4.0.1") ? "ddms:subjectCoverage" : "ddms:subjectCoverage/ddms:Subject";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// Identical productionMetrics
			if (version.isAtLeast("4.0.1")) {
				subjectElement = Util.buildDDMSElement("Subject", null);
				subjectElement.appendChild(CategoryTest.getFixtureList().get(0).getXOMElementCopy());
				subjectElement.appendChild(ProductionMetricTest.getFixtureList().get(0).getXOMElementCopy());
				subjectElement.appendChild(ProductionMetricTest.getFixtureList().get(0).getXOMElementCopy());
				component = getInstance(SUCCESS, wrapInnerElement(subjectElement));
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
			SubjectCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			SubjectCoverage dataComponent = getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest
				.getFixtureList(), ProductionMetricTest.getFixtureList(), NonStateActorTest.getFixtureList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			SubjectCoverage dataComponent = getInstance(SUCCESS, null, CategoryTest.getFixtureList(),
				ProductionMetricTest.getFixtureList(), NonStateActorTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, KeywordTest.getFixtureList(), null, ProductionMetricTest
				.getFixtureList(), NonStateActorTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.0.1")) {
				dataComponent = getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest.getFixtureList(), null,
					NonStateActorTest.getFixtureList());
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest.getFixtureList(),
					ProductionMetricTest.getFixtureList(), null);
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			SubjectCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest.getFixtureList(),
				ProductionMetricTest.getFixtureList(), NonStateActorTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SubjectCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, KeywordTest.getFixtureList(), CategoryTest.getFixtureList(),
				ProductionMetricTest.getFixtureList(), NonStateActorTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testCategoryReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Category> categories = CategoryTest.getFixtureList();
			getInstance(SUCCESS, null, categories, null, null);
			getInstance(SUCCESS, null, categories, null, null);
		}
	}

	public void testKeywordReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<Keyword> keywords = KeywordTest.getFixtureList();
			getInstance(SUCCESS, keywords, null, null, null);
			getInstance(SUCCESS, keywords, null, null, null);
		}
	}

	public void testMetricReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<ProductionMetric> metrics = ProductionMetricTest.getFixtureList();
			getInstance(SUCCESS, KeywordTest.getFixtureList(), null, metrics, null);
			getInstance(SUCCESS, KeywordTest.getFixtureList(), null, metrics, null);
		}
	}

	public void testActorReuse() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			List<NonStateActor> actors = NonStateActorTest.getFixtureList();
			getInstance(SUCCESS, KeywordTest.getFixtureList(), null, null, actors);
			getInstance(SUCCESS, KeywordTest.getFixtureList(), null, null, actors);
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
			SubjectCoverage component = new SubjectCoverage(KeywordTest.getFixtureList(),
				CategoryTest.getFixtureList(), null, null, attr);
			if (!version.isAtLeast("3.0"))
				assertTrue(component.getSecurityAttributes().isEmpty());
			else
				assertEquals(attr, component.getSecurityAttributes());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new SubjectCoverage(KeywordTest.getFixtureList(), CategoryTest.getFixtureList(), null, null,
				SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testWrongVersions() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		List<Keyword> keywords = KeywordTest.getFixtureList();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(keywords, null, null, null, SecurityAttributesTest.getFixture());
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "At least 1 keyword or category must exist.");
		}

		DDMSVersion.setCurrentVersion("2.0");
		List<Category> categories = CategoryTest.getFixtureList();
		DDMSVersion.setCurrentVersion("3.0");
		try {
			new SubjectCoverage(null, categories, null, null, SecurityAttributesTest.getFixture());
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "At least 1 keyword or category must exist.");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SubjectCoverage component = getInstance(SUCCESS, getValidElement(sVersion));
			SubjectCoverage.Builder builder = new SubjectCoverage.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			SubjectCoverage.Builder builder = new SubjectCoverage.Builder();
			builder.getCategories().get(0).setCode("TEST");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "label attribute is required.");
			}
			builder.getCategories().get(0).setQualifier("qualifier");
			builder.getCategories().get(0).setLabel("label");
			builder.commit();

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

			if (version.isAtLeast("4.0.1")) {
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
