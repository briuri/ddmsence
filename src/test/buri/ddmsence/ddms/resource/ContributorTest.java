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
package buri.ddmsence.ddms.resource;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.RoleEntityTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:contributor elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ContributorTest extends AbstractComponentTestCase {

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
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Contributor testConstructor(boolean expectFailure, Element element) {
		Contributor component = null;
		try {
			SecurityAttributesTest.getFixture().addTo(element);
			component = new Contributor(element);
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
	 * @param entity the producer entity
	 * @param pocType the POCType (DDMS 4.0 or later)
	 */
	private Contributor testConstructor(boolean expectFailure, IRoleEntity entity, String pocType) {
		Contributor component = null;
		try {
			component = new Contributor(entity, pocType, SecurityAttributesTest.getFixture());
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(OrganizationTest.getFixture().getOutput(isHTML, "contributor."));
		if (version.isAtLeast("4.0"))
			text.append(buildOutput(isHTML, "contributor.POCType", "ICD-710"));
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
		if (version.isAtLeast("4.0"))
			xml.append(" ddms:POCType=\"ICD-710\"");
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n\t<ddms:").append(
			Organization.getName(version)).append(">\n");
		xml.append("\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t</ddms:").append(Organization.getName(version)).append(">\n</ddms:contributor>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Contributor.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(Contributor.getName(version), null);
			element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, OrganizationTest.getFixture(), null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing entity
			Element element = Util.buildDDMSElement(Contributor.getName(version), null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing entity		
			testConstructor(WILL_FAIL, (IRoleEntity) null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Contributor dataComponent = testConstructor(WILL_SUCCEED, OrganizationTest.getFixture(), RoleEntityTest.getPOCType());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Contributor dataComponent = testConstructor(WILL_SUCCEED, new Service(Util.getXsListAsList("DISA PEO-GES"),
				Util.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, OrganizationTest.getFixture(), RoleEntityTest.getPOCType());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, OrganizationTest.getFixture(), RoleEntityTest.getPOCType());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = new Contributor(OrganizationTest.getFixture(), null, SecurityAttributesTest.getFixture());
			assertEquals(SecurityAttributesTest.getFixture(), component.getSecurityAttributes());
		}
	}

	public void testPOCTypeWrongVersion() {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Contributor(OrganizationTest.getFixture(), "ICD-710", SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Contributor.Builder builder = new Contributor.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Contributor.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Contributor.Builder();
			builder.setEntityType(Organization.getName(version));
			builder.getOrganization().setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
