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
package buri.ddmsence.ddms.security.ism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to the ISM attributes</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SecurityAttributesTest extends AbstractComponentTestCase {

	private static final String TEST_CLASS = "U";
	private static final List<String> TEST_OWNERS = new ArrayList<String>();
	static {
		TEST_OWNERS.add("USA");
	}
	private static final Map<String, String> TEST_OTHERS_31 = new HashMap<String, String>();
	static {
		TEST_OTHERS_31.put(SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "RD");
		TEST_OTHERS_31.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_31.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_31.put(SecurityAttributes.COMPILATION_REASON_NAME, "NO");
		TEST_OTHERS_31.put(SecurityAttributes.COMPLIES_WITH_NAME, "ICD-710");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_31.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_31.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_31.put(SecurityAttributes.DISPLAY_ONLY_TO_NAME, "AIA");
		TEST_OTHERS_31.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_31.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_31.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_31.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_31.put(SecurityAttributes.NON_US_CONTROLS_NAME, "ATOMAL");
		TEST_OTHERS_31.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_31.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_31.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
	}
	private static final Map<String, String> TEST_OTHERS_30 = new HashMap<String, String>();
	static {
		TEST_OTHERS_30.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_30.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_30.put(SecurityAttributes.COMPILATION_REASON_NAME, "NO");
		TEST_OTHERS_30.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-11");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_30.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_30.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_30.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_30.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_30.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_30.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_30.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_30.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_30.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
		TEST_OTHERS_30.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
	}
	private static final Map<String, String> TEST_OTHERS_20 = new HashMap<String, String>();
	static {
		TEST_OTHERS_20.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_20.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_20.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-11");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
		TEST_OTHERS_20.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_20.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_20.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_20.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_20.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_20.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_20.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_20.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_20.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
		TEST_OTHERS_20.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
	}

	/**
	 * Constructor
	 */
	public SecurityAttributesTest() {
		super(null);
	}

	/**
	 * Resets the validation property.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		PropertyReader.setProperty("ism.cve.validationAsErrors", "true");
	}

	/**
	 * Returns a set of attributes for a specific version of DDMS.
	 * 
	 * @return an attribute group
	 */
	private static Map<String, String> getOtherAttributes() {
		String version = DDMSVersion.getCurrentVersion().getVersion();
		if ("2.0".equals(version))
			return (new HashMap<String, String>(TEST_OTHERS_20));
		if ("3.0".equals(version))
			return (new HashMap<String, String>(TEST_OTHERS_30));
		return (new HashMap<String, String>(TEST_OTHERS_31));
	}

	/**
	 * Returns a set of attributes for a specific version of DDMS, with a single attribute replaced by a custom value.
	 * 
	 * @param key the key of the attribute to replace
	 * @param value the new value to set for that attribute
	 * @return an attribute group
	 */
	private static Map<String, String> getOtherAttributes(String key, String value) {
		Map<String, String> baseAttributes = new HashMap(getOtherAttributes());
		baseAttributes.put(key, value);
		return (baseAttributes);
	}

	/**
	 * Helper method to confirm that changing a single attribute correctly affects equality of two instances
	 * 
	 * @param expected the base set of attributes
	 * @param key the key of the attribute that will change
	 * @param value the value of the attribute that will change
	 */
	private void assertAttributeChangeAffectsEquality(SecurityAttributes expected, String key, String value) {
		Map<String, String> others = getOtherAttributes(key, value);
		assertFalse(expected.equals(testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, others)));
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @param includeAll true if all attributes should be included.
	 * @return SecurityAttributes
	 */
	public static SecurityAttributes getFixture(boolean includeAll) throws InvalidDDMSException {
		Map<String, String> others = getOtherAttributes();
		return (new SecurityAttributes(TEST_CLASS, TEST_OWNERS, includeAll ? others : null));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private SecurityAttributes testConstructor(boolean expectFailure, Element element) {
		SecurityAttributes attributes = null;
		try {
			attributes = new SecurityAttributes(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param classification the classification level, which must be a legal classification type (optional)
	 * @param ownerProducers a list of ownerProducers (optional)
	 * @param otherAttributes a name/value mapping of other ISM attributes. The value will be a String value, as it
	 * appears in XML.
	 * @return a valid object
	 */
	private SecurityAttributes testConstructor(boolean expectFailure, String classification,
		List<String> ownerProducers, Map<String, String> otherAttributes) {
		SecurityAttributes attributes = null;
		try {
			attributes = new SecurityAttributes(classification, ownerProducers, otherAttributes);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ismPrefix = PropertyReader.getPrefix("ism");
			String icNamespace = version.getIsmNamespace();

			// All fields
			Element element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, ismPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			getFixture(true).addTo(element);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, ismPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, ismPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, ismPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace,
				Util.getXsList(TEST_OWNERS));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, getOtherAttributes());

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, null);

			// Extra fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, getOtherAttributes("notAnAttribute", "test"));
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ismPrefix = PropertyReader.getPrefix("ism");
			String icNamespace = version.getIsmNamespace();

			// invalid declassDate
			Element element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, ismPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, ismPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, ismPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace,
				Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, ismPrefix, SecurityAttributes.DECLASS_DATE_NAME, icNamespace, "2001");
			testConstructor(WILL_FAIL, element);

			// invalid dateOfExemptedSource
			element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, ismPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, ismPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, ismPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace,
				Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, ismPrefix, SecurityAttributes.DECLASS_DATE_NAME, icNamespace, "2001");
			Util.addAttribute(element, ismPrefix, SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace, "2001");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);

			// invalid declassDate
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS,
				getOtherAttributes(SecurityAttributes.DECLASS_DATE_NAME, "2004"));

			// nonsensical declassDate
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS,
				getOtherAttributes(SecurityAttributes.DECLASS_DATE_NAME, "notAnXmlDate"));

			// invalid dateOfExemptedSource
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS,
				getOtherAttributes(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2004"));

			// nonsensical dateOfExemptedSource
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS,
				getOtherAttributes(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "notAnXmlDate"));
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String icNamespace = version.getIsmNamespace();

			// No warnings
			Element element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), Security.EXCLUDE_FROM_ROLLUP_NAME,
				icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes attr = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, attr.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String icNamespace = version.getIsmNamespace();

			Element element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), Security.EXCLUDE_FROM_ROLLUP_NAME,
				icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS,
				getOtherAttributes());

			assertEquals(elementAttributes, elementAttributes);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String icNamespace = version.getIsmNamespace();

			Element element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), Security.EXCLUDE_FROM_ROLLUP_NAME,
				icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes expected = testConstructor(WILL_SUCCEED, element);

			if (version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "FRD");
			assertFalse(expected.equals(testConstructor(WILL_SUCCEED, "C", TEST_OWNERS, getOtherAttributes()))); // Classification
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.CLASSIFIED_BY_NAME, DIFFERENT_VALUE);
			if (version.isAtLeast("3.0"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.COMPILATION_REASON_NAME,
					DIFFERENT_VALUE);
			if (version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.COMPLIES_WITH_NAME, "DoD5230.24");
			if (!version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME,
					"2001-10-10");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DECLASS_DATE_NAME, "2001-10-10");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DECLASS_EVENT_NAME, DIFFERENT_VALUE);
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X4");
			if (!version.isAtLeast("3.0"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "false");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME,
				DIFFERENT_VALUE);
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DERIVED_FROM_NAME, DIFFERENT_VALUE);
			if (version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DISPLAY_ONLY_TO_NAME, "USA");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "EYES");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.FGI_SOURCE_OPEN_NAME, "BGR");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "BGR");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.NON_IC_MARKINGS_NAME, "SBU");
			if (version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.NON_US_CONTROLS_NAME, "BALK");
			assertFalse(expected.equals(testConstructor(WILL_SUCCEED, TEST_CLASS, Util.getXsListAsList("AUS"),
				getOtherAttributes()))); // OwnerProducer
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.RELEASABLE_TO_NAME, "BGR");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-AIA");
			assertAttributeChangeAffectsEquality(expected, SecurityAttributes.SCI_CONTROLS_NAME, "TK");
			if (!version.isAtLeast("3.1"))
				assertAttributeChangeAffectsEquality(expected, SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "X4");
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SecurityAttributes elementAttributes = getFixture(true);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementAttributes.equals(wrongComponent));
		}
	}

	public void testWrongVersionAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		SecurityAttributes attr = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, getOtherAttributes());
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Title("Wrong Version Title", attr);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void test30AttributesIn31() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		Map<String, String> others = getOtherAttributes(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.0 attributes in 3.1.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		others = getOtherAttributes(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2010-01-01");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.0 attributes in 3.1.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void test31AttributesIn30() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		Map<String, String> others = getOtherAttributes(SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "RD");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		others = getOtherAttributes(SecurityAttributes.COMPLIES_WITH_NAME, "ICD-710");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		others = getOtherAttributes(SecurityAttributes.DISPLAY_ONLY_TO_NAME, "AIA");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		others = getOtherAttributes(SecurityAttributes.NON_US_CONTROLS_NAME, "ATOMAL");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testClassificationValidation() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Map<String, String> others = getOtherAttributes();

			// Missing classification
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, TEST_OWNERS, others);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Empty classification
			dataAttributes = testConstructor(WILL_SUCCEED, "", TEST_OWNERS, others);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Invalid classification
			testConstructor(WILL_FAIL, "ZOO", TEST_OWNERS, others);

			// No ownerProducers
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, null, others);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// No non-empty ownerProducers
			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("");
			dataAttributes = testConstructor(WILL_FAIL, TEST_CLASS, ownerProducers, others);
		}
	}

	public void testDateOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Map<String, String> others = new HashMap<String, String>();
			others.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, null, others);
			assertEquals(buildOutput(true, "declassDate", "2005-10-10"), dataAttributes.getOutput(true, ""));
			assertEquals(buildOutput(false, "declassDate", "2005-10-10"), dataAttributes.getOutput(false, ""));

			if (!version.isAtLeast("3.1")) {
				others = new HashMap<String, String>();
				others.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-10");
				dataAttributes = testConstructor(WILL_SUCCEED, null, null, others);
				assertEquals(buildOutput(true, "dateOfExemptedSource", "2005-10-10"),
					dataAttributes.getOutput(true, ""));
				assertEquals(buildOutput(false, "dateOfExemptedSource", "2005-10-10"),
					dataAttributes.getOutput(false, ""));
			}
		}
	}

	public void testOldClassifications() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		testConstructor(WILL_SUCCEED, "NS-S", TEST_OWNERS, null);
		testConstructor(WILL_SUCCEED, "NS-A", TEST_OWNERS, null);
		DDMSVersion.setCurrentVersion("3.0");
		testConstructor(WILL_FAIL, "NS-S", TEST_OWNERS, null);
		testConstructor(WILL_FAIL, "NS-A", TEST_OWNERS, null);
	}

	public void test30AttributesIn20() throws InvalidDDMSException {
		try {
			DDMSVersion.setCurrentVersion("3.0");
			Map<String, String> others = getOtherAttributes();
			DDMSVersion.setCurrentVersion("2.0");
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed DDMS 3.0 attributes to be used in DDMS 2.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void test20AttributesIn30() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = getOtherAttributes(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
		try {
			DDMSVersion.setCurrentVersion("3.0");
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			fail("Allowed DDMS 2.0 attributes to be used in DDMS 3.0.");
		} catch (InvalidDDMSException e) {
			// Good
		}

		DDMSVersion.setCurrentVersion("2.0");
		map.remove(SecurityAttributes.COMPILATION_REASON_NAME);
		SecurityAttributes attr = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
		assertTrue(attr.getOutput(true, "").contains(buildOutput(true, "declassManualReview", "true")));
		assertTrue(attr.getOutput(false, "").contains(buildOutput(false, "declassManualReview", "true")));
	}

	public void testMultipleDeclassException() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = getOtherAttributes(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1 25X2");
		new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
	}

	public void testMultipleTypeExempted() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = getOtherAttributes(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "X1 X2");
		new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
	}

	public void testDeclassManualReviewHtmlOutput() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
		SecurityAttributes attributes = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
		assertEquals(buildOutput(true, "classification", "U") + buildOutput(true, "declassManualReview", "true")
			+ buildOutput(true, "ownerProducer", "USA"), attributes.getOutput(true, ""));
	}

	public void testCVEErrorsByDefault() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "UnknownValue");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			fail("Allowed invalid CVE value without throwing an Exception.");
		} catch (InvalidDDMSException e) {
			assertEquals(
				"UnknownValue is not a valid enumeration token for this attribute, as specified in CVEnumISM25X.xml.",
				e.getMessage());
		}
	}

	public void testCVEWarnings() {
		PropertyReader.setProperty("ism.cve.validationAsErrors", "false");
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "UnknownValue");
		try {
			SecurityAttributes attr = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			List<ValidationMessage> warnings = attr.getValidationWarnings();
			assertEquals(1, warnings.size());
			assertEquals(
				"UnknownValue is not a valid enumeration token for this attribute, as specified in CVEnumISM25X.xml.",
				warnings.get(0).getText());
		} catch (InvalidDDMSException e) {
			fail("An exception was thrown when a warning was expected.");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			SecurityAttributes component = getFixture(true);

			// Equality after Building
			SecurityAttributes.Builder builder = new SecurityAttributes.Builder(component);
			assertEquals(builder.commit(), component);

			// Validation
			builder = new SecurityAttributes.Builder();
			builder.setClassification("SuperSecret");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			SecurityAttributes.Builder builder = new SecurityAttributes.Builder();
			assertNotNull(builder.getOwnerProducers().get(1));
			assertNotNull(builder.getSCIcontrols().get(1));
			assertNotNull(builder.getSARIdentifier().get(1));
			assertNotNull(builder.getDisseminationControls().get(1));
			assertNotNull(builder.getFGIsourceOpen().get(1));
			assertNotNull(builder.getFGIsourceProtected().get(1));
			assertNotNull(builder.getReleasableTo().get(1));
			assertNotNull(builder.getNonICmarkings().get(1));

			if (version.isAtLeast("3.1")) {
				assertNotNull(builder.getAtomicEnergyMarkings().get(1));
				assertNotNull(builder.getCompliesWith().get(1));
				assertNotNull(builder.getDisplayOnlyTo().get(1));
				assertNotNull(builder.getNonUSControls().get(1));
			}
		}
	}

	public void testBuilderListEquality() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.1");
		SecurityAttributes.Builder builder = new SecurityAttributes.Builder();
		builder.setAtomicEnergyMarkings(Util.getXsListAsList(""));
		assertTrue(builder.isEmpty());
		builder.setAtomicEnergyMarkings(Util.getXsListAsList("RD FRD"));
		assertFalse(builder.isEmpty());
	}
}
