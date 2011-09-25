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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
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
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static IndividualValue getFixture() {
		try {
			return (new IndividualValue(TEST_VALUE, null, null, null, SecurityAttributesTest.getFixture()));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<IndividualValue> getFixtureList() {
		List<IndividualValue> list = new ArrayList<IndividualValue>();
		list.add(IndividualValueTest.getFixture());
		return (list);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private IndividualValue getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		IndividualValue component = null;
		try {
			component = new IndividualValue(element);
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
	 * @param value the value of the element's child text
	 * @param id the NTK ID (optional)
	 * @param idReference a reference to an NTK ID (optional)
	 * @param qualifier an NTK qualifier (optional)
	 * @return a valid object
	 */
	private IndividualValue getInstance(String message, String value, String id, String idReference,
		String qualifier) {
		boolean expectFailure = !Util.isEmpty(message);
		IndividualValue component = null;
		try {
			component = new IndividualValue(value, id, idReference, qualifier, SecurityAttributesTest.getFixture());
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_NTK_PREFIX,
				IndividualValue.getName(version));
			getInstance("Unexpected namespace URI and local name encountered: ddms:wrongName", getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(ntkPrefix, IndividualValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);

			// No optional fields
			getInstance(SUCCESS, TEST_VALUE, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// Missing security attributes
			Element element = Util.buildElement(ntkPrefix, IndividualValue.getName(version), version.getNtkNamespace(),
				TEST_VALUE);
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing security attributes
			try {
				new IndividualValue(TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER, null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			IndividualValue component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// No value
			component = getInstance(SUCCESS, null, null, null, null);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "A ntk:AccessIndividualValue element was found with no value.";
			String locator = "ntk:AccessIndividualValue";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			IndividualValue elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			IndividualValue dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			IndividualValue elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			IndividualValue dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_ID, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, DIFFERENT_VALUE, TEST_ID_REFERENCE,
				TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_ID, DIFFERENT_VALUE, TEST_QUALIFIER);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, DIFFERENT_VALUE);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			IndividualValue component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			IndividualValue component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_ID, TEST_ID_REFERENCE, TEST_QUALIFIER);
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			IndividualValue component = getInstance(SUCCESS, getValidElement(sVersion));

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
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		}
	}
}
