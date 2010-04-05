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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
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
	private static final Map<String, String> TEST_OTHERS = new HashMap<String, String>();
	static {
		TEST_OTHERS.put("SCIcontrols", "HCS");
		TEST_OTHERS.put("SARIdentifier", "SAR-USA");
		TEST_OTHERS.put("disseminationControls", "UCNI");
		TEST_OTHERS.put("FGIsourceOpen", "ALA");
		TEST_OTHERS.put("FGIsourceProtected", "FGI");
		TEST_OTHERS.put("releasableTo", "AIA");
		TEST_OTHERS.put("nonICmarkings", "SINFO");
		TEST_OTHERS.put("classifiedBy", " MN");
		TEST_OTHERS.put("compilationReason", "NO");
		TEST_OTHERS.put("derivativelyClassifiedBy", "OP");
		TEST_OTHERS.put("classificationReason", "PQ");
		TEST_OTHERS.put("derivedFrom", "QR");
		TEST_OTHERS.put("declassDate", "2005-10-10");
		TEST_OTHERS.put("declassEvent", "RS");
		TEST_OTHERS.put("declassException", "25X1");
		TEST_OTHERS.put("typeOfExemptedSource", "OADR");
		TEST_OTHERS.put("dateOfExemptedSource", "2005-10-11");
	}

	/**
	 * Constructor
	 */
	public SecurityAttributesTest() {
		super(null);
	}

	/**
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @param includeAll true if all attributes should be included.
	 * @return SecurityAttributes
	 */
	public static SecurityAttributes getFixture(boolean includeAll) throws InvalidDDMSException {
		return (new SecurityAttributes(TEST_CLASS, TEST_OWNERS, includeAll ? TEST_OTHERS : null));
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
			// All fields
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			getFixture(true).addTo(element);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			Util.addAttribute(element, ICISM_PREFIX, "classification", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), TEST_CLASS);
			Util.addAttribute(element, ICISM_PREFIX, "ownerProducer", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), Util.getXsList(TEST_OWNERS));
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, TEST_OTHERS);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, null);

			// Extra fields
			Map<String, String> more = new HashMap<String, String>(TEST_OTHERS);
			more.put("notAnAttribute", "test");
			testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, more);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// invalid declassDate
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			Util.addAttribute(element, ICISM_PREFIX, "classification", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), TEST_CLASS);
			Util.addAttribute(element, ICISM_PREFIX, "ownerProducer", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, ICISM_PREFIX, "declassDate",
				DDMSVersion.getCurrentVersion().getIcismNamespace(), "2001");
			testConstructor(WILL_FAIL, element);

			// invalid dateOfExemptedSource
			element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			Util.addAttribute(element, ICISM_PREFIX, "classification", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), TEST_CLASS);
			Util.addAttribute(element, ICISM_PREFIX, "ownerProducer", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), Util.getXsList(TEST_OWNERS));
			Util.addAttribute(element, ICISM_PREFIX, "declassDate",
				DDMSVersion.getCurrentVersion().getIcismNamespace(), "2001");
			Util.addAttribute(element, ICISM_PREFIX, "dateOfExemptedSource", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "2001");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// invalid declassDate
			Map<String, String> more = new HashMap<String, String>(TEST_OTHERS);
			more.put("declassDate", "2004");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);

			// invalid dateOfExemptedSource
			more = new HashMap<String, String>(TEST_OTHERS);
			more.put("dateOfExemptedSource", "2004");
			testConstructor(WILL_FAIL, TEST_CLASS, TEST_OWNERS, more);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			getFixture(true).addTo(element);
			SecurityAttributes attr = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, attr.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			getFixture(true).addTo(element);
			SecurityAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, TEST_OTHERS);

			assertEquals(elementAttributes, elementAttributes);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Element element = Util.buildDDMSElement(Security.NAME, null);
			Util.addAttribute(element, ICISM_PREFIX, "excludeFromRollup", DDMSVersion.getCurrentVersion()
				.getIcismNamespace(), "true");
			getFixture(true).addTo(element);
			SecurityAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, "C", TEST_OWNERS, TEST_OTHERS);
			assertFalse(elementAttributes.equals(dataAttributes));

			List<String> owners = new ArrayList<String>(TEST_OWNERS);
			owners.add("AUS");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, owners, TEST_OTHERS);
			assertFalse(elementAttributes.equals(dataAttributes));

			Map<String, String> changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("SCIcontrols", "TK");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("SARIdentifier", "SAR-AIA");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("disseminationControls", "SAMI");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("FGIsourceOpen", "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("FGIsourceProtected", "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("releasableTo", "BGR");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("nonICmarkings", "SBU");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("classifiedBy", DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("compilationReason", DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("derivativelyClassifiedBy", DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("derivedFrom", DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("declassDate", "2001-10-10");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("declassEvent", DIFFERENT_VALUE);
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("declassException", "25X4");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("typeOfExemptedSource", "X4");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));

			changed = new HashMap<String, String>(TEST_OTHERS);
			changed.put("dateOfExemptedSource", "2001-10-10");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, TEST_OWNERS, changed);
			assertFalse(elementAttributes.equals(dataAttributes));
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

	public void testClassificationValidation() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing classification
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, TEST_OWNERS, TEST_OTHERS);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Empty classification
			dataAttributes = testConstructor(WILL_SUCCEED, "", TEST_OWNERS, TEST_OTHERS);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Invalid classification
			testConstructor(WILL_FAIL, "ZOO", TEST_OWNERS, TEST_OTHERS);

			// No ownerProducers
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_CLASS, null, TEST_OTHERS);
			try {
				dataAttributes.requireClassification();
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// No non-empty ownerProducers
			List<String> owners = new ArrayList<String>();
			owners.add("");
			dataAttributes = testConstructor(WILL_FAIL, TEST_CLASS, owners, TEST_OTHERS);
		}
	}

	public void testDateOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Map<String, String> others = new HashMap<String, String>();
			others.put("declassDate", "2005-10-10");
			others.put("dateOfExemptedSource", "2005-10-10");
			SecurityAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, null, others);
			assertEquals(
				"<meta name=\"declassDate\" content=\"2005-10-10\" />\n<meta name=\"dateOfExemptedSource\" content=\"2005-10-10\" />\n",
				dataAttributes.toHTML(""));
			assertEquals("Declass Date: 2005-10-10\nDate Of Exempted Source: 2005-10-10\n", dataAttributes.toText(""));
		}
	}
}
