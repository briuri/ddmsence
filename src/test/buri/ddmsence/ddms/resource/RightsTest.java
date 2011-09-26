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
package buri.ddmsence.ddms.resource;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>
 * Tests related to ddms:rights elements
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class RightsTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public RightsTest() {
		super("rights.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Rights getFixture() {
		try {
			return (new Rights(true, true, true));
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
	private Rights getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Rights component = null;
		try {
			component = new Rights(element);
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
	 * @param privacyAct the value for the privacyAct attribute
	 * @param intellectualProperty the value for the intellectualProperty attribute
	 * @param copyright the value for the copyright attribute
	 * @return a valid object
	 */
	private Rights getInstance(String message, boolean privacyAct, boolean intellectualProperty, boolean copyright) {
		boolean expectFailure = !Util.isEmpty(message);
		Rights component = null;
		try {
			component = new Rights(privacyAct, intellectualProperty, copyright);
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
		text.append(buildOutput(isHTML, "rights.privacyAct", "true"));
		text.append(buildOutput(isHTML, "rights.intellectualProperty", "true"));
		text.append(buildOutput(isHTML, "rights.copyright", "false"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:rights ").append(getXmlnsDDMS()).append(
			" ddms:privacyAct=\"true\" ddms:intellectualProperty=\"true\" ddms:copyright=\"false\" />");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Rights component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(Rights.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + Rights.getName(version), component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			getInstance(WRONG_NAME_MESSAGE, element);
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Rights.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, true, true, true);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Rights component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights dataComponent = getInstance(SUCCESS, true, true, false);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights dataComponent = getInstance(SUCCESS, false, true, false);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, true, false, false);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, true, true, true);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Language wrongComponent = new Language("qualifier", "value");
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, true, true, false);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, true, true, false);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testDefaultValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Element element = Util.buildDDMSElement(Rights.getName(version), null);
			Rights component = getInstance(SUCCESS, element);
			assertFalse(component.getPrivacyAct());
			assertFalse(component.getIntellectualProperty());
			assertFalse(component.getCopyright());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Rights component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Rights.Builder builder = new Rights.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Rights.Builder();
			assertNull(builder.commit());

			// Default values (at least 1 value must be explicit to prevent a null commit)
			builder = new Rights.Builder();
			builder.setPrivacyAct(true);
			assertFalse(builder.commit().getIntellectualProperty());
			assertFalse(builder.commit().getCopyright());
			builder = new Rights.Builder();
			builder.setIntellectualProperty(true);
			assertFalse(builder.commit().getPrivacyAct());
		}
	}
}
