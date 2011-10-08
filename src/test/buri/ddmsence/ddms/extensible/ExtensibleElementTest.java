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
package buri.ddmsence.ddms.extensible;

import java.io.IOException;

import nu.xom.Element;

import org.xml.sax.SAXException;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to extensible layer elements </p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public class ExtensibleElementTest extends AbstractBaseTestCase {

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
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		return (Util.buildElement(TEST_PREFIX, TEST_NAME, TEST_NAMESPACE, "This is an extensible element."));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ExtensibleElement getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ExtensibleElement component = null;
		try {
			component = new ExtensibleElement(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), TEST_PREFIX, TEST_NAME);
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getFixtureElement());
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Using the DDMS namespace
			Element element = Util.buildDDMSElement("name", null);
			getInstance("Extensible elements cannot be defined in the DDMS namespace.", element);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			ExtensibleElement component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleElement elementComponent = getInstance(SUCCESS, getFixtureElement());

			Element element = Util.buildElement(TEST_PREFIX, TEST_NAME, TEST_NAMESPACE,
				"This is an extensible element.");
			ExtensibleElement dataComponent = getInstance(SUCCESS, element);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleElement elementComponent = getInstance(SUCCESS, getFixtureElement());
			Element element = Util.buildElement(TEST_PREFIX, "newName", TEST_NAMESPACE,
				"This is an extensible element.");
			ExtensibleElement dataComponent = getInstance(SUCCESS, element);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleElement component = getInstance(SUCCESS, getFixtureElement());
			assertEquals("", component.toHTML());
			assertEquals("", component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleElement component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws SAXException, IOException, InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ExtensibleElement component = getInstance(SUCCESS, getFixtureElement());

			// Equality after Building
			ExtensibleElement.Builder builder = new ExtensibleElement.Builder(component);
			assertEquals(component, builder.commit());
			builder = new ExtensibleElement.Builder();
			builder.setXml(getFixtureElement().toXML());
			assertEquals(component, builder.commit());

			// Empty case
			builder = new ExtensibleElement.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new ExtensibleElement.Builder();
			builder.setXml("InvalidXml");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "Could not create a valid element");
			}
			builder.setXml(getExpectedXMLOutput());
			builder.commit();
		}
	}
}
