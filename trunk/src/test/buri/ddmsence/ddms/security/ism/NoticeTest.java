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
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ISM:Notice elements </p>
 * 
 * <p> The valid instance of ISM:Notice is generated, rather than relying on the ISM schemas to validate an XML file.
 * </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public NoticeTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			String ismPrefix = PropertyReader.getPrefix("ism");
			String ismNs = version.getIsmNamespace();

			Element element = Util.buildElement(ismPrefix, Notice.getName(version), ismNs, null);
			element.addNamespaceDeclaration(ismPrefix, version.getIsmNamespace());
			NoticeAttributesTest.getFixture().addTo(element);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(NoticeTextTest.getFixtureElement());
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
	public static List<Notice> getFixtureList() {
		try {
			List<Notice> list = new ArrayList<Notice>();
			list.add(new Notice(NoticeTest.getFixtureElement()));
			return (list);
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
	private Notice getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Notice component = null;
		try {
			component = new Notice(element);
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
	 * @param noticeTexts the notice texts (at least 1 required)
	 * @return a valid object
	 */
	private Notice getInstance(String message, List<NoticeText> noticeTexts) {
		boolean expectFailure = !Util.isEmpty(message);
		Notice component = null;
		try {
			component = new Notice(noticeTexts, SecurityAttributesTest.getFixture(), NoticeAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "notice.noticeText", "noticeText"));
		text.append(buildOutput(isHTML, "notice.noticeText.pocType", "ABC"));
		text.append(buildOutput(isHTML, "notice.noticeText.classification", "U"));
		text.append(buildOutput(isHTML, "notice.noticeText.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "notice.classification", "U"));
		text.append(buildOutput(isHTML, "notice.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "notice.noticeType", "ABC"));
		text.append(buildOutput(isHTML, "notice.noticeReason", "noticeReason"));
		text.append(buildOutput(isHTML, "notice.noticeDate", "2011-09-15"));
		text.append(buildOutput(isHTML, "notice.unregisteredNoticeType", "unregisteredNoticeType"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ISM:Notice ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:noticeType=\"ABC\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
		xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"ABC\">noticeText</ISM:NoticeText></ISM:Notice>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_ISM_PREFIX, Notice
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
			getInstance(SUCCESS, NoticeTextTest.getFixtureList());

			// No attributes
			try {
				new Notice(NoticeTextTest.getFixtureList(), null, null);
			}
			catch (InvalidDDMSException e) {
				fail("Prevented valid data.");
			}
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No NoticeTexts
			Element element = new Element(getFixtureElement());
			element.removeChildren();
			getInstance("At least one ISM:NoticeText", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No NoticeTexts
			getInstance("At least one ISM:NoticeText", (List) null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Notice component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice elementComponent = getInstance(SUCCESS, getFixtureElement());
			Notice dataComponent = getInstance(SUCCESS, NoticeTextTest.getFixtureList());

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			List<NoticeText> list = NoticeTextTest.getFixtureList();
			list.add(new NoticeText(NoticeTextTest.getFixtureElement()));
			Notice elementComponent = getInstance(SUCCESS, getFixtureElement());
			Notice dataComponent = getInstance(SUCCESS, list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, NoticeTextTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, NoticeTextTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		// Implicit, since 1 NoticeText is required and that requires DDMS 4.0 or greater.
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = getInstance(SUCCESS, getFixtureElement());
			Notice.Builder builder = new Notice.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice.Builder builder = new Notice.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getNoticeTexts().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice.Builder builder = new Notice.Builder();
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			try {
				builder.commit();
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least one ISM:NoticeText");
			}
			builder.getNoticeTexts().get(0).setValue("TEST");
			builder.getNoticeTexts().get(0).getSecurityAttributes().setClassification("U");
			builder.getNoticeTexts().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Notice.Builder builder = new Notice.Builder();
			assertNotNull(builder.getNoticeTexts().get(1));
		}
	}
}
