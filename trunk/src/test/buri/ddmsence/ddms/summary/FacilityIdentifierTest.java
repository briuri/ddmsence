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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:facilityIdentifier elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class FacilityIdentifierTest extends AbstractComponentTestCase {
	private static final String TEST_BENUMBER = "1234DD56789";
	private static final String TEST_OSUFFIX = "DD123";

	/**
	 * Constructor
	 */
	public FacilityIdentifierTest() {
		super("facilityIdentifier.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static FacilityIdentifier getFixture() {
		try {
			return (new FacilityIdentifier("1234DD56789", "DD123"));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private FacilityIdentifier testConstructor(boolean expectFailure, Element element) {
		FacilityIdentifier component = null;
		try {
			component = new FacilityIdentifier(element);
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
	 * @param beNumber the beNumber (required)
	 * @param osuffix the Osuffix (required, because beNumber is required)
	 * @return a valid object
	 */
	private FacilityIdentifier testConstructor(boolean expectFailure, String beNumber, String osuffix) {
		FacilityIdentifier component = null;
		try {
			component = new FacilityIdentifier(beNumber, osuffix);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "facilityIdentifier.beNumber", TEST_BENUMBER));
		text.append(buildOutput(isHTML, "facilityIdentifier.osuffix", TEST_OSUFFIX));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:facilityIdentifier ").append(getXmlnsDDMS()).append(" ");
		xml.append("ddms:beNumber=\"").append(TEST_BENUMBER).append("\" ");
		xml.append("ddms:osuffix=\"").append(TEST_OSUFFIX).append("\" />");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				FacilityIdentifier.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing beNumber
			Element element = Util.buildDDMSElement(FacilityIdentifier.getName(version), null);
			Util.addDDMSAttribute(element, "osuffix", TEST_OSUFFIX);
			testConstructor(WILL_FAIL, element);

			// Empty beNumber
			element = Util.buildDDMSElement(FacilityIdentifier.getName(version), null);
			Util.addDDMSAttribute(element, "beNumber", "");
			Util.addDDMSAttribute(element, "osuffix", TEST_OSUFFIX);
			testConstructor(WILL_FAIL, element);

			// Missing osuffix
			element = Util.buildDDMSElement(FacilityIdentifier.getName(version), null);
			Util.addDDMSAttribute(element, "beNumber", TEST_BENUMBER);
			testConstructor(WILL_FAIL, element);

			// Empty osuffix
			element = Util.buildDDMSElement(FacilityIdentifier.getName(version), null);
			Util.addDDMSAttribute(element, "beNumber", TEST_BENUMBER);
			Util.addDDMSAttribute(element, "osuffix", "");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing beNumber
			testConstructor(WILL_FAIL, null, TEST_OSUFFIX);

			// Empty beNumber
			testConstructor(WILL_FAIL, "", TEST_OSUFFIX);

			// Missing osuffix
			testConstructor(WILL_FAIL, TEST_BENUMBER, null);

			// Empty osuffix
			testConstructor(WILL_FAIL, TEST_BENUMBER, "");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			FacilityIdentifier dataComponent = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			FacilityIdentifier dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_OSUFFIX);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_BENUMBER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_BENUMBER, TEST_OSUFFIX);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			FacilityIdentifier component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			FacilityIdentifier.Builder builder = new FacilityIdentifier.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new FacilityIdentifier.Builder();
			assertNull(builder.commit());
			// Validation
			builder = new FacilityIdentifier.Builder();
			builder.setBeNumber(TEST_BENUMBER);
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
