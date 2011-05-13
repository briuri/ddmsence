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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:countryCode elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class CountryCodeTest extends AbstractComponentTestCase {

	private static final String TEST_PARENT_TYPE = PostalAddress.NAME;
	private static final String TEST_QUALIFIER = "ISO-3166";
	private static final String TEST_VALUE = "USA";

	/**
	 * Constructor
	 */
	public CountryCodeTest() {
		super("countryCode.xml");
	}

	/**
	 * Returns a country code fixture
	 * 
	 * @param parentName either geographicIdentifier or postalAddress
	 */
	protected static CountryCode getFixture(String parentName) throws InvalidDDMSException {
		return (new CountryCode(parentName, "ISO-3166", "USA"));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param parentType the enclosing parent type
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private CountryCode testConstructor(boolean expectFailure, String parentType, Element element) {
		CountryCode component = null;
		try {
			component = new CountryCode(parentType, element);
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
	 * @param parentType the enclosing parent type
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private CountryCode testConstructor(boolean expectFailure, String parentType, String qualifier, String value) {
		CountryCode component = null;
		try {
			component = new CountryCode(parentType, qualifier, value);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 * 
	 * @param parentType the enclosing parent type
	 */
	private String getExpectedHTMLOutput(String parentType) {
		String parentHtml = (parentType.equals(GeographicIdentifier.NAME) ? "geospatial.identifier"
			: "geospatial.address");
		StringBuffer html = new StringBuffer();
		html.append("<meta name=\"").append(parentHtml).append(".country.qualifier\" content=\"")
			.append(TEST_QUALIFIER).append("\" />\n");
		html.append("<meta name=\"").append(parentHtml).append(".country\" content=\"").append(TEST_VALUE)
			.append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 * 
	 * @param parentType the enclosing parent type
	 */
	private String getExpectedTextOutput(String parentType) {
		String parentText = (parentType.equals(GeographicIdentifier.NAME) ? "Geographic Identifier" : "Postal Address");
		StringBuffer text = new StringBuffer();
		text.append(parentText).append(" Country Qualifier: ").append(TEST_QUALIFIER).append("\n");
		text.append(parentText).append(" Country: ").append(TEST_VALUE).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:countryCode xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\" ");
		xml.append("ddms:qualifier=\"").append(TEST_QUALIFIER).append("\" ddms:value=\"").append(TEST_VALUE)
			.append("\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			assertEquals(CountryCode.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + CountryCode.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);

			// Missing qualifier
			Element element = Util.buildDDMSElement(CountryCode.NAME, null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, element);

			// Empty qualifier
			element = Util.buildDDMSElement(CountryCode.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", "");
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, element);

			// Missing value
			element = Util.buildDDMSElement(CountryCode.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, element);

			// Empty value
			element = Util.buildDDMSElement(CountryCode.NAME, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(element, "value", "");
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing qualifier
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, null, TEST_VALUE);

			// Empty qualifier
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, "", TEST_VALUE);

			// Missing value
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, TEST_QUALIFIER, null);

			// Empty value
			testConstructor(WILL_FAIL, TEST_PARENT_TYPE, TEST_QUALIFIER, "");
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			CountryCode component = testConstructor(WILL_SUCCEED, "postalAddress", getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode elementComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			CountryCode dataComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode elementComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			CountryCode dataComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode elementComponent = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(component.getParentType()), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedHTMLOutput(component.getParentType()), component.toHTML());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifier.NAME, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(component.getParentType()), component.toHTML());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifier.NAME, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedHTMLOutput(component.getParentType()), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			assertEquals(getExpectedTextOutput(component.getParentType()), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedTextOutput(component.getParentType()), component.toText());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifier.NAME, getValidElement(version));
			assertEquals(getExpectedTextOutput(component.getParentType()), component.toText());

			component = testConstructor(WILL_SUCCEED, GeographicIdentifier.NAME, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedTextOutput(component.getParentType()), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testValidateProducerTypeSuccess() {
		try {
			CountryCode.validateParentType(PostalAddress.NAME);
		} catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}

	public void testValidateProducerTypeFailure() {
		try {
			CountryCode.validateParentType("notCorrect");
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			CountryCode component = testConstructor(WILL_SUCCEED, TEST_PARENT_TYPE, getValidElement(version));
			
			// Equality after Building
			CountryCode.Builder builder = new CountryCode.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new CountryCode.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new CountryCode.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
