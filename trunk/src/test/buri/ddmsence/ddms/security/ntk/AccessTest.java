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
 * <p>Tests related to ntk:Access elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class AccessTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public AccessTest() {
		super("access.xml");
	}

	/**
	 * Creates a Access fixture
	 */
	public static Access getFixture() {
		try {
			return (new Access(getIndividualList(), null, null, SecurityAttributesTest.getFixture(false)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static List<Individual> getIndividualList() {
		List<Individual> list = new ArrayList<Individual>();
		list.add(IndividualTest.getFixture());
		return (list);
	}
	
	/**
	 * Helper method to create a fixture
	 */
	private static List<Group> getGroupList() {
		List<Group> list = new ArrayList<Group>();
		list.add(GroupTest.getFixture());
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
	private Access testConstructor(boolean expectFailure, Element element) {
		Access component = null;
		try {
			component = new Access(element);
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
	 * @param individuals the individuals
	 * @param groups the groups
	 * @parma profileList the profilesprofiles the profiles in this list (required)
	 */
	private Access testConstructor(boolean expectFailure, List<Individual> individuals, List<Group> groups,
		ProfileList profileList) {
		Access component = null;
		try {
			component = new Access(individuals, groups, profileList, SecurityAttributesTest.getFixture(false));
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
		xml.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t\t\t<ntk:AccessIndividualValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">user_2321889:Doe_John_H</ntk:AccessIndividualValue>\n");
		xml.append("\t\t</ntk:AccessIndividual>\n");
		xml.append("\t</ntk:AccessIndividualList>\n");
		xml.append("\t<ntk:AccessGroupList>\n");
		xml.append("\t\t<ntk:AccessGroup ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t\t\t<ntk:AccessGroupValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">WISE/RODCA</ntk:AccessGroupValue>\n");
		xml.append("\t\t</ntk:AccessGroup>\n");
		xml.append("\t</ntk:AccessGroupList>\n");
		xml.append("\t<ntk:AccessProfileList ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t\t<ntk:AccessProfile ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t\t\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t\t\t<ntk:AccessProfileValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ntk:vocabulary=\"vocabulary\">profile</ntk:AccessProfileValue>\n");
		xml.append("\t\t</ntk:AccessProfile>\n");
		xml.append("\t</ntk:AccessProfileList>\n");
		xml.append("</ntk:Access>");
		
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Access.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ntk.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ntk.prefix") + ":" + Access.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getProperty("ntk.prefix");
			
			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));
			
			// No optional fields
			Element element = Util.buildElement(ntkPrefix, Access.getName(version), version.getNtkNamespace(), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), ProfileListTest.getFixture());
			
			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getProperty("ntk.prefix");

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing security attributes
			Element element = Util.buildElement(ntkPrefix, Access.getName(version), version.getNtkNamespace(), null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing security attributes
			try {
				new Access(null, null, null, null);
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
			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
			
			// Empty
			component = testConstructor(WILL_SUCCEED, null, null, null);
			assertEquals(1, component.getValidationWarnings().size());
			String text = "An ntk:Access element was found with no individual, group, or profile information.";
			String locator = "ntk:Access";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Access dataComponent = testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), ProfileListTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Access dataComponent = testConstructor(WILL_SUCCEED, null, getGroupList(), ProfileListTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
			
			dataComponent = testConstructor(WILL_SUCCEED, getIndividualList(), null, ProfileListTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
			
			dataComponent = testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), ProfileListTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), ProfileListTest.getFixture());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getIndividualList(), getGroupList(), ProfileListTest.getFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Access component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

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
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Access.Builder builder = new Access.Builder();
			assertNotNull(builder.getIndividuals().get(1));
			assertNotNull(builder.getGroups().get(1));
		}
	}
}
