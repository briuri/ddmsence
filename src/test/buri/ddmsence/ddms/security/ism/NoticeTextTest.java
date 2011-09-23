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
package buri.ddmsence.ddms.security.ism;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ISM:NoticeText elements</p>
 * 
 * <p> The valid instance of ISM:NoticeText is generated, rather than relying on the ISM schemas to validate an XML
 * file.</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeTextTest extends AbstractComponentTestCase {

	private static final String TEST_VALUE = "noticeText";
	private static final List<String> TEST_POC_TYPES = Util.getXsListAsList("DoD-Dist-B");

	/**
	 * Constructor
	 */
	public NoticeTextTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a canned fixed value recordKeeper for testing.
	 * 
	 * @return a XOM element representing a valid applicationSoftware
	 */
	protected static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String ismPrefix = PropertyReader.getPrefix("ism");
		String ismNs = version.getIsmNamespace();

		Element element = Util.buildElement(ismPrefix, NoticeText.getName(version), ismNs, TEST_VALUE);
		element.addNamespaceDeclaration(ismPrefix, version.getIsmNamespace());
		SecurityAttributesTest.getFixture(false).addTo(element);
		Util.addAttribute(element, ismPrefix, "pocType", ismNs, "DoD-Dist-B");
		return (element);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private NoticeText testConstructor(boolean expectFailure, Element element) {
		NoticeText component = null;
		try {
			component = new NoticeText(element);
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
	 * @param value the value (optional)
	 * @param pocTypes the poc types (optional)
	 * @return a valid object
	 */
	private NoticeText testConstructor(boolean expectFailure, String value, List<String> pocTypes) {
		NoticeText component = null;
		try {
			component = new NoticeText(value, pocTypes, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "noticeText", TEST_VALUE));
		text.append(buildOutput(isHTML, "noticeText.pocType", Util.getXsList(TEST_POC_TYPES)));
		text.append(buildOutput(isHTML, "noticeText.classification", "U"));
		text.append(buildOutput(isHTML, "noticeText.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ISM:NoticeText ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\"");
		xml.append(">").append(TEST_VALUE).append("</ISM:NoticeText>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getFixtureElement()), DEFAULT_ISM_PREFIX, NoticeText
				.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ismPrefix = PropertyReader.getPrefix("ism");

			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());

			// No optional fields
			Element element = Util
				.buildElement(ismPrefix, NoticeText.getName(version), version.getIsmNamespace(), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_POC_TYPES);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// There are no invalid constructors right now -- every field is optional.
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// There are no invalid constructors right now -- every field is optional.
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ismPrefix = PropertyReader.getPrefix("ism");

			// No warnings
			NoticeText component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());

			// Empty value
			Element element = Util
				.buildElement(ismPrefix, NoticeText.getName(version), version.getIsmNamespace(), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "An ISM:NoticeText element was found with no value.";
			String locator = "ISM:NoticeText";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeText elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			NoticeText dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_POC_TYPES);

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeText elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			NoticeText dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_POC_TYPES);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeText component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_POC_TYPES);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeText component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_POC_TYPES);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new NoticeText(TEST_VALUE, TEST_POC_TYPES, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeText component = testConstructor(WILL_SUCCEED, getFixtureElement());

			NoticeText.Builder builder = new NoticeText.Builder();
			assertNull(builder.commit());

			// Equality after Building
			builder = new NoticeText.Builder(component);
			assertEquals(builder.commit(), component);
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			NoticeText.Builder builder = new NoticeText.Builder();
			assertNotNull(builder.getPocTypes().get(1));
		}
	}
}
