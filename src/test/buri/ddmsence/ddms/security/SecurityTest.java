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

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.security.ntk.Access;
import buri.ddmsence.ddms.security.ntk.AccessTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:security elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SecurityTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public SecurityTest() {
		super("security.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure, Element element) {
		Security component = null;
		try {
			component = new Security(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Generates a NoticeList for testing.
	 */
	private NoticeList getNoticeList() throws InvalidDDMSException {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? new NoticeList(NoticeListTest.getFixtureElement())
			: null);
	}

	/**
	 * Generates an Access for testing.
	 */
	private Access getAccess() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? AccessTest.getFixture() : null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param noticeList the notice list (optional)
	 * @param access NTK access information (optional)
	 * @return a valid object
	 */
	private Security testConstructor(boolean expectFailure, NoticeList noticeList, Access access) {
		Security component = null;
		try {
			component = new Security(noticeList, access, SecurityAttributesTest.getFixture(false));
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = "security.";
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("3.0"))
			text.append(buildOutput(isHTML, prefix + "excludeFromRollup", "true"));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText", "noticeText"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.pocType", "DoD-Dist-B"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeType", "noticeType"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeReason", "noticeReason"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeDate", "2011-09-15"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.unregisteredNoticeType",
				"unregisteredNoticeType"));
			text.append(buildOutput(isHTML, prefix + "noticeList.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.ownerProducer", "USA"));
			text.append(AccessTest.getFixture().getOutput(isHTML, "security."));
		}
		text.append(SecurityAttributesTest.getFixture(false).getOutput(isHTML, prefix));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:security ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		if (version.isAtLeast("3.0"))
			xml.append("ISM:excludeFromRollup=\"true\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		if (!version.isAtLeast("4.0"))
			xml.append(" />");
		else {
			xml.append(">\n");
			xml.append("\t<ddms:noticeList ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t\t<ISM:Notice ISM:noticeType=\"noticeType\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t\t\t<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\">noticeText</ISM:NoticeText>\n");
			xml.append("\t\t</ISM:Notice>\n");
			xml.append("\t</ddms:noticeList>\n");
			xml.append("\t<ntk:Access xmlns:ntk=\"urn:us:gov:ic:ntk\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t\t<ntk:AccessIndividualList>\n");
			xml.append("\t\t\t<ntk:AccessIndividual ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
			xml.append("\t\t\t\t<ntk:AccessIndividualValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
			xml.append("user_2321889:Doe_John_H</ntk:AccessIndividualValue>\n");
			xml.append("\t\t\t</ntk:AccessIndividual>\n");
			xml.append("\t\t</ntk:AccessIndividualList>\n");
			xml.append("\t</ntk:Access>\n");
			xml.append("</ddms:security>");
		}
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Security.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup",
					version.getIsmNamespace(), "true");
				SecurityAttributesTest.getFixture(false).addTo(element);
				testConstructor(WILL_SUCCEED, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getNoticeList(), getAccess());

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing excludeFromRollup
			Element element = Util.buildDDMSElement(Security.getName(version), null);
			testConstructor(WILL_FAIL, element);

			// Incorrect excludeFromRollup
			element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version.getIsmNamespace(),
				"false");
			testConstructor(WILL_FAIL, element);

			// Invalid excludeFromRollup
			element = Util.buildDDMSElement(Security.getName(version), null);
			Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version.getIsmNamespace(),
				"aardvark");
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Bad security attributes
			try {
				new Security(null, null, (SecurityAttributes) null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Security component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Nested warnings
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup",
					version.getIsmNamespace(), "true");
				Element accessElement = Util.buildElement(PropertyReader.getPrefix("ntk"), Access.getName(version),
					version.getNtkNamespace(), null);
				SecurityAttributesTest.getFixture(false).addTo(accessElement);
				element.appendChild(accessElement);
				SecurityAttributesTest.getFixture(false).addTo(element);
				component = testConstructor(WILL_SUCCEED, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "An ntk:Access element was found with no individual, group, or profile information.";
				String locator = "ddms:security/ntk:Access";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Security dataComponent = testConstructor(WILL_SUCCEED, getNoticeList(), getAccess());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (version.isAtLeast("4.0")) {
				Security elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
				Security dataComponent = testConstructor(WILL_SUCCEED, getNoticeList(), null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = testConstructor(WILL_SUCCEED, null, getAccess());
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getNoticeList(), getAccess());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getNoticeList(), getAccess());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		String icPrefix = PropertyReader.getPrefix("ism");
		String icNamespace = version.getIsmNamespace();

		Element element = Util.buildDDMSElement("security", null);
		Util.addAttribute(element, icPrefix, "classification", icNamespace, "U");
		Util.addAttribute(element, icPrefix, "ownerProducer", icNamespace, "USA");
		Util.addAttribute(element, icPrefix, "excludeFromRollup", icNamespace, "true");
		try {
			new Security(element);
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Security.Builder builder = new Security.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Security.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Security.Builder();
			builder.getSecurityAttributes().setClassification("SuperSecret");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
