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
package buri.ddmsence.ddms.resource;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.RoleEntityTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:contributor elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ContributorTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public ContributorTest() {
		super("contributor.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Contributor getFixture() {
		try {
			return (new Contributor(ServiceTest.getFixture(), null, null));
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
	private Contributor getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Contributor component = null;
		try {
			SecurityAttributesTest.getFixture().addTo(element);
			component = new Contributor(element);
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
	 * @param entity the producer entity
	 * @param pocTypes the pocType (DDMS 4.0.1 or later)
	 */
	private Contributor getInstance(String message, IRoleEntity entity, List<String> pocTypes) {
		boolean expectFailure = !Util.isEmpty(message);
		Contributor component = null;
		try {
			component = new Contributor(entity, pocTypes, SecurityAttributesTest.getFixture());
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
		StringBuffer text = new StringBuffer();
		text.append(OrganizationTest.getFixture().getOutput(isHTML, "contributor.", ""));
		if (version.isAtLeast("4.0.1"))
			text.append(buildOutput(isHTML, "contributor.pocType", "DoD-Dist-B"));
		text.append(buildOutput(isHTML, "contributor.classification", "U"));
		text.append(buildOutput(isHTML, "contributor.ownerProducer", "USA"));
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
		xml.append("<ddms:contributor ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM());
		if (version.isAtLeast("4.0.1"))
			xml.append(" ism:pocType=\"DoD-Dist-B\"");
		xml.append(" ism:classification=\"U\" ism:ownerProducer=\"USA\">\n\t<ddms:").append(
			Organization.getName(version)).append(">\n");
		xml.append("\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t</ddms:").append(Organization.getName(version)).append(">\n</ddms:contributor>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Contributor.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Contributor.getName(version), null);
			element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, OrganizationTest.getFixture(), null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ismPrefix = PropertyReader.getPrefix("ism");

			// Missing entity
			Element element = Util.buildDDMSElement(Contributor.getName(version), null);
			getInstance("entity is required.", element);

			if (version.isAtLeast("4.0.1")) {
				// Invalid pocType
				element = Util.buildDDMSElement(Contributor.getName(version), null);
				element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
				Util.addAttribute(element, ismPrefix, "pocType", version.getIsmNamespace(), "Unknown");
				getInstance("Unknown is not a valid enumeration token", element);

				// Partial Invalid pocType
				element = Util.buildDDMSElement(Contributor.getName(version), null);
				element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
				Util.addAttribute(element, ismPrefix, "pocType", version.getIsmNamespace(), "DoD-Dist-B Unknown");
				getInstance("Unknown is not a valid enumeration token", element);
			}
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing entity
			getInstance("entity is required.", (IRoleEntity) null, null);

			if (version.isAtLeast("4.0.1")) {
				// Invalid pocType
				getInstance("Unknown is not a valid enumeration token", OrganizationTest.getFixture(),
					Util.getXsListAsList("Unknown"));

				// Partial Invalid pocType
				getInstance("Unknown is not a valid enumeration token", OrganizationTest.getFixture(),
					Util.getXsListAsList("DoD-Dist-B Unknown"));
			}
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Contributor component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Contributor dataComponent = getInstance(SUCCESS, OrganizationTest.getFixture(),
				RoleEntityTest.getPocTypes());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Contributor dataComponent = getInstance(SUCCESS, new Service(Util.getXsListAsList("DISA PEO-GES"),
				Util.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, OrganizationTest.getFixture(), RoleEntityTest.getPocTypes());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, OrganizationTest.getFixture(), RoleEntityTest.getPocTypes());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = new Contributor(OrganizationTest.getFixture(), null,
				SecurityAttributesTest.getFixture());
			assertEquals(SecurityAttributesTest.getFixture(), component.getSecurityAttributes());
		}
	}

	public void testWrongVersionPocType() {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Contributor(OrganizationTest.getFixture(), Util.getXsListAsList("DoD-Dist-B"),
				SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "This component cannot have a pocType");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Contributor component = getInstance(SUCCESS, getValidElement(sVersion));
			Contributor.Builder builder = new Contributor.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Contributor.Builder builder = new Contributor.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setPocTypes(Util.getXsListAsList("DoD-Dist-B"));
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Contributor.Builder builder = new Contributor.Builder();
			builder.setEntityType(Organization.getName(version));
			builder.getOrganization().setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "At least 1 name element must");
			}
			builder.getOrganization().setNames(Util.getXsListAsList("DISA"));
			builder.commit();
		}
	}
}
