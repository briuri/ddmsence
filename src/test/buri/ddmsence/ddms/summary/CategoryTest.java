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

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
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
	 * Returns a fixture object for testing.
	 */
	public static List<Category> getFixtureList() {
		try {
			List<Category> categories = new ArrayList<Category>();
			categories.add(new Category("urn:buri:ddmsence:categories", "DDMS", "DDMS", null));
			return (categories);
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
	private Category getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Category component = null;
		try {
			component = new Category(element);
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
	 * @param qualifier the qualifier (optional)
	 * @param code the code (optional)
	 * @param label the label (required)
	 * @return a valid object
	 */
	private Category getInstance(String message, String qualifier, String code, String label) {
		boolean expectFailure = !Util.isEmpty(message);
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Category component = null;
		try {
			component = new Category(qualifier, code, label, version.isAtLeast("4.0") ? SecurityAttributesTest
				.getFixture() : null);
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "category.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "category.code", TEST_CODE));
		text.append(buildOutput(isHTML, "category.label", TEST_LABEL));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, "category.classification", "U"));
			text.append(buildOutput(isHTML, "category.ownerProducer", "USA"));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:category ").append(getXmlnsDDMS()).append(" ");
		if (version.isAtLeast("4.0")) {
			xml.append(getXmlnsISM()).append(" ");
		}
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ");
		xml.append("ddms:code=\"").append(TEST_CODE).append("\" ");
		xml.append("ddms:label=\"").append(TEST_LABEL).append("\"");
		if (version.isAtLeast("4.0")) {
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		}
		xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Category.getName(version));
			getInstance("Unexpected namespace URI and local name encountered: ddms:wrongName", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Category.getName(version), null);
			Util.addDDMSAttribute(element, "label", TEST_LABEL);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);

			// No optional fields
			getInstance(SUCCESS, "", "", TEST_LABEL);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing label
			Element element = Util.buildDDMSElement(Category.getName(version), null);
			getInstance("moo", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing label
			getInstance("moo", TEST_QUALIFIER, TEST_CODE, null);

			// Qualifier not URI
			getInstance("moo", INVALID_URI, TEST_CODE, TEST_LABEL);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Category component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Category dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Category dataComponent = getInstance(SUCCESS, "", TEST_CODE, TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, "", TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_CODE, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testExtensibleSuccess() throws InvalidDDMSException {
		// Extensible attribute added
		DDMSVersion.setCurrentVersion("3.0");
		ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
		new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attr);
	}

	public void testExtensibleFailure() throws InvalidDDMSException {
		// Wrong DDMS Version
		DDMSVersion.setCurrentVersion("2.0");
		ExtensibleAttributes attributes = ExtensibleAttributesTest.getFixture();
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}

		DDMSVersion version = DDMSVersion.setCurrentVersion("3.0");

		// Using ddms:qualifier as the extension (data)
		List<Attribute> extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:qualifier", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}

		// Using ddms:code as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:code", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}

		// Using ddms:label as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:label", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}

		// Using icism:classification as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("icism:classification", version.getIsmNamespace(), "U"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
		}
		catch (InvalidDDMSException e) {
			fail("Prevented valid data.");
		}
	}

	public void testSecurityAttributesWrongVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "moo");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Category component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Category.Builder builder = new Category.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Category.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Category.Builder();
			builder.setQualifier(TEST_QUALIFIER);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "moo");
			}
		}
	}
}
