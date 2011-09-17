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
package buri.ddmsence.ddms.security.ntk;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ntk:ProfileList elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProfileListTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public ProfileListTest() {
		super("accessProfileList.xml");
	}

	/**
	 * Creates a ProfileList fixture
	 */
	public static ProfileList getFixture() {
		try {
			return (new ProfileList(getProfileList(), SecurityAttributesTest.getFixture(false)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static List<Profile> getProfileList() {
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(ProfileTest.getFixture());
		return (profiles);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ProfileList testConstructor(boolean expectFailure, Element element) {
		ProfileList component = null;
		try {
			component = new ProfileList(element);
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
	 * @param profiles the profiles in this list (required)
	 */
	private ProfileList testConstructor(boolean expectFailure, List<Profile> profiles) {
		ProfileList component = null;
		try {
			component = new ProfileList(profiles, SecurityAttributesTest.getFixture(false));
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
		text.append(getProfileList().get(0).getOutput(isHTML, "profileList."));
		text.append(buildOutput(isHTML, "profileList.classification", "U"));
		text.append(buildOutput(isHTML, "profileList.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessProfileList ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t<ntk:AccessProfile ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t\t<ntk:AccessProfileValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ntk:vocabulary=\"vocabulary\">profile</ntk:AccessProfileValue>\n");
		xml.append("\t</ntk:AccessProfile>\n");
		xml.append("</ntk:AccessProfileList>\n");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(ProfileList.getName(version), component.getName());
			assertEquals(PropertyReader.getPrefix("ntk"), component.getPrefix());
			assertEquals(PropertyReader.getPrefix("ntk") + ":" + ProfileList.getName(version),
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
			testConstructor(WILL_SUCCEED, getValidElement(versionString));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getProfileList());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing profile
			Element element = Util.buildElement(ntkPrefix, ProfileList.getName(version), version.getNtkNamespace(),
				null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildElement(ntkPrefix, ProfileList.getName(version), version.getNtkNamespace(), null);
			for (Profile profile : getProfileList())
				element.appendChild(profile.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing profile
			testConstructor(WILL_FAIL, (List) null);

			// Missing security attributes
			try {
				new ProfileList(getProfileList(), null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
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
			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			ProfileList dataComponent = testConstructor(WILL_SUCCEED, getProfileList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			List<Profile> profiles = getProfileList();
			List<ProfileValue> valueList = new ArrayList<ProfileValue>();
			valueList.add(ProfileValueTest.getFixture("profile2"));
			profiles.add(new Profile(SystemNameTest.getFixture(), valueList, SecurityAttributesTest.getFixture(false)));
			ProfileList dataComponent = testConstructor(WILL_SUCCEED, profiles);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getProfileList());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getProfileList());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, getProfileList());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			ProfileList component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			ProfileList.Builder builder = new ProfileList.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new ProfileList.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getProfiles().get(0);
			assertTrue(builder.isEmpty());
			builder.getProfiles().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new ProfileList.Builder();
			builder.getSecurityAttributes().setClassification("U");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			ProfileList.Builder builder = new ProfileList.Builder();
			assertNotNull(builder.getProfiles().get(1));
		}
	}
}