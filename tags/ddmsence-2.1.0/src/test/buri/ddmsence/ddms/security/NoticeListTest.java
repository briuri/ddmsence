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
package buri.ddmsence.ddms.security;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.Notice;
import buri.ddmsence.ddms.security.ism.NoticeTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:noticeList elements </p>
 * 
 * <p> Because a ddms:noticeList is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeListTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public NoticeListTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();

			Element element = Util.buildDDMSElement(NoticeList.getName(version), null);
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(NoticeTest.getFixtureElement());
			return (element);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static NoticeList getFixture() {
		try {
			return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? new NoticeList(getFixtureElement()) : null);
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
	private NoticeList getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		NoticeList component = null;
		try {
			component = new NoticeList(element);
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
	 * @param notices the notices (at least 1 required)
	 * @return a valid object
	 */
	private NoticeList getInstance(String message, List<Notice> notices) {
		boolean expectFailure = !Util.isEmpty(message);
		NoticeList component = null;
		try {
			component = new NoticeList(notices, SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText", "noticeText"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.pocType", "DoD-Dist-B"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.classification", "U"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "noticeList.notice.classification", "U"));
		text.append(buildOutput(isHTML, "noticeList.notice.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeType", "DoD-Dist-B"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeReason", "noticeReason"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeDate", "2011-09-15"));
		text.append(buildOutput(isHTML, "noticeList.notice.unregisteredNoticeType", "unregisteredNoticeType"));
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			text.append(buildOutput(isHTML, "noticeList.notice.externalNotice", "false"));	
		}
		text.append(buildOutput(isHTML, "noticeList.classification", "U"));
		text.append(buildOutput(isHTML, "noticeList.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:noticeList ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:Notice ISM:noticeType=\"DoD-Dist-B\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
		xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\"");
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.1")) {
			xml.append(" ISM:externalNotice=\"false\"");	
		}
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\">noticeText</ISM:NoticeText>");
		xml.append("</ISM:Notice>");
		xml.append("</ddms:noticeList>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_DDMS_PREFIX, NoticeList
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, NoticeTest.getFixtureList());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No Notices
			Element element = new Element(getFixtureElement());
			element.removeChildren();
			getInstance("At least one ISM:Notice", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No Notices
			getInstance("At least one ISM:Notice", (List) null);

			// No attributes
			try {
				new NoticeList(NoticeTest.getFixtureList(), null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			NoticeList component = getInstance(SUCCESS, getFixtureElement());

			// 4.1 ISM:externalNotice used
			if (version.isAtLeast("4.1")) {
				assertEquals(1, component.getValidationWarnings().size());	
				String text = "The ISM:externalNotice attribute in this DDMS component";
				String locator = "ddms:noticeList/ISM:Notice";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
			// No warnings 
			else {
				assertEquals(0, component.getValidationWarnings().size());
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList elementComponent = getInstance(SUCCESS, getFixtureElement());
			NoticeList dataComponent = getInstance(SUCCESS, NoticeTest.getFixtureList());

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			List<Notice> list = NoticeTest.getFixtureList();
			list.add(new Notice(NoticeTest.getFixtureElement()));
			NoticeList elementComponent = getInstance(SUCCESS, getFixtureElement());
			NoticeList dataComponent = getInstance(SUCCESS, list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, NoticeTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, NoticeTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		// Implicit, since 1 Notice is required and that requires DDMS 4.0.1 or greater.
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList component = getInstance(SUCCESS, getFixtureElement());
			NoticeList.Builder builder = new NoticeList.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList.Builder builder = new NoticeList.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getNotices().get(1).getNoticeTexts().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			NoticeList.Builder builder = new NoticeList.Builder();
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least one ISM:Notice");
			}
			builder.getNotices().get(0).getNoticeTexts().get(0).setValue("test");
			builder.getNotices().get(0).getSecurityAttributes().setClassification("U");
			builder.getNotices().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getNotices().get(0).getNoticeTexts().get(0).getSecurityAttributes().setClassification("U");
			builder.getNotices().get(0).getNoticeTexts().get(0).getSecurityAttributes().setOwnerProducers(
				Util.getXsListAsList("USA"));
			builder.commit();
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			NoticeList.Builder builder = new NoticeList.Builder();
			assertNotNull(builder.getNotices().get(1));
		}
	}
}
