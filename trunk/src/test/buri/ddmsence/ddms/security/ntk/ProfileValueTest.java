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
package buri.ddmsence.ddms.security.ntk;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ntk:AccessProfileValue elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProfileValueTest extends AbstractComponentTestCase {

	private static final String TEST_VALUE = "profile";
	private static final String TEST_VOCABULARY = "vocabulary";
	private static final String TEST_ID = "ID";
	private static final String TEST_ID_REFERENCE = "ID";
	private static final String TEST_QUALIFIER = "qualifier";

	/**
	 * Constructor
	 */
	public ProfileValueTest() {
		super("accessProfileValue.xml");
	}

	/**
	 * Creates a ProfileValue fixture
	 */
	public static ProfileValue getFixture(String value) {
		try {
			return (new ProfileValue(value, TEST_VOCABULARY, null, null, null, SecurityAttributesTest.getFixture(false)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
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
	private ProfileValue testConstructor(boolean expectFailure, Element element) {
		ProfileValue component = null;
		try {
			component = new ProfileValue(element);
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
	 * @param value the value of the element's child text
	 * @param vocabulary the vocabulary (required)
	 * @param id the NTK ID (optional)
	 * @param idReference a reference to an NTK ID (optional)
	 * @param qualifier an NTK qualifier (optional)
	 * @return a valid object
	 */
	private ProfileValue testConstructor(boolean expectFailure, String value, String vocabulary, String id,
		String idReference, String qualifier) {
		ProfileValue component = null;
		try {
			component = new ProfileValue(value, vocabulary, id, idReference, qualifier,
				SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "profileValue", TEST_VALUE));
		text.append(buildOutput(isHTML, "profileValue.vocabulary", TEST_VOCABULARY));
		text.append(buildOutput(isHTML, "profileValue.id", TEST_ID));
		text.append(buildOutput(isHTML, "profileValue.idReference", TEST_ID_REFERENCE));
		text.append(buildOutput(isHTML, "profileValue.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "profileValue.classification", "U"));
		text.append(buildOutput(isHTML, "profileValue.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessProfileValue ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM());
		xml.append(" ntk:id=\"ID\" ntk:IDReference=\"ID\" ntk:qualifier=\"qualifier\"");
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ntk:vocabulary=\"vocabulary\">");
		xml.append(TEST_VALUE).append("</ntk:AccessProfileValue>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_NTK_PREFIX,
				ProfileValue.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(ntkPrefix, ProfileValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			Util.addAttribute(element, ntkPrefix, "vocabulary", version.getNtkNamespace(), TEST_VOCABULARY);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing vocabulary
			Element element = Util.buildElement(ntkPrefix, ProfileValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util
				.buildElement(ntkPrefix, ProfileValue.getName(version), version.getNtkNamespace(), TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing vocabulary
			testConstructor(WILL_FAIL, TEST_VALUE, null, null, null, null);
			// Missing security attributes
			try {
				new ProfileValue(TEST_VALUE, TEST_VOCABULARY, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER, null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			ProfileValue component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			component = testConstructor(WILL_SUCCEED, null, TEST_VOCABULARY, null, null, null);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ntk:AccessProfileValue element was found with no value.";
			String locator = "ntk:AccessProfileValue";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileValue elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ProfileValue dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID,
				TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileValue elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ProfileValue dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_VOCABULARY, TEST_ID,
				TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, DIFFERENT_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, DIFFERENT_VALUE,
				TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID, DIFFERENT_VALUE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID, TEST_ID_REFERENCE,
				DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileValue component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileValue component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_VOCABULARY, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileValue component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			ProfileValue.Builder builder = new ProfileValue.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new ProfileValue.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new ProfileValue.Builder();
			builder.setValue(TEST_VALUE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		}
	}
}
