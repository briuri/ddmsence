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
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:productionMetric elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProductionMetricTest extends AbstractComponentTestCase {

	private static final String TEST_SUBJECT = "FOOD";
	private static final String TEST_COVERAGE = "AFG";

	/**
	 * Constructor
	 */
	public ProductionMetricTest() {
		super("productionMetric.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<ProductionMetric> getFixtureList() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			List<ProductionMetric> metrics = new ArrayList<ProductionMetric>();
			if (version.isAtLeast("4.0"))
				metrics.add(new ProductionMetric("FOOD", "AFG", SecurityAttributesTest.getFixture()));
			return (metrics);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ProductionMetric getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ProductionMetric component = null;
		try {
			component = new ProductionMetric(element);
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
	 * @param subject a method of categorizing the subject of a document in a fashion understandable by DDNI-A
	 * (required)
	 * @param coverage a method of categorizing the coverage of a document in a fashion understandable by DDNI-A
	 * (required)
	 * @param label the label (required)
	 * @return a valid object
	 */
	private ProductionMetric getInstance(String message, String subject, String coverage) {
		boolean expectFailure = !Util.isEmpty(message);
		ProductionMetric component = null;
		try {
			component = new ProductionMetric(subject, coverage, SecurityAttributesTest.getFixture());
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "productionMetric.subject", TEST_SUBJECT));
		text.append(buildOutput(isHTML, "productionMetric.coverage", TEST_COVERAGE));
		text.append(buildOutput(isHTML, "productionMetric.classification", "U"));
		text.append(buildOutput(isHTML, "productionMetric.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:productionMetric ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ddms:subject=\"").append(TEST_SUBJECT).append("\" ").append("ddms:coverage=\"").append(
			TEST_COVERAGE).append("\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				ProductionMetric.getName(version));
			getInstance("Unexpected namespace URI and local name encountered: ddms:wrongName", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_SUBJECT, TEST_COVERAGE);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing subject
			Element element = Util.buildDDMSElement(ProductionMetric.getName(version), null);
			element.addAttribute(Util.buildDDMSAttribute("coverage", TEST_COVERAGE));
			getInstance("moo", element);

			// Missing coverage
			element = Util.buildDDMSElement(ProductionMetric.getName(version), null);
			element.addAttribute(Util.buildDDMSAttribute("subject", TEST_SUBJECT));
			getInstance("moo", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing subject
			getInstance("moo", null, TEST_COVERAGE);

			// Missing coverage
			getInstance("moo", TEST_SUBJECT, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ProductionMetric component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			ProductionMetric dataComponent = getInstance(SUCCESS, TEST_SUBJECT, TEST_COVERAGE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			ProductionMetric dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_COVERAGE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_SUBJECT, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_SUBJECT, TEST_COVERAGE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_SUBJECT, TEST_COVERAGE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new ProductionMetric(TEST_SUBJECT, TEST_COVERAGE, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProductionMetric component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			ProductionMetric.Builder builder = new ProductionMetric.Builder(component);
			assertEquals(builder.commit(), component);

			// Validation
			builder = new ProductionMetric.Builder();
			assertNull(builder.commit());
			builder.setCoverage(TEST_COVERAGE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "moo");
			}
			builder.setSubject(TEST_SUBJECT);
			builder.commit();
		}
	}
}
