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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:nonStateActor elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NonStateActorTest extends AbstractBaseTestCase {

	private static final String TEST_VALUE = "Laotian Monks";
	private static final Integer TEST_ORDER = Integer.valueOf(1);
	private static final String TEST_QUALIFIER = "urn:sample";

	/**
	 * Constructor
	 */
	public NonStateActorTest() {
		super("nonStateActor.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @param a fixed order value
	 */
	public static NonStateActor getFixture(int order) {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			return (version.isAtLeast("4.0.1") ? new NonStateActor(TEST_VALUE, Integer.valueOf(order), getQualifier(),
				SecurityAttributesTest.getFixture()) : null);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a dummy value for the qualifier, based upon the current DDMS version.
	 */
	private static String getQualifier() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.1") ? TEST_QUALIFIER : null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<NonStateActor> getFixtureList() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			List<NonStateActor> actors = new ArrayList<NonStateActor>();
			if (version.isAtLeast("4.0.1"))
				actors.add(new NonStateActor(TEST_VALUE, TEST_ORDER, getQualifier(),
					SecurityAttributesTest.getFixture()));
			return (actors);
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
	private NonStateActor getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		NonStateActor component = null;
		try {
			component = new NonStateActor(element);
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
	 * @param value the value of the actor (optional)
	 * @param order the order of the actor (optional)
	 * @param qualifier the qualifier of the actor (optional)
	 * @return a valid object
	 */
	private NonStateActor getInstance(String message, String value, Integer order, String qualifier) {
		boolean expectFailure = !Util.isEmpty(message);
		NonStateActor component = null;
		try {
			component = new NonStateActor(value, order, qualifier, SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "nonStateActor.value", TEST_VALUE));
		text.append(buildOutput(isHTML, "nonStateActor.order", String.valueOf(TEST_ORDER)));
		if (version.isAtLeast("4.1"))
			text.append(buildOutput(isHTML, "nonStateActor.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "nonStateActor.classification", "U"));
		text.append(buildOutput(isHTML, "nonStateActor.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:nonStateActor ").append(getXmlnsDDMS()).append(" ");
		xml.append(getXmlnsISM()).append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ");
		xml.append("ddms:order=\"").append(TEST_ORDER).append("\"");
		if (version.isAtLeast("4.1"))
			xml.append(" ddms:qualifier=\"").append(TEST_QUALIFIER).append("\"");
		xml.append(">").append(TEST_VALUE).append("</ddms:nonStateActor>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				NonStateActor.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(NonStateActor.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_VALUE, TEST_ORDER, getQualifier());

			// No optional fields
			getInstance(SUCCESS, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Bad qualifier
			if (version.isAtLeast("4.1")) {
				Element element = Util.buildDDMSElement(NonStateActor.getName(version), null);
				Util.addDDMSAttribute(element, "qualifier", INVALID_URI);
				getInstance("Invalid URI", element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Bad qualifier
			if (version.isAtLeast("4.1")) {
				getInstance("Invalid URI", TEST_VALUE, TEST_ORDER, INVALID_URI);
			}
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor component = getInstance(SUCCESS, getValidElement(sVersion));

			// 4.1 ddms:qualifier element used
			if ("4.1".equals(sVersion)) {
				assertEquals(1, component.getValidationWarnings().size());
				String text = "The ddms:qualifier attribute in this DDMS component";
				String locator = "ddms:nonStateActor";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
			// No warnings
			else {
				assertEquals(0, component.getValidationWarnings().size());
			}

			// Empty value
			if (!version.isAtLeast("5.0")) {
				Element element = Util.buildDDMSElement(NonStateActor.getName(version), null);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "A ddms:nonStateActor element was found with no value.";
				String locator = "ddms:nonStateActor";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testDeprecatedConstructor() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor dataComponent = new NonStateActor(TEST_VALUE, TEST_ORDER, SecurityAttributesTest.getFixture());
			assertTrue(Util.isEmpty(dataComponent.getQualifier()));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			NonStateActor dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_ORDER, getQualifier());

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			NonStateActor dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_ORDER, getQualifier());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, null, getQualifier());
			assertFalse(elementComponent.equals(dataComponent));

			if (version.isAtLeast("4.1")) {
				dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_ORDER, DIFFERENT_VALUE);
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_ORDER, getQualifier());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_ORDER, getQualifier());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new NonStateActor(TEST_VALUE, TEST_ORDER, null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The nonStateActor element cannot be used");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor component = getInstance(SUCCESS, getValidElement(sVersion));
			NonStateActor.Builder builder = new NonStateActor.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NonStateActor.Builder builder = new NonStateActor.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setOrder(TEST_ORDER);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// There are no invalid cases right now -- every field is optional.
		}
	}
}
