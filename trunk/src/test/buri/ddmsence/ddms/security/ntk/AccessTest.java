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

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>
 * Tests related to ntk:Access elements
 * </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class AccessTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public AccessTest() {
		super("access.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Access getFixture() {
		try {
			return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? new Access(IndividualTest.getFixtureList(),
				null, null, SecurityAttributesTest.getFixture()) : null);
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
	private Access getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Access component = null;
		try {
			component = new Access(element);
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
	 * @param individuals the individuals
	 * @param groups the groups
	 * @parma profileList the profilesprofiles the profiles in this list (required)
	 */
	private Access getInstance(String message, List<Individual> individuals, List<Group> groups, ProfileList profileList) {
		boolean expectFailure = !Util.isEmpty(message);
		Access component = null;
		try {
			component = new Access(individuals, groups, profileList, SecurityAttributesTest.getFixture());
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
		text.append(IndividualTest.getFixture().getOutput(isHTML, "access.individualList."));
		text.append(GroupTest.getFixture().getOutput(isHTML, "access.groupList."));
		text.append(ProfileListTest.getFixture().getOutput(isHTML, "access."));
		text.append(buildOutput(isHTML, "access.classification", "U"));
		text.append(buildOutput(isHTML, "access.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:Access ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t<ntk:AccessIndividualList>\n");
		xml.append("\t\t<ntk:AccessIndividual ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml
			.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml
			.append("\t\t\t<ntk:AccessIndividualValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">user_2321889:Doe_John_H</ntk:AccessIndividualValue>\n");
		xml.append("\t\t</ntk:AccessIndividual>\n");
		xml.append("\t</ntk:AccessIndividualList>\n");
		xml.append("\t<ntk:AccessGroupList>\n");
		xml.append("\t\t<ntk:AccessGroup ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml
			.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml
			.append("\t\t\t<ntk:AccessGroupValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">WISE/RODCA</ntk:AccessGroupValue>\n");
		xml.append("\t\t</ntk:AccessGroup>\n");
		xml.append("\t</ntk:AccessGroupList>\n");
		xml.append("\t<ntk:AccessProfileList ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t\t<ntk:AccessProfile ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml
			.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml
			.append("\t\t\t<ntk:AccessProfileValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ntk:vocabulary=\"vocabulary\">profile</ntk:AccessProfileValue>\n");
		xml.append("\t\t</ntk:AccessProfile>\n");
		xml.append("\t</ntk:AccessProfileList>\n");
		xml.append("</ntk:Access>");

		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_NTK_PREFIX, Access
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildElement(ntkPrefix, Access.getName(version), version.getNtkNamespace(), null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, IndividualTest.getFixtureList(), GroupTest.getFixtureList(), ProfileListTest
				.getFixture());

			// No optional fields
			getInstance(SUCCESS, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// Missing security attributes
			Element element = Util.buildElement(ntkPrefix, Access.getName(version), version.getNtkNamespace(), null);
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing security attributes
			try {
				new Access(null, null, null, null);
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
			Access component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Empty
			component = getInstance(SUCCESS, null, null, null);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "An ntk:Access element was found with no individual, group, or profile information.";
			String locator = "ntk:Access";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Access elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Access dataComponent = getInstance(SUCCESS, IndividualTest.getFixtureList(), GroupTest.getFixtureList(),
				ProfileListTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Access elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Access dataComponent = getInstance(SUCCESS, null, GroupTest.getFixtureList(), ProfileListTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, IndividualTest.getFixtureList(), null, ProfileListTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, IndividualTest.getFixtureList(), GroupTest.getFixtureList(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Access component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, IndividualTest.getFixtureList(), GroupTest.getFixtureList(),
				ProfileListTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Access component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, IndividualTest.getFixtureList(), GroupTest.getFixtureList(),
				ProfileListTest.getFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Access component = getInstance(SUCCESS, getValidElement(sVersion));

			// Equality after Building
			Access.Builder builder = new Access.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Access.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getIndividuals().get(0);
			assertTrue(builder.isEmpty());
			builder.getGroups().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new Access.Builder();
			builder.getSecurityAttributes().setClassification("U");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least 1 ownerProducer must be set.");
			}
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}

	public void testWrongVersion() {
		// Implicit, since the NTK namespace does not exist before DDMS 4.0.
	}
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Access.Builder builder = new Access.Builder();
			assertNotNull(builder.getIndividuals().get(1));
			assertNotNull(builder.getGroups().get(1));
		}
	}
}
