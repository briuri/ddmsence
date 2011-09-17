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
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ntk:AccessIndividualValue elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class IndividualValueTest extends AbstractComponentTestCase {

	private static final String TEST_VALUE = "user_2321889:Doe_John_H";
	private static final String TEST_ID = "ID";
	private static final String TEST_ID_REFERENCE = "ID";
	private static final String TEST_QUALIFIER = "qualifier";

	/**
	 * Constructor
	 */
	public IndividualValueTest() {
		super("accessIndividualValue.xml");
	}

	/**
	 * Creates an IndividualValue fixture
	 */
	public static IndividualValue getFixture() {
		try {
			return (new IndividualValue(TEST_VALUE, null, null, null, SecurityAttributesTest.getFixture(false)));
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
	private IndividualValue testConstructor(boolean expectFailure, Element element) {
		IndividualValue component = null;
		try {
			component = new IndividualValue(element);
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
	 * @param id the NTK ID (optional)
	 * @param idReference a reference to an NTK ID (optional)
	 * @param qualifier an NTK qualifier (optional)
	 * @return a valid object
	 */
	private IndividualValue testConstructor(boolean expectFailure, String value, String id, String idReference,
		String qualifier) {
		IndividualValue component = null;
		try {
			component = new IndividualValue(value, id, idReference, qualifier, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "individualValue", TEST_VALUE));
		text.append(buildOutput(isHTML, "individualValue.id", TEST_ID));
		text.append(buildOutput(isHTML, "individualValue.idReference", TEST_ID_REFERENCE));
		text.append(buildOutput(isHTML, "individualValue.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "individualValue.classification", "U"));
		text.append(buildOutput(isHTML, "individualValue.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessIndividualValue ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM());
		xml.append(" ntk:id=\"ID\" ntk:IDReference=\"ID\" ntk:qualifier=\"qualifier\"");
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ntk:AccessIndividualValue>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(IndividualValue.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ntk"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ntk") + ":" + IndividualValue.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildElement(ntkPrefix, IndividualValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing security attributes
			Element element = Util.buildElement(ntkPrefix, IndividualValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing security attributes
			try {
				new IndividualValue(TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER, null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			component = testConstructor(WILL_SUCCEED, null, null, null, null);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ntk:AccessIndividualValue element was found with no value.";
			String locator = "ntk:AccessIndividualValue";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			IndividualValue dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			IndividualValue dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, DIFFERENT_VALUE, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, DIFFERENT_VALUE, TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			IndividualValue component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			IndividualValue.Builder builder = new IndividualValue.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new IndividualValue.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new IndividualValue.Builder();
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
