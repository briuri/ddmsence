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
package buri.ddmsence.ddms.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to the ICISM attributes</p>
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
		TEST_OTHERS_31.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
		TEST_OTHERS_31.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_31.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_31.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_31.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_31.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_31.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_31.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_31.put(SecurityAttributes.COMPILATION_REASON_NAME, "NO");
		TEST_OTHERS_31.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_31.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_31.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_31.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_31.put(SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "RD");
		TEST_OTHERS_31.put(SecurityAttributes.COMPLIES_WITH_NAME, "ICD-710");
		TEST_OTHERS_31.put(SecurityAttributes.DISPLAY_ONLY_TO_NAME, "AIA");
		TEST_OTHERS_31.put(SecurityAttributes.NON_US_CONTROLS_NAME, "ATOMAL");
	}
	private static final Map<String, String> TEST_OTHERS_30 = new HashMap<String, String>();
	static {
		TEST_OTHERS_30.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
		TEST_OTHERS_30.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_30.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_30.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_30.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_30.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_30.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_30.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_30.put(SecurityAttributes.COMPILATION_REASON_NAME, "NO");
		TEST_OTHERS_30.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_30.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_30.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_30.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_30.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
		TEST_OTHERS_30.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-11");
	}
	private static final Map<String, String> TEST_OTHERS_20 = new HashMap<String, String>();
	static {
		TEST_OTHERS_20.put(SecurityAttributes.SCI_CONTROLS_NAME, "HCS");
		TEST_OTHERS_20.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-USA");
		TEST_OTHERS_20.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "FOUO");
		TEST_OTHERS_20.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "ALA");
		TEST_OTHERS_20.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "FGI");
		TEST_OTHERS_20.put(SecurityAttributes.RELEASABLE_TO_NAME, "AIA");
		TEST_OTHERS_20.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SINFO");
		TEST_OTHERS_20.put(SecurityAttributes.CLASSIFIED_BY_NAME, " MN");
		TEST_OTHERS_20.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, "OP");
		TEST_OTHERS_20.put(SecurityAttributes.CLASSIFICATION_REASON_NAME, "PQ");
		TEST_OTHERS_20.put(SecurityAttributes.DERIVED_FROM_NAME, "QR");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_EVENT_NAME, "RS");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1");
		TEST_OTHERS_20.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
		TEST_OTHERS_20.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-11");
		TEST_OTHERS_20.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
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
		PropertyReader.setProperty("icism.cve.validationAsErrors", "true");
	}
	
	/**
	 * Returns a set of attributes for a specific version of DDMS.
	 * 
	 * @param version the DDMS Version
	 * @return an attribute group
	 */
	private static Map<String, String> getOtherAttributes(String version) {
		if ("2.0".equals(version))
			return (new HashMap<String, String>(TEST_OTHERS_20));
		if ("3.0".equals(version))
			return (new HashMap<String, String>(TEST_OTHERS_30));
		return (new HashMap<String, String>(TEST_OTHERS_31));
	}
	
	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @param includeAll true if all attributes should be included.
	 * @return SecurityAttributes
	 */
	public static SecurityAttributes getFixture(boolean includeAll) throws InvalidDDMSException {
		Map<String, String> others = getOtherAttributes(DDMSVersion.getCurrentVersion().getVersion());		
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
	 * @param otherAttributes a name/value mapping of other ICISM attributes. The value will be a String value, as it
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
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			// All fields
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, icPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			getFixture(true).addTo(element);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, icPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, icPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, icPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace, 
				Util.getXsList(TEST_OWNERS));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Map<String, String> others = getOtherAttributes(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, others);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, null);

			// Extra fields
			Map<String, String> more = new HashMap<String, String>(others);
			more.put("notAnAttribute", "test");
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, more);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			// invalid declassDate
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, icPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, icPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, icPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace,
				Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, icPrefix, SecurityAttributes.DECLASS_DATE_NAME,
				icNamespace, "2001");
			testConstructor(WILL_FAIL, element);

			// invalid dateOfExemptedSource
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, icPrefix, Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			Util.addAttribute(element, icPrefix, SecurityAttributes.CLASSIFICATION_NAME, icNamespace, TEST_CLASS);
			Util.addAttribute(element, icPrefix, SecurityAttributes.OWNER_PRODUCER_NAME, icNamespace,
				Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, icPrefix, SecurityAttributes.DECLASS_DATE_NAME, icNamespace, "2001");
			Util.addAttribute(element, icPrefix, SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace,
				"2001");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// invalid declassDate
			Map<String, String> more = new HashMap<String, String>(getOtherAttributes(version));
			more.put(SecurityAttributes.DECLASS_DATE_NAME, "2004");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);

			// nonsensical declassDate
			more = new HashMap<String, String>(getOtherAttributes(version));
			more.put(SecurityAttributes.DECLASS_DATE_NAME, "notAnXmlDate");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);
			
			// invalid dateOfExemptedSource
			more = new HashMap<String, String>(getOtherAttributes(version));
			more.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2004");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);
			
			// nonsensical dateOfExemptedSource
			more = new HashMap<String, String>(getOtherAttributes(version));
			more.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "notAnXmlDate");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			// No warnings
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes attr = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, attr.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			Map<String, String> others = getOtherAttributes(version);
			
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, others);

			assertEquals(elementAttributes, elementAttributes);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();			
			Map<String, String> others = getOtherAttributes(version);
			
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace, "true");
			getFixture(true).addTo(element);
			SecurityAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, "C", TEST_OWNERS, others);
			assertFalse(elementAttributes.equals(dataAttributes));

			List<String> owners = new ArrayList<String>(TEST_OWNERS);
			owners.add("AUS");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, owners, others);
			assertFalse(elementAttributes.equals(dataAttributes));

			Map<String, String> changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.SCI_CONTROLS_NAME, "TK");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.SAR_IDENTIFIER_NAME, "SAR-AIA");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DISSEMINATION_CONTROLS_NAME, "EYES");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.FGI_SOURCE_OPEN_NAME, "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.RELEASABLE_TO_NAME, "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.NON_IC_MARKINGS_NAME, "SBU");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.CLASSIFIED_BY_NAME, DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			if (!"2.0".equals(version)) {
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.COMPILATION_REASON_NAME, DIFFERENT_VALUE);
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
			}

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DERIVED_FROM_NAME, DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DECLASS_DATE_NAME, "2001-10-10");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DECLASS_EVENT_NAME, DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(others);
			changed.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X4");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			if (!"3.1".equals(version)) {
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "X4");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));

				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2001-10-10");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
			}
			if ("3.1".equals(version)) {
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "FRD");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
				
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.COMPLIES_WITH_NAME, "DoD5230.24");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
				
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.DISPLAY_ONLY_TO_NAME, "USA");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
				
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.NON_US_CONTROLS_NAME, "BALK");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
			}
			if ("2.0".equals(version)) {
				changed = new HashMap<String, String>(others);
				changed.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "false");
				dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
				assertFalse(elementAttributes.equals(dataAttributes));
			}
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SecurityAttributes elementAttributes = getFixture(true);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementAttributes.equals(wrongComponent));
		}
	}
	
	public void testWrongVersionAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		SecurityAttributes attr = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, getOtherAttributes("3.0"));
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
		Map<String, String> others = getOtherAttributes("3.1");
		others.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "OADR");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.0 attributes in 3.1.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		others = getOtherAttributes("3.1");
		others.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2010-01-01");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.0 attributes in 3.1.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	public void test31AttributesIn30() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		Map<String, String> others = getOtherAttributes("3.0");
		others.put(SecurityAttributes.ATOMIC_ENERGY_MARKINGS_NAME, "RD");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		others = getOtherAttributes("3.0");
		others.put(SecurityAttributes.COMPLIES_WITH_NAME, "ICD-710");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		others = getOtherAttributes("3.0");
		others.put(SecurityAttributes.DISPLAY_ONLY_TO_NAME, "AIA");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		others = getOtherAttributes("3.0");
		others.put(SecurityAttributes.NON_US_CONTROLS_NAME, "ATOMAL");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, others);
			fail("Allowed 3.1 attributes in 3.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testClassificationValidation() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Map<String, String> others = getOtherAttributes(version);
			
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
			List<String> owners = new ArrayList<String>();
			owners.add("");
			dataAttributes = testConstructor(WILL_FAIL, TEST_CLASS, owners, others);
		}
	}

	public void testDateOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Map<String, String> others = new HashMap<String, String>();
			others.put(SecurityAttributes.DECLASS_DATE_NAME, "2005-10-10");
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, null, others);
			assertEquals("<meta name=\"declassDate\" content=\"2005-10-10\" />\n", dataAttributes.toHTML(""));
			assertEquals("Declass Date: 2005-10-10\n", dataAttributes.toText(""));

			if (!"3.1".equals(version)) {
				others = new HashMap<String, String>();
				others.put(SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, "2005-10-10");
				dataAttributes = testConstructor(WILL_SUCCEED, null, null, others);
				assertEquals("<meta name=\"dateOfExemptedSource\" content=\"2005-10-10\" />\n", dataAttributes.toHTML(""));
				assertEquals("Date Of Exempted Source: 2005-10-10\n", dataAttributes.toText(""));
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
			DDMSVersion.setCurrentVersion("2.0");
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, getOtherAttributes("3.0"));
			fail("Allowed DDMS 3.0 attributes to be used in DDMS 2.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void test20AttributesIn30() throws InvalidDDMSException {
		Map<String, String> map = new HashMap<String, String>(getOtherAttributes("2.0"));
		map.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
		try {
			DDMSVersion.setCurrentVersion("3.0");
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			fail("Allowed DDMS 2.0 attributes to be used in DDMS 3.0.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		DDMSVersion.setCurrentVersion("2.0");
		map.remove(SecurityAttributes.COMPILATION_REASON_NAME);
		SecurityAttributes attr = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
		assertTrue(attr.toHTML("").contains("<meta name=\"declassManualReview\" content=\"true\" />"));
		assertTrue(attr.toText("").contains("Declass Manual Review: true"));		
	}
	
	public void testMultipleDeclassException() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = new HashMap<String, String>(TEST_OTHERS_20);
		map.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "25X1 25X2");
		new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
	}
	
	public void testMultipleTypeExempted() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = new HashMap<String, String>(TEST_OTHERS_20);
		map.put(SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, "X1 X2");
		new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
	}
	
	public void testDeclassManualReviewHtmlOutput() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, "true");
		SecurityAttributes attributes = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
		assertEquals("<meta name=\"classification\" content=\"U\" />\n"
			+ "<meta name=\"declassManualReview\" content=\"true\" />\n"
			+ "<meta name=\"ownerProducer\" content=\"USA\" />\n", attributes.toHTML(""));
	}
	
	public void testCVEErrorsByDefault() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "UnknownValue");
		try {
			new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			fail("Allowed invalid CVE value without throwing an Exception.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("UnknownValue is not a valid enumeration token for this attribute, as specified in CVEnumISM25X.xml.", e.getMessage());
		}
	}
	
	public void testCVEWarnings() {
		PropertyReader.setProperty("icism.cve.validationAsErrors", "false");
		Map<String, String> map = new HashMap<String, String>();
		map.put(SecurityAttributes.DECLASS_EXCEPTION_NAME, "UnknownValue");
		try {
			SecurityAttributes attr = new SecurityAttributes(TEST_CLASS, TEST_OWNERS, map);
			List<ValidationMessage> warnings = attr.getValidationWarnings();
			assertEquals(1, warnings.size());
			assertEquals("UnknownValue is not a valid enumeration token for this attribute, as specified in CVEnumISM25X.xml.",
				warnings.get(0).getText());
		}
		catch (InvalidDDMSException e) {
			fail("An exception was thrown when a warning was expected.");
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
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
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SecurityAttributes.Builder builder = new SecurityAttributes.Builder();
			assertNotNull(builder.getOwnerProducers().get(1));
			assertNotNull(builder.getSCIcontrols().get(1));
			assertNotNull(builder.getSARIdentifier().get(1));
			assertNotNull(builder.getDisseminationControls().get(1));
			assertNotNull(builder.getFGIsourceOpen().get(1));
			assertNotNull(builder.getFGIsourceProtected().get(1));
			assertNotNull(builder.getReleasableTo().get(1));
			assertNotNull(builder.getNonICmarkings().get(1));
			
			if ("3.1".equals(version)) {
				assertNotNull(builder.getAtomicEnergyMarkings().get(1));
				assertNotNull(builder.getCompliesWith().get(1));
				assertNotNull(builder.getDisplayOnlyTo().get(1));
				assertNotNull(builder.getNonUSControls().get(1));
			}	
		}
	}
}
