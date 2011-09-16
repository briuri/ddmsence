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
import buri.ddmsence.util.PropertyReader;
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
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Category testConstructor(boolean expectFailure, Element element) {
		Category component = null;
		try {
			component = new Category(element);
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
	 * @param qualifier the qualifier (optional)
	 * @param code the code (optional)
	 * @param label the label (required)
	 * @return a valid object
	 */
	private Category testConstructor(boolean expectFailure, String qualifier, String code, String label) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Category component = null;
		try {
			component = new Category(qualifier, code, label,
				version.isAtLeast("4.0") ? SecurityAttributesTest.getFixture(false) : null);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
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
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Category.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Category.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(Category.getName(version), null);
			Util.addDDMSAttribute(element, "label", TEST_LABEL);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);

			// No optional fields
			testConstructor(WILL_SUCCEED, "", "", TEST_LABEL);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Missing label
			Element element = Util.buildDDMSElement(Category.getName(version), null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing label
			testConstructor(WILL_FAIL, TEST_QUALIFIER, TEST_CODE, null);

			// Qualifier not URI
			testConstructor(WILL_FAIL, INVALID_URI, TEST_CODE, TEST_LABEL);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Category dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Category dataComponent = testConstructor(WILL_SUCCEED, "", TEST_CODE, TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, "", TEST_LABEL);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_CODE, TEST_LABEL);
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
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion version = DDMSVersion.setCurrentVersion("3.0");

		// Using ddms:qualifier as the extension (data)
		List<Attribute> extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:qualifier", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		// Using ddms:code as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:code", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		// Using ddms:label as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("ddms:label", version.getNamespace(), "dog"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		// Using icism:classification as the extension (data)
		extAttributes = new ArrayList<Attribute>();
		extAttributes.add(new Attribute("icism:classification", version.getIsmNamespace(), "U"));
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, null, attributes);
		} catch (InvalidDDMSException e) {
			fail("Prevented valid data.");
		}
	}

	public void testSecurityAttributesWrongVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Category(TEST_QUALIFIER, TEST_CODE, TEST_LABEL, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Category component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

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
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
