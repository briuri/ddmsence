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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:keyword elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class KeywordTest extends AbstractBaseTestCase {

	private static final String TEST_VALUE = "Tornado";

	/**
	 * Constructor
	 */
	public KeywordTest() {
		super("keyword.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<Keyword> getFixtureList() {
		try {
			List<Keyword> keywords = new ArrayList<Keyword>();
			keywords.add(new Keyword("DDMSence", null));
			keywords.add(new Keyword("Uri", null));
			return (keywords);
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
	private Keyword getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Keyword component = null;
		try {
			component = new Keyword(element);
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
	 * @param value the value child text
	 * @return a valid object
	 */
	private Keyword getInstance(String message, String value) {
		boolean expectFailure = !Util.isEmpty(message);
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Keyword component = null;
		try {
			component = new Keyword(value, version.isAtLeast("4.0") ? SecurityAttributesTest.getFixture() : null);
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
		text.append(buildOutput(isHTML, "keyword", TEST_VALUE));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, "keyword.classification", "U"));
			text.append(buildOutput(isHTML, "keyword.ownerProducer", "USA"));
		}
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:keyword ").append(getXmlnsDDMS()).append(" ");
		if (version.isAtLeast("4.0"))
			xml.append(getXmlnsISM()).append(" ");
		xml.append("ddms:value=\"").append(TEST_VALUE).append("\"");
		if (version.isAtLeast("4.0"))
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Keyword
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
			getInstance(SUCCESS, TEST_VALUE);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing value
			Element element = Util.buildDDMSElement(Keyword.getName(version), null);
			getInstance("value attribute is required.", element);

			// Empty value
			element = Util.buildDDMSElement(Keyword.getName(version), "");
			getInstance("value attribute is required.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing value
			getInstance("value attribute is required.", (String) null);

			// Empty value
			getInstance("value attribute is required.", "");
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Keyword component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Keyword dataComponent = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Keyword dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testExtensibleSuccess() throws InvalidDDMSException {
		// Extensible attribute added
		DDMSVersion.setCurrentVersion("3.0");
		ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
		new Keyword("xml", null, attr);
	}

	public void testExtensibleFailure() throws InvalidDDMSException {
		// Wrong DDMS Version
		DDMSVersion.setCurrentVersion("2.0");
		ExtensibleAttributes attributes = ExtensibleAttributesTest.getFixture();
		try {
			new Keyword(TEST_VALUE, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "xs:anyAttribute cannot be applied");
		}

		DDMSVersion version = DDMSVersion.setCurrentVersion("3.0");
		Attribute attr = new Attribute("ddms:value", version.getNamespace(), "dog");

		// Using ddms:value as the extension (data)
		List<Attribute> extAttributes = new ArrayList<Attribute>();
		extAttributes.add(attr);
		attributes = new ExtensibleAttributes(extAttributes);
		try {
			new Keyword(TEST_VALUE, null, attributes);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The extensible attribute with the name, ddms:value");
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Keyword(TEST_VALUE, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Security attributes cannot be applied");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Keyword component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Keyword.Builder builder = new Keyword.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Keyword.Builder();
			assertNull(builder.commit());
		}
	}
}
