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
package buri.ddmsence.ddms.security.ntk;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ntk:AccessProfileList elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProfileListTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public ProfileListTest() {
		super("accessProfileList.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static ProfileList getFixture() {
		try {
			return (new ProfileList(ProfileTest.getFixtureList(), SecurityAttributesTest.getFixture()));
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
	private ProfileList getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		ProfileList component = null;
		try {
			component = new ProfileList(element);
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
	 * @param profiles the profiles in this list (required)
	 */
	private ProfileList getInstance(String message, List<Profile> profiles) {
		boolean expectFailure = !Util.isEmpty(message);
		ProfileList component = null;
		try {
			component = new ProfileList(profiles, SecurityAttributesTest.getFixture());
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
		text.append(ProfileTest.getFixtureList().get(0).getOutput(isHTML, "profileList.", ""));
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
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_NTK_PREFIX,
				ProfileList.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, ProfileTest.getFixtureList());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// Missing profile
			Element element = Util.buildElement(ntkPrefix, ProfileList.getName(version), version.getNtkNamespace(),
				null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("At least one profile is required.", element);

			// Missing security attributes
			element = Util.buildElement(ntkPrefix, ProfileList.getName(version), version.getNtkNamespace(), null);
			for (Profile profile : ProfileTest.getFixtureList())
				element.appendChild(profile.getXOMElementCopy());
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing profile
			getInstance("At least one profile is required.", (List) null);

			// Missing security attributes
			try {
				new ProfileList(ProfileTest.getFixtureList(), null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			ProfileList component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			ProfileList dataComponent = getInstance(SUCCESS, ProfileTest.getFixtureList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			List<Profile> profiles = ProfileTest.getFixtureList();
			List<ProfileValue> valueList = new ArrayList<ProfileValue>();
			valueList.add(ProfileValueTest.getFixture("profile2"));
			profiles.add(new Profile(SystemNameTest.getFixture(), valueList, SecurityAttributesTest.getFixture()));
			ProfileList dataComponent = getInstance(SUCCESS, profiles);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, ProfileTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = getInstance(SUCCESS, ProfileTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersion() {
		// Implicit, since the NTK namespace does not exist before DDMS 4.0.1.
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList component = getInstance(SUCCESS, getValidElement(sVersion));
			ProfileList.Builder builder = new ProfileList.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList.Builder builder = new ProfileList.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getProfiles().get(0);
			assertTrue(builder.isEmpty());
			builder.getProfiles().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ProfileList.Builder builder = new ProfileList.Builder();
			builder.getSecurityAttributes().setClassification("U");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least one profile is required.");
			}
			builder.getProfiles().get(0).getSystemName().setValue("TEST");
			builder.getProfiles().get(0).getProfileValues().get(0).setVocabulary("vocab");
			builder.getProfiles().get(0).getProfileValues().get(0).setValue("TEST");
			builder.getProfiles().get(0).getSystemName().getSecurityAttributes().setClassification("U");
			builder.getProfiles().get(0).getSystemName().getSecurityAttributes().setOwnerProducers(
				Util.getXsListAsList("USA"));
			builder.getProfiles().get(0).getProfileValues().get(0).getSecurityAttributes().setClassification("U");
			builder.getProfiles().get(0).getProfileValues().get(0).getSecurityAttributes().setOwnerProducers(
				Util.getXsListAsList("USA"));
			builder.getProfiles().get(0).getSecurityAttributes().setClassification("U");
			builder.getProfiles().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			ProfileList.Builder builder = new ProfileList.Builder();
			assertNotNull(builder.getProfiles().get(1));
		}
	}
}
