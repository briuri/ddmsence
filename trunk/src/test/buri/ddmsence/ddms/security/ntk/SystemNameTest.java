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
 * <p>Tests related to ntk:AccessSystemName elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class SystemNameTest extends AbstractComponentTestCase {

	private static final String TEST_VALUE = "DIAS";
	private static final String TEST_ID = "ID";
	private static final String TEST_ID_REFERENCE = "ID";
	private static final String TEST_QUALIFIER = "qualifier";

	/**
	 * Constructor
	 */
	public SystemNameTest() {
		super("accessSystemName.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Creates a SystemName fixture
	 */
	public static SystemName getFixture() {
		try {
			return (new SystemName(TEST_VALUE, null, null, null, SecurityAttributesTest.getFixture(false)));
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
	private SystemName testConstructor(boolean expectFailure, Element element) {
		SystemName component = null;
		try {
			component = new SystemName(element);
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
	private SystemName testConstructor(boolean expectFailure, String value, String id, String idReference,
		String qualifier) {
		SystemName component = null;
		try {
			component = new SystemName(value, id, idReference, qualifier, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "systemName", TEST_VALUE));
		text.append(buildOutput(isHTML, "systemName.id", TEST_ID));
		text.append(buildOutput(isHTML, "systemName.idReference", TEST_ID_REFERENCE));
		text.append(buildOutput(isHTML, "systemName.qualifier", TEST_QUALIFIER));
		text.append(buildOutput(isHTML, "systemName.classification", "U"));
		text.append(buildOutput(isHTML, "systemName.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessSystemName ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM());
		xml.append(" ntk:id=\"ID\" ntk:IDReference=\"ID\" ntk:qualifier=\"qualifier\"");
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append(TEST_VALUE).append("</ntk:AccessSystemName>");
		return (xml.toString());
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_NTK_PREFIX,
				SystemName.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(ntkPrefix, SystemName.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_VALUE, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// Missing value
			Element element = Util
				.buildElement(ntkPrefix, SystemName.getName(version), version.getNtkNamespace(), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildElement(ntkPrefix, SystemName.getName(version), version.getNtkNamespace(), TEST_VALUE);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing value
			testConstructor(WILL_FAIL, null, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);

			// Missing security attributes
			try {
				new SystemName(TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER, null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			SystemName component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SystemName elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SystemName dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SystemName elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			SystemName dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_ID, TEST_ID_REFERENCE,
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

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SystemName component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SystemName component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SystemName component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			SystemName.Builder builder = new SystemName.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new SystemName.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new SystemName.Builder();
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