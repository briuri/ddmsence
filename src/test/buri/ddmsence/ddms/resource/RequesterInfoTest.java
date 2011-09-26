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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>
 * Tests related to ddms:requesterInfo elements
 * </p>
 * 
 * <p>
 * Because a ddms:requesterInfo is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves.
 * </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RequesterInfoTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public RequesterInfoTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @param useOrg true to put an organization in, false for a person
	 */
	public static Element getFixtureElement(boolean useOrg) {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildDDMSElement(RequesterInfo.getName(version), null);
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
			element.appendChild(useOrg ? OrganizationTest.getFixture().getXOMElementCopy() : PersonTest.getFixture()
				.getXOMElementCopy());
			SecurityAttributesTest.getFixture().addTo(element);
			return (element);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<RequesterInfo> getFixtureList() {
		try {
			List<RequesterInfo> list = new ArrayList<RequesterInfo>();
			list.add(new RequesterInfo(RequesterInfoTest.getFixtureElement(true)));
			return (list);
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
	private RequesterInfo getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		RequesterInfo component = null;
		try {
			component = new RequesterInfo(element);
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
	 * @param entity the person or organization in this role
	 * @param org the organization
	 */
	private RequesterInfo getInstance(String message, IRoleEntity entity) {
		boolean expectFailure = !Util.isEmpty(message);
		RequesterInfo component = null;
		try {
			component = new RequesterInfo(entity, SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "requesterInfo.entityType", "organization"));
		text.append(buildOutput(isHTML, "requesterInfo.name", "DISA"));
		text.append(buildOutput(isHTML, "requesterInfo.classification", "U"));
		text.append(buildOutput(isHTML, "requesterInfo.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:requesterInfo ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization>");
		xml.append("</ddms:requesterInfo>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement(true)), DEFAULT_DDMS_PREFIX, RequesterInfo
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields, organization
			getInstance(SUCCESS, getFixtureElement(true));

			// All fields, person
			getInstance(SUCCESS, getFixtureElement(false));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields, organization
			getInstance(SUCCESS, OrganizationTest.getFixture());

			// All fields, person
			getInstance(SUCCESS, PersonTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing entity
			Element element = Util.buildDDMSElement(RequesterInfo.getName(version), null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("entity is required.", element);

			// Missing security attributes
			element = Util.buildDDMSElement(RequesterInfo.getName(version), null);
			element.appendChild(OrganizationTest.getFixture().getXOMElementCopy());
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing entity
			getInstance("entity is required.", (IRoleEntity) null);

			// Wrong entity
			getInstance("The entity must be a person or an organization.", new Service(Util.getXsListAsList("Service"), null, null));

			// Missing security attributes
			try {
				new RequesterInfo(OrganizationTest.getFixture(), null);
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
			RequesterInfo component = getInstance(SUCCESS, getFixtureElement(true));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RequesterInfo elementComponent = getInstance(SUCCESS, getFixtureElement(true));
			RequesterInfo dataComponent = getInstance(SUCCESS, OrganizationTest.getFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RequesterInfo elementComponent = getInstance(SUCCESS, getFixtureElement(true));
			RequesterInfo dataComponent = getInstance(SUCCESS, PersonTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RequesterInfo elementComponent = getInstance(SUCCESS, getFixtureElement(true));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RequesterInfo component = getInstance(SUCCESS, getFixtureElement(true));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, OrganizationTest.getFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RequesterInfo component = getInstance(SUCCESS, getFixtureElement(true));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, OrganizationTest.getFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			new RequesterInfo(OrganizationTest.getFixture(), SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The requesterInfo element cannot be used");
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Equality after Building, organization
			RequesterInfo component = getInstance(SUCCESS, getFixtureElement(true));
			RequesterInfo.Builder builder = new RequesterInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Equality after Building, person
			component = getInstance(SUCCESS, getFixtureElement(false));
			builder = new RequesterInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RequesterInfo.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new RequesterInfo.Builder();
			builder.getPerson().setNames(Util.getXsListAsList("Brian"));
			builder.getPerson().setSurname("Uri");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.commit();
		}
	}
}
