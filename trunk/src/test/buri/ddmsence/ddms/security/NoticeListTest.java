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
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.Notice;
import buri.ddmsence.ddms.security.ism.NoticeTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:noticeList elements</p>
 * 
 * <p> Because a ddms:noticeList is a local component, we cannot load a valid document from a unit test data file.
 * We have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class NoticeListTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public NoticeListTest() {
		super(null);
	}
			
	/**
	 * Returns a canned fixed value for testing.
	 * 
	 * @return a XOM element representing a valid applicationSoftware
	 */
	public static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		
		Element element = Util.buildDDMSElement(NoticeList.getName(version), null);	
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		SecurityAttributesTest.getFixture(false).addTo(element);
		element.appendChild(NoticeTest.getFixtureElement());
		return (element);
	}
	
	/**
	 * Helper method to create a list of notices
	 */
	private List<Notice> getNoticeList() throws InvalidDDMSException {
		List<Notice> list = new ArrayList<Notice>();
		list.add(new Notice(NoticeTest.getFixtureElement()));
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
	private NoticeList testConstructor(boolean expectFailure, Element element) {
		NoticeList component = null;
		try {
			component = new NoticeList(element);
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
	 * @param notices the notices (at least 1 required)
	 * @return a valid object
	 */
	private NoticeList testConstructor(boolean expectFailure, List<Notice> notices) {
		NoticeList component = null;
		try {
			component = new NoticeList(notices, SecurityAttributesTest.getFixture(false));
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
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText", "noticeText"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.pocType", "DoD-Dist-B"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.classification", "U"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeText.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "noticeList.notice.classification", "U"));
		text.append(buildOutput(isHTML, "noticeList.notice.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeType", "noticeType"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeReason", "noticeReason"));
		text.append(buildOutput(isHTML, "noticeList.notice.noticeDate", "2011-09-15"));
		text.append(buildOutput(isHTML, "noticeList.notice.unregisteredNoticeType", "unregisteredNoticeType"));
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
		xml.append("<ISM:Notice ISM:noticeType=\"noticeType\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
		xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\">noticeText</ISM:NoticeText>");
		xml.append("</ISM:Notice>");
		xml.append("</ddms:noticeList>");
		System.out.println(xml.toString());
		return (xml.toString());
	}
	
	public void testNameAndNamespace() throws InvalidDDMSException  {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(NoticeList.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ddms"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ddms") + ":" + NoticeList.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getNoticeList());			
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No Notices
			Element element = new Element(getFixtureElement());
			element.removeChildren();
			testConstructor(WILL_FAIL, element);			
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No Notices
			testConstructor(WILL_FAIL, (List) null);
			
			// No attributes
			try {
				new NoticeList(getNoticeList(), null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			
			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			NoticeList dataComponent = testConstructor(WILL_SUCCEED, getNoticeList());

			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			List<Notice> list = getNoticeList();
			list.add(new Notice(NoticeTest.getFixtureElement()));
			NoticeList elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			NoticeList dataComponent = testConstructor(WILL_SUCCEED, list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getNoticeList());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getNoticeList());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, getNoticeList());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() throws InvalidDDMSException {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new NoticeList(getNoticeList(),SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			NoticeList component = testConstructor(WILL_SUCCEED, getFixtureElement());

			NoticeList.Builder builder = new NoticeList.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getNotices().get(1).getNoticeTexts().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());
			// Equality after Building
			builder = new NoticeList.Builder(component);
			assertEquals(builder.commit(), component);
		}
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			NoticeList.Builder builder = new NoticeList.Builder();
			assertNotNull(builder.getNotices().get(1));
		}
	}
}
