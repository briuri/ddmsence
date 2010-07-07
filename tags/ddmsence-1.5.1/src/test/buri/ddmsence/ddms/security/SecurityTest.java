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
package buri.ddmsence.ddms.security;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:security elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SecurityTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public SecurityTest() {
		super("security.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure, Element element) {
		Security component = null;
		try {
			component = new Security(element);
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
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure) {
		Security component = null;
		try {
			component = new Security(SecurityAttributesTest.getFixture(false));
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		if (!DDMSVersion.isCurrentVersion("2.0"))
			html.append("<meta name=\"security.excludeFromRollup\" content=\"true\" />\n");
		html.append("<meta name=\"security.classification\" content=\"U\" />\n");
		html.append("<meta name=\"security.ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		if (!DDMSVersion.isCurrentVersion("2.0"))
			text.append("Exclude From Rollup: true\n");
		text.append("Classification: U\n");
		text.append("ownerProducer: USA\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:security xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\" xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIcismNamespace()).append("\" ");
		if (!DDMSVersion.isCurrentVersion("2.0"))
			xml.append("ICISM:excludeFromRollup=\"true\" ");
		xml.append("ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Security.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Security.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing excludeFromRollup
			Element element = Util.buildDDMSElement(Security.NAME, null);
			testConstructor(WILL_FAIL, element);

			// Incorrect excludeFromRollup
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "false");
			testConstructor(WILL_FAIL, element);
			
			// Invalid excludeFromRollup
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "aardvark");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Bad security attributes
			try {
				new Security((SecurityAttributes) null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Security component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Security dataComponent = testConstructor(WILL_SUCCEED);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Can't test this yet.
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED);
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED);
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}
	
	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		String icPrefix = PropertyReader.getProperty("icism.prefix");
		String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
		
		Element element = Util.buildDDMSElement("security", null);
		Util.addAttribute(element, icPrefix, "classification", icNamespace, "U");
		Util.addAttribute(element, icPrefix, "ownerProducer", icNamespace, "USA");
		Util.addAttribute(element, icPrefix, "excludeFromRollup", icNamespace, "true");
		try {
			new Security(element);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

}
