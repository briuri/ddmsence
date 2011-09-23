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

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to the ISM notice attributes</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeAttributesTest extends AbstractComponentTestCase {

	private static final String TEST_NOTICE_TYPE = "noticeType";
	private static final String TEST_NOTICE_REASON = "noticeReason";
	private static final String TEST_NOTICE_DATE = "2011-09-15";
	private static final String TEST_UNREGISTERED_NOTICE_TYPE = "unregisteredNoticeType";

	/**
	 * Constructor
	 */
	public NoticeAttributesTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static NoticeAttributes getFixture() {
		try {
			return (new NoticeAttributes(TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
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
	private NoticeAttributes testConstructor(boolean expectFailure, Element element) {
		NoticeAttributes attributes = null;
		try {
			attributes = new NoticeAttributes(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param noticeType the notice type (with a value from the CVE)
	 * @param noticeReason the reason associated with a notice
	 * @param noticeDate the date associated with a notice
	 * @param unregisteredNoticeType a notice type not in the CVE
	 * @return a valid object
	 */
	private NoticeAttributes testConstructor(boolean expectFailure, String noticeType, String noticeReason,
		String noticeDate, String unregisteredNoticeType) {
		NoticeAttributes attributes = null;
		try {
			attributes = new NoticeAttributes(noticeType, noticeReason, noticeDate, unregisteredNoticeType);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			getFixture().addTo(element);
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildDDMSElement(Resource.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ismPrefix = PropertyReader.getPrefix("ism");
			String icNamespace = version.getIsmNamespace();

			// invalid noticeType
			// TBD

			// invalid noticeDate
			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.NOTICE_DATE_NAME, icNamespace, "2001");
			testConstructor(WILL_FAIL, element);

			StringBuffer longString = new StringBuffer();
			for (int i = 0; i < NoticeAttributes.MAX_LENGTH / 10 + 1; i++) {
				longString.append("0123456789");
			}

			// too long noticeReason
			element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.NOTICE_REASON_NAME, icNamespace, longString
				.toString());
			testConstructor(WILL_FAIL, element);

			// too long unregisteredNoticeType
			element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.UNREGISTERED_NOTICE_TYPE_NAME, icNamespace,
				longString.toString());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// invalid noticeType
			// TBD

			// horribly invalid noticeDate
			testConstructor(WILL_FAIL, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, "baboon", TEST_UNREGISTERED_NOTICE_TYPE);

			// invalid noticeDate
			testConstructor(WILL_FAIL, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, "2001", TEST_UNREGISTERED_NOTICE_TYPE);

			StringBuffer longString = new StringBuffer();
			for (int i = 0; i < NoticeAttributes.MAX_LENGTH / 10 + 1; i++) {
				longString.append("0123456789");
			}

			// too long noticeReason
			testConstructor(WILL_FAIL, TEST_NOTICE_TYPE, longString.toString(), TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE);

			// too long unregisteredNoticeType
			testConstructor(WILL_FAIL, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE, longString.toString());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Element element = Util.buildDDMSElement(Resource.getName(version), null);

			getFixture().addTo(element);
			NoticeAttributes attr = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, attr.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String icNamespace = version.getIsmNamespace();

			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), Security.EXCLUDE_FROM_ROLLUP_NAME, icNamespace,
				"true");
			getFixture().addTo(element);
			NoticeAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			NoticeAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_NOTICE_TYPE, TEST_NOTICE_REASON,
				TEST_NOTICE_DATE, TEST_UNREGISTERED_NOTICE_TYPE);

			assertEquals(elementAttributes, elementAttributes);
			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			getFixture().addTo(element);
			NoticeAttributes expected = testConstructor(WILL_SUCCEED, element);

			NoticeAttributes test = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_NOTICE_REASON,
				TEST_NOTICE_DATE, TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = testConstructor(WILL_SUCCEED, TEST_NOTICE_TYPE, DIFFERENT_VALUE, TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = testConstructor(WILL_SUCCEED, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, "2011-08-15",
				TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = testConstructor(WILL_SUCCEED, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE,
				DIFFERENT_VALUE);
			assertFalse(expected.equals(test));
		}

	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes elementAttributes = getFixture();
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementAttributes.equals(wrongComponent));
		}
	}

	public void testWrongVersion() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		Element element = Util.buildDDMSElement(Resource.getName(version), null);
		Util.addAttribute(element, PropertyReader.getPrefix("ism"), NoticeAttributes.NOTICE_DATE_NAME, version
			.getIsmNamespace(), "2011-09-15");
		testConstructor(WILL_FAIL, element);

		DDMSVersion.setCurrentVersion("4.0");
		NoticeAttributes attributes = getFixture();
		try {
			attributes.addTo(element);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testEmptiness() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, null, null, null);
			assertTrue(dataAttributes.isEmpty());
			dataAttributes = testConstructor(WILL_SUCCEED, null, null, null, TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(dataAttributes.isEmpty());
		}
	}

	public void testDateOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes dataAttributes = testConstructor(WILL_SUCCEED, null, null, "2005-10-10", null);
			assertEquals(buildOutput(true, "noticeDate", "2005-10-10"), dataAttributes.getOutput(true, ""));
			assertEquals(buildOutput(false, "noticeDate", "2005-10-10"), dataAttributes.getOutput(false, ""));
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes component = getFixture();

			// Equality after Building
			NoticeAttributes.Builder builder = new NoticeAttributes.Builder(component);
			assertEquals(builder.commit(), component);

			// Validation
			builder = new NoticeAttributes.Builder();
			assertTrue(builder.isEmpty());
			builder.setNoticeType("");
			assertTrue(builder.isEmpty());
			builder.setUnregisteredNoticeType("test");
			assertFalse(builder.isEmpty());
			builder.setNoticeDate("2001");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
