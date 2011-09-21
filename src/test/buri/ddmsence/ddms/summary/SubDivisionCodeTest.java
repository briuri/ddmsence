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
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:subDivisionCode elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class SubDivisionCodeTest extends AbstractComponentTestCase {

	private static final String TEST_QUALIFIER = "ISO-3166";
	private static final String TEST_VALUE = "USA";

	/**
	 * Constructor
	 */
	public SubDivisionCodeTest() {
		super("subDivisionCode.xml");
	}

	/**
	 * Returns a subDivisionCode fixture
	 */
	protected static SubDivisionCode getFixture() throws InvalidDDMSException {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? new SubDivisionCode("ISO-3166", "USA") : null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private SubDivisionCode testConstructor(boolean expectFailure, Element element) {
		SubDivisionCode component = null;
		try {
			component = new SubDivisionCode(element);
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
	 * @param qualifier the qualifier value
	 * @param value the value
	 * @return a valid object
	 */
	private SubDivisionCode testConstructor(boolean expectFailure, String qualifier, String value) {
		SubDivisionCode component = null;
		try {
			component = new SubDivisionCode(qualifier, value);
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
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "subDivisionCode.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "subDivisionCode.value", TEST_VALUE));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:subDivisionCode ").append(getXmlnsDDMS()).append(" ddms:qualifier=\"").append(TEST_QUALIFIER)
			.append("\" ddms:value=\"").append(TEST_VALUE).append("\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				SubDivisionCode.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			testConstructor(WILL_SUCCEED, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String subCode = SubDivisionCode.getName(version);
			if (!version.isAtLeast("4.0"))
				continue;

			// Missing qualifier
			Element element = Util.buildDDMSElement(subCode, null);
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);

			// Empty qualifier
			element = Util.buildDDMSElement(subCode, null);
			Util.addDDMSAttribute(element, "qualifier", "");
			Util.addDDMSAttribute(element, "value", TEST_VALUE);
			testConstructor(WILL_FAIL, element);

			// Missing value
			element = Util.buildDDMSElement(subCode, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			testConstructor(WILL_FAIL, element);

			// Empty value
			element = Util.buildDDMSElement(subCode, null);
			Util.addDDMSAttribute(element, "qualifier", TEST_QUALIFIER);
			Util.addDDMSAttribute(element, "value", "");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing qualifier
			testConstructor(WILL_FAIL, null, TEST_VALUE);

			// Empty qualifier
			testConstructor(WILL_FAIL, "", TEST_VALUE);

			// Missing value
			testConstructor(WILL_FAIL, TEST_QUALIFIER, null);

			// Empty value
			testConstructor(WILL_FAIL, TEST_QUALIFIER, "");
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			SubDivisionCode component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SubDivisionCode dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SubDivisionCode dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VALUE);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_QUALIFIER, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new SubDivisionCode(TEST_QUALIFIER, TEST_VALUE);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			SubDivisionCode component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			SubDivisionCode.Builder builder = new SubDivisionCode.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new SubDivisionCode.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new SubDivisionCode.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
