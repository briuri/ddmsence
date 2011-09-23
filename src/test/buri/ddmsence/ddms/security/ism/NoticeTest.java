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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ISM:Notice elements</p>
 * 
 * <p> The valid instance of ISM:Notice is generated, rather than relying on the ISM schemas to validate an XML
 * file.</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public NoticeTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a canned fixed value for testing.
	 * 
	 * @return a XOM element representing a valid applicationSoftware
	 */
	public static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String ismPrefix = PropertyReader.getPrefix("ism");
		String ismNs = version.getIsmNamespace();

		Element element = Util.buildElement(ismPrefix, Notice.getName(version), ismNs, null);
		element.addNamespaceDeclaration(ismPrefix, version.getIsmNamespace());
		NoticeAttributesTest.getFixture().addTo(element);
		SecurityAttributesTest.getFixture(false).addTo(element);
		element.appendChild(NoticeTextTest.getFixtureElement());
		return (element);
	}

	/**
	 * Helper method to create a list of noticeTexts
	 */
	private List<NoticeText> getNoticeTextList() throws InvalidDDMSException {
		List<NoticeText> list = new ArrayList<NoticeText>();
		list.add(new NoticeText(NoticeTextTest.getFixtureElement()));
		return (list);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Notice testConstructor(boolean expectFailure, Element element) {
		Notice component = null;
		try {
			component = new Notice(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param noticeTexts the notice texts (at least 1 required)
	 * @return a valid object
	 */
	private Notice testConstructor(boolean expectFailure, List<NoticeText> noticeTexts) {
		Notice component = null;
		try {
			component = new Notice(noticeTexts, SecurityAttributesTest.getFixture(false), NoticeAttributesTest
				.getFixture());
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "notice.noticeText", "noticeText"));
		text.append(buildOutput(isHTML, "notice.noticeText.pocType", "DoD-Dist-B"));
		text.append(buildOutput(isHTML, "notice.noticeText.classification", "U"));
		text.append(buildOutput(isHTML, "notice.noticeText.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "notice.classification", "U"));
		text.append(buildOutput(isHTML, "notice.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "notice.noticeType", "noticeType"));
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
		xml.append("ISM:noticeType=\"noticeType\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
		xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\">noticeText</ISM:NoticeText></ISM:Notice>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getFixtureElement()), DEFAULT_ISM_PREFIX, Notice
				.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getNoticeTextList());

			// No attributes
			try {
				new Notice(getNoticeTextList(), null, null);
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
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No NoticeTexts
			testConstructor(WILL_FAIL, (List) null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Notice component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Notice dataComponent = testConstructor(WILL_SUCCEED, getNoticeTextList());

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			List<NoticeText> list = getNoticeTextList();
			list.add(new NoticeText(NoticeTextTest.getFixtureElement()));
			Notice elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Notice dataComponent = testConstructor(WILL_SUCCEED, list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getNoticeTextList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, getNoticeTextList());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() throws InvalidDDMSException {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new Notice(getNoticeTextList(), SecurityAttributesTest.getFixture(false), null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Notice component = testConstructor(WILL_SUCCEED, getFixtureElement());

			Notice.Builder builder = new Notice.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getNoticeTexts().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());
			// Equality after Building
			builder = new Notice.Builder(component);
			assertEquals(builder.commit(), component);
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
