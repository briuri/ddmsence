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

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:countryCode elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class CountryCodeTest extends AbstractBaseTestCase {

	private static final String TEST_QUALIFIER = "ISO-3166";
	private static final String TEST_VALUE = "USA";

	/**
	 * Constructor
	 */
	public CountryCodeTest() {
		super("countryCode.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static CountryCode getFixture() {
		try {
			if (DDMSVersion.getCurrentVersion().isAtLeast("5.0"))
				return (new CountryCode("http://api.nsgreg.nga.mil/geo-political/GENC/2/ed1", "US"));
			return (new CountryCode("ISO-3166", "USA"));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}
	
	/**
	 * Returns the name of the qualifier attribute, based on DDMS version
	 */
	public static String getQualifierName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("5.0") ? "codespace" : "qualifier");
	}

	/**
	 * Returns the name of the value attribute, based on DDMS version
	 */
	public static String getValueName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("5.0") ? "code" : "value");
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private CountryCode getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		CountryCode component = null;
		try {
			component = new CountryCode(element);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private CountryCode getInstance(String message, String qualifier, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		CountryCode component = null;
		try {
			component = new CountryCode(qualifier, value);
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
		text.append(buildOutput(isHTML, "countryCode." + getQualifierName(), TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "countryCode." + getValueName(), TEST_VALUE));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:countryCode ").append(getXmlnsDDMS()).append(" ddms:").append(getQualifierName()).append("=\"").append(TEST_QUALIFIER)
			.append("\" ddms:").append(getValueName()).append("=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, CountryCode
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			getInstance(SUCCESS, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String countryCodeName = CountryCode.getName(version);
			
			String qualifierName = getQualifierName();
			String valueName = getValueName();

			// Missing qualifier
			Element element = Util.buildDDMSElement(countryCodeName, null);
			Util.addDDMSAttribute(element, valueName, TEST_VALUE);
			getInstance(qualifierName + " attribute is required.", element);

			// Empty qualifier
			element = Util.buildDDMSElement(countryCodeName, null);
			Util.addDDMSAttribute(element, qualifierName, "");
			Util.addDDMSAttribute(element, valueName, TEST_VALUE);
			getInstance(qualifierName + " attribute is required.", element);

			// Missing value
			element = Util.buildDDMSElement(countryCodeName, null);
			Util.addDDMSAttribute(element, qualifierName, TEST_QUALIFIER);
			getInstance(valueName + " attribute is required.", element);

			// Empty value
			element = Util.buildDDMSElement(countryCodeName, null);
			Util.addDDMSAttribute(element, qualifierName, TEST_QUALIFIER);
			Util.addDDMSAttribute(element, valueName, "");
			getInstance(valueName + " attribute is required.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			String qualifierName = getQualifierName();
			String valueName = getValueName();
			
			// Missing qualifier
			getInstance(qualifierName + " attribute is required.", null, TEST_VALUE);

			// Empty qualifier
			getInstance(qualifierName + " attribute is required.", "", TEST_VALUE);

			// Missing value
			getInstance(valueName + " attribute is required.", TEST_QUALIFIER, null);

			// Empty value
			getInstance(valueName + " attribute is required.", TEST_QUALIFIER, "");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			CountryCode component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			CountryCode dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			CountryCode dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			CountryCode component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			CountryCode component = getInstance(SUCCESS, getValidElement(sVersion));
			CountryCode.Builder builder = new CountryCode.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			CountryCode.Builder builder = new CountryCode.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			CountryCode.Builder builder = new CountryCode.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, getQualifierName() + " attribute is required.");
			}
			builder.setQualifier(TEST_QUALIFIER);
			builder.commit();
		}
	}
}
