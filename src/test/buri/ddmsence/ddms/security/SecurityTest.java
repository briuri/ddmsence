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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.security.ntk.Access;
import buri.ddmsence.ddms.security.ntk.AccessTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:security elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SecurityTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public SecurityTest() {
		super("security.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Security getFixture() {
		try {
			return (new Security(null, null, SecurityAttributesTest.getFixture()));
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
	private Security getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Security component = null;
		try {
			component = new Security(element);
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
	 * @param noticeList the notice list (optional)
	 * @param access NTK access information (optional)
	 * @return a valid object
	 */
	private Security getInstance(String message, NoticeList noticeList, Access access) {
		boolean expectFailure = !Util.isEmpty(message);
		Security component = null;
		try {
			component = new Security(noticeList, access, SecurityAttributesTest.getFixture());
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String prefix = "security.";
		StringBuffer text = new StringBuffer();
		if (version.isAtLeast("3.0"))
			text.append(buildOutput(isHTML, prefix + "excludeFromRollup", "true"));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText", "noticeText"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.pocType", "ABC"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeText.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeType", "ABC"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeReason", "noticeReason"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.noticeDate", "2011-09-15"));
			text.append(buildOutput(isHTML, prefix + "noticeList.notice.unregisteredNoticeType",
				"unregisteredNoticeType"));
			text.append(buildOutput(isHTML, prefix + "noticeList.classification", "U"));
			text.append(buildOutput(isHTML, prefix + "noticeList.ownerProducer", "USA"));
			text.append(AccessTest.getFixture().getOutput(isHTML, "security."));
		}
		text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, prefix));
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
			xml.append("\t\t<ISM:Notice ISM:noticeType=\"ABC\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
			xml.append("\t\t\t<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"ABC\">noticeText</ISM:NoticeText>\n");
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

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Security
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version
					.getIsmNamespace(), "true");
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance(SUCCESS, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, NoticeListTest.getFixture(), AccessTest.getFixture());

			// No optional fields
			getInstance(SUCCESS, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing excludeFromRollup
			if (version.isAtLeast("3.0")) {
				Element element = Util.buildDDMSElement(Security.getName(version), null);
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("The excludeFromRollup attribute is required.", element);

				// Incorrect excludeFromRollup
				element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version
					.getIsmNamespace(), "false");
				getInstance("The excludeFromRollup attribute must have a fixed value", element);

				// Invalid excludeFromRollup
				element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version
					.getIsmNamespace(), "aardvark");
				getInstance("The excludeFromRollup attribute is required.", element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Bad security attributes
			try {
				new Security(null, null, null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "security attributes is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Security component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Nested warnings
			if (version.isAtLeast("4.0")) {
				Element element = Util.buildDDMSElement(Security.getName(version), null);
				Util.addAttribute(element, PropertyReader.getPrefix("ism"), "excludeFromRollup", version
					.getIsmNamespace(), "true");
				Element accessElement = Util.buildElement(PropertyReader.getPrefix("ntk"), Access.getName(version),
					version.getNtkNamespace(), null);
				SecurityAttributesTest.getFixture().addTo(accessElement);
				element.appendChild(accessElement);
				SecurityAttributesTest.getFixture().addTo(element);
				component = getInstance(SUCCESS, element);
				assertEquals(1, component.getValidationWarnings().size());
				String text = "An ntk:Access element was found with no";
				String locator = "ddms:security/ntk:Access";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Security dataComponent = getInstance(SUCCESS, NoticeListTest.getFixture(), AccessTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			if (version.isAtLeast("4.0")) {
				Security elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
				Security dataComponent = getInstance(SUCCESS, NoticeListTest.getFixture(), null);
				assertFalse(elementComponent.equals(dataComponent));

				dataComponent = getInstance(SUCCESS, null, AccessTest.getFixture());
				assertFalse(elementComponent.equals(dataComponent));
			}
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, NoticeListTest.getFixture(), AccessTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, NoticeListTest.getFixture(), AccessTest.getFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersionExcludeFromRollup() throws InvalidDDMSException {
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
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The excludeFromRollup attribute cannot be used");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Security component = getInstance(SUCCESS, getValidElement(sVersion));

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
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "SuperSecret is not a valid enumeration token");
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
