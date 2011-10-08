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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to the ISM notice attributes </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeAttributesTest extends AbstractBaseTestCase {

	private static final String TEST_NOTICE_TYPE = "ABC";
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
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private NoticeAttributes getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		NoticeAttributes attributes = null;
		try {
			attributes = new NoticeAttributes(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param noticeType the notice type (with a value from the CVE)
	 * @param noticeReason the reason associated with a notice
	 * @param noticeDate the date associated with a notice
	 * @param unregisteredNoticeType a notice type not in the CVE
	 * @return a valid object
	 */
	private NoticeAttributes getInstance(String message, String noticeType, String noticeReason, String noticeDate,
		String unregisteredNoticeType) {
		boolean expectFailure = !Util.isEmpty(message);
		NoticeAttributes attributes = null;
		try {
			attributes = new NoticeAttributes(noticeType, noticeReason, noticeDate, unregisteredNoticeType);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (attributes);
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			getFixture().addTo(element);
			getInstance(SUCCESS, element);

			// No optional fields
			element = Util.buildDDMSElement(Resource.getName(version), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE, TEST_UNREGISTERED_NOTICE_TYPE);

			// No optional fields
			getInstance(SUCCESS, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ismPrefix = PropertyReader.getPrefix("ism");
			String icNamespace = version.getIsmNamespace();

			// invalid noticeType
			Element element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.NOTICE_TYPE_NAME, icNamespace, "Unknown");
			getInstance("Unknown is not a valid enumeration token", element);

			// invalid noticeDate
			element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.NOTICE_DATE_NAME, icNamespace, "2001");
			getInstance("The noticeDate attribute must be in the xs:date format", element);

			StringBuffer longString = new StringBuffer();
			for (int i = 0; i < NoticeAttributes.MAX_LENGTH / 10 + 1; i++) {
				longString.append("0123456789");
			}

			// too long noticeReason
			element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.NOTICE_REASON_NAME, icNamespace, longString
				.toString());
			getInstance("The noticeReason attribute must be shorter", element);

			// too long unregisteredNoticeType
			element = Util.buildDDMSElement(Resource.getName(version), null);
			Util.addAttribute(element, ismPrefix, NoticeAttributes.UNREGISTERED_NOTICE_TYPE_NAME, icNamespace,
				longString.toString());
			getInstance("The unregisteredNoticeType attribute must be shorter", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// invalid noticeType
			getInstance("Unknown is not a valid enumeration token", "Unknown", TEST_NOTICE_REASON, "2001",
				TEST_UNREGISTERED_NOTICE_TYPE);

			// horribly invalid noticeDate
			getInstance("The ISM:noticeDate attribute is not in a valid date format.", TEST_NOTICE_TYPE,
				TEST_NOTICE_REASON, "baboon", TEST_UNREGISTERED_NOTICE_TYPE);

			// invalid noticeDate
			getInstance("The noticeDate attribute must be in the xs:date format", TEST_NOTICE_TYPE, TEST_NOTICE_REASON,
				"2001", TEST_UNREGISTERED_NOTICE_TYPE);

			StringBuffer longString = new StringBuffer();
			for (int i = 0; i < NoticeAttributes.MAX_LENGTH / 10 + 1; i++) {
				longString.append("0123456789");
			}

			// too long noticeReason
			getInstance("The noticeReason attribute must be shorter", TEST_NOTICE_TYPE, longString.toString(),
				TEST_NOTICE_DATE, TEST_UNREGISTERED_NOTICE_TYPE);

			// too long unregisteredNoticeType
			getInstance("The unregisteredNoticeType attribute must be shorter", TEST_NOTICE_TYPE, TEST_NOTICE_REASON,
				TEST_NOTICE_DATE, longString.toString());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Element element = Util.buildDDMSElement(Resource.getName(version), null);

			getFixture().addTo(element);
			NoticeAttributes attr = getInstance(SUCCESS, element);
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
			NoticeAttributes elementAttributes = getInstance(SUCCESS, element);
			NoticeAttributes dataAttributes = getInstance(SUCCESS, TEST_NOTICE_TYPE, TEST_NOTICE_REASON,
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
			NoticeAttributes expected = getInstance(SUCCESS, element);

			NoticeAttributes test = getInstance(SUCCESS, "DEF", TEST_NOTICE_REASON, TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = getInstance(SUCCESS, TEST_NOTICE_TYPE, DIFFERENT_VALUE, TEST_NOTICE_DATE,
				TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = getInstance(SUCCESS, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, "2011-08-15",
				TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(expected.equals(test));

			test = getInstance(SUCCESS, TEST_NOTICE_TYPE, TEST_NOTICE_REASON, TEST_NOTICE_DATE, DIFFERENT_VALUE);
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

	public void testAddTo() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			NoticeAttributes component = getFixture();
			
			Element element = Util.buildDDMSElement("sample", null);
			component.addTo(element);
			NoticeAttributes output = new NoticeAttributes(element);
			assertEquals(component, output);
		}
	}

	public void testGetNonNull() throws InvalidDDMSException {
		NoticeAttributes component = new NoticeAttributes(null, null, null, null);
		NoticeAttributes output = NoticeAttributes.getNonNullInstance(null);
		assertEquals(component, output);
		assertTrue(output.isEmpty());
		
		output = NoticeAttributes.getNonNullInstance(getFixture());
		assertEquals(getFixture(), output);
	}
	
	public void testWrongVersion() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		Element element = Util.buildDDMSElement(Resource.getName(version), null);
		Util.addAttribute(element, PropertyReader.getPrefix("ism"), NoticeAttributes.NOTICE_DATE_NAME, version
			.getIsmNamespace(), "2011-09-15");
		getInstance("Notice attributes cannot be used", element);

		DDMSVersion.setCurrentVersion("4.0");
		NoticeAttributes attributes = getFixture();
		try {
			attributes.addTo(element);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "These attributes cannot decorate");
		}
	}

	public void testIsEmpty() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes dataAttributes = getInstance(SUCCESS, null, null, null, null);
			assertTrue(dataAttributes.isEmpty());
			dataAttributes = getInstance(SUCCESS, null, null, null, TEST_UNREGISTERED_NOTICE_TYPE);
			assertFalse(dataAttributes.isEmpty());
		}
	}

	public void testDateOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes dataAttributes = getInstance(SUCCESS, null, null, "2005-10-10", null);
			assertEquals(buildOutput(true, "noticeDate", "2005-10-10"), dataAttributes.getOutput(true, ""));
			assertEquals(buildOutput(false, "noticeDate", "2005-10-10"), dataAttributes.getOutput(false, ""));
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			
			NoticeAttributes component = getFixture();
			NoticeAttributes.Builder builder = new NoticeAttributes.Builder(component);
			assertEquals(builder.commit(), component);
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes.Builder builder = new NoticeAttributes.Builder();
			assertNotNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setNoticeType("");
			assertTrue(builder.isEmpty());
			builder.setNoticeReason(TEST_NOTICE_REASON);
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeAttributes.Builder builder = new NoticeAttributes.Builder();
			builder.setNoticeDate("2001");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The noticeDate attribute must be in the xs:date format");
			}
			builder.setNoticeDate("2011-01-20");
			builder.commit();
		}
	}
}
