/* Copyright 2010 by Brian Uri!
   
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
package buri.ddmsence.ddms.extensible;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to extensible layer elements</p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public class ExtensibleElementTest extends AbstractComponentTestCase {

	private static final String TEST_NAME = "extension";
	private static final String TEST_PREFIX = "ddmsence";
	private static final String TEST_NAMESPACE = "http://ddmsence.urizone.net/";

	/**
	 * Constructor
	 */
	public ExtensibleElementTest() {
		super(null);
	}

	/**
	 * Returns a data fixture that can be used to create an ExtensibleElement.
	 */
	public static Element getElementFixture() {
		return (Util.buildElement(TEST_PREFIX, TEST_NAME, TEST_NAMESPACE, "This is an extensible element."));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ExtensibleElement testConstructor(boolean expectFailure, Element element) {
		ExtensibleElement component = null;
		try {
			component = new ExtensibleElement(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddmsence:extension xmlns:ddmsence=\"http://ddmsence.urizone.net/\">").append(
			"This is an extensible element.</ddmsence:extension>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
			assertEquals(TEST_NAME, component.getName());
			assertEquals(TEST_PREFIX, component.getPrefix());
			assertEquals(TEST_PREFIX + ":" + TEST_NAME, component.getQualifiedName());
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getElementFixture());
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Using the DDMS namespace
			Element element = Util.buildDDMSElement("name", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement elementComponent = testConstructor(WILL_SUCCEED, getElementFixture());

			Element element = Util.buildElement(TEST_PREFIX, TEST_NAME, TEST_NAMESPACE,
				"This is an extensible element.");
			ExtensibleElement dataComponent = testConstructor(WILL_SUCCEED, element);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement elementComponent = testConstructor(WILL_SUCCEED, getElementFixture());
			Element element = Util.buildElement(TEST_PREFIX, "newName", TEST_NAMESPACE,
				"This is an extensible element.");
			ExtensibleElement dataComponent = testConstructor(WILL_SUCCEED, element);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement elementComponent = testConstructor(WILL_SUCCEED, getElementFixture());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
			assertEquals("", component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
			assertEquals("", component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}
	
	public void testVersion() {
		ExtensibleElement component = testConstructor(WILL_SUCCEED, getElementFixture());
		assertEquals("?", component.getDDMSVersion());
	}
}
